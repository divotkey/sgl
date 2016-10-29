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
 * A four dimensional quaternion. Quaternions are commonly used to represent
 * rotations in three-dimensional space.
 * 
 * @see <a href=
 *      "http://sjbrown.co.uk/2002/05/01/representing-rotations-in-quaternion-arithmetic/">Representing
 *      Rotations in Quaternion Arithmetic</a>
 */
public final class Quaternion {

	/** Used for tests to avoid precision error. */
	private static final double EPSILON = 0.000000001;

	/** Used to store a quaternion temporarily. Method that use this are not thread-safe. */
	private static Quaternion tmp1 = new Quaternion();
	
	/** Used to store a quaternion temporarily. Method that use this are not thread-safe. */
	private static Quaternion tmp2 = new Quaternion();

	/** The w-component of this quaternion. */
	public double w;
	
	/** The x-component of this quaternion. */
	public double x;
	
	/** The y-component of this quaternion. */
	public double y;
	
	/** The z-component of this quaternion. */
	public double z;
	
	
	/**
	 * Creates a new instance. The new instance will be set to identity.
	 */
	public Quaternion() {
	    setToIdentity();
	}
	
	/**
	 * Creates a new instance with all components copied from the specified
	 * quaternion.
	 * 
	 * @param o
	 *            the other quaternion
	 */
	public Quaternion(Quaternion o) {
		set(o);
	}
	
	/**
	 * Creates a new instance with the specified components.
	 * 
	 * @param w
	 *            the w-component
	 * @param x
	 *            the x-component
	 * @param y
	 *            the y-component
	 * @param z
	 *            the z-component
	 */
	public Quaternion(double w, double x, double y, double z) {
		set(w, x, y, z);
	}
	
	/**
	 * Returns a copy of this quaternion.
	 * 
	 * @return the newly created quaternion
	 */
	public Quaternion copy() {
		return new Quaternion(this);
	}
			
	/**
	 * Sets this quaternion by the specified components.
	 * @param w
	 *            the w-component
	 * @param x
	 *            the x-component
	 * @param y
	 *            the y-component
	 * @param z
	 *            the z-component
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion set(double w, double x, double y, double z) {
		this.w = w; this.x = x; this.y = y; this.z = z;
		return this;
	}
	
	/**
	 * Sets this quaternion to the specified w-component and axis vector.
	 * 
	 * @param w
	 *            the w-component
	 * @param axis
	 *            the axis vector
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion set(double w, Vector3d axis) {
		this.w = w; this.x = axis.x; this.y = axis.y; this.z = axis.z;
		return this;
	}
	
	/**
	 * Sets this quaternion to be equal with the specified quaternion.
	 * 
	 * @param o
	 *            the other quaternion
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion set(Quaternion o) {
		w = o.w; x = o.x; y = o.y; z = o.z;
		return this;
	}
	
	/**
	 * Returns the squared length of this quaternion.
	 * 
	 * @return the length^2
	 */
	public double lengthSquared() {
		return w * w + x * x + y * y + z * z;
	}
	
	/**
	 * Returns the length of this quaternion.
	 * @return the length
	 */
	public double length() {
		return Math.sqrt(w * w + x * x + y * y + z * z);
	}
	
	/**
	 * Adds the specified quaternion to this quaternion.
	 * 
	 * @param o
	 *            the quaternion to add
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion add(Quaternion o) {
		w += o.w; x += o.x; y += o.y; z += o.z;
		return this;
	}
	
	/**
	 * Subtracts the specified quaternion from this quaternion.
	 * 
	 * @param o
	 *            the quaternion to subtract
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion sub(Quaternion o) {
		w -= o.w; x -= o.x; y -= o.y; z -= o.z;
		return this;
	}
	
	/**
	 * Sets the length of this quaternion to one. If this quaternion has zero
	 * length, this method has no effect.
	 * <p>
	 * This method avoid unnecessary calculation if the quaternion is already of
	 * length one or zero.
	 * </p>
	 * 
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion normalizeLazy() {
		double l2 = lengthSquared();
		if (l2 != 0.0 && !MathUtils.isEqual(l2, 1.0, EPSILON)) {
			double l = Math.sqrt(l2);
			w /= l;
			x /= l;
			y /= l;
			z /= l;
		}
		return this;
	}
	
    /**
     * Multiplies each component of this quaternion with the given scalar
     * in-place.
     * 
     * @param s
     *            the scalar value
     * @return a reference to this quaternion for method chaining
     */
	public Quaternion mul(double s) {
	    w *= s; x *= s; y *= s; z *= s; 
	    return this;
	}
	
	/**
	 * Multiplies this quaternion with the specified quaternion in-place (this = this * o). 
	 * 
	 * @param o
	 *            the other quaternion to multiply
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion mul(Quaternion o) {
		double x = this.w * o.x + this.x * o.w + this.y * o.z - this.z * o.y;
		double y = this.w * o.y + this.y * o.w + this.z * o.x - this.x * o.z;
		double z = this.w * o.z + this.z * o.w + this.x * o.y - this.y * o.x;
		double w = this.w * o.w - this.x * o.x - this.y * o.y - this.z * o.z;

		this.x = x; this.y = y; this.z = z; this.w = w;
		return this;
	}
	
	/**
	 * Multiplies this quaternion with the specified quaternion in-place (this = o * this). 
	 * 
	 * @param o
	 *            the other quaternion to multiply
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion preMul(Quaternion other) {
		double x = other.w * this.x + other.x * this.w + other.y * this.z - other.z * this.y;
		double y = other.w * this.y + other.y * this.w + other.z * this.x - other.x * this.z;
		double z = other.w * this.z + other.z * this.w + other.x * this.y - other.y * this.x;
		double w = other.w * this.w - other.x * this.x - other.y * this.y - other.z * this.z;
		this.x = x; this.y = y; this.z = z; this.w = w;
		return this;
	}
	
	/**
	 * Sets this quaternion to the identity quaternion.
	 * 
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion setToIdentity() {
		return set(1, 0, 0, 0);
	}
	
	/**
	 * Sets this quaternion to the given euler angles.
	 * 
	 * @param yaw
	 *            rotation about the y axis in radians
	 * @param pitch
	 *            rotation about the x axis in radians
	 * @param roll
	 *            rotation about the z axis in radians
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion setToRotation(double yaw, double pitch, double roll) {
		double hr = roll * 0.5f, shr = Math.sin(hr), chr =  Math.cos(hr);
		
		double hp = pitch * 0.5f, shp = Math.sin(hp),        chp = Math.cos(hp);
		double hy = yaw * 0.5f,   shy = (float)Math.sin(hy), chy = Math.cos(hy);
		
		double chy_shp = chy * shp;
		double shy_chp = shy * chp;
		double chy_chp = chy * chp;
		double shy_shp = shy * shp;

		w = (chy_chp * chr) + (shy_shp * shr);
		x = (chy_shp * chr) + (shy_chp * shr);
		y = (shy_chp * chr) - (chy_shp * shr); 
		z = (chy_chp * shr) - (shy_shp * chr); 

		return this;
	}
	
	/**
	 * Sets this quaternion to the given Euler angles. Calling this method with
	 * an vector v is equivalent to calling
	 * {@code setToRotation(v.y, v.x, v.z)};
	 * 
	 * @param angles
	 *            a vector holding the Euler angles
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion setToRotation(Vector3d angles) {
		return setToRotation(angles.y, angles.x, angles.z);
	}
	
	/**
	 * Extracts the Euler angles from this quaternion.
	 * 
	 * @param result
	 *            the vector where to store the angles
	 * @return reference to the result vector
	 */
	public Vector3d getEulerAngles(Vector3d result) {
		return result.set(getYaw(), getPitch(), getRoll());
	}
	
	/**
	 * Sets the length of this quaternion to one. If this quaternion has zero length, this method has no effect.
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion normalize() {
		double lng = length();
		if (lng != 0) {
			w /= lng;
			x /= lng;
			y /= lng;
			z /= lng;
		}
		return this;
	}
		
	private int getGimbalPole () {
		double t = y * x + z * w;
		return t > 0.499 ? 1 : (t < -0.499 ? -1 : 0);
	}	
	
	/**
	 * Returns the rotation about the x axis.
	 * 
	 * @return the angle in radians
	 */
	public double getPitch () {
		int pole = getGimbalPole();
		return pole == 0 ? Math.asin(MathUtils.clamp(2 * (w * x - z * y), -1.0, 1.0)) : pole * Math.PI * 0.5;
	}	
	
	/**
	 * Returns the rotation about the y axis.
	 * 
	 * @return the angle in radians
	 */
	public double getYaw () {
		return getGimbalPole() == 0 ? Math.atan2(2.0 * (y * w + x * z), 1.0 - 2.0 * (y * y + x * x)) : 0.0;
	}	
	
	/**
	 * Returns the rotation about the z axis.
	 * 
	 * @return the angle in radians
	 */
	public double getRoll () {
		int pole = getGimbalPole();
		return pole == 0 ? Math.atan2(2.0 * (w * z + y * x), 1.0 - 2.0 * (x * x + z * z))
				: pole * 2.0 * Math.atan2(y, w);
	}	
	
	/**
	 * Conjugates this quaternion.
	 * 
	 * @return a reference to this quaternion for method chaining
	 */
	public Quaternion conjugate() {
		x = -x; y = -y; z = -z;
		return this;
	}
	
	/**
	 * Transforms (rotates) the specified vector by this quaternion. The result
	 * is stored in the given vector.
	 * <p>
	 * v' = q * v * q<sup>-1</sup>
	 * </p>
	 * 
	 * @param v
	 *            the vector to transform
	 * @return reference to the transformed input vector
	 */
	public Vector3d transform(Vector3d v) {
		tmp2.set(this).conjugate().preMul(tmp1.set(0, v)).preMul(this);		
		v.set(tmp2.x, tmp2.y, tmp2.z);
		return v;
	}
	
	/**
	 * Transforms (rotates) the specified vector by this quaternion. The input
	 * vector and the result vector can be identical.
	 * <p>
	 * v' = q * v * q<sup>-1</sup>
	 * </p>
	 * 
	 * @param v
	 *            the vector to transform
	 * @param result
	 *            the result vector used to store the result
	 * @return reference to the result vector
	 */
	public Vector3d transform(Vector3d v, Vector3d result) {
		tmp2.set(this).conjugate().preMul(tmp1.set(0, v)).preMul(this);		
		result.set(tmp2.x, tmp2.y, tmp2.z);
		return result;
	}

	/**
	 * Transforms (rotates) the specified vector by the inverse of this
	 * quaternion. The input vector and the result vector can be identical.
	 * 
	 * <p>
	 * v' = q<sup>-1</sup> * v * q
	 * </p>
	 * 
	 * @param v
	 *            the vector to transform
	 * @param result
	 *            the result vector used to store the result
	 * @return reference to the result vector
	 */
	public Vector3d transformInverse(Vector3d v, Vector3d result) {
		tmp2.set(this).conjugate().mul(tmp1.set(0, v)).mul(this);		
		result.set(tmp2.x, tmp2.y, tmp2.z);
		return result;
	}
	
	/**
	 * Transforms (rotates) the specified vector by the inverse of this
	 * quaternion. The result is stored in the given vector.
	 * 
	 * <p>
	 * v' = q<sup>-1</sup> * v * q
	 * </p>
	 * 
	 * @param v
	 *            the vector to transform
	 * @return reference to the result vector
	 */
	public Vector3d transformInverse(Vector3d v) {
		tmp2.set(this).conjugate().mul(tmp1.set(0, v)).mul(this);		
		v.set(tmp2.x, tmp2.y, tmp2.z);
		return v;
	}
	
    @Override
    public String toString() {
        return String.format("<%f, %f, %f, %f>", w, x, y, z);
    }
	
}
