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

import java.util.ArrayDeque;
import java.util.Deque;

class InputEvent {

	public enum Type {
		KEY_DOWN, 
		KEY_UP, 
		MOUSE_DOWN, 
		MOUSE_UP, 
		MOUSE_MOVE, 
		MOUSE_SCROLL, 
		MOUSE_WHEEL
	};
    
    private static Deque<InputEvent> pool = new ArrayDeque<>();
    public Type type;
    public boolean pressed;
    public int keycode;
    public int button;
    public int x;
    public int y;
	public double rotation;
    public char character;
    
    public static InputEvent obtainEvent() {
        InputEvent result;
        if (!pool.isEmpty()) {
            result = pool.removeLast();
        } else {
            result = new InputEvent();
        }
        return result;
    }
    
    private InputEvent() {
        // intentionally left empty
    }
    
    public void free() {
        assert pool.contains(this) == false : "input event already freed";
        pool.addLast(this);
    }
}