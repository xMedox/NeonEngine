package net.medox.neonengine.rendering.resourceManagement;

import java.util.HashMap;
import java.util.Map;

//import net.medox.neonengine.rendering.CubeMap;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.CubeMap;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Texture;

public abstract class MappedValues{
	private final Map<String, Texture> textureMap;
	private final Map<String, CubeMap> cubeMapMap;
	private final Map<String, Vector3f> vector3fMap;
	private final Map<String, Float> floatMap;
	
	public MappedValues(){
		textureMap = new HashMap<String, Texture>();
		cubeMapMap = new HashMap<String, CubeMap>();
		vector3fMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
	}
	
	public void setTexture(String name, Texture texture){
		textureMap.remove(name);
		textureMap.put(name, texture);
	}
	
	public void setCubeMap(String name, CubeMap cubeMap){
		cubeMapMap.remove(name);
		cubeMapMap.put(name, cubeMap);
	}
	
	public void setVector3f(String name, Vector3f vector3f){
		vector3fMap.remove(name);
		vector3fMap.put(name, vector3f);
	}
	
	public void setFloat(String name, float floatValue){
		floatMap.remove(name);
		floatMap.put(name, floatValue);
	}
	
	public Texture getTexture(String name){
		final Texture result = textureMap.get(name);
		
		return result == null ? Material.DEFAULT_DIFFUSE_TEXTURE : result;
	}
	
	public CubeMap getCubeMap(String name){
		final CubeMap result = cubeMapMap.get(name);
		
		return result == null ? null : result;
	}
	
	public Vector3f getVector3f(String name){
		final Vector3f result = vector3fMap.get(name);
		
		return result == null ? new Vector3f(0, 0, 0) : result;
	}
	
	public float getFloat(String name){
		final Float result = floatMap.get(name);
		
		return result == null ? 0 : result;
	}
	
//	public void cleanUp(){
//		for(int i = 0; i < textureHashMap.size(); i++){
//			textureHashMap.get(i).cleanUp();
//		}
//		for(int i = 0; i < cubeMapHashMap.size(); i++){
//			cubeMapHashMap.get(i).cleanUp();
//		}
//	}
}
