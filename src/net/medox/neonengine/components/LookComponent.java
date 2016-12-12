package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Window;

public class LookComponent extends EntityComponent{
	public static final Vector3f Y_AXIS = new Vector3f(0, 1, 0);
	
	private final InputKey unlockMouseKey;
	private final InputKey setMouse;
	
	private float sensitivity;
	private boolean invertY;
	
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
		
		if(Input.inputKeyDown(unlockMouseKey) && Input.isGrabbed()){
			Input.setGrabbed(false);
		}else if(Input.inputKeyDown(setMouse) && !Input.isGrabbed()){
			Input.setMousePosition(centerPosition);
			Input.setGrabbed(true);
		}
		
		if(Input.isGrabbed()){
			final Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

//			boolean rotY = deltaPos.getX() != 0;
//			boolean rotX = deltaPos.getY() != 0;

			if(deltaPos.getX() == 0){
				y = 0;
			}else{
				y = (float)Math.toRadians(deltaPos.getX() * sensitivity);
				getTransform().rotate(Y_AXIS, y);
				
				Input.setMousePosition(centerPosition);
			}
			
			if(deltaPos.getY() == 0){
				x = 0;
			}else{
				float xSave = 0;
				
				if(invertY){
					xSave = (float)Math.toRadians(deltaPos.getY() * sensitivity);
				}else{
					xSave = (float)-Math.toRadians(deltaPos.getY() * sensitivity);
				}
				
//				float rot = Math.max(-57, Math.min(57, (float)Math.toDegrees(getTransform().getRot().getForward().getY()) + (float)Math.toDegrees(x)));
				
				if(xSave > 0){
					x = Util.clamp(getTransform().getRot().getBack().getY() + xSave, -1.1f, 1f);
					
					x -= getTransform().getRot().getBack().getY();
				}else{
					x = Util.clamp(getTransform().getRot().getBack().getY() + xSave, -1f, 1.1f);
					
					x -= getTransform().getRot().getBack().getY();
				}
				
				
				
//				System.out.println(Math.toDegrees(x) + "|" + Math.toDegrees(rot));
				
//				rot = Util.clamp(getTransform().getRot().getUp().getY() + x, 0.1f, 10000);
//				
//				rot -= getTransform().getRot().getUp().getY();
				
//				if(getTransform().getRot().getUp().getY() + x < 0.1f){
//					x = 0;
//				}
//				
//				System.out.println((float)Math.toDegrees(getTransform().getRot().getForward().getY()));
				
//				System.out.println((float)Math.toRadians(rot) + "|" + x);
				
//				if((float)Math.toDegrees(getTransform().getRot().getUp().getY()) < 0){
////					getTransform().rotate(getTransform().getRot().getRight(), (float)Math.toRadians(-89));
//					getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//////					getTransform().setRot(new Quaternion(new Vector3f(-89, rot.getY(), rot.getZ()), (float)Math.toRadians(1)));
//////				}
//////				
//////				if(getTransform().getRot().getForward().getY() < 1){
////////					getTransform().setRot(new Quaternion(new Vector3f((float)Math.toRadians(-89), rot.getY(), rot.getZ()), 1));
//////					getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//////////					getTransform().getRot().mul(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-89)));
//				}else{
//					getTransform().rotate(getTransform().getRot().getRight(), x);
//				}
				
				getTransform().rotate(getTransform().getRot().getRight(), x);
				
				Input.setMousePosition(centerPosition);
			}
			
//			System.out.println(getTransform().getRot().getUp().toString());
//			System.out.println(getTransform().getRot().getUp().getY() + "|" + getTransform().getRot().getDown().getY() +  "|" + getTransform().getRot().getForward().getY());
			
//			if(rotY || rotX){
//				Input.setMousePosition(centerPosition);
//			}
		}
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
