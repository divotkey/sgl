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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

public class JavaAudio implements Audio {

	/** Used to convert short values to floats. */
	private static final float SHORT_TO_FLOAT = 1.0f / Short.MAX_VALUE;


	/** The default buffer size for audio processing. */
	private static final int DEFAULT_BUFFER_SIZE = 1024;
	
	/** Audio format used for output. */
	private AudioFormat format = new AudioFormat(44100.0f,	// sample rate
												 16, 		// sample size (bits)
												 2, 		// channels
												 true, 		// signed
												 false);	// big endian

	/** The sound format used to load and store audio clips. */
	private AudioFormat formatMono = new AudioFormat(format.getEncoding(), format.getSampleRate(),
			format.getSampleSizeInBits(), 1, format.getFrameSize() / 2, format.getFrameRate(), format.isBigEndian());
	

	/** Used to mix the sounds clips. */
	private MixProcessor mixProc;
	
	/** Used for buffered reading of audio files. */
	private byte[] readBuffer = new byte[1024];
	
	/** Used to read audio files into byte array. */
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
	/**
	 * Creates a new instance.
	 */
	public JavaAudio() {
		// intentionally left empty
	}
	
	public boolean isOpen() {
		return mixProc != null;
	}
	
	public void open() {
		open(DEFAULT_BUFFER_SIZE);
	}
	
	public void open(int bufferSize) throws IllegalStateException {
		if (mixProc != null) {
			throw new IllegalStateException("already opened");
		}
		
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize * format.getFrameSize());
			line.start();
			mixProc = new MixProcessor(line);
			new Thread(mixProc).start();
		} catch (LineUnavailableException e) {
			throw new RuntimeException("unable to open audio output line", e);
		}
	}
	
	private int playSound(MonoSound snd, float volume, float leftGain, float rightGain, boolean loop) {
		MonoMix m;
		mixProc.addMix(m = MonoMix.obtain(snd.data)
				.volume(volume)
				.leftGain(leftGain)
				.rightGain(rightGain)
				.loop(loop));
		return m.getId();
	}

	private int playSound(StereoSound snd, float volume, float leftGain, float rightGain, boolean loop) {
		StereoMix m;
		mixProc.addMix(m = StereoMix
				.obtain(snd.data)
				.volume(volume)
				.leftGain(leftGain)
				.rightGain(rightGain)
				.loop(loop));
		return m.getId();
	}
	
	public void close() {
		if (mixProc != null) {
			mixProc.terminate();
			mixProc = null;
		}
	}
		
	@Override
	public Sound createSound(InputStream is) throws IOException {
		if (!is.markSupported()) {
			// load into memory to bypass the limitation that
			// Java audio cannot handle such streams
			is = toMemoryStream(is);
		}
		
		try {
			boolean stereo = AudioSystem.getAudioFileFormat(is).getFormat().getChannels() == 2;
			if (stereo) {
				return new StereoSound(convertToFloats(loadAudio(is, format)));
			} else {
				return new MonoSound(convertToFloats(loadAudio(is, formatMono)));
			}
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("unsupported audio format", e);
		}
	}
		
	/**
	 * Converts a byte array with 6-bit audio data to float data.
	 * 
	 * @param bytes
	 *            the byte array of audio data to convert
	 * @return float array containing the converted audio data
	 */
	private float[] convertToFloats(byte[] bytes) {
		float[] floats = new float[bytes.length >> 1];
		
		for (int i = 0, j = 0; i < floats.length; ++i) {
			byte lo = bytes[j++]; byte hi = bytes[j++];
			floats[i] = toShort(hi, lo) * SHORT_TO_FLOAT;
		}
		
		return floats;
	}
	
	/**
	 * Loads an audio file specified as input stream into a byte array
	 * 
	 * @param is
	 *            the input stream representing the audio file
	 * @param fmt
	 *            the target audio format the loaded file should be converted to
	 * @return the byte array containing the raw audio data
	 * @throws IOException
	 *             in case the audio file could not be loaded
	 */
	private byte[] loadAudio(InputStream is, AudioFormat fmt) throws IOException {
				
		try (AudioInputStream as = AudioSystem.getAudioInputStream(fmt, AudioSystem.getAudioInputStream(is))) {
			bos.reset();
			int n;
			while ((n = as.read(readBuffer)) > 0) {
				bos.write(readBuffer, 0, n);
			}
			bos.flush();
			return bos.toByteArray();
		}
		catch (UnsupportedAudioFileException e) {
			throw new IOException("unsupported audio format", e);
		}
	}
	
	/**
	 * Loads the specified stream into memory mapped stream.
	 * 
	 * @param is
	 *            the stream to be loaded
	 * @return an input stream served from memory
	 * @throws IOException
	 *             in case of an I/O error
	 */
	private InputStream toMemoryStream(InputStream is) throws IOException {
		try {
			bos.reset();
			int n;
			while ((n = is.read(readBuffer)) > 0) {
				bos.write(readBuffer, 0, n);
			}
			return new ByteArrayInputStream(bos.toByteArray());
		} finally {
			// close stream and ignore errors happening when closing the stream
			try { is.close(); } catch (IOException e) { }
		}
	}

	private short toShort(byte hi, byte lo) {
		return (short) (hi << 8 | lo & 0xff);
	}
	
	private class MonoSound implements Sound {
		
		private float[] data;
		private double panning = 0.0;
		private double volume = 1.0;
		private boolean loop = false;

		public MonoSound(float[] data) {
			this.data = data;
		}

		@Override
		public int play() {
			double p = (1.0 + panning) / 2.0;
			double lg = Math.cos(p * Math.PI * 0.5); 
			double rg = Math.sin(p * Math.PI * 0.5); 
			return playSound(this, (float) volume, (float) lg, (float) rg, loop);
		}

		@Override
		public Sound setPanning(double p) throws IllegalArgumentException {
			if (p < -1.0 || p > 1.0) {
				throw new IllegalArgumentException("panning value must be in the range of [-1, 1], got " + p);
			}
			panning = p;
			return this;
		}

		@Override
		public double getPanning() {
			return panning;
		}

		@Override
		public Sound setVolume(double v) throws IllegalArgumentException {
			if (volume < 0 || volume > 1.0) {
				throw new IllegalArgumentException("volume must be within the range [0, 1], got " + v);
			}
			volume = v;
			return this;
		}

		@Override
		public double getVolume() {
			return volume;
		}

		@Override
		public Sound setLoop(boolean value) {
			loop = true;
			return this;
		}

	}
	
	private class StereoSound implements Sound {
		
		private float[] data;
		private double panning = 0.0;
		private double volume = 1.0;
		private boolean loop = false;

		public StereoSound(float[] data) {
			this.data = data;
		}

		@Override
		public int play() {
			if (panning == 0) {
				return playSound(this, (float) volume, 1.0f, 1.0f, loop);
			} else if (panning > 0) {
				return playSound(this,(float) volume, (float) (1.0 - panning / 1.0), 1.0f, loop);				
			} else {
				return playSound(this, (float) volume, 1.0f, (float) (1.0 + panning / 1.0), loop);								
			}
		}

		@Override
		public Sound setPanning(double p) throws IllegalArgumentException {
			if (p < -1.0 || p > 1.0) {
				throw new IllegalArgumentException("panning value must be in the range of [-1, 1], got " + p);
			}
			panning = p;
			return this;
		}

		@Override
		public double getPanning() {
			return panning;
		}

		@Override
		public Sound setVolume(double v) throws IllegalArgumentException {
			if (volume < 0 || volume > 1.0) {
				throw new IllegalArgumentException("volume must be within the range [0, 1], got " + v);
			}
			volume = v;
			return this;
		}

		@Override
		public double getVolume() {
			return volume;
		}

		@Override
		public Sound setLoop(boolean value) {
			loop = value;
			return this;
		}

	}

	@Override
	public void setVolume(double v) throws IllegalArgumentException {
		if (v < 0.0 || v > 1.0) {
			throw new IllegalArgumentException("volume parameter out of range [0, 1], got " + v);
		}
		mixProc.setVolume((float) v);
	}

	@Override
	public double getVolume() {
		return (float) mixProc.getVolume();
	}

	@Override
	public void stopSound(int h) {
		if (h == INVALID_HANDLE) {
			stopAll();
		} else {
			mixProc.stopMix(h);			
		}
	}

	@Override
	public void setVolume(int h, double v) throws IllegalArgumentException {
		if (v < 0.0 || v > 1.0) {
			throw new IllegalArgumentException("volume parameter out of range [0, 1], got " + v);
		}
		if (h == INVALID_HANDLE) {
			mixProc.setVolume((float) v);
		} else {
			mixProc.setVolume(h, (float) v);			
		}
	}

	@Override
	public double getVolume(int h) {
		if (h == INVALID_HANDLE) {
			return getVolume();
		} 
		return mixProc.getVolume(h);
	}

	@Override
	public void stopAll() {
		mixProc.stopAll();
	}
		
}
