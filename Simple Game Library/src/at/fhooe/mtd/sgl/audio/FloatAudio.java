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
 * A real number representation of an one-channel audio material with single
 * precision. Sample data are stored as signed real number.
 * 
 * <p>
 * The purpose of this class is to be used for sample rate conversion and
 * exchange of audio data. Its main feature is that audio data can be accessed
 * at any position specified in terms of time instead of sample number.
 * </p>
 */
public class FloatAudio {

	/** Used to convert signed short values to floats. */
	private static final float SHORT_TO_FLOAT = 1.0f / Short.MAX_VALUE;
	
	/** Used to convert signed byte values to floats. */
	private static final float BYTE_TO_FLOAT = 1.0f / Byte.MAX_VALUE;

	/** Whether to use nearest neighbor interpolation. */
	private boolean nearestNeighbor = false;
	
	/** The sample rate of the audio material. */
	private float sampleRate;
	
	/** The floating precision audio data. */
	private float[] data;

	
	/**
	 * Creates a new instance.
	 * 
	 * @param rate
	 *            the sample rate in samples per second
	 * @param numSamples
	 *            the number of samples
	 * @throws IllegalArgumentException
	 *             in case the rate or the number of samples is invalid
	 */
	public FloatAudio(Float rate, int numSamples) throws IllegalArgumentException {
		if (rate <= 0) {
			throw new IllegalArgumentException("illegal sample rate " + rate);
		}
		
		if (numSamples < 0) {
			throw new IllegalArgumentException("number of samples must be greater or equal zero");
		}
		
		this.sampleRate = rate;
		this.data = new float[numSamples];
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param rate
	 *            the sample rate in samples per second
	 * @param duration
	 *            the duration in seconds
	 * @throws IllegalArgumentException
	 *             in case the rate or the duration is invalid
	 */
	public FloatAudio(Float rate, float duration) throws IllegalArgumentException {
		this(rate, (int) (rate * checkDuration(duration)));
	}
		
	/**
	 * Creates a new instance. If the sample rates do not match, a sample rate
	 * conversion is carried out.
	 * 
	 * @param rate
	 *            the sample rate in samples per second
	 * @param o
	 *            another instance which audio data should be used/converted
	 * @throws IllegalArgumentException
	 *             in case the rate is invalid
	 */
	public FloatAudio(float rate, FloatAudio o) throws IllegalArgumentException {
		this(rate, rate == o.sampleRate ? o.data.length : (int) (o.getDuration() * rate));

		// copy or convert sample data
		if (o.sampleRate == sampleRate) {
			assert data.length == o.data.length;
			System.arraycopy(o.data, 0, data, 0, o.data.length);
		} else {
			// convert samples
			float dt = 1.0f / sampleRate;
			for (int i = 0; i < this.data.length; ++i) {
				this.data[i] = o.getSample(i * dt);
			}
		}
	}
		
	private static float checkDuration(float duration) {
		if (duration <= 0) {
			throw new IllegalArgumentException("invalid duration " + duration);
		}
		return duration;
	}
	
	/**
	 * Returns whether linear interpolation (LERP) is used. Linear interpolation is
	 * used by default.
	 * 
	 * @return {@code true} if linear interpolation is used, {@code false} for
	 *         nearest neighbor interpolation
	 */
	public boolean isLinearInterpolation() {
		return !nearestNeighbor;
	}
	
	/**
	 * Specifies is linear or nearest neighbor interpolation should be used to
	 * access samples. Linear interpolation is used by default.
	 * 
	 * @param b
	 *            {@code} true for linear interpolation, {@code} false for nearest
	 *            neighbor interpolation
	 */
	public void setLinearInterpoiation(boolean b) {
		nearestNeighbor = !b;
	}
	
	/**
	 * Sets the sample at the specified index. The sample data is specified as
	 * 16-bit signed integer.
	 * 
	 * @param idx
	 *            index of the sample to set
	 * @param smpl
	 *            signed short representing the 16-bit sample data
	 * @throws IndexOutOfBoundsException
	 *             in case the specified index is invalid
	 */
	public void setSample(int idx, short smpl) throws IndexOutOfBoundsException {
		data[idx] = smpl * SHORT_TO_FLOAT;
	}
	
	/**
	 * Sets the sample at the specified index. The sample data is specified as
	 * 8-bit signed integer.
	 * 
	 * @param idx
	 *            index of the sample to set
	 * @param smpl
	 *            signed byte representing the 8-bit sample data
	 * @throws IndexOutOfBoundsException
	 *             in case the specified index is invalid
	 */
	public void setSample(int idx, byte smpl) throws IndexOutOfBoundsException {
		data[idx] = smpl  * BYTE_TO_FLOAT;
	}
	
	/**
	 * Sets the sample at the specified index.
	 * 
	 * @param idx
	 *            index of the sample to set
	 * @param smpl
	 *            float value representing the sample data
	 * @throws IndexOutOfBoundsException
	 */
	public void setSample(int idx, float smpl) throws IndexOutOfBoundsException {
		data[idx] = smpl;
	}
	
	/**
	 * Returns the internal array of samples. Changes to this array will affect this
	 * audio data object.
	 * 
	 * @return the array of sample data
	 */
	public float[] getSamples() {
		return data;
	}
	
	/**
	 * Returns the duration of the audio in seconds.
	 * 
	 * @return duration in seconds
	 */
	public float getDuration() {
		return data.length / sampleRate;
	}
	
	/**
	 * Returns the sample at the specified time.
	 * 
	 * @param t
	 *            the time
	 * @return the sample value at the specified time
	 */
	public float getSample(float t) throws IllegalArgumentException {
		if (t < 0 || t > getDuration()) {
			throw new IllegalArgumentException("invalid time " + t);
		}
		
		// nearest neighbor interpolation
		if (nearestNeighbor) {
			int idx = (int) (t * sampleRate);
			return data[idx];
		} else {
			// linear interpolation
			float fpos = t * sampleRate;
			int idx = (int) fpos;
			float p = fpos - idx;
			
			if (idx + 1 < data.length) {
				return data[idx] * (1.0f - p) + data[idx + 1] * p;
			} else {
				return data[idx] * (1.0f - p);
			}			
		}
		
	}
	
	/**
	 * Tests if all samples are in the range of [-1, 1].
	 * 
	 * @return {@true} if all samples are valid.
	 */
	public boolean isValid() {
		for (float s : data) {
			if (s < 1.0 || s > 1.0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Returns the sample rate.
	 * 
	 * @return the sample rate in samples per second
	 */
	public float getSampleRate() {
		return sampleRate;
	}
	
	/**
	 * Returns the number of stored samples.
	 * 
	 * @return number of samples
	 */
	public int getNumberOfSamples() {
		return data.length;
	}
	
	/**
	 * Returns the specified sample as signed 16-bit integer number.
	 * 
	 * @param idx
	 *            the index of the sample to return
	 * @return the requested sample as singed short
	 * @throws IndexOutOfBoundsException
	 *             in case the specified index is invalid
	 */
	public short getSignedShort(int idx) throws IndexOutOfBoundsException {
		return (short) (data[idx] * Short.MAX_VALUE);
	}
	
	
	/* Commented out, cause this API is somewhat inefficient and was added for debugging */
	public byte getSignedShortLowByte(int idx) throws IndexOutOfBoundsException {
		return (byte) (getSignedShort(idx) & 0xff);
	}
	
	public byte getSignedShortHighByte(int idx) throws IndexOutOfBoundsException {
		return (byte) ((getSignedShort(idx) >> 8) & 0xff);
	}

	/**
	 * Returns the index of the sample data with the largest amplitude.
	 * 
	 * @return the peek index
	 */
	public int findPeak() {
		int result = 0;
		double largestValue = Float.NEGATIVE_INFINITY;
		for (int i = 0; i < data.length; ++i) {
			double curValue = Math.abs(data[i]);
			if (curValue > largestValue) {
				largestValue = curValue;
				result = i;
			}
		}
		return result;
	}
	
	/**
	 * Multiplies all values by the given scalar.
	 * 
	 * @param s
	 *            the scalar
	 */
	public void scale(float s) {
		for (int i = 0; i < data.length; ++i) {
			data[i] *= s;
		}
	}
	
	/**
	 * Normalizes the audio data.
	 */
	public void normalize() {
		normalize(1.0f);
	}
	
	public void normalize(float s) {
		if (s > 1.0 || s < 0.0) {
			throw new IllegalArgumentException("normalize scaling out of range " + s);
		}
		
		float peak = data[findPeak()];
		if (peak != 0 && peak != 1.0) {
			scale(s/peak);
		}
	}
}
