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

/**
 * This class contains several mathematical utility methods and constants.
 */
public class MathUtils {

    /** Constant used to convert nanoseconds to milliseconds. */
	public static final double NANO_TO_MILLIS = 1.0 / 1000000.0;
	
    /** Constant used to convert nanoseconds to seconds. */
    public static final double NANO_TO_SECONDS = 1.0 / 1000000000.0;

    /**
     * Clamps the a value to a specified range.
     * 
     * @param value
     *            the value to be clamped
     * @param min
     *            the minimum allowed value
     * @param max
     *            the maximum allowed value
     * @return the clamped value
     */
    public static double clamp(double value, double min, double max) {
		return value < min ? min : value > max ? max : value;
	}
	
    /**
     * Clamps the a value to a specified range.
     * 
     * @param value
     *            the value to be clamped
     * @param min
     *            the minimum allowed value
     * @param max
     *            the maximum allowed value
     * @return the clamped value
     */
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
    /**
     * Clamps the a value to a specified range.
     * 
     * @param value
     *            the value to be clamped
     * @param min
     *            the minimum allowed value
     * @param max
     *            the maximum allowed value
     * @return the clamped value
     */
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
    /**
     * Clamps the a value to a specified range.
     * 
     * @param value
     *            the value to be clamped
     * @param min
     *            the minimum allowed value
     * @param max
     *            the maximum allowed value
     * @return the clamped value
     */
	public static long clamp(long value, long min, long max) {
		return value < min ? min : value > max ? max : value;
	}
	
    /**
     * Linearly interpolates between two values.
     * 
     * @param from
     *            the start value
     * @param to
     *            the end value
     * @param p
     *            the current interpolation position, must be between 0 and 1
     * @return
     */
    public static double lerp (double from, double to, double p) {
        assert p >= 0 && p <= 1 : "interpolation position out of range";
        return from + (to - from) * p;
    }	
    
    /**
     * Linearly interpolates between two values.
     * 
     * @param from
     *            the start value
     * @param to
     *            the end value
     * @param p
     *            the current interpolation position, must be between 0 and 1
     * @return
     */
    public static float lerp (float from, float to, float p) {
        assert p >= 0 && p <= 1 : "interpolation position out of range";
        return from + (to - from) * p;
    }   
}
