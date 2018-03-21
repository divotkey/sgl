package at.fhooe.mtd.sgl.audio;

/**
 * Base class for mix objects.
 */
public abstract class AbstractMix<T> {
	
	/** Used for uninitialized mix instances. */
	public static final int INVALID_ID = 0;

	/** Used to generate unique identifier for mix instances. */
	private static int counter;
	
	/** The unique identifier of this mix. */
	private int id;

	/** The audio data represented as float values. */
	private float data[];
	
	/** The left gain factor of this mix. */
	private float leftGain;
	
	/** The right gain factor of this mix. */
	private float rightGain;
	
	/** The volume of this mix. */
	private float volume;
	
	/** Whether this mix should be looped. */
	private boolean loop;
	
	/** Current position within this mix. */
	public int pos;
	
	/**
	 * Returns the next unique identifier.
	 * 
	 * @return the next identifier
	 */
	private static int nextId() {
		return ++counter;
	}
		
	/**
	 * Returns the unique identifier of this mix.
	 * 
	 * @return the unique identifier
	 */
	public int getId() {
		return id;
	}
	
	public float getLeftGain() {
		return leftGain;
	}
	
	public T leftGain(float g) {
		leftGain = g;
		return getThis();
	}
	
	public T rightGain(float g) {
		rightGain = g;
		return getThis();
	}
	
	public float getRightGain() {
		return rightGain;
	}
	
	public T volume(float v) {
		volume = v;
		return getThis();
	}
	
	public float getVolume() {
		return volume;
	}
	
	public boolean isLoop() {
		return loop;
	}
	
	public T loop(boolean value) {
		loop = value;
		return getThis();
	}

	public float[] getAudioData() {
		return data;
	}
	
	/**
	 * Resets this instance to is default condition.
	 */
	protected void reset() {
		data = null;
		pos = 0;
		leftGain = rightGain = volume = 1.0f;
		loop = false;
		id = INVALID_ID;
	}

	protected void init(float[] data) {
		this.data = data;
		this.id = nextId();
	}
	
	protected abstract T getThis();		
}
