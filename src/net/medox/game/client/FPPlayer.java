package net.medox.game.client;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Window;

public class FPPlayer extends Entity{
	public Camera camera;
	
	public InputKey forwardKey = new InputKey(Input.KEYBOARD, Input.KEY_W);
	public InputKey backKey = new InputKey(Input.KEYBOARD, Input.KEY_S);
	public InputKey leftKey = new InputKey(Input.KEYBOARD, Input.KEY_A);
	public InputKey rightKey = new InputKey(Input.KEYBOARD, Input.KEY_D);
	
	public InputKey sprintKey = new InputKey(Input.KEYBOARD, Input.KEY_LEFT_SHIFT);
	
//	public boolean forward = false;
//	public boolean backward = false;
//	public boolean left = false;
//	public boolean right = false;
//	
//	public boolean sprint = false;
	public boolean enableSprint = true;
	
	public float x = 0;
	public float y = 0;
		
	public float sensitivity = 0.25f;
	public InputKey unlockMouseKey = new InputKey(Input.KEYBOARD, Input.KEY_ESCAPE);
	public InputKey setMouse = new InputKey(Input.MOUSE, Input.BUTTON_LEFT);
	
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	
	private float normalSpeed = 15f;
	private float sprintSpeed = 15f*2;
	
	private float speed = normalSpeed;
	
	public FPPlayer(){
		camera = new Camera((float)Math.toRadians(65.0f), 0.01f, 1000.0f);
		addComponent(camera);
	}
	
	@Override
	public void input(float delta){
//		forward = Input.inputKey(forwardKey);
//		backward = Input.inputKey(backKey);
//		left = Input.inputKey(leftKey);
//		right = Input.inputKey(rightKey);
//		
//		sprint = Input.inputKey(sprintKey);
		
		if(enableSprint){
			if(Input.inputKey(sprintKey)){
				speed = sprintSpeed;
			}else{
				speed = normalSpeed;
			}
		}else{
			speed = normalSpeed;
		}
		
		float moveAmt = speed*delta;
		
		if(Input.inputKey(forwardKey)){
			move(getTransform().getRot().getForward(), moveAmt);
		}
		if(Input.inputKey(backKey)){
			move(getTransform().getRot().getForward(), -moveAmt);
		}
		if(Input.inputKey(leftKey)){
			move(getTransform().getRot().getLeft(), moveAmt);
		}
		if(Input.inputKey(rightKey)){
			move(getTransform().getRot().getRight(), moveAmt);
		}
		
		Vector2f centerPosition = Window.getCenterPosition();
		
		if(Input.inputKeyDown(unlockMouseKey)){
			Input.setGrabbed(false);
		}
		
		if(Input.inputKeyDown(setMouse)){
			Input.setMousePosition(centerPosition);
			Input.setGrabbed(true);
		}
				
		if(Input.isGrabbed()){
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if(rotY){
				y = (float) Math.toRadians(deltaPos.getX() * sensitivity);
				getTransform().rotate(yAxis, y);
			}else{
				y = 0;
			}
			
			if(rotX){
//				x = (float) Math.toRadians(-deltaPos.getY() * sensitivity);
				x = (float) Math.toRadians(deltaPos.getY() * sensitivity);
				getTransform().rotate(getTransform().getRot().getRight(), x);
			}else{
				x = 0;
			}
			
			if(rotY || rotX){
				Input.setMousePosition(new Vector2f(Window.getWidth()/2, Window.getHeight()/2));
			}
		}
	}
	
	public void move(Vector3f dir, float amt){
		getTransform().move(dir.mul(amt));
	}
}
