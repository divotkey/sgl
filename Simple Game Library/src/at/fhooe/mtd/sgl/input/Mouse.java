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

package at.fhooe.mtd.sgl.input;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mouse extends InputDevice implements MouseListener,
		MouseMotionListener, MouseWheelListener {

    public static final int MAX_BUTTON = 10;
    
    private boolean[] buttons = new boolean[MAX_BUTTON];
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
            case MOUSE_WHEEL:
            	fireMouseWheel(event.rotation, (int) event.x, event.button);
                break;
                
            default:
                // ignore
            }
            event.free();
        }
        events.clear();
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

	@Override
	public synchronized void mouseWheelMoved(MouseWheelEvent e) {
        InputEvent event = InputEvent.obtainEvent();
        event.type = InputEvent.Type.MOUSE_WHEEL;
        event.button = e.getButton();
        event.x = e.getWheelRotation();
        event.y = e.getClickCount();
        event.rotation = e.getPreciseWheelRotation();
        events.add(event);
        
        e.consume();
	}
}