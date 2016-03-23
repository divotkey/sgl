/*******************************************************************************
 * Copyright (c) 2016 Roman Divotkey, Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *  
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *  
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/

package at.fhooe.mtd.igp.sgl.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard implements KeyListener {

    public static int MAX_KEYCODE = 1024;
    
    private boolean keys[] = new boolean[MAX_KEYCODE];
    private List<InputEvent> events = new ArrayList<>();
    private List<InputListener> listeners = new ArrayList<>();
    
    public Keyboard() {
        // intentionally left empty
    }
    
    private void fireKeyDown(int keycode) {
        for (InputListener l : listeners) {
            if (l.keyDown(keycode) )
                break;
        }
    }
    
    private void fireKeyUp(int keycode) {
        for (InputListener l : listeners) {
            if (l.keyUp(keycode) )
                break;
        }
    }
        
    public synchronized boolean isPressed(int keycode)
            throws IndexOutOfBoundsException {
        return keys[keycode];
    }
    
    public void addInputListener(InputListener l) {
        listeners.add(l);
    }
    
    public void removeInputListener(InputListener l) {
        listeners.remove(l);
    }
        
    public synchronized void update() {
        for (InputEvent event : events) {
            switch (event.type) {
            case KEY_DOWN:
                fireKeyDown(event.keycode);
                break;
            case KEY_UP:
                fireKeyUp(event.keycode);
                break;
            default:
                // ignore
            }
            event.free();
        }
        events.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        e.consume();
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        if (e.getKeyCode() < MAX_KEYCODE) {
            if (keys[e.getKeyCode()])
                return;
            keys[e.getKeyCode()] = true;
        }
        
        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.KEY_DOWN;
        event.keycode = e.getKeyCode();
        events.add(event);
        
        
        e.consume();
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        if (e.getKeyCode() < MAX_KEYCODE) {
            keys[e.getKeyCode()] = false;
        }

        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.KEY_UP;
        event.keycode = e.getKeyCode();
        events.add(event);
        
        e.consume();
    }
}