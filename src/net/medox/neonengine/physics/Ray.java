package net.medox.neonengine.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ClosestRayResultCallback;

public class Ray{
	private final Collider collider;
	private final net.medox.neonengine.math.Vector3f hitPoint;
	private final net.medox.neonengine.math.Vector3f hitNormal;
	private final boolean hasHit;
	
	public Ray(net.medox.neonengine.math.Vector3f rayFromWorld, net.medox.neonengine.math.Vector3f rayToWorld){
		final ClosestRayResultCallback callback = new ClosestRayResultCallback(new Vector3(rayFromWorld.getX(), rayFromWorld.getY(), rayFromWorld.getZ()), new Vector3(rayToWorld.getX(), rayToWorld.getY(), rayToWorld.getZ()));
		
		PhysicsEngine.rayTest(rayFromWorld, rayToWorld, callback);
		
		hasHit = callback.hasHit();
		
		Vector3 hitPointWorld = new Vector3(0, 0, 0);
		callback.getHitPointWorld(hitPointWorld);
		
		hitPoint = new net.medox.neonengine.math.Vector3f(hitPointWorld.x, hitPointWorld.y, hitPointWorld.z);
		
		Vector3 hitNormalWorld = new Vector3(0, 0, 0);
		callback.getHitNormalWorld(hitNormalWorld);
		
		hitNormal = new net.medox.neonengine.math.Vector3f(hitNormalWorld.x, hitNormalWorld.y, hitNormalWorld.z);
		
		if(callback.hasHit()){
//			collider = ((Collider)callback.getCollisionObject().getUserPointer());
			collider = PhysicsEngine.getById(callback.getCollisionObject().getUserValue());
		}else{
			collider = new Collider();
		}
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
