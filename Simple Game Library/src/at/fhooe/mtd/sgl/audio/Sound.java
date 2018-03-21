package at.fhooe.mtd.sgl.audio;

/**
 * Represents a small audio clip which is completely stored in memory. 
 */
public interface Sound {
	
	/**
	 * Plays this sound with its currently set parameters (volume, panning, etc.).
	 * The playback will start immediately after this method returns. Changing this
	 * sounds parameters has no effect on the current playback.
	 * 
	 * @return a handle used to control the playback of this sound
	 */
	public int play();
		
	/**
	 * Sets the panning for this sound. The panning is specified within the range of
	 * [-1, 1] whereas -1 is full left and 1 is full right. Zero is center position.
	 * 
	 * @param p
	 *            the panning in the range [-1, 1]
	 * @throws IllegalStateException
	 *             in case the specified panning value is out of range
	 */
	public void setPanning(double p) throws IllegalArgumentException;
	
	/**
	 * Returns the currently set panning for this sound.
	 * 
	 * @return the panning in the range of [-1, 1]
	 *
	 * @see #setPanning(double)
	 */
	public double getPanning();
	
	/**
	 * Sets the volume of this sound. The volume must be in the range of [0, 1].
	 * 
	 * @param v
	 *            the volume of this sound
	 * @throws IllegalArgumentException
	 *             in case the specified volume is out of range
	 */
	public void setVolume(double v) throws IllegalArgumentException ;
	
	/**
	 * Returns the volume of this sound.
	 * 
	 * @return the volume of this sound
	 */
	public double getVolume();
	
	/**
	 * Specifies if this sound should be looped.
	 * 
	 * @param value
	 *            {@code true} if this sound should be looped
	 */
	public void setLoop(boolean value);
}
