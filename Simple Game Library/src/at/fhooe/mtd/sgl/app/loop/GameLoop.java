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

public class GameLoop {

    private Updatable updatable;
    
    public interface Updatable {
        public void update(double dt);
    }
    
    private LoopState state;
    private boolean running;
    private double ups;
            
    public GameLoop(double ups) {
        setUps(ups);
        switchState(new NotRunning(this, LoopMode.NO_WAIT));
    }

    LoopState getState(LoopMode m) {
        switch (m) {
        case NO_WAIT:
            return new NoWait(this);
        case BUSY_WAIT:
            return new BusyWait(this);
        case IDLE_WAIT:
            return new IdleWait(this);
        default:
            return null;
        }
    }
    
    void switchState(LoopState newState) {
        if (state != null) {
            state.exit();
        }
        
        state = newState;
        if (state != null) {
            state.enter();            
        }
    }

    public void setMode(LoopMode m) {
        state.setMode(m);
    }
    
    public LoopMode getMode() {
        return state.getMode();
    }

    public void setUpdatable(Updatable u) {
        updatable = u;
    }
    
    public Updatable getUpdatable() {
        return updatable;
    }
    
    public void stop() {
        state.stop();
    }
        
    void setRunning(boolean value) {
        running = value;
    }
    
    boolean isRunning() {
        return running;
    }        
    
    public void run() {
        assert state instanceof NotRunning;
        assert running == false;
        do {
            state.run();
        } while (running);
        switchState(new NotRunning(this, getMode()));
    }

    private void setUps(double ups) {
        if (ups <= 0) {
            throw new IllegalArgumentException("ups must be > 0, got " + ups);
        }
        this.ups = ups;
    }
    
    public double getUps() {
        return ups;
    }

    public double getDeltaTime() {
        return state.getDeltaTime();
    }
}