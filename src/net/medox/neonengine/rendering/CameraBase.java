package net.medox.neonengine.rendering;

import net.medox.neonengine.math.Matrix4f;

public class CameraBase{
	private int mode;
	private Matrix4f projection;
	
	public CameraBase(){
		mode = -1;
		projection = new Matrix4f().initIdentity();
	}
	
	public void update(){}
	
	public int getMode(){
		return mode;
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public Matrix4f getProjection(){
		return projection;
	}
	
	public void setProjection(Matrix4f projection){
		this.projection = projection;
	}
	
	
	public float getFov(){
		return -1;
	}
	
	public float getAspectRatio(){
		return -1;
	}
	
	public float getZNear(){
		return -1;
	}
	
	public float getZFar(){
		return -1;
	}
	
	public void setFov(float fov){}
	
	public void setAspectRatio(float aspectRatio){}
	
	public void setZNear(float zNear){}
	
	public void setZFar(float zFar){}
	
	
	public float getLeft(){
		return -1;
	}
	
	public float getRight(){
		return -1;
	}
	
	public float getBottom(){
		return -1;
	}
	
	public float getTop(){
		return -1;
	}
	
	public float getNear(){
		return -1;
	}
	
	public float getFar(){
		return -1;
	}
	
	public void setLeft(float left){}
	
	public void setRight(float right){}
	
	public void setBottom(float bottom){}
	
	public void setTop(float top){}
	
	public void setNear(float near){}
	
	public void setFar(float far){}
}
