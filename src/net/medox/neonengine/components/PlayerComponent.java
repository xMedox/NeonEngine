package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.CapsuleCollider;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private Camera camera;
	private CapsuleCollider capsule;
	
	private CharacterController controller;
	
	public PlayerComponent(Camera camera){
		capsule = new CapsuleCollider(0.5f, 1f);
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		capsule.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		capsule.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		capsule.setSleepingThresholds(0, 0);
//		controlBall.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
//		PhysicsEngine.addObject(cylinder);
		
		controller = new CharacterController(capsule, 0.3f);
		
		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(1);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
//		controller.setFallSpeed(9.80665f);
//		controller.setGravity(9.80665f);
		
//		controller.setFallSpeed(9.81f);
//		controller.setGravity(PhysicsEngine.getGravity());
		
//		System.out.println(capsule.getGravity());
//		System.out.println(controller.getGravity());
		
		this.camera = camera;
	}
	
	public CharacterController getController(){
		return controller;
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(controller.getPos());
	}
	
	@Override
	public void input(float delta){
		float speed = 6;
		
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			speed = 10;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		
		if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
		}
		if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
		}
		if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
		}
		if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKeyDown(Input.KEY_SPACE)){
			controller.jump();
		}
		
		move(dir.mul(speed));
	}
	
	public void move(Vector3f vel){
		controller.setWalkDirection(vel.mul(0.015f));
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addController(controller);
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeController(controller);
	}
}
