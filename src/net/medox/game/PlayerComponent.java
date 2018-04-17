package net.medox.game;

import net.medox.neonengine.core.Entity;
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
	
	private final Collider collider;
	private final Camera camera;
	
	private float speed;
	private float sprintSpeed;
	
	private Entity entity;
	
	private int group;
	private int mask;
	
	public PlayerComponent(Collider collider, Camera camera, float speed, float sprintSpeed){
		this(collider, camera, speed, sprintSpeed, -1, -1, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D), new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT), new InputKey(Input.KEYBOARD, Input.KEY_SPACE));
	}
	
	public PlayerComponent(Collider collider, Camera camera, float speed, float sprintSpeed, int group, int mask){
		this(collider, camera, speed, sprintSpeed, group, mask, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D), new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT), new InputKey(Input.KEYBOARD, Input.KEY_SPACE));
	}
	
	public PlayerComponent(Collider collider, Camera camera, float speed, float sprintSpeed, int group, int mask, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey, InputKey sprintKey, InputKey jumpKey){
		this.collider = collider;
		
//		controller.setMaxJumpHeight(4);
//		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(4f);
		
//		controller.setMaxSlope((float)Math.toRadians(60));
		
		this.camera = camera;
		this.speed = speed;
		this.sprintSpeed = sprintSpeed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
		this.sprintKey = sprintKey;
		this.jumpKey = jumpKey;
		
		entity = getParent();
		
		this.group = group;
		this.mask = mask;
	}
	
	@Override
	public void input(float delta){
		float moveSpeed = speed;
		
		if(Input.getInputKey(sprintKey)){
			moveSpeed = sprintSpeed;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		boolean changed = false;
		
		if(Input.getInputKey(forwardKey) && !Input.getInputKey(backKey)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(leftKey) && !Input.getInputKey(rightKey)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(backKey) && !Input.getInputKey(forwardKey)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		if(Input.getInputKey(rightKey) && !Input.getInputKey(leftKey)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
			changed = true;
		}
		
		if(Input.getInputKeyDown(jumpKey)){
			collider.applyCentralImpulse(new Vector3f(0, 400, 0));
		}
		
		if(changed){
			dir = dir.normalized();
			
			move(dir.mul(moveSpeed), delta);
		}else{
			move(dir, delta);
		}
	}
	
	@Override
	public void update(float delta){
		entity.getTransform().setPos(collider.getPos());
	}
	
	public void move(Vector3f vel, float delta){
//		controller.setWalkDirection(vel);
//		collider.applyCentralImpulse(vel.mul(40).mul(delta));
//		collider.setPos(collider.getPos().add(vel.mul(delta)));
		collider.setLinearVelocity(new Vector3f(vel.getX(), collider.getLinearVelocity().getY(), vel.getZ()));
	}
	
	public Collider getCollider(){
		return collider;
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
	
	public Entity getEnity(){
		return entity;
	}
	
	public void setEnity(Entity entity){
		this.entity = entity;
	}
	
	@Override
	public void addToEngine(){
		if(group != -1 && mask != -1){
			PhysicsEngine.addCollider(collider, group, mask);
		}else{
			PhysicsEngine.addCollider(collider);
		}
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeCollider(collider);
	}
}
