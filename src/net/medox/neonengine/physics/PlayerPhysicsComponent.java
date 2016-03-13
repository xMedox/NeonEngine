package net.medox.neonengine.physics;

//import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;

public class PlayerPhysicsComponent extends EntityComponent{
	private Camera camera;
	private Capsule capsule;
	
	private CharacterController controller;
	
	public PlayerPhysicsComponent(Camera camera){		
//		cylinder = new Cylinder(1, 2);
//		capsule = new Cylinder(new Vector3f(0.5f, 2, 0.5f));
		capsule = new Capsule(0.5f, 1f);
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		capsule.setMassProps(2.5f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		capsule.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		capsule.setSleepingThresholds(0, 0);
//		controlBall.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
//		capsule.s
		
//		PhysicsEngine.addObject(cylinder);
		
		controller = new CharacterController(capsule, 0.3f);
		
		controller.setJumpSpeed(4);
//		controller.setJumpSpeed(100);
//		controller.setMaxJumpHeight(1);
		
		controller.setMaxSlope((float)Math.toRadians(55));
		
//		controller.setFallSpeed(9.80665f);
//		controller.setGravity(9.80665f);
		
//		controller.setFallSpeed(9.81f);
//		controller.setGravity(PhysicsEngine.getGravity());
		
//		System.out.println(capsule.getGravity());
//		System.out.println(controller.getGravity());
		
		this.camera = camera;
	}
	
	public Capsule getCapsule(){
		return capsule;
	}
	
	@Override
	public void update(float delta){
//		System.out.println(controller.collidesWith(collider));
//		if(controller.collidesWith(collider)){
//			System.out.println("COLLIDE");
//		}
		
		getTransform().setPos(controller.getPos());
//		getTransform().setRot(t.getRot());
		
//		capsule.setTransform(t);
	}
	
	@Override
	public void input(float delta){
		float speed = 6;
		
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
		
		if(Input.getKey(Input.KEY_LEFT_SHIFT)){
			speed = 10;
		}
		
//		Vector3f y = capsule.getLinearVelocity();
		
		Vector3f dir = new Vector3f(0, 0, 0);
		
//		boolean antislide = true;
		
		if(Input.getKey(Input.KEY_W) && !Input.getKey(Input.KEY_S)){
			dir = dir.add(camera.getTransform().getRot().getForward().mul(new Vector3f(1, 0, 1)).normalized());
			
//			antislide = false;
		}
		if(Input.getKey(Input.KEY_A) && !Input.getKey(Input.KEY_D)){
			dir = dir.add(camera.getTransform().getRot().getLeft().mul(new Vector3f(1, 0, 1)).normalized());
			
//			antislide = false;
		}
		if(Input.getKey(Input.KEY_S) && !Input.getKey(Input.KEY_W)){
			dir = dir.add(camera.getTransform().getRot().getBack().mul(new Vector3f(1, 0, 1)).normalized());
			
//			antislide = false;
		}
		if(Input.getKey(Input.KEY_D) && !Input.getKey(Input.KEY_A)){
			dir = dir.add(camera.getTransform().getRot().getRight().mul(new Vector3f(1, 0, 1)).normalized());
			
//			antislide = false;
		}
		
		if(Input.getKeyDown(Input.KEY_SPACE)){
			controller.jump();
//			dir.setY(0.5f);
			
//			antislide = false;
		}
		
//		if(!antislide){
			move(dir.mul(speed));
//		}
		
//		if(antislide){
//			Vector3f anti = new Vector3f(0, 0, 0);
////			body.getLinearVelocity(antislide);
////			
////			antislide.x *= 3;
////			antislide.z *= 3;
////			
//			anti.setY(y.getY());
//			
//			move(anti);
//		}
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
