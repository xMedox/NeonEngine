package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.rendering.Window;

public class FullscreenSetter extends EntityComponent{
	private final InputKey fullscreenKey;
	
	public FullscreenSetter(){
		this(new InputKey(Input.KEYBOARD, Input.KEY_F11));
	}
	
	public FullscreenSetter(InputKey fullscreenKey){
		this.fullscreenKey = fullscreenKey;
	}
	
	@Override
	public void input(float delta){
		if(Input.inputKeyDown(fullscreenKey)){
			Input.setGrabbed(false);
			Window.setFullscreen(!Window.isFullscreen());
		}
	}
}
