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
import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

class WindowedState extends ScreenState {

    private JFrame frame;
    private Canvas canvas;
    private Graphics2D g2d;
    private BufferStrategy bufStrat;
    private DisplayMode mode;
    
    public WindowedState(Screen context, DisplayMode m) {
        super(context);
        mode = m;
    }
        
    @Override
    public void enter() {
        // initialize frame
        frame = new JFrame(getContext().getTitle());
        frame.setResizable(false);
        frame.setIgnoreRepaint(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        // initialize canvas
        canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(mode.getWidth(), mode.getHeight()));
        frame.add(canvas);
        frame.pack();
        
        // initialize listeners
        for (KeyListener l : getContext().getKeyListeners()) {
            canvas.addKeyListener(l);
        }
        
        for (MouseListener l : getContext().getMouseListeners()) {
            canvas.addMouseListener(l);
        }

        for (MouseMotionListener l : getContext().getMouseMotionListeners()) {
            canvas.addMouseMotionListener(l);
        }

        for (MouseWheelListener l : getContext().getMouseWheelListeners()) {
            canvas.addMouseWheelListener(l);
        }
        
        try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					initialize();
				}
			});
            
        } catch (InvocationTargetException | InterruptedException e) {
            throw new RuntimeException("unable to open window", e);
        }

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                getContext().closing();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                getContext().iconified();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                getContext().deiconified();
            }
            
        });
        
        canvas.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                getContext().resized(canvas.getWidth(), canvas.getHeight());
            }
        });
    }
    
    private void initialize() {
        frame.setVisible(true);
        
        // make sure the canvas gets all input
        canvas.requestFocusInWindow();
        
        // initialize buffer strategy
        initBufferStrategy(canvas);    
        frame.pack();
        
        setShowCursor(getContext().isShowCursor());
    }

    private void initBufferStrategy(Canvas canvas) {
        try {
            canvas.createBufferStrategy(2, BufferCapabilitiesBuilder.getInstance()
                    .accelerated(true)
                    .vsync(getContext().isVsyncEnabled())
                    .create());
        } catch (AWTException e) {
            System.err.println("unable to created buffer strategy "
                    + e.getMessage());
            canvas.createBufferStrategy(2);
        }
        
        bufStrat = canvas.getBufferStrategy();
    }
    
    @Override
    public void exit() {
        if (g2d != null) g2d.dispose();
        frame.dispose();
    }

    @Override
    public void open(DisplayMode m) {
        throw new IllegalStateException("screen already open");
    }

    @Override
    public void close() {
        getContext().switchState(new ClosedScreen(getContext()));        
    }

    @Override
    public void updateVsyncState() {
        initBufferStrategy(canvas);
    }

    @Override
    public Graphics2D beginUpdate() {
        assert g2d == null : "call beginUpdate() out of sequence";
        g2d = (Graphics2D) bufStrat.getDrawGraphics();
        g2d.setBackground(getContext().getClearColor());
        g2d.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        return g2d;
    }

    @Override
    public void endUpdate() {
        assert g2d == null : "call to endUpdate() out of sequence";
        g2d.dispose();
        g2d = null;
        bufStrat.show();
        
        // this is recommended especially for Unix-like systems
        Toolkit.getDefaultToolkit().sync();
    }

    @Override
    public void addKeyListener(KeyListener l) {
        canvas.addKeyListener(l);
    }

    @Override
    public void removeKeyListener(KeyListener l) {
        canvas.removeKeyListener(l);
    }

    @Override
    public void addMouseListener(MouseListener l) {
        canvas.addMouseListener(l);
    }

    @Override
    public void removeMouseListener(MouseListener l) {
        canvas.removeMouseListener(l);
    }

    @Override
    public void addMouseMotionListener(MouseMotionListener l) {
        canvas.addMouseMotionListener(l);
    }

    @Override
    public void removeMouseMotionListener(MouseMotionListener l) {
        canvas.removeMouseMotionListener(l);
    }    
    
	@Override
	public void addMouseWheelListener(MouseWheelListener l) {
        canvas.addMouseWheelListener(l);
	}

	@Override
	public void removeMouseWheelListener(MouseWheelListener l) {
        canvas.removeMouseWheelListener(l);
	}
    
    @Override
    public void setFullScreen(boolean value) {
        if (value == false) return;
        getContext().switchState(new FullState(getContext(), mode));
    }

    @Override
    public boolean isFullscreen() {
        return false;
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
    public void setShowCursor(boolean value) {
        if (value) {
            frame.setCursor(Cursor.getDefaultCursor());
        } else {
            frame.setCursor(getBlankCursor());
        }
    }
}