package net.medox.neonengine.lighting;

import net.medox.neonengine.rendering.BaseCamera;

public class ShadowInfo{
	private final BaseCamera base;
	private final boolean flipFaces;
	private final int shadowMapSizeAsPowerOf2;
	private final float shadowSoftness;
	private final float lightBleedReductionAmount;
	private final float minVariance;
	
	public ShadowInfo(BaseCamera base, boolean flipFaces, int shadowMapSizeAsPowerOf2){
		this.base = base;
		this.flipFaces = flipFaces;
		this.shadowMapSizeAsPowerOf2 = shadowMapSizeAsPowerOf2;
		shadowSoftness = 1.0f;
		lightBleedReductionAmount = 0.2f;
		minVariance = 0.00002f;
	}
	
	public ShadowInfo(BaseCamera base, boolean flipFaces, int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		this.base = base;
		this.flipFaces = flipFaces;
		this.shadowMapSizeAsPowerOf2 = shadowMapSizeAsPowerOf2;
		this.shadowSoftness = shadowSoftness;
		this.lightBleedReductionAmount = lightBleedReductionAmount;
		this.minVariance = minVariance;
	}
	
	public int getShadowMapSizeAsPowerOf2(){
		return shadowMapSizeAsPowerOf2;
	}

	public float getShadowSoftness(){
		return shadowSoftness;
	}

	public float getLightBleedReductionAmount(){
		return lightBleedReductionAmount;
	}

	public float getMinVariance(){
		return minVariance;
	}

	public BaseCamera getBase(){
		return base;
	}

	public boolean shouldFlipFaces(){
		return flipFaces;
	}
}
