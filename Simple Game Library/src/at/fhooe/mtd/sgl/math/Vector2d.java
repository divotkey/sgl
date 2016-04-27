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
 * A 2 dimensional vector with double-precision.
 */
public final class Vector2d {

	/** A vector with zero length. */
	public static final Vector2d ZERO = new Vector2d(0, 0);
	
	/** The x coordinate. */
	public double x;
	
	/** The y coordinate. */
	public double y;
	
	
	/**
	 * Create a new vector with zero length. 
	 */
	public Vector2d() {
		// intentionally left empty
	}
	
	/**
	 * Creates a new vector with the given coordinates.
	 * 
	 * @param x
	 *            x coordinate of the new vector
	 * @param y
	 *            y coordinate of the new vector
	 */
	public Vector2d(double x, double y) {
		set(x, y);
	}

	/**
	 * Creates a new vector from the given vector (copy constructor).
	 * 
	 * @param o
	 *            the other vector
	 */
	public Vector2d(Vector2d o) {
		set(o);
	}
	
	/**
	 * Creates a copy of this vector.
	 * 
	 * @return a newly created instance with its values set to this vector
	 */
	public Vector2d copy() {
		return new Vector2d(this);
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
	 * Sets this vector to the given vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d set(double x, double y) {
		this.x = x; 
		this.y = y;
		return this;
	}

	/**
	 * Sets this vector o the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d set(Vector2d o) {
		x = o.x; 
		y = o.y;
		return this;
	}

	/**
	 * Returns the length of this vector.
	 * 
	 * @return the length of this vector
	 */
	public double length() {
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Returns the distance between this vector and the specified vector.
     * @param x
     *            the x coordinate of the other vector
     * @param y
     *            the y coordinate of the other vector
	 * @return the distance
	 */
	public double distance(double x, double y) {
	    return length(this.x - x, this.y - y);
	}
	
    /**
     * Returns the distance between this vector and the specified vector.
     * 
     * @param o
     *            the other vector
     * @return the distance
     */
	public double distance(Vector2d o) {
	    return distance(o.x, o.y);
	}
	
    /**
     * Returns the squared distance between this vector and the specified
     * vector.
     * 
     * @param x
     *            the x coordinate of the other vector
     * @param y
     *            the y coordinate of the other vector
     * @return the squared distance
     */
	public double distanceSquared(double x, double y) {
	    return lengthSquared(this.x - x, this.y - y);
	}
	
    /**
     * Returns the squared distance between this vector and the specified
     * vector.
     * 
     * @param o
     *            the other vector
     * @return the squared distance
     */
    public double distanceSquared(Vector2d o) {
        return distanceSquared(o.x, o.y);
    }
	
	/**
	 * Returns the length of the specified vector;
	 * 
     * @param x
     *            the x coordinate of the vector
     * @param y
     *            the y coordinate of the vector
	 * @return
	 */
    public static double length(double x, double y) {
        return Math.sqrt(x * x + y * y);
    }
	
    /**
     * Returns the length of the specified vector squared
     * 
     * @param x
     *            the x coordinate of the vector
     * @param y
     *            the y coordinate of the vector
     * @return
     */
    public static double lengthSquared(double x, double y) {
        return x * x + y * y;
    }
	
	/**
	 * Returns the length of this vector squared.
	 * 
	 * @return the squared length of this vector
	 */
	public double lengthSquared() {
		return x * x + y * y;
	}
	
	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param o
	 *            the vector to subtract
	 * @return reference to this vector for method chaining
	 */
	public Vector2d sub(Vector2d o) {
		x -= o.x;
		y -= o.y;
		return this;
	}

	/**
	 * Subtracts the given vector from this vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d sub(double x, double y) {
		this.x -= x;
		this.y -= y;
		return this;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param o
	 *            the vector to add
	 * @return reference to this vector for method chaining
	 */
	public Vector2d add(Vector2d o) {
		x += o.x;
		y += o.y;
		return this;
	}
	
	/**
	 * Adds the given vector to this vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d add(double x, double y) {
		this.x += x;
		this.y += y;
		return this;
	}

	/**
	 * Scales this vector by the given scalar value.
	 * 
	 * @param s
	 *            the scalar value
	 * @return reference to this vector for method chaining
	 */
	public Vector2d scale(double s) {
		x *= s;
		y *= s;
		return this;
	}

	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param sx
	 *            x coordinate of the other vector
	 * @param sy
	 *            y coordinate of the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d scale(double sx, double sy) {
		x *= sx;
		y *= sy;
		return this;
	}

	/**
	 * Scales this vector by the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return reference to this vector for method chaining
	 */
	public Vector2d scale(Vector2d o) {
		x *= o.x;
		y *= o.y;
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
	public Vector2d mulAdd(Vector2d o, double s) {
		x += o.x * s;
		y += o.y * s;
		return this;
	}

	/**
	 * Calculates the dot product between this vector and the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return the dot product between this vector and the other vector
	 */
	public double dot(Vector2d o) {
		return x * o.x + y * o.y;
	}
	
	/**
	 * Calculates the dot product between this vector and the given vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @return the dot product between this vector and the other vector
	 */
	public double dot(double x, double y) {
		return this.x * x + this.y * y;
	}

    /**
     * Calculates the dot product between the two specified vectors.
     * 
     * @param x1
     *            the x coordinate of the first vector
     * @param y1
     *            the y coordinate of the first vector
     * @param x2
     *            the x coordinate of the second vector
     * @param y2
     *            the y coordinate of the second vector
     * @return the dot product between the two specified vectors
     */
	public static double dot(double x1, double y1, double x2, double y2) {
	    return x1 * x2 + y1 * y2;
	}
	
	/**
	 * Normalizes this vector. If the vector has zero length, this method has no
	 * effect.
	 * 
	 * @return reference to this vector for method chaining
	 */
	public Vector2d normalize() {
		double len = length();
		if (len != 0) {
			x /= len;
			y /= len;
		}
		return this;
	}
	
	/**
	 * Rotates the vector by the given angle counter-clockwise.
	 * 
	 * @param phi
	 *            the angle in radians
	 * @return reference to this vector for method chaining
	 */
	public Vector2d rotate(double phi) {
		double cosa = Math.cos(phi);
		double sina = Math.sin(phi);
		
		double xt = this.x * cosa - this.y * sina;
		this.y = this.y * cosa + this.x * sina;
		this.x = xt;
		return this;
	}

    /**
     * Sets this vector to perpendicular version of itself.
     * 
     * @return reference to this vector for method chaining
     */
	public Vector2d perpendicularize() {
		double tmp = x;
		x = -y;
		y = tmp;
		return this;
	}

	/**
	 * Sets this vector to zero length.
	 */
	public void setZero() {
		x = y = 0;
	}

    @Override
    public String toString() {
        return String.format("<%f, %f>", x, y);
    }
	
}
