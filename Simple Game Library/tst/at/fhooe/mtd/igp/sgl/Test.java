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
package at.fhooe.mtd.igp.sgl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import com.sun.glass.events.KeyEvent;

import at.fhooe.mtd.igp.sgl.Sgl;
import at.fhooe.mtd.igp.sgl.app.ApplicationListener;
import at.fhooe.mtd.igp.sgl.app.Java2dApplication;
import at.fhooe.mtd.igp.sgl.app.Java2dApplicationConfig;
import at.fhooe.mtd.igp.sgl.graphics.Alignment;
import at.fhooe.mtd.igp.sgl.graphics.GfxConfigurator;
import at.fhooe.mtd.igp.sgl.graphics.TextRenderer;

public class Test implements ApplicationListener {

    private static final String APP_TITLE = "SGL Hello World Demo";
    private static final String VERSION = "1.0";

    public static void main(String[] args) {
        GfxConfigurator gfxc = new GfxConfigurator();
        gfxc.runDialog();
        
        Java2dApplicationConfig config = Java2dApplicationConfig.create(gfxc);
        config.title = APP_TITLE + " - v" + VERSION;
                
        new Java2dApplication(config, new Test());
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