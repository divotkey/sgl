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
package at.fhooe.mtd.igp.sgl.graphics;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * This interface encapsulates access to the graphics system.
 */
public interface Graphics {

    /**
     * Describes the available the quality levels used for graphics output.
     */
    public enum Quality {Fast, Good, Best}
    
    /**
     * Set the render quality of the graphics system.
     * 
     * @param q
     *            the quality level to be used
     */
    public void setGraphicsQuality(Quality q);
    
    /**
     * Return the currently set render quality.
     * 
     * @return the render quality
     */
    public Quality getRenderQuality();
    
    /**
     * Returns the current screen width in pixels.
     * 
     * @return width in pixels
     */
    public int getWidth();
    
    /**
     * Return the current screen height in pixels.
     * 
     * @return height in pixels
     */
    public int getHeight();
    
    /**
     * Begins the rendering update. The method will return the graphics context
     * of the back buffer.
     * 
     * @return graphics context of the back buffer
     */
    public Graphics2D beginUpdate();

    /**
     * Finishes the rendering of the frame. This method will also exchange back
     * and front buffer.
     */
    public void endUpdate();

    /**
     * Sets the clear color. The clear color will be used to clear the back
     * buffer when {@link #beginUpdate} is called.
     * 
     * @param c
     *            the clear color
     */
    public void setClearColor(Color c);

    /**
     * Return the clear color.
     * 
     * @return the clear color
     */
    public Color getClearColor();

    /**
     * This method enables or disables the mouse cursor.
     * 
     * @param show
     *            {@code true} if the mouse cursor should be displayed
     */
    public void showCursor(boolean show);
    
    /**
     * Switches the graphics system to full-screen mode. If the system is
     * already in full-screen mode, this method does nothing.
     * 
     * <p>
     * <strong>Note:</strong>Some platforms do not support full-screen mode. In
     * this case the system will stay in windowed mode without any further
     * notice.
     * </p>
     * 
     * @param fs
     *            {@code true} if the graphics system should run in full-screen
     *            mode
     */
    public void setFullScreen(boolean fs);
    
    /**
     * Returns {@code true} if the graphics system is running in full-screen
     * mode.
     * 
     * @return {@code true} if in full-screen mode
     */
    public boolean isFullScreen();

    /**
     * Retrieves the current graphics context.
     * 
     * @return the graphics context
     */
    public Graphics2D getGraphicsContext();
    
    /**
     * Adds the specified graphics listener to receive graphics event.
     * 
     * @param l
     *            the graphics listener to be added
     */
    public void addGraphicsListener(GraphicsListener l);
    
    /**
     * Removes the specified graphics listener so that it no longer receives
     * graphics event.
     * 
     * @param l
     *            the graphics listener to be removed
     */
    public void removeGraphicsListener(GraphicsListener l);
    
}