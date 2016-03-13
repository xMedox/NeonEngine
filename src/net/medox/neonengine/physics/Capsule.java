package net.medox.neonengine.physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class Capsule extends Collider{
	private static final Transform DEFAULT_TRANSFORM = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(4, 4, 4), 1.0f));
	
	private CollisionShape shape;
	
	public Capsule(float radius, float height){
		super();
		
//		final MotionState motionState = new DefaultMotionState(DEFAULT_TRANSFORM);
		
		shape = new CapsuleShape(radius, height);
		
		final Vector3f inertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(1f, inertia);
		
//		RigidBodyConstructionInfo bodyConstructionInfo = new RigidBodyConstructionInfo(1f, motionState, shape, inertia);
//		RigidBodyConstructionInfo bodyConstructionInfo = new RigidBodyConstructionInfo(1f, new DefaultMotionState(DEFAULT_TRANSFORM), shape, inertia);
		
//		ballBodyConstructionInfo.restitution = 0.5f;
//		ballBodyConstructionInfo.angularDamping = 0.95f;
		
//		setBody(new RigidBody(bodyConstructionInfo));
		setBody(new RigidBody(new RigidBodyConstructionInfo(1f, new DefaultMotionState(DEFAULT_TRANSFORM), shape, inertia)));
	}
	
	@Override
	public void setMassProps(float mass){
		final Vector3f inertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(mass, inertia);
		
		setMassProps(mass, new net.medox.neonengine.math.Vector3f(inertia.x, inertia.y, inertia.z));
	}
	
	@Override
	public net.medox.neonengine.math.Vector3f getScale(){
		final Vector3f scale = shape.getLocalScaling(new Vector3f());
		
		return new net.medox.neonengine.math.Vector3f(scale.x, scale.y, scale.z);
	}
	
	@Override
	public void setScale(net.medox.neonengine.math.Vector3f scale){
		shape.setLocalScaling(new Vector3f(scale.getX(), scale.getY(), scale.getZ()));
		
		getBody().setCollisionShape(shape);
	}
	
	@Override
	public void setScale(float scale){
		shape.setLocalScaling(new Vector3f(scale, scale, scale));
		
		getBody().setCollisionShape(shape);
	}
	
	@Override
	public CollisionShape getCollisionShape(){
		return shape;
	}
}
