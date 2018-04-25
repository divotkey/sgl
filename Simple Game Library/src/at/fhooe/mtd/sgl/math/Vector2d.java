/*******************************************************************************
 * Copyright (c) 2016 - 2018 Roman Divotkey,
 * Univ. of Applied Sciences Upper Austria. 
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
 * A two-dimensional vector with double-precision.
 * <p>
 * These vector classes are optimized to avoid repetitive memory allocation.
 * This means, that almost all methods modify the vector
 * <strong>in-place</strong>, meaning the vector itself is changed. In order to
 * make it easy to carry out several operations at once without staring a new
 * statement, most methods <strong>return a this reference</strong>, which is
 * often <strong>mistaken as the a result vector</strong>.
 * 
 * <h3>Example</h3> Let's assume we want to add two vectors and store the result
 * in a third vector. So we have the two vectors to add, we call them
 * {@code v1}, and {@code v2} and the result vector {@code v3}. The code
 * sequence for the operation v3 = v1 + v2 would look like this:
 * 
 * <pre>
 * Vector2d v1 = new Vector(1, 2);
 * Vector2d v2 = new Vector(3, 4);
 * Vector2d v3 = new Vector();
 * 
 * v3.set(v1).add(v2);
 * </pre>
 * 
 * First we assign the vector v2 to the result vector v3 and then we add the
 * vector v3. Both vectors v1 and v2 are untouched, v3 carries the result.
 * 
 * To make it more illustrative what the code actually does, we can rewrite the
 * code sequence like this:
 * 
 * <pre>
 * Vector2d v1 = new Vector(1, 2);
 * Vector2d v2 = new Vector(3, 4);
 * Vector2d v3 = new Vector();
 *
 * v3.set(v1);
 * v3.add(v2);
 * </pre>
 * 
 * This code does exactly the same as the first version, however requires two
 * statements to carry out the operation. This is the reason, why methods like
 * {@code add}, {@code sub}, {@code scale}, {@code muladd}, {@code cross} etc.
 * return a this reference of its vector.
 *
 * <h3>Unintended alteration</h3> The following code demonstrates a tricky
 * situation where an unintended alteration of source vectors could take place.
 * Let's assume we have a method that requires one vector as parameter
 * {@code foo} and we have another method that takes two vectors as parameter
 * {@code bar}. Now we want to call {@code foo} from {@code bar} with the
 * addition of parameter {@code v1} and {@code v2}, meaning we want to call
 * <em>foo(v1 + v2)</em>.
 * 
 * <pre>
 * void foo(Vector2d v) {
 * 	// does something with v
 * }
 * 
 * void bar(Vector2d v1, Vector2d v2) {
 * 	foo(v1.add(v2)); // unintended alteration of vector v1!
 * }
 * </pre>
 * 
 * This code would modify vector {@code v1}, which is probably not what we want.
 * The next example solves this problem and shows several versions of method
 * {@code bar}, which do basically all the same, they use a temporary vector
 * instance to store the result of the <em>v1 + v2</em> operation:
 * 
 * <pre>
 * class MyClass {
 * 
 *     // Auxiliary vector used to avoid repetitive memory allocation.
 *     Vector2d auxVec = new Vector2d();
 * 
 *     void foo(Vector3d v) {
 *         // does something with v
 *     }
 * 
 *     void bar1(Vector2d v1, Vector2d v2) {
 *         foo(v1.copy().add(v2));
 *     }
 * 
 *     void bar2(Vector2d v1, Vector2d v2) {
 *         foo(new Vector2d(v1).add(v2));
 *     }
 * 
 *     void bar3(Vector2d v1, Vector2d v2) {
 *         Vector3d tmp = new Vector3d();
 *         foo(tmp.set(v1).add(v2));
 *     }
 *     
 *     void bar4(Vector2d v1, Vector2d v2) {
 *         foo(auxVec.set(v1).add(v2));
 *     }
 * </pre>
 * 
 * The last version {@code bar4} if probably the best solution if the operation
 * is executed quite a lot (e.g. within the game loop), because it avoid the
 * allocation (and destruction) of a temporary instance of a vector object.
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
	 * Returns the vector's component with the specified index.
	 * 
	 * @param idx
	 *            the index of the component of this vector
	 * @return the requested value
	 */
	public double get(int idx) {
		switch (idx) {
		case 0: 
			return x;
		case 1: 
			return y;
		default:	
			throw new IndexOutOfBoundsException("index must be between 0 and 2, got " + idx);
		}
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
		this.x = x; this.y = y;
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
		x = o.x; y = o.y;
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
	public Vector2d set(double[] src) throws IndexOutOfBoundsException{
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
	public Vector2d set(double[] src, int pos) throws IndexOutOfBoundsException {
		x = src[pos]; y = src[pos + 1];
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
	 * Calculates the angle of this vector relative to the given vector. Angles
	 * are towards the positive y-axis.
	 * 
	 * @param o
	 *            the other vector
	 * @return the angle between this vector and the given vector in radians
	 */
	public double angle(Vector2d o) {
		return Math.atan2(cross(o), dot(o));
	}	
	
	/**
	 * Calculates the angle of this vector relative to the given vector. Angles are
	 * towards the positive y-axis.
	 * 
	 * @param vx
	 *            x-coordinate of the other vector
	 * @param vy
	 *            y-coordinate of the other vector
	 * @return the angle between this vector and the given vector in radians
	 */
	public double angle(double vx, double vy) {
		return Math.atan2(cross(vx, vy), dot(vx, vy));
	}
	
	/**
	 * Calculates the 2d cross product between this vector and the given vector.
	 * 
	 * @param x
	 *            the x coordinate of the other vector
	 * @param y
	 *            the y coordinate of the other vector
	 * @return the 2d cross product between this vector and the other vector
	 */
	public double cross(double x, double y) {
		return this.x * y - this.y * x;
	}
	
	/**
	 * Calculates the 2d cross product between this vector and the given vector.
	 * 
	 * @param o
	 *            the other vector
	 * @return the 2d cross  product between this vector and the other vector
	 */
	public double cross(Vector2d o) {
		return x * o.y - y * o.x;		
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
	 * @return the length of the specified vector
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
     * @return the length of the specified vector
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
	 *            the scaling factor of the x-component
	 * @param sy
	 *            the scaling factor of the y-component
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
		x += o.x * s; y += o.y * s;
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
	public Vector2d mulAdd(Vector2d o, Vector3d sv) {
		x += o.x * sv.x; y += o.y * sv.y;
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
	 * 
	 * @return reference to this vector for method chaining
	 */
	public Vector2d setZero() {
		x = y = 0;
		return this;
	}

    @Override
    public String toString() {
        return String.format("<%f, %f>", x, y);
    }
	
}
