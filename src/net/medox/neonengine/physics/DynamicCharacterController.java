package net.medox.neonengine.physics;

public class DynamicCharacterController{
	private final Collider collider;
	
//	private float jumpSpeed;
	private float jumpHeight;
	
	public DynamicCharacterController(Collider collider, float stepHeight){
		this.collider = collider;
	}
	
//	public void setFlags(int flags){
//		ghostObject.setCollisionFlags(flags);
//	}
	
	public void setWalkDirection(net.medox.neonengine.math.Vector3f dir){
		collider.applyCentralImpulse(dir.mul(100));
	}
	
	public Collider getCollider(){
		return collider;
	}
	
//	public float getJumpSpeed(){
//		return jumpSpeed;
//	}
//	
//	public float getFallSpeed(){
//		return characterController.getFallSpeed();
//	}
	
	public float getMaxJumpHeight(){
		return jumpHeight;
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void cleanUp(){
		collider.cleanUp();
	}
}
