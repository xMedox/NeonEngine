package net.medox.neonengine.rendering;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;

public class Camera extends EntityComponent{
	private CameraBase base;
	private Frustum frustum;
	
	public Camera(){
		base = new CameraBase();
	}
	
	public Camera(float fov, float aspectRatio, float zNear, float zFar){
		base = new PerspectiveBase(fov, aspectRatio, zNear, zFar);
	}
	
	public Camera(float fov, float zNear, float zFar){
		base = new PerspectiveBase(fov, (float)Window.getWidth()/(float)Window.getHeight(), zNear, zFar);
	}
	
	public Camera(float left, float right, float bottom, float top, float near, float far){
		base = new OrthographicBase(left, right, bottom, top, near, far);
	}
	
	public void update(){
		base.update();
	}
	
	public int getMode(){
		return base.getMode();
	}
	
	public Matrix4f getViewProjection(){
		final Vector3f cameraPos = getTransform().getTransformedPos().mul(-1);
		
		return base.getProjection().mul(getTransform().getTransformedRot().conjugate().toRotationMatrix().mul(new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ())));
	}
	
	@Override
	public void addToEngine(){
		RenderingEngine.setMainCamera(this);
	}
	
	public void updateFrustum(){
		frustum = new Frustum(this);
	}
	
	public Frustum getFrustum(){
		return frustum;
	}
	
	public void changeMode(float fov, float aspectRatio, float zNear, float zFar){
		base = new PerspectiveBase(fov, aspectRatio, zNear, zFar);
		frustum = null;
	}
	
	public void changeMode(float fov, float zNear, float zFar){
		base = new PerspectiveBase(fov, (float)Window.getWidth()/(float)Window.getHeight(), zNear, zFar);
		frustum = null;
	}
	
	public void changeMode(float left, float right, float bottom, float top, float near, float far){
		base = new OrthographicBase(left, right, bottom, top, near, far);
		frustum = null;
	}
	
	public void changeMode(CameraBase base){
		this.base = base;
		frustum = null;
	}
	
	
	public float getFov(){
		return base.getFov();
	}
	
	public float getAspectRatio(){
		return base.getAspectRatio();
	}
	
	public float getZNear(){
		return base.getZNear();
	}
	
	public float getZFar(){
		return base.getZFar();
	}
	
	public void setFov(float fov){
		base.setFov(fov);
	}
	
	public void setAspectRatio(float aspectRatio){
		base.setAspectRatio(aspectRatio);
	}
	
	public void setZNear(float zNear){
		base.setZNear(zNear);
	}
	
	public void setZFar(float zFar){
		base.setZFar(zFar);
	}
	
	
	public float getLeft(){
		return base.getLeft();
	}
	
	public float getRight(){
		return base.getRight();
	}
	
	public float getBottom(){
		return base.getBottom();
	}
	
	public float getTop(){
		return base.getTop();
	}
	
	public float getNear(){
		return base.getNear();
	}
	
	public float getFar(){
		return base.getFar();
	}
	
	public void setLeft(float left){
		base.setLeft(left);
	}
	
	public void setRight(float right){
		base.setRight(right);
	}
	
	public void setBottom(float bottom){
		base.setBottom(bottom);
	}
	
	public void setTop(float top){
		base.setTop(top);
	}
	
	public void setNear(float near){
		base.setNear(near);
	}
	
	public void setFar(float far){
		base.setFar(far);
	}
}
