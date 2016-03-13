package net.medox.neonengine.physics;

import java.util.ArrayList;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.ConvexHullShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import com.bulletphysics.util.ObjectArrayList;

public class MeshShape extends Collider{
	private static final Transform DEFAULT_TRANSFORM = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(-10, 15, 0), 1.0f));
	
	private CollisionShape shape;
	
	public MeshShape(ArrayList<net.medox.neonengine.math.Vector3f> positions){
		super();
		
//		final MotionState motionState = new DefaultMotionState(DEFAULT_TRANSFORM);
		
		final ObjectArrayList<Vector3f> points = new ObjectArrayList<Vector3f>();
		
//		TriangleMeshShape tetraMesh;
//		tetraMesh.
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(-1, 0, 1), btVector3( 1, 0, -1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0, -1), btVector3(-1, 0, 1), btVector3( 1, 0,  1), false);
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(0, 1, 0), btVector3(-1, 0, 1), false);
//		tetraMesh.addTriangle(btVector3(-1, 0, -1), btVector3(0, 1, 0), btVector3( 1, 0,-1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0, -1), btVector3(0, 1, 0), btVector3( 1, 0, 1), false);
//		tetraMesh.addTriangle(btVector3( 1, 0,  1), btVector3(0, 1, 0), btVector3(-1, 0, 1), false);
//		tetraShape = new BvhTriangleMeshShape(&tetraMesh, false);
		
		for(int i = 0; i < positions.size(); i++){
			final net.medox.neonengine.math.Vector3f save = positions.get(i);
			
			points.add(new Vector3f(save.getX(), save.getY(), save.getZ()));
		}
		
		shape = new ConvexHullShape(points);
		
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
	public CollisionShape getCollisionShape(){
		return shape;
	}
}
