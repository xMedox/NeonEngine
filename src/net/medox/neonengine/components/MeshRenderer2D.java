package net.medox.neonengine.components;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Texture;

public class MeshRenderer2D extends Entity2DComponent{
	private final Texture texture;
	
	public MeshRenderer2D(Texture texture){
		this.texture = texture;
	}
	
	@Override
	public void render(){		
		if(RenderingEngine.mesh2DInFrustum(getTransform())){
			RenderingEngine.render2DMesh(getTransform(), texture);
		}
	}
}
