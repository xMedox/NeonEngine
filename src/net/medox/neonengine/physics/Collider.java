package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.dispatch.CollisionFlags;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.linearmath.Transform;

public class Collider{
	public static final Transform DEFAULT_TRANSFORM = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1.0f));
	
	private final List<Collider> hitList;
	
	private RigidBody body;
	
	private int group;
	private Object object;
	
	public Collider(){
		hitList = new ArrayList<Collider>();
	}
	
	public int getGroup(){
		return group;
	}
	
	public void setGroup(int group){
		this.group = group;
	}
	
	public Object getObject(){
		return object;
	}
	
	public void setObject(Object object){
		this.object = object;
	}
	
	public RigidBody getBody(){
		return body;
	}
	
	public void add(Collider c){
		hitList.add(c);
	}
	
	public void clearList(){
		hitList.clear();
	}
	
	public boolean collidesWith(Collider c){
//		for(int i = 0; i < hitList.size(); i++){
//			if(hitList.get(i).equals(c)){
//				return true;
//			}
//		}
		
//		return body.checkCollideWith(c.getBody());
		
		return hitList.contains(c);
//		return false;
	}
	
	public void setBody(RigidBody body){
		this.body = body;
//		body.setSleepingThresholds(0, 0);
		body.setUserPointer(this);
		body.setCollisionFlags(body.getCollisionFlags() | CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
//		setSleepingThresholds(0, 0);
	}
	
	public void activate(boolean forceActivation){
		body.activate(forceActivation);
	}
	
//	public void applyForce(net.medox.neonengine.core.Vector3f force, net.medox.neonengine.core.Vector3f origin){
//		body.applyForce(new Vector3f(force.getX(), force.getY(), force.getZ()), new Vector3f(origin.getX(), origin.getY(), origin.getZ()));
//	}
//	
//	public void applyImpulse(net.medox.neonengine.core.Vector3f force, net.medox.neonengine.core.Vector3f origin){
//		body.applyImpulse(new Vector3f(force.getX(), force.getY(), force.getZ()), new Vector3f(origin.getX(), origin.getY(), origin.getZ()));
//	}
	
//	public void addToEngine(){
//		PhysicsEngine.addObject(this);
//	}
//	
//	public void removeFromEngine(){
//		PhysicsEngine.removeObject(this);
//	}
	
//	public void setFlags(int flags){
//		body.setCollisionFlags(flags);
//	}
	
	public void applyCentralForce(net.medox.neonengine.math.Vector3f force){
		body.applyCentralForce(new Vector3f(force.getX(), force.getY(), force.getZ()));
	}
	
	public void applyCentralImpulse(net.medox.neonengine.math.Vector3f impulse){
		body.applyCentralImpulse(new Vector3f(impulse.getX(), impulse.getY(), impulse.getZ()));
	}
	
//	public void applyGravity(){
//		body.applyGravity();
//	}
	
	public void applyTorqueImpulse(net.medox.neonengine.math.Vector3f torque){
		body.applyTorqueImpulse(new Vector3f(torque.getX(), torque.getY(), torque.getZ()));
	}
	
	public void clearForces(){
		body.clearForces();
	}
	
	public net.medox.neonengine.math.Vector3f getAngularVelocity(){
		final Vector3f angularVelocity = body.getAngularVelocity(new Vector3f(0, 0, 0));
		
		return new net.medox.neonengine.math.Vector3f(angularVelocity.x, angularVelocity.y, angularVelocity.z); 
	}
	
	public net.medox.neonengine.math.Vector3f getPos(){
//		Vector3f pos = body.getWorldTransform(new Transform()).origin;
		final Vector3f pos = body.getCenterOfMassPosition(new Vector3f());
		
		return new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z);
	}
	
	public net.medox.neonengine.math.Quaternion getRot(){
//		Quat4f rot = body.getWorldTransform(new Transform()).getRotation(new Quat4f());
		final Quat4f rot = body.getOrientation(new Quat4f());
		
		return new net.medox.neonengine.math.Quaternion(rot.x, rot.y, rot.z, rot.w);
	}
	
	public net.medox.neonengine.math.Vector3f getScale(){
		return null;
	}
	
	public void setScale(net.medox.neonengine.math.Vector3f scale){
		
	}
	
	public void setScale(float scale){
		
	}
	
	public net.medox.neonengine.core.Transform getTransform(){
		final net.medox.neonengine.core.Transform result = new net.medox.neonengine.core.Transform();
		
//		Vector3f pos = body.getWorldTransform(new Transform()).origin;
		final Vector3f pos = body.getCenterOfMassPosition(new Vector3f());
//		Quat4f rot = body.getWorldTransform(new Transform()).getRotation(new Quat4f());
		final Quat4f rot = body.getOrientation(new Quat4f());
//		net.medox.neonengine.math.Vector3f scale = getScale();
		
		result.setPos(pos.x, pos.y, pos.z);
		result.setRot(rot.x, rot.y, rot.z, rot.w);
		result.setScale(getScale());
		
		return result;
	}
	
	public net.medox.neonengine.math.Vector3f getLinearVelocity(){
		final Vector3f linearVelocity = body.getLinearVelocity(new Vector3f(0, 0, 0));
		
		return new net.medox.neonengine.math.Vector3f(linearVelocity.x, linearVelocity.y, linearVelocity.z);
	}
	
	public void remove(){
		PhysicsEngine.removeObject(this);
	}
	
	public void setAngularFactor(float angFac){
		body.setAngularFactor(angFac);
	}
	
	public void setAngularVelocity(net.medox.neonengine.math.Vector3f angVel){
		body.setAngularVelocity(new Vector3f(angVel.getX(), angVel.getY(), angVel.getZ()));
	}
	
	public void setFriction(float friction){
		body.setFriction(friction);
	}
	
	public void setGravity(float gravity){
		body.setGravity(new Vector3f(0, gravity, 0));
	}
	
	public void setLinearVelocity(net.medox.neonengine.math.Vector3f linVel){
		body.setLinearVelocity(new Vector3f(linVel.getX(), linVel.getY(), linVel.getZ()));
	}
	
	public void setMassProps(float mass, net.medox.neonengine.math.Vector3f inertia){
		body.setMassProps(mass, new Vector3f(inertia.getX(), inertia.getY(), inertia.getZ()));
	}
	
	public void setMassProps(float mass){
		
	}
	
	public void setRestitution(float restitution){
		body.setRestitution(restitution);
	}
	
	public void setSleepingThresholds(float linear, float angular){
		body.setSleepingThresholds(linear, angular);
	}
	
	public void setTransform(net.medox.neonengine.core.Transform worldTransform){
		body.setWorldTransform(new Transform(new Transform(new Matrix4f(new Quat4f(worldTransform.getRot().getX(), worldTransform.getRot().getY(), worldTransform.getRot().getZ(), worldTransform.getRot().getW()), new Vector3f(worldTransform.getPos().getX(), worldTransform.getPos().getY(), worldTransform.getPos().getZ()), 1.0f))));
		
		setScale(worldTransform.getScale());
	}
	
	public float getGravity(){		
		return body.getGravity(new Vector3f(0, 0, 0)).y;
	}
	
	public CollisionShape getCollisionShape(){
		return null;
	}
	
	public void setPos(net.medox.neonengine.math.Vector3f transform){
		final Transform t = body.getWorldTransform(new Transform());
		t.origin.x = transform.getX();
		t.origin.y = transform.getY();
		t.origin.z = transform.getZ();
		
		body.setWorldTransform(t);
	}
	
	public void setRot(net.medox.neonengine.math.Quaternion rotation){
		final Transform t = body.getWorldTransform(new Transform());
		t.setRotation(new Quat4f(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()));
		
		body.setWorldTransform(t);
	}
}
