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

/**
 * Interface for input handlers listening to user input events.
 */
public interface InputListener {

	/**
	 * Called on a key-down event. The parameter {@code ch} represents the character
	 * associated with the pressed key according to the current key mapping. Due to
	 * the fact, that not all keys on the keyboard represent a printable character,
	 * this parameter might be {@code null}.
	 * 
	 * @param keycode
	 *            the key code of the pressed key
	 * @param ch
	 *            the character associated with that key
	 * @return {@code true} if the event has been processed
	 */
    public boolean keyDown(int keycode, char ch);
    
    /**
     * Called on a key-up event.
     * 
	 * @param keycode
	 *            the key code of the released key
	 * @return {@code true} if the event has been processed
     */
    public boolean keyUp(int keycode);
    
	/**
	 * Called on when a mouse button is pressed.
	 * 
	 * @param x
	 *            the x-coordinate of mouse cursor screen space
	 * @param y
	 *            the y-coordinate of mouse cursor screen space
	 * @param button
	 *            the numerical identifier of the mouse button
	 * @return {@code true} if the event has been processed
	 */
    public boolean mouseDown(int x, int y, int button);
    
	/**
	 * Called on when a mouse button is released.
	 * 
	 * @param x
	 *            the x-coordinate of mouse cursor screen space
	 * @param y
	 *            the y-coordinate of mouse cursor screen space
	 * @param button
	 *            the numerical identifier of the mouse button
	 * @return {@code true} if the event has been processed
	 */
    public boolean mouseUp(int x, int y, int button);
    
	/**
	 * Called on when a mouse is moved.
	 * 
	 * @param x
	 *            the x-coordinate of mouse cursor screen space
	 * @param y
	 *            the y-coordinate of mouse cursor screen space
	 * @return {@code true} if the event has been processed
	 */
    public boolean mouseMove(int x, int y);
    
	/**
	 * Called when the mouse wheel is moved.
	 * <p>
	 * High resolution movement of the mouse wheel is not supported on all
	 * platforms. The value of the high resolution movement is equal to the number
	 * of click which is offered as integer number but with fractions of movement,
	 * e.g.c, 1.42 click.
	 * </p>
	 * 
	 * <p>
	 * Java running on MacOS environments supports high resolutions mouse wheel
	 * movement.
	 * </p>
	 * 
	 * @param amount
	 *            the high resolution movement of the mouse wheel
	 * @param ticks
	 *            the low precision movement of the mouse wheel
	 * @param button
	 *            the numerical identifier of the mouse button (wheel)
	 * @return {@code true} if the event has been processed
	 */
	public boolean scrolled(double amount, int ticks, int button);
    
}
