package net.medox.neonengine.components2D;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class Slider extends Entity2DComponent{
	private final int orientation;
	private final Vector3f color;
	private final Vector3f colorSlider;
	private final InputKey key;
	
	private float progress;
	private boolean grabbed;
	private Vector2f oldPos;
	
	public Slider(float progress){
		this(progress, new Vector3f(1, 0, 0));
	}
	
	public Slider(float progress, Vector3f color){
		this(progress, color, new Vector3f(1, 1, 1));
	}
	
	public Slider(float progress, Vector3f color, Vector3f colorSlider){
		this(progress, color, colorSlider, 0);
	}
	
	public Slider(float progress, Vector3f color, Vector3f colorSlider, int orientation){
		this(progress, color, colorSlider, orientation, new InputKey(Input.MOUSE, Input.BUTTON_LEFT));
	}
	
	public Slider(float progress, Vector3f color, Vector3f colorSlider, int orientation, InputKey key){
		this.orientation = orientation;
		this.color = color;
		this.colorSlider = colorSlider;
		this.progress = progress;
		this.key = key;
	}
	
	@Override
	public void input(float delta){
		if(!Input.isGrabbed() && Input.inputKeyDown(key)){
			if(!grabbed){
				if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
					grabbed = true;
					oldPos = Input.getMousePosition();
					
					final float oldProgress = progress;
					
					if(orientation == 0){
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0))).getX(), 0, 1);
					}else if(orientation == 1){
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(0, 7))).div(getTransform().getScale().sub(new Vector2f(0, 14))).getY(), 0, 1);
					}
					
					if(oldProgress != progress){
						changed();
					}
				}
			}
		}

		if(!Input.isGrabbed() && Input.inputKey(key)){
			if(grabbed){
				if(!oldPos.equals(Input.getMousePosition())){
					oldPos = Input.getMousePosition();
					
					if(orientation == 0){
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0))).getX(), 0, 1);
					}else if(orientation == 1){
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(0, 7))).div(getTransform().getScale().sub(new Vector2f(0, 14))).getY(), 0, 1);
					}
					
					changed();
				}
			}
		}else{
			grabbed = false;
		}
	}
	
	public float getProgress(){
		return progress;
	}
	
	public void setProgress(float progress){
		this.progress = progress;
		
		changed();
	}
	
	public void changed(){}
	
	@Override
	public void render(){
		if(RenderingEngine.mesh2DInFrustum(getTransform())){
			RenderingEngine.render2DMesh(getTransform(), -1, new Vector3f(0f, 0f, 0f));
			
			final Transform2D transform = new Transform2D();
			
			transform.setPos(getTransform().getTransformedPos().add(1/*2*/));
			
			if(orientation == 0){
				transform.setScale(getTransform().getScale().sub(new Vector2f(7, 0)).sub(2).mul(new Vector2f(progress, 1)));
			}else if(orientation == 1){
				transform.setScale(getTransform().getScale().sub(new Vector2f(0, 7)).sub(2).mul(new Vector2f(1, progress)));
			}
			
			RenderingEngine.render2DMesh(transform, -1, color);
			
			if(orientation == 0){
				transform.setPos(getTransform().getTransformedPos().add(new Vector2f(7, 0)).add(getTransform().getScale().sub(new Vector2f(14, 0)).mul(new Vector2f(progress, 0)).sub(new Vector2f(7, 0))));
				transform.setScale(getTransform().getScale().mul(new Vector2f(0, 1)).add(new Vector2f(14, 0)));
			}else if(orientation == 1){
				transform.setPos(getTransform().getTransformedPos().add(new Vector2f(0, 7)).add(getTransform().getScale().sub(new Vector2f(0, 14)).mul(new Vector2f(0, progress)).sub(new Vector2f(0, 7))));
				transform.setScale(getTransform().getScale().mul(new Vector2f(1, 0)).add(new Vector2f(0, 14)));
			}
			
			RenderingEngine.render2DMesh(transform, -1, new Vector3f(0, 0, 0));
			
			transform.setPos(transform.getTransformedPos().add(1));
			transform.setScale(transform.getScale().sub(2));
			
			RenderingEngine.render2DMesh(transform, -1, colorSlider);
		}
	}
}
