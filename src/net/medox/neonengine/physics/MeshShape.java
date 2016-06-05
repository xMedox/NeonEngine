package net.medox.neonengine.physics;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.collision.btConvexHullShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody.btRigidBodyConstructionInfo;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import net.medox.neonengine.core.DataUtil;

public class MeshShape extends Collider{
	private final btCollisionShape shape;
	
	public MeshShape(ArrayList<net.medox.neonengine.math.Vector3f> positions){
		super();
		
//		final MotionState motionState = new DefaultMotionState(DEFAULT_TRANSFORM);
		
//		final FloatBuffer points = DataUtil.createFloatBuffer((positions.size()-1) * 3);
		
//		TriangleMeshShape tetraMesh;
//		tetraMesh.
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(-1, 0, 1), btVector3( 1, 0, -1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0, -1), btVector3(-1, 0, 1), btVector3( 1, 0,  1), false);
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(0, 1, 0), btVector3(-1, 0, 1), false);
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(0, 1, 0), btVector3( 1, 0,-1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0, -1), btVector3(0, 1, 0), btVector3( 1, 0, 1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0,  1), btVector3(0, 1, 0), btVector3(-1, 0, 1), false);
//		tetraShape = new BvhTriangleMeshShape(&tetraMesh, false);
		
//		for(int i = 0; i < positions.size(); i++){
//			final net.medox.neonengine.math.Vector3f save = positions.get(i);
//			
//			points.add(new Vector3(save.getX(), save.getY(), save.getZ()));
//		}
		
		final FloatBuffer buffer = DataUtil.createFloatBuffer(positions.size() * 3);
		
		for(int i = 0; i < positions.size(); i++){
			buffer.put(positions.get(i).getX());
			buffer.put(positions.get(i).getY());
			buffer.put(positions.get(i).getZ());
		}
		
		buffer.flip();
		
		shape = new btConvexHullShape(buffer);
		
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
	public btCollisionShape getCollisionShape(){
		return shape;
	}
}
