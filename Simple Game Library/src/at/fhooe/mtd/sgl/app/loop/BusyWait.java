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

class BusyWait extends LoopState {
    
    /** Nanoseconds multiplied by this constant will be seconds. */
    private static final double NANO_TO_SECOND = 1.0 / 1000000000;

    /** Seconds multiplied by this constant will be nanoseconds. */
    private static final long SECOND_TO_NANO = 1000000000;
    
    private long last;

    private long targetNs;
    
    public BusyWait(GameLoop context) {
        super(context);
    }
    
    @Override
    public void enter() {
        last = System.nanoTime();
        targetNs = (long) (1.0 / getContext().getUps() * SECOND_TO_NANO);
        running = true;
    }
    
    @Override
    public void run() {
        while (running) {
            long t1 = System.nanoTime();
            deltaTime = (-last + (last = t1)) * NANO_TO_SECOND;
            getContext().getUpdatable().update(deltaTime);
            
            long t2 = System.nanoTime();
            long toWait = targetNs - (t2 - t1);
            while (System.nanoTime() - t2 < toWait) {};
        }
    }


    @Override
    public LoopMode getMode() {
        return LoopMode.BUSY_WAIT;
    }
}