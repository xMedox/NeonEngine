package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.math.Vector3f;

public class Material{
	public static final Texture DEFAULT_DIFFUSE_MAP_TEXTURE = ImageUtil.createDefaultDiffuseMap();
	public static final Texture DEFAULT_NORMAL_MAP_TEXTURE = ImageUtil.createDefaultNormalMap();
//	public static final Texture DEFAULT_DISPLACEMENT_MAP_TEXTURE = ImageUtil.createDefaultDisplacementMap();
	public static final Texture DEFAULT_ROUGHNESS_MAP_TEXTURE = ImageUtil.createDefaultRoughnessMap();
	public static final Texture DEFAULT_METALLIC_MAP_TEXTURE = ImageUtil.createDefaultMetallicMap();
	public static final Texture DEFAULT_EMISSIVE_MAP_TEXTURE = ImageUtil.createDefaultEmissiveMap();
	
//	public static final Texture defaultCubeMap = Util.createDefaultCubeMap();
	
	public static final float DEFAULT_ROUGHNESS = 0;
	public static final float DEFAULT_METALLIC = 0;
	public static final float DEFAULT_EMISSIVE = 0;
	
//	public static final float DEFAULT_DISP_MAP_SCALE = 0.0f;
//	public static final float DEFAULT_DISP_MAP_OFFSET = 0.0f;
	
	private final Map<String, Texture> textureMap;
	private final Map<String, CubeMap> cubeMapMap;
	private final Map<String, Vector3f> vector3fMap;
	private final Map<String, Float> floatMap;
	
	public Material(){
		textureMap = new HashMap<String, Texture>();
		cubeMapMap = new HashMap<String, CubeMap>();
		vector3fMap = new HashMap<String, Vector3f>();
		floatMap = new HashMap<String, Float>();
				
		setTexture("diffuseMap", DEFAULT_DIFFUSE_MAP_TEXTURE);
		setTexture("normalMap", DEFAULT_NORMAL_MAP_TEXTURE);
//		setTexture("dispMap", DEFAULT_DISPLACEMENT_MAP_TEXTURE);
		setTexture("roughnessMap", DEFAULT_ROUGHNESS_MAP_TEXTURE);
		setTexture("metallicMap", DEFAULT_METALLIC_MAP_TEXTURE);
		setTexture("emissiveMap", DEFAULT_EMISSIVE_MAP_TEXTURE);
		
		setFloat("roughness", DEFAULT_ROUGHNESS);
		setFloat("metallic", DEFAULT_METALLIC);
		setFloat("emissive", DEFAULT_EMISSIVE);
		
//		/*float baseBias = defaultDispMapScale/2.0f;*/
//		setFloat("dispMapScale", DEFAULT_DISP_MAP_SCALE);
//		/*setFloat("dispMapBias", -baseBias + baseBias * defaultDispMapOffset);*/
//		setFloat("dispMapBias", DEFAULT_DISP_MAP_OFFSET);
	}
	
	public void setDiffuseMap(Texture texture){
		setTexture("diffuseMap", texture);
	}
	public void setNormalMap(Texture texture){
		setTexture("normalMap", texture);
	}
	public void setRoughnessMap(Texture texture){
		setTexture("roughnessMap", texture);
	}
	public void setMetallicMap(Texture texture){
		setTexture("metallicMap", texture);
	}
	public void setEmissiveMap(Texture texture){
		setTexture("emissiveMap", texture);
	}
	
	public void setRoughness(float value){
		setFloat("roughness", value);
	}
	public void setMetallic(float value){
		setFloat("metallic", value);
	}
	public void setEmissive(float value){
		setFloat("emissive", value);
	}
	
	public Texture getDiffuseMap(){
		return getTexture("diffuse");
	}
	public Texture getNormalMap(){
		return getTexture("normalMap");
	}
	public Texture getRoughnessMap(){
		return getTexture("roughnessMap");
	}
	public Texture getMetallicMap(){
		return getTexture("metallicMap");
	}
	public Texture getEmissiveMap(){
		return getTexture("emissiveMap");
	}
	
	public float getRoughness(){
		return getFloat("roughness");
	}
	public float getMetallic(){
		return getFloat("metallic");
	}
	public float getEmissive(){
		return getFloat("emissive");
	}
	
//	@Override
//	public void setFloat(String name, float floatValue){
//		if(name.equals("dispMapBias")){
////			float dispMapOffset = floatValue;
//			final float baseBias = getFloat("dispMapScale")/2.0f;
//			
//			floatValue = -baseBias + baseBias * /*dispMapOffset*/floatValue;
//		}else if(name.equals("dispMapScale")){
//			final float dispMapScale = getFloat("dispMapScale");
////			float dispMapBias = getFloat("dispMapBias");
//			
////			float dispMapOffset = (dispMapBias + (dispMapScale/2))/(dispMapScale/2);
////			float newDispMapBias = -(floatValue/2) + (floatValue/2) * dispMapOffset;
//			
//			super.setFloat("dispMapBias", /*newDispMapBias*/-(floatValue/2) + (floatValue/2) * ((getFloat("dispMapBias") + (dispMapScale/2))/(dispMapScale/2)));
//		}
//		
//		super.setFloat(name, floatValue);
//	}
	
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
		return textureMap.get(name);
	}
	public CubeMap getCubeMap(String name){
		return cubeMapMap.get(name);
	}
	public Vector3f getVector3f(String name){
		return vector3fMap.get(name);
	}
	public float getFloat(String name){
		return floatMap.get(name);
	}
}
