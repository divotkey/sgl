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
 * A 4x4 Matrix. The matrix is organized in row-major order.
 */
public final class Matrix4d {

	/** Used to avoid repetitive memory allocation.*/
	private static final Matrix4d tmp = new Matrix4d();
	
	/** The elements first row. */
    public	double	m00, m01, m02, m03;
    
	/** The elements second row. */
    public	double	m10, m11, m12, m13;
    
	/** The elements third row. */
    public	double	m20, m21, m22, m23;
    
	/** The elements fourth row. */
    public	double	m30, m31, m32, m33;
    
    /**
     * Creates a new instance initialized to zero.
     */
    public Matrix4d() {
    	// intentionally left empty
    }

	/**
	 * Creates a new instance initialized with the values of the given matrix.
	 * 
	 * @param o
	 *            the matrix from witch to copy the values
	 */
    public Matrix4d(Matrix4d o) {
    	set(o);
    }

	/**
	 * Sets this matrix to the given matrix.
	 * 
	 * @param o
	 *            the matrix from witch to copy the values
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d set(Matrix4d o) {
    	m00 = o.m00; m01 = o.m01; m02 = o.m02; m03 = o.m03;
    	m10 = o.m10; m11 = o.m11; m12 = o.m12; m13 = o.m13;
    	m20 = o.m20; m21 = o.m21; m22 = o.m22; m23 = o.m23;
    	m30 = o.m30; m31 = o.m31; m32 = o.m32; m33 = o.m33;
    	return this;
    }

	/**
	 * Sets this matrix to the transposed of the specified matrix.
	 * 
	 * @param m
	 *            the other matrix
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setTransposed(Matrix4d o) {
    	m00 = o.m00; m01 = o.m10; m02 = o.m20; m03 = o.m30;
    	m10 = o.m01; m11 = o.m11; m12 = o.m21; m13 = o.m31;
    	m20 = o.m02; m21 = o.m12; m22 = o.m22; m23 = o.m32;
    	m30 = o.m03; m31 = o.m13; m32 = o.m23; m33 = o.m33;
    	return this;
    }

    public Matrix4d setInverted(Matrix4d o) {
		double det = o.m30 * o.m21 * o.m12 * o.m03 - o.m20 * o.m31 * o.m12 * o.m03 - o.m30 * o.m11
				* o.m22 * o.m03 + o.m10 * o.m31 * o.m22 * o.m03 + o.m20 * o.m11 * o.m32 * o.m03 - o.m10
				* o.m21 * o.m32 * o.m03 - o.m30 * o.m21 * o.m02 * o.m13 + o.m20 * o.m31 * o.m02 * o.m13
				+ o.m30 * o.m01 * o.m22 * o.m13 - o.m00 * o.m31 * o.m22 * o.m13 - o.m20 * o.m01 * o.m32
				* o.m13 + o.m00 * o.m21 * o.m32 * o.m13 + o.m30 * o.m11 * o.m02 * o.m23 - o.m10 * o.m31
				* o.m02 * o.m23 - o.m30 * o.m01 * o.m12 * o.m23 + o.m00 * o.m31 * o.m12 * o.m23 + o.m10
				* o.m01 * o.m32 * o.m23 - o.m00 * o.m11 * o.m32 * o.m23 - o.m20 * o.m11 * o.m02 * o.m33
				+ o.m10 * o.m21 * o.m02 * o.m33 + o.m20 * o.m01 * o.m12 * o.m33 - o.m00 * o.m21 * o.m12
				* o.m33 - o.m10 * o.m01 * o.m22 * o.m33 + o.m00 * o.m11 * o.m22 * o.m33;
		
		if (det == 0f)  {
			throw new RuntimeException("matrix is not invertible");
		}
		
		double inv_det = 1.0f / det;

		m00 = (o.m12 * o.m23 * o.m31 - o.m13 * o.m22 * o.m31 + o.m13 * o.m21 * o.m32 - o.m11 * o.m23 * o.m32
				- o.m12 * o.m21 * o.m33 + o.m11 * o.m22 * o.m33) * inv_det;
		m01 = (o.m03 * o.m22 * o.m31 - o.m02 * o.m23 * o.m31 - o.m03 * o.m21 * o.m32 + o.m01 * o.m23 * o.m32
				+ o.m02 * o.m21 * o.m33 - o.m01 * o.m22 * o.m33) * inv_det;
		m02 = (o.m02 * o.m13 * o.m31 - o.m03 * o.m12 * o.m31 + o.m03 * o.m11 * o.m32 - o.m01 * o.m13 * o.m32
				- o.m02 * o.m11 * o.m33 + o.m01 * o.m12 * o.m33) * inv_det;
		m03 = (o.m03 * o.m12 * o.m21 - o.m02 * o.m13 * o.m21 - o.m03 * o.m11 * o.m22 + o.m01 * o.m13 * o.m22
				+ o.m02 * o.m11 * o.m23 - o.m01 * o.m12 * o.m23) * inv_det;
		m10 = (o.m13 * o.m22 * o.m30 - o.m12 * o.m23 * o.m30 - o.m13 * o.m20 * o.m32 + o.m10 * o.m23 * o.m32
				+ o.m12 * o.m20 * o.m33 - o.m10 * o.m22 * o.m33) * inv_det;
		m11 = (o.m02 * o.m23 * o.m30 - o.m03 * o.m22 * o.m30 + o.m03 * o.m20 * o.m32 - o.m00 * o.m23 * o.m32
				- o.m02 * o.m20 * o.m33 + o.m00 * o.m22 * o.m33) * inv_det;
		m12 = (o.m03 * o.m12 * o.m30 - o.m02 * o.m13 * o.m30 - o.m03 * o.m10 * o.m32 + o.m00 * o.m13 * o.m32
				+ o.m02 * o.m10 * o.m33 - o.m00 * o.m12 * o.m33) * inv_det;
		m13 = (o.m02 * o.m13 * o.m20 - o.m03 * o.m12 * o.m20 + o.m03 * o.m10 * o.m22 - o.m00 * o.m13 * o.m22
				- o.m02 * o.m10 * o.m23 + o.m00 * o.m12 * o.m23) * inv_det;
		m20 = (o.m11 * o.m23 * o.m30 - o.m13 * o.m21 * o.m30 + o.m13 * o.m20 * o.m31 - o.m10 * o.m23 * o.m31
				- o.m11 * o.m20 * o.m33 + o.m10 * o.m21 * o.m33) * inv_det;
		m21 = (o.m03 * o.m21 * o.m30 - o.m01 * o.m23 * o.m30 - o.m03 * o.m20 * o.m31 + o.m00 * o.m23 * o.m31
				+ o.m01 * o.m20 * o.m33 - o.m00 * o.m21 * o.m33) * inv_det;
		m22 = (o.m01 * o.m13 * o.m30 - o.m03 * o.m11 * o.m30 + o.m03 * o.m10 * o.m31 - o.m00 * o.m13 * o.m31
				- o.m01 * o.m10 * o.m33 + o.m00 * o.m11 * o.m33) * inv_det;
		m23 = (o.m03 * o.m11 * o.m20 - o.m01 * o.m13 * o.m20 - o.m03 * o.m10 * o.m21 + o.m00 * o.m13 * o.m21
				+ o.m01 * o.m10 * o.m23 - o.m00 * o.m11 * o.m23) * inv_det;
		m30 = (o.m12 * o.m21 * o.m30 - o.m11 * o.m22 * o.m30 - o.m12 * o.m20 * o.m31 + o.m10 * o.m22 * o.m31
				+ o.m11 * o.m20 * o.m32 - o.m10 * o.m21 * o.m32) * inv_det;
		m31 = (o.m01 * o.m22 * o.m30 - o.m02 * o.m21 * o.m30 + o.m02 * o.m20 * o.m31 - o.m00 * o.m22 * o.m31
				- o.m01 * o.m20 * o.m32 + o.m00 * o.m21 * o.m32) * inv_det;
		m32 = (o.m02 * o.m11 * o.m30 - o.m01 * o.m12 * o.m30 - o.m02 * o.m10 * o.m31 + o.m00 * o.m12 * o.m31
				+ o.m01 * o.m10 * o.m32 - o.m00 * o.m11 * o.m32) * inv_det;
		m33 = (o.m01 * o.m12 * o.m20 - o.m02 * o.m11 * o.m20 + o.m02 * o.m10 * o.m21 - o.m00 * o.m12 * o.m21
				- o.m01 * o.m10 * o.m22 + o.m00 * o.m11 * o.m22) * inv_det;
		
    	return this;    	
    }
    
	/**
	 * Sets this matrix to the specified values.
	 * 
	 * @param m00
	 *            first element of first row
	 * @param m01
	 *            second element of first row
	 * @param m02
	 *            third element of first row
	 * @param m03
	 *            fourth element of first row
	 * @param m10
	 *            first element of second row
	 * @param m11
	 *            second element of second row
	 * @param m12
	 *            third element of second row
	 * @param m13
	 *            fourth element of second row
	 * @param m20
	 *            first element of third row
	 * @param m21
	 *            second element of third row
	 * @param m22
	 *            third element of third row
	 * @param m23
	 *            fourth element of third row
	 * @param m30
	 *            first element of fourth row
	 * @param m31
	 *            second element of fourth row
	 * @param m32
	 *            third element of fourth row
	 * @param m33
	 *            fourth element of fourth row
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d set(double m00, double m01, double m02, double m03,
    					double m10, double m11, double m12, double m13,
    					double m20, double m21, double m22, double m23,
    					double m30, double m31, double m32, double m33) {

    	this.m00 = m00; this.m01 = m01; this.m02 = m02; this.m03 = m03;
    	this.m10 = m10; this.m11 = m11; this.m12 = m12; this.m13 = m13;
    	this.m20 = m20; this.m21 = m21; this.m22 = m22; this.m23 = m23;
    	this.m30 = m30; this.m31 = m31; this.m32 = m32; this.m33 = m33;
    	return this;
    }
    
	/**
	 * This this matrix to identity.
	 * 
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToIdentity() {
    	m00 = 1; m01 = 0; m02 = 0; m03 = 0;
    	m10 = 0; m11 = 1; m12 = 0; m13 = 0;
    	m20 = 0; m21 = 0; m22 = 1; m23 = 0;
    	m30 = 0; m31 = 0; m32 = 0; m33 = 1;
    	return this;
    }

	/**
	 * Sets this matrix to a translate matrix with the specified translation.
	 * 
	 * @param tx
	 *            the distance to translate in the x-axis direction
	 * @param ty
	 *            the distance to translate in the y-axis direction
	 * @param tz
	 *            the distance to translate in the z-axis direction
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToTranslate(double tx, double ty, double tz) {
    	m00 = 1; m01 = 0; m02 = 0; m03 = tx;
    	m10 = 0; m11 = 1; m12 = 0; m13 = ty;
    	m20 = 0; m21 = 0; m22 = 1; m23 = tz;
    	m30 = 0; m31 = 0; m32 = 0; m33 = 1;
    	return this;
    }
    
	/**
	 * Sets this matrix to a translate matrix with the specified translation.
	 * 
	 * @param v
	 *            the translation vector
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToTranslate(Vector3d v) {
    	return setToTranslate(v.x, v.y, v.z);
    }

	/**
	 * Sets this matrix to a scale matrix with the specified scale factors.
	 * 
	 * @param sx
	 *            scale factor for the x-axis
	 * @param sy
	 *            scale factor for the y-axis
	 * @param sz
	 *            scale factor for the z-axis
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToScale(double sx, double sy, double sz) {
    	m00 = sx; m01 = 0;  m02 = 0;  m03 = 0;
    	m10 = 0;  m11 = sy; m12 = 0;  m13 = 0;
    	m20 = 0;  m21 = 0;  m22 = sz; m23 = 0;
    	m30 = 0;  m31 = 0;  m32 = 0;  m33 = 1;
    	return this;
    }
    
	/**
	 * Sets this matrix to a scale matrix with the specified scale factors.
	 * 
	 * @param v
	 *            the vector containing the scale factors
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToScale(Vector3d v) {
    	return setToScale(v.x, v.y, v.z);
    }
    
	/**
	 * Sets this matrix to a scale matrix with the specified scale factor.
	 * 
	 * @param s
	 *            the scale factor used for all dimensions
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToScale(double s) {
    	return setToScale(s, s, s);
    }
    
	/**
	 * Sets this matrix to a rotation matrix rotating about the x-axis by the
	 * specified angle.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToRotateX(double angle) {
    	double sin = Math.sin(angle); double cos = Math.cos(angle);
    	m00 = 1; m01 = 0;   m02 = 0;    m03 = 0;
    	m10 = 0; m11 = cos; m12 = -sin; m13 = 0;
    	m20 = 0; m21 = sin; m22 = cos;  m23 = 0;
    	m30 = 0; m31 = 0;   m32 = 0;    m33 = 1;
    	return this;
    }

	/**
	 * Sets this matrix to a rotation matrix rotating about the y-axis by the
	 * specified angle.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToRotateY(double angle) {
    	double sin = Math.sin(angle); double cos = Math.cos(angle);
    	m00 = cos;  m01 = 0; m02 = sin; m03 = 0;
    	m10 = 0;    m11 = 1; m12 = 0;   m13 = 0;
    	m20 = -sin; m21 = 0; m22 = cos; m23 = 0;
    	m30 = 0;    m31 = 0; m32 = 0;   m33 = 1;
    	return this;
    }
    
	/**
	 * Sets this matrix to a rotation matrix rotating about the z-axis by the
	 * specified angle.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d setToRotateZ(double angle) {
    	double sin = Math.sin(angle); double cos = Math.cos(angle);
    	m00 = cos; m01 = -sin; m02 = 0; m03 = 0;
    	m10 = sin; m11 = cos;  m12 = 0; m13 = 0;
    	m20 = 0;   m21 = 0;    m22 = 1; m23 = 0;
    	m30 = 0;   m31 = 0;    m32 = 0; m33 = 1;
    	return this;
    }
    
    public Matrix4d set(Vector3d t, Quaternion q) {
    	return set(t.x, t.y, t.z, q.w, q.x, q.y, q.z);
    }
    
    public Matrix4d set(double tx, double ty, double tz, double qw, double qx, double qy, double qz) {
		double xs = qx * 2,  ys = qy * 2,  zs = qz * 2;
		double wx = qw * xs, wy = qw * ys, wz = qw * zs;
		double xx = qx * xs, xy = qx * ys, xz = qx * zs;
		double yy = qy * ys, yz = qy * zs, zz = qz * zs;

		m00 = (1.0f - (yy + zz));
		m01 = (xy - wz);
		m02 = (xz + wy);
		m03 = tx;

		m10 = (xy + wz);
		m11 = (1.0f - (xx + zz));
		m12 = (yz - wx);
		m13 = ty;

		m20 = (xz - wy);
		m21 = (yz + wx);
		m22 = (1.0f - (xx + yy));
		m23 = tz;

		m30 = 0.f;
		m31 = 0.f;
		m32 = 0.f;
		m33 = 1.0f;
		
		return this;
    }
    
    public Matrix4d setToRotate(double qw, double qx, double qy, double qz) {
    	return set(0, 0, 0, qw, qx, qy, qz);
    }
    
    public Matrix4d setToRotate(Quaternion q) {
    	return set(0, 0, 0, q.w, q.x, q.y, q.z);
    }
    
	/**
	 * Multiplies this matrix with a translate matrix with the specified
	 * translation.
	 * 
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param tx
	 *            the distance to translate in the x-axis direction
	 * @param ty
	 *            the distance to translate in the y-axis direction
	 * @param tz
	 *            the distance to translate in the z-axis direction
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d translate(double tx, double ty, double tz) {
    	return mul(tmp.setToTranslate(tx, ty, tz));
    }
    
	/**
	 * Multiplies this matrix with a translate matrix with the specified
	 * translation.
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param v
	 *            the translation vector
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d translate(Vector3d v) {
    	return mul(tmp.setToTranslate(v));
    }
    
	/**
	 * Multiplies this matrix with a scale matrix with the specified scaling
	 * factors.
	 * 
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param sx
	 *            the distance to translate in the x-axis direction
	 * @param sy
	 *            the distance to translate in the y-axis direction
	 * @param sz
	 *            the distance to translate in the z-axis direction
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d scale(double sx, double sy, double sz) {
    	return mul(tmp.setToScale(sx, sy, sz));
    }
    
	/**
	 * Multiplies this matrix with a scale matrix with the specified scaling
	 * factors.
	 * 
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param v
	 *            the vector containing the scale factors
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d scale(Vector3d v) {
    	return mul(tmp.setToScale(v));
    }
    
	/**
	 * Multiplies this matrix with a scale matrix with the specified scaling
	 * factor.
	 * 
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param s
	 *            the scale factor used for all dimensions
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d scale(double s) {
    	return mul(tmp.setToScale(s));
    }
    
	/**
	 * Multiplies this matrix with a rotation matrix rotating about the x-axis.
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d rotateX(double angle) {
    	return mul(tmp.setToRotateX(angle));
    }

	/**
	 * Multiplies this matrix with a rotation matrix rotating about the y-axis.
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d rotateY(double angle) {
    	return mul(tmp.setToRotateY(angle));
    }
    
	/**
	 * Multiplies this matrix with a rotation matrix rotating about the z-axis.
	 * <p>
	 * <strong>Note:</strong> This method is not thread-safe.
	 * </p>
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d rotateZ(double angle) {
    	return mul(tmp.setToRotateZ(angle));
    }
    
	/**
	 * Transposes this matrix.
	 * 
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d transpose() {
    	setTransposed(tmp.set(this));
		return this;
    }
    
    /**
     * Inverts this matrix;
     * 
	 * @return a reference to this matrix for method chaining
     */
    public Matrix4d inverse() {
    	
		double det = m30 * m21 * m12 * m03 - m20 * m31 * m12 * m03 - m30 * m11
				* m22 * m03 + m10 * m31 * m22 * m03 + m20 * m11 * m32 * m03 - m10
				* m21 * m32 * m03 - m30 * m21 * m02 * m13 + m20 * m31 * m02 * m13
				+ m30 * m01 * m22 * m13 - m00 * m31 * m22 * m13 - m20 * m01 * m32
				* m13 + m00 * m21 * m32 * m13 + m30 * m11 * m02 * m23 - m10 * m31
				* m02 * m23 - m30 * m01 * m12 * m23 + m00 * m31 * m12 * m23 + m10
				* m01 * m32 * m23 - m00 * m11 * m32 * m23 - m20 * m11 * m02 * m33
				+ m10 * m21 * m02 * m33 + m20 * m01 * m12 * m33 - m00 * m21 * m12
				* m33 - m10 * m01 * m22 * m33 + m00 * m11 * m22 * m33;
		
		if (det == 0f)  {
			throw new RuntimeException("matrix is not invertible");
		}
		
		double inv_det = 1.0f / det;
		
		double tm00 = m12 * m23 * m31 - m13 * m22 * m31 + m13 * m21 * m32 - m11 * m23 * m32 - m12 * m21 * m33
				+ m11 * m22 * m33;
		double tm01 = m03 * m22 * m31 - m02 * m23 * m31 - m03 * m21 * m32 + m01 * m23 * m32 + m02 * m21 * m33
				- m01 * m22 * m33;
		double tm02 = m02 * m13 * m31 - m03 * m12 * m31 + m03 * m11 * m32 - m01 * m13 * m32 - m02 * m11 * m33
				+ m01 * m12 * m33;
		double tm03 = m03 * m12 * m21 - m02 * m13 * m21 - m03 * m11 * m22 + m01 * m13 * m22 + m02 * m11 * m23
				- m01 * m12 * m23;
		double tm10 = m13 * m22 * m30 - m12 * m23 * m30 - m13 * m20 * m32 + m10 * m23 * m32 + m12 * m20 * m33
				- m10 * m22 * m33;
		double tm11 = m02 * m23 * m30 - m03 * m22 * m30 + m03 * m20 * m32 - m00 * m23 * m32 - m02 * m20 * m33
				+ m00 * m22 * m33;
		double tm12 = m03 * m12 * m30 - m02 * m13 * m30 - m03 * m10 * m32 + m00 * m13 * m32 + m02 * m10 * m33
				- m00 * m12 * m33;
		double tm13 = m02 * m13 * m20 - m03 * m12 * m20 + m03 * m10 * m22 - m00 * m13 * m22 - m02 * m10 * m23
				+ m00 * m12 * m23;
		double tm20 = m11 * m23 * m30 - m13 * m21 * m30 + m13 * m20 * m31 - m10 * m23 * m31 - m11 * m20 * m33
				+ m10 * m21 * m33;
		double tm21 = m03 * m21 * m30 - m01 * m23 * m30 - m03 * m20 * m31 + m00 * m23 * m31 + m01 * m20 * m33
				- m00 * m21 * m33;
		double tm22 = m01 * m13 * m30 - m03 * m11 * m30 + m03 * m10 * m31 - m00 * m13 * m31 - m01 * m10 * m33
				+ m00 * m11 * m33;
		double tm23 = m03 * m11 * m20 - m01 * m13 * m20 - m03 * m10 * m21 + m00 * m13 * m21 + m01 * m10 * m23
				- m00 * m11 * m23;
		double tm30 = m12 * m21 * m30 - m11 * m22 * m30 - m12 * m20 * m31 + m10 * m22 * m31 + m11 * m20 * m32
				- m10 * m21 * m32;
		double tm31 = m01 * m22 * m30 - m02 * m21 * m30 + m02 * m20 * m31 - m00 * m22 * m31 - m01 * m20 * m32
				+ m00 * m21 * m32;
		double tm32 = m02 * m11 * m30 - m01 * m12 * m30 - m02 * m10 * m31 + m00 * m12 * m31 + m01 * m10 * m32
				- m00 * m11 * m32;
		double tm33 = m01 * m12 * m20 - m02 * m11 * m20 + m02 * m10 * m21 - m00 * m12 * m21 - m01 * m10 * m22
				+ m00 * m11 * m22;
		
		m00 = tm00 * inv_det;
		m01 = tm01 * inv_det;
		m02 = tm02 * inv_det;
		m03 = tm03 * inv_det;
		m10 = tm10 * inv_det;
		m11 = tm11 * inv_det;
		m12 = tm12 * inv_det;
		m13 = tm13 * inv_det;
		m20 = tm20 * inv_det;
		m21 = tm21 * inv_det;
		m22 = tm22 * inv_det;
		m23 = tm23 * inv_det;
		m30 = tm30 * inv_det;
		m31 = tm31 * inv_det;
		m32 = tm32 * inv_det;
		m33 = tm33 * inv_det;
			
    	return this;
    }
        
	/**
	 * Multiplies this matrix with the specified matrix.
	 * 
	 * @param o
	 *            the other matrix
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix4d mul(final Matrix4d o) {
    	set(m00 * o.m00 + m01 * o.m10 + m02 * o.m20 + m03 * o.m30,
    		m00 * o.m01 + m01 * o.m11 + m02 * o.m21 + m03 * o.m31,
    		m00 * o.m02 + m01 * o.m12 + m02 * o.m22 + m03 * o.m32,
    		m00 * o.m03 + m01 * o.m13 + m02 * o.m23 + m03 * o.m33,
    		
    		m10 * o.m00 + m11 * o.m10 + m12 * o.m20 + m13 * o.m30,
    		m10 * o.m01 + m11 * o.m11 + m12 * o.m21 + m13 * o.m31,
    		m10 * o.m02 + m11 * o.m12 + m12 * o.m22 + m13 * o.m32,
    		m10 * o.m03 + m11 * o.m13 + m12 * o.m23 + m13 * o.m33,
    		
    		m20 * o.m00 + m21 * o.m10 + m22 * o.m20 + m23 * o.m30,
    		m20 * o.m01 + m21 * o.m11 + m22 * o.m21 + m23 * o.m31,
    		m20 * o.m02 + m21 * o.m12 + m22 * o.m22 + m23 * o.m32,
    		m20 * o.m03 + m21 * o.m13 + m22 * o.m23 + m23 * o.m33,
    		
    		m30 * o.m00 + m31 * o.m10 + m32 * o.m20 + m33 * o.m30,
    		m30 * o.m01 + m31 * o.m11 + m32 * o.m21 + m33 * o.m31,
    		m30 * o.m02 + m31 * o.m12 + m32 * o.m22 + m33 * o.m32,
    		m30 * o.m03 + m31 * o.m13 + m32 * o.m23 + m33 * o.m33);
    	
    	return this;
    }
    
	/**
	 * Transforms the specified point. This method multiplies the specified
	 * point with this matrix assuming that the fourth element of the vector is
	 * one. The result is stored in the specified output vector {@code pt}.
	 * 
	 * <p>
	 * The point to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param p
	 *            the point to be transformed.
	 * @param pt
	 *            the transformed point
	 * @return reference to the transformed point
	 */
    public Vector3d transformPoint(Vector3d p, Vector3d pt) {
    	double x = p.x; double y = p.y;
    	
		pt.x = m00 * x + m01 * y + m02 * p.z + m03;
		pt.y = m10 * x + m11 * y + m12 * p.z + m13;
		pt.z = m20 * x + m21 * y + m22 * p.z + m23;

    	return pt;
    }
    
	/**
	 * Transforms the specified point in-place. This method multiplies the specified
	 * point with this matrix assuming that the fourth element of the vector is
	 * one.
	 * 
	 * @param p
	 *            the point to be transformed.
	 * @return reference to the transformed point
	 */
    public Vector3d transformPoint(Vector3d p) {
    	double x = p.x; double y = p.y;
    	
		p.x = m00 * x + m01 * y + m02 * p.z + m03;
		p.y = m10 * x + m11 * y + m12 * p.z + m13;
		p.z = m20 * x + m21 * y + m22 * p.z + m23;

    	return p;
    }
    
	/**
	 * Transforms the specified vector. This method multiplies the specified
	 * vector with this matrix assuming that the fourth element of the vector is
	 * zero. The result is stored in the specified output vector {@code vt}.
	 * 
	 * <p>
	 * The vector to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param v
	 *            the vector to be transformed.
	 * @param vt
	 *            the transformed vector
	 * @return reference to the transformed vector
	 */
    public Vector3d transformVector(Vector3d v, Vector3d vt) {
    	double x = v.x; double y = v.y;
    	
		vt.x = m00 * x + m01 * y + m02 * v.z;
		vt.y = m10 * x + m11 * y + m12 * v.z;
		vt.z = m20 * x + m21 * y + m22 * v.z;

    	return vt;
    }
    
	/**
	 * Transforms the specified vector in-place. This method multiplies the specified
	 * vector with this matrix assuming that the fourth element of the vector is
	 * zero. 
	 * 
	 * @param v
	 *            the vector to be transformed.
	 * @param vt
	 *            the transformed vector
	 * @return reference to the transformed vector
	 */
    public Vector3d transformVector(Vector3d v) {
    	double x = v.x; double y = v.y;
    	
		v.x = m00 * x + m01 * y + m02 * v.z;
		v.y = m10 * x + m11 * y + m12 * v.z;
		v.z = m20 * x + m21 * y + m22 * v.z;

    	return v;
    }

	/**
	 * Transforms the specified four dimensional vector. This method multiplies
	 * the specified vector with this matrix. Using a four-dimensional vector,
	 * there is no need to distinguish between point and vector. The result is
	 * stored in the specified output vector {@code vt}.
	 * 
	 * <p>
	 * The vector to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param v
	 *            the vector to be transformed
	 * @param vt
	 *            the vector where to store the result
	 * @return reference to the result vector
	 */
    public Vector4d transform(Vector4d v, Vector4d vt) {
    	double x = v.x; double y = v.y; double z = v.z;
    	
    	vt.x = m00 * x + m01 * y + m02 * z + m03 * v.w;
    	vt.y = m10 * x + m11 * y + m12 * z + m13 * v.w;
    	vt.z = m20 * x + m21 * y + m22 * z + m23 * v.w;
    	vt.w = m30 * x + m31 * y + m32 * z + m33 * v.w;
    	return vt;
    }
    
	/**
	 * Transforms the specified four dimensional vector in-place. This method
	 * multiplies the specified vector with this matrix. Using a
	 * four-dimensional vector, there is no need to distinguish between point
	 * and vector.
	 * 
	 * @param v
	 *            the vector to be transformed
	 * @return reference to the result vector
	 */
    public Vector4d transform(Vector4d v) {
    	double x = v.x; double y = v.y; double z = v.z;
    	
    	v.x = m00 * x + m01 * y + m02 * z + m03 * v.w;
    	v.y = m10 * x + m11 * y + m12 * z + m13 * v.w;
    	v.z = m20 * x + m21 * y + m22 * z + m23 * v.w;
    	v.w = m30 * x + m31 * y + m32 * z + m33 * v.w;
    	return v;
    }
    
	/**
	 * Transforms the specified four-dimensional vector with the transposed of
	 * this matrix. This method multiplies the specified vector with the
	 * transposed of this matrix. Using a four-dimensional vector, there is no
	 * need to distinguish between point and vector. The result is stored in the
	 * specified output vector {@code vt}.
	 * 
	 * <p>
	 * The vector to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param v
	 *            the vector to be transformed
	 * @param vt
	 *            the vector where to store the result
	 * @return reference to the result vector
	 */
    public Vector4d transformTransposed(Vector4d v, Vector4d vt) {
    	double x = v.x; double y = v.y; double z = v.z;
    	
    	vt.x = m00 * x + m10 * y + m20 * z + m30 * v.w;
    	vt.y = m01 * x + m11 * y + m21 * z + m31 * v.w;
    	vt.z = m02 * x + m12 * y + m22 * z + m32 * v.w;
    	vt.w = m03 * x + m13 * y + m23 * z + m33 * v.w;
    	return vt;
    }
   
	/**
	 * Transforms the specified point with the transposed of this matrix. This
	 * method multiplies the specified vector with the transposed of this matrix
	 * assuming that the fourth element of the vector is one. The result is
	 * stored in the specified output vector {@code vt}.
	 * 
	 * <p>
	 * The vector to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param v
	 *            the vector to be transformed
	 * @param vt
	 *            the transformed vector
	 * @return reference to the transformed vector
	 */
    public Vector3d transformPointTransposed(Vector3d v, Vector3d vt) {
    	double x = v.x; double y = v.y;
		vt.x = m00 * x + m10 * y + m20 * v.z + m30;
		vt.y = m01 * x + m11 * y + m21 * v.z + m31;
		vt.z = m02 * x + m12 * y + m22 * v.z + m32;
    	return vt;
    }
    
	/**
	 * Transforms the specified vector with the transposed of this matrix. This
	 * method multiplies the specified vector with the transposed of this matrix
	 * assuming that the fourth element of the vector is zero. The result is
	 * stored in the specified output vector {@code vt}.
	 * 
	 * <p>
	 * The vector to be transformed and the output vector can be identical.
	 * </p>
	 * 
	 * @param v
	 *            the vector to be transformed.
	 * @param vt
	 *            the transformed vector
	 * @return reference to the transformed vector
	 */
    public Vector3d transformVectorTransposed(Vector3d v, Vector3d vt) {
    	double x = v.x; double y = v.y;
		vt.x = m00 * x + m10 * y + m20 * v.z;
		vt.y = m01 * x + m11 * y + m21 * v.z;
		vt.z = m02 * x + m12 * y + m22 * v.z;
    	return vt;
    }

	/**
	 * Multiplies the specified point vector. The vector components are divided
	 * by w assuming that the fourth element of the vector is one.
	 * 
	 * @param p
	 *            the point to be projected
	 * @param pp
	 *            the projected vector
	 * @return a reference to the projected vector
	 */
	public Vector3d projectPoint(Vector3d p, Vector3d pp) {
    	double x = p.x; double y = p.y;
		double w = x * m30 + y * m31 + p.z * m32 + m33;	
    	
    	pp.x = (m00 * x + m01 * y + m02 * p.z + m03) / w;
    	pp.y = (m10 * x + m11 * y + m12 * p.z + m13) / w;
    	pp.z = (m20 * x + m21 * y + m22 * p.z + m23) / w;
		
		return pp;
	}
	
	public double projectPointW(Vector3d p, Vector3d pp) {
    	double x = p.x; double y = p.y;
		double w = x * m30 + y * m31 + p.z * m32 + m33;	
    	
    	pp.x = m00 * x + m01 * y + m02 * p.z + m03;
    	pp.y = m10 * x + m11 * y + m12 * p.z + m13;
    	pp.z = m20 * x + m21 * y + m22 * p.z + m23;
		
		return w;		
	}
	
	
	/**
	 * Multiplies the specified point vector in-place. The vector components are
	 * divided by w assuming that the fourth element of the vector is one.
	 * 
	 * @param p
	 *            the point to be projected
	 * @return a reference to the projected vector
	 */
	public Vector3d projectPoint(Vector3d p) {
    	double x = p.x; double y = p.y;
		double w = x * m30 + y * m31 + p.z * m32 + m33;	
    	
    	p.x = (m00 * x + m01 * y + m02 * p.z + m03) / w;
    	p.y = (m10 * x + m11 * y + m12 * p.z + m13) / w;
    	p.z = (m20 * x + m21 * y + m22 * p.z + m23) / w;
		
		return p;
	}
}
