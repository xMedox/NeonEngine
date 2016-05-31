package net.medox.neonengine.physics;

import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionWorld.ClosestRayResultCallback;

public class Ray{
	private final Collider collider;
	private final net.medox.neonengine.math.Vector3f hitPoint;
	private final net.medox.neonengine.math.Vector3f hitNormal;
	private final boolean hasHit;
	
	public Ray(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld){
		final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3f(rayFromWorld.getX(), rayFromWorld.getY(), rayFromWorld.getZ()), new Vector3f(rayToWorld.getX(), rayToWorld.getY(), rayToWorld.getZ()));
		
		PhysicsEngine.rayTest(rayFromWorld, rayToWorld, callback);
		
		hasHit = callback.hasHit();
		
		hitPoint = new net.medox.neonengine.math.Vector3f(callback.hitPointWorld.x, callback.hitPointWorld.y, callback.hitPointWorld.z);
		
		if(callback.hasHit()){
			collider = ((Collider)callback.collisionObject.getUserPointer());
		}else{
			collider = new Collider();
		}
		
		hitNormal = new net.medox.neonengine.math.Vector3f(callback.hitNormalWorld.x, callback.hitNormalWorld.y, callback.hitNormalWorld.z);
	}
	
	public Collider getHitCollider(){
		return collider;
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
