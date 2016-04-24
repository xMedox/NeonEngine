package net.medox.neonengine.rendering;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class DirectionalLight extends BaseLight{
	private float halfShadowArea;
	
	public DirectionalLight(Vector3f color, float intensity){
		super(color, intensity);
		
		setShader(new Shader("forwardDirectional"));
		
		setShadowInfo(new ShadowInfo(null, true, 0, 0, 0, 0));
	}
	
	public DirectionalLight(Vector3f color, float intensity, int shadowMapSizeAsPowerOf2, float shadowArea, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		super(color, intensity);
		
		setShader(new Shader("forwardDirectional"));

		this.halfShadowArea = shadowArea/2.0f;
		
		if(NeonEngine.OPTION_SHADOW_QUALITY >= 1 && shadowMapSizeAsPowerOf2 != 0){
			shadowMapSizeAsPowerOf2 -= 1;
			
			if(NeonEngine.OPTION_SHADOW_QUALITY >= 2){
				shadowMapSizeAsPowerOf2 -= 1;
			}
			
			if(shadowMapSizeAsPowerOf2 < 1){
				shadowMapSizeAsPowerOf2 = 1;
			}
		}
		if(shadowMapSizeAsPowerOf2 != 0){
			setShadowInfo(new ShadowInfo(new Orthographic(-halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea, -halfShadowArea, halfShadowArea), true, shadowMapSizeAsPowerOf2, shadowSoftness, lightBleedReductionAmount, minVariance));
		}
	}
	
	@Override
	public ShadowCameraTransform calcShadowCameraTransform(Vector3f mainCameraPos, Quaternion mainCameraRot){
		final ShadowCameraTransform result = new ShadowCameraTransform(mainCameraPos.add(mainCameraRot.getForward().mul(halfShadowArea)), getTransform().getTransformedRot());
		
		final float worldTexelSize = (halfShadowArea*2)/((float)(1 << getShadowInfo().getShadowMapSizeAsPowerOf2()));
		
		final Vector3f lightSpaceCameraPos = result.pos.rotate(result.rot.conjugate());
		
		lightSpaceCameraPos.setX(worldTexelSize * (float)Math.floor(lightSpaceCameraPos.getX() / worldTexelSize));
		lightSpaceCameraPos.setY(worldTexelSize * (float)Math.floor(lightSpaceCameraPos.getY() / worldTexelSize));
		
		result.pos = lightSpaceCameraPos.rotate(result.rot);
		
		return result;
	}
}
