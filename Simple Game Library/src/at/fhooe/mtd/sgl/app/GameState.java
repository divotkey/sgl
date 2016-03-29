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
package at.fhooe.mtd.sgl.app;

public class GameState {

    private Game context;
    
    public GameState(Game context) {
        this.context = context;
    }
    
    public void enter() { }
    
    public void exit() { }
    
    public void resize(int width, int height) { }
    
    public void update(double dt) {}
    
    public final Game getContext() {
        return context;
    }

}
