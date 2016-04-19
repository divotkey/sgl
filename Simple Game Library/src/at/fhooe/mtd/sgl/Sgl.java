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
package at.fhooe.mtd.sgl;

import at.fhooe.mtd.sgl.app.Application;
import at.fhooe.mtd.sgl.graphics.Graphics;
import at.fhooe.mtd.sgl.input.Input;

/**
 * This class provides global access to main parts of SGL. The references
 * provided here are initialized and maintained by the active application e.g.
 * {@code Java2dApplication}.
 */
public class Sgl {

    /** Reference to the application interface. */
    public static Application app;
    
    /** Reference to the graphics interface. */
    public static Graphics graphics;
    
    /** Reference to the input interface. */
    public static Input input;
    
}
