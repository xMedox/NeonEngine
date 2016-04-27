package net.medox.propeditor;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.Window;

public class Main{
	public static void main(String[] args){
		NeonEngine.OPTION_ENABLE_VSYNC = 1;
		NeonEngine.OPTION_ENABLE_FXAA = 0;
		NeonEngine.OPTION_ENABLE_MIPMAPPING = 0;
		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
		NeonEngine.OPTION_ENABLE_2D = 1;
		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
		NeonEngine.OPTION_ENABLE_BLOOM = 1;
		NeonEngine.OPTION_MSAA_MULTIPLIER = 1;
		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
		NeonEngine.OPTION_SHADOW_QUALITY = 0;
		
		NeonEngine.init(new TestGame(), 60);
		
		Window.setStartTitle("Project Space");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("./res/icon16.png", "./res/icon32.png");
		Window.setStartCursor("cursor test red.png", 0, 0);
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
}
