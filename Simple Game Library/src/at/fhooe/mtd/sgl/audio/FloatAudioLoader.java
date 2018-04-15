package at.fhooe.mtd.sgl.audio;

import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

public class FloatAudioLoader {
		
	/** Initial size of input float buffers. */
	private static final int INITIAL_CHANNEL_BUFFER_SIZE = 0xffff;
		
	/** The separated channels of the audio data. */
	private float channels[][];
	
	/** The current position within the audio data channel array. */
	private int positions[];
			
	/** Stores one frame of audio data. */
	private byte frameBuffer[];
	
	/** The current state according to audio format to process. */
	private State curState;
	
	/** The sample rate of the audio data. */
	private float sampleRate;
	
	
	public void load(InputStream is) throws IOException, UnsupportedAudioFileException {
		try(AudioInputStream ais = AudioSystem.getAudioInputStream(is)) {
			//Uncomment for debug print
			System.out.println(ais.getFormat());
			
			initialize(ais.getFormat());
			
			int n;
			while ((n = ais.read(frameBuffer, 0, frameBuffer.length)) == frameBuffer.length) {
				for (int i = 0; i < channels.length; ++i) {
					curState.readChannel(i);
				}
			}
			
			if (n >= 0) {
				throw new IOException("unexpected end of file");
			}
		}
	}
	
	public int numChannels() {
		return channels.length;
	}
	
	public FloatAudio getChannel(int ch) throws IndexOutOfBoundsException {
		FloatAudio result = new FloatAudio(sampleRate, positions[ch]);
		System.arraycopy(channels[ch], 0, result.getSamples(), 0, positions[ch]);
		return result;
	}
	
		
	private void initialize(AudioFormat format) throws UnsupportedAudioFileException {
		if (format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
			selectSignedState(format);
		} else if (format.getEncoding() == AudioFormat.Encoding.PCM_UNSIGNED) {
			selectUnsignedState(format);
		} else if (format.getEncoding() == AudioFormat.Encoding.PCM_FLOAT) {
			throw new RuntimeException("not implemented");
		} else {
			throw new UnsupportedAudioFileException("encoding not supported " + format.getEncoding());
		}
		
		initChannels(format.getChannels());
		if (frameBuffer == null || frameBuffer.length != format.getFrameSize()) {
			frameBuffer = new byte[format.getFrameSize()];
		}
		sampleRate = format.getSampleRate();
	}

	private void selectSignedState(AudioFormat format) throws UnsupportedAudioFileException {
		switch (format.getSampleSizeInBits()) {
		case 8:
			curState = new Pcm8SignedState();
			break;
		case 16:
			curState = format.isBigEndian() ? new Pcm16SignedBeState() : new Pcm16SignedLeState();
			break;
		case 24:
			curState = format.isBigEndian() ? new Pcm24SignedBeState() : new Pcm24SignedLeState();
			break;
			
		default:
			throw new UnsupportedAudioFileException("sample size not supported " + format.getSampleSizeInBits());
		}
	}
	
	private void selectUnsignedState(AudioFormat format) throws UnsupportedAudioFileException {
		switch (format.getSampleSizeInBits()) {
		case 8:
			curState = new Pcm8UnsignedState();
			break;
			
		default:
			throw new UnsupportedAudioFileException("sample size not supported " + format.getSampleSizeInBits());
		}
	}
	
	private void initChannels(int numChannels) {
		channels = new float[numChannels][];
		positions = new int[numChannels];
		for (int i = 0; i< channels.length; ++i) {
			channels[i] = new float[INITIAL_CHANNEL_BUFFER_SIZE];
			positions[i] = 0;
		}
	}
	
	private void putSample(int ch, float sample) throws IndexOutOfBoundsException {
		while (positions[ch] >= channels[ch].length) {
			float tmp[] = new float[channels[ch].length * 2];
			System.arraycopy(channels[ch], 0, tmp, 0, channels[ch].length);
			channels[ch] = tmp;
		}
		channels[ch][positions[ch]++] = sample;
	}
					
	/////////////////////////////////////////////////
	/////// Inner classes (states)
	/////////////////////////////////////////////////
	
	private interface State {
		
		public void readChannel(int ch);
		
	}
	
	private class Pcm8UnsignedState implements State {
		
		@Override
		public void readChannel(int ch) {
			putSample(ch, unsignedByteToSignedFloat(0xff & frameBuffer[0]));
		}
		
		private float unsignedByteToSignedFloat(int b) {
			return -1.0f + b * (2.0f/255);
		}
		
	}
	
	private class Pcm8SignedState implements State {
		
		/** Used to convert short values to floats. */
		private static final float BYTE_TO_FLOAT = 1.0f / Byte.MAX_VALUE;
		
		@Override
		public void readChannel(int ch) {
			putSample(ch, frameBuffer[0] * BYTE_TO_FLOAT);
		}
				
	}
	
	private abstract class Pcm16SignedState implements State {
		
		/** Used to convert short values to floats. */
		private static final float SHORT_TO_FLOAT = 1.0f / Short.MAX_VALUE;
		
		@Override
		public void readChannel(int ch) {
			putSample(ch, toShort(frameBuffer[ch << 1], frameBuffer[1 + (ch << 1)]) * SHORT_TO_FLOAT);
		}
		
		protected abstract short toShort(byte b1, byte b2);
	}
	
	/**
	 * Reads 16-bit samples, little endian, signed.
	 */
	private class Pcm16SignedLeState extends Pcm16SignedState {

		protected short toShort(byte b1, byte b2) {
			return (short) (b2 << 8 | b1 & 0xff);
		}
		
	}
	
	/**
	 * Reads 16-bit samples, big endian, signed.
	 */
	private class Pcm16SignedBeState extends Pcm16SignedState {

		protected short toShort(byte b1, byte b2) {
			return (short) (b1 << 8 | b2 & 0xff);
		}
		
	}
	
	private abstract class Pcm24SignedState implements State {
		
		/** Used to convert short values to floats. */
		private static final float TO_FLOAT = 1.0f / 8388607;
		
		@Override
		public void readChannel(int ch) {
			int idx = ch * 3;
			putSample(ch, toInt(frameBuffer[idx], frameBuffer[idx + 1], frameBuffer[idx + 2]) * TO_FLOAT);
		}
		
		protected abstract int toInt(byte b1, byte b2, byte b3);
	}
	
	private class Pcm24SignedLeState extends Pcm24SignedState {

		@Override
		protected int toInt(byte b1, byte b2, byte b3) {
			
			return (b3 << 16) | (0xff00 & (b2 << 8)) | (b1 & 0xff);
		}
		
	}
	
	private class Pcm24SignedBeState extends Pcm24SignedState {

		@Override
		protected int toInt(byte b1, byte b2, byte b3) {
			return (b1 << 16) | (0xff00 & (b2 << 8)) | (b3 & 0xff);
		}
		
	}
}
