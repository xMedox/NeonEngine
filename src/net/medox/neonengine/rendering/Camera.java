package net.medox.neonengine.rendering;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;

public class Camera extends EntityComponent{
	private Frustum frustum;
	
	private Base base;
	
//	public Camera(Matrix4f projection){
//		this.projection = projection;
//		
////		RenderingEngine.setMainCamera(this);
//	}
	
	public Camera(){
		base = new Base();
	}
	
	public Camera(float fov, float aspectRatio, float zNear, float zFar){
		base = new Perspective(fov, aspectRatio, zNear, zFar);
	}
	
	public Camera(float fov, float zNear, float zFar){
		base = new Perspective(fov, (float)Window.getWidth()/(float)Window.getHeight(), zNear, zFar);
	}
	
	public Camera(float left, float right, float bottom, float top, float near, float far){
		base = new Orthographic(left, right, bottom, top, near, far);
	}
	
//	public Camera(float fov, float aspect, float zNear, float zFar){
//		this.fov = fov;
//		this.zNear = zNear;
//		this.zFar = zFar;
//		this.aspect = aspect;
//		
//		this.projection = new Matrix4f().initPerspective(fov, aspect, zNear, zFar);
//		
////		RenderingEngine.setMainCamera(this);
//	}
//	
//	public Camera(float fov, float zNear, float zFar){
//		this.fov = fov;
//		this.zNear = zNear;
//		this.zFar = zFar;
//		this.aspect = 1;
//		
//		this.projection = new Matrix4f().initPerspective(fov, 1, zNear, zFar);
//		
////		RenderingEngine.setMainCamera(this);
//	}
	
//	public void setProjection(Matrix4f projection){
//		this.projection = projection;
//	}
	
	public void update(){
		base.update();
	}
	
	public Matrix4f getViewProjection(){
//		Matrix4f cameraRotation = getTransform().getTransformedRot().conjugate().toRotationMatrix();
		final Vector3f cameraPos = getTransform().getTransformedPos().mul(-1);
		
//		Matrix4f cameraTranslation = new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ());
		
//		return base.getProjection().mul(cameraRotation.mul(cameraTranslation));
		return base.getProjection().mul(getTransform().getTransformedRot().conjugate().toRotationMatrix().mul(new Matrix4f().initTranslation(cameraPos.getX(), cameraPos.getY(), cameraPos.getZ())));
	}
	
//	public float getFov(){
//		return fov;
//	}
//	
//	public float getZNear(){
//		return zNear;
//	}
//	
//	public float getZFar(){
//		return zFar;
//	}
//	
//	public float getAspect(){
//		return aspect;
//	}
	
//	public void setFov(float fov){
//		this.fov = fov;
//		this.projection = new Matrix4f().initPerspective(fov, 0, zNear, zFar);
//		changeAspect();
//	}
	
	@Override
	public void addToEngine(){
		RenderingEngine.setMainCamera(this);
	}
	
//	@Override
//	public void cleanUp(){
//		RenderingEngine.removeLight(this);
//	}
	
	public void updateFrustum(){
//		if(getFov() != -1){
			frustum = new Frustum(this);
//		}
	}
//	
	public Frustum getFrustum(){
		return frustum;
	}
	
	public void changeMode(float fov, float aspectRatio, float zNear, float zFar){
		base = new Perspective(fov, aspectRatio, zNear, zFar);
		frustum = null;
	}
	
	public void changeMode(float fov, float zNear, float zFar){
		base = new Perspective(fov, (float)Window.getWidth()/(float)Window.getHeight(), zNear, zFar);
		frustum = null;
	}
	
	public void changeMode(float left, float right, float bottom, float top, float near, float far){
		base = new Orthographic(left, right, bottom, top, near, far);
		frustum = null;
	}
	
	public void changeMode(Base base){
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

class Base{
	private Matrix4f projection;
	
	public Base(){
		projection = new Matrix4f().initIdentity();
	}
	
	public void update(){
		
	}
	
	public void setProjection(Matrix4f projection){
		this.projection = projection;
	}
	
	public Matrix4f getProjection(){
		return projection;
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
	
	public void setFov(float fov){
//		return -1;
	}
	
	public void setAspectRatio(float aspectRatio){
//		return -1;
	}
	
	public void setZNear(float zNear){
//		return -1;
	}
	
	public void setZFar(float zFar){
//		return -1;
	}
	
	
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
	
	public void setLeft(float left){
//		return -1;
	}
	
	public void setRight(float right){
//		return -1;
	}
	
	public void setBottom(float bottom){
//		return -1;
	}
	
	public void setTop(float top){
//		return -1;
	}
	
	public void setNear(float near){
//		return -1;
	}
	
	public void setFar(float far){
//		return -1;
	}
}

class Perspective extends Base{
	private float fov;
	private float aspectRatio;
	private float zNear;
	private float zFar;
	
	public Perspective(float fov, float aspectRatio, float zNear, float zFar){
		this.fov = fov;
		this.aspectRatio = aspectRatio;
		this.zNear = zNear;
		this.zFar = zFar;
		
		setProjection(new Matrix4f().initPerspective(fov, aspectRatio, zNear, zFar));
	}
	
	@Override
	public void update(){
		aspectRatio = (float)Window.getWidth()/(float)Window.getHeight();
		
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

class Orthographic extends Base{
	private float left;
	private float right;
	private float bottom;
	private float top;
	private float near;
	private float far;
	
	public Orthographic(float left, float right, float bottom, float top, float near, float far){
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
		this.near = near;
		this.far = far;
		
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
