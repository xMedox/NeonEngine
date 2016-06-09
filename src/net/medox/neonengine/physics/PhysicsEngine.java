package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.RayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btAxisSweep3;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseInterface;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy.CollisionFilterGroups;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btCollisionDispatcher;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObjectWrapper;
import com.badlogic.gdx.physics.bullet.collision.btDefaultCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btGhostPairCallback;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.badlogic.gdx.physics.bullet.dynamics.btConstraintSolver;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btSequentialImpulseConstraintSolver;

public class PhysicsEngine{
	private static btDiscreteDynamicsWorld dynamicsWorld;
	
	private static ArrayList<Collider> colliders;
	private static Map<Integer, Collider> collidersSave;
	private static int nextValue;
	
	public static void init(){
		Bullet.init();
		
//		final btBroadphaseInterface broadphase = new btDbvtBroadphase();
		final btBroadphaseInterface broadphase = new btAxisSweep3(new Vector3(-10000, -10000, -10000), new Vector3(10000, 10000, 10000));
		final btCollisionConfiguration collisionConfiguration = new btDefaultCollisionConfiguration();
		final btCollisionDispatcher dispatcher = new btCollisionDispatcher(collisionConfiguration);
		final btConstraintSolver solver = new btSequentialImpulseConstraintSolver();
		dynamicsWorld = new btDiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
		
//		dynamicsWorld.setGravity(new Vector3(0, -10, 0));
//		dynamicsWorld.setGravity(new Vector3(0, -9.80665f, 0));
		dynamicsWorld.setGravity(new Vector3(0, -9.81f, 0));
//		dynamicsWorld.setGravity(new Vector3(0, -1f, 0));
		
		dynamicsWorld.getDispatchInfo().setAllowedCcdPenetration(0.001f);
		
		dynamicsWorld.getSolverInfo().setNumIterations(60);
		
		new Callback();
		broadphase.getOverlappingPairCache().setInternalGhostPairCallback(new btGhostPairCallback());
		
		colliders = new ArrayList<Collider>();
		collidersSave = new ConcurrentHashMap<Integer, Collider>();
	}
	
	public static Collider getById(int index){
		return collidersSave.get(index);
	}
	
	public static void addCollider(Collider collider){
		dynamicsWorld.addRigidBody(collider.getBody());
		colliders.add(collider);
		collidersSave.put(nextValue, collider);
		collider.getBody().setUserValue(nextValue);
		
		nextValue += 1;
	}
	
	public static void addCollider(Collider collider, int group, int mask){
		dynamicsWorld.addRigidBody(collider.getBody(), (short)group, (short)mask);
		colliders.add(collider);
		collidersSave.put(nextValue, collider);
		collider.getBody().setUserValue(nextValue);
		
		nextValue += 1;
	}
	
	public static void removeCollider(Collider collider){
		collidersSave.remove(collider.getBody().getUserValue());
		dynamicsWorld.removeRigidBody(collider.getBody());
		colliders.remove(collider);
	}
	
	public static void addConstraint(Constraint constraint){
		dynamicsWorld.addConstraint(constraint.getConstraint());
	}
	
	public static void removeConstraint(Constraint constraint){
		dynamicsWorld.removeConstraint(constraint.getConstraint());
	}
	
	public static void addController(CharacterController controller){
		dynamicsWorld.addAction(controller.getController());
		dynamicsWorld.addCollisionObject(controller.getGhost(), (short)CollisionFilterGroups.CharacterFilter, (short)(CollisionFilterGroups.StaticFilter | CollisionFilterGroups.DefaultFilter));
		colliders.add(controller.getCollider());
		collidersSave.put(nextValue, controller.getCollider());
		controller.getGhost().setUserValue(nextValue);
		
		nextValue += 1;
	}
	
//	public static void addController(CharacterController controller, int group, int mask){
//		dynamicsWorld.addAction(controller.getController());
//		dynamicsWorld.addCollisionObject(controller.getGhost(), (short)group, (short)mask);
//		colliders.add(controller.getCollider());
//	}
	
	public static void removeController(CharacterController controller){
		collidersSave.remove(controller.getGhost().getUserValue());
		dynamicsWorld.removeAction(controller.getController());
		dynamicsWorld.removeCollisionObject(controller.getGhost());
		colliders.remove(controller.getCollider());
	}
	
	public static void update(float delta){
		for(int i = 0; i < colliders.size(); i++){
			colliders.get(i).clearList();
		}
		
		dynamicsWorld.stepSimulation(delta, 60);
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
}

class Callback extends ContactListener{
	@Override
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObjectWrapper colObj0Wrap, int partId0, int index0, btCollisionObjectWrapper colObj1Wrap, int partId1, int index1){
		PhysicsEngine.getById(colObj0Wrap.getCollisionObject().getUserValue()).add(PhysicsEngine.getById(colObj1Wrap.getCollisionObject().getUserValue()));
		PhysicsEngine.getById(colObj1Wrap.getCollisionObject().getUserValue()).add(PhysicsEngine.getById(colObj0Wrap.getCollisionObject().getUserValue()));
		
		return false;
    }
}
