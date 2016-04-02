package net.medox.neonengine.core;

import java.util.ArrayList;
import java.util.List;

public class Entity2D{
	private final List<Entity2D> children;
	private final List<Entity2DComponent> components;
	private final Transform2D transform;
	
	private Entity2D parent;
	private boolean added;
	
	public Entity2D(){
		children = new ArrayList<Entity2D>();
		components = new ArrayList<Entity2DComponent>();
		transform = new Transform2D();
	}
	
	public Entity2D addChild(Entity2D child){
		children.add(child);
		child.setParent(this);
		child.getTransform().setParent(transform);
		
		if(added == true){
			child.addToEngine();
		}
		
		return this;
	}
	
	public Entity2D addComponent(Entity2DComponent component){
		components.add(component);
		component.setParent(this);
		
		if(added == true){
			component.addToEngine();
		}
		
		return this;
	}
	
	public Entity2D removeChild(Entity2D child){
		child.cleanUp();
		children.remove(child);
		
		return this;
	}
	
	public Entity2D removeChildren(){
		for(int i = 0; i < children.size(); i++){
			children.get(i).cleanUp();
		}
		
		children.clear();
		
		return this;
	}
	
	public Entity2D removeComponent(Entity2DComponent component){
		component.cleanUp();
		components.remove(component);
		
		return this;
	}
	
	public Entity2D removeComponents(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).cleanUp();
		}
		
		components.clear();
		
		return this;
	}
	
	public Entity2D removeSelf(){
		parent.removeChild(this);
		
		return this;
	}
	
	public void cleanUp(){
		added = false;
		
		for(int i = 0; i < components.size(); i++){
			components.get(i).cleanUp();
		}
		for(int i = 0; i < children.size(); i++){
			children.get(i).cleanUp();
		}
		
		components.clear();
		children.clear();
	}
	
	public void inputAll(float delta){
		input(delta);
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).inputAll(delta);
		}
	}
	
	public void updateAll(float delta){
		update(delta);
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).updateAll(delta);
		}
	}
	
	public void renderAll(){
		render();
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).renderAll();
		}
	}
	
	public void input(float delta){
		transform.update();
		
		for(int i = 0; i < components.size(); i++){
			components.get(i).input(delta);
		}
	}
	
	public void update(float delta){
		for(int i = 0; i < components.size(); i++){
			components.get(i).update(delta);
		}
	}
	
	public void render(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).render();
		}
	}
	
	public ArrayList<Entity2D> getAllAttached(){
		final ArrayList<Entity2D> result = new ArrayList<Entity2D>();
		
		for(int i = 0; i < children.size(); i++){
			result.addAll(children.get(i).getAllAttached());
		}
		
		result.add(this);
		return result;
	}
	
	public List<Entity2D> getChildren(){
		return children;
	}
	
	public List<Entity2DComponent> getComponents(){
		return components;
	}
	
	public Transform2D getTransform(){
		return transform;
	}
	
	public void setParent(Entity2D parent){
		this.parent = parent;
		getTransform().setParent(transform);
	}
	
	public Entity2D getParent(){
		return parent;
	}
	
	public void addToEngine(){
		added = true;
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).addToEngine();
		}
		for(int i = 0; i < components.size(); i++){
			components.get(i).addToEngine();
		}
	}
}
