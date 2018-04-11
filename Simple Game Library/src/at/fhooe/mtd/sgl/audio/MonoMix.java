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

import java.util.ArrayList;
import java.util.List;

/**
 * A one channel audio mix to be player. This class is internally used by the
 * audio system.
 */
final class MonoMix extends AbstractMix<MonoMix> {
	
	/** The pool of mix instances. */
	private static final List<MonoMix> pool = new ArrayList<>();					
	
	/**
	 * Obtains a new mix instance.
	 * 
	 * @param data the audio data associated with this mix
	 * @return the new mix instance
	 */
	public static MonoMix obtain(float[] data) {
		assert data != null;
		
		MonoMix result;
		if (pool.isEmpty()) {
			result = new MonoMix();
		} else {
			result = pool.remove(pool.size() - 1);
		}
		
		result.init(data);
		result.loopStart = 0.0f;
		result.loopEnd = data.length;
		return result;
	}
	
	public float getData() {
		assert !isFree();
		int idx = (int) fpos;
		float p = fpos - idx;
		
		float d1 = data[idx];
		float d2 = idx + 1 < data.length ? data[idx + 1] : 0.0f;
		
		return (1.0f - p) * d1 + p * d2;
	}
	
	@Override
	public boolean hasData() {
		assert !isFree();
		return fpos < data.length ;
	}
	
	@Override
	public void moveToEnd() {
		assert !isFree();
		fpos = data.length;
	}

	
	/**
	 * Creates a new mix instance.
	 */
	private MonoMix() {
		reset();
	}
		
	/**
	 * Frees this mix instance.
	 */
	public void free() {
		reset();
		pool.add(this);
	}
		
	@Override
	protected MonoMix getThis() {
		return this;
	}
	
}
