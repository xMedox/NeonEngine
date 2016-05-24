package net.medox.game;

import net.medox.neonengine.components.ParticleRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Time;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.ParticleMaterial;
import net.medox.neonengine.rendering.Texture;

public class SmokeComponent extends EntityComponent{
	private float timer = 0;
	
	@Override
	public void update(float delta){
		if(timer > 0){
			timer -= delta;
		}else{
			timer = Util.randomFloat()*0.6f*Time.getSecond();
			
			Entity particle = new Entity();
			
			ParticleMaterial pMat = new ParticleMaterial();
			pMat.setDiffuseMap(new Texture("base.png"));
			pMat.setEmissive(1);
			
			particle.addComponent(new ParticleRenderer(pMat));
			particle.addComponent(new Smoke());
			
			particle.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
//			particle.getTransform().setScale(new Vector3f(2, 2, 2));
//			particle.getTransform().setPos(new Vector3f(i*2-2.5f*2, 5, 1+j*2));
			
//			particle.getTransform().setPos(new Vector3f(Util.randomFloat()*0.7f, 0, Util.randomFloat()*0.7f));
			
			getParent().addChild(particle);
		}
	}
}
