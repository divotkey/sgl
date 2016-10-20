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
 * A four-dimensional vector with double-precision.
 * <p>
 * These vector classes are optimized to avoid repetitive memory allocation. For
 * a more detailed explanation see {@link Vector2d}.
 * </p>
 */
public class Vector4d {

	/**
	 * A vector with zero length.
	 * <p>
	 * <strong>Note:</strong> In Java this vector is not really a constant. Use
	 * with care, do not modify it.
	 * </p>
	 */
	public static final Vector4d ZERO = new Vector4d(0, 0, 0, 0);

	/** The x coordinate. */
	public double x;
	
	/** The y coordinate. */
	public double y;
	
	/** The z coordinate. */
	public double z;
	
	/** The w coordinate. */
	public double w;

	
	/**
	 * Create a new vector with zero length. 
	 */
	public Vector4d() {
		// intentionally left empty
	}

	/**
	 * Creates a new vector with the given coordinates.
	 * 
	 * @param x
	 *            x coordinate of the new vector
	 * @param y
	 *            y coordinate of the new vector
	 * @param z
	 *            z coordinate of the new vector
	 * @param w
	 *            w coordinate of the new vector
	 */
	public Vector4d(double x, double y, double z, double w) {
		set(x, y, z, w);
	}
	
	/**
	 * Creates a new vector from the given vector (copy constructor).
	 * 
	 * @param o
	 *            the other vector
	 */
	public Vector4d(Vector4d o) {
		set(o);
	}
	
	/**
	 * Creates a new vector from the given vector and w-component.
	 * 
	 * @param o
	 *            the other vector
	 * @param w
	 *            the w-component
	 */
	public Vector4d(Vector3d o, double w) {
		set(o, w);
	}

	/**
	 * Creates a copy of this vector.
	 * 
	 * @return a newly created instance with its values set to this vector
	 */
	public Vector4d copy() {
		return new Vector4d(this);
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
	public double getY() {
		return y;
	}
	
	/**
	 * Returns the z coordinate of this vector.
	 * 
	 * @return the z coordinate
	 */
	public double getZ() {
		return z;
	}
	
	/**
	 * Returns the w coordinate of this vector.
	 * 
	 * @return the w coordinate
	 */
	public double getW() {
		return w;
	}
	
	/**
	 * Sets the x coordinate of this vector.
	 * 
	 * @param x
	 *            the x coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector4d setX(double x) {
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
	public Vector4d setY(double y) {
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
	public Vector4d setZ(double z) {
		this.z = z;
		return this;
	}
	
	/**
	 * Sets the w coordinate of this vector.
	 * 
	 * @param w
	 *            the w coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector4d setW(double w) {
		this.w = w;
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
	 * @param w
	 *            the w coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d set(double x, double y, double z, double w) {
		this.x = x; this.y = y; this.z = z; this.w = w;
		return this;
	}
	
	/**
	 * Sets this vector o the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d set(Vector4d o) {
		x = o.x; y = o.y; z = o.z; w = o.w;
		return this;
	}
	
	/**
	 * Sets the vector's components using values form the specified array.
	 * 
	 * @param src
	 *            the array where to take the components from
	 * @return reference to this vector for method chaining
	 * @throws IndexOutOfBoundsException
	 *             if the specified position is negative or the array does not
	 *             contain enough values
	 */
	public Vector4d set(double[] src) throws IndexOutOfBoundsException{
		return set(src, 0);
	}
	
	/**
	 * Sets the vector's components using values form the specified array.
	 * 
	 * @param src
	 *            the array where to take the components from
	 * @param pos
	 *            the position within the array
	 * @return reference to this vector for method chaining
	 * @throws IndexOutOfBoundsException
	 *             if the specified position is negative or the array does not
	 *             contain enough values
	 */
	public Vector4d set(double[] src, int pos) throws IndexOutOfBoundsException {
		x = src[pos]; y = src[pos + 1]; z = src[pos + 2]; w = src[pos + 3];
		return this;		
	}
	
	/**
	 * Sets the vector to the given vector and w-component.
	 * 
	 * @param v
	 *            the other vector
	 * @param w
	 *            the w component
	 * @return reference to this vector for method chaining
	 */
	public Vector4d set(Vector3d v, double w) {
		x = v.x; y = v.y; z = v.z; this.w = w;
		return this;
	}
	
	
	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z + w * w);
	}	
	
	/**
	 * Returns the length of this vector squared.
	 * 
	 * @return the squared length of this vector
	 */
	public double lengthSquared() {
		return x * x + y * y + z * z + w * w;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param o
	 *            the vector to add
	 * @return reference to this vector for method chaining
	 */
	public Vector4d add(Vector4d o) {
		x += o.x; y += o.y; z += o.z; w += o.w;
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
	 * @param w
	 *            the w coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d add(double x, double y, double z, double w) {
		this.x += x; this.y += y; this.z += z; this.w += w;
		return this;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param o
	 *            the vector to subtract
	 * @return reference to this vector for method chaining
	 */
	public Vector4d sub(Vector4d o) {
		x -= o.x; y -= o.y; z -= o.z; w -= o.w;
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
	 * @param w
	 *            the w coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d sub(double x, double y, double z, double w) {
		this.x -= x; this.y -= y; this.z -= z; this.w -= w;
		return this;
	}
	
	/**
	 * Scales this vector by the given scalar value.
	 * 
	 * @param s
	 *            the scalar value
	 * @return reference to this vector for method chaining
	 */
	public Vector4d scale(double s) {
		x *= s; y *= s; z *= s; w *= s;
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
	 * @param sw
	 *            the scaling factor of the w-component
	 * @return reference to this vector for method chaining
	 */
	public Vector4d scale(double sx, double sy, double sz, double sw) {
		x *= sx; y *= sy; z *= sz; w *= sw;
		return this;
	}
	
	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d scale(Vector4d o) {
		x *= o.x; y *= o.y; z *= o.z; w *= o.w;
		return this;
	}
	
	/**
	 * Scales the given vector with the given scalar value and add the result to
	 * this vector.
	 * 
	 * @param o
	 *            the other vector that should be scaled and added
	 * @param s
	 *            the scalar value that will scale the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector4d mulAdd(Vector4d o, double s) {
		x += o.x * s; y += o.y * s; z += o.z * s; w += o.w * s;
		return this;
	}
	
	
    @Override
    public String toString() {
        return String.format("<%f, %f, %f, %f>", x, y, z, w);
    }
	
}
