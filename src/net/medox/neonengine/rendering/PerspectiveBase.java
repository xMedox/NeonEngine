package net.medox.neonengine.rendering;

import net.medox.neonengine.math.Matrix4f;

public class PerspectiveBase extends CameraBase{
	private float fov;
	private float aspectRatio;
	private float zNear;
	private float zFar;
	
	public PerspectiveBase(float fov, float aspectRatio, float zNear, float zFar){
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.zNear = zNear;
		this.zFar = zFar;
		
		setMode(CameraBase.PERSPECTIVE_MODE);
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	private int updateSize(int value){
		return value <= 0 ? 1 : value;
	}
	
	@Override
	public void update(){
		aspectRatio = (float)updateSize(Window.getWidth())/(float)updateSize(Window.getHeight());
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	@Override
	public float getFov(){
		return fov;
	}
	
	@Override
	public float getAspectRatio(){
		return aspectRatio;
	}
	
	@Override
	public float getZNear(){
		return zNear;
	}
	
	@Override
	public float getZFar(){
		return zFar;
	}
	
	@Override
	public void setFov(float fov){
		this.fov = fov;
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	@Override
	public void setAspectRatio(float aspectRatio){
		this.aspectRatio = aspectRatio;
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	@Override
	public void setZNear(float zNear){
		this.zNear = zNear;
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	@Override
	public void setZFar(float zFar){
		this.zFar = zFar;
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
}
