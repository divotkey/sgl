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

/**
 * Abstract base class for major game states. Typical major state for games are:
 * <ul>
 * <li>Splash Screen</li>
 * <li>Main Menu</li>
 * <li>Play State</li>
 * <li>Settings Screen</li>
 * <li>Achievements (High Score etc.)</li>
 * <li>Credits</li>
 * <li>etc...</li>
 * </ul>
 *
 * @see Game
 * @see ApplicationListener
 */
public abstract class GameState {

    /** The context of this state. */
    private Game context;
    
    public GameState(Game context) {
        this.context = context;
    }
    
    /**
     * Invoked when this state is entered.
     */
    public void enter() { }

    /**
     * Invoked when this state is exited.
     */
    public void exit() { }

    /**
     * Invoked when the size of the application window is changed. This method
     * is also invoked the application is started.
     * 
     * @param width
     *            the width of the application window in pixels
     * @param height
     *            the height of the application window in pixels
     */
    public void resize(int width, int height) { }
    
    /**
     * Called once within each update cycle.
     * 
     * @param dt
     *            the elapsed time since the last cycle in seconds
     */
    public void update(double dt) {}
    
    /**
     * Return the context of this state.
     * 
     * @return the context
     */
    public final Game getContext() {
        return context;
    }

}
