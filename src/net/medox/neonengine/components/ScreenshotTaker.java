package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.rendering.Window;

public class ScreenshotTaker extends EntityComponent{
	private final InputKey screenshotKey;
	
	public ScreenshotTaker(){
		this(new InputKey(Input.KEYBOARD, Input.KEY_F1));
	}
	
	public ScreenshotTaker(InputKey screenshotKey){
		this.screenshotKey = screenshotKey;
	}
	
	@Override
	public void input(float delta){
		if(Input.inputKeyDown(screenshotKey)){
			Window.takeScreenshot();
		}
	}
}
