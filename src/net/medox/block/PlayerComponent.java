package net.medox.block;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.Ray;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Texture;

public class PlayerComponent extends EntityComponent{
	private Camera camera;
	private Box box;
	
	private float attackTimer;
	
	private CharacterController controller;
	
	private Texture t1;
	private Texture t2;
	private Texture t3;
	private Texture t4;
	
	private Texture g1;
	
	public PlayerComponent(Camera camera){
		box = new Box(new Vector3f(0.475f, 0.975f, 0.475f));
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		box.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		box.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		box.setSleepingThresholds(0, 0);
		
		Transform t = new Transform();
		t.setPos(new Vector3f(4, 4, 4));
		box.setTransform(t);
		
		controller = new CharacterController(box, 0.05f);
		
		controller.setJumpSpeed(4.6f);
//		controller.setFallSpeed(100f);
		controller.setMaxJumpHeight(1f);
//		controller.setJumpSpeed(100);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
		this.camera = camera;
		
		t1 = new Texture("block60.png", true);
		t2 = new Texture("block70.png", true);
		t3 = new Texture("block21.png", true);
		t4 = new Texture("block30.png", true);
		
		g1 = new Texture("block60_glow.png", true);
	}
	
	public Box getBox(){
		return box;
	}
	
	@Override
	public void update(float delta){
		getTransform().setPos(controller.getPos());
	}
	
	@Override
	public void input(float delta){
		float speed = 4;
		
//		if(Input.getKeyDown(Input.KEY_L)){
//			Ray ray = new Ray(camera.getTransform().getTransformedPos(), camera.getTransform().getTransformedPos().add(camera.getTransform().getTransformedRot().getForward().mul(5)));
//			
//			if(ray.getHitCollider().equals(collider)){
//				collider.activate(true);
//				collider.applyCentralForce(camera.getTransform().getTransformedRot().getForward().mul(20));
//			}
//		}
//		
//		if(Input.getKeyDown(Input.KEY_K)){
//			collider.setLinearVelocity(new Vector3f(0, 0, 0));
//			collider.setAngularVelocity(new Vector3f(0, 0, 0));
//		}
		
//		if(Input.getMouseDown(Input.BUTTON_MIDDLE)){
//			audio.play();
////			audio.setRolloffFactor(0.25f);
//		}
		
		if(attackTimer > 0){
			attackTimer -= delta;
		}else{
			if(Input.getMouseDown(Input.BUTTON_LEFT) && Input.isGrabbed()){
				attackTimer = 2*60*0.016666668f;
				
				Ray ray = new Ray(camera.getTransform().getTransformedPos(), camera.getTransform().getTransformedPos().add(camera.getTransform().getRot().getForward().mul(3)));
				
				if(ray.getHitCollider().getGroup() == 1){
					WolfComponent wolf = (WolfComponent)ray.getHitCollider().getObject();
					if(!wolf.isDead()){
						int r = Util.randomInt(0, 3);
						
						Texture t = null;
						Texture g = null;
						
						if(r == 0){
							t = t1;
							g = g1;
						}else if(r == 1){
							t = t2;
							g = Material.DEFAULT_GLOW_MAP_TEXTURE;
						}else if(r == 2){
							t = t3;
							g = Material.DEFAULT_GLOW_MAP_TEXTURE;
						}else if(r == 3){
							t = t4;
							g = Material.DEFAULT_GLOW_MAP_TEXTURE;
						}
						
						wolf.getMeshRenderer().setDiffuseMap(t);
						wolf.getMeshRenderer().setGlowMap(g);
						
						wolf.damage(1);
					}
				}
			}
		}
		
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			speed = 6;
		}
		
		Vector3f dir = new Vector3f(0, 0, 0);
		
		if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
		}
		
		if(Input.getKeyDown(Input.KEY_SPACE)){
			controller.jump();
		}
		
		move(dir.mul(speed));
	}
	
	public void move(Vector3f vel){
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
