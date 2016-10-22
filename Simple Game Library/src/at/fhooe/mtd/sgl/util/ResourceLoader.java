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

package at.fhooe.mtd.sgl.util;

import java.io.InputStream;
import java.net.URL;

public class ResourceLoader {

    /**
     * The URL of the specified resource.
     * 
     * @param name
     *            the resource name of path
     * @return the UTL the specified resource
     * @throws IllegalArgumentException
     *             in case the specified resource has not been found
     */
    public static URL getUrl(String name) throws IllegalArgumentException {
        URL result = ResourceLoader.class.getResource(name);
        if (result == null && !name.startsWith("/")) {
            result = ResourceLoader.class.getResource("/" + name);
        }
        if (result == null) {
            throw new IllegalArgumentException("unknown resource + " + name);
        }
        
        return result;
    }
    
    /**
     * Retrieves the input stream to the specified resource.
     * 
     * @param name
     *            the resource name of path
     * @return the input stream of the specified resource
     * @throws IllegalArgumentException
     *             in case the specified resource has not been found
     */    
    public static InputStream getStream(String name)
            throws IllegalArgumentException {
        
        InputStream result = ResourceLoader.class.getResourceAsStream(name);
        if (result == null && !name.startsWith("/")) {
            result = ResourceLoader.class.getResourceAsStream("/" + name);
        }
        if (result == null) {
            throw new IllegalArgumentException("unknown resource " + name);
        }
        
        return result;
   }
    
}
