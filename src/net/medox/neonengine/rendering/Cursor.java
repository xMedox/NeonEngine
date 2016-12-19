package net.medox.neonengine.rendering;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryUtil;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.ImageUtil.ImageData;
import net.medox.neonengine.rendering.resourceManagement.CursorData;

public class Cursor{
	private static final Map<String, CursorData> loadedCursors = new ConcurrentHashMap<String, CursorData>();
	
	private final String fileName;
	private final int xPos;
	private final int yPos;
	
	private CursorData resource;
	private boolean cleanedUp;
	
	public Cursor(String fileName){
		this(fileName, 0, 0);
	}
	
	public Cursor(String fileName, int xPos, int yPos){
		this.fileName = fileName;
		this.xPos = -xPos;
		this.yPos = yPos;
		
		if(Window.gotCreated()){
			create();
		}
	}
	
	public void create(){
		resource = loadedCursors.get(fileName);
		
		if(resource == null){
			final ImageData pixels = ImageUtil.imageToByteBuffer("./res/" + fileName);
			final GLFWImage image = GLFWImage.malloc().set(pixels.width, pixels.height, pixels.data);
			
			long cursor = GLFW.glfwCreateCursor(image, xPos, yPos);
			image.free();
			
			if(cursor == MemoryUtil.NULL){
				NeonEngine.throwError("Error: Failed to create the cursor.");
			}
			
			resource = new CursorData(cursor);
			loadedCursors.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	
	public long getId(){
		return resource.getId();
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(resource.removeReference() && !fileName.equals("")){
				resource.dispose();
				loadedCursors.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public static void dispose(){
		for(final CursorData data : loadedCursors.values()){
			data.dispose();
		}
	}
}
