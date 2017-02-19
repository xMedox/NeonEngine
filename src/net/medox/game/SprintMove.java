package net.medox.game;

import net.medox.neonengine.components.MoveComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;

public class SprintMove extends MoveComponent{
	private InputKey sprintKey;
	private float normalSpeed;
	private float sprintSpeed;
	
	private boolean enableSprint = true;
	
	public SprintMove(float speed, float sprintSpeed){
		super(speed);
		this.normalSpeed = speed;
		this.sprintSpeed = sprintSpeed;
		sprintKey = new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT);
	}
	
	public SprintMove(float speed, float sprintSpeed, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey){
		super(speed, forwardKey, backKey, leftKey, rightKey);
		this.normalSpeed = speed;
		this.sprintSpeed = sprintSpeed;
		sprintKey = new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT);
	}
	
	public SprintMove(float speed, float sprintSpeed, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey, InputKey sprintKey){
		super(speed, forwardKey, backKey, leftKey, rightKey);
		this.normalSpeed = speed;
		this.sprintSpeed = sprintSpeed;
		this.sprintKey = sprintKey;
	}
	
	@Override
	public void input(float delta){
		if(enableSprint){
			if(Input.getInputKey(sprintKey)){
				setSpeed(sprintSpeed);
			}else{
				setSpeed(normalSpeed);
			}
		}else{
			setSpeed(normalSpeed);
		}
		
		super.input(delta);
	}

	public float getSprintSpeed(){
		return sprintSpeed;
	}

	public void setSprintSpeed(float sprintSpeed){
		this.sprintSpeed = sprintSpeed;
	}

	public boolean isEnableSprint(){
		return enableSprint;
	}

	public void setEnableSprint(boolean enableSprint){
		this.enableSprint = enableSprint;
	}
}
