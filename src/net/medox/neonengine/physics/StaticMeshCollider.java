package net.medox.neonengine.physics;

import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBvhTriangleMeshShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btTriangleMesh;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

public class StaticMeshCollider extends Collider{
	private final btBvhTriangleMeshShape shape;
	private final btTriangleMesh meshInterface;
	private boolean cleanedUp;
	
	public StaticMeshCollider(List<Integer> indicies, List<net.medox.neonengine.math.Vector3f> positions){
		super();
		
		meshInterface = new btTriangleMesh();
		
		for(int i = 0; i < indicies.size(); i+=3){
			final net.medox.neonengine.math.Vector3f pos1 = positions.get(indicies.get(i));
			final net.medox.neonengine.math.Vector3f pos2 = positions.get(indicies.get(i+1));
			final net.medox.neonengine.math.Vector3f pos3 = positions.get(indicies.get(i+2));
			
			meshInterface.addTriangle(new Vector3(pos1.getX(), pos1.getY(), pos1.getZ()), new Vector3(pos2.getX(), pos2.getY(), pos2.getZ()), new Vector3(pos3.getX(), pos3.getY(), pos3.getZ()), true);
		}
		
		shape = new btBvhTriangleMeshShape(meshInterface, true);
		
//		final btConvexHullShape s = new btConvexHullShape();
//		
//		for(int i = 0; i < positions.size(); i++){
//			final net.medox.neonengine.math.Vector3f pos1 = positions.get(i);
//			
//			s.addPoint(new Vector3(pos1.getX(), pos1.getY(), pos1.getZ()));
//		}
//		
//		shape = s;
		
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
		if(!cleanedUp){
			shape.dispose();
			meshInterface.dispose();
			super.cleanUp();
			
			cleanedUp = true;
		}
	}
}
