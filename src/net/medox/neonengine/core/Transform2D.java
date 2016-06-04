package net.medox.neonengine.core;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;

public class Transform2D{
	private Transform2D parent;
	private Matrix4f parentMatrix;
	
	private Vector2f position;
	private Vector2f scale;
	
	private Vector2f oldPosition;
	private Vector2f oldScale;
	
	public Transform2D(){
		position = new Vector2f(0, 0);
		scale = new Vector2f(1, 1);
		
		oldPosition = position;
		oldScale = scale;
		
		parentMatrix = new Matrix4f().initIdentity();
	}
	
	public void update(){
//		if(oldPosition != null){
			oldPosition.set(position);
			oldScale.set(scale);
//		}else{
//			oldPosition = position.add(1.0f);
//			oldScale = scale.add(1.0f);
//		}
	}
	
	public void move(Vector2f add){
		position = position.add(add);
	}
	
	public void scale(Vector2f add){
		scale = scale.add(add);
	}
	
//	public void rotate(Vector3f axis, float angle){
//		rot = new Quaternion(axis, angle).mul(rot).normalized();
//	}
//	
//	public void rotate(Quaternion quaternion){
//		rot = quaternion.mul(rot).normalized();
//	}
//	
//	public void lookAt(Vector3f point, Vector3f up){
//		rot = getLookAtRotation(point, up);
//	}
//	
//	public Quaternion getLookAtRotation(Vector3f point, Vector3f up){
//		return new Quaternion(new Matrix4f().initRotation(point.sub(pos).normalized(), up));
//	}
	
	public boolean hasChanged(){
		return parent != null && parent.hasChanged() ? true : !position.equals(oldPosition) ? true: !scale.equals(oldScale) ? true: false;
	}
	
	public Matrix4f getTransformation(){
		return getParentMatrix().mul(/*translationMatrix*/new Matrix4f().initTranslation(position.getX(), position.getY(), 0).mul(/*rotationMatrix*/new Quaternion(0, 0, 0, 1).toRotationMatrix().mul(/*scaleMatrix*/new Matrix4f().initScale(scale.getX(), scale.getY(), 0))));
	}
	
	private Matrix4f getParentMatrix(){
		if(parent != null && parent.hasChanged()){
			parentMatrix = parent.getTransformation();
		}
		
		return parentMatrix;
	}
	
	public void setParent(Transform2D parent){
		this.parent = parent;
	}
	
	public Vector2f getTransformedPos(){
		Vector2f parentPos = new Vector2f(0, 0);
		
		if(parent != null){
			parentPos = parent.getTransformedPos();
		}
		
		return parentPos.add(position);
		
//		return new Vector2f(getParentMatrix().transform(new Vector3f(pos.getX(), pos.getY(), 0)).getX(), getParentMatrix().transform(new Vector3f(pos.getX(), pos.getY(), 0)).getY());
	}
	
	public Vector2f getPos(){
		return position;
	}
	
	public void setPos(Vector2f translation){
		position = translation;
	}
	
	public void setPos(float x, float y){
		position.set(x, y);
	}
	
//	public Quaternion getRot(){
//		return rot;
//	}
//	
//	public void setRot(float rotation){
//		this.rot = new Quaternion(new Vector3f(0, 0, 1), rotation).normalized();
//	}
//	
//	public void setRot(float x, float y, float z, float w){
//		rot.set(x, y, z, w);
//	}
	
	public Vector2f getScale(){
		return scale;
	}
	
	public void setScale(Vector2f scale){
		this.scale = scale;
	}
	
	public void setScale(float scale){
		this.scale.set(scale, scale);
	}
	
	public void setScale(float x, float y){
		scale.set(x, y);
	}
}
