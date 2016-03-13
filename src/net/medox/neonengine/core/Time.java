package net.medox.neonengine.core;

import org.lwjgl.glfw.GLFW;

public class Time{
//	private static final long SECOND = 1000000000L;
	
//	public static double getTime(){
//		return (double)System.nanoTime()/(double)SECOND;
//	}
	
	public static double getTime(){
		return GLFW.glfwGetTime();
	}
}
