package net.medox.neonengine.core;

public abstract class Entity2DComponent{
	private Entity2D parent;
	
	public void input(float delta){}
	public void update(float delta){}
	public void render(){}
	
	public Entity2D getParent(){
		return parent;
	}
	
	public void setParent(Entity2D parent){
		this.parent = parent;
	}
	
	public Transform2D getTransform(){
		return parent.getTransform();
	}
	
	public void removeSelf(){
		parent.removeComponent(this);
	}
	
	public void addToEngine(){}
	
	public void cleanUp(){}
}
