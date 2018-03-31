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

class NotRunning extends LoopState {

    private LoopMode mode;

    public NotRunning(GameLoop context, LoopMode m) {
        super(context);
        mode = m;
    }

    @Override
    public void enter() {
        getContext().setRunning(false);
    }
    
    @Override
    public void run() {
        getContext().setRunning(true);
        getContext().switchState(getContext().getState(mode));
    }

    @Override
    public void setMode(LoopMode m) {
        mode = m;
    }

    @Override
    public LoopMode getMode() {
        return mode;
    }
}