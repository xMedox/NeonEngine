package net.medox.neonengine.core;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Shader;

public class Entity{
	private final List<Entity> children;
	private final List<EntityComponent> components;
	private final Transform transform;
	
	private Entity parent;
	
	public Entity(){
		children = new ArrayList<Entity>();
		components = new ArrayList<EntityComponent>();
		transform = new Transform();
	}
	
//	public void setPos(Vector3f translation){
//		transform.setPos(translation);
//	}
//	
//	public void setScale(Vector3f scale){
//		transform.setScale(scale);
//	}
//	
//	public void setRot(Quaternion rotation){
//		transform.setRot(rotation);
//	}
//	
//	public void move(Vector3f add){
//		transform.move(add);
//	}
//	
//	public void scale(Vector3f add){
//		transform.scale(add);
//	}
//	
//	public void rotate(Vector3f axis, float angle){
//		transform.rotate(axis, angle);
//	}
//	
//	public void rotate(Quaternion quaternion){
//		transform.rotate(quaternion);
//	}
	
	public Entity addChild(Entity child){
		children.add(child);
		child.setParent(this);
		child.getTransform().setParent(transform);
		child.setEngine();
		
		return this;
	}
	
	public Entity addComponent(EntityComponent component){
		components.add(component);
		component.setParent(this);
		
		return this;
	}
	
	public Entity removeChild(Entity child){
		child.cleanUp();
		children.remove(child);
		
//		child = null;
		
		return this;
	}
	
	public Entity removeChildren(){
		for(int i = 0; i < children.size(); i++){
			children.get(i).cleanUp();
		}
		
		children.clear();
		
		return this;
	}
	
	public Entity removeComponent(EntityComponent component){
		component.cleanUp();
		components.remove(component);
		
//		component = null;
		
		return this;
	}
	
	public Entity removeComponents(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).cleanUp();
		}
		
		components.clear();
		
		return this;
	}
	
	public Entity removeSelf(){
		parent.removeChild(this);
		
		return this;
	}
	
	public void cleanUp(){
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
	
	public void renderAll(Shader shader, Camera camera){
		render(shader, camera);
		
		for(int i = 0; i < children.size(); i++){
			children.get(i).renderAll(shader, camera);
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
	
	public void render(Shader shader, Camera camera){
		for(int i = 0; i < components.size(); i++){
			components.get(i).render(shader, camera);
		}
	}
	
	public ArrayList<Entity> getAllAttached(){
		final ArrayList<Entity> result = new ArrayList<Entity>();
		
		for(int i = 0; i < children.size(); i++){
			result.addAll(children.get(i).getAllAttached());
		}
		
		result.add(this);
		return result;
	}
	
	public List<Entity> getChildren(){
		return children;
	}
	
	public List<EntityComponent> getComponents(){
		return components;
	}
	
	public Transform getTransform(){
		return transform;
	}
	
	public void setParent(Entity parent){
		this.parent = parent;
		getTransform().setParent(transform);
	}
	
	public Entity getParent(){
		return parent;
	}
	
	public void setEngine(){
		for(int i = 0; i < components.size(); i++){
			components.get(i).addToEngine();
		}
	}
}
