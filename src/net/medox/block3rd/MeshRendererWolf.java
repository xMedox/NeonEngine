package net.medox.block3rd;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class MeshRendererWolf extends EntityComponent{
	private Mesh mesh;
	private Material material;
	
	public MeshRendererWolf(Mesh mesh, Material material){
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(mesh.inFrustum(getTransform(), camera)){
			RenderingEngine.addMesh(shader, getTransform(), mesh, material, camera);
		}
	}
	
	public void setDiffuseMap(Texture texture){
		material.setDiffuseMap(texture);
	}
	
	public void setEmissiveMap(Texture texture){
		material.setEmissiveMap(texture);
	}
}
