package net.medox.block;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class FPS extends Entity2DComponent{
	private Vector3f color;
	
	public FPS(){
		this(new Vector3f(1, 1, 1));
	}
	
	public FPS(Vector3f color){
		this.color = color;
	}
	
	@Override
	public void render(){
		RenderingEngine.drawString(7, 3, Integer.toString(RenderingEngine.getFPS()), 2, 2, color.sub(0.6f));
		RenderingEngine.drawString(5, 5, Integer.toString(RenderingEngine.getFPS()), 2, 2, color);
		
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			RenderingEngine.drawString(7, 3+18+11, "OpenGL", 2, 2, color.sub(0.6f));
			RenderingEngine.drawString(5, 5+18+11, "OpenGL", 2, 2, color);
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			RenderingEngine.drawString(7, 3+18+11, "Vulkan", 2, 2, color.sub(0.6f));
			RenderingEngine.drawString(5, 5+18+11, "Vulkan", 2, 2, color);
		}
	}
}
