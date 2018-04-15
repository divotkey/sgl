package at.fhooe.mtd.sgl.audio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

import at.fhooe.mtd.sgl.math.MathUtils;

/**
 * This class mixes 2-channel mix objects with single precision floating point
 * values to an audio output line. The mixing process is carried out in a
 * separate thread.
 */
public class MixProcessor2f {

	/**
	 * The status of the mixing thread.
	 * 
	 * <ul>
	 * 
	 * <li>{@link #TERMINATED}<br>
	 * The mixing thread has been terminated or has never been started. In this
	 * state, the thread can be stared by calling
	 * {@link MixProcessor2f#engage()}</li>
	 * 
	 * <li>{@link #RUNNING}<br>
	 * The mixing thread is currently running and could be terminated by calling
	 * {@link MixProcessor2f#terminate()}</li>
	 * 
	 * <li>{@link #TERMINATING}<br>
	 * The termination of the mixing thread has been scheduled.</li>
	 * 
	 * </ul>
	 */
	public enum Status {
		/** The mixing thread is terminated and not running. */
		TERMINATED, 
		
		/** The mixing thread is running. */
		RUNNING, 
		
		/** The termination of the mixing thread has been scheduled. */
		TERMINATING
	};
	
	/** Default fade-out time in seconds. */
	private static final double DEFAULT_FADE_OUT_TIME = 0.1;
	
	/** The list of mixes to be processed. */
	private List<Mix2f> mixes = new ArrayList<>();
	
	/** Audio output line to which write the final audio stream. */
	private SourceDataLine line;
	
	/** The audio buffer. */
	private byte[] outBuffer;
	
	/** The buffer used to mix the output. */
	private float[] mixBuffer;
		
	/** Master volume. */
	private float volume = 1.0f;
	
	/** The number of samples used to fade out. */
	private int numFadeOutSamples;
	
	/** The current thread status of this mix process. */
	private Status status = Status.TERMINATED;

	
	/**
	 * Creates a new instance.
	 * 
	 * @param srcLine
	 *            the source data line used to write the audio stream
	 * @throws IllegalArgumentException
	 *             in case the audio format of the specified line is incompatible
	 */
	public MixProcessor2f(SourceDataLine srcLine) throws IllegalArgumentException {
		AudioFormat format = srcLine.getFormat();
		if (format.getChannels() != 2 || format.getSampleSizeInBits() != 16) {
			throw new IllegalArgumentException(
					"source line has invalid audio format; 16bit, singed, 2 channels, little endian "
					+ "required, got" + format);
		}
				
		numFadeOutSamples = (int) (format.getSampleRate() * DEFAULT_FADE_OUT_TIME);
		outBuffer = new byte[srcLine.getBufferSize()];
		mixBuffer = new float[srcLine.getBufferSize() / format.getFrameSize() * format.getChannels()];
		this.line = srcLine;
	}
	
	/**
	 * Adds the specified mix to be processed.
	 * 
	 * @param m
	 *            the mix to add
	 * @return numeric handle used to control the playback of the mix
	 */
	public void addMix(Mix2f m) {
		synchronized(mixes) {
			mixes.add(m);
		}
	}
	
	/**
	 * Terminates the mixer thread.
	 */
	public synchronized void terminate() {
		if (status == Status.RUNNING) {
			status = Status.TERMINATING;
		}
	}
	
	/**
	 * Returns the current thread status of this mix processor.
	 * 
	 * @return the thread status
	 */
	public synchronized Status getStatus() {
		return status;
	}
	
	
	/**
	 * Starts the mixer thread.
	 */
	public synchronized void engage() {
		if (status != Status.TERMINATED) {
			throw new IllegalStateException("cannot terminate, mixer not running");
		}
		
		status = Status.RUNNING;
		new Thread(new Runnable() {
			@Override
			public void run() {
				process();
			}
		}).start();
	}
	
	/**
	 * Sets the master volume of this mix processor.
	 * 
	 * @param v
	 *            the volume
	 */
	public void setVolume(float v) {
		assert v >= 0.0;
		volume = v;
	}
	
	/**
	 * Returns the master volume of this mix processor.
	 * 
	 * @return the volume
	 */
	public float getVolume() {
		return volume;
	}
	
	/**
	 * Sets the volume of the mix object with the specified identifier.
	 * <p>
	 * If no mix object with the specified identifier could be found, this method
	 * has no effect.
	 * </p>
	 * 
	 * @param id
	 *            the identifier of the mix object
	 * @param v
	 *            the new volume
	 */
	public void setVolume(int id, float v) {
		synchronized (mixes) {
			Mix2f m = findMix(id);
			if (m != null) {
				m.setVolume(v);
			}
		}
	}
	
	/**
	 * Returns the volume of the mix object with the specified identifier.
	 * <p>
	 * If no mix object with the specified identifier could be found, this method
	 * has no effect.
	 * </p>
	 * 
	 * @param id
	 *            the identifier of the mix object
	 * @return the volume of the mix object or zero if the mix object could not be
	 *         found
	 */
	public float getVolume(int id) {
		synchronized (mixes) {
			Mix2f m = findMix(id);
			if (m != null) {
				return m.getVolume();
			}
		}
		return 0.0f;
	}

	/**
	 * Specifies whether the mix object with the specified identifier should be
	 * looped.
	 * <p>
	 * If no mix object with the specified identifier could be found, this method
	 * has no effect.
	 * </p>
	 * 
	 * @param id
	 *            the handle of the mix object
	 * @param b
	 *            {@code true} if the mix object should be looped
	 */
	public void setLooping(int id, boolean b) {
		synchronized (mixes) {
			Mix2f m = findMix(id);
			if (m != null) {
				m.setLooping(b);
			}
		}
	}
	
	/**
	 * Terminates the playback of the mix object with the specified identifier. If
	 * the no mix object with the specified ID does exist, this method has no
	 * effect.
	 * 
	 * @param id
	 *            the identifier of the mix object
	 */
	public void stopMix(int id) {
		synchronized (mixes) {
			Mix2f m = findMix(id);
			if (m != null) {
				// TODO add fade-out command with parameterized duration and use stop here
				//m.stop();
				m.fadeOut(numFadeOutSamples);
			}
		}
	}

	/**
	 * Stops playback of all currently processed mix objects.
	 */
	public void stopAll() {
		synchronized (mixes) {
			for (int i = 0; i < mixes.size(); ++i) {
				mixes.get(i).free();
			}
			mixes.clear();
		}
	}
	
	/**
	 * The processing loop executed by the mixing thread.
	 */
	private void process() {
		while (getStatus() == Status.RUNNING) {
			mix();
			writeBuffer();
		}
	}
	
	/**
	 * Mixes the currently active mix objects into the mix buffer.
	 */
	private void mix() {
		// clear mix buffer
		Arrays.fill(mixBuffer, 0.0f);
		
		synchronized (mixes) {
			for (int i = 0; i < mixes.size(); ) {
				if (mix(mixes.get(i))) {
					++i;
				} else {
					mixes.remove(i).free();
				}
			}
		}
	}
	
	/**
	 * Mixes the specified mix object into the mix buffer.
	 * 
	 * @param m
	 *            the mix object to process
	 * @return {@code true} if the mix object has still data, {@code false} if the
	 *         mix object can be removed
	 */
	private boolean mix(Mix2f m) {
		
		for (int i = 0; i < mixBuffer.length && m.hasData(); ) {
			mixBuffer[i++] += m.getChannel1() * volume;
			mixBuffer[i++] += m.getChannel2() * volume;
			m.nextData();
		}
		
		return m.hasData();
	}

	/**
	 * Writes the current content of the mix buffer to the output audio line.
	 */
	private void writeBuffer() {

		// copy mix buffer to out buffer (and do format conversion)
		for (int i = 0, j = 0; i < mixBuffer.length; ++i) {
			short value = (short) (MathUtils.clamp(mixBuffer[i], -1.0f, 1.0f) * Short.MAX_VALUE);
			outBuffer[j++] = (byte) (value & 0xff);
			outBuffer[j++] = (byte) ((value >> 8) & 0xff);
		}
				
		// write byte buffer to line
		line.write(outBuffer, 0, outBuffer.length);
	}
	
	/**
	 * Retrieves the mix object with the specified ID.
	 * 
	 * @param id
	 *            the identifier of the mix object to retrieve
	 * @return the requested mix object or {@code null} if the mix object could not
	 *         be found
	 */
	private Mix2f findMix(int id) {
		synchronized (mixes) {
			for (int i = 0; i < mixes.size(); ++i) {
				Mix2f m = mixes.get(i);
				if (m.getId() == id) {
					return m;
				}
			}
		}
		
		return null;
	}
	
}
