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

import at.fhooe.mtd.sgl.Sgl;
import at.fhooe.mtd.sgl.app.ApplicationListener;

public class Game implements ApplicationListener {

    private GameState state;
    
    public final void switchState(GameState gs) {
        if (state != null) {
            state.exit();
        }
        
        state = gs;
        
        if (state != null) {
            state.enter();
            state.resize(Sgl.graphics.getWidth(), Sgl.graphics.getHeight());
        }
    }
    
    public final GameState getState() {
        return state;
    }
    
    @Override
    public void create() { }

    @Override
    public void dispose() {
        switchState(null);
    }

    @Override
    public void update(double dt) {
        if (state != null)
            state.update(dt);
    }

    @Override
    public void resize(int width, int height) {
        if (state != null)
            state.resize(width, height);
    }

}
