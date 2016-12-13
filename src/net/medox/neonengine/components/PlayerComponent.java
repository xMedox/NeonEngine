package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.Collider;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private Camera camera;
	
	private CharacterController controller;
	
	public PlayerComponent(Collider collider, Camera camera){
		controller = new CharacterController(collider, 0.3f);
		
		controller.setMaxJumpHeight(4);
//		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(4f);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
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
