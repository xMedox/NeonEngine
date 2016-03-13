package net.medox.neonengine.components;

//import net.medox.neonengine.core.Transform;
//import net.medox.neonengine.core.Vector3f;
//import net.medox.neonengine.rendering.Material;
//import net.medox.neonengine.rendering.Mesh;
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
		RenderingEngine.add2DMesh(getTransform(), texture);
	}
}
