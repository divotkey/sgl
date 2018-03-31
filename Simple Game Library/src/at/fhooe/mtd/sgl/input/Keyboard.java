/*******************************************************************************
 * Copyright (c) 2016 - 2018 Roman Divotkey,
 * Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *   
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *    
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/

package at.fhooe.mtd.sgl.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class Keyboard extends InputDevice implements KeyListener {

	public static int MAX_KEYCODE = 1024;
    
    private boolean keys[] = new boolean[MAX_KEYCODE];
    private List<InputEvent> events = new ArrayList<>();

        
    public Keyboard() {
        // intentionally left empty
    }
    
        
    public synchronized boolean isPressed(int keycode)
            throws IndexOutOfBoundsException {
        if (keycode >= MAX_KEYCODE) {
            return false;
        }
        return keys[keycode];
    }    
        
    public synchronized void update() {
        for (InputEvent event : events) {
            switch (event.type) {
            case KEY_DOWN:
                fireKeyDown(event.keycode, event.character);
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
        event.character = e.getKeyChar();
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