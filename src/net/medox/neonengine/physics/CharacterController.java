package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btConvexShape;
import com.badlogic.gdx.physics.bullet.collision.btPairCachingGhostObject;
import com.badlogic.gdx.physics.bullet.dynamics.btKinematicCharacterController;

public class CharacterController{
	private static final List<CharacterController> characterControllers = new ArrayList<CharacterController>();
	
	private final btPairCachingGhostObject ghostObject;
	private final btKinematicCharacterController characterController;
	private final Collider collider;
	
//	private float jumpSpeed;
	private float jumpHeight;
	
	public CharacterController(Collider collider, float stepHeight){
		this.collider = collider;
		
		ghostObject = new btPairCachingGhostObject();
		
		final net.medox.neonengine.math.Quaternion rotation = new net.medox.neonengine.math.Quaternion(new net.medox.neonengine.math.Vector3f(1, 0, 0), (float)Math.toRadians(90));
		
		final Matrix4 trans = collider.getBody().getWorldTransform();
		trans.set(trans.getTranslation(new Vector3()), new Quaternion(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()), trans.getScale(new Vector3()));
		
		ghostObject.setWorldTransform(trans);
		
		ghostObject.setCollisionShape(collider.getCollisionShape());
		ghostObject.setCollisionFlags(CollisionFlags.CF_CHARACTER_OBJECT);
		
		ghostObject.setCollisionFlags(ghostObject.getCollisionFlags() | CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		
//		ghostObject.forceActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
//		characterController = new btKinematicCharacterController(ghostObject, (ConvexShape)collider.getCollisionShape(), stepHeight);
		characterController = new btKinematicCharacterController(ghostObject, (btConvexShape)collider.getCollisionShape(), stepHeight, new Vector3(0, 1, 0));
		
		characterController.setGravity(new Vector3(0, PhysicsEngine.getGravity(), 0));
		
//		physics.getWorld().addCollisionObject(ghostObject, BroadphaseProxy.CharacterFilter, btBroadphaseProxy.StaticFilter | BroadphaseProxy.DefaultFilter);
		
		characterControllers.add(this);
	}
	
//	public void setFlags(int flags){
//		ghostObject.setCollisionFlags(flags);
//	}
	
	public void setWalkDirection(net.medox.neonengine.math.Vector3f dir){
		characterController.setWalkDirection(new Vector3(dir.getX(), dir.getY(), dir.getZ()));
	}
	
	public void jump(){
		if(characterController.canJump()){
			characterController.jump(new Vector3(0, jumpHeight, 0));
//			characterController.setJumpSpeed(jumpSpeed);
		}
	}
	
//	public void setJumpSpeed(float jumpSpeed){
//		this.jumpSpeed = jumpSpeed;
//	}
//	
//	public void setFallSpeed(float fallSpeed){
//		characterController.setFallSpeed(fallSpeed);
//	}
	
	public void setMaxJumpHeight(float maxJumpHeight){
		jumpHeight = maxJumpHeight;
	}
	
	public void setStepHeight(float stepHeight){
		characterController.setStepHeight(stepHeight);
	}
	
	public void setMaxSlope(float slopeRadians){
		characterController.setMaxSlope(slopeRadians);
	}
	
//	public void setFallSpeed(float fallSpeed){
//		kinematicCharacterController.setFallSpeed(fallSpeed);
//	}
	
	public void setUpAxis(net.medox.neonengine.math.Vector3f axis){
		characterController.setUp(new Vector3(axis.getX(), axis.getY(), axis.getZ()));
	}
	
	public void setGravity(float gravity){
		characterController.setGravity(new Vector3(0, gravity, 0));
	}
	
	public net.medox.neonengine.math.Vector3f getPos(){
		final Vector3 pos = ghostObject.getWorldTransform().getTranslation(new Vector3());
		
		return new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z);
	}
	
	public net.medox.neonengine.math.Quaternion getRot(){
		final Quaternion rot = ghostObject.getWorldTransform().getRotation(new Quaternion());
		
		return new net.medox.neonengine.math.Quaternion(rot.x, rot.y, rot.z, rot.w);
	}
	
	public net.medox.neonengine.core.Transform getTransform(){
		final net.medox.neonengine.core.Transform result = new net.medox.neonengine.core.Transform();
		
		final Vector3 pos = ghostObject.getWorldTransform().getTranslation(new Vector3());
		final Quaternion rot = ghostObject.getWorldTransform().getRotation(new Quaternion());
		
		result.setPos(new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z));
		result.setRot(new net.medox.neonengine.math.Quaternion(rot.x, rot.y, rot.z, rot.w));
		
		return result;
	}
	
	public float getGravity(){
		return characterController.getGravity().y;
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
	
	public btKinematicCharacterController getController(){
		return characterController;
	}
	
	public btPairCachingGhostObject getGhost(){
		return ghostObject;
	}
	
	public Collider getCollider(){
		return collider;
	}
	
	public void setTransform(net.medox.neonengine.core.Transform worldTransform){
		ghostObject.setWorldTransform(new Matrix4().set(new Vector3(worldTransform.getPos().getX(), worldTransform.getPos().getY(), worldTransform.getPos().getZ()), new Quaternion(worldTransform.getRot().getX(), worldTransform.getRot().getY(), worldTransform.getRot().getZ(), worldTransform.getRot().getW())));
	}
	
	public void setPos(net.medox.neonengine.math.Vector3f transform){
		final Matrix4 trans = ghostObject.getWorldTransform();
		trans.setTranslation(new Vector3(transform.getX(), transform.getY(), transform.getZ()));
		
		ghostObject.setWorldTransform(trans);
	}
	
	public void setRot(net.medox.neonengine.math.Quaternion rotation){
		final Matrix4 trans = ghostObject.getWorldTransform();
		trans.set(trans.getTranslation(new Vector3()), new Quaternion(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()), trans.getScale(new Vector3()));
		
		ghostObject.setWorldTransform(trans);
	}
	
//	public float getJumpSpeed(){
//		return jumpSpeed;
//	}
//	
//	public float getFallSpeed(){
//		return characterController.getFallSpeed();
//	}
	
	public float getMaxJumpHeight(){
		return jumpHeight;
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		characterControllers.remove(this);
		
		super.finalize();
	}
	
	public void cleanUp(){
		ghostObject.dispose();
		characterController.dispose();
	}
	
	public static void dispose(){
		for(final CharacterController data : characterControllers){
			data.cleanUp();
		}
	}
}
