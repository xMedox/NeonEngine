package net.medox.game;

import net.medox.neonengine.animation.AnimatedModel;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;

public class AnimationComponent extends EntityComponent{
	private AnimatedModel model;
	
	public AnimationComponent(AnimatedModel model){
		this.model = model;
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_RIGHT_SHIFT)){
			model.stop();
		}
		if(Input.getKeyDown(Input.KEY_PERIOD)){
			model.reset();
		}
		if(Input.getKeyDown(Input.KEY_ENTER)){
			model.start();
		}
	}
}
