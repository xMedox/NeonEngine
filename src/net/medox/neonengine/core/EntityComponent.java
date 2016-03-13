package net.medox.neonengine.core;

import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Shader;

public abstract class EntityComponent{
	private Entity parent;
	
	public void input(float delta){}
	public void update(float delta){}
	public void render(Shader shader, Camera camera){}
	
	public Entity getParent(){
		return parent;
	}
	
	public void setParent(Entity parent){
		this.parent = parent;
	}
	
	public Transform getTransform(){
		return parent.getTransform();
	}
	
	public void removeSelf(){
		parent.removeComponent(this);
	}
	
	public void addToEngine(){
		
	}
	
	public void cleanUp(){
		
	}
}
