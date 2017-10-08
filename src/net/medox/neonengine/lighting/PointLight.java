package net.medox.neonengine.lighting;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.PerspectiveBase;
import net.medox.neonengine.rendering.Shader;

public class PointLight extends BaseLight{
	private static final int COLOR_DEPTH = 256;
	
	private Attenuation attenuation;
	private float range;
	
	protected PointLight(Vector3f color, float intensity, int type){
		super(color, intensity, type);
	}
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation){
		super(color, intensity, POINT_LIGHT);
		
		init(attenuation);
		
		setShader(new Shader("deferredPoint"));
		
		setShadowInfo(new ShadowInfo(null, false, 0, 0, 0, 0));
	}
	
	public PointLight(Vector3f color, float intensity, Attenuation attenuation, int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		super(color, intensity, POINT_LIGHT);
		
		init(attenuation);
		
		setShader(new Shader("deferredPoint"));
		
		if(NeonEngine.getShadowQuality() >= 1 && shadowMapSizeAsPowerOf2 != 0){
			shadowMapSizeAsPowerOf2 -= 1;
			
			if(NeonEngine.getShadowQuality() >= 2){
				shadowMapSizeAsPowerOf2 -= 1;
			}
			
			if(shadowMapSizeAsPowerOf2 < 1){
				shadowMapSizeAsPowerOf2 = 1;
			}
		}
		if(shadowMapSizeAsPowerOf2 != 0){
			setShadowInfo(new ShadowInfo(new PerspectiveBase((float)Math.toRadians(90.0f), 1.0f, 0.1f, range), false, shadowMapSizeAsPowerOf2, shadowSoftness, lightBleedReductionAmount, minVariance));
		}
	}
	
	public void init(Attenuation attenuation){
		this.attenuation = attenuation;
		
		final float a = attenuation.getExponent();
		final float b = attenuation.getLinear();
		final float c = attenuation.getConstant() - COLOR_DEPTH * getIntensity() * getColor().max();
		
		this.range = (float)(-b + Math.sqrt(b * b - 4 * a * c))/(2 * a);
	}
	
	public float getRange(){
		return range;
	}
	
	public Attenuation getAttenuation(){
		return attenuation;
	}
}
