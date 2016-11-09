package net.medox.neonengine.rendering.resourceManagement;

import org.lwjgl.glfw.GLFW;

import net.medox.neonengine.core.ReferenceCounter;

public class CursorData extends ReferenceCounter{
	private final long id;
	
	public CursorData(long id){
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void dispose(){
		GLFW.glfwDestroyCursor(id);
	}
}
