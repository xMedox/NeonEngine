package net.medox.game;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;

public class SoundComponent extends EntityComponent{
	private Sound audio;
	
	private InputKey play;
	
	public SoundComponent(){
		this("hit.ogg");
	}
	
	public SoundComponent(String fileName){
		this(fileName, new InputKey(Input.MOUSE, Input.BUTTON_MIDDLE));
	}
	
	public SoundComponent(String fileName, InputKey play){
		audio = new Sound(fileName);
		
		this.play = play;
//		audio.setPitch(1.0f);
//		audio.setGain(1.0f);
//		audio.setPosition(new Vector3f(0.0f, 0.0f, 0.0f));
//		audio.setVelocity(new Vector3f(0.0f, 0.0f, 0.0f));
//		audio.setLooping(false);
//		audio.setRolloffFactor(4.0f);
	}
	
	@Override
	public void input(float delta){
		if(Input.getInputKeyDown(play)){
			audio.play();
		}
		
//		if(Input.getMouseDown(Input.BUTTON_4)){
//			audio.pause();
//		}
//		
//		if(Input.getMouseDown(Input.BUTTON_5)){
//			audio.rewind();
//		}
//		
//		if(Input.getKeyDown(Input.KEY_O)){
//			audio.setTimeOffset(132.1500319f);
//		}
	}
	
	@Override
	public void update(float delta){
		audio.setPosition(getTransform().getTransformedPos());
		
//		System.out.println(audio.isPlaying() + "|" + audio.isPaused() + "|" + audio.getTimeOffset() + "/" + audio.getLength());
	}
	
	public Sound getSound(){
		return audio;
	}
}
