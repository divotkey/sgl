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
package at.fhooe.mtd.sgl.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;

import javax.swing.TransferHandler;

/**
 * This interface encapsulates access to the graphics system.
 */
public interface Graphics {

	/**
	 * Sets the title of the window. This method has no effect in full-screen
	 * mode.
	 * 
	 * @param title
	 *            the title text to be set
	 */
	void setTitle(String title);
	
    /**
     * Polls the current mouse position. The returned location is more
     * up-to-date than the mouse position provided by the input facility.
     * <p>
     * This method uses low the level API to of Java's AWT to query the mouse
     * location in screen coordinates and then uses translates these coordinates
     * in window coordinates.
     * </p>
     * 
     * @param result
     *            the current mouse position in pixels
     * @return a reference to the given result point for method chaining
     */
    public Point getMousePosition(Point result);
    
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
    public Quality getGraphicsQuality();
    
    /**
     * Returns the current screen width in pixels.
     * 
     * @return width in pixels
     */
    public int getWidth();
    
    /**
     * Returns the aspect ratio of the screen dimension.
     * @return aspect ratio
     */
    public double getAspectRatio();
    
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
     * Returns the current graphics context. If this method is called outside a
     * render cycle, that is between a call to {@link #beginUpdate} and
     * {@link #endUpdate}, it will return {@code null}.
     * 
     * @return the current graphics context of the back buffer
     */
    public Graphics2D getGraphicsContext() throws IllegalStateException;

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

    /**
     * Retrieves the font metrics for the specified font.
     * 
     * @param font
     *            the font for which the font metrics should be retrieved
     * @return the font metrics
     */
    public FontMetrics getFontMetrics(Font font);
    
	/**
	 * Retrieves the font render context.
	 * 
	 * @return the font render context
	 */
    public FontRenderContext getFontRenderContext();
    
    /**
     * Sets the transfer handler for the top level window. The transfer handler
     * is used to support e.g. drag and drop operations.
     * 
     * @param newHandler
     *            the new TransferHandler
     */
    public void setTransferHandler(TransferHandler newHandler);

	/**
	 * Enables or disables vertical synchronization.
	 * 
	 * <p>
	 * <strong>Note: </strong> Vertical synchronization, also reported to be
	 * active, may not work on all systems.
	 * </p>
	 * 
	 * @param b
	 *            {@code true} to enable vsync
	 */
	void setVsyncEnabled(boolean b);

	/**
	 * Returns whether vertical synchronization is enabled.
	 * 
	 * <p>
	 * <strong>Note: </strong> Vertical synchronization, also reported to be
	 * active, may not work on all systems.
	 * </p>
	 * 
	 * @return {@code true} if vertical synchronization is enabled.
	 */
	boolean isVsyncEnabled();

}