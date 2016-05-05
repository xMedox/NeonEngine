package net.medox.neonengine.math;

public class Vector3f{
	private float x;
	private float y;
	private float z;
	
	//only for networking{
	public Vector3f(){
		x = 0;
		y = 0;
		z = 0;
	}
	//}
	
	public Vector3f(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public float length(){
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	public Vector3f max(Vector3f r){
		return new Vector3f(Math.max(x, r.getX()), Math.max(y, r.getY()), Math.max(z, r.getZ()));
	}
	
	public float max(){
		return Math.max(x, Math.max(y, z));
	}
	
	public float dot(Vector3f r){
		return x * r.getX() + y * r.getY() + z * r.getZ();
	}
	
	public Vector3f cross(Vector3f r){
		return new Vector3f(y * r.getZ() - z * r.getY(), z * r.getX() - x * r.getZ(), x * r.getY() - y * r.getX());
	}
	
	public Vector3f normalized(){
		final float length = length();

		return new Vector3f(x / length, y / length, z / length);
	}
	
	public Vector3f rotate(Vector3f axis, float angle){
		final float cosAngle = (float)Math.cos(-angle);
		
		return this.cross(axis.mul((float)Math.sin(-angle))).add(this.mul(cosAngle).add(axis.mul(this.dot(axis.mul(1 - cosAngle)))));
	}
	
	public Vector3f rotate(Quaternion rotation){
		final Quaternion w = rotation.mul(this).mul(rotation.conjugate());
		
		return new Vector3f(w.getX(), w.getY(), w.getZ());
	}
	
	public Vector3f lerp(Vector3f dest, float lerpFactor){
		return dest.sub(this).mul(lerpFactor).add(this);
	}
	
	public Vector3f reflect(Vector3f normal){
		return this.sub(normal.mul(this.dot(normal) * 2));
	}
	
	public Vector3f add(Vector3f r){
		return new Vector3f(x + r.getX(), y + r.getY(), z + r.getZ());
	}
	
	public Vector3f add(float r){
		return new Vector3f(x + r, y + r, z + r);
	}
	
	public Vector3f sub(Vector3f r){
		return new Vector3f(x - r.getX(), y - r.getY(), z - r.getZ());
	}
	
	public Vector3f sub(float r){
		return new Vector3f(x - r, y - r, z - r);
	}
	
	public Vector3f mul(Vector3f r){
		return new Vector3f(x * r.getX(), y * r.getY(), z * r.getZ());
	}
	
	public Vector3f mul(float r){
		return new Vector3f(x * r, y * r, z * r);
	}
	
	public Vector3f div(Vector3f r){
		return new Vector3f(x / r.getX(), y / r.getY(), z / r.getZ());
	}
	
	public Vector3f div(float r){
		return new Vector3f(x / r, y / r, z / r);
	}
	
	public Vector3f abs(){
		return new Vector3f(Math.abs(x), Math.abs(y), Math.abs(z));
	}
	
	public Vector2f getXY(){
		return new Vector2f(x, y);
	}
	
	public Vector2f getYZ(){
		return new Vector2f(y, z);
	}
	
	public Vector2f getZX(){
		return new Vector2f(z, x);
	}
	
	public Vector2f getYX(){
		return new Vector2f(y, x);
	}
	
	public Vector2f getZY(){
		return new Vector2f(z, y);
	}
	
	public Vector2f getXZ(){
		return new Vector2f(x, z);
	}
	
	public Vector3f set(float x, float y, float z){
		this.x = x;
		this.y = y;
		this.z = z;
		
		return this;
	}
	
	public Vector3f set(Vector3f r){
		this.x = r.getX();
		this.y = r.getY();
		this.z = r.getZ();
		
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
	
	public boolean equals(Vector3f r){
		return x == r.getX() && y == r.getY() && z == r.getZ();
	}
	
	@Override
	public String toString(){
		return "(" + x + ", " + y + ", " + z + ")";
	}
}
