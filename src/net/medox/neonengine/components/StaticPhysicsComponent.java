package net.medox.neonengine.components;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.physics.Collider;
import net.medox.neonengine.physics.PhysicsEngine;

public class StaticPhysicsComponent extends EntityComponent{
	private Collider collider;
	private int group;
	private int mask;
	
	public StaticPhysicsComponent(Collider collider, int group, int mask){
		this.collider = collider;
		this.group = group;
		this.mask = mask;
	}
	
	public StaticPhysicsComponent(Collider collider){
		this(collider, -1, -1);
	}
	
	public Collider getCollider(){
		return collider;
	}
	
	public void setCollider(Collider collider){
		this.collider = collider;
	}
	
	@Override
	public void addToEngine(){
		if(group != -1 && mask != -1){
			PhysicsEngine.addCollider(this.collider, group, mask);
		}else{
			PhysicsEngine.addCollider(this.collider);
		}
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeCollider(collider);
	}
}
