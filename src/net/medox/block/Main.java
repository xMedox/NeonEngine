package net.medox.block;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.rendering.Window;

public class Main{
	public static void main(String[] args){
		System.out.println("Starting up");
		
//		CoreEngine.OPTION_ENABLE_VSYNC = 0;
//		CoreEngine.OPTION_ENABLE_MSAA = 1;
//		CoreEngine.OPTION_ENABLE_FXAA = 0;
//		CoreEngine.OPTION_ENABLE_MIPMAPPING = 0;
//		CoreEngine.OPTION_ENABLE_SHADOWS = 1;
//		CoreEngine.OPTION_ENABLE_2D = 1;
//		CoreEngine.OPTION_ENABLE_PARTICLES = 0;
//		CoreEngine.OPTION_TEXTURE_QUALITY = 0;
//		CoreEngine.OPTION_SHADOW_QUALITY = 0;
//		
//		CoreEngine.init(/*60*/1000, new TestGame());
		
		CoreEngine.OPTION_ENABLE_VSYNC = 1;
		CoreEngine.OPTION_ENABLE_MSAA = 1;
		CoreEngine.OPTION_ENABLE_FXAA = 0;
		CoreEngine.OPTION_ENABLE_MIPMAPPING = 0;
		CoreEngine.OPTION_ENABLE_SHADOWS = 1;
		CoreEngine.OPTION_ENABLE_2D = 1;
		CoreEngine.OPTION_ENABLE_PARTICLES = 0;
		CoreEngine.OPTION_TEXTURE_QUALITY = 0;
		CoreEngine.OPTION_SHADOW_QUALITY = 0;
		
		CoreEngine.init(60, new TestGame());
		
		Window.setStartTitle("Project Space");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(true);
		Window.setStartResizable(true);
		Window.setStartIcon("./res/icon16.png", "./res/icon32.png");
//		Window.setStartCursor("cursor test red.png", 0, 0);
//		Window.setStartSizeLimits(256, 256, 854, 480);
		
		CoreEngine.createWindow();
		
		CoreEngine.start();
	}
}
