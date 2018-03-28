package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.RayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy.CollisionFilterGroups;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

public class PhysicsEngine{
	private static btDiscreteDynamicsWorld dynamicsWorld;
	
	private static List<Collider> colliders;
	private static List<Constraint> constraints;
	private static Map<Integer, Collider> colliderIds;
	private static int nextId;
	
	private static int updatesPerSecond;
	
	public static void init(){
		Bullet.init();
		
		final btCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		final btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
//		final btBroadphaseInterface broadphase = new btDbvtBroadphase();
		final btBroadphaseInterface broadphase = new btAxisSweep3(new Vector3(-10000, -10000, -10000), new Vector3(10000, 10000, 10000));
		final btConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		
		dynamicsWorld.setGravity(new Vector3(0, -9.81f/*-9.80665f*/, 0));
		
		dynamicsWorld.getDispatchInfo().setAllowedCcdPenetration(0.00001f);
		
		dynamicsWorld.getSolverInfo().setNumIterations(20);
		
		new Callback();
		broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());
		
		colliders = new ArrayList<Collider>();
		constraints = new ArrayList<Constraint>();
		colliderIds = new HashMap<Integer, Collider>();
		
		updatesPerSecond = 240;
	}
	
	public static Collider getById(int index){
		return colliderIds.get(index);
	}
	
	public static void addCollider(Collider collider){
		addCollider(collider, CollisionFilterGroups.DefaultFilter, CollisionFilterGroups.AllFilter);
	}
	
	public static void addCollider(Collider collider, int group, int mask){
		dynamicsWorld.addRigidBody(collider.getBody(), (short)group, (short)mask);
		colliders.add(collider);
		colliderIds.put(nextId, collider);
		collider.getBody().setUserValue(nextId);
		
		nextId += 1;
	}
	
	public static void removeCollider(Collider collider){
		colliderIds.remove(collider.getBody().getUserValue());
		dynamicsWorld.removeRigidBody(collider.getBody());
		colliders.remove(collider);
	}
	
	public static void addConstraint(Constraint constraint){
		dynamicsWorld.addConstraint(constraint.getConstraint());
		constraints.add(constraint);
	}
	
	public static void removeConstraint(Constraint constraint){
		dynamicsWorld.removeConstraint(constraint.getConstraint());
		constraints.remove(constraint);
	}
	
	public static void addController(CharacterController controller){
		addController(controller, CollisionFilterGroups.CharacterFilter, CollisionFilterGroups.StaticFilter | CollisionFilterGroups.DefaultFilter);
	}
	
	public static void addController(CharacterController controller, int group, int mask){
		dynamicsWorld.addAction(controller.getController());
		dynamicsWorld.addCollisionObject(controller.getGhost(), (short)group, (short)mask);
		colliders.add(controller.getCollider());
		colliderIds.put(nextId, controller.getCollider());
		controller.getGhost().setUserValue(nextId);
		
		nextId += 1;
	}
	
	public static void removeController(CharacterController controller){
		colliderIds.remove(controller.getGhost().getUserValue());
		dynamicsWorld.removeAction(controller.getController());
		dynamicsWorld.removeCollisionObject(controller.getGhost());
		colliders.remove(controller.getCollider());
	}
	
	public static void update(float delta){
		for(int i = 0; i < colliders.size(); i++){
			colliders.get(i).clearList();
		}
		
		dynamicsWorld.stepSimulation(delta, updatesPerSecond, 1f/updatesPerSecond);
	}
	
	public static void setGravity(float gravity){
		dynamicsWorld.setGravity(new Vector3(0, gravity, 0));
	}
	
	public static float getGravity(){
		return dynamicsWorld.getGravity().y;
	}
	
	public static void rayTest(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld, RayResultCallback resultCallback){
		dynamicsWorld.rayTest(new Vector3(rayFromWorld.getX(), rayFromWorld.getY(), rayFromWorld.getZ()), new Vector3(rayToWorld.getX(), rayToWorld.getY(), rayToWorld.getZ()), resultCallback);
	}
	
	public static int getUpdatesPerSecond(){
		return updatesPerSecond;
	}
	
	public static void dispose(){
		if(dynamicsWorld != null){
			dynamicsWorld.dispose();
		}
	}
}

class Callback extends ContactListener{
	@Override
	public boolean onContactAdded(btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1){
		PhysicsEngine.getById(colObj0.getUserValue()).add(PhysicsEngine.getById(colObj1.getUserValue()));
		PhysicsEngine.getById(colObj1.getUserValue()).add(PhysicsEngine.getById(colObj0.getUserValue()));
		
		return false;
	}
}
