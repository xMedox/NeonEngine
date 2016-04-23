package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

public class ParticleMaterial{
	public static final Texture DEFAULT_DIFFUSE_TEXTURE = ImageUtil.createDefaultDiffuseMap();
	
	public static final float DEFAULT_SPECULAR_INTENSITY = 0;
	public static final float DEFAULT_SPECULAR_POWER = 0;
	public static final float DEFAULT_GLOW = 0;
	
	private final Map<String, Texture> textureMap;
	private final Map<String, Float> floatMap;
	
	public ParticleMaterial(){
		textureMap = new HashMap<String, Texture>();
		floatMap = new HashMap<String, Float>();
		
		setTexture("diffuse", DEFAULT_DIFFUSE_TEXTURE);
		
		setFloat("specularIntensity", DEFAULT_SPECULAR_INTENSITY);
		setFloat("specularPower", DEFAULT_SPECULAR_POWER);
		setFloat("glow", DEFAULT_GLOW);
	}
	
	public void setDiffuseMap(Texture texture){
		setTexture("diffuse", texture);
	}
	
	public void setSpecularIntensity(float value){
		setFloat("specularIntensity", value);
	}
	
	public void setSpecularPower(float value){
		setFloat("specularPower", value);
	}
	
	public void setGlow(float value){
		setFloat("glow", value);
	}
	
	public Texture getDiffuseMap(){
		return getTexture("diffuse");
	}
	
	public float getSpecularIntensity(){
		return getFloat("specularIntensity");
	}
	
	public float getSpecularPower(){
		return getFloat("specularPower");
	}
	
	public float getGlow(){
		return getFloat("glow");
	}
	
	public void setTexture(String name, Texture texture){
		textureMap.remove(name);
		textureMap.put(name, texture);
	}
	
	public void setFloat(String name, float floatValue){
		floatMap.remove(name);
		floatMap.put(name, floatValue);
	}
	
	public Texture getTexture(String name){
		return textureMap.get(name);
	}
	
	public float getFloat(String name){
		return floatMap.get(name);
	}
}
