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

public interface Application {

    /**
     * Describes the available modes for executing the main application loop.
     * <p>Currently available modes are</p>
     * <ul>
     * <li>{@link #NO_WAIT}</li>
     * <li>{@link #BUSY_WAIT}</li>
     * <li>{@link #IDLE_WAIT}</li>
     * </ul>
     */
    public enum LoopMode {
        
        /**
         * The main loop will be executed as many times per second as possible
         */
        NO_WAIT, 
        
        /**
         * The main loop will be executed with the configured update rate using
         * busy waiting. This version will consume as much CPU time as
         * {@code NO_WAIT}.
         */
        BUSY_WAIT,
        
        /**
         * The main loop will be executed with the configured update rate using
         * idle waiting. This mode will suspend the executing thread to save CPU
         * time. This mode is less accurate then {@code BUSY_WAIT}.
         */
        IDLE_WAIT
    }
    
    /**
     * Schedules to exit the application. Calling this method is preferred 
     * way to nicely exit an application.
     */
    public void exit();

    /**
     * Returns the current delta time.
     * 
     * @return the delta time in seconds
     */
    public double getDeltaTime();
    
}
