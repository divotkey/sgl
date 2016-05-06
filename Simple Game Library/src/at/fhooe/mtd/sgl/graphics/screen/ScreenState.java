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
package at.fhooe.mtd.sgl.graphics.screen;

import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.TransferHandler;


abstract class ScreenState {
        
    private Screen context;
    private Cursor noCursor = null;
    
    public ScreenState(Screen context) {
        this.context = context;
    }
    
    protected final Cursor getBlankCursor() {
        if (noCursor == null) {
            noCursor = Toolkit.getDefaultToolkit()
                    .createCustomCursor(new BufferedImage(16,
                    16, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank");
        }
        return noCursor;
    }
    
    protected final Screen getContext() {
        return context;
    }
    
    public void enter() {};
    public void exit() {};
    
    public abstract void open(DisplayMode mode);
    public abstract void close();
    public abstract void updateVsyncState();

    public abstract Graphics2D beginUpdate();
    public abstract void endUpdate();

    public abstract void addKeyListener(KeyListener l);
    public abstract void removeKeyListener(KeyListener l);

    public abstract void setFullScreen(boolean value);
    public abstract boolean isFullscreen();

    public abstract void addMouseListener(MouseListener l);
    public abstract void removeMouseListener(MouseListener l);

    public abstract void addMouseMotionListener(MouseMotionListener l);
    public abstract void removeMouseMotionListener(MouseMotionListener l);

	public abstract void addMouseWheelListener(MouseWheelListener l);
	public abstract void removeMouseWheelListener(MouseWheelListener l);
    
    public abstract int getWidth();
    public abstract int getHeight();

    public abstract void setShowCursor(boolean value);

    public abstract FontMetrics getFontMetrics(Font font);

    public abstract void setTransferHandler(TransferHandler newHandler);

    public abstract Point getMousePosition(Point result);

}