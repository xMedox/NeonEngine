package net.medox.block3rd;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;

public class WolfComponent extends EntityComponent{
	private float health;
	private boolean dead;
	
	private BoxCollider box;
	private Vector3f add;
	private Vector3f sub;
//	private float timer;
	private Vector3f dir;
	
	private Sound damageSound;
	private MeshRendererWolf wolfR;
	private CharacterController controller;
	
	public WolfComponent(MeshRendererWolf wolfR){
		health = 8;
		
		damageSound = new Sound("bullet.wav");
		
//		cylinder = new Cylinder(1, 2);
//		capsule = new Cylinder(new Vector3f(0.5f, 2, 0.5f));
		box = new BoxCollider(new Vector3f(0.275f, 0.575f, 0.775f));
		
		add = new Vector3f(0, -0.575f, 0);
		sub = new Vector3f(0, 1, 0);
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		box.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		box.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		box.setSleepingThresholds(0, 0);
		
		Transform t = new Transform();
		t.setPos(new Vector3f(7, 7, 7));
		box.setTransform(t);
//		controlBall.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
//		capsule.s
		
//		PhysicsEngine.addObject(cylinder);
		
		box.setGroup(1);
		box.setObject(this);
		
		controller = new CharacterController(box, 0.05f);
		
		controller.setJumpSpeed(4.6f);
//		controller.setFallSpeed(100f);
		controller.setMaxJumpHeight(1f);
//		controller.setJumpSpeed(100);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
//		controller.setFallSpeed(9.80665f);
//		controller.setGravity(9.80665f);
		
//		controller.setFallSpeed(9.81f);
//		controller.setGravity(PhysicsEngine.getGravity());
		
//		System.out.println(capsule.getGravity());
//		System.out.println(controller.getGravity());
		
		this.wolfR = wolfR;
	}
	
	public boolean isDead(){
		return dead;
	}
	
	public void damage(float amount){
		if(!dead){
			health -= amount;
			damageSound.play();
			
			if(health <= 0){
				dead = true;
			}
		}
	}
	
	public BoxCollider getBox(){
		return box;
	}
	
	public MeshRendererWolf getMeshRenderer(){
		return wolfR;
	}
	
	@Override
	public void update(float delta){
		if(!dead){
			float speed = 2;
			
	//		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
	//			speed = 6;
	//		}
			
	//		if(timer > 0){
	//			timer -= delta;
				
				dir = new Vector3f(0, 0, 0);
				
				dir = dir.add(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
				
	//			Ray ray = new Ray(getTransform().getTransformedPos().sub(add), getTransform().getTransformedPos().sub(add).add(dir));
	//			
	//			if(ray.hasHit()){
	//				Ray ray2 = new Ray(getTransform().getTransformedPos().sub(add).add(sub), getTransform().getTransformedPos().sub(add).add(sub).add(dir));
	//				
	//				if(!ray2.hasHit()){
	//					controller.jump();
	//				}
	//			}
				
				move(dir.mul(speed));
	//		}else{
	//			timer = 1*Time.getSecond();
				
	//			int i = Util.randomInt(0, 4);
	//			
	//			dir = new Vector3f(0, 0, 0);
	//			
	//			if(i == 0){
	//				dir = dir.add(getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
	//			}else if(i == 1){
	//				dir = dir.add(getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
	//			}else if(i == 2){
	//				dir = dir.add(getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
	//			}else if(i == 3){
	//				dir = dir.add(getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
	//			}
	//		}
			
	//		System.out.println(controller.collidesWith(collider));
	//		if(controller.collidesWith(collider)){
	//			System.out.println("COLLIDE");
	//		}
			controller.setRot(new Quaternion(sub, 1*delta).mul(/*controller.getRot()*/getTransform().getTransformedRot()).normalized());
			
	//		controller.setPos(new Vector3f(7, 7, 7));
	//		getTransform().rotate(new Vector3f(0, 1, 0), 1*delta);
	//		getTransform().setRot(t.getRot());
			
	//		box.setTransform(controller.getTransform());
		}else{
			move(new Vector3f(0, 0, 0));
		}
		
		getTransform().setPos(controller.getPos().add(add));
		getTransform().setRot(controller.getRot());
		
		damageSound.setPosition(getTransform().getTransformedPos());
	}
	
	public void move(Vector3f vel){
//		System.out.println(controller.onGround());
		
//		cylinder.setLinearVelocity(vel);
//		if(controller.onGround()){
			controller.setWalkDirection(vel.mul(0.015f));
//		}else{
//			controller.setWalkDirection(vel.mul(0.0125f));
//		}
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addController(controller);
	}
	
	@Override
	public void cleanUp(){
//		PhysicsEngine.removeObject(cylinder);
		PhysicsEngine.removeController(controller);
	}
}
