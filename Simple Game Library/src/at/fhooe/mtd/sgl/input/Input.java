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

public interface Input {

	public static final int DEFAULT_PRIORITY = 0;
	
	/**
	 * Adds the specified input listener to receive input events.
	 * 
	 * @param l
	 *            the listener to be added
	 */
    public void addInputListener(InputListener l);
    
	/**
	 * Adds the specified input listener to receive input events. The input
	 * listener will be receive input events according to its priority. Listener
	 * with a lower priority number will receive input events first.
	 * 
	 * @param l
	 *            the listener to be added
	 * @param priority
	 *            the priority of the listener
	 */
    public void addInputListener(InputListener l, int priority);
    
	/**
	 * Removes the specified input listener so that it no longer receives input
	 * events.
	 * 
	 * @param l
	 *            the listener to be removed
	 */
    public void removeInputListener(InputListener l);
    
	/**
	 * Polls the state of the specified key.
	 * 
	 * @param keycode
	 *            the key code of the key's state to be polled
	 * @return {@code true} if the key is pressed, {@code false} otherwise
	 */
    public boolean isKeyPressed(int keycode);

	/**
	 * Polls the state of the specified mouse button.
	 * 
	 * @param button
	 *            the number of the button which state should be polled
	 * @return {@code true} if the button is pressed, {@code false} otherwise
	 */
    public boolean isMouseButtonPressed(int button);
    
	/**
	 * Returns the x-coordinate of the current mouse position.
	 * 
	 * @return the x-coordinate in pixels
	 */
    public int getMouseX();
    
	/**
	 * Returns the y-coordinate of the current mouse position.
	 * 
	 * @return the y-coordinate in pixels
	 */
    public int getMouseY();    
}
