package net.medox.neonengine.math;

import java.nio.FloatBuffer;

public class Matrix4f{
	private float[][] m;
	
	public Matrix4f(){
		m = new float[4][4];
	}
	
	public Matrix4f initIdentity(){
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initTranslation(float x, float y, float z){
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z){
		final Matrix4f rx = new Matrix4f();
		final Matrix4f ry = new Matrix4f();
		final Matrix4f rz = new Matrix4f();
		
		final float xNew = (float)Math.toRadians(x);
		final float yNew = (float)Math.toRadians(y);
		final float zNew = (float)Math.toRadians(z);
		
		rz.m[0][0] = (float)Math.cos(zNew);rz.m[0][1] = -(float)Math.sin(zNew);	rz.m[0][2] = 0;						rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(zNew);rz.m[1][1] = (float)Math.cos(zNew);	rz.m[1][2] = 0;						rz.m[1][3] = 0;
		rz.m[2][0] = 0;					   rz.m[2][1] = 0;						rz.m[2][2] = 1;						rz.m[2][3] = 0;
		rz.m[3][0] = 0;					   rz.m[3][1] = 0;						rz.m[3][2] = 0;						rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;					   rx.m[0][1] = 0;						rx.m[0][2] = 0;						rx.m[0][3] = 0;
		rx.m[1][0] = 0;					   rx.m[1][1] = (float)Math.cos(xNew);	rx.m[1][2] = -(float)Math.sin(xNew);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					   rx.m[2][1] = (float)Math.sin(xNew);	rx.m[2][2] = (float)Math.cos(xNew);	rx.m[2][3] = 0;
		rx.m[3][0] = 0;					   rx.m[3][1] = 0;						rx.m[3][2] = 0;						rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(yNew);ry.m[0][1] = 0;						ry.m[0][2] = -(float)Math.sin(yNew);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					   ry.m[1][1] = 1;						ry.m[1][2] = 0;						ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(yNew);ry.m[2][1] = 0;						ry.m[2][2] = (float)Math.cos(yNew);	ry.m[2][3] = 0;
		ry.m[3][0] = 0;					   ry.m[3][1] = 0;						ry.m[3][2] = 0;						ry.m[3][3] = 1;
		
		m = rz.mul(ry.mul(rx)).getM();

		return this;
	}
	
	public Matrix4f initScale(float x, float y, float z){
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar){
		final float tanHalfFOV = (float)Math.tan(fov / 2);
		final float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);m[0][1] = 0;				m[0][2] = 0;					m[0][3] = 0;
		m[1][0] = 0;								m[1][1] = 1.0f / tanHalfFOV;m[1][2] = 0;					m[1][3] = 0;
		m[2][0] = 0;								m[2][1] = 0;				m[2][2] = (-zNear -zFar)/zRange;m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;								m[3][1] = 0;				m[3][2] = 1;					m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far){
		final float width = right - left;
		final float height = top - bottom;
		final float depth = far - near;
		
		m[0][0] = 2/width;	m[0][1] = 0;		m[0][2] = 0;		m[0][3] = -(right + left)/width;
		m[1][0] = 0;		m[1][1] = 2/height;	m[1][2] = 0;		m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = -2/depth;	m[2][3] = -(far + near)/depth;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up){
		final Vector3f f = forward.normalized();
		
		Vector3f r = up.normalized();
		r = r.cross(f);
		
		return initRotation(f, f.cross(r), r);
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right){
		m[0][0] = right.getX();		m[0][1] = right.getY();		m[0][2] = right.getZ();		m[0][3] = 0;
		m[1][0] = up.getX();		m[1][1] = up.getY();		m[1][2] = up.getZ();		m[1][3] = 0;
		m[2][0] = forward.getX();	m[2][1] = forward.getY();	m[2][2] = forward.getZ();	m[2][3] = 0;
		m[3][0] = 0;				m[3][1] = 0;				m[3][2] = 0;				m[3][3] = 1;
		
		return this;
	}
	
	public Vector3f transform(Vector3f v){
		return new Vector3f(m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2] * v.getZ() + m[0][3], 
							m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2] * v.getZ() + m[1][3],
							m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ() + m[2][3]);
	}
	
	public Matrix4f translate(Vector3f v){
		m[3][0] += m[0][0] * v.getX() + m[1][0] * v.getY() + m[2][0] * v.getZ();
		m[3][1] += m[0][1] * v.getX() + m[1][1] * v.getY() + m[2][1] * v.getZ();
		m[3][2] += m[0][2] * v.getX() + m[1][2] * v.getY() + m[2][2] * v.getZ();
		m[3][3] += m[0][3] * v.getX() + m[1][3] * v.getY() + m[2][3] * v.getZ();
		
		return this;
	}
	
	public Matrix4f mul(Matrix4f m){
		final Matrix4f res = new Matrix4f();
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				res.set(i, j, this.m[i][0] * m.get(0, j) + 
							  this.m[i][1] * m.get(1, j) +
							  this.m[i][2] * m.get(2, j) +
							  this.m[i][3] * m.get(3, j));
			}
		}
		
		return res;
	}
	
	public static Matrix4f mul(Matrix4f left, Matrix4f right){
		Matrix4f dest = new Matrix4f();

		float m00 = left.m[0][0] * right.m[0][0] + left.m[1][0] * right.m[0][1] + left.m[2][0] * right.m[0][2] + left.m[3][0] * right.m[0][3];
		float m01 = left.m[0][1] * right.m[0][0] + left.m[1][1] * right.m[0][1] + left.m[2][1] * right.m[0][2] + left.m[3][1] * right.m[0][3];
		float m02 = left.m[0][2] * right.m[0][0] + left.m[1][2] * right.m[0][1] + left.m[2][2] * right.m[0][2] + left.m[3][2] * right.m[0][3];
		float m03 = left.m[0][3] * right.m[0][0] + left.m[1][3] * right.m[0][1] + left.m[2][3] * right.m[0][2] + left.m[3][3] * right.m[0][3];
		float m10 = left.m[0][0] * right.m[1][0] + left.m[1][0] * right.m[1][1] + left.m[2][0] * right.m[1][2] + left.m[3][0] * right.m[1][3];
		float m11 = left.m[0][1] * right.m[1][0] + left.m[1][1] * right.m[1][1] + left.m[2][1] * right.m[1][2] + left.m[3][1] * right.m[1][3];
		float m12 = left.m[0][2] * right.m[1][0] + left.m[1][2] * right.m[1][1] + left.m[2][2] * right.m[1][2] + left.m[3][2] * right.m[1][3];
		float m13 = left.m[0][3] * right.m[1][0] + left.m[1][3] * right.m[1][1] + left.m[2][3] * right.m[1][2] + left.m[3][3] * right.m[1][3];
		float m20 = left.m[0][0] * right.m[2][0] + left.m[1][0] * right.m[2][1] + left.m[2][0] * right.m[2][2] + left.m[3][0] * right.m[2][3];
		float m21 = left.m[0][1] * right.m[2][0] + left.m[1][1] * right.m[2][1] + left.m[2][1] * right.m[2][2] + left.m[3][1] * right.m[2][3];
		float m22 = left.m[0][2] * right.m[2][0] + left.m[1][2] * right.m[2][1] + left.m[2][2] * right.m[2][2] + left.m[3][2] * right.m[2][3];
		float m23 = left.m[0][3] * right.m[2][0] + left.m[1][3] * right.m[2][1] + left.m[2][3] * right.m[2][2] + left.m[3][3] * right.m[2][3];
		float m30 = left.m[0][0] * right.m[3][0] + left.m[1][0] * right.m[3][1] + left.m[2][0] * right.m[3][2] + left.m[3][0] * right.m[3][3];
		float m31 = left.m[0][1] * right.m[3][0] + left.m[1][1] * right.m[3][1] + left.m[2][1] * right.m[3][2] + left.m[3][1] * right.m[3][3];
		float m32 = left.m[0][2] * right.m[3][0] + left.m[1][2] * right.m[3][1] + left.m[2][2] * right.m[3][2] + left.m[3][2] * right.m[3][3];
		float m33 = left.m[0][3] * right.m[3][0] + left.m[1][3] * right.m[3][1] + left.m[2][3] * right.m[3][2] + left.m[3][3] * right.m[3][3];

		dest.m[0][0] = m00;
		dest.m[0][1] = m01;
		dest.m[0][2] = m02;
		dest.m[0][3] = m03;
		dest.m[1][0] = m10;
		dest.m[1][1] = m11;
		dest.m[1][2] = m12;
		dest.m[1][3] = m13;
		dest.m[2][0] = m20;
		dest.m[2][1] = m21;
		dest.m[2][2] = m22;
		dest.m[2][3] = m23;
		dest.m[3][0] = m30;
		dest.m[3][1] = m31;
		dest.m[3][2] = m32;
		dest.m[3][3] = m33;

		return dest;
	}
	
	public Matrix4f rotate(float angle, Vector3f axis){
		Matrix4f dest = new Matrix4f().initIdentity();
		
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.getX()*axis.getY();
		float yz = axis.getY()*axis.getZ();
		float xz = axis.getX()*axis.getZ();
		float xs = axis.getX()*s;
		float ys = axis.getY()*s;
		float zs = axis.getZ()*s;

		float f00 = axis.getX()*axis.getX()*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		// n[3] not used
		float f10 = xy*oneminusc-zs;
		float f11 = axis.getY()*axis.getY()*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		// n[7] not used
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.getZ()*axis.getZ()*oneminusc+c;

		float t00 = m[0][0] * f00 + m[1][0] * f01 + m[2][0] * f02;
		float t01 = m[0][1] * f00 + m[1][1] * f01 + m[2][1] * f02;
		float t02 = m[0][2] * f00 + m[1][2] * f01 + m[2][2] * f02;
		float t03 = m[0][3] * f00 + m[1][3] * f01 + m[2][3] * f02;
		float t10 = m[0][0] * f10 + m[1][0] * f11 + m[2][0] * f12;
		float t11 = m[0][1] * f10 + m[1][1] * f11 + m[2][1] * f12;
		float t12 = m[0][2] * f10 + m[1][2] * f11 + m[2][2] * f12;
		float t13 = m[0][3] * f10 + m[1][3] * f11 + m[2][3] * f12;
		dest.m[2][0] = m[0][0] * f20 + m[1][0] * f21 + m[2][0] * f22;
		dest.m[2][1] = m[0][1] * f20 + m[1][1] * f21 + m[2][1] * f22;
		dest.m[2][2] = m[0][2] * f20 + m[1][2] * f21 + m[2][2] * f22;
		dest.m[2][3] = m[0][3] * f20 + m[1][3] * f21 + m[2][3] * f22;
		dest.m[0][0] = t00;
		dest.m[0][1] = t01;
		dest.m[0][2] = t02;
		dest.m[0][3] = t03;
		dest.m[1][0] = t10;
		dest.m[1][1] = t11;
		dest.m[1][2] = t12;
		dest.m[1][3] = t13;
		
		return dest;
	}
	
	public Matrix4f load(FloatBuffer buf){
		m[0][0] = buf.get();
		m[0][1] = buf.get();
		m[0][2] = buf.get();
		m[0][3] = buf.get();
		m[1][0] = buf.get();
		m[1][1] = buf.get();
		m[1][2] = buf.get();
		m[1][3] = buf.get();
		m[2][0] = buf.get();
		m[2][1] = buf.get();
		m[2][2] = buf.get();
		m[2][3] = buf.get();
		m[3][0] = buf.get();
		m[3][1] = buf.get();
		m[3][2] = buf.get();
		m[3][3] = buf.get();
		
		return this;
	}
	
	public float[][] getM(){
		float[][] res = new float[4][4];
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				res[i][j] = m[i][j];
			}
		}
		
		return res;
	}
	
	public static Quaternion transform(Matrix4f left, Quaternion right){
		Quaternion dest = new Quaternion();
		
		float x = left.m[0][0] * right.getX() + left.m[1][0] * right.getY() + left.m[2][0] * right.getZ() + left.m[3][0] * right.getW();
		float y = left.m[0][1] * right.getX() + left.m[1][1] * right.getY() + left.m[2][1] * right.getZ() + left.m[3][1] * right.getW();
		float z = left.m[0][2] * right.getX() + left.m[1][2] * right.getY() + left.m[2][2] * right.getZ() + left.m[3][2] * right.getW();
		float w = left.m[0][3] * right.getX() + left.m[1][3] * right.getY() + left.m[2][3] * right.getZ() + left.m[3][3] * right.getW();
		
		dest.setX(x);
		dest.setY(y);
		dest.setZ(z);
		dest.setW(w);

		return dest;
	}
	
	public Matrix4f transpose(){
		Matrix4f result = new Matrix4f();
		
		float m00 = m[0][0];
		float m01 = m[1][0];
		float m02 = m[2][0];
		float m03 = m[3][0];
		float m10 = m[0][1];
		float m11 = m[1][1];
		float m12 = m[2][1];
		float m13 = m[3][1];
		float m20 = m[0][2];
		float m21 = m[1][2];
		float m22 = m[2][2];
		float m23 = m[3][2];
		float m30 = m[0][3];
		float m31 = m[1][3];
		float m32 = m[2][3];
		float m33 = m[3][3];
		
		result.m[0][0] = m00;
		result.m[0][1] = m01;
		result.m[0][2] = m02;
		result.m[0][3] = m03;
		result.m[1][0] = m10;
		result.m[1][1] = m11;
		result.m[1][2] = m12;
		result.m[1][3] = m13;
		result.m[2][0] = m20;
		result.m[2][1] = m21;
		result.m[2][2] = m22;
		result.m[2][3] = m23;
		result.m[3][0] = m30;
		result.m[3][1] = m31;
		result.m[3][2] = m32;
		result.m[3][3] = m33;
		
		return result;
	}
	
	public Matrix4f invert(){
		float determinant = determinant();
		
		if(determinant != 0){
			Matrix4f result = new Matrix4f();
				
			float determinant_inv = 1f/determinant;
			
			
			float t00 =  determinant3x3(m[1][1], m[1][2], m[1][3], m[2][1], m[2][2], m[2][3], m[3][1], m[3][2], m[3][3]);
			float t01 = -determinant3x3(m[1][0], m[1][2], m[1][3], m[2][0], m[2][2], m[2][3], m[3][0], m[3][2], m[3][3]);
			float t02 =  determinant3x3(m[1][0], m[1][1], m[1][3], m[2][0], m[2][1], m[2][3], m[3][0], m[3][1], m[3][3]);
			float t03 = -determinant3x3(m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2], m[3][0], m[3][1], m[3][2]);
			
			float t10 = -determinant3x3(m[0][1], m[0][2], m[0][3], m[2][1], m[2][2], m[2][3], m[3][1], m[3][2], m[3][3]);
			float t11 =  determinant3x3(m[0][0], m[0][2], m[0][3], m[2][0], m[2][2], m[2][3], m[3][0], m[3][2], m[3][3]);
			float t12 = -determinant3x3(m[0][0], m[0][1], m[0][3], m[2][0], m[2][1], m[2][3], m[3][0], m[3][1], m[3][3]);
			float t13 =  determinant3x3(m[0][0], m[0][1], m[0][2], m[2][0], m[2][1], m[2][2], m[3][0], m[3][1], m[3][2]);
			
			float t20 =  determinant3x3(m[0][1], m[0][2], m[0][3], m[1][1], m[1][2], m[1][3], m[3][1], m[3][2], m[3][3]);
			float t21 = -determinant3x3(m[0][0], m[0][2], m[0][3], m[1][0], m[1][2], m[1][3], m[3][0], m[3][2], m[3][3]);
			float t22 =  determinant3x3(m[0][0], m[0][1], m[0][3], m[1][0], m[1][1], m[1][3], m[3][0], m[3][1], m[3][3]);
			float t23 = -determinant3x3(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[3][0], m[3][1], m[3][2]);
			
			float t30 = -determinant3x3(m[0][1], m[0][2], m[0][3], m[1][1], m[1][2], m[1][3], m[2][1], m[2][2], m[2][3]);
			float t31 =  determinant3x3(m[0][0], m[0][2], m[0][3], m[1][0], m[1][2], m[1][3], m[2][0], m[2][2], m[2][3]);
			float t32 = -determinant3x3(m[0][0], m[0][1], m[0][3], m[1][0], m[1][1], m[1][3], m[2][0], m[2][1], m[2][3]);
			float t33 =  determinant3x3(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2]);
			
			result.m[0][0] = t00*determinant_inv;
			result.m[1][1] = t11*determinant_inv;
			result.m[2][2] = t22*determinant_inv;
			result.m[3][3] = t33*determinant_inv;
			result.m[0][1] = t10*determinant_inv;
			result.m[1][0] = t01*determinant_inv;
			result.m[2][0] = t02*determinant_inv;
			result.m[0][2] = t20*determinant_inv;
			result.m[1][2] = t21*determinant_inv;
			result.m[2][1] = t12*determinant_inv;
			result.m[0][3] = t30*determinant_inv;
			result.m[3][0] = t03*determinant_inv;
			result.m[1][3] = t31*determinant_inv;
			result.m[3][1] = t13*determinant_inv;
			result.m[3][2] = t23*determinant_inv;
			result.m[2][3] = t32*determinant_inv;
			
			return result;
		}else{
			return null;
		}
	}
	
	private float determinant(){ 
		float f = m[0][0] 
				* ((m[1][1] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][1] + m[1][3] * m[2][1] * m[3][2]) 
						- m[1][3] * m[2][2] * m[3][1] 
						- m[1][1] * m[2][3] * m[3][2] 
						- m[1][2] * m[2][1] * m[3][3]); 
		f -= m[0][1] 
				* ((m[1][0] * m[2][2] * m[3][3] + m[1][2] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][2]) 
						- m[1][3] * m[2][2] * m[3][0] 
						- m[1][0] * m[2][3] * m[3][2] 
						- m[1][2] * m[2][0] * m[3][3]); 
		f += m[0][2] 
				* ((m[1][0] * m[2][1] * m[3][3] + m[1][1] * m[2][3] * m[3][0] + m[1][3] * m[2][0] * m[3][1]) 
						- m[1][3] * m[2][1] * m[3][0] 
						- m[1][0] * m[2][3] * m[3][1] 
						- m[1][1] * m[2][0] * m[3][3]); 
		f -= m[0][3] 
				* ((m[1][0] * m[2][1] * m[3][2] + m[1][1] * m[2][2] * m[3][0] + m[1][2] * m[2][0] * m[3][1]) 
						- m[1][2] * m[2][1] * m[3][0] 
						- m[1][0] * m[2][2] * m[3][1] 
						- m[1][1] * m[2][0] * m[3][2]); 
		return f; 
	}
	
	private float determinant3x3(float t00, float t01, float t02, float t10, float t11, float t12, float t20, float t21, float t22) { 
		return    t00 * (t11 * t22 - t12 * t21) 
				+ t01 * (t12 * t20 - t10 * t22) 
				+ t02 * (t10 * t21 - t11 * t20); 
	}
	
//	public float[] getMList(){
//		float[] res = new float[16];
//
//		for(int j = 0; j < 4; j++){
//			for(int i = 0; i < 4; i++){
//				res[i+j] = m[i][j];
//			}
//		}
//
//		return res;
//	}

	public float get(int x, int y){
		return m[x][y];
	}

	public void setM(float[][] m){
		this.m = m;
	}

	public void set(int x, int y, float value){
		m[x][y] = value;
	}
	
	@Override
	public String toString(){
		return "{(" + m[0][0] + ", " + m[0][1] + ", " + m[0][2] + ", " + m[0][3] + "), " +
			    "(" + m[1][0] + ", " + m[1][1] + ", " + m[1][2] + ", " + m[1][3] + "), " +
			    "(" + m[2][0] + ", " + m[2][1] + ", " + m[2][2] + ", " + m[2][3] + "), " +
			    "(" + m[3][0] + ", " + m[3][1] + ", " + m[3][2] + ", " + m[3][3] + ")}";
	}
}
