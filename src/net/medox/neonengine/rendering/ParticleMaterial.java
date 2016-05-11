package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

public class ParticleMaterial{
	public static final Texture DEFAULT_DIFFUSE_MAP_TEXTURE = ImageUtil.createDefaultDiffuseMap();
	
	public static final float DEFAULT_SPECULAR_INTENSITY = 0;
	public static final float DEFAULT_SPECULAR_POWER = 0;
	public static final float DEFAULT_EMISSIVE = 0;
	
	private final Map<String, Texture> textureMap;
	private final Map<String, Float> floatMap;
	
	public ParticleMaterial(){
		textureMap = new HashMap<String, Texture>();
		floatMap = new HashMap<String, Float>();
		
		setTexture("diffuseMap", DEFAULT_DIFFUSE_MAP_TEXTURE);
		
		setFloat("specularIntensity", DEFAULT_SPECULAR_INTENSITY);
		setFloat("specularPower", DEFAULT_SPECULAR_POWER);
		setFloat("emissive", DEFAULT_EMISSIVE);
	}
	
	public void setDiffuseMap(Texture texture){
		setTexture("diffuseMap", texture);
	}
	
	public void setSpecularIntensity(float value){
		setFloat("specularIntensity", value);
	}
	
	public void setSpecularPower(float value){
		setFloat("specularPower", value);
	}
	
	public void setEmissive(float value){
		setFloat("emissive", value);
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
	
	public float getEmissive(){
		return getFloat("emissive");
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
