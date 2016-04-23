package net.medox.game;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.Window;

public class Main{
	public static void main(String[] args){
		System.out.println("Starting up");
		
		NeonEngine.init(new TestGame(), /*600*/60);
		
		Window.setStartTitle("Project Knight");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("./res/icon_16.png", "./res/icon_32.png");
		Window.setStartCursor("cursor test 2.png", 0, 0);
		
//		Window.setStartTitle("Project Guns");
//		Window.setStartFullscreen(true);
//		Window.setStartIcon("icon_16.png", "icon_32.png", "icon_128.png");
//		Window.setStartCursor("cursor.png", 0, 0);
		
//		Window.createWindow();
		NeonEngine.createWindow();
		
//		System.out.println("--------------------------------------------------------------");
////		System.out.println("Game version:     " + "Alpha 0.3");
////		System.out.println("Engine version:   " + CoreEngine.getVersion());
//		System.out.println("OS name:          " + System.getProperty("os.name"));
//		System.out.println("OS version:       " + System.getProperty("os.version"));
//		System.out.println("OS arch:          " + System.getProperty("os.arch"));
//		System.out.println("Arch data model:  " + System.getProperty("sun.arch.data.model"));
//		System.out.println("Java version:     " + System.getProperty("java.version"));
////		System.out.println("LWJGL version:    " + Sys.getVersion());
////		System.out.println("LWJGL version:    " + Sys.VERSION_MAJOR + "|" + Sys.VERSION_MINOR + "|" + Sys.VERSION_REVISION);
//		System.out.println("OpenGL version:   " + GL11.glGetString(GL11.GL_VERSION));
//		System.out.println("Max Texture size: " + GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
//		System.out.println("--------------------------------------------------------------");
		
		NeonEngine.start();
		
//		Testing.runAllTests();
		
//		 while(!Window.isCloseRequested()){
//			 Window.render();
//		 }
	}
}
