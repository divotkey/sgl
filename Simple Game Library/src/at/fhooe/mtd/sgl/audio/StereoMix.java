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
 * A two channel audio clip to be mixed with other audio clips. This class is
 * used internally by the audio system.
 */
final class StereoMix extends AbstractMix<StereoMix> {
	
	/** The pool of mix instances. */
	private static final List<StereoMix> pool = new ArrayList<>();
				
	/**
	 * Obtains a new mix instance.
	 * 
	 * @param data the audio data associated with this mix
	 * @return the new mix instance
	 */
	public static StereoMix obtain(float[] data) {
		assert data != null;
		
		StereoMix result;
		if (pool.isEmpty()) {
			result = new StereoMix();
		} else {
			result = pool.remove(pool.size() - 1);
		}
		
		result.init(data);
		return result;
	}
	
	/**
	 * Creates a new mix instance.
	 */
	private StereoMix() {
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
	protected StereoMix getThis() {
		return this;
	}
	
}
