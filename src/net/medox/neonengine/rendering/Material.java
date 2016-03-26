package net.medox.neonengine.rendering;

import net.medox.neonengine.core.Util;
import net.medox.neonengine.rendering.resourceManagement.MappedValues;

public class Material extends MappedValues{
	public static final Texture DEFAULT_DIFFUSE_TEXTURE = Util.createDefaultDiffuseMap();
	public static final Texture DEFAULT_NORMAL_MAP_TEXTURE = Util.createDefaultNormalMap();
//	public static final Texture DEFAULT_DISPLACEMENT_MAP_TEXTURE = Util.createDefaultDisplacementMap();
	public static final Texture DEFAULT_SPECULAR_MAP_TEXTURE = Util.createDefaultSpecularMap();
	public static final Texture DEFAULT_GLOW_MAP_TEXTURE = Util.createDefaultGlowMap();
	
//	public static final Texture defaultCubeMap = Util.createDefaultCubeMap();
	
	public static final float DEFAULT_SPECULAR_INTENSITY = 0;
	public static final float DEFAULT_SPECULAR_POWER = 0;
	
//	public static final float DEFAULT_DISP_MAP_SCALE = 0.0f;
//	public static final float DEFAULT_DISP_MAP_OFFSET = 0.0f;
	
	public Material(){
		super();
				
		setTexture("diffuse", DEFAULT_DIFFUSE_TEXTURE);
		setTexture("normalMap", DEFAULT_NORMAL_MAP_TEXTURE);
//		setTexture("dispMap", DEFAULT_DISPLACEMENT_MAP_TEXTURE);
		setTexture("specMap", DEFAULT_SPECULAR_MAP_TEXTURE);
		setTexture("glowMap", DEFAULT_GLOW_MAP_TEXTURE);
		
		setFloat("specularIntensity", DEFAULT_SPECULAR_INTENSITY);
		setFloat("specularPower", DEFAULT_SPECULAR_POWER);
		
//		/*float baseBias = defaultDispMapScale/2.0f;*/
//		setFloat("dispMapScale", DEFAULT_DISP_MAP_SCALE);
//		/*setFloat("dispMapBias", -baseBias + baseBias * defaultDispMapOffset);*/
//		setFloat("dispMapBias", DEFAULT_DISP_MAP_OFFSET);
	}
	
	public void setDiffuseMap(Texture texture){
		setTexture("diffuse", texture);
	}
	
	public void setNormalMap(Texture texture){
		setTexture("normalMap", texture);
	}
	
	public void setSpecularMap(Texture texture){
		setTexture("specMap", texture);
	}
	
	public void setGlowMap(Texture texture){
		setTexture("glowMap", texture);
	}
	
	public void setSpecularIntensity(float value){
		setFloat("specularIntensity", value);
	}
	
	public void setSpecularPower(float value){
		setFloat("specularPower", value);
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
}
