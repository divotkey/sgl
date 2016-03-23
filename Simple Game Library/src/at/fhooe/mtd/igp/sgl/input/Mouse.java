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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mouse implements MouseListener, MouseMotionListener {

    public static final int MAX_BUTTON = 10;
    private boolean[] buttons = new boolean[MAX_BUTTON];
    private List<InputListener> listeners = new ArrayList<>();
    private List<InputEvent> events = new ArrayList<>();
    private int posX = 0;
    private int posY = 0;
    private int lastX;
    private int lastY;
    private int deltaY = 0;
    private int deltaX = 0;
    
    
    public Mouse() {
        Arrays.fill(buttons, false);
        posX = posY = lastX = lastY = deltaX = deltaY = 0;
    }

    public void addInputListener(InputListener l) {
        listeners.add(l);
    }
    
    public void removeInputListener(InputListener l) {
        listeners.remove(l);
    }
    
    public int getDeltaX() {
        return deltaX;
    }
    
    public int getDeltaY() {
        return deltaY;
    }
    
    public synchronized int getPosX() {
        return posX;
    }

    public synchronized int getPosY() {
        return posY;
    }
    
    public synchronized boolean isPressed(int button)
            throws IndexOutOfBoundsException {
        return buttons[button];
    }
    
    public synchronized void update() {
        deltaX = posX - lastX;
        deltaY = posY - lastY;
        lastX = posX;
        lastY = posY;
        
        for (InputEvent event : events) {
            switch (event.type) {
            case MOUSE_DOWN:
                fireMouseDown(event.x, event.y, event.button);
                break;
            case MOUSE_UP:
                fireMouseUp(event.x, event.y, event.button);
                break;
            case MOUSE_MOVE:
                fireMouseMove(event.x, event.y);
                break;
                
            default:
                // ignore
            }
            event.free();
        }
        events.clear();
    }    

    private void fireMouseMove(int x, int y) {
        for (InputListener l : listeners) {
            if (l.mouseMove(x, y) )
                break;
        }
    }
    
    
    private void fireMouseDown(int x, int y, int button) {
        for (InputListener l : listeners) {
            if (l.mouseDown(x, y, button) )
                break;
        }
    }

    private void fireMouseUp(int x, int y, int button) {
        for (InputListener l : listeners) {
            if (l.mouseUp(x, y, button) )
                break;
        }
    }
    
    
    /////////////////////////////////////////////////
    /////// Interface MouseListener
    /////////////////////////////////////////////////
    
    @Override
    public void mouseClicked(MouseEvent e) {
        e.consume();
    }

    @Override
    public synchronized void mousePressed(MouseEvent e) {
        if (e.getButton() < MAX_BUTTON) {
            buttons[e.getButton()] = true;
        }
        
        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.MOUSE_DOWN;
        event.button = e.getButton(); 
        event.x = e.getX();
        event.y = e.getY();
        events.add(event);        
        
        e.consume();
    }

    @Override
    public synchronized void mouseReleased(MouseEvent e) {
        if (e.getButton() < MAX_BUTTON) {
            buttons[e.getButton()] = false;
        }
        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.MOUSE_UP;
        event.button = e.getButton(); 
        event.x = e.getX();
        event.y = e.getY();
        events.add(event);
        e.consume();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        e.consume();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        e.consume();
    }

    /////////////////////////////////////////////////
    /////// Interface MouseMotionListener
    /////////////////////////////////////////////////
    
    @Override
    public synchronized void mouseDragged(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();

        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.MOUSE_MOVE;
        event.x = e.getX();
        event.y = e.getY();
        events.add(event);
        
        e.consume();
    }

    @Override
    public synchronized void mouseMoved(MouseEvent e) {
        posX = e.getX();
        posY = e.getY();
        
        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.MOUSE_MOVE;
        event.x = e.getX();
        event.y = e.getY();
        events.add(event);
        
        e.consume();
    }
}