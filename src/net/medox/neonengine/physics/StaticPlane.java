package net.medox.neonengine.physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class StaticPlane extends Collider{
	private static final Transform DEFAULT_TRANSFORM = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, -1-1f, 5), 1.0f));
	
	private CollisionShape shape;
	
	public StaticPlane(net.medox.neonengine.math.Vector3f planeNormal, float planeConstant){
		super();
		
//		final MotionState motionState = new DefaultMotionState(DEFAULT_TRANSFORM);
		
		shape = new StaticPlaneShape(new Vector3f(planeNormal.getX(), planeNormal.getY(), planeNormal.getZ()), planeConstant);
		
		final Vector3f inertia = new Vector3f(0, 0, 0);
		shape.calculateLocalInertia(1f, inertia);
		
//		final RigidBodyConstructionInfo bodyConstructionInfo = new RigidBodyConstructionInfo(1f, motionState, shape, inertia);
		
//		ballBodyConstructionInfo.restitution = 0.5f;
//		ballBodyConstructionInfo.angularDamping = 0.95f;
		
//		setBody(new RigidBody(bodyConstructionInfo));
//		setBody(new RigidBody(new RigidBodyConstructionInfo(1f, motionState, shape, inertia)));
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
