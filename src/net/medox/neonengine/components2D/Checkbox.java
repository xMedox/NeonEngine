package net.medox.neonengine.components2D;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class Checkbox extends Entity2DComponent{
	private final Vector3f color;
	private final Vector3f colorChecked;
	private final InputKey key;
	
	private boolean selected;
	private boolean pressed;
	
	public Checkbox(boolean pressed){
		this(pressed, new Vector3f(1, 1, 1), new Vector3f(1, 0, 0));
	}
	
	public Checkbox(boolean pressed, Vector3f color, Vector3f colorChecked){
		this(pressed, color, colorChecked, new InputKey(Input.MOUSE, Input.BUTTON_LEFT));
	}
	
	public Checkbox(boolean pressed, Vector3f color, Vector3f colorChecked, InputKey key){
		this.pressed = pressed;
		this.color = color;
		this.colorChecked = colorChecked;
		this.key = key;
	}
	
	@Override
	public void input(float delta){
		if(!Input.isGrabbed() && Input.inputKeyDown(key)){
			if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
				selected = true;
			}
		}
		
		if(Input.inputKeyUp(key) && selected){
			selected = false;
			if(!Input.isGrabbed()){
				if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
					pressed = !pressed;
					
					changed();
				}
			}
		}
	}
	
	public boolean isPressed(){
		return pressed;
	}
	
	public void setPressed(boolean pressed){
		this.pressed = pressed;
		
		changed();
	}
	
	public void changed(){}
	
	@Override
	public void render(){
		if(RenderingEngine.mesh2DInFrustum(getTransform())){
			RenderingEngine.render2DMesh(getTransform(), -1, new Vector3f(0f, 0f, 0f));
			
			final Transform2D transform = new Transform2D();
			transform.setPos(getTransform().getTransformedPos().add(1));
			transform.setScale(getTransform().getScale().sub(2).mul(new Vector2f(1, 1)));
			
			if(pressed){
				RenderingEngine.render2DMesh(transform, -1, colorChecked);
			}else{
				RenderingEngine.render2DMesh(transform, -1, color);
			}
		}
	}
}
