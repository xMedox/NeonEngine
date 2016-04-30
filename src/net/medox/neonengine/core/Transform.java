package net.medox.neonengine.core;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class Transform{
	private Transform parent;
	private Matrix4f parentMatrix;
	
	private Vector3f pos;
	private Quaternion rot;
	private Vector3f scale;
	
	private Vector3f oldPos;
	private Quaternion oldRot;
	private Vector3f oldScale;
	
	public Transform(){
		pos = new Vector3f(0, 0, 0);
		rot = new Quaternion(0, 0, 0, 1);
		scale = new Vector3f(1, 1, 1);
		
		oldPos = pos;
		oldRot = rot;
		oldScale = scale;
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	public void update(){
//		if(oldPos != null){
//			oldPos.set(pos);
//			oldRot.set(rot);
//			oldScale.set(scale);
//		}else{
			oldPos = new Vector3f(0, 0, 0).set(pos).add(1.0f);
			oldRot = new Quaternion(0, 0, 0, 0).set(rot).mul(0.5f);
			oldScale = new Vector3f(0, 0, 0).set(scale).add(1.0f);
//		}
	}
	
	public void move(Vector3f add){
		pos = pos.add(add);
	}
	
	public void scale(Vector3f add){
		scale = scale.add(add);
	}
	
	public void rotate(Vector3f axis, float angle){
		rot = new Quaternion(axis, angle).mul(rot).normalized();
	}
	
	public void rotate(Quaternion quaternion){
		rot = quaternion.mul(rot).normalized();
	}
	
	public void lookAt(Vector3f point, Vector3f up){
		rot = getLookAtRotation(point, up);
	}
	
	public Quaternion getLookAtRotation(Vector3f point, Vector3f up){
		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
	}
	
	public boolean hasChanged(){
		return parent != null && parent.hasChanged() ? true : !pos.equals(oldPos) ? true: !rot.equals(oldRot) ? true: !scale.equals(oldScale) ? true: false;
	}
	
	public Matrix4f getTransformation(){
		return getParentMatrix().mul(/*translationMatrix*/new Matrix4f().initTranslation(pos.getX(), pos.getY(), pos.getZ()).mul(rot.toRotationMatrix().mul(/*scaleMatrix*/new Matrix4f().initScale(scale.getX(), scale.getY(), scale.getZ()))));
	}
	
//	public Matrix4f getTransformationList(){
//		Matrix4f result = new Matrix4f();
//		
//		result.set(0, 0, rot.getLeft().getX());
//		result.set(1, 0, rot.getLeft().getY());
//		result.set(2, 0, rot.getLeft().getY());
//		
//		result.set(0, 1, rot.getUp().getX());
//		result.set(1, 1, rot.getUp().getY());
//		result.set(2, 1, rot.getUp().getY());
//		
//		result.set(0, 2, rot.getForward().getX());
//		result.set(1, 2, rot.getForward().getY());
//		result.set(2, 2, rot.getForward().getY());
//		
//		result.set(0, 3, pos.getX());
//		result.set(1, 3, pos.getY());
//		result.set(2, 3, pos.getY());
//		
//		return result;
//	}
	
	private Matrix4f getParentMatrix(){
		if(parent != null && parent.hasChanged()){
			parentMatrix = parent.getTransformation();
		}
		
		return parentMatrix;
	}
	
	public void setParent(Transform parent){
		this.parent = parent;
	}
	
	public Vector3f getTransformedPos(){
		return getParentMatrix().transform(pos);
	}
	
	public Quaternion getTransformedRot(){
		Quaternion parentRotation = new Quaternion(0, 0, 0, 1);
		
		if(parent != null){
			parentRotation = parent.getTransformedRot();
		}
		
		return parentRotation.mul(rot);
	}
	
	public Vector3f getPos(){
		return pos;
	}
	
	public void setPos(Vector3f translation){
		pos = translation;
	}
	
	public void setPos(float x, float y, float z){
		pos.set(x, y, z);
	}
	
	public Quaternion getRot(){
		return rot;
	}
	
	public void setRot(Quaternion rotation){
		rot = rotation;
	}
	
	public void setRot(float x, float y, float z, float w){
		rot.set(x, y, z, w);
	}
	
	public Vector3f getScale(){
		return scale;
	}
	
	public void setScale(Vector3f scale){
		this.scale = scale;
	}
	
	public void setScale(float scale){
		this.scale.set(scale, scale, scale);
	}
	
	public void setScale(float x, float y, float z){
		scale.set(x, y, z);
	}
}
