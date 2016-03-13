package net.medox.neonengine.components;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class Progressbar extends Entity2DComponent{
	private final int orientation;
	private final Vector3f color;
	
	private float progress;
	
	public Progressbar(float progress){
		this(progress, 0);
	}
	
	public Progressbar(float progress, Vector3f color){
		this(progress, 0, color);
	}
	
	public Progressbar(float progress, int orientation){
		this(progress, orientation, new Vector3f(1, 1, 1));
	}
	
	public Progressbar(float progress, int orientation, Vector3f color){
		this.orientation = orientation;
		this.progress = progress;
		this.color = color;
	}
	
	@Override
	public void input(float delta){
		progress += delta/2;
		
		if(progress > 1){
			progress -= 1;
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
		RenderingEngine.add2DMesh(getTransform(), -1, /*new Vector3f(0.25f, 0.25f, 0.25f)*/new Vector3f(0, 0, 0));
		
		final Transform2D transform = new Transform2D();
		transform.setPos(getTransform().getTransformedPos().add(1/*2*/));
		
		if(orientation == 0){
			transform.setScale(getTransform().getScale().sub(2/*4*/).mul(new Vector2f(progress, 1)));
		}else if(orientation == 1){
			transform.setScale(getTransform().getScale().sub(2/*4*/).mul(new Vector2f(1, progress)));
		}
		
		RenderingEngine.add2DMesh(transform, -1, color);
		
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
