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

import java.util.ArrayDeque;
import java.util.Deque;

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
public class Game<T extends Game<?>> implements ApplicationListener {

    /** The currently active state. */
    private GameState<T> state;
    
    /** Flag determining if an update cycle is in progress.*/
    private boolean updating = false;
    
    /** Queue with pending commands. */
    private Deque<Runnable> commands = new ArrayDeque<>();
    
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
    public Game(GameState<T> gs) {
        state = gs;
        if (state != null) {
            state.enter();
            // resize will be called by the ApplicationListener interface
        }
    }

    /**
     * Switches to the specified state. The current state will be exited and the
     * new stated will be entered. It is safe to pass {@code null} as argument
     * for the new state. The state's {@code resize} method will be called after
     * the new state has been entered.
     * 
     * @param gs
     *            the new game state
     */
    public final void switchState(GameState<T> gs) {
        switchState(gs, true);
    }
    
    /**
     * Switches to the specified state. The current state will be exited and the
     * new stated will be entered. It is safe to pass {@code null} as argument
     * for the new state.
     * 
     * @param gs
     *            the new game state
     * @param callResize
     *            if set to {@code false} a call to the state's resize method
     *            will be omitted
     */
    public final void switchState(GameState<T> gs, boolean callResize) {
    	if (updating) {
    		// no state changes during update -> queue command
    		commands.add(() -> switchState(gs, callResize));
    		return;
    	}
    	
    	// exit current state
        if (state != null) {
            state.exit();
        }

        // switch to new state
        state = gs;
        
        if (state != null) {
            state.enter();
            if (callResize) {
                state.resize(Sgl.graphics.getWidth(), Sgl.graphics.getHeight());
            }
        }        
    }
    
    /**
     * Returns the current state.
     * 
     * @return the currently active state or {@code null} if the application has
     *         no state at the moment
     */
    public final GameState<T> getState() {
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
    	// update state (if there is one)
    	if (state != null) {
        	updating = true;
            state.update(dt);
            updating = false;
    	}
        
        // execute pending commands
        while (!commands.isEmpty()) {
        	commands.remove().run();
        }
    }

    @Override
    public void resize(int width, int height) {
        if (state != null)
            state.resize(width, height);
    }

}
