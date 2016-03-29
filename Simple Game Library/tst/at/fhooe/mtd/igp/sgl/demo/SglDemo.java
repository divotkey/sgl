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
package at.fhooe.mtd.igp.sgl.demo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

import at.fhooe.mtd.sgl.Sgl;
import at.fhooe.mtd.sgl.app.ApplicationListener;
import at.fhooe.mtd.sgl.app.Java2dApplication;
import at.fhooe.mtd.sgl.app.Java2dApplicationConfig;
import at.fhooe.mtd.sgl.graphics.Alignment;
import at.fhooe.mtd.sgl.graphics.GfxConfigurator;
import at.fhooe.mtd.sgl.graphics.TextRenderer;

public class SglDemo implements ApplicationListener {

    private static final String APP_TITLE = "SGL Demo";
    private static final String VERSION = "1.0";

    public static void main(String[] args) {
        GfxConfigurator gfxc = new GfxConfigurator();
        gfxc.runDialog();
        
        Java2dApplicationConfig config = new Java2dApplicationConfig();
        config.displayMode = gfxc.getDisplayMode();
        config.vsync = gfxc.isVSync();
        config.fullScreen = gfxc.isFullScreen();
        config.loopMode = gfxc.getLoopMode();
        config.graphicsQuality = gfxc.getGraphicsQuality();
        config.title = APP_TITLE + " - v" + VERSION;
                
        new Java2dApplication(config, new SglDemo());
    }

    private TextRenderer txtRenderer;

    @Override
    public void create() {
        Sgl.graphics.setClearColor(Color.BLACK);
        Sgl.graphics.showCursor(true);
        txtRenderer = new TextRenderer();
        txtRenderer.setFont(new Font(Font.MONOSPACED, Font.BOLD, 32));
        txtRenderer.setColor(Color.WHITE);
    }

    @Override
    public void dispose() { 
        txtRenderer = null;
    }

    @Override
    public void update(double dt) {
        Graphics2D g = Sgl.graphics.beginUpdate();
        txtRenderer.reset();
        txtRenderer.render(g, "Hello world");
        Sgl.graphics.endUpdate();
        
        if (Sgl.input.isKeyPressed(KeyEvent.VK_ESCAPE)) {
            Sgl.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {
        txtRenderer.setAlignment(Alignment.CENTER);
        txtRenderer.setPos(width / 2, height / 2);
    }
}