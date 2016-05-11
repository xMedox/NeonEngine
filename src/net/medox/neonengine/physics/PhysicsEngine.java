package net.medox.neonengine.physics;

import java.util.ArrayList;

import javax.vecmath.Vector3f;

import com.bulletphysics.BulletGlobals;
import com.bulletphysics.ContactAddedCallback;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.CollisionFilterGroups;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.CollisionWorld.RayResultCallback;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.dispatch.GhostPairCallback;
import com.bulletphysics.collision.narrowphase.ManifoldPoint;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.constraintsolver.ConstraintSolver;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;

public class PhysicsEngine{
	private static ArrayList<Collider> colliders;
	private static DiscreteDynamicsWorld dynamicsWorld;
	
	public static void init(){
		colliders = new ArrayList<Collider>();
		
		final Callback callback = new Callback();
		
//		final BroadphaseInterface broadphase = new DbvtBroadphase();
		BroadphaseInterface broadphase = new AxisSweep3(new Vector3f(-10000, -10000, -10000), new Vector3f(10000, 10000, 10000));
		final CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		final CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		final ConstraintSolver solver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		
//		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
//		dynamicsWorld.setGravity(new Vector3f(0, -9.80665f, 0));
		dynamicsWorld.setGravity(new Vector3f(0, -9.81f, 0));
//		dynamicsWorld.setGravity(new Vector3f(0, -1f, 0));
		
		dynamicsWorld.getDispatchInfo().allowedCcdPenetration = 0.001f;
		
		BulletGlobals.setContactAddedCallback(callback);
//		BulletGlobals.setContactProcessedCallback(callback);
//		dynamicsWorld.getPairCache().setInternalGhostPairCallback(new GhostPairCallback());
		broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new GhostPairCallback());
//		GImpactCollisionAlgorithm.registerAlgorithm(dispatcher);
	}
	
	public static void addObject(Collider collider){
		dynamicsWorld.addRigidBody(collider.getBody());
		colliders.add(collider);
	}
	
	public static void addObject(Collider collider, int group, int mask){
		dynamicsWorld.addRigidBody(collider.getBody(), (short)group, (short)mask);
		colliders.add(collider);
	}
	
	public static void removeObject(Collider collider){
		dynamicsWorld.removeRigidBody(collider.getBody());
		colliders.remove(collider);
	}
	
	public static void addConstraint(Constraint constraint){
		dynamicsWorld.addConstraint(constraint.getConstraint());
//		colliders.add(collider);
	}
	
	public static void removeConstraint(Constraint constraint){
		dynamicsWorld.removeConstraint(constraint.getConstraint());
//		colliders.remove(collider);
	}
	
	public static void addController(CharacterController controller){
		dynamicsWorld.addAction(controller.getController());
		dynamicsWorld.addCollisionObject(controller.getGhost(), CollisionFilterGroups.CHARACTER_FILTER, (short)(CollisionFilterGroups.STATIC_FILTER | CollisionFilterGroups.DEFAULT_FILTER));
		colliders.add(controller.getCollider());
	}
	
//	public static void addController(CharacterController controller, int group, int mask){
//		dynamicsWorld.addAction(controller.getController());
//		dynamicsWorld.addCollisionObject(controller.getGhost(), (short)group, (short)mask);
//		colliders.add(controller.getCollider());
//	}
	
	public static void removeController(CharacterController controller){
		dynamicsWorld.removeAction(controller.getController());
		dynamicsWorld.removeCollisionObject(controller.getGhost());
		colliders.remove(controller.getCollider());
	}
	
	public static void update(float delta){
		for(int i = 0; i < colliders.size(); i++){
			colliders.get(i).clearList();
		}
		
		dynamicsWorld.stepSimulation(delta, 10);
	}
	
	public static void setGravity(float gravity){
		dynamicsWorld.setGravity(new Vector3f(0, gravity, 0));
	}
	
	public static float getGravity(){
//		Vector3f gravity = dynamicsWorld.getGravity(new Vector3f(0, 0, 0));
		
		return dynamicsWorld.getGravity(new Vector3f(0, 0, 0)).y;
	}
	
	public static void rayTest(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld, RayResultCallback resultCallback){
		dynamicsWorld.rayTest(new Vector3f(rayFromWorld.getX(), rayFromWorld.getY(), rayFromWorld.getZ()), new Vector3f(rayToWorld.getX(), rayToWorld.getY(), rayToWorld.getZ()), resultCallback);
	}
}

class Callback extends ContactAddedCallback{
	@Override
	public boolean contactAdded(ManifoldPoint arg0, CollisionObject arg1, int arg2, int arg3, CollisionObject arg4, int arg5, int arg6){
		((Collider)arg1.getUserPointer()).add(((Collider)arg4.getUserPointer()));
		((Collider)arg4.getUserPointer()).add(((Collider)arg1.getUserPointer()));
		
		return false;
	}
}

//	class Callback extends ContactProcessedCallback{
//	@Override
//	public boolean contactProcessed(ManifoldPoint arg0, Object arg1, Object arg2){
//		((Collider)((CollisionObject) arg1).getUserPointer()).add(((Collider)((CollisionObject) arg2).getUserPointer()));
//		((Collider)((CollisionObject) arg2).getUserPointer()).add(((Collider)((CollisionObject) arg1).getUserPointer()));
//		
//		return false;
//	}
//}
