package net.medox.game;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.rendering.Material;

public class EmissiveComponent extends EntityComponent{
	private Material material;
	private float time;
	
	public EmissiveComponent(Material material){
		this.material = material;
		time = 0;
	}
	
	@Override
	public void update(float delta){
		time += delta;
		
		material.setEmissive((float)Math.sin(time) * 0.5f + 0.5f);
	}
}
