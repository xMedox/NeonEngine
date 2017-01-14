package net.medox.game;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;

public class SoundComponent extends EntityComponent{
	private Sound audio;
	
	public SoundComponent(){
		audio = new Sound("hit.ogg");
		
//		audio.setPitch(1.0f);
//		audio.setGain(1.0f);
//		audio.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
//		audio.setVelocity(new Vector3f(0.0f, 0.0f, 0.0f));
//		audio.setLooping(false);
//		audio.setRolloffFactor(4.0f);
	}
	
	@Override
	public void input(float delta){
		if(Input.getMouseDown(Input.BUTTON_MIDDLE)){
			audio.play();
		}
	}
	
	@Override
	public void update(float delta){
		audio.setPosition(getTransform().getTransformedPos());
	}
}
