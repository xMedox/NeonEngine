package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.ParticleMaterial;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;

public class ParticleRenderer extends EntityComponent{
	private final ParticleMaterial material;
	
	public ParticleRenderer(ParticleMaterial material){
		this.material = material;
	}
	
	@Override
	public void render(Shader shader, Camera camera){
		if(RenderingEngine.particleInFrustum(getTransform(), camera)){
			RenderingEngine.renderParticle(getTransform(), material);
		}
	}
}
