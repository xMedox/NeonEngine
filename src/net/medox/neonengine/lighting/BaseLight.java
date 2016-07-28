package net.medox.neonengine.lighting;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;

public class BaseLight extends EntityComponent{
	private Vector3f color;
	private float intensity;
	private Shader shader;
	private ShadowInfo shadowInfo;
	
	public BaseLight(Vector3f color, float intensity){
		this.color = color;
		this.intensity = intensity;
	}
	
	public void setShader(Shader shader){
		this.shader = shader;
	}
	
	public Shader getShader(){
		return shader;
	}
	
	public Vector3f getColor(){
		return color;
	}
	
	public void setColor(Vector3f color){
		this.color = color;
	}
	
	public float getIntensity(){
		return intensity;
	}
	
	public void setIntensity(float intensity){
		this.intensity = intensity;
	}
	
	public ShadowInfo getShadowInfo(){
		return shadowInfo;
	}
	
	public void setShadowInfo(ShadowInfo shadowInfo){
		this.shadowInfo = shadowInfo;
	}
	
	public ShadowCameraTransform calcShadowCameraTransform(Vector3f mainCameraPos, Quaternion mainCameraRot){
		return new ShadowCameraTransform(getTransform().getTransformedPos(), getTransform().getTransformedRot());
	}
	
	@Override
	public void addToEngine(){
		RenderingEngine.addLight(this);
	}
	
	@Override
	public void cleanUp(){
		RenderingEngine.removeLight(this);
	}
}
