package net.medox.block3rd;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.Window;

public class Main{
	public static void main(String[] args){
//		NeonEngine.OPTION_ENABLE_VSYNC = 0;
//		NeonEngine.OPTION_ENABLE_FXAA = 0;
//		NeonEngine.OPTION_ENABLE_MIPMAPPING = 0;
//		NeonEngine.OPTION_ENABLE_SHADOWS = 1;
//		NeonEngine.OPTION_ENABLE_2D = 1;
//		NeonEngine.OPTION_ENABLE_PARTICLES = 0;
//		NeonEngine.OPTION_ENABLE_BLOOM = 1;
//		NeonEngine.OPTION_MSAA_MULTIPLIER = 1;
//		NeonEngine.OPTION_TEXTURE_QUALITY = 0;
//		NeonEngine.OPTION_SHADOW_QUALITY = 0;
//		
//		NeonEngine.init(new TestGame(), /*60*/1000);
		
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
		
		Window.setStartTitle("Project Robot");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("icon16.png", "icon32.png");
//		Window.setStartCursor("cursor test red.png", 0, 0);
//		Window.setStartSizeLimits(256, 256, 854, 480);
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
}
