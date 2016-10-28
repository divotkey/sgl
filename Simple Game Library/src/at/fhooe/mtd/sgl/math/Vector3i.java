/*******************************************************************************
 * Copyright (c) 2016 Roman Divotkey, Univ. of Applied Sciences Upper Austria. 
 * All rights reserved.
 *  
 * This file is subject to the terms and conditions defined in file
 * 'LICENSE', which is part of this source code package.
 *  
 * THIS CODE IS PROVIDED AS EDUCATIONAL MATERIAL AND NOT INTENDED TO ADDRESS
 * ALL REAL WORLD PROBLEMS AND ISSUES IN DETAIL.
 *******************************************************************************/
package at.fhooe.mtd.sgl.math;

/**
 * A three-dimensional vector with integer-precision.
 * <p>
 * These vector classes are optimized to avoid repetitive memory allocation. For
 * a more detailed explanation see {@link Vector2d}.
 * </p>
 */
public class Vector3i {

	/** The x coordinate. */
	public int x;
	
	/** The y coordinate. */
	public int y;
	
	/** The z coordinate. */
	public int z;
	
	/**
	 * Create a new vector with zero length. 
	 */
	public Vector3i() {
		// intentionally left empty
	}

	/**
	 * Creates a new vector from the given vector (copy constructor).
	 * 
	 * @param o
	 *            the other vector
	 */
	public Vector3i(Vector3i o) {
		set(o);
	}
	
	/**
	 * Returns the x coordinate of this vector.
	 * 
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Returns the y coordinate of this vector.
	 * 
	 * @return the y coordinate
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Returns the z coordinate of this vector.
	 * 
	 * @return the z coordinate
	 */
	public int getZ() {
		return z;
	}
	
	/**
	 * Sets the x coordinate of this vector.
	 * 
	 * @param x
	 *            the x coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector3i setX(int x) {
		this.x = x;
		return this;
	}
	
	/**
	 * Sets the y coordinate of this vector.
	 * 
	 * @param y
	 *            the y coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector3i setY(int y) {
		this.y = y;
		return this;
	}
	
	/**
	 * Sets the z coordinate of this vector.
	 * 
	 * @param z
	 *            the z coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector3i setZ(int z) {
		this.z = z;
		return this;
	}
	
	/**
	 * Sets this vector to the given vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @param z
	 *            the z coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3i set(int x, int y, int z) {
		this.x = x; this.y = y; this.z = z;
		return this;
	}
	
	/**
	 * Sets this vector to the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3i set(Vector3i o) {
		x = o.x; y = o.y; z = o.z;
		return this;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param o
	 *            the vector to subtract
	 * @return reference to this vector for method chaining
	 */
	public Vector3i sub(Vector3i o) {
		x -= o.x; y -= o.y; z -= o.z;
		return this;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @param z
	 *            the z coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3i sub(int x, int y, int z) {
		this.x -= x; this.y -= y; this.z -= z;
		return this;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param o
	 *            the vector to add
	 * @return reference to this vector for method chaining
	 */
	public Vector3i add(Vector3i o) {
		x += o.x; y += o.y; z += o.z;
		return this;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @param z
	 *            the z coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3i add(int x, int y, int z) {
		this.x += x; this.y += y; this.z += z;
		return this;
	}
	
	/**
	 * Scales this vector by the given scalar value.
	 * 
	 * @param s
	 *            the scalar value
	 * @return reference to this vector for method chaining
	 */
	public Vector3i scale(int s) {
		x *= s; y *= s; z *= s;
		return this;
	}
	
	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param sx
	 *            the scaling factor of the x-component
	 * @param sy
	 *            the scaling factor of the y-component
	 * @param sz
	 *            the scaling factor of the z-component
	 * @return reference to this vector for method chaining
	 */
	public Vector3i scale(int sx, int sy, int sz) {
		x *= sx; y *= sy; z *= sz;
		return this;
	}
	
	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3i scale(Vector3i o) {
		x *= o.x; y *= o.y; z *= o.z;
		return this;
	}
	
}
