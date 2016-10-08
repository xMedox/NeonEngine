package net.medox.neonengine.core;

import net.medox.neonengine.rendering.RenderingEngine;

public abstract class Game{
	private final Entity root = new Entity();
	private final Entity2D root2D = new Entity2D();
	
	private ProfileTimer updateTimer;
	private ProfileTimer inputTimer;
	
	public void init(){
		if(NeonEngine.OPTION_ENABLE_PROFILING == 1){
			updateTimer = new ProfileTimer();
			inputTimer = new ProfileTimer();
		}
	}
	
	public void cleanUp(){}
	
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
		if(NeonEngine.OPTION_ENABLE_PROFILING == 1){
			inputTimer.startInvocation();
		}
		root.inputAll(delta);
		root2D.inputAll(delta);
		if(NeonEngine.OPTION_ENABLE_PROFILING == 1){
			inputTimer.stopInvocation();
		}
	}
	
	public void update(float delta){
		if(NeonEngine.OPTION_ENABLE_PROFILING == 1){
			updateTimer.startInvocation();
		}
		root.updateAll(delta);
		root2D.updateAll(delta);
		if(NeonEngine.OPTION_ENABLE_PROFILING == 1){
			updateTimer.stopInvocation();
		}
	}

	public void render(){
		RenderingEngine.render(root);
		RenderingEngine.render(root2D);
	}
	
	public void addEntity(Entity object){
		object.addToEngine();
		root.addChild(object);
	}
	
	public void addEntity2D(Entity2D object){
		object.addToEngine();
		root2D.addChild(object);
	}
	
	public void removeEntity(Entity object){
		root.removeChild(object);
	}
	
	public void removeEntity2D(Entity2D object){
		root2D.removeChild(object);
	}
}
