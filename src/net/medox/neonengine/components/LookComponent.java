package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Window;

public class LookComponent extends EntityComponent{
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	private final InputKey unlockMouseKey;
	private final InputKey setMouse;
	
	private final float limit = 89.9f;
	
	private float sensitivity;
	private boolean invertY;
	
	private float xRotation;
	private float yRotation;
	
	private float x;
	private float y;
	
	public LookComponent(float sensitivity){
		this(sensitivity, false);
	}
	
	public LookComponent(float sensitivity, boolean invertY){
		this(sensitivity, invertY, new InputKey(Input.KEYBOARD, Input.KEY_ESCAPE));
	}
	
	public LookComponent(float sensitivity, boolean invertY, InputKey unlockMouseKey){
		this(sensitivity, invertY, unlockMouseKey, new InputKey(Input.MOUSE, Input.BUTTON_LEFT));
	}
	
	public LookComponent(float sensitivity, boolean invertY, InputKey unlockMouseKey, InputKey setMouse){
		this.sensitivity = sensitivity;
		this.invertY = invertY;
		this.unlockMouseKey = unlockMouseKey;
		this.setMouse = setMouse;
	}
	
	@Override
	public void input(float delta){
		final Vector2f centerPosition = Window.getCenterPosition();
		
		getTransform().setRot(new Quaternion(0, 0, 0, 1));
		
		if(Input.inputKeyDown(unlockMouseKey) && Input.isGrabbed()){
			Input.setGrabbed(false);
		}else if(Input.inputKeyDown(setMouse) && !Input.isGrabbed()){
			Input.setMousePosition(centerPosition);
			Input.setGrabbed(true);
		}
		
		if(Input.isGrabbed()){
			final Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			if(deltaPos.getX() == 0){
				y = 0;
			}else{
				y = /*(float)Math.toRadians(*/deltaPos.getX() * sensitivity/*)*/;
				
				yRotation += y;
				
				y = (float)Math.toRadians(y);
				
				Input.setMousePosition(centerPosition);
			}
			
			if(deltaPos.getY() == 0){
				x = 0;
			}else{
				if(invertY){
					x = /*(float)Math.toRadians(*/deltaPos.getY() * sensitivity/*)*/;
				}else{
					x = /*(float)-Math.toRadians(*/-(deltaPos.getY() * sensitivity)/*)*/;
				}
				
				if(xRotation+x > limit){
					x -= xRotation-limit+x;
				}
				if(xRotation+x < -limit){
					x -= xRotation+limit+x;
				}
				
				xRotation += x;
				
				x = (float)Math.toRadians(x);
				
				Input.setMousePosition(centerPosition);
			}
		}
		
		getTransform().rotate(Y_AXIS, (float)Math.toRadians(yRotation));
		getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(xRotation));
	}
	
	public float getX(){
		return x;
	}
	
	public float getY(){
		return y;
	}

	public float getSensitivity(){
		return sensitivity;
	}

	public void setSensitivity(float sensitivity){
		this.sensitivity = sensitivity;
	}
}
