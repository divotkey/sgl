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

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface encapsulates access to the audio system.
 */
public interface Audio {
	
	/** Used for uninitialized sound handles. */
	public static final int INVALID_HANDLE = 0;

	/**
	 * Creates a new sound clip from the specified audio stream.
	 * 
	 * @param is
	 *            input stream of the specified audio file to load
	 * @return the newly created sound instance
	 * @throws IOException
	 *             in case the audio file could not be loaded
	 */
	public Sound createSound(InputStream is) throws IOException;
	
	/**
	 * Sets the master volume.
	 * 
	 * @param v
	 *            new master volume in the range of [0, 1]
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public void setVolume(double v) throws IllegalArgumentException;
	
	/**
	 * Returns the currently set master volume.
	 * 
	 * @return the current master volume within the range [0, 1]
	 */
	public double getVolume();
	
	/**
	 * Stops the playback of the specified sound. If the specified handle is
	 * invalid, this method has no effect. If the specified handle 0, all sounds
	 * currently played are stopped.
	 * 
	 * @param h
	 *            the handle of the sound
	 */
	public void stopSound(int h);
	
	/**
	 * Carries out a smooth fade out of the specified sound. If the specified handle
	 * is invalid, this method has no effect.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param duration
	 *            the duration of the fade-out effect
	 * @throws IllegalArgumentException
	 *             in case the specified duration is less or equal zero
	 */
	public void fadeOut(int h, double duration) throws IllegalArgumentException;
	
	/**
	 * Sets the volume of the sound with the specified handle. If the specified
	 * handle is invalid, this method has no effect.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param v
	 *            new master volume in the range of [0, 1]
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public void setVolume(int h, double v) throws IllegalArgumentException;
	
	/**
	 * Returns the volume of the specified sound.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @return the current volume or 0 if the handle is invalid
	 */
	public double getVolume(int h);
	
	/**
	 * Specifies whether the specified sound should be looped.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param b
	 *            {@code true} if the sound should be looped
	 */
	public void setLooping(int h, boolean b);
		
	/**
	 * Sets the pitch multiplier of the specified sound.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param p
	 *            the pitch multiplier
	 * @throws IllegalArgumentException
	 *             if the specified pitch is illegal less or equal zero
	 */
	public void setPitch(int h, double p) throws IllegalArgumentException;
	
	/**
	 * Sets the panning for the specified sound. The panning is specified within the
	 * range of [-1, 1] whereas -1 is full left and 1 is full right. Zero is center
	 * position.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param p
	 *            the panning in the range [-1, 1]
	 * @throws IllegalArgumentException
	 *             if the specified pitch is illegal less or equal zero
	 */
	public void setPanning(int h, double p) throws IllegalArgumentException;
	
	/**
	 * Stops all currently played sounds.
	 */
	public void stopAll();
	
}
