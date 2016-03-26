package net.medox.neonengine.rendering;

import net.medox.neonengine.core.Util;
import net.medox.neonengine.rendering.resourceManagement.MappedValues;

public class ParticleMaterial extends MappedValues{
	public static final Texture DEFAULT_DIFFUSE_TEXTURE = Util.createDefaultDiffuseMap();
	
	public static final float DEFAULT_SPECULAR_INTENSITY = 0;
	public static final float DEFAULT_SPECULAR_POWER = 0;
	public static final float DEFAULT_GLOW = 0;
	
	public ParticleMaterial(){
		super();
		
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
}
