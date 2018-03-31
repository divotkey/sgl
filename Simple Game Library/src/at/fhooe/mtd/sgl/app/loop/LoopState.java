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
package at.fhooe.mtd.sgl.app.loop;

import at.fhooe.mtd.sgl.app.Application.LoopMode;

abstract class LoopState {
    
    private GameLoop context;
    protected boolean running;
    protected double deltaTime;
    
    public LoopState(GameLoop context) {
        this.context = context;
    }

    public final GameLoop getContext() {
        return context;
    }
    
    public void enter() { }
    
    public void exit() { }

    public abstract void run();

    public void stop() {
        running = false;
        context.setRunning(false);
    }
    
    public void setMode(LoopMode m) {
        if (getMode() == m) return;
        running = false;
        context.switchState(context.getState(m));
    }
    
    public abstract LoopMode getMode();

    public double getDeltaTime() {
        return deltaTime;
    }
}