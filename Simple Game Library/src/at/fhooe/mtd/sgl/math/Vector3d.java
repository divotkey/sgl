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
 * A three-dimensional vector with double-precision.
 * <p>
 * These vector classes are optimized to avoid repetitive memory allocation. For
 * a more detailed explanation see {@link Vector2d}.
 * </p>
 */
public class Vector3d {
	
	/** Used for tests to avoid precision error. */
	public static final double EPSILON = 0.000000001;

	/**
	 * A vector with zero length.
	 * <p>
	 * <strong>Note:</strong> In Java this vector is not really a constant. Use
	 * with care, do not modify it.
	 * </p>
	 */
	public static final Vector3d ZERO = new Vector3d(0, 0, 0);

	/** The x coordinate. */
	public double x;
	
	/** The y coordinate. */
	public double y;
	
	/** The z coordinate. */
	public double z;
	
	/**
	 * Create a new vector with zero length. 
	 */
	public Vector3d() {
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
	 */
	public Vector3d(double x, double y, double z) {
		set(x, y, z);
	}
	
	/**
	 * Creates a new vector from the given vector (copy constructor).
	 * 
	 * @param o
	 *            the other vector
	 */
	public Vector3d(Vector3d o) {
		set(o);
	}
	
	/**
	 * Creates a new vector from the given vector and z-component.
	 * 
	 * @param o
	 *            the other vector
	 * @param z
	 *            the z-component
	 */
	public Vector3d(Vector2d o, double z) {
		set(o, z);
	}
	
	/**
	 * Creates a copy of this vector.
	 * 
	 * @return a newly created instance with its values set to this vector
	 */
	public Vector3d copy() {
		return new Vector3d(this);
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
	 * Sets the x coordinate of this vector.
	 * 
	 * @param x
	 *            the x coordinate
	 * @return reference to this vector for method chaining
	 */
	public Vector3d setX(double x) {
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
	public Vector3d setY(double y) {
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
	public Vector3d setZ(double z) {
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
	public Vector3d set(double x, double y, double z) {
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
	public Vector3d set(Vector3d o) {
		x = o.x; y = o.y; z = o.z;
		return this;
	}
	
	/**
	 * Sets this vector to the given vector. The fourth component of the
	 * four-dimensional vector will be ignored.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d set(Vector4d o) {
		x = o.x; y = o.y; z = o.z;
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
	public Vector3d set(double[] src) throws IndexOutOfBoundsException{
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
	public Vector3d set(double[] src, int pos) throws IndexOutOfBoundsException {
		x = src[pos]; y = src[pos + 1]; z = src[pos + 2];
		return this;		
	}
	
	/**
	 * Sets the vector to the given vector and z-component.
	 * 
	 * @param v
	 *            the other vector
	 * @param z
	 *            the z component
	 * @return reference to this vector for method chaining
	 */
	public Vector3d set(Vector2d v, double z) {
		x = v.x; y = v.y; this.z = z;
		return this;
	}
	
	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public double length() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	/**
	 * Returns the length of this vector squared.
	 * 
	 * @return the squared length of this vector
	 */
	public double lengthSquared() {
		return x * x + y * y + z * z;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param o
	 *            the vector to subtract
	 * @return reference to this vector for method chaining
	 */
	public Vector3d sub(Vector3d o) {
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
	public Vector3d sub(double x, double y, double z) {
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
	public Vector3d add(Vector3d o) {
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
	public Vector3d add(double x, double y, double z) {
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
	public Vector3d scale(double s) {
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
	public Vector3d scale(double sx, double sy, double sz) {
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
	public Vector3d scale(Vector3d o) {
		x *= o.x; y *= o.y; z *= o.z;
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
	public Vector3d mulAdd(Vector3d o, double s) {
		x += o.x * s; y += o.y * s; z += o.z * s;
		return this;
	}
	
	/**
	 * Scales the given vector by the given scale vector and adds the result to
	 * this vector.
	 * 
	 * @param o
	 *            the other vector that should be scaled and added
	 * @param sv
	 *            the scale vector that will scale the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d mulAdd(Vector3d o, Vector3d sv) {
		x += o.x * sv.x; y += o.y * sv.y; z += o.z * sv.z;
		return this;
	}
	
	/**
	 * Calculates the dot product between this vector and the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return the dot product between this vector and the other vector
	 */
	public double dot(Vector3d o) {
		return x * o.x + y * o.y + z * o.z;
	}
	
	/**
	 * Calculates the dot product between this vector and the given vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @param z
	 *            the z coordinate of the other vector
	 * @return the dot product between this vector and the other vector
	 */
	public double dot(double x, double y, double z) {
		return this.x * x + this.y * y + this.z * z;
	}
	
	/**
	 * Sets this vector to the cross product between this vector and the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d cross(Vector3d o) {
		return this.set(y * o.z - z * o.y, z * o.x - x * o.z, x * o.y - y * o.x);
	}
	
	/**
	 * Sets this vector to the cross product between this vector and the given
	 * vector.
	 * 
	 * @param vx
	 *            the x component of the vector
	 * @param vy
	 *            the y component of the vector
	 * @param vz
	 *            the z component of the vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d cross(double vx, double vy, double vz) {
		return this.set(y * vz - z * vy, z * vx - x * vz, x * vy - y * vx);
	}
	
	/**
	 * Normalizes this vector. If the vector has zero length, this method has no
	 * effect.
	 * 
	 * @return reference to this vector for method chaining
	 */
	public Vector3d normalize() {
		double len = length();
		if (len != 0) {
			x /= len;
			y /= len;
			z /= len;
		}
		return this;
	}
	
	/**
	 * Flips the sign of all components of this vector.
	 * 
	 * @return reference to this vector for method chaining
	 */
	public Vector3d flip() {
		x = -x; y = -y; z = -z;
		return this;
	}
	
	/**
	 * Sets this vector to zero length.
	 */
	public Vector3d setZero() {
		x = y = z = 0;
		return this;
	}
	
	/**
	 * Test is this vector is exactly zero length.
	 * 
	 * @return {@code true} if this vector is zero length.
	 */
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	/**
	 * Rotates this vector about the x axis.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateX(double angle) {
		double cos = Math.cos(angle); double sin = Math.sin(angle);
		
		double t = y * cos - z * sin;
		z = y * sin + z * cos;
		y = t;
		
		return this;
	}
	
	/**
	 * Rotates this vector about the x axis.
	 * 
	 * @param angle
	 *            the angle in degrees
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateDegX(double angle) {
		return rotateX(Math.toRadians(angle));
	}
	
	/**
	 * Rotates this vector about the y axis.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateY(double angle) {
		double cos = Math.cos(angle); double sin = Math.sin(angle);
		
		double t = z * sin + x * cos;
		z = z * cos - x * sin;
		x = t;
		
		return this;
	}
	
	/**
	 * Rotates this vector about the y axis.
	 * 
	 * @param angle
	 *            the angle in degrees
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateDegY(double angle) {
		return rotateX(Math.toRadians(angle));
	}
	
	/**
	 * Rotates this vector about the z axis.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateZ(double angle) {
		double cos = Math.cos(angle); double sin = Math.sin(angle);
		
		double t = x * cos - y * sin;
		y = x * sin + y * cos; 
		x = t;
		
		return this;
	}
	
	/**
	 * Rotates this vector about the z axis.
	 * 
	 * @param angle
	 *            the angle in degrees
	 * @return reference to this vector for method chaining
	 */
	public Vector3d rotateDegZ(double angle) {
		return rotateX(Math.toRadians(angle));
	}
	
	/**
	 * Tests if this vector is a unit vector.
	 * 
	 * @return {@code true} if this vector is a unit vector
	 */
	public boolean isUnit() {
		return isUnit(EPSILON);
	}
	
	/**
	 * Tests if this vector is a unit vector.
	 * 
	 * @param e
	 *            error margin (epsilon) 
	 * @return {@code true} if this vector is a unit vector
	 */
	public boolean isUnit(double e) {
		return Math.abs(lengthSquared() - 1.0) < e;
	}
	
	/**
	 * Tests if this vector is equal with the specified vector. The comparison
	 * is made using the given tolerance value (epsilon) within the two vectors
	 * considered to be equal. The tolerance value is used for each dimension
	 * separately.
	 * 
	 * @param o
	 *            the other vector
	 * @param e
	 *            the tolerance value
	 * @return {@code true} if the two vectors are equal
	 */
	public boolean equals(Vector3d o, double e) {
		if (o == null) return false;
		if (Math.abs(o.x - x) > e) return false;
		if (Math.abs(o.y - y) > e) return false;
		if (Math.abs(o.z - z) > e) return false;
		return true;		
	}
	
    @Override
    public String toString() {
        return String.format("<%f, %f, %f>", x, y, z);
    }
	
}
