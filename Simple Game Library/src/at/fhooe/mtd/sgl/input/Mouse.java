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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import at.fhooe.mtd.sgl.Sgl;
import at.fhooe.mtd.sgl.graphics.screen.Screen;

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
    private boolean trapped = false;
    private Robot robot;
    
    // Used to get screen coordinates (required for mouse trap)
    private Screen screen;
    private Point pt = new Point();
    
    public Mouse(Screen screen) {
    	this.screen = screen;
        Arrays.fill(buttons, false);
        posX = posY = lastX = lastY = deltaX = deltaY = 0;
        try {
			robot = new Robot();
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
    }
    
	/**
	 * Defines if the mouse should be trapped inside the window.
	 * 
	 * @param b
	 *            {@code true} if the mouse should be trapped
	 */
	public void setTrapped(boolean b) {
		if (trapped == b) return;				
		trapped = b;
		if (trapped) {
			lastX = lastY = deltaX = deltaY = 0;
			centerMouse();
		}
	}
	
	/**
	 * Returns if the mouse is trapped inside the window.
	 * 
	 * @return {@code true} if the mouse is trapped
	 */
	public boolean isTrapped() {
		return trapped;
	}
    
    /**
     * Returns the delta movement in x-direction since last update.
     * 
     * @return the delta movement in x-direction
     */
    public synchronized int getDeltaX() {
        return deltaX;
    }
    
    /**
     * Returns the delta movement in y-direction since last update.
     * 
     * @return the delta movement in y-direction
     */
    public synchronized int getDeltaY() {
        return deltaY;
    }
    
    public synchronized int getPosX() {
        return posX;
    }

    public synchronized int getPosY() {
        return posY;
    }
    
	public synchronized void setPos(int x, int y) {
		posX = x; posY = y;
		deltaX = 0; deltaY = 0;
		setMouse(x, y);
	}
    
    public synchronized boolean isPressed(int button)
            throws IndexOutOfBoundsException {
        return buttons[button];
    }
    
    public synchronized void update() {
    	if (!trapped) {
            deltaX = posX - lastX;
            deltaY = posY - lastY;
            lastX = posX;
            lastY = posY;
    	} else {
    		deltaX = lastX;
    		deltaY = lastY;
    		lastX = lastY = 0;
    	}
    	
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

    private void centerMouse() {
		posX = Sgl.graphics.getWidth() / 2;
		posY = Sgl.graphics.getHeight() / 2;
		setMouse(posX, posY);    	
    }
    
    private void setMouse(int x, int y) {
    	pt = screen.getLocationOnScreen();
		robot.mouseMove(pt.x + x, pt.y + y);
    }
    
    @Override
    public synchronized void mouseMoved(MouseEvent e) {
    	if (trapped) {
    		posX = Sgl.graphics.getWidth() / 2;
    		posY = Sgl.graphics.getHeight() / 2;
    		lastX += e.getX() - posX;
    		lastY += e.getY() - posY;
    		setMouse(posX, posY);
    	} else {
	        posX = e.getX();
	        posY = e.getY();
	
	        InputEvent event = InputEvent.obtainEvent();
	        event.type = InputEvent.Type.MOUSE_MOVE;
	        event.x = e.getX();
	        event.y = e.getY();
	        events.add(event);
    	}        
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