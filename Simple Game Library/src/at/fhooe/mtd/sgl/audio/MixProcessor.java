package at.fhooe.mtd.sgl.audio;

import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

public class MixProcessor implements Runnable {
	
	/** The list of mixes to be processed. */
	private List<MonoMix> monoMixes = new ArrayList<>();
	
	/** The list of 2-channel mixes to be processed. */
	private List<StereoMix> stereoMixes = new ArrayList<>();
	

	/** Audio output line to which write the final audio stream. */
	private SourceDataLine line;
	
	/** The audio buffer. */
	private byte[] outBuffer;
	
	/** The buffer used to mix the output. */
	private float[] mixBuffer;
	
	/** Control variable used to terminate thread. */
	private boolean terminate = false;
		
	
	/**
	 * Creates a new instance.
	 * 
	 * @param srcLine
	 *            the source data line used to write the audio stream
	 * @throws IllegalArgumentException
	 *             in case the audio format of the specified line is incompatible
	 */
	public MixProcessor(SourceDataLine srcLine) throws IllegalArgumentException {
		AudioFormat format = srcLine.getFormat();
		if (format.getChannels() != 2 || format.getSampleSizeInBits() != 16) {
			throw new IllegalArgumentException(
					"source line has invalid audio format; 16bit, singed, 2 channels, little endian "
					+ "required, got" + format);
		}
				
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
	public void addMix(MonoMix m) {
		synchronized(monoMixes) {
			monoMixes.add(m);
		}
	}
	
	public void addMix(StereoMix m) {
		synchronized(stereoMixes) {
			stereoMixes.add(m);
		}
	}

	public synchronized void terminate() {
		terminate = true;
	}
	
	public synchronized boolean isTerminated() {
		return terminate;
	}
	
	@Override
	public void run() {
		
		while ( !isTerminated() ) {
			mix();
			writeBuffer();
		}
	}
	
	private void mix() {
		// clear mix buffer
		
		for (int i = 0; i < mixBuffer.length; ++i) {
			mixBuffer[i] = 0;
		}

		// mix mono clips
		synchronized (monoMixes) {
			for (int i = 0; i < monoMixes.size(); ) {
				if (mix(monoMixes.get(i))) {
					monoMixes.remove(i).free();
				} else {
					++i;
				}
			}
		}
		
		// mix stereo clips
		synchronized (stereoMixes) {
			for (int i = 0; i < stereoMixes.size(); ) {
				if (mix(stereoMixes.get(i))) {
					stereoMixes.remove(i).free();
				} else {
					++i;
				}
			}
		}
		
	}
	
	private boolean mix(StereoMix mix) {
		float[] data = mix.getAudioData();
		float gl = mix.getLeftGain();
		float gr = mix.getRightGain();
		float v = mix.getVolume();
		
		for (int j = 0; mix.pos < data.length && j < mixBuffer.length;) {
			mixBuffer[j] = clamp(mixBuffer[j] + data[mix.pos] * gl * v);
			++j; ++mix.pos;
			mixBuffer[j] = clamp(mixBuffer[j] + data[mix.pos] * gr * v);
			++j; ++mix.pos;
		}
		
		if (mix.pos >= data.length) {
			if (mix.isLoop()) {
				mix.pos = 0;
			} else {
				return true;
			}
		}
		return false;
	}

	private boolean mix(MonoMix mix) {
		float[] data = mix.getAudioData();
		float gl = mix.getLeftGain();
		float gr = mix.getRightGain();
		float v = mix.getVolume();
		
		for (int j = 0; mix.pos < data.length && j < mixBuffer.length; ++mix.pos) {
			mixBuffer[j] = clamp(mixBuffer[j] + data[mix.pos] * gl * v);
			++j;
			mixBuffer[j] = clamp(mixBuffer[j] + data[mix.pos] * gr * v);
			++j;
		}
		
		if (mix.pos >= data.length) {
			if (mix.isLoop()) {
				mix.pos = 0;
			} else {
				return true;
			}
		}
		return false;
	}
	
	private float clamp(float x) {
		if (x < -1.0f)
			return -1.0f;
		if (x > 1.0f)
			return 1.0f;
		return x;
	}
	
	private void writeBuffer() {

		// copy mix buffer to out buffer (and do format conversion)
		for (int i = 0, j = 0; i < mixBuffer.length; ++i) {
			short value = (short) (mixBuffer[i] * Short.MAX_VALUE);
			outBuffer[j++] = (byte) (value & 0xff);
			outBuffer[j++] = (byte) ((value >> 8) & 0xff);
		}
				
		// write byte buffer to line
		line.write(outBuffer, 0, outBuffer.length);
	}

	private MonoMix findMonoMix(int id) {
		for (int i = 0; i < this.monoMixes.size(); ++i) {
			MonoMix m = monoMixes.get(i);
			if (m.getId() == id)
				return m;
		}
		return null;
	}
	
	private StereoMix findStereoMix(int id) {
		for (int i = 0; i < this.stereoMixes.size(); ++i) {
			StereoMix m = stereoMixes.get(i);
			if (m.getId() == id)
				return m;
		}
		return null;
	}
		
	/**
	 * Stops the playback of the mix with the specified identifier.
	 * @param id the identifier of the mix to stop
	 */
	public void stopMix(int id) {
		synchronized (monoMixes) {
			 MonoMix mix = findMonoMix(id);
			 if (mix != null) {
					mix.loop(false);
					mix.pos = mix.getAudioData().length;
			 }
		}
		
		synchronized (stereoMixes) {
			 StereoMix mix = findStereoMix(id);
			 if (mix != null) {
					mix.loop(false);
					mix.pos = mix.getAudioData().length;
			 }
		}
	}

	public void setVolume(int id, double v) {
		synchronized (monoMixes) {
			 MonoMix mix = findMonoMix(id);
			 if (mix != null) {
				mix.volume((float) v);
			 }
		}
		synchronized (stereoMixes) {
			 StereoMix mix = findStereoMix(id);
			 if (mix != null) {
					mix.volume((float) v);
			 }
		}
	}

	public double getVolume(int id) {
		synchronized (monoMixes) {
			 MonoMix mix = findMonoMix(id);
			 if (mix != null) {
				 return mix.getVolume();
			 }
		}
		
		synchronized (stereoMixes) {
			 StereoMix mix = findStereoMix(id);
			 if (mix != null) {
				 return mix.getVolume();
			 }
		}
		
		return 0;
	}
}