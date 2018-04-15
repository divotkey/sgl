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
import java.util.NoSuchElementException;

/**
 * Mixes and interpolates two-channel audio data for two-channel output.
 */
public final class StereoMix2f implements Mix2f {

	/** The pool of instances. */
	private static List<StereoMix2f> pool = new ArrayList<>();
	
	/** The identifier of this mix object. */
	private int id;
	
	/** The current position within the sample data. */
	private float pos;
	
	/** The final position as float representation. */
	private float endPos;
	
	/** The pitch multiplier. */
	private float pitch;
	
	/** The audio samples. */
	private float[] data;
	
	/** The gain factor for the left channel. */
	private float leftGain;
	
	/** The gain factor for the right channel. */
	private float rightGain;
	
	/** The panning for this sound [-1, 1]. */
	private float panning;
	
	/** The volume of this mix object. */
	private float volume;
	
	/** An instance of the play-once state. */
	private PlayOnceState playOnce = new PlayOnceState();
	
	/** An instance of the loop state. */
	private LoopState loop = new LoopState();
	
	/** An instance of the fade-out state. */
	private FadeOutState fadeOut = new FadeOutState();
	
	/** An instance of the fade-out state while looping. */
	private FadeOutLoopState fadeOutLoop = new FadeOutLoopState();
	
	/** An instance of the end state. */
	private EndState end = new EndState();
	
	/** Defines the point where to start looping. */
	private float loopStart;
	
	/** Defines the point where to stop looping. */
	private float loopEnd;
	
	/** The current state. */
	private State curState = end;
	

	/**
	 * Obtains an instance of this mix object.
	 * 
	 * @param data
	 *            the sample data as singed float value
	 * @return the instance of the mix object
	 */
	public static StereoMix2f obtain(int id, float[] data) {
		assert data.length % 2 == 0;
		
		StereoMix2f result;
		if (pool.isEmpty()) {
			result = new StereoMix2f();
		} else {
			result = pool.remove(pool.size() - 1);
		}
		
		result.id = id;
		result.data = data;
		result.pos = result.loopStart = 0.0f;
		result.volume = result.leftGain = result.rightGain = result.pitch = 1.0f;
		result.panning = 0.0f;
		result.endPos = result.loopEnd = (data.length >> 1) - 1;
		result.switchState(result.playOnce);
		return result;
	}
	
	/**
	 * Creates a new instance. Deliberately made private.
	 */
	private StereoMix2f() {
		reset();
	}
			
	/**
	 * Returns the actual sample of the left channel.
	 * 
	 * @param sample
	 *            the raw sample value
	 * @return the sample value for the left channel
	 */
	private float getLeftChannel(float sample) {
		assert leftGain >= 0.0f : "right gain = " + rightGain;
		assert volume >= 0.0f : "volume = " + volume;
		return sample * volume * leftGain;
	}
	
	/**
	 * Returns the actual sample of the right channel.
	 * 
	 * @param sample
	 *            the raw sample value
	 * @return the sample value for the right channel
	 */
	private float getRightChannel(float sample) {
		assert rightGain >= 0.0f : "right gain = " + rightGain;
		assert volume >= 0.0f : "volume = " + volume;
		return sample * volume * rightGain;
	}
	
	/**
	 * Releases resources.
	 */
	private void reset() {
		id = Audio.INVALID_HANDLE;
		data = null;
		switchState(end);
	}
	
	private void updatePanning() {
		if (panning == 0) {
			leftGain = rightGain = 1.0f;
		} else if (panning > 0) {
			leftGain = (float) (1.0 - panning / 1.0);
			rightGain = 1.0f;
		} else {
			leftGain = 1.0f;
			rightGain = (float) (1.0 + panning / 1.0);
		}
	}
	
	private void switchState(State s) {
		if (curState == s)
			return;
		if (curState != null) {
			curState.exit();
		}
		curState = s;
		curState.enter();
	}
	
	/////////////////////////////////////////////////
	/////// Interface Mix2f
	/////////////////////////////////////////////////
	
	@Override
	public float getChannel1() throws NoSuchElementException {
		return curState.getChannel1();
	}

	@Override
	public float getChannel2() throws NoSuchElementException {
		return curState.getChannel2();
	}

	@Override
	public void nextData() {
		curState.nextData();
	}

	@Override
	public boolean hasData() {
		return curState.hasData();
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public float getVolume() {
		return volume;
	}

	@Override
	public void setVolume(float v) {
		volume = v;
	}
	
	@Override
	public void setPanning(float p) {
		assert p >= -1.0f && p <= 1.0f;
		panning = p;
		updatePanning();
	}
	
	@Override
	public float getPanning() {
		return panning;
	}
	
	@Override
	public void setPitch(float p) {
		assert p > 0.0f;
		pitch = p;
	}

	@Override
	public float getPitch() {
		return pitch;
	}
	
	@Override
	public void setLoopStart(float p) {
		loopStart = p;
	}

	@Override
	public void setLoopEnd(float p) {
		loopEnd = p;
	}

	@Override
	public void setLooping(boolean b) {
		curState.setLooping(b);
	}

	@Override
	public void stop() {
		switchState(end);
	}	
	
	@Override
	public void fadeOut(int n) {
		curState.fadeOut(n);
	}
	
	@Override
	public void free() {
		reset();
		pool.add(this);
	}
	
	/////////////////////////////////////////////////
	/////// Internal States
	/////////////////////////////////////////////////
	
	private interface State {
		public void enter();
		public void exit();
		public boolean hasData();
		public void nextData();
		public float getChannel1();
		public float getChannel2();
		public void fadeOut(int numSamples);
		public void setLooping(boolean b);
	}
	
	private abstract class LipState implements State {
		
		/** Current (interpolated) sample of channel 1. */
		private float sample1;
		
		/** Current (interpolated) sample of channel 2. */
		private float sample2;

		/**
		 * Updates the samples for left and right channel using linear interpolation.
		 * The samples are interpolated against zero if the end of sample data has been
		 * reached.
		 */
		protected final void updateSamples() {
			int idx = (int) pos;
			float p = pos - idx;
			float np = 1.0f - p;
			idx <<= 1;
			
			if (idx + 2 < data.length) {
				sample1 = data[idx] * np + data[idx + 2] * p;
				sample2 = data[idx + 1] * np + data[idx + 3] * p;
			} else {
				sample1 = data[idx] * np;
				sample2 = data[idx + 1] * np;
			}
		}
		
		@Override
		public final float getChannel1() {
			return getLeftChannel(sample1);
		}

		@Override
		public final float getChannel2() {
			return getRightChannel(sample2);
		}
	}
	
	private abstract class LipLoopState implements State {
		
		/** Current (interpolated) sample of channel 1. */
		private float sample1;
		
		/** Current (interpolated) sample of channel 2. */
		private float sample2;


		/**
		 * Updates the samples for left and right channel using linear interpolation.
		 * The samples are interpolated against zero if the end of sample data has been
		 * reached.
		 */
		protected final void updateSamples() {
			int idx = (int) pos;
			float p = pos - idx;
			float np = 1.0f - p;
			idx <<= 1;
			
			assert pos < loopEnd + 1;
			if (pos < loopEnd) {
				sample1 = data[idx] * np + data[idx + 2] * p;
				sample2 = data[idx + 1] * np + data[idx + 3] * p;
			} else {
				int idx2 = (int) loopStart;
				sample1 = data[idx] * np + data[idx2] * p;
				sample2 = data[idx + 1] * np + data[idx2 + 1] * p;
			}			
			
		}
		
		@Override
		public final float getChannel1() {
			return getLeftChannel(sample1);
		}

		@Override
		public final float getChannel2() {
			return getRightChannel(sample2);
		}
	}
	
	
	private class PlayOnceState extends LipState {
		
		@Override
		public void enter() {
			updateSamples();
		}
		
		@Override
		public void exit() {
			// intentionally left empty
		}
		
		@Override
		public boolean hasData() {
			return true;
		}

		@Override
		public void nextData() {
			pos += pitch;
			
			if (pos >= endPos) {
				switchState(end);
			} else {
				updateSamples();
			}
		}


		@Override
		public void setLooping(boolean b) {
			if (b) {
				switchState(loop);
			}
		}

		@Override
		public void fadeOut(int numSamples) {
			switchState(fadeOut.numSamples(numSamples));
		}
		
	}
	
	private class LoopState extends LipLoopState {
		
		@Override
		public void enter() {
			updateSamples();
		}
		
		@Override
		public void exit() {
			// intentionally left empty
		}
		
		@Override
		public boolean hasData() {
			return true;
		}

		@Override
		public void nextData() {
			assert loopEnd <= endPos;
			pos += pitch;
			
			// allow position to be loop end + 0.999 to interpolate against loop start
			if (pos >= loopEnd + 1.0f) {
				pos = loopStart + (pos - loopEnd - 1.0f);
			}

			// interpolate sample considering loop boundaries
			updateSamples();
		}
				
		@Override
		public void setLooping(boolean b) {
			if (!b) {
				switchState(playOnce);
			}
		}

		@Override
		public void fadeOut(int numSamples) {
			switchState(fadeOutLoop.numSamples(numSamples));
		}
		
	}
	
	private class FadeOutState extends LipState {

		/** Remaining samples for fade-out. */
		private int numSamples;
		
		
		/** Determines how much to reduce the volume each sample. */
		private float deltaVolume;
		
		@Override
		public void enter() {
			updateSamples();
		}
		
		@Override
		public void exit() {
			// intentionally left empty
		}
		
		/**
		 * Sets the number of samples used to reach zero volume.
		 * 
		 * @param numSamples
		 *            number of samples for fade-out
		 * @return reference to this state for method chaining
		 */
		public FadeOutState numSamples(int numSamples) {
			this.numSamples = numSamples;
			deltaVolume = volume / this.numSamples;
			return this;
		}
		
		@Override
		public boolean hasData() {
			return true;
		}

		@Override
		public void nextData() {
			pos += pitch;
			--numSamples;
			volume = Math.max(0.0f, volume - deltaVolume);
			
			if (pos >= endPos || numSamples <= 0) {
				switchState(end);
			} else {
				updateSamples();
			}
		}

		@Override
		public void setLooping(boolean b) {
			if (b) {
				switchState(fadeOutLoop.numSamples(numSamples));
			}
		}

		@Override
		public void fadeOut(int numSamples) {
			// ignore
		}
		
	}
	
	private class FadeOutLoopState extends LipLoopState {

		/** Remaining samples for fade-out. */
		private int numSamples;
				
		/** Determines how much to reduce the volume each sample. */
		private float deltaVolume;
		
		@Override
		public void enter() {
			updateSamples();
		}
		
		@Override
		public void exit() {
			// intentionally left empty
		}
		
		
		/**
		 * Sets the number of samples used to reach zero volume.
		 * 
		 * @param numSamples
		 *            number of samples for fade-out
		 * @return reference to this state for method chaining
		 */
		public FadeOutLoopState numSamples(int numSamples) {
			this.numSamples = numSamples;
			deltaVolume = volume / this.numSamples;
			return this;
		}
		
		@Override
		public boolean hasData() {
			return true;
		}

		@Override
		public void nextData() {
			assert loopEnd <= endPos;
			pos += pitch;
			--numSamples;
			volume = Math.max(0.0f, volume - deltaVolume);
			
			if (numSamples <= 0) {
				switchState(end);
				return;
			}			
			
			// allow position to be loop end + 0.999 to interpolate against loop start
			if (pos >= loopEnd + 1.0f) {
				pos = loopStart + (pos - loopEnd - 1.0f);
			}
			
			// interpolate sample considering loop boundaries
			updateSamples();
		}

		@Override
		public void setLooping(boolean b) {
			if (!b) {
				switchState(fadeOut.numSamples(numSamples));
			}
		}

		@Override
		public void fadeOut(int numSamples) {
			// ignore
		}
		
	}
	
	
	private class EndState implements State {

		@Override
		public void enter() {
			// intentionally left empty
		}

		@Override
		public void exit() {
			// intentionally left empty
		}
		
		@Override
		public boolean hasData() {
			return false;
		}

		@Override
		public void nextData() {
			// Intentionally left empty
		}

		@Override
		public float getChannel1() {
			throw new NoSuchElementException("no channel data available");
		}

		@Override
		public float getChannel2() {
			throw new NoSuchElementException("no channel data available");
		}
		
		@Override
		public void setLooping(boolean b) {
			// ignore
		}

		@Override
		public void fadeOut(int numSamples) {
			// ignore
		}
		
	}

}
