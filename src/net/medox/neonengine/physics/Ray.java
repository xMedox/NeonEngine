package net.medox.neonengine.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;
import com.badlogic.gdx.physics.bullet.collision.btBroadphaseProxy.CollisionFilterGroups;

public class Ray{
	private final Collider hitCollider;
	private final net.medox.neonengine.math.Vector3f hitPoint;
	private final net.medox.neonengine.math.Vector3f hitNormal;
	private final boolean hasHit;
	
	public Ray(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld){
		this(rayFromWorld, rayToWorld, CollisionFilterGroups.DefaultFilter, CollisionFilterGroups.AllFilter);
	}
	
	public Ray(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld, int group, int mask){
		final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(rayFromWorld.getX(), rayFromWorld.getY(), rayFromWorld.getZ()), new Vector3(rayToWorld.getX(), rayToWorld.getY(), rayToWorld.getZ()));
		
		callback.setCollisionFilterGroup((short)group);
		callback.setCollisionFilterMask((short)mask);
		
		PhysicsEngine.rayTest(rayFromWorld, rayToWorld, callback);
		
		hasHit = callback.hasHit();
		
		final Vector3 hitPointWorld = new Vector3(0, 0, 0);
		callback.getHitPointWorld(hitPointWorld);
		
		hitPoint = new net.medox.neonengine.math.Vector3f(hitPointWorld.x, hitPointWorld.y, hitPointWorld.z);
		
		final Vector3 hitNormalWorld = new Vector3(0, 0, 0);
		callback.getHitNormalWorld(hitNormalWorld);
		
		hitNormal = new net.medox.neonengine.math.Vector3f(hitNormalWorld.x, hitNormalWorld.y, hitNormalWorld.z);
		
		if(hasHit){
			hitCollider = PhysicsEngine.getById(callback.getCollisionObject().getUserValue());
		}else{
			hitCollider = null;
		}
		
		callback.dispose();
	}
	
	public Collider getHitCollider(){
		return hitCollider;
	}
	
	public net.medox.neonengine.math.Vector3f getHitPoint(){
		return hitPoint;
	}
	
	public net.medox.neonengine.math.Vector3f getHitNormal(){
		return hitNormal;
	}
	
	public boolean hasHit(){
		return hasHit;
	}
}
