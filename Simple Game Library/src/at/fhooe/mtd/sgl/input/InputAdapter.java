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

/**
 * An abstract adapter class for receiving input events. The methods in this
 * class are empty. This class exists as convenience for creating listener
 * objects.
 */
public abstract class InputAdapter implements InputListener {

    @Override
    public boolean keyDown(int keycode, char ch) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean mouseDown(int x, int y, int button) {
        return false;
    }

    @Override
    public boolean mouseUp(int x, int y, int button) {
        return false;
    }

    @Override
    public boolean mouseMove(int x, int y) {
        return false;
    }

	@Override
	public boolean scrolled(double amount, int ticks, int button) {
        return false;
	}

}