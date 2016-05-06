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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_FRACTIONALMETRICS;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.KEY_RENDERING;
import static java.awt.RenderingHints.KEY_TEXT_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
import static java.awt.RenderingHints.VALUE_FRACTIONALMETRICS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BICUBIC;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
import static java.awt.RenderingHints.VALUE_RENDER_QUALITY;
import static java.awt.RenderingHints.VALUE_RENDER_SPEED;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
import static java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON;

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.TransferHandler;

import at.fhooe.mtd.sgl.graphics.Graphics;
import at.fhooe.mtd.sgl.graphics.GraphicsListener;

public class Screen implements Graphics {

    private ScreenState state = new ClosedScreen(this);
    private String title;
    private boolean vsync;
    private Color clearColor;
    private List<GraphicsListener> gfxListener = new ArrayList<>();
    private List<KeyListener> keyListeners = new ArrayList<>();
    private List<MouseListener> mouseListeners = new ArrayList<>();
    private List<MouseMotionListener> mouseMotionListeners = new ArrayList<>();
    private List<MouseWheelListener> mouseWheelListeners = new ArrayList<>();
    private TransferHandler transferHandler;
    private boolean showCursor;
    private Quality quality = Graphics.Quality.Good;
    private Graphics2D context;
    
    private List<Command> commands = new CopyOnWriteArrayList<>();
    
    public Screen() {
        this("[UNTITLED]");
    }
    
    public Screen(String title) {
        this.title = title;
    }
    
	public void addMouseWheelListener(MouseWheelListener l) {
		mouseWheelListeners.add(l);
		state.addMouseWheelListener(l);
	}

	public void removeMouseWheelListener(MouseWheelListener l) {
		mouseWheelListeners.remove(l);
		state.removeMouseWheelListener(l);
	}
	
    public void addMouseMotionListener(MouseMotionListener l) {
        mouseMotionListeners.add(l);
        state.addMouseMotionListener(l);
    }

    public void removeMouseMotionListener(MouseMotionListener l) {
        mouseMotionListeners.remove(l);
        state.removeMouseMotionListener(l);
    }
    
    public void addMouseListener(MouseListener l) {
        mouseListeners.add(l);
        state.addMouseListener(l);
    }
    
    public void removeMouseListener(MouseListener l) {
        mouseListeners.remove(l);
        state.removeMouseListener(l);
    }
    
    public void addKeyListener(KeyListener l) {
        keyListeners.add(l);
        state.addKeyListener(l);
    }
    
    public void removeKeyListener(KeyListener l) {
        keyListeners.remove(l);
        state.removeKeyListener(l);
    }    
    
    void switchState(ScreenState newState) {
        if (state != null) {
            state.exit();
        }
        
        state = newState;
        if (state != null) {
            state.enter();
        }
    }
    
    public void open(DisplayMode mode) throws IllegalStateException {
        state.open(mode);
    }
        
    public void close() throws IllegalStateException {
        state.close();
    }
        
    public void setVsyncEnabled(boolean value) {
        if (value == vsync) return;
        vsync = value;
        state.updateVsyncState();
    }
    
    public boolean isVsyncEnabled() {
        return vsync;
    }
    
    @Override
    public void setFullScreen(boolean value) {
        state.setFullScreen(value);
    }
    
    @Override
    public boolean isFullScreen() {
        return state.isFullscreen();
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return title;
    }

    @Override
    public void setTransferHandler(TransferHandler newHandler) {
        transferHandler = newHandler;
        state.setTransferHandler(newHandler);
    }
    
    @Override
    public void setClearColor(Color c) {
        clearColor = c;
    }
    
    @Override
    public Color getClearColor() {
        return clearColor;
    }

    List<KeyListener> getKeyListeners() {
        return keyListeners;
    }

    List<MouseListener> getMouseListeners() {
        return mouseListeners;
    }

    List<MouseMotionListener> getMouseMotionListeners() {
        return mouseMotionListeners;
    }
    
    List<MouseWheelListener> getMouseWheelListeners() {
		return mouseWheelListeners;
	}

    TransferHandler getTransferHandler() {
        return transferHandler;
    }
    
    void closing() {
    	commands.add(new ClosingCommand());
    }

    void iconified() {
    	commands.add(new IconifiedCommand());
    }
    
    void deiconified() {
    	commands.add(new DeiconifiedCommand());
    }

    void resized(int width, int height) {
    	commands.add(new ResizeCommand(width, height));
    }
    
    
    /////////////////////////////////////////////////
    /////// Interface graphics
    /////////////////////////////////////////////////
    
    @Override
    public Graphics2D beginUpdate() {
        context = state.beginUpdate();
        setQuality(context);
        return context;
    }
    
    protected void setQuality(Graphics2D g) {
        switch (quality) {
        case Best:
            g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
            g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
            g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON);
            break;
        case Fast:
            g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_OFF);
            g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);
            g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_SPEED);
            g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_OFF);
            break;
        case Good:
            g.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            g.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
            g.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(KEY_FRACTIONALMETRICS, VALUE_FRACTIONALMETRICS_ON);
            break;
        }
    }

    @Override
    public void endUpdate() {
        state.endUpdate();
        context = null;
        
        // execute pending commands
        for (Command cmd : commands) {
        	cmd.execute();
        }
        commands.clear();
    }
    
    @Override
    public int getWidth() {
        return state.getWidth();
    }

    @Override
    public int getHeight() {
        return state.getHeight();
    }
    
    @Override
    public double getAspectRatio() {
        return (double) getWidth() / (double) getHeight();
    }

    @Override
    public void showCursor(boolean show) {
        showCursor = show;
        state.setShowCursor(show);
    }

    public boolean isShowCursor() {
        return showCursor;
    }

    @Override
    public void setGraphicsQuality(Quality q) {
        quality  = q;
    }

    @Override
    public Quality getGraphicsQuality() {
        return quality;
    }

    @Override
    public Graphics2D getGraphicsContext() {
        return context;
    }

    @Override
    public void addGraphicsListener(GraphicsListener l) {
        assert !gfxListener.contains(l) : "graphics listener already added";
        gfxListener.add(l);
    }

    @Override
    public void removeGraphicsListener(GraphicsListener l) {
        gfxListener.remove(l);
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        return state.getFontMetrics(font);
    }

    /////////////////////////////////////////////////
    /////// Inner Classes
    /////////////////////////////////////////////////
    
    private abstract class Command {
    	abstract void execute();
    }
    
    private class ResizeCommand extends Command {
    	int width, height;
    	
    	ResizeCommand(int w, int h) {
    		width = w;
    		height = h;
    	}
    	
		@Override
		void execute() {
	        for (GraphicsListener l : gfxListener) {
	            l.resized(width, height);
	        }
		}
    }
    
    private class DeiconifiedCommand extends Command {
		@Override
		void execute() {
	        for (GraphicsListener l : gfxListener) {
	        	l.deiconified();
	        }
		}
    }
        
    private class IconifiedCommand extends Command {
		@Override
		void execute() {
	        for (GraphicsListener l : gfxListener) {
	        	l.iconified();
	        }
		}
    }
    
    private class ClosingCommand extends Command {
		@Override
		void execute() {
	        for (GraphicsListener l : gfxListener) {
	        	l.closing();
	        }
		}
    }

    @Override
    public Point getMousePosition(Point result) {
        return state.getMousePosition(result);
    }
    
}