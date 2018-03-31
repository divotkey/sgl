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
package at.fhooe.mtd.sgl.graphics;

public enum Alignment {
    
    CENTER(0b0000), LEFT(0b0001), RIGHT(0b0010), TOP(0b0100), BOTTOM(0b1000),
    
    TOP_LEFT(TOP.getCode() | LEFT.getCode()),
    TOP_RIGHT(TOP.getCode() | RIGHT.getCode()),
    BOTTOM_LEFT(BOTTOM.getCode() | LEFT.getCode()), 
    BOTTOM_RIGHT(BOTTOM.getCode() | RIGHT.getCode());
    
    private int code;

    private Alignment(int code) {
        this.code = code;
    }
    
    public int getCode() {
        return code;
    }
}
