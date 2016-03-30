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

package at.fhooe.mtd.sgl.math;

public class MathUtils {

	public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static long clamp(long value, long min, long max) {
		return value < min ? min : value > max ? max : value;
	}
	
    public static double lerp (double from, double to, double p) {
        return from + (to - from) * p;
    }	
    
    public static float lerp (float from, float to, float p) {
        return from + (to - from) * p;
    }   
}
