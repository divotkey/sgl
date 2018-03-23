package at.fhooe.mtd.sgl.audio;

import java.io.IOException;
import java.io.InputStream;

/**
 * This interface encapsulates access to the audio system.
 */
public interface Audio {
	
	/** Used for uninitialized sound handles. */
	public static final int INVALID_HANDLE = 0;

	/**
	 * Creates a new sound instance from the specified audio stream.
	 * 
	 * @param is
	 *            input stream of the specified audio file to load
	 * @return the newly created sound instance
	 * @throws IOException
	 *             in case the audio file could not be loaded
	 */
	public Sound createSound(InputStream is) throws IOException;
	
	/**
	 * Sets the master volume.
	 * 
	 * @param v
	 *            new master volume in the range of [0, 1]
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public void setVolume(double v) throws IllegalArgumentException;
	
	/**
	 * Returns the currently set master volume.
	 * 
	 * @return the current master volume within the range [0, 1]
	 */
	public double getVolume();
	
	/**
	 * Stops the playback of the specified sound. If the specified handle is
	 * invalid, this method has no effect. If the specified handle 0, all sounds
	 * currently played are stopped.
	 * 
	 * @param h
	 *            the handle of the sound
	 */
	public void stopSound(int h);
	
	/**
	 * Sets the volume of the sound with the specified handle. If the specified
	 * handle is invalid, this method has no effect.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @param v
	 *            new master volume in the range of [0, 1]
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public void setVolume(int h, double v) throws IllegalArgumentException;
	
	/**
	 * Returns the volume of the specified sound.
	 * 
	 * @param h
	 *            the handle of the sound
	 * @return the current volume or 0 if the handle is invalid
	 */
	public double getVolume(int h);
	
	/**
	 * Stops all currently played sounds.
	 * @return 
	 */
	public void stopAll();
	
}
