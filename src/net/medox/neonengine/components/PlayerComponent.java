package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.Collider;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.Camera;

public class PlayerComponent extends EntityComponent{
	private final InputKey forwardKey;
	private final InputKey backKey;
	private final InputKey leftKey;
	private final InputKey rightKey;
	private final InputKey sprintKey;
	private final InputKey jumpKey;
	
	private final Camera camera;
	private final CharacterController controller;
	
	public PlayerComponent(Collider collider, Camera camera){
		this(collider, camera, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D), new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT), new InputKey(Input.KEYBOARD, Input.KEY_SPACE));
	}
	
	public PlayerComponent(Collider collider, Camera camera, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey, InputKey sprintKey, InputKey jumpKey){
		controller = new CharacterController(collider, 0.3f);
		
		controller.setMaxJumpHeight(4);
//		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(4f);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		this.camera = camera;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.sprintKey = sprintKey;
		this.jumpKey = jumpKey;
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
		
		if(Input.inputKey(sprintKey)){
			speed = 10;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		boolean changed = false;
		
		if(Input.inputKey(forwardKey) && !Input.inputKey(backKey)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.inputKey(leftKey) && !Input.inputKey(rightKey)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.inputKey(backKey) && !Input.inputKey(forwardKey)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.inputKey(rightKey) && !Input.inputKey(leftKey)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		
		if(Input.inputKeyDown(jumpKey)){
			controller.jump();
		}
		
		if(changed){
			move(dir.normalized().mul(speed));
		}else{
			move(dir);
		}
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
