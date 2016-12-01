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
		RenderingEngine.drawString(12, 0, Integer.toString(RenderingEngine.getFPS()), color.sub(0.6f), 2, 2);
		RenderingEngine.drawString(10, 2, Integer.toString(RenderingEngine.getFPS()), color, 2, 2);
		
		RenderingEngine.drawString(12, 0+18+10, "OpenGL", color.sub(0.6f), 2, 2);
		RenderingEngine.drawString(10, 2+18+10, "OpenGL", color, 2, 2);
	}
}
