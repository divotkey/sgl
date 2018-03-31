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

public interface InputListener {

    public boolean keyDown(int keycode, char ch);
    public boolean keyUp(int keycode);
    public boolean mouseDown(int x, int y, int button);
    public boolean mouseUp(int x, int y, int button);
    public boolean mouseMove(int x, int y);
	public boolean scrolled(double amount, int ticks, int button);
    
}
