package net.medox.neonengine.components;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class Button extends Entity2DComponent{
	private final Vector3f color;
	
	public Button(){
		this(new Vector3f(1, 1, 1));
	}
	
	public Button(Vector3f color){
		this.color = color;
	}
	
	@Override
	public void input(float delta){
		if(!Input.isGrabbed() && Input.getMouseDown(1)){
			if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
				pressed();
			}
		}
	}
	
	public void pressed(){
		
	}
	
	@Override
	public void render(){
		RenderingEngine.add2DMesh(getTransform(), -1, /*new Vector3f(0.25f, 0.25f, 0.25f)*/new Vector3f(0, 0, 0));
		
		final Transform2D transform = new Transform2D();
		transform.setPos(getTransform().getTransformedPos().add(1/*2*/));
		transform.setScale(getTransform().getScale().sub(2/*4*/).mul(new Vector2f(1, 1)));
		
		RenderingEngine.add2DMesh(transform, -1, color);
	}
}
