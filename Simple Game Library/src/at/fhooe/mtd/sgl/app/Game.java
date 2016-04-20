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

/**
 * This class together with the abstract class {@code GameState} implement the
 * classic state pattern. It can be used to cover the major states (or screens)
 * of the application.
 * <p>
 * This class implements the {@code ApplicationListener} interface and passes
 * the application events on the current state.
 * </p>
 * 
 * @see GameState
 * @see ApplicationListener
 */
public class Game implements ApplicationListener {

    /** The currently active state. */
    private GameState state;
    
    /**
     * Creates a new instance with no active game state.
     */
    public Game() {
        this(null);
    }
    
    /**
     * Creates a new instance of using the specified state as initial state.
     * 
     * @param gs
     *            the initial game state
     */
    public Game(GameState gs) {
        state = gs;
        if (state != null) {
            state.enter();
            // resize will be called by the ApplicationListener interface
        }
    }

    /**
     * Switches to the specified state. The current state will be exited and the
     * new stated will be entered. It is safe to pass {@code null} as argument
     * for the new state.
     * 
     * @param gs
     *            the new game state
     */
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
    
    /**
     * Returns the current state.
     * 
     * @return the currently active state or {@code null} if the application has
     *         no state at the moment
     */
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
