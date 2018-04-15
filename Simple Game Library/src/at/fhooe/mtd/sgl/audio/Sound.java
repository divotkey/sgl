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

/**
 * Represents a small audio clip which is completely stored in memory. 
 */
public interface Sound {
	
	/**
	 * Plays this sound with its currently set parameters (volume, panning, etc.).
	 * The playback will start immediately after this method returns. Changing this
	 * sounds parameters has no effect on the current playback.
	 * 
	 * @return a handle used to control the playback of this sound
	 */
	public int play();
		
	/**
	 * Sets the panning for this sound. The panning is specified within the range of
	 * [-1, 1] whereas -1 is full left and 1 is full right. Zero is center position.
	 * 
	 * @param p
	 *            the panning in the range [-1, 1]
	 * @return reference to this sound for method chaining
	 * @throws IllegalStateException
	 *             in case the specified panning value is out of range
	 */
	public Sound setPanning(double p) throws IllegalArgumentException;
	
	/**
	 * Returns the currently set panning for this sound.
	 * 
	 * @return the panning in the range of [-1, 1]
	 *
	 * @see #setPanning(double)
	 */
	public double getPanning();
	
	/**
	 * Sets the volume of this sound. The volume must be in the range of [0, 1].
	 * 
	 * @param v
	 *            the volume of this sound
	 * @return reference to this sound for method chaining
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public Sound setVolume(double v) throws IllegalArgumentException;
	
	/**
	 * Returns the volume of this sound.
	 * 
	 * @return the volume of this sound
	 */
	public double getVolume();
	
	/**
	 * Sets the pitch multiplier of this sound.
	 * 
	 * @param p
	 *            the pitch multiplier
	 * @return reference to this sound for method chaining
	 * @throws IllegalArgumentException
	 *             if the specified pitch is illegal less or equal zero
	 */
	public Sound setPitch(double p) throws IllegalArgumentException;

	/**
	 * Returns the current pitch multiplier of this sound.
	 * 
	 * @return the current pitch
	 */
	public double getPitch();
	
	/**
	 * Specifies the region within the sound clip to be looped.
	 * 
	 * <p>
	 * The start and end point of the region is specified in seconds. As example, a
	 * loop region of 1.0 to 3.0 will loop the sound clip staring from the first
	 * second to the third second.
	 * </p>
	 * 
	 * @param t1
	 *            the start time of the region
	 * @param t2
	 *            the end time of the region
	 * @return reference to this sound for method chaining
	 * @throws IllegalArgumentException
	 *             in case the specified region is invalid
	 */
	public Sound setLoopRegion(double t1, double t2) throws IllegalArgumentException;
	
	/**
	 * Specifies if this sound should be looped.
	 * 
	 * @param value
	 *            {@code true} if this sound should be looped
	 * @return reference to this sound for method chaining
	 */
	public Sound setLoop(boolean value);
	
	/**
	 * Returns whether this sound clip is looping.
	 * 
	 * @return {@code true} if looping is enabled, {@code} false otherwise
	 */
	public boolean isLooping();
	
	/**
	 * Returns the duration of this sound clip. The duration is calculated assuming
	 * a pitch multiplier of 1.0.
	 * 
	 * @return the duration in seconds
	 */
	public double getDuration();

}
