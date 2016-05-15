package net.medox.block3rd;

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
		RenderingEngine.drawString(7, 3, Integer.toString(RenderingEngine.getFPS()), color.sub(0.6f), 2, 2);
		RenderingEngine.drawString(5, 5, Integer.toString(RenderingEngine.getFPS()), color, 2, 2);
		
		RenderingEngine.drawString(7, 3+18+11, "OpenGL", color.sub(0.6f), 2, 2);
		RenderingEngine.drawString(5, 5+18+11, "OpenGL", color, 2, 2);
	}
}
