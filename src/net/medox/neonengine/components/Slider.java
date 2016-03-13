package net.medox.neonengine.components;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class Slider extends Entity2DComponent{
	private final int orientation;
	private final Vector3f color;
	private final Vector3f colorSlider;
	
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
		this.orientation = orientation;
		this.color = color;
		this.colorSlider = colorSlider;
		this.progress = progress;
	}
	
	@Override
	public void input(float delta){
		if(!Input.isGrabbed() && Input.getMouseDown(1)){
////			Vector2f mousePos = Input.getMousePosition();
//			
////			Vector2f pos = getTransform().getTransformedPos();
////			Vector2f scale = getTransform().getScale();
//			
			if(!grabbed){
				if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
	////			if(mousePos.getX() >= pos.getX() && mousePos.getY() >= pos.getY() && mousePos.getX() < pos.getX() + scale.getX() && mousePos.getY() < pos.getY() + scale.getY()){
	//				pressed = !pressed;
					
					grabbed = true;
					oldPos = Input.getMousePosition();
					
					final float oldProgress = progress;
					
					if(orientation == 0){
//						float pro = (oldPos.sub(getTransform().getPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0)))).getX();
						
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0))).getX(), 0, 1);
					}else if(orientation == 1){
//						float pro = (oldPos.sub(getTransform().getPos().add(new Vector2f(0, 7))).div(getTransform().getScale().sub(new Vector2f(0, 14)))).getY();
						
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(0, 7))).div(getTransform().getScale().sub(new Vector2f(0, 14))).getY(), 0, 1);
					}
					
					if(oldProgress != progress){
						changed();
					}
	//				
	//				changed(progress);
				}
			}
		}

		if(!Input.isGrabbed() && Input.getMouse(1)){
			if(grabbed){
				if(!oldPos.equals(Input.getMousePosition())){
					oldPos = Input.getMousePosition();
					
//					progress = box.getPos().add(box.getScale().mul(0.5f)).div(getTransform().getPos());
//					System.out.println(oldPos.sub(getTransform().getPos().add(new Vector2f(7, 0))).div(getTransform().getScale().add(new Vector2f(14, 0)))/*.div(getTransform().getPos())*/);
//					Vector2f vec = oldPos.sub(getTransform().getPos().add(new Vector2f(7, 0))).div(getTransform().getScale().add(new Vector2f(14, 0)));
					
					if(orientation == 0){
//						float pro = (oldPos.sub(getTransform().getPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0)))).getX();
						
						progress = Util.clamp(oldPos.sub(getTransform().getTransformedPos().add(new Vector2f(7, 0))).div(getTransform().getScale().sub(new Vector2f(14, 0))).getX(), 0, 1);
					}else if(orientation == 1){
//						float pro = (oldPos.sub(getTransform().getPos().add(new Vector2f(0, 7))).div(getTransform().getScale().sub(new Vector2f(0, 14)))).getY();
						
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
	
	public void changed(){
		
	}
	
	@Override
	public void render(){
		RenderingEngine.add2DMesh(getTransform(), -1, /*new Vector3f(0.25f, 0.25f, 0.25f)*/new Vector3f(0f, 0f, 0f));
		
		final Transform2D transform = new Transform2D();
		
		transform.setPos(getTransform().getTransformedPos().add(1/*2*/));
		
		if(orientation == 0){
			transform.setScale(getTransform().getScale().sub(new Vector2f(7, 0)).sub(2/*4*/).mul(new Vector2f(progress, 1)));
		}else if(orientation == 1){
			transform.setScale(getTransform().getScale().sub(new Vector2f(0, 7)).sub(2/*4*/).mul(new Vector2f(1, progress)));
		}
		
		RenderingEngine.add2DMesh(transform, -1, color);
		
		if(orientation == 0){
//			transform.setPos(getTransform().getPos().add(getTransform().getScale().sub(2/*4*/).mul(new Vector2f(progress, 1))));
			transform.setPos(getTransform().getTransformedPos().add(new Vector2f(7, 0)).add(getTransform().getScale().sub(new Vector2f(14, 0)).mul(new Vector2f(progress, 0)).sub(new Vector2f(7, 0))));
//			transform.setScale(getTransform().getScale().sub(2/*4*/).sub(new Vector2f(7, 0)).mul(new Vector2f(progress, 1)).mul(new Vector2f(0, 1)).add(new Vector2f(16, 0)));
//			transform.setPos(transform.getTransformedPos().add(transform.getScale().mul(new Vector2f(1, 0)).sub(new Vector2f(8, 1))));
			transform.setScale(getTransform().getScale().mul(new Vector2f(0, 1)).add(new Vector2f(14, 0)));
		}else if(orientation == 1){
//			transform.setPos(getTransform().getPos().add(getTransform().getScale().sub(2/*4*/).sub(new Vector2f(7, 0)).mul(new Vector2f(progress, 0)).sub(new Vector2f(7, 0))));
			transform.setPos(getTransform().getTransformedPos().add(new Vector2f(0, 7)).add(getTransform().getScale().sub(new Vector2f(0, 14)).mul(new Vector2f(0, progress)).sub(new Vector2f(0, 7))));
//			transform.setScale(getTransform().getScale().sub(2/*4*/).sub(new Vector2f(0, 7)).mul(new Vector2f(1, progress)).mul(new Vector2f(1, 0)).add(new Vector2f(0, 16)));
//			transform.setPos(transform.getTransformedPos().add(transform.getScale().mul(new Vector2f(0, 1)).sub(new Vector2f(1, 8))));
			transform.setScale(getTransform().getScale().mul(new Vector2f(1, 0)).add(new Vector2f(0, 14)));
		}
		
		RenderingEngine.add2DMesh(transform, -1, new Vector3f(0, 0, 0));
		
		transform.setPos(transform.getTransformedPos().add(1));
		transform.setScale(transform.getScale().sub(2));
		
		RenderingEngine.add2DMesh(transform, -1, colorSlider);
		
//		RenderingEngine.add2DMesh(getTransform(), -3);
//		
//		Transform2D transform = new Transform2D();
//		transform.setPos(getTransform().getTransformedPos().add(1));
//		transform.setScale(getTransform().getScale().sub(2));
//		
//		RenderingEngine.add2DMesh(transform, -1);
//		
//		transform.setPos(getTransform().getTransformedPos().add(1));
//		
//		if(orientation == 0){
//			transform.setScale(getTransform().getScale().sub(2).mul(new Vector2f(progress, 1)));
//		}else if(orientation == 1){
//			transform.setScale(getTransform().getScale().sub(2).mul(new Vector2f(1, progress)));
//		}
//		
//		RenderingEngine.add2DMesh(transform, -2);
		
		
//		RenderingEngine.add2DMesh(getTransform(), -1);
//		
//		Transform2D transform = new Transform2D();
//		transform.setPos(getTransform().getTransformedPos().add(1));
//		
//		if(orientation == 0){
//			transform.setScale(getTransform().getScale().mul(new Vector2f(progress, 1)).sub(new Vector2f(2, 2)));
//		}else if(orientation == 1){
//			transform.setScale(getTransform().getScale().mul(new Vector2f(1, progress)).sub(new Vector2f(2, 2)));
//		}
//		
//		RenderingEngine.add2DMesh(transform, -2);
		
//		progress
		
//		transform.setPos(getTransform().getTransformedPos().add(getTransform().getScale().mul(0.25f*0.5f)));
//		transform.setScale(getTransform().getScale().mul(0.75f));
//		
//		if(pressed){
//			RenderingEngine.add2DMesh(transform, -2);
//		}else{
//			RenderingEngine.add2DMesh(transform, -1);
//		}
	}
}
