package net.medox.block;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.physics.Collider;

public class Tester extends EntityComponent{
	private Collider c1;
	private Collider c2;
	
	public Tester(Collider c1, Collider c2){
		this.c1 = c1;
		this.c2 = c2;
	}
	
	@Override
	public void update(float delta){
		System.out.println(c1.collidesWith(c2));
	}
}
