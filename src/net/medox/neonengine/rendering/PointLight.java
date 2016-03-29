package net.medox.neonengine.rendering;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.math.Vector3f;

public class PointLight extends BaseLight{
	protected static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;
	private float range;
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation){
		super(color, intensity);
		this.attenuation = attenuation;
		
		final float a = attenuation.getExponent();
		final float b = attenuation.getLinear();
		final float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c))/(2 * a);
		
		setShader(new Shader("forwardPoint"));
		
		setShadowInfo(new ShadowInfo(null, false, 0, 0, 0, 0));
	}
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation,
			int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		super(color, intensity);
		this.attenuation = attenuation;
		
		final float a = attenuation.getExponent();
		final float b = attenuation.getLinear();
		final float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c))/(2 * a);
		
		setShader(new Shader("forwardPoint"));
		
		if(CoreEngine.OPTION_SHADOW_QUALITY >= 1 && shadowMapSizeAsPowerOf2 != 0){
			shadowMapSizeAsPowerOf2 -= 1;
			
			if(CoreEngine.OPTION_SHADOW_QUALITY >= 2){
				shadowMapSizeAsPowerOf2 -= 1;
			}
			
			if(shadowMapSizeAsPowerOf2 < 1){
				shadowMapSizeAsPowerOf2 = 1;
			}
		}
		if(shadowMapSizeAsPowerOf2 != 0){
			setShadowInfo(new ShadowInfo(new Perspective((float)Math.toRadians(90.0f), 1.0f, 0.1f, range), false, shadowMapSizeAsPowerOf2,
				shadowSoftness, lightBleedReductionAmount, minVariance));
		}
	}
	
	public float getRange(){
		return range;
	}
	
	public Attenuation getAttenuation(){
		return attenuation;
	}
}
