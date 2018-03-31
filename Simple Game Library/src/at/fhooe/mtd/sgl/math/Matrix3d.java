package at.fhooe.mtd.sgl.math;

/**
 * A 3x3 matrix. The matrix is organized in row-major order.
 */
public class Matrix3d {

	/** The elements first row. */
    public	double	m00, m01, m02;
    
	/** The elements second row. */
    public	double	m10, m11, m12;
    
	/** The elements third row. */
    public	double	m20, m21, m22;    
	
    /**
     * Creates a new instance initialized to zero.
     */
    public Matrix3d() {
    	// intentionally left empty
    }
    
	/**
	 * Creates a new instance initialized with the values of the given matrix.
	 * 
	 * @param o
	 *            the matrix from witch to copy the values
	 */
    public Matrix3d(Matrix3d o) {
    	set(o);
    }
    
	/**
	 * Sets this matrix to the given matrix.
	 * 
	 * @param o
	 *            the matrix from witch to copy the values
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d set(Matrix3d o) {
    	m00 = o.m00; m01 = o.m01; m02 = o.m02;
    	m10 = o.m10; m11 = o.m11; m12 = o.m12; 
    	m20 = o.m20; m21 = o.m21; m22 = o.m22;
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
	 * @param m10
	 *            first element of second row
	 * @param m11
	 *            second element of second row
	 * @param m12
	 *            third element of second row
	 * @param m20
	 *            first element of third row
	 * @param m21
	 *            second element of third row
	 * @param m22
	 *            third element of third row
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d set(double m00, double m01, double m02, 
    					double m10, double m11, double m12,
    					double m20, double m21, double m22) {

    	this.m00 = m00; this.m01 = m01; this.m02 = m02; 
    	this.m10 = m10; this.m11 = m11; this.m12 = m12; 
    	this.m20 = m20; this.m21 = m21; this.m22 = m22;
    	return this;
    }
    
	/**
	 * This this matrix to identity.
	 * 
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToIdentity() {
    	m00 = 1; m01 = 0; m02 = 0; 
    	m10 = 0; m11 = 1; m12 = 0; 
    	m20 = 0; m21 = 0; m22 = 1; 
    	return this;
    }
    
	/**
	 * Sets this matrix to a translate matrix with the specified translation.
	 * 
	 * @param tx
	 *            the distance to translate in the x-axis direction
	 * @param ty
	 *            the distance to translate in the y-axis direction
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToTranslate(double tx, double ty) {
    	m00 = 1; m01 = 0; m02 = tx;
    	m10 = 0; m11 = 1; m12 = ty;
    	m20 = 0; m21 = 0; m22 = 1;
    	return this;
    }
    
	/**
	 * Sets this matrix to a translate matrix with the specified translation.
	 * 
	 * @param v
	 *            the translation vector
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToTranslate(Vector2d v) {
    	return setToTranslate(v.x, v.y);
    }
    
	/**
	 * Sets this matrix to a scale matrix with the specified scale factors.
	 * 
	 * @param sx
	 *            scale factor for the x-axis
	 * @param sy
	 *            scale factor for the y-axis
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToScale(double sx, double sy) {
    	m00 = sx; m01 = 0;  m02 = 0; 
    	m10 = 0;  m11 = sy; m12 = 0; 
    	m20 = 0;  m21 = 0;  m22 = 1;
    	return this;
    }
    
	/**
	 * Sets this matrix to a scale matrix with the specified scale factors.
	 * 
	 * @param v
	 *            the vector containing the scale factors
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToScale(Vector2d v) {
    	return setToScale(v.x, v.y);
    }
    
	/**
	 * Sets this matrix to a rotation matrix rotating by the specified angle.
	 * 
	 * @param angle
	 *            the angle in radians
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d setToRotate(double angle) {
    	double sin = Math.sin(angle); double cos = Math.cos(angle);
    	m00 =  cos; m10 = sin; m20 = 0;
    	m01 = -sin; m11 = cos; m21 = 0;
    	m02 =    0; m12 =   0; m22 = 1;
    	
    	return this;
    }
    
	/**
	 * Multiplies this matrix with the specified matrix.
	 * 
	 * @param o
	 *            the other matrix
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d mul(Matrix3d o) {
		set(m00 * o.m00 + m01 * o.m10 + m02 * o.m20,
		    m00 * o.m01 + m01 * o.m11 + m02 * o.m21,
		    m00 * o.m02 + m01 * o.m12 + m02 * o.m22,
		    
		    m10 * o.m00 + m11 * o.m10 + m12 * o.m20,
		    m10 * o.m01 + m11 * o.m11 + m12 * o.m21,
		    m10 * o.m02 + m11 * o.m12 + m12 * o.m22,

		    m20 * o.m00 + m21 * o.m10 + m22 * o.m20,
		    m20 * o.m01 + m21 * o.m11 + m22 * o.m21,
		    m20 * o.m02 + m21 * o.m12 + m22 * o.m22);
		
		return this;
    }
    
	/**
	 * Multiplies the two specified matrices and stores the result in this
	 * matrix.
	 * 
	 * @param a
	 *            the first matrix
	 * @param b
	 *            the second matrix
	 * @return a reference to this matrix for method chaining
	 */
    public Matrix3d mul(final Matrix3d a, final Matrix3d b) {
		m00 = a.m00 * b.m00 + a.m01 * b.m10 + a.m02 * b.m20;
		m01 = a.m00 * b.m01 + a.m01 * b.m11 + a.m02 * b.m21;
		m02 = a.m00 * b.m02 + a.m01 * b.m12 + a.m02 * b.m22;
			    
		m10	= a.m10 * b.m00 + a.m11 * b.m10 + a.m12 * b.m20;
		m11 = a.m10 * b.m01 + a.m11 * b.m11 + a.m12 * b.m21;
		m12	= a.m10 * b.m02 + a.m11 * b.m12 + a.m12 * b.m22;

		m20 = a.m20 * b.m00 + a.m21 * b.m10 + a.m22 * b.m20;
		m21	= a.m20 * b.m01 + a.m21 * b.m11 + a.m22 * b.m21;
		m22	= a.m20 * b.m02 + a.m21 * b.m12 + a.m22 * b.m22;
        
    	return this;
    }

	/**
	 * Transforms the specified point. This method multiplies the specified
	 * point with this matrix assuming that the third element of the vector is
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
    public Vector2d transformPoint(Vector2d p, Vector2d pt) {
		pt.x = m00 * p.x + m01 * p.y + m02;
		pt.y = m10 * p.x + m11 * p.y + m12;
		
    	return pt;
    }
    
	/**
	 * Transforms the specified point in-place. This method multiplies the specified
	 * point with this matrix assuming that the third element of the vector is
	 * one.
	 * 
	 * @param p
	 *            the point to be transformed.
	 * @return reference to the transformed point
	 */
    public Vector2d transformPoint(Vector2d p) {
    	p.set(m00 * p.x + m01 * p.y + m02,
			  m10 * p.x + m11 * p.y + m12);
    	
    	return p;
    }
    
	/**
	 * Transforms the specified vector. This method multiplies the specified
	 * vector with this matrix assuming that the third element of the vector is
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
    public Vector2d transformVector(Vector2d v, Vector2d vt) {
    	vt.x = m00 * v.x + m01 * v.y;
    	vt.y = m10 * v.x + m11 * v.y;
    	
    	return vt;
    }
    
	/**
	 * Transforms the specified vector in-place. This method multiplies the specified
	 * vector with this matrix assuming that the third element of the vector is
	 * zero. 
	 * 
	 * @param v
	 *            the vector to be transformed.
	 * @param vt
	 *            the transformed vector
	 * @return reference to the transformed vector
	 */
    public Vector2d transformVector(Vector2d v) {
    	return v.set(m00 * v.x + m01 * v.y, 
    				 m10 * v.x + m11 * v.y);
    }
    
}
