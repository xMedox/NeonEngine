package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject.CollisionFlags;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;

public class Collider{
	public static final Matrix4 DEFAULT_TRANSFORM = new Matrix4().set(new Vector3(0, 0, 0), new Quaternion(0, 0, 0, 1), new Vector3(1, 1, 1));
	
	private final List<Collider> hitList;
	
	private btRigidBody body;
	
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
	
	public btRigidBody getBody(){
		return body;
	}
	
	public void add(Collider c){
		hitList.add(c);
	}
	
	public void clearList(){
		hitList.clear();
	}
	
	public boolean collidesWith(Collider c){
		return hitList.contains(c);
	}
	
	public void setBody(btRigidBody body){
		this.body = body;
		body.setCollisionFlags(body.getCollisionFlags() | CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
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
		body.applyCentralForce(new Vector3(force.getX(), force.getY(), force.getZ()));
	}
	
	public void applyCentralImpulse(net.medox.neonengine.math.Vector3f impulse){
		body.applyCentralImpulse(new Vector3(impulse.getX(), impulse.getY(), impulse.getZ()));
	}
	
//	public void applyGravity(){
//		body.applyGravity();
//	}
	
	public void applyTorqueImpulse(net.medox.neonengine.math.Vector3f torque){
		body.applyTorqueImpulse(new Vector3(torque.getX(), torque.getY(), torque.getZ()));
	}
	
	public void clearForces(){
		body.clearForces();
	}
	
	public net.medox.neonengine.math.Vector3f getAngularVelocity(){
		final Vector3 angularVelocity = body.getAngularVelocity();
		
		return new net.medox.neonengine.math.Vector3f(angularVelocity.x, angularVelocity.y, angularVelocity.z); 
	}
	
	public net.medox.neonengine.math.Vector3f getPos(){
		final Vector3 pos = body.getCenterOfMassPosition();
		
		return new net.medox.neonengine.math.Vector3f(pos.x, pos.y, pos.z);
	}
	
	public net.medox.neonengine.math.Quaternion getRot(){
		final Quaternion rot = body.getOrientation();
		
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
		
		final Vector3 pos = body.getCenterOfMassPosition();
		final Quaternion rot = body.getOrientation();
		
		result.setPos(pos.x, pos.y, pos.z);
		result.setRot(rot.x, rot.y, rot.z, rot.w);
		result.setScale(getScale());
		
		return result;
	}
	
	public net.medox.neonengine.math.Vector3f getLinearVelocity(){
		final Vector3 linearVelocity = body.getLinearVelocity();
		
		return new net.medox.neonengine.math.Vector3f(linearVelocity.x, linearVelocity.y, linearVelocity.z);
	}
	
	public void remove(){
		PhysicsEngine.removeObject(this);
	}
	
	public void setAngularFactor(float angFac){
		body.setAngularFactor(angFac);
	}
	
	public void setAngularVelocity(net.medox.neonengine.math.Vector3f angVel){
		body.setAngularVelocity(new Vector3(angVel.getX(), angVel.getY(), angVel.getZ()));
	}
	
	public void setFriction(float friction){
		body.setFriction(friction);
	}
	
	public void setGravity(float gravity){
		body.setGravity(new Vector3(0, gravity, 0));
	}
	
	public void setLinearVelocity(net.medox.neonengine.math.Vector3f linVel){
		body.setLinearVelocity(new Vector3(linVel.getX(), linVel.getY(), linVel.getZ()));
	}
	
	public void setMassProps(float mass, net.medox.neonengine.math.Vector3f inertia){
		body.setMassProps(mass, new Vector3(inertia.getX(), inertia.getY(), inertia.getZ()));
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
		body.setWorldTransform(new Matrix4().set(new Vector3(worldTransform.getPos().getX(), worldTransform.getPos().getY(), worldTransform.getPos().getZ()), new Quaternion(worldTransform.getRot().getX(), worldTransform.getRot().getY(), worldTransform.getRot().getZ(), worldTransform.getRot().getW())));
		
		setScale(worldTransform.getScale());
	}
	
	public float getGravity(){		
		return body.getGravity().y;
	}
	
	public btCollisionShape getCollisionShape(){
		return null;
	}
	
	public void setPos(net.medox.neonengine.math.Vector3f transform){
		final Matrix4 t = body.getWorldTransform();
		t.setTranslation(new Vector3(transform.getX(), transform.getY(), transform.getZ()));
		
		body.setWorldTransform(t);
	}
	
	public void setRot(net.medox.neonengine.math.Quaternion rotation){
		final Matrix4 t = body.getWorldTransform();
		t.set(t.getTranslation(new Vector3()), new Quaternion(rotation.getX(), rotation.getY(), rotation.getZ(), rotation.getW()), t.getScale(new Vector3()));
		
		body.setWorldTransform(t);
	}
}
