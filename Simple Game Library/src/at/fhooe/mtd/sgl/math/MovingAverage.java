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
package at.fhooe.mtd.sgl.math;

import java.util.Arrays;

public class MovingAverage {

	/** The default window size. */
    public static final int DETAULT_WINDOW_SIZE = 50;
    
    private double[] dataPoints;
    private double sum;
    private double avg;
    private int pos;

	/**
	 * Creates a new instance using the default window size of 50.
	 */
    public MovingAverage() {
        this(DETAULT_WINDOW_SIZE);
    }

	/**
	 * Creates a new instance with the specified window size.
	 * 
	 * @param windowSize
	 *            the window size
	 */
    public MovingAverage(int windowSize) {
        dataPoints = new double[windowSize];
        Arrays.fill(dataPoints, 0.0);
        pos = 0;
        avg = sum = 0.0;
    }
    
    public void addDataPoint(double value) {
        sum += -dataPoints[pos] + (dataPoints[pos] = value);
        pos = ++pos % dataPoints.length;
        avg = sum / dataPoints.length;
    }
    
    public double getAverage() {
        return avg;
    }
    
    public int getWindowSize() {
        return dataPoints.length;
    }
}
