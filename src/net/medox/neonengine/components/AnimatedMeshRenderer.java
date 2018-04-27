package net.medox.neonengine.components;

import net.medox.neonengine.animation.AnimatedModel;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;

public class AnimatedMeshRenderer extends EntityComponent{
	private final AnimatedModel mesh;
	private final Material material;
	
	public AnimatedMeshRenderer(AnimatedModel mesh, Material material){
		this.mesh = mesh;
		this.material = material;
	}
	
	public AnimatedModel getAnimatedModel(){
		return mesh;
	}
	
	@Override
	public void update(float delta){
		mesh.update(delta);
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(RenderingEngine.meshInFrustum(getTransform(), mesh, camera)){
			RenderingEngine.renderMesh(shader, getTransform(), mesh, material, camera);
		}
	}
}
