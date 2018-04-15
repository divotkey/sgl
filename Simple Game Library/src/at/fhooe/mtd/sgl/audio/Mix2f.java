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
package at.fhooe.mtd.sgl.audio;

import java.util.NoSuchElementException;

/**
 * A two-channel mix object with single precision floating point values.
 */
public interface Mix2f {
	
	/**
	 * Returns the unique identifier of this mix object.
	 * 
	 * @return the unique ID
	 */
	public int getId();
	
	/**
	 * Gets the sample at the current position of the first channel
	 * 
	 * @return floating point value representing the sample
	 * @throws NoSuchElementException
	 *             in case this mix object has no more data
	 */
	public float getChannel1() throws NoSuchElementException;
	
	/**
	 * Gets the sample at the current position of the second channel
	 * 
	 * @return floating point value representing the sample
	 * @throws NoSuchElementException
	 *             in case this mix object has no more data
	 */
	public float getChannel2() throws NoSuchElementException;
	
	/**
	 * Moves to the next sample position. If this mix has already reached its end
	 * state, this method has no effect.
	 */
	public void nextData();
	
	/**
	 * Returns whether this mix object has more data available. If this method
	 * returns {@code true}, it is safe query the channel data.
	 * 
	 * @return {@code true} if more data is available
	 */
	public boolean hasData();
	
	/**
	 * Returns the volume of this mix object.
	 * 
	 * @return the volume
	 */
	public float getVolume();
	
	/**
	 * Sets the volume of this mix.
	 * 
	 * @param v
	 *            the new volume
	 */
	public void setVolume(float v);
	
	/**
	 * Sets the panning for this mix object. The panning is specified within the range of
	 * [-1, 1] whereas -1 is full left and 1 is full right. Zero is center position.
	 * 
	 * @param p
	 *            the panning in the range [-1, 1]	public void setPanning(float p);
	 */
	public void setPanning(float p);
	
	/**
	 * Returns the currently set panning for this mix object.
	 * 
	 * @return the panning in the range of [-1, 1]
	 * @see #setPanning(float)
	 */
	public float getPanning();
	
	/**
	 * Sets the pitch multiplier of this sound.
	 * 
	 * @param p
	 *            the pitch multiplier
	 */
	public void setPitch(float p);
	
	/**
	 * Returns the current pitch multiplier of this sound.
	 * 
	 * @return the current pitch
	 */
	public float getPitch();
	
	/**
	 * Defines the point where to start looping.
	 * 
	 * @param pos
	 *            the position where the loop starts
	 */
	public void setLoopStart(float pos);
	
	/**
	 * Defines the point where to stop looping.
	 * 
	 * @param pos
	 *            the position where the loop stops
	 */
	public void setLoopEnd(float pos);
	
	/**
	 * Starts or stops looping the sample.
	 * 
	 * @param b
	 *            {@code true} to start looping, {@code false} to stop
	 */
	public void setLooping(boolean b);
	
	/**
	 * Stops the playback of this mix object.
	 */
	public void stop();
	
	/**
	 * Fades out this mix object.
	 * 
	 * @param n
	 *            the number of sample values used to reach zero volume
	 */
	public void fadeOut(int n);
	
	/**
	 * Frees this mix instance and puts it back into its pool.
	 */
	public void free();
	
}
