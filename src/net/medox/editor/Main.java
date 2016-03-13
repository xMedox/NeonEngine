package net.medox.editor;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.rendering.Window;

public class Main{
	public static void main(String[] args){
		System.out.println("Starting up");
		
		CoreEngine.init(60, new TestGame());
		
		Window.setStartTitle("NeonEngine Editor");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("./res/icon_16.png", "./res/icon_32.png");
		Window.setStartCursor("cursor test 2.png", 0, 0);
		
		CoreEngine.createWindow();
		
//		System.out.println("-----------------------------------------------");
//		System.out.println("OS name:        " + System.getProperty("os.name"));
//		System.out.println("OS version:     " + System.getProperty("os.version"));
//		System.out.println("OS arch:        " + System.getProperty("os.arch"));
//		System.out.println("OS arch:        " + System.getProperty("sun.arch.data.model"));
//		System.out.println("Java version:   " + System.getProperty("java.version"));
//		System.out.println("LWJGL version:  " + Version.getVersion());
//		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
//		System.out.println("-----------------------------------------------");
		
		CoreEngine.start();
	}
}
