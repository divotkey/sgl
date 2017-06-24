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

import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

class FullState extends ScreenState implements HierarchyListener {

    private JFrame frame;
    private GraphicsDevice gd;
    private BufferStrategy bufStrat;
    private Graphics2D g2d;
    private boolean frameIsReady;
    private DisplayMode mode;
    
    public FullState(Screen context, DisplayMode m) {
        super(context);
        mode = m;
    }

    @Override
    public void enter() {
        frame = new JFrame(getContext().getTitle());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setUndecorated(true);
        frame.setIgnoreRepaint(true);
        frame.setResizable(false);
        frame.setIconImages(getContext().getApplicationIcons());
        
        GraphicsEnvironment ge = 
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        gd = ge.getDefaultScreenDevice();
        
        if (!gd.isFullScreenSupported()) {
            System.err.print("full-screen not supported");
            getContext().switchState(new WindowedState(getContext(), mode));
            return;
        }

        frameIsReady = false;      
        frame.addHierarchyListener(this);
        gd.setFullScreenWindow(frame);

        if (!setDisplayMode(mode)) {
            getContext().switchState(new WindowedState(getContext(), mode));
            return;
        }
        waitForFrame();
        frame.removeHierarchyListener(this);
                
        registerListeners();
        
        try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					initBufferStrategy();
				}
			});
            
        } catch (InvocationTargetException | InterruptedException e) {
			System.err.println("unable to initialize full-screen: "
					+ e.getMessage());
            e.printStackTrace();
            getContext().switchState(new WindowedState(getContext(), mode));
        }
        
        setShowCursor(getContext().isShowCursor());
    }

	private void registerListeners() {
		for (KeyListener l : getContext().getKeyListeners()) {
            frame.addKeyListener(l);
        }

        for (MouseListener l : getContext().getMouseListeners()) {
            frame.addMouseListener(l);
        }

        for (MouseMotionListener l : getContext().getMouseMotionListeners()) {
            frame.addMouseMotionListener(l);
        }
  
        for (MouseWheelListener l : getContext().getMouseWheelListeners()) {
        	frame.addMouseWheelListener(l);
        }
        
        frame.setTransferHandler(getContext().getTransferHandler());
	}
	
    private void deregisterListeners() {
        for (KeyListener l : getContext().getKeyListeners()) {
            frame.removeKeyListener(l);
        }

        for (MouseListener l : getContext().getMouseListeners()) {
            frame.removeMouseListener(l);
        }

        for (MouseMotionListener l : getContext().getMouseMotionListeners()) {
            frame.removeMouseMotionListener(l);
        }
  
        for (MouseWheelListener l : getContext().getMouseWheelListeners()) {
        	frame.removeMouseWheelListener(l);
        }
    }    
    
    private void waitForFrame() {
        synchronized(frame) {
            while (!frameIsReady) {
                try {
                    frame.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    
    @Override
    public void exit() {
    	deregisterListeners();
        gd.setFullScreenWindow(null);
        frame.dispose();
    }
        
    private void initBufferStrategy() {
        try {
            frame.createBufferStrategy(2, BufferCapabilitiesBuilder.getInstance()
                    .accelerated(true)
                    .vsync(getContext().isVsyncEnabled())
                    .create());
        } catch (AWTException e) {
            System.err.println("unable to created buffer strategy "
                    + e.getMessage());
            frame.createBufferStrategy(2);
        }
        
        bufStrat = frame.getBufferStrategy();
    }
        
    private boolean setDisplayMode(DisplayMode m) {
        if (!gd.isDisplayChangeSupported()) {
            System.err.println("unable to change display mode");
            return false;
        }
        
        try {
            gd.setDisplayMode(m);
        } catch (Exception e) {
            System.err.println("unable to set display mode " + mode + ": "
                    + e.getMessage());
            return false;
        }
        
        return true;
    }
    
    
    @Override
    public void open(DisplayMode m) {
        throw new IllegalStateException("screen already opened");        
    }

    
    @Override
    public void close() {
        getContext().switchState(new ClosedScreen(getContext()));
    }

    @Override
    public void updateVsyncState() {
        initBufferStrategy();
    }

    @Override
    public Graphics2D beginUpdate() {
        assert g2d == null : "call beginUpdate() out of sequence";
        g2d = (Graphics2D) bufStrat.getDrawGraphics();
        g2d.setBackground(getContext().getClearColor());
        g2d.clearRect(0, 0, frame.getWidth(), frame.getHeight());
        
        return g2d;
    }

    @Override
    public void endUpdate() {
        assert g2d != null : "call to endUpdate() out of sequence";
        g2d.dispose();
        g2d = null;
        bufStrat.show();
        
        // this is recommended especially for Unix-like systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void addKeyListener(KeyListener l) {
        frame.addKeyListener(l);
    }

    @Override
    public void removeKeyListener(KeyListener l) {
        frame.removeKeyListener(l);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        frame.addMouseListener(l);
    }    
    
    @Override
    public void removeMouseListener(MouseListener l) {
        frame.removeMouseListener(l);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener l) {
        frame.addMouseMotionListener(l);
    }

    @Override
    public void removeMouseMotionListener(MouseMotionListener l) {
        frame.removeMouseMotionListener(l);
    }    
    
	@Override
	public void addMouseWheelListener(MouseWheelListener l) {
		frame.addMouseWheelListener(l);
	}

	@Override
	public void removeMouseWheelListener(MouseWheelListener l) {
		frame.removeMouseWheelListener(l);
	}
    
    @Override
    public void setFullScreen(boolean value) {
        if (value) return;
        getContext().switchState(new WindowedState(getContext(), mode));
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }
    
    @Override
    public int getWidth() {
        return mode.getWidth();
    }

    @Override
    public int getHeight() {
        return mode.getHeight();
    }
    
    @Override
    public void hierarchyChanged(HierarchyEvent e) {
        if (frame.isShowing()) {
            synchronized(frame) {
                frameIsReady = true;
                frame.notifyAll();
            }
        }
    }
    
    @Override
    public void setShowCursor(boolean value) {
        if (value) {
            frame.setCursor(Cursor.getDefaultCursor());
        } else {
            frame.setCursor(getBlankCursor());
        }
    }

    @Override
    public FontMetrics getFontMetrics(Font font) {
        Graphics2D g = (Graphics2D) frame.getGraphics();
        getContext().setQuality(g);
        FontMetrics fm = g.getFontMetrics(font);
        g.dispose();
        return fm;
    }

	@Override
	public FontRenderContext getFontMetrics() {
        Graphics2D g = (Graphics2D) frame.getGraphics();
        getContext().setQuality(g);
        FontRenderContext frc = g.getFontRenderContext();
        g.dispose();
		return frc;
	}
    
    @Override
    public void setTransferHandler(TransferHandler newHandler) {
        frame.setTransferHandler(newHandler);
    }    
    
    @Override
    public Point getMousePosition(Point result) {
        result = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(result, frame);
        return result;
    }

	@Override
	public void convertPointToScreen(Point p) {
		// points is already in screen coordinates
	}
	
	@Override
	public Point getLocationOnScreen() {
		return frame.getLocationOnScreen();
	}

	@Override
	public void setTitle(String title) {
		frame.setTitle(title);
	}
}