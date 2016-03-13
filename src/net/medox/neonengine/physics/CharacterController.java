package net.medox.neonengine.physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
//import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.ConvexShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.linearmath.Transform;

public class CharacterController{	
	private PairCachingGhostObject ghostObject;
	private KinematicCharacterController characterController;
	private Collider collider;
	
	public CharacterController(Collider collider, float stepHeight){
		this.collider = collider;
		
		ghostObject = new PairCachingGhostObject();
		ghostObject.setWorldTransform(collider.getBody().getWorldTransform(new Transform()));
		
		ghostObject.setCollisionShape(collider.getCollisionShape());
		ghostObject.setCollisionFlags(CollisionFlags.CHARACTER_OBJECT);
		
		ghostObject.setUserPointer(collider);
		ghostObject.setCollisionFlags(ghostObject.getCollisionFlags() | CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
		
//		ghostObject.forceActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
		characterController = new KinematicCharacterController(ghostObject, (ConvexShape)collider.getCollisionShape(), stepHeight);
		
		characterController.setGravity(-PhysicsEngine.getGravity());
		
//		kinematicCharacterController.setUpAxis(1);
		
//		physics.getWorld().addCollisionObject(ghostObject, BroadphaseProxy.CharacterFilter, btBroadphaseProxy.StaticFilter | BroadphaseProxy.DefaultFilter);
	}
	
//	public void setFlags(int flags){
//		ghostObject.setCollisionFlags(flags);
//	}
	
	public void setWalkDirection(net.medox.neonengine.math.Vector3f dir){
		characterController.setWalkDirection(new Vector3f(dir.getX(), dir.getY(), dir.getZ()));
	}
	
	public void jump(){
		characterController.jump();
	}
	
	public void setJumpSpeed(float jumpSpeed){
		characterController.setJumpSpeed(jumpSpeed);
	}
	
	public void setFallSpeed(float fallSpeed){
		characterController.setFallSpeed(fallSpeed);
	}
	
	public void setMaxJumpHeight(float maxJumpHeight){
		characterController.setMaxJumpHeight(maxJumpHeight);
	}
	
	public void setMaxSlope(float slopeRadians){
		characterController.setMaxSlope(slopeRadians);
	}
	
//	public void setFallSpeed(float fallSpeed){
//		kinematicCharacterController.setFallSpeed(fallSpeed);
//	}
	
	public void setUpAxis(float axis){
		characterController.setUpAxis(0);
	}
	
	public void setGravity(float gravity){
		characterController.setGravity(-gravity);
	}
	
	public net.medox.neonengine.math.Vector3f getPos(){
		final Vector3f pos = ghostObject.getWorldTransform(new Transform()).origin;
		
		return new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z);
	}
	
	public net.medox.neonengine.math.Quaternion getRot(){
		final Quat4f rot = ghostObject.getWorldTransform(new Transform()).getRotation(new Quat4f());
		
		return new net.medox.neonengine.math.Quaternion(rot.x, rot.y, rot.z, rot.w);
	}
	
	public net.medox.neonengine.core.Transform getTransform(){
		final net.medox.neonengine.core.Transform result = new net.medox.neonengine.core.Transform();
		
		final Transform t = ghostObject.getWorldTransform(new Transform());
		final Vector3f pos = t.origin;
		final Quat4f rot = t.getRotation(new Quat4f());
		
		result.setPos(new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z));
		result.setRot(new net.medox.neonengine.math.Quaternion(rot.x, rot.y, rot.z, rot.w));
		
		return result;
	}
	
	public float getGravity(){
		return -characterController.getGravity();
	}
	
	public float getMaxSlope(){
		return characterController.getMaxSlope();
	}
	
	public boolean onGround(){
		return characterController.onGround();
	}
	
	public boolean collidesWith(Collider c){
		return collider.collidesWith(c);
	}
	
	public KinematicCharacterController getController(){
		return characterController;
	}
	
	public PairCachingGhostObject getGhost(){
		return ghostObject;
	}
	
	public Collider getCollider(){
		return collider;
	}
	
	public void setTransform(net.medox.neonengine.core.Transform worldTransform){
		final net.medox.neonengine.math.Vector3f pos = worldTransform.getPos();
		final net.medox.neonengine.math.Quaternion rot = worldTransform.getRot();
		
		ghostObject.setWorldTransform(new Transform(new Transform(new Matrix4f(new Quat4f(rot.getX(), rot.getY(), rot.getZ(), rot.getW()), new Vector3f(pos.getX(), pos.getY(), pos.getZ()), 1.0f))));
	}
	
	public void setPos(net.medox.neonengine.math.Vector3f transform){
		final Transform t = ghostObject.getWorldTransform(new Transform());
		t.origin.x = transform.getX();
		t.origin.y = transform.getY();
		t.origin.z = transform.getZ();
		
		ghostObject.setWorldTransform(t);
	}
	
	public void setRot(net.medox.neonengine.math.Quaternion rotation){
		final Transform t = ghostObject.getWorldTransform(new Transform());
		t.setRotation(new Quat4f(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()));
		
		ghostObject.setWorldTransform(t);
	}
}
