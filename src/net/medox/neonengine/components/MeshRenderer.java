package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;

public class MeshRenderer extends EntityComponent{
	private final Mesh mesh;
	private final Material material;
	
	public MeshRenderer(Mesh mesh, Material material){
		this.mesh = mesh;
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(mesh.inFrustum(getTransform(), camera)){
			shader.bind();
			shader.updateUniforms(getTransform(), material, camera);
			mesh.draw();
		}
	}
}
