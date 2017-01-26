package net.medox.neonengine.lighting;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.PerspectiveBase;
import net.medox.neonengine.rendering.Shader;

public class SpotLight extends PointLight{
	private float cutoff;
	
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float viewAngle){
		super(color, intensity, SPOT_LIGHT);
		
		init(attenuation, viewAngle);
		
		setShader(new Shader("forwardSpot"));
		
		setShadowInfo(new ShadowInfo(null, false, 0, 0, 0, 0));
	}
	
	public SpotLight(Vector3f color, float intensity, Attenuation attenuation, float viewAngle, int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		super(color, intensity, SPOT_LIGHT);
		
		init(attenuation, viewAngle);
		
		setShader(new Shader("forwardSpot"));
		
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
			setShadowInfo(new ShadowInfo(new PerspectiveBase(viewAngle, 1.0f, 0.1f, getRange()), false, shadowMapSizeAsPowerOf2, shadowSoftness, lightBleedReductionAmount, minVariance));
		}
	}
	
	public void init(Attenuation attenuation, float viewAngle){
		super.init(attenuation);
		
		cutoff = (float)Math.cos(viewAngle/2);
	}
	
	public float getCutoff(){
		return cutoff;
	}
}
