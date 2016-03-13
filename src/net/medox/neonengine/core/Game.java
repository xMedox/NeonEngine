package net.medox.neonengine.core;

import net.medox.neonengine.rendering.RenderingEngine;

public abstract class Game{
	private final Entity root = new Entity();
	private final Entity2D root2D = new Entity2D();
	
	private final ProfileTimer updateTimer = new ProfileTimer();
	private final ProfileTimer inputTimer = new ProfileTimer();
	
	public void init(){
		
	}
	
	public void cleanUp(){
		
	}
	
	public void dispose(){
		root.removeChildren();
		root2D.removeChildren();
	}
	
	public double displayInputTime(double dividend){
		return inputTimer.displayAndReset("Input Time: ", dividend);
	}
	
	public double displayUpdateTime(double dividend){
		return updateTimer.displayAndReset("Update Time: ", dividend);
	}
	
	public void input(float delta){
		inputTimer.startInvocation();
		getRootEntity().inputAll(delta);
		getRootEntity2D().inputAll(delta);
		inputTimer.stopInvocation();
	}
	
	public void update(float delta){
		updateTimer.startInvocation();
		getRootEntity().updateAll(delta);
		getRootEntity2D().updateAll(delta);
		updateTimer.stopInvocation();
	}

	public void render(){
		RenderingEngine.render(getRootEntity());
		RenderingEngine.render(getRootEntity2D());
	}
	
	public void addEntity(Entity object){
		getRootEntity().addChild(object);
	}
	
	public void addEntity2D(Entity2D object){
		getRootEntity2D().addChild(object);
	}
	
	public void removeEntity(Entity object){
		getRootEntity().removeChild(object);
	}
	
	public Entity getRootEntity(){
//		if(root == null){
//			root = new Entity();
//		}
//		
		return root;
	}
	
	public Entity2D getRootEntity2D(){
//		if(root2D == null){
//			root2D = new Entity2D();
//		}
//		
		return root2D;
	}
}
