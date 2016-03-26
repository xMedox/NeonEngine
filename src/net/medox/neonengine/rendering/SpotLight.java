package net.medox.neonengine.rendering;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.math.Vector3f;

public class SpotLight extends PointLight{
	private float cutoff;
	
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float viewAngle){
		super(color, intensity, attenuation);
		
		cutoff = (float)Math.cos(viewAngle/2);
		
		setShader(new Shader("forwardSpot"));
	}
	
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float viewAngle, 
            		 int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		this(color, intensity, attenuation, viewAngle);
		
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
			setShadowInfo(new ShadowInfo(new Perspective(viewAngle, 1.0f, 0.1f, getRange()), false, shadowMapSizeAsPowerOf2,
				shadowSoftness, lightBleedReductionAmount, minVariance));
		}
	}
	
	public float getCutoff(){
		return cutoff;
	}
}
