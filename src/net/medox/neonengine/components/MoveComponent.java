package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector3f;

public class MoveComponent extends EntityComponent{
	private final InputKey forwardKey;
	private final InputKey backKey;
	private final InputKey leftKey;
	private final InputKey rightKey;
	
	private float speed;
	
	public MoveComponent(float speed){
		this(speed, new InputKey(Input.KEYBOARD, Input.KEY_W), new InputKey(Input.KEYBOARD, Input.KEY_S), new InputKey(Input.KEYBOARD, Input.KEY_A), new InputKey(Input.KEYBOARD, Input.KEY_D));
	}
	
	public MoveComponent(float speed, InputKey forwardKey, InputKey backKey, InputKey leftKey, InputKey rightKey){
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
	}
	
	@Override
	public void input(float delta){
		final float moveAmt = speed*delta;
		
		if(Input.getInputKey(forwardKey)){
			move(getTransform().getRot().getForward(), moveAmt);
		}
		if(Input.getInputKey(backKey)){
			move(getTransform().getRot().getBack(), moveAmt);
		}
		if(Input.getInputKey(leftKey)){
			move(getTransform().getRot().getLeft(), moveAmt);
		}
		if(Input.getInputKey(rightKey)){
			move(getTransform().getRot().getRight(), moveAmt);
		}
	}
	
	public void move(Vector3f dir, float amt){
		getTransform().move(dir.mul(amt));
	}
	
	public float getSpeed(){
		return speed;
	}
	
	public void setSpeed(float speed){
		this.speed = speed;
	}
}
