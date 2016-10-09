package net.medox.game;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Window;

public class ChangeMode extends EntityComponent{
	private InputKey wireframeKey;
	
	private Shader filter;
	private boolean filterEnabled;
	
	public ChangeMode(){
		this.wireframeKey = new InputKey(Input.KEYBOARD, Input.KEY_F2);
		
		filter = new Shader("filterGrey");
	}
	
	public ChangeMode(InputKey screenshotKey){
		this.wireframeKey = screenshotKey;
		
		filter = new Shader("filterGrey");
	}
	
	@Override
	public void input(float delta){
		if(Input.inputKeyDown(wireframeKey)){
			RenderingEngine.setWireframeMode(!RenderingEngine.isWireframeMode());
		}
//		if(Input.getKeyDown(Input.KEY_F3)){
//			RenderingEngine.setScanMode(!RenderingEngine.getScanMode());
//		}
		if(Input.getKeyDown(Input.KEY_F3)){
			if(NeonEngine.is2DEnabled()){
				NeonEngine.enable2D(false);
			}else{
				NeonEngine.enable2D(true);
			}
		}
		if(Input.getKeyDown(Input.KEY_F4)){
			if(!filterEnabled){
				RenderingEngine.addFilter(filter);
				
				filterEnabled = !filterEnabled;
			}else{
				RenderingEngine.removeFilter(filter);
				
				filterEnabled = !filterEnabled;
			}
		}
		if(Input.getKey(Input.KEY_F5)){
			Window.setPos(Window.getX()+(int)(60*delta), Window.getY()+(int)(60*delta));
		}
		if(Input.getKey(Input.KEY_F6)){
			Window.setSize(Window.getWidth()+(int)(60*delta), Window.getHeight()+(int)(60*delta));
		}
	}
}
