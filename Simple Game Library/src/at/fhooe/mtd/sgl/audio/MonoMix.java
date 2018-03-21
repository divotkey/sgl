package at.fhooe.mtd.sgl.audio;

import java.util.ArrayList;
import java.util.List;

/**
 * A one channel audio mix to be player. This class is internally used by the
 * audio system.
 */
final class MonoMix extends AbstractMix<MonoMix> {
	
	/** The pool of mix instances. */
	private static final List<MonoMix> pool = new ArrayList<>();					
	
	/**
	 * Obtains a new mix instance.
	 * 
	 * @param data the audio data associated with this mix
	 * @return the new mix instance
	 */
	public static MonoMix obtain(float[] data) {
		assert data != null;
		
		MonoMix result;
		if (pool.isEmpty()) {
			result = new MonoMix();
		} else {
			result = pool.remove(pool.size() - 1);
		}
		
		result.init(data);
		return result;
	}
	
	/**
	 * Creates a new mix instance.
	 */
	private MonoMix() {
		reset();
	}
		
	/**
	 * Frees this mix instance.
	 */
	public void free() {
		reset();
		pool.add(this);
	}
		
	@Override
	protected MonoMix getThis() {
		return this;
	}
	
}
