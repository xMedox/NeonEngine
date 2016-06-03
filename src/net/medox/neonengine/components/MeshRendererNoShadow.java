package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;

public class MeshRendererNoShadow extends EntityComponent{
	private final Mesh mesh;
	private final Material material;
	
	public MeshRendererNoShadow(Mesh mesh, Material material){
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(RenderingEngine.getRenderingState() != RenderingEngine.SHADOW_STATE){
			if(mesh.inFrustum(getTransform(), camera)){
				RenderingEngine.renderMesh(shader, getTransform(), mesh, material, camera);
			}
		}
	}
}
