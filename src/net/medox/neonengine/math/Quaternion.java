package net.medox.neonengine.math;

public class Quaternion{
	private float x;
	private float y;
	private float z;
	private float w;
	
	public Quaternion(){
		x = 0;
		y = 0;
		z = 0;
		w = 1;
	}
	
	public Quaternion(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}

	public Quaternion(Vector3f axis, float angle){
		final float sinHalfAngle = (float)Math.sin(angle / 2);

		this.x = axis.getX() * sinHalfAngle;
		this.y = axis.getY() * sinHalfAngle;
		this.z = axis.getZ() * sinHalfAngle;
		this.w = (float)Math.cos(angle / 2);
	}

	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z + w * w);
	}
	
	public Quaternion normalized(){
		final float length = length();
		
		return new Quaternion(x / length, y / length, z / length, w / length);
	}
	
	public Quaternion conjugate(){
		return new Quaternion(-x, -y, -z, w);
	}

	public Quaternion mul(float r){
		return new Quaternion(x * r, y * r, z * r, w * r);
	}

	public Quaternion mul(Quaternion r){
		return new Quaternion(x * r.getW() + w * r.getX() + y * r.getZ() - z * r.getY(), y * r.getW() + w * r.getY() + z * r.getX() - x * r.getZ(), z * r.getW() + w * r.getZ() + x * r.getY() - y * r.getX(), w * r.getW() - x * r.getX() - y * r.getY() - z * r.getZ());
	}
	
	public Quaternion mul(Vector3f r){
		return new Quaternion(w * r.getX() + y * r.getZ() - z * r.getY(), w * r.getY() + z * r.getX() - x * r.getZ(), w * r.getZ() + x * r.getY() - y * r.getX(), -x * r.getX() - y * r.getY() - z * r.getZ());
	}
	
	public Quaternion sub(Quaternion r){
		return new Quaternion(x - r.getX(), y - r.getY(), z - r.getZ(), w - r.getW());
	}
	
	public Quaternion add(Quaternion r){
		return new Quaternion(x + r.getX(), y + r.getY(), z + r.getZ(), w + r.getW());
	}

	public Matrix4f toRotationMatrix(){
		return new Matrix4f().initRotation(new Vector3f(2.0f * (x*z - w*y), 2.0f * (y*z + w*x), 1.0f - 2.0f * (x*x + y*y)), new Vector3f(2.0f * (x*y + w*z), 1.0f - 2.0f * (x*x + z*z), 2.0f * (y*z - w*x)), new Vector3f(1.0f - 2.0f * (y*y + z*z), 2.0f * (x*y - w*z), 2.0f * (x*z + w*y)));
	}
	
	public float dot(Quaternion r){
		return x * r.getX() + y * r.getY() + z * r.getZ() + w * r.getW();
	}

	public Quaternion nlerp(Quaternion dest, float lerpFactor, boolean shortest){
		Quaternion correctedDest = dest;

		if(shortest && this.dot(dest) < 0){
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
		}

		return correctedDest.sub(this).mul(lerpFactor).add(this).normalized();
	}

	public Quaternion slerp(Quaternion dest, float lerpFactor, boolean shortest){
		float cos = this.dot(dest);
		Quaternion correctedDest = dest;

		if(shortest && cos < 0){
			cos = -cos;
			correctedDest = new Quaternion(-dest.getX(), -dest.getY(), -dest.getZ(), -dest.getW());
		}

		if(Math.abs(cos) >= 1 - /*EPSILON*/1e3f){
			return nlerp(correctedDest, lerpFactor, false);
		}

		final float sin = (float)Math.sqrt(1.0f - cos * cos);
		final float angle = (float)Math.atan2(sin, cos);
		final float invSin =  1.0f/sin;

		return this.mul((float)Math.sin((1.0f - lerpFactor) * angle) * invSin).add(correctedDest.mul((float)Math.sin((lerpFactor) * angle) * invSin));
	}
	
	public Matrix4f toRotationMatrix2(){
		Matrix4f matrix = new Matrix4f();
		final float xy = x * y;
		final float xz = x * z;
		final float xw = x * w;
		final float yz = y * z;
		final float yw = y * w;
		final float zw = z * w;
		final float xSquared = x * x;
		final float ySquared = y * y;
		final float zSquared = z * z;
		matrix.set(0, 0, (1 - 2 * (ySquared + zSquared)));
		matrix.set(0, 1, (2 * (xy - zw)));
		matrix.set(0, 2, (2 * (xz + yw)));
		matrix.set(0, 3, 0);
		matrix.set(1, 0, (2 * (xy + zw)));
		matrix.set(1, 1, (1 - 2 * (xSquared + zSquared)));
		matrix.set(1, 2, (2 * (yz - xw)));
		matrix.set(1, 3, 0);
		matrix.set(2, 0, (2 * (xz - yw)));
		matrix.set(2, 1, (2 * (yz + xw)));
		matrix.set(2, 2, (1 - 2 * (xSquared + ySquared)));
		matrix.set(2, 3, 0);
		matrix.set(3, 0, 0);
		matrix.set(3, 1, 0);
		matrix.set(3, 2, 0);
		matrix.set(3, 3, 1);
		return matrix;
	}
	
	public static Quaternion interpolate(Quaternion a, Quaternion b, float blend){
		Quaternion result = new Quaternion();
		result = result.normalized();
		float dot = a.w * b.w + a.x * b.x + a.y * b.y + a.z * b.z;
		float blendI = 1f - blend;
		if(dot < 0){
			result.w = blendI * a.w + blend * -b.w;
			result.x = blendI * a.x + blend * -b.x;
			result.y = blendI * a.y + blend * -b.y;
			result.z = blendI * a.z + blend * -b.z;
		}else{
			result.w = blendI * a.w + blend * b.w;
			result.x = blendI * a.x + blend * b.x;
			result.y = blendI * a.y + blend * b.y;
			result.z = blendI * a.z + blend * b.z;
		}
		result = result.normalized();
		return result;
	}
	
	public static Quaternion fromMatrix(Matrix4f matrix){
		float w, x, y, z;
		float diagonal = matrix.get(0, 0) + matrix.get(1, 1) + matrix.get(2, 2);
		if(diagonal > 0){
			float w4 = (float) (Math.sqrt(diagonal + 1f) * 2f);
			w = w4 / 4f;
			x = (matrix.get(2, 1) - matrix.get(1, 2)) / w4;
			y = (matrix.get(0, 2) - matrix.get(2, 0)) / w4;
			z = (matrix.get(1, 0) - matrix.get(0, 1)) / w4;
		}else if ((matrix.get(0, 0) > matrix.get(1, 1)) && (matrix.get(0, 0) > matrix.get(2, 2))){
			float x4 = (float) (Math.sqrt(1f + matrix.get(0, 0) - matrix.get(1, 1) - matrix.get(2, 2)) * 2f);
			w = (matrix.get(2, 1) - matrix.get(1, 2)) / x4;
			x = x4 / 4f;
			y = (matrix.get(0, 1) + matrix.get(1, 0)) / x4;
			z = (matrix.get(0, 2) + matrix.get(2, 0)) / x4;
		}else if (matrix.get(1, 1) > matrix.get(2, 2)){
			float y4 = (float) (Math.sqrt(1f + matrix.get(1, 1) - matrix.get(0, 0) - matrix.get(2, 2)) * 2f);
			w = (matrix.get(0, 2) - matrix.get(2, 0)) / y4;
			x = (matrix.get(0, 1) + matrix.get(1, 0)) / y4;
			y = y4 / 4f;
			z = (matrix.get(1, 2) + matrix.get(2, 1)) / y4;
		}else{
			float z4 = (float) (Math.sqrt(1f + matrix.get(2, 2) - matrix.get(0, 0) - matrix.get(1, 1)) * 2f);
			w = (matrix.get(1, 0) - matrix.get(0, 1)) / z4;
			x = (matrix.get(0, 2) + matrix.get(2, 0)) / z4;
			y = (matrix.get(1, 2) + matrix.get(2, 1)) / z4;
			z = z4 / 4f;
		}
		
		Quaternion quat = new Quaternion(x, y, z, w);
		quat = quat.normalized();
		
		return quat;
	}
	
	public Quaternion(Matrix4f rot){
		final float trace = rot.get(0, 0) + rot.get(1, 1) + rot.get(2, 2);

		if(trace > 0){
			final float s = 0.5f / (float)Math.sqrt(trace+ 1.0f);
			w = 0.25f / s;
			x = (rot.get(1, 2) - rot.get(2, 1)) * s;
			y = (rot.get(2, 0) - rot.get(0, 2)) * s;
			z = (rot.get(0, 1) - rot.get(1, 0)) * s;
		}else{
			if(rot.get(0, 0) > rot.get(1, 1) && rot.get(0, 0) > rot.get(2, 2)){
				final float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(0, 0) - rot.get(1, 1) - rot.get(2, 2));
				w = (rot.get(1, 2) - rot.get(2, 1)) / s;
				x = 0.25f * s;
				y = (rot.get(1, 0) + rot.get(0, 1)) / s;
				z = (rot.get(2, 0) + rot.get(0, 2)) / s;
			}else if(rot.get(1, 1) > rot.get(2, 2)){
				final float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(1, 1) - rot.get(0, 0) - rot.get(2, 2));
				w = (rot.get(2, 0) - rot.get(0, 2)) / s;
				x = (rot.get(1, 0) + rot.get(0, 1)) / s;
				y = 0.25f * s;
				z = (rot.get(2, 1) + rot.get(1, 2)) / s;
			}else{
				final float s = 2.0f * (float)Math.sqrt(1.0f + rot.get(2, 2) - rot.get(0, 0) - rot.get(1, 1));
				w = (rot.get(0, 1) - rot.get(1, 0) ) / s;
				x = (rot.get(2, 0) + rot.get(0, 2) ) / s;
				y = (rot.get(1, 2) + rot.get(2, 1) ) / s;
				z = 0.25f * s;
			}
		}

		final float length = (float)Math.sqrt(x*x + y*y + z*z +w*w);
		x /= length;
		y /= length;
		z /= length;
		w /= length;
	}

	public Vector3f getForward(){
		return new Vector3f(0, 0, 1).rotate(this);
	}

	public Vector3f getBack(){
		return new Vector3f(0, 0, -1).rotate(this);
	}

	public Vector3f getUp(){
		return new Vector3f(0, 1, 0).rotate(this);
	}

	public Vector3f getDown(){
		return new Vector3f(0, -1, 0).rotate(this);
	}

	public Vector3f getRight(){
		return new Vector3f(1, 0, 0).rotate(this);
	}

	public Vector3f getLeft(){
		return new Vector3f(-1, 0, 0).rotate(this);
	}

	public Quaternion set(float x, float y, float z, float w){
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
		
		return this;
	}
	
	public Quaternion set(Quaternion r){
		this.x = r.getX();
		this.y = r.getY();
		this.z = r.getZ();
		this.w = r.getW();
		
		return this;
	}

	public float getX(){
		return x;
	}

	public void setX(float x){
		this.x = x;
	}

	public float getY(){
		return y;
	}

	public void setY(float y){
		this.y = y;
	}

	public float getZ(){
		return z;
	}

	public void setZ(float z){
		this.z = z;
	}

	public float getW(){
		return w;
	}

	public void setW(float w){
		this.w = w;
	}

	public boolean equals(Quaternion r){
		return x == r.getX() && y == r.getY() && z == r.getZ() && w == r.getW();
	}
	
	@Override
	public String toString(){
		return "(" + x + ", " + y + ", " + z + ", " + w + ")";
	}
}
