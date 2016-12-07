package net.medox.neonengine.lighting;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import net.medox.neonengine.rendering.BaseCamera;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Texture;

public class ShadowInfo{
	private final BaseCamera base;
	private final boolean flipFaces;
	private final int shadowMapSizeAsPowerOf2;
	private final Texture shadowMap;
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
		
		if(shadowMapSizeAsPowerOf2 > RenderingEngine.NUM_SHADOW_MAPS){
			System.err.println("Error: the size of the shadow map is to big");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		final int shadowMapSize = 1 << (shadowMapSizeAsPowerOf2 + 1);
		
		shadowMap = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
	}
	
	public ShadowInfo(BaseCamera base, boolean flipFaces, int shadowMapSizeAsPowerOf2, float shadowSoftness, float lightBleedReductionAmount, float minVariance){
		this.base = base;
		this.flipFaces = flipFaces;
		this.shadowMapSizeAsPowerOf2 = shadowMapSizeAsPowerOf2;
		this.shadowSoftness = shadowSoftness;
		this.lightBleedReductionAmount = lightBleedReductionAmount;
		this.minVariance = minVariance;
		
		if(shadowMapSizeAsPowerOf2 > RenderingEngine.NUM_SHADOW_MAPS){
			System.err.println("Error: the size of the shadow map is to big");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		final int shadowMapSize = 1 << (shadowMapSizeAsPowerOf2 + 1);
		
		shadowMap = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
	}
	
	public int getShadowMapSizeAsPowerOf2(){
		return shadowMapSizeAsPowerOf2;
	}
	
	public Texture getShadowMap(){
		return shadowMap;
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
