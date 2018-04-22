package net.medox.neonengine.animation;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class JointTransform{
	private final Vector3f position;
	private final Quaternion rotation;
	
	public JointTransform(Vector3f position, Quaternion rotation){
		this.position = position;
		this.rotation = rotation;
	}
	
	protected Matrix4f getLocalTransform(){
		Matrix4f matrix = new Matrix4f();
		matrix.translate(position);
		return matrix.mul(rotation.toRotationMatrix());
	}
	
	protected static JointTransform interpolate(JointTransform frameA, JointTransform frameB, float progression){
		Vector3f pos = interpolate(frameA.position, frameB.position, progression);
		Quaternion rot = Quaternion.interpolate(frameA.rotation, frameB.rotation, progression);
		return new JointTransform(pos, rot);
	}
	
	private static Vector3f interpolate(Vector3f start, Vector3f end, float progression){
		float x = start.getX() + (end.getX() - start.getX()) * progression;
		float y = start.getY() + (end.getY() - start.getY()) * progression;
		float z = start.getZ() + (end.getZ() - start.getZ()) * progression;
		return new Vector3f(x, y, z);
	}
}
