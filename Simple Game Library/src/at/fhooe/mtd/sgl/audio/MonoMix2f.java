package at.fhooe.mtd.sgl.audio;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Mixes and interpolates one-channel audio data for two-channel output.
 */
public final class MonoMix2f implements Mix2f {

	/** The pool of instances. */
	private static List<MonoMix2f> pool = new ArrayList<>();
	
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
	public static MonoMix2f obtain(int id, float[] data) {
		MonoMix2f result;
		if (pool.isEmpty()) {
			result = new MonoMix2f();
		} else {
			result = pool.remove(pool.size() - 1);
		}
		
		result.id = id;
		result.data = data;
		result.pos = result.loopStart = 0.0f;
		result.volume = result.leftGain = result.rightGain = result.pitch = 1.0f;
		result.panning = 0.0f;
		result.endPos = result.loopEnd = data.length - 1;
		result.switchState(result.playOnce);
		return result;
	}
	
	/**
	 * Creates a new instance. Deliberately made private.
	 */
	private MonoMix2f() {
		reset();
	}
	
	/**
	 * Returns the interpolated sample at the current position.
	 * 
	 * @return the interpolated sample
	 */
	private float getSample() {
		int idx = (int) pos;
		float p = pos - idx;

		if (idx + 1 < data.length) {
			return data[idx] * (1.0f - p) + data[idx + 1] * p;
		} else {
			return data[idx] * (1.0f - p);			
		}
	}
	
	/**
	 * Returns the interpolated sample at the current position. This method
	 * interpolates correctly according to the currently set looping boundaries.
	 * 
	 * @return the interpolated sample
	 */
	private float getLoopSample() {
		int idx = (int) pos;
		float p = pos - idx;
		
		assert pos < loopEnd + 1;
		if (pos < loopEnd) {
			return data[idx] * (1.0f - p) + data[idx + 1] * p;
		} else {
			return data[idx] * (1.0f - p) + data[(int) loopStart] * p;
		}
	}
	
	/**
	 * Returns the actual sample of the left channel.
	 * 
	 * @param sample
	 *            the raw sample value
	 * @return the sample value for the left channel
	 */
	private float getLeftChannel(float sample) {
		assert leftGain >= 0.0f : "left gain = " + leftGain;
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
		assert rightGain >= 0.0f : "right gain = " + leftGain;
		assert volume >= 0.0f : "volume = " + volume;
		return sample * volume * rightGain;
	}
	
	/**
	 * Releases resources.
	 */
	private void reset() {
		id = Audio.INVALID_HANDLE;
		data = null;
	}
	
	/**
	 * Updates left and right gain according to current panning.
	 */
	private void updatePanning() {
		double p = (1.0 + panning) / 2.0;
		leftGain = (float) Math.cos(p * Math.PI * 0.5); 
		rightGain = (float) Math.sin(p * Math.PI * 0.5); 
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
		curState = end;
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
	
	private class PlayOnceState implements State {

		/** Current (interpolated) sample. */
		private float sample;
		
		@Override
		public void enter() {
			sample = getSample();
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
			
			if (pos > endPos) {
				switchState(end);
			} else {
				sample = getSample();
			}
		}

		@Override
		public float getChannel1() {
			return getLeftChannel(sample);
		}

		@Override
		public float getChannel2() {
			return getRightChannel(sample);
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
	
	private class LoopState implements State {

		/** Current (interpolated) sample. */
		private float sample;
		
		@Override
		public void enter() {
			sample = getLoopSample();
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
			assert loopEnd <= endPos : "loopEnd > endPos (loopEnd = " + loopEnd + ", endPos = " + endPos;
			pos += pitch;
			
			// allow position to be loop end + 0.999 to interpolate against loop start
			if (pos >= loopEnd + 1.0f) {
				pos = loopStart + (pos - loopEnd - 1.0f);
			}

			// interpolate sample considering loop boundaries
			sample = getLoopSample();
		}
		
		@Override
		public float getChannel1() {
			return getLeftChannel(sample);
		}

		@Override
		public float getChannel2() {
			return getRightChannel(sample);
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
	
	private class FadeOutState implements State {

		/** Remaining samples for fade-out. */
		private int numSamples;
		
		/** Current (interpolated) sample. */
		private float sample;
		
		/** Determines how much to reduce the volume each sample. */
		private float deltaVolume;
		
		private float startVolume;
		private int cntSamples;
				
		@Override
		public void enter() {
			sample = getSample();
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
			System.out.println("fade out: " + getId()+ " : samples: " + numSamples);
			this.cntSamples = this.numSamples = numSamples;
//			deltaVolume = volume / this.numSamples;
			startVolume = volume;
			return this;
		}
		
		@Override
		public boolean hasData() {
			return true;
		}

		@Override
		public void nextData() {
			pos += pitch;
			--cntSamples;
			volume = startVolume * ((float) Math.max(0, cntSamples) / numSamples);
			
//			if (cntSamples % 1000 == 0 || cntSamples <= 10)
//				System.out.println(String.format("s=%d, v=%.3f", cntSamples, volume));
			
			if (pos >= endPos || cntSamples < 0) {
				switchState(end);
			} else {
				sample = getSample() * volume;
			}
		}

		@Override
		public float getChannel1() {
			return getLeftChannel(sample);
		}

		@Override
		public float getChannel2() {
			return getRightChannel(sample);
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
	
	private class FadeOutLoopState implements State {

		/** Remaining samples for fade-out. */
		private int numSamples;
		
		/** Current (interpolated) sample. */
		private float sample;
		
		/** Determines how much to reduce the volume each sample. */
		private float deltaVolume;
		
		@Override
		public void enter() {
			sample = getLoopSample();			
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
			volume -= deltaVolume;
			
			if (numSamples < 0) {
				// switch to end state
				switchState(end);
				return;
			}			
			
			// allow position to be loop end + 0.999 to interpolate against loop start
			if (pos >= loopEnd + 1.0f) {
				pos = loopStart + (pos - loopEnd - 1.0f);
			}
			
			// interpolate sample considering loop boundaries
			sample = getLoopSample();			
		}

		@Override
		public float getChannel1() {
			return getLeftChannel(sample);
		}

		@Override
		public float getChannel2() {
			return getRightChannel(sample);
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

		@Override
		public void enter() {
			// intentionally left empty
		}

		@Override
		public void exit() {
			// intentionally left empty
		}
		
	}

}
