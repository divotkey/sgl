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

import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;

import javax.swing.TransferHandler;

class ClosedScreen extends ScreenState {

    private boolean fullScreen;

    public ClosedScreen(Screen context) {
        super(context);
    }
    
    @Override
    public void open(DisplayMode mode) {
        if (fullScreen) {
            getContext().switchState(
                    new FullState(getContext(), mode));
        } else {
            getContext().switchState(
                    new WindowedState(getContext(), mode));
        }
    }

    @Override
    public void close() {
        throw new IllegalStateException("screen already closed");
    }

    @Override
    public void updateVsyncState() {
        // ignore
    }

    @Override
    public Graphics2D beginUpdate() {
        throw new IllegalStateException("screen not opened");
    }

    
    @Override
    public void endUpdate() {
        throw new IllegalStateException("screen not opened");
    }

    @Override
    public void addKeyListener(KeyListener l) {
        // ignore
    }

    @Override
    public void removeKeyListener(KeyListener l) {
        // ignore
    }
    
    @Override
    public void addMouseListener(MouseListener l) {
        // ignore
    }

    @Override
    public void removeMouseListener(MouseListener l) {
        // ignore
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener l) {
        // ignore
    }

    @Override
    public void removeMouseMotionListener(MouseMotionListener l) {
        // ignore
    }    

	@Override
	public void addMouseWheelListener(MouseWheelListener l) {
        // ignore
	}

	@Override
	public void removeMouseWheelListener(MouseWheelListener l) {
        // ignore
	}
    
    @Override
    public void setFullScreen(boolean value) {
        fullScreen = value;
    }

    @Override
    public boolean isFullscreen() {
        return fullScreen;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setShowCursor(boolean value) {
        // ignore
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        return null;
    }

	@Override
	public FontRenderContext getFontMetrics() {
		return null;
	}
    
    @Override
    public void setTransferHandler(TransferHandler newHandler) {
        // ignore
    }

    @Override
    public Point getMousePosition(Point result) {
        result.x = 0;
        result.y = 0;
        return result;
    }

	@Override
	public void convertPointToScreen(Point p) {
		// ignore
	}

	@Override
	public Point getLocationOnScreen() {
		return null;
	}

	@Override
	public void setTitle(String title) {
		// ignore
	}
    
}