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
package at.fhooe.mtd.igp.sgl.app;

import at.fhooe.mtd.igp.sgl.Sgl;
import at.fhooe.mtd.igp.sgl.app.loop.GameLoop;
import at.fhooe.mtd.igp.sgl.graphics.GraphicsAdapter;
import at.fhooe.mtd.igp.sgl.graphics.screen.Screen;
import at.fhooe.mtd.igp.sgl.input.Input;
import at.fhooe.mtd.igp.sgl.input.InputListener;
import at.fhooe.mtd.igp.sgl.input.Keyboard;
import at.fhooe.mtd.igp.sgl.input.Mouse;

public class Java2dApplication implements Application, Input {

    private ApplicationListener appListener;
    private Screen screen;
    private GameLoop loop;
    private Keyboard keyboard;
    private Mouse mouse;
    
    public Java2dApplication(Java2dApplicationConfig c, ApplicationListener l) {
        appListener = l;
        
        // initialize screen
        screen = new Screen();
        screen.setTitle(c.title);
        screen.setFullScreen(c.fullScreen);
        screen.setVsyncEnabled(c.vsync);
        screen.showCursor(c.showCursor);
        screen.setGraphicsQuality(c.graphicsQuality);
        screen.addGraphicsListener(new GraphicsAdapter() {
            
            @Override
            public void closing() {
                if (c.exitOnClose) {
                    exit();
                }
            }

            @Override
            public void resized(int width, int height) {
                appListener.resize(width, height);
            }
            
        });

        // initialize input
        mouse = new Mouse();
        keyboard = new Keyboard();
        screen.addKeyListener(keyboard);
        screen.addMouseListener(mouse);
        screen.addMouseMotionListener(mouse);
                
        // initialize game loop
        loop = new GameLoop(c.ups);
        loop.setMode(c.loopMode);
        loop.setUpdatable((double dt) -> { update(dt); });   

        // initialize Sgl global data structure
        Sgl.app = this;
        Sgl.graphics = screen;
        Sgl.input = this;
        
        // run application
        screen.open(c.displayMode);
        appListener.create();
        appListener.resize(screen.getWidth(), screen.getHeight());
        loop.run();
        
        // clean up
        appListener.dispose();
        screen.removeKeyListener(keyboard);
        screen.removeMouseListener(mouse);
        screen.removeMouseMotionListener(mouse);
        screen.close();
        screen = null; keyboard = null; mouse = null; loop = null;
        appListener = null;
    }
    
    private void update(double dt) {
        mouse.update();
        keyboard.update();
        appListener.update(dt);
    }

    /////////////////////////////////////////////////
    /////// Interface Application
    /////////////////////////////////////////////////    
    
    @Override
    public void exit() {
        loop.stop();
    }
    
    /////////////////////////////////////////////////
    /////// Interface Input
    /////////////////////////////////////////////////

    @Override
    public void addInputListener(InputListener l) {
        keyboard.addInputListener(l);
        mouse.addInputListener(l);
    }

    @Override
    public void removeInputListener(InputListener l) {
        keyboard.removeInputListener(l);
        mouse.removeInputListener(l);
    }
    
    @Override
    public boolean isKeyPressed(int keyCode) {
        return keyboard.isPressed(keyCode);
    }

    @Override
    public double getDeltaTime() {
        return loop.getDeltaTime();
    }    
    
    /////////////////////////////////////////////////
    /////// Interface GraphicsListener
    /////////////////////////////////////////////////
    
}