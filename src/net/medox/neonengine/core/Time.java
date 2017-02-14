package net.medox.neonengine.core;

import java.util.Calendar;

import org.lwjgl.glfw.GLFW;

public class Time{
	private static final float SECOND = 1f;
	
	public static double getTime(){
		return GLFW.glfwGetTime();
	}
	
	public static float getSecond(){
		return SECOND;
	}
	
	public static int getCurrentYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static int getCurrentMonth(){
		return Calendar.getInstance().get(Calendar.MONTH)+1;
	}
	
	public static int getCurrentDay(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getCurrentHour(){
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getCurrentMinute(){
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
	public static int getCurrentSecond(){
		return Calendar.getInstance().get(Calendar.SECOND);
	}
}
