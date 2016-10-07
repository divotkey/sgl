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

public class Vector3d {
	
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
		this.x = x; 
		this.y = y;
		this.z = z;
		return this;
	}
	
	/**
	 * Sets this vector o the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d set(Vector3d o) {
		x = o.x; 
		y = o.y;
		z = o.z;
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
		x -= o.x;
		y -= o.y;
		z -= o.z;
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
		this.x -= x;
		this.y -= y;
		this.z -= z;
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
		x += o.x;
		y += o.y;
		z += o.z;
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
		this.x += x;
		this.y += y;
		this.z += z;
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
		x *= s;
		y *= s;
		z *= s;
		return this;
	}
	
	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param sx
	 *            x coordinate of the other vector
	 * @param sy
	 *            y coordinate of the other vector
	 * @param sz
	 *            z coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector3d scale(double sx, double sy, double sz) {
		x *= sx;
		y *= sy;
		z *= sz;
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
		x *= o.x;
		y *= o.y;
		z *= o.z;
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
		x += o.x * s;
		y += o.y * s;
		z += o.z * s;
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
	
    @Override
    public String toString() {
        return String.format("<%f, %f, %f>", x, y, z);
    }
	
}
