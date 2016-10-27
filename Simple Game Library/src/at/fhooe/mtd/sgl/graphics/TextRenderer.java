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
import java.awt.geom.Rectangle2D;

public class TextRenderer {

    private static final Font DEF_FONT = new Font(Font.DIALOG, Font.PLAIN, 12);
    
    private Alignment alignment = Alignment.LEFT;
    private double posX;
    private double posY;
    private double offY;
    private Color color = Color.BLACK;
    private Font font;
    
    public TextRenderer() {
        this(DEF_FONT);
    }
    
    public TextRenderer(Font f) {
        setFont(f);
    }

	/**
	 * Resets the vertical position of this text renderer.
	 * 
	 * @return a reference to this text renderer for method chaining
	 */
    public TextRenderer reset() {
        offY = 0;
        return this;
    }
    
    public void setFont(Font f) {
        font = f;
    }
    
    public Font getFont() {
        return font;
    }
    
    public void setPos(double x, double y) {
        setPosX(x);
        setPosY(y);
    }
    
    public void setPosX(double x) {
        posX = x;
    }
    
    public void setPosY(double y) {
        posY = y;
    }
    
    public double getPosX() {
        return posX;
    }   
    
    public double getPosY() {
        return posY;
    }
    
    private double calcPosX(double textWidth) {
        if ((alignment.getCode() & Alignment.RIGHT.getCode()) != 0) {
            // RIGHT, TOP_RIGHT, BOTTOM_RIGHT
            return posX - textWidth;
        } else if ((alignment.getCode() & Alignment.LEFT.getCode()) == 0) {
            // CENTER, TOP, BOTTOM
            return posX - textWidth / 2;
        } else {
            // LEFT, TOP_LEFT, BOTTOM_LEFT
            return posX;            
        }
    }
    
    private double calcPosY(double textheight, FontMetrics fm) {
        if ((alignment.getCode() & Alignment.TOP.getCode()) != 0) {
            // TOP, TOP_RIGHT, TOP_LEFT
            return posY + fm.getAscent();
        } else if ((alignment.getCode() & Alignment.BOTTOM.getCode()) == 0) {
            // CENTER, LEFT, RIGHT
            return (posY - fm.getDescent()) + textheight / 2;
        } else {
            // BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT
           return posY - fm.getDescent();
        }
    }
    
    public void render(Graphics2D g, String s) {
        g.setColor(color);
        g.setFont(font);
        
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D b = fm.getStringBounds(s, g);
        
        double x = calcPosX(b.getWidth());
        double y = calcPosY(b.getHeight(), fm) + offY;
        
        g.drawString(s, (float) x, (float) y);
        offY += b.getHeight();
    }
               
    public Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(Alignment alignment) {
        this.alignment = alignment;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getTextHeight(Graphics2D g) {
        return g.getFontMetrics(font).getHeight();
    }

}