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
package at.fhooe.mtd.igp.sgl.graphics.screen;

import java.awt.BufferCapabilities;
import java.awt.BufferCapabilities.FlipContents;
import java.awt.ImageCapabilities;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class BufferCapabilitiesBuilder {
    
    private FlipContents flipContents = FlipContents.UNDEFINED;
    private boolean vsync = false;
    private boolean accelerated = true;
    
    public static BufferCapabilitiesBuilder getInstance() {
        return new BufferCapabilitiesBuilder();
    }
    
    public BufferCapabilitiesBuilder vsync(boolean value) {
        vsync = value;
        return this;
    }
    
    public boolean isVsync() {
        return vsync;
    }
    
    public boolean isAccelerated() {
        return accelerated;
    }
    
    public BufferCapabilitiesBuilder accelerated(boolean value) {
        accelerated = value;
        return this;
    }
    
    public FlipContents getFlipContents() {
        return flipContents;
    }
    
    public BufferCapabilitiesBuilder flipContents(FlipContents fc) {
        flipContents = fc;
        return this;
    }
        
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public BufferCapabilities create() {
        ImageCapabilities front = new ImageCapabilities(accelerated);
        ImageCapabilities back = new ImageCapabilities(accelerated);
        
        try {
            Class extBufCapClz = Class
                    .forName("sun.java2d.pipe.hw.ExtendedBufferCapabilities");
            
            Class vsyncTypeCls = Class
                    .forName("sun.java2d.pipe.hw.ExtendedBufferCapabilities$VSyncType");
            
            Object vsType = Enum.valueOf(vsyncTypeCls, vsync ? "VSYNC_ON"
                    : "VSYNC_OFF");
            
            Constructor<?> constr = extBufCapClz.getConstructor(ImageCapabilities.class,
                    ImageCapabilities.class, FlipContents.class, vsyncTypeCls);
            
            return (BufferCapabilities) constr.newInstance(front, back, flipContents, vsType);
        } catch (ClassNotFoundException | NoSuchMethodException
                | SecurityException | InstantiationException
                | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            
            e.printStackTrace();
            
            return new BufferCapabilities(front, back, flipContents);
        }
    }       
}