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

/**
 * This implementation of the {@link Audio} interface is using Java's low level
 * audio API and offers 2 audio channels with single precision floating point
 * values.
 */
public class JavaAudio2f implements Audio {

	/** Used to convert short values to floats. */
	private static final float SHORT_TO_FLOAT = 1.0f / Short.MAX_VALUE;


	/** The default buffer size for audio processing. */
	public static final int DEFAULT_BUFFER_SIZE = 1024;
	
	/** Audio format used for output. */
	private AudioFormat format = new AudioFormat(44100.0f,	// sample rate
												 16, 		// sample size (bits)
												 2, 		// channels
												 true, 		// signed
												 false);	// big endian

	/** The sound format used to load and store audio clips. */
	private AudioFormat formatMono = new AudioFormat(format.getEncoding(), format.getSampleRate(),
			format.getSampleSizeInBits(), 1, format.getFrameSize() / 2, format.getFrameRate(), format.isBigEndian());
	

	/** Used to load and audio data int float audio objects. */
	private FloatAudioLoader fal = new FloatAudioLoader();
	
	/** Used to mix the sounds clips. */
	private MixProcessor2f mixProc;
	
	/** Used for buffered reading of audio files. */
	private byte[] readBuffer = new byte[1024];
	
	/** Used to read audio files into byte array. */
	private ByteArrayOutputStream bos = new ByteArrayOutputStream();
		
	/** The last id used as sound handle. */
	private int lastId = Audio.INVALID_HANDLE;
	
	
	/**
	 * Creates a new instance.
	 */
	public JavaAudio2f() {
		// intentionally left empty
	}
	
	/**
	 * Returns whether this audio system has already been initialized.
	 * 
	 * @return {@true} if this audio system has already been initialized
	 */
	public boolean isOpen() {
		return mixProc != null;
	}
	
	/**
	 * Initializes the this audio system using the default buffer size.
	 * 
	 * @throws IllegalStateException
	 *             in case the audio system has already been initialized
	 */
	public void open() throws IllegalStateException  {
		open(DEFAULT_BUFFER_SIZE);
	}
	
	/**
	 * Initializes the this audio system using the specified buffer size. The
	 * specified buffer size represents the number of audio frames not the number of
	 * bytes.
	 * 
	 * @param bufferSize
	 *            the buffer size
	 * @throws IllegalStateException
	 *             in case the audio system has already been initialized
	 */
	public void open(int bufferSize) throws IllegalStateException {
		
		if (mixProc != null) {
			throw new IllegalStateException("already opened");
		}
		
		try {
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
			SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
			line.open(format, bufferSize * format.getFrameSize());
			line.start();
			mixProc = new MixProcessor2f(line);
			mixProc.engage();
		} catch (LineUnavailableException e) {
			throw new RuntimeException("unable to open audio output line", e);
		}
	}

	/**
	 * Closes this audio system. If this audio system has not been initialized or
	 * already closed, this method has no effect.
	 */
	public void close() {
		if (mixProc != null) {
			mixProc.terminate();
			mixProc = null;
		}
	}
	
	/**
	 * Returns the one-channel audio format this system is using.
	 * 
	 * @return the audio format
	 */
	public AudioFormat getMonoFormat() {
		return formatMono;
	}

	/**
	 * Returns the two-channel audio format this system is using.
	 * 
	 * @return the audio format
	 */
	public AudioFormat getStereoFormat() {
		return format;
	}
	
	/**
	 * Returns the next unique identifier to be used as sound handle.
	 * 
	 * @return the identifier
	 */
	private int nextId() {
		return ++lastId;
	}
				
	/**
	 * Creates a new sound instance from the specified audio input stream stream.
	 * 
	 * @param ais
	 *            the audio input stream containing the audio data
	 * @return the newly created sound instance
	 * @throws IOException
	 *             in case the audio file could not be loaded
	 */
	public Sound createSound(AudioInputStream ais) throws IOException {
		int numChannels = ais.getFormat().getChannels();
		
		if (numChannels == 1) {
			return new MonoSound2f(convertToFloats(loadAudio(ais, formatMono)));
		} else if (numChannels == 2){
			throw new RuntimeException("not implemented");
			//return new StereoSound(convertToFloats(loadAudio(ais, format)));
		} else {
			throw new IOException("unsupported audio format (neither mono nor stereo)");
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
			return loadAudio(as);
		}
		catch (UnsupportedAudioFileException e) {
			throw new IOException("unsupported audio format", e);
		}
	}
	
	/**
	 * Stores the data of the specified audio stream into a byte array.
	 * 
	 * @param ais
	 *            the audio stream from where to read audio data
	 * @param fmt
	 *            the target format
	 * @return the newly created byte array
	 * @throws IOException
	 *             in case of an IO error
	 */
	private byte[] loadAudio(AudioInputStream ais, AudioFormat fmt) throws IOException  {
		try(AudioInputStream ais2 = AudioSystem.getAudioInputStream(fmt, ais)) {
			return loadAudio(ais2);
		}
	}
	
	/**
	 * Stores the data of the specified audio stream into a byte array.
	 * 
	 * @param ais
	 *            the audio stream from where to read audio data
	 * @return the newly created byte array
	 * @throws IOException
	 *             in case of an IO error
	 */
	private byte[] loadAudio(AudioInputStream ais) throws IOException {
		bos.reset();
		int n;
		while ((n = ais.read(readBuffer)) > 0) {
			bos.write(readBuffer, 0, n);
		}
		bos.flush();
		return bos.toByteArray();
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

	/**
	 * Creates a new one-channel sound clip from the specified audio data.
	 * 
	 * @param data
	 *            the audio data
	 * @return the newly created sound
	 */
	public Sound createSound(FloatAudio data) {
		if (data.getSampleRate() == formatMono.getSampleRate()) {
			return new MonoSound2f(data.getSamples());
		}
		
		// convert sample rate
		FloatAudio resampled = new FloatAudio(formatMono.getSampleRate(), data);
		if (!resampled.isValid()) {
			resampled.normalize();
		}
		return new MonoSound2f(resampled.getSamples());
	}
	
	public Sound createSound(FloatAudio ch1Data, FloatAudio ch2Data) {
		if (ch1Data.getSampleRate() != format.getSampleRate()) {
			ch1Data = new FloatAudio(format.getSampleRate(), ch1Data);
			if (!ch1Data.isValid())
				ch1Data.normalize();
		}
		
		if (ch2Data.getSampleRate() != format.getSampleRate()) {
			ch2Data = new FloatAudio(format.getSampleRate(), ch2Data);
			if (!ch2Data.isValid())
				ch2Data.normalize();
		}
		
		// interleave the two channels to one
		float data[] = new float[2 * Math.max(ch1Data.getNumberOfSamples(), ch2Data.getNumberOfSamples())];
		for (int i = 0; i < ch1Data.getNumberOfSamples(); ++i) {
			data[i << 1] = ch1Data.getSamples()[i];
		}
		for (int i = 0; i < ch2Data.getNumberOfSamples(); ++i) {
			data[1 + (i << 1)] = ch2Data.getSamples()[i];
		}
		
		return new StereoSound2f(data);		
	}
	
	
	/////////////////////////////////////////////////
	/////// Interface Audio
	/////////////////////////////////////////////////

//	@Override
	public Sound createSound(InputStream is) throws IOException {
		try {
			fal.load(is);
			switch (fal.numChannels()) {
			case 1:
				return createSound(fal.getChannel(0));
			case 2:
				return createSound(fal.getChannel(0), fal.getChannel(1));
			default:
				throw new IOException("unsupported audio format, invalid number of channels " + fal.numChannels());
			}
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("unsupported audio format", e);
		}
	}
	
//	@Override
	public Sound createSound2(InputStream is) throws IOException {
		if (!is.markSupported()) {
			// load into memory to bypass the limitation that
			// Java audio cannot handle such streams
			is = toMemoryStream(is);
		}
		
		try {
			AudioFormat f = AudioSystem.getAudioFileFormat(is).getFormat();
			System.out.println(f);
			int numChannels = f.getChannels();
			if (numChannels == 1) {
				return new MonoSound2f(convertToFloats(loadAudio(is, formatMono)));
			} else if (numChannels == 2){
				return new StereoSound2f(convertToFloats(loadAudio(is, format)));
			} else {
				throw new IOException("unsupported audio format (neither mono nor stereo)");
			}
		} catch (UnsupportedAudioFileException e) {
			throw new IOException("unsupported audio format", e);
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
	public void setLooping(int h, boolean b) {
		mixProc.setLooping(h, b);
	}	
	
	@Override
	public void stopAll() {
		mixProc.stopAll();
	}	
	
	/////////////////////////////////////////////////
	/////// Private Inner Classes
	/////////////////////////////////////////////////
	
	private abstract class Sound2f<T extends Sound> implements Sound {
		
		private float[] data;
		private double panning = 0.0;
		private double volume = 1.0;
		private boolean loop = false;
		private double pitch = 1.0;
		private double loopStart;
		private double loopEnd;
		
		public Sound2f(float[] data) {
			this.data = data;
			setLoopRegion(0, getDuration());
		}
		
		/**
		 * Returns the sample data of this sound.
		 * 
		 * @return the sample data as signed float array
		 */
		protected final float[] getData() {
			return data;
		}
		
		/**
		 * Returns the start of the loop region.
		 * 
		 * @return start of loop region as sample position
		 */
		protected final double getLoopStart() {
			return loopStart;
		}
		
		/**
		 * Returns the end of the loop region.
		 * 
		 * @return end of loop region as sample position
		 */
		protected final double getLoopEnd() {
			return loopEnd;
		}
		
		/**
		 * Reference to the concrete 'this' type used for method chaining.
		 * 
		 * @return the this reference
		 */
		protected abstract T getThis();
		
		@Override
		public final T setPanning(double p) throws IllegalArgumentException {
			if (p < -1.0 || p > 1.0) {
				throw new IllegalArgumentException("panning value must be in the range of [-1, 1], got " + p);
			}
			panning = p;
			return getThis();
		}

		@Override
		public final double getPanning() {
			return panning;
		}

		@Override
		public final T setVolume(double v) throws IllegalArgumentException {
			if (v < 0 || v > 1.0) {
				throw new IllegalArgumentException("volume must be within the range [0, 1], got " + v);
			}
			volume = v;
			return getThis();
		}

		@Override
		public final double getVolume() {
			return volume;
		}
		
		@Override
		public final T setPitch(double p) throws IllegalArgumentException {
			if (p <= 0.0) {
				throw new IllegalArgumentException("illegal pitch value, got " + p);
			}
			pitch = p;
			return getThis();
		}

		@Override
		public final double getPitch() {
			return pitch;
		}

		@Override
		public final T setLoop(boolean value) {
			loop = value;
			return getThis();
		}
		
		@Override
		public final boolean isLooping() {
			return loop;
		}
		
		@Override
		public Sound setLoopRegion(double t1, double t2) throws IllegalArgumentException {
			if (t1 >= t2 || t1 > getDuration() || t2 > getDuration()) {
				throw new IllegalArgumentException("invalid loop region " + t1 + " to " + t2);
			}
			
			loopStart = t1 * formatMono.getSampleRate();
			loopEnd = Math.min(data.length - 1, t2 * formatMono.getSampleRate() - 1);
			return this;
		}
		
	}
	
	private class MonoSound2f extends Sound2f<MonoSound2f> {
		
		/**
		 * Creates a new instance. The specified sample data must be one-channel singed
		 * floating point values.
		 * 
		 * @param data
		 *            the sample data
		 */
		public MonoSound2f(float[] data) {
			super(data);
		}
		
		@Override
		public int play() {
			MonoMix2f m = MonoMix2f.obtain(nextId(), getData());
			m.setVolume((float) getVolume());
			m.setPanning((float) getPanning());
			m.setLoopStart((float) getLoopStart());
			m.setLoopEnd((float) getLoopEnd());
			m.setLooping(isLooping());
			m.setPitch((float) getPitch());
			mixProc.addMix(m);
			return m.getId();
		}

		@Override
		protected MonoSound2f getThis() {
			return this;
		}
		
		@Override
		public double getDuration() {
			return getData().length / formatMono.getSampleRate();
		}
		
	}
	
	private class StereoSound2f extends Sound2f<StereoSound2f> {
		
		/**
		 * Creates a new instance. The specified sample data must be two-channel singed
		 * floating point values.
		 * 
		 * @param data
		 *            the sample data
		 */
		public StereoSound2f(float[] data) {
			super(data);
			assert data.length % 2 == 0;
		}
		
		@Override
		public int play() {
			StereoMix2f m = StereoMix2f.obtain(nextId(), getData());
			m.setVolume((float) getVolume());
			m.setPanning((float) getPanning());
			m.setLoopStart((float) getLoopStart());
			m.setLoopEnd((float) getLoopEnd());
			m.setLooping(isLooping());
			m.setPitch((float) getPitch());
			mixProc.addMix(m);
			return m.getId();
		}

		@Override
		protected StereoSound2f getThis() {
			return this;
		}
		
		@Override
		public double getDuration() {
			return getData().length / format.getSampleRate() / 2;
		}
		
	}
			
}
