package net.medox.neonengine.rendering.resourceManagement;

import org.lwjgl.glfw.GLFW;

import net.medox.neonengine.core.ReferenceCounter;

public class CursorData extends ReferenceCounter{
	private final long id;
	
	private boolean cleanedUp;
	
	public CursorData(long id){
		super();
		
		this.id = id;
	}
	
	public long getId(){
		return id;
	}
	
	public void dispose(){
		if(!cleanedUp){
//			if(id != 0){
				GLFW.glfwDestroyCursor(id);
				
//				id = 0;
//			}
			
			cleanedUp = true;
		}
	}
}
