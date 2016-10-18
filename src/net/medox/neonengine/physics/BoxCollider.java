package net.medox.neonengine.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

public class BoxCollider extends Collider{
	private final btCollisionShape shape;
	
	public BoxCollider(net.medox.neonengine.math.Vector3f halfExtents){
		super();
		
//		final MotionState motionState = new DefaultMotionState(DEFAULT_TRANSFORM);
		
		shape = new btBoxShape(new Vector3(halfExtents.getX(), halfExtents.getY(), halfExtents.getZ()));
		
		final Vector3 inertia = new Vector3(0, 0, 0);
		shape.calculateLocalInertia(1f, inertia);
		
//		final RigidBodyConstructionInfo bodyConstructionInfo = new RigidBodyConstructionInfo(1f, motionState, shape, inertia);
		
//		ballBodyConstructionInfo.restitution = 0.5f;
//		ballBodyConstructionInfo.angularDamping = 0.95f;
		
//		setBody(new RigidBody(bodyConstructionInfo));
//		setBody(new RigidBody(new RigidBodyConstructionInfo(1f, motionState, shape, inertia)));
		setBody(new btRigidBody(new btRigidBodyConstructionInfo(1f, new btDefaultMotionState(DEFAULT_TRANSFORM), shape, inertia)));
	}
	
	@Override
	public void setMassProps(float mass){
		final Vector3 inertia = new Vector3(0, 0, 0);
		shape.calculateLocalInertia(mass, inertia);
		
		setMassProps(mass, new net.medox.neonengine.math.Vector3f(inertia.x, inertia.y, inertia.z));
	}
	
	@Override
	public net.medox.neonengine.math.Vector3f getScale(){
		final Vector3 scale = shape.getLocalScaling();
		
		return new net.medox.neonengine.math.Vector3f(scale.x, scale.y, scale.z);
	}
	
	@Override
	public void setScale(net.medox.neonengine.math.Vector3f scale){
		shape.setLocalScaling(new Vector3(scale.getX(), scale.getY(), scale.getZ()));
		
		getBody().setCollisionShape(shape);
	}
	
	@Override
	public void setScale(float scale){
		shape.setLocalScaling(new Vector3(scale, scale, scale));
		
		getBody().setCollisionShape(shape);
	}
	
	@Override
	public btCollisionShape getCollisionShape(){
		return shape;
	}
	
	@Override
	public void cleanUp(){
		shape.dispose();
		super.cleanUp();
	}
}
