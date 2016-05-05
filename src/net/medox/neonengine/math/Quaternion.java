package net.medox.neonengine.math;

public class Quaternion{
	private float x;
	private float y;
	private float z;
	private float w;
	
	//only for networking{
	public Quaternion(){
		x = 0;
		y = 0;
		z = 0;
		w = 1;
	}
	//}

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
