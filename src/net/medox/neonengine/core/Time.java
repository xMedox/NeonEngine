package net.medox.neonengine.core;

import org.lwjgl.glfw.GLFW;

public class Time{
	private static final float SECOND = 60*0.016666668f;
	
	public static double getTime(){
		return GLFW.glfwGetTime();
	}
	
	public static float getSecond(){
		return SECOND;
	}
}
