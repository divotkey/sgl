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

import java.awt.DisplayMode;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import at.fhooe.mtd.sgl.app.Application.LoopMode;
import at.fhooe.mtd.sgl.graphics.GfxConfigurator;
import at.fhooe.mtd.sgl.graphics.Graphics.Quality;

/**
 * This class holds configuration data for Java2dApplications.
 * 
 * @see Java2dApplication
 */
public class Java2dApplicationConfig {

    /** The Java2D display mode used for the application. */
    public DisplayMode displayMode = new DisplayMode(800, 600, 32, 60);
    
    /** Defines if v-sync should be enabled or not. */
    public boolean vsync = true;
    
    /** Defines if the application should attempt to start in full-screen. */
    public boolean fullScreen = false;
    
    /** The title of the application window. */
    public String title = "[UNTITLED]";
    
    /** The target update rate of the application. */
    public double ups = 60;
    
    /** The strategy used to achieve the target update rate. */
    public LoopMode loopMode = LoopMode.NO_WAIT;
    
    /** Defines if the mouse cursor should be visible. */
    public boolean showCursor = true;

    /** Defines the render quality. */
    public Quality graphicsQuality = Quality.Good;

    /** Defines the behavior if the user attempts to close the window. */
    public boolean exitOnClose = true;
    
    /** List icon images used as the application icons. */
    public List<Image> iconImages = new ArrayList<>();
    
    /** Whether to activate the audio system. */
    public boolean enableAudio = true;
    
    
    /**
     * Create a new instance using the selected configuration of the specified
     * GfxConfiguratior.
     * 
     * @param gfxc
     *            GfxConiguration that contains the configuration
     * @return a newly created and initialized Java2dApplicationConfig instance
     */
    public static Java2dApplicationConfig create(GfxConfigurator gfxc) {
        Java2dApplicationConfig config = new Java2dApplicationConfig();
        config.displayMode = gfxc.getDisplayMode();
        config.vsync = gfxc.isVSync();
        config.fullScreen = gfxc.isFullScreen();
        config.loopMode = gfxc.getLoopMode();
        config.graphicsQuality = gfxc.getGraphicsQuality();
        
        return config;
    }
}