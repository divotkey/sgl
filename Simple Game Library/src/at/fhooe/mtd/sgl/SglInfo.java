/*******************************************************************************
 * Copyright (c) 2016 - 2018 Roman Divotkey,
 * Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *   
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *    
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/
package at.fhooe.mtd.sgl;

/**
 * Provides version information of SGL.
 */
public class SglInfo {

	/** The name of this library. */
    public static final String NAME = "Simple Game Library";

    /** The version number as string. */
    public static final String VERSION = "1.6.1";
    
    /** The copyright holder of this version. */
    public static final String COPYRIGHT_HOLDER 
        = "Roman Divotkey, Univ. of Applied Sciences Upper Austria";
    
    /** The copyright year. */
    public static final String COPYRIGHT_YEAR = "2016 - 2018";
        
    /** The name plus version information. */
    public static final String FULL_NAME = NAME + " (" + VERSION + ")";
    
    /** The complete copyright string. */
    public static final String COPYRIGHT = "Copyright (c) " + COPYRIGHT_YEAR
            + COPYRIGHT_HOLDER + "." + " All rights reserved.";
    
}
