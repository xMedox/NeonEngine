package net.medox.neonengine.rendering;

import net.medox.neonengine.math.Matrix4f;

public class OrthographicBase extends CameraBase{
	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
	public OrthographicBase(float left, float right, float bottom, float top, float near, float far){
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		
		setMode(CameraBase.ORTHOGRAPHIC_MODE);
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	@Override
	public void update(){
		left = 0;
		right = Window.getWidth();
		bottom = 0;
		top = Window.getHeight();
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public float getLeft(){
		return left;
	}
	
	public float getRight(){
		return right;
	}
	
	public float getBottom(){
		return bottom;
	}
	
	public float getTop(){
		return top;
	}
	
	public float getNear(){
		return near;
	}
	
	public float getFar(){
		return far;
	}
	
	public void setLeft(float left){
		this.left = left;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public void setRight(float right){
		this.right = right;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public void setBottom(float bottom){
		this.bottom = bottom;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public void setTop(float top){
		this.top = top;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public void setNear(float near){
		this.near = near;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
	
	public void setFar(float far){
		this.far = far;
		
		setProjection(new Matrix4f().initOrthographic(left, right, bottom, top, near, far));
	}
}
