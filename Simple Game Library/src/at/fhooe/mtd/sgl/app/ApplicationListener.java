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
 * Application listener get informed when the state of the application
 * changes or an update is required. This is the main interface that needs
 * to be implemented at by a new application.
 */
public interface ApplicationListener {

    /** 
     * Invoked when the application is created.
     */
    public void create();
    
    /** 
     * Invoked when the application is destroyed.
     */
    public void dispose();
    
    /**
     * Called once each cycle of the application's main loop.
     * 
     * @param dt
     *            the elapsed delta time since the last cycle in seconds
     */
    public void update(double dt);
    
    /**
     * Invoked when the application windows is resized. This method is also
     * called when the application is created after the {@link #create} method
     * has been called.
     * 
     * @param width
     *            the width of the application window in pixels
     * @param height
     *            the height of the application window in pixels
     */
    public void resize(int width, int height);
    
}
