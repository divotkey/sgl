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
package at.fhooe.mtd.sgl.app.loop;

import at.fhooe.mtd.sgl.app.Application.LoopMode;


class NoWait extends LoopState {
    
    /** Nanoseconds multiplied by this constant will be seconds. */
    private static final double NANO_TO_SECOND = 1.0 / 1000000000;
    
    private long last;
    
    public NoWait(GameLoop context) {
        super(context);
    }
    
    @Override
    public void enter() {
        last = System.nanoTime();
        running = true;
    }
    
    @Override
    public void run() {
        while (running) {
            deltaTime = (-last + (last = System.nanoTime())) * NANO_TO_SECOND;
            getContext().getUpdatable().update(deltaTime);            
        }
    }

    @Override
    public void stop() {
        running = false;
        getContext().setRunning(false);
    }

    @Override
    public LoopMode getMode() {
        return LoopMode.NO_WAIT;
    }
}