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
 * Base class for mix objects.
 */
public abstract class AbstractMix<T> {
	
	/** Used for uninitialized mix instances. */
	public static final int INVALID_ID = 0;

	/** Used to generate unique identifier for mix instances. */
	private static int counter;
	
	/** The unique identifier of this mix. */
	private int id;

	/** The audio data represented as float values. */
	protected float data[];
	
	/** The left gain factor of this mix. */
	private float leftGain;
	
	/** The right gain factor of this mix. */
	private float rightGain;
	
	/** The volume of this mix. */
	private float volume;
	
	/** The pitch multiplier. */
	private float pitch;
	
	/** Whether this mix should be looped. */
	private boolean loop;
	
	/** The start sample of the loop. */
	protected float loopStart;

	/** The end sample of the loop. */
	protected float loopEnd;
	
	/** Floating position. */
	protected float fpos;
	
	/** INdicates that this mix is not in used. */
	private boolean free;
		
	/** Control variable determining fade-out state. */
	private boolean fade;
	
	/** Numbers of samples left for fade-out. */
	private int fadeCount;
	
	/** Amount to decrease volume for each sample during fade-out. */
	private float dv;
		
	
	/**
	 * Returns the next unique identifier.
	 * 
	 * @return the next identifier
	 */
	private static int nextId() {
		return ++counter;
	}
		
	/**
	 * Returns the unique identifier of this mix.
	 * 
	 * @return the unique identifier
	 */
	public int getId() {
		assert !isFree();
		return id;
	}
	
	public float getLeftGain() {
		assert !isFree();
		return leftGain;
	}
	
	public T leftGain(float g) {
		assert !isFree();
		leftGain = g;
		return getThis();
	}
	
	public T rightGain(float g) {
		assert !isFree();
		rightGain = g;
		return getThis();
	}
	
	public float getRightGain() {
		assert !isFree();
		return rightGain;
	}
	
	public T volume(float v) {
		assert !isFree();
		volume = v;
		return getThis();
	}
	
	public T pitch(float p) {
		assert !isFree();
		pitch = p;
		return getThis();
	}
	
	public float getPitch() {
		assert !isFree();
		return pitch;
	}
	
	public float getVolume() {
		assert !isFree();
		return volume;
	}
	
	public boolean isLoop() {
		assert !isFree();
		return loop;
	}
	
	public T loop(boolean value) {
		assert !isFree();
		loop = value;
		
		return getThis();
	}

	public float[] getAudioData() {
		assert !isFree();
		return data;
	}
	
	public void fadeOut(int n) {
		assert !isFree();
		fade = true;
		fadeCount = n;
		dv = volume / fadeCount;
	}
	
	public void nextPos() {
		assert !isFree();
		fpos += pitch;
		if (fade) {
			volume -= dv;
			if (--fadeCount <= 0) {
				moveToEnd();
			}
		} else if (loop) {
			if (fpos >= loopEnd) {
				fpos = loopStart;
			}
		}
	}
			
	public void rewind() {
		assert !isFree();
		fpos = 0.0f;
	}
		
	/**
	 * Resets this instance to is default condition.
	 */
	protected void reset() {
		data = null;
		fpos = 0.0f;
		leftGain = rightGain = volume = pitch = 1.0f;
		loopStart = loopEnd = 0;
		loop = false;
		fade = false;
		free = true;
		id = INVALID_ID;
	}
	
	public boolean isFree() {
		return free;
	}
	
	protected void init(float[] data) {
		this.data = data;
		this.id = nextId();
		this.free = false;
	}
	
	public abstract boolean hasData();
	protected abstract void moveToEnd();
	protected abstract T getThis();
}
