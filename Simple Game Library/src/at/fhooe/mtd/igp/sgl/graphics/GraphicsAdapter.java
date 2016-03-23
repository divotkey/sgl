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
package at.fhooe.mtd.igp.sgl.graphics;

/**
 * An abstract adapter class for receiving graphics events. The methods in this
 * class are empty. This class exists as convenience for creating listener
 * objects.
 */
public abstract class GraphicsAdapter implements GraphicsListener {

    @Override
    public void closing() {
        // intentionally left empty
    }

    @Override
    public void resized(int width, int height) {
        // intentionally left empty
    }

    @Override
    public void iconified() {
        // intentionally left empty
    }

    @Override
    public void deiconified() {
        // intentionally left empty
    }

}
