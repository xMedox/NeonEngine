package net.medox.neonengine.rendering.resourceManagement;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.core.Util;

public class CubeMapData extends ReferenceCounter{
	private final int textureTarget;
	
	private int textureID;
	private int frameBuffer;
	private int renderBuffer;
	private int width[];
	private int height[];
	
	public CubeMapData(int textureTarget, int[] width, int[] height, ByteBuffer[] data, int filters, int internalFormat, int format, int type, boolean clamp, int attachments){		
		super();
		
		this.textureTarget = textureTarget;
		
		if(!NeonEngine.is2x2TextureEnabled()){
			this.width = width;
			this.height = height;
		}else{
			this.width = new int[]{2, 2, 2, 2, 2, 2};
			this.height = new int[]{2, 2, 2, 2, 2, 2};
		}
		
		frameBuffer = 0;
		renderBuffer = 0;
		
		initTextures(data, filters, internalFormat, format, type, clamp);
		initRenderTargets(attachments);
	}
	
	public void bind(int samplerSlot){
		if(samplerSlot < 0 || samplerSlot > 31){
			NeonEngine.throwError("Error: The sampler slot is too high or too low.");
		}
		
//		if(RenderingEngine.textureBound.get(samplerSlot) != textureID){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
			GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
			
//			RenderingEngine.textureBound.put(samplerSlot, textureID);
//		}
	}
	
	public void bindAsRenderTarget(int face){
//		for(int i = 0; i < 32; i++){
//			RenderingEngine.textureBound.put(i, -1);
//		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, textureID, 0);
		
		if(!NeonEngine.is1x1ViewportEnabled()){
			GL11.glViewport(0, 0, width[face], height[face]);
		}else{
			GL11.glViewport(0, 0, 1, 1);
		}
	}
	
	public void changeRenderBufferSize(int width, int height){
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
	}
	
	public void bindAsRenderTarget(int face, int mip){
//		for(int i = 0; i < 32; i++){
//			RenderingEngine.textureBound.put(i, -1);
//		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
		GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + face, textureID, mip);
		
		if(!NeonEngine.is1x1ViewportEnabled()){
			GL11.glViewport(0, 0, (int)(width[face] * Math.pow(0.5, mip)), (int)(height[face] * Math.pow(0.5, mip)));
		}else{
			GL11.glViewport(0, 0, 1, 1);
		}
	}
	
	public int[] getWidth(){
		return width;
	}
	
	public int[] getHeight(){
		return height;
	}
	
	private void initTextures(ByteBuffer[] data, int filters, int internalFormat, int format, int type, boolean clamp){
//		this.internalFormat = internalFormat;
//		this.format = format;
		
//		textureID = glGenTextures();
////		GL13.glActiveTexture(GL13.GL_TEXTURE0);
//		glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
//		
//		for(int i = 0; i < 6; i++){
//			glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL_TEXTURE_MIN_FILTER, filters[i]);
//			glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL_TEXTURE_MAG_FILTER, filters[i]);
//			
//			if(clamp){
//				glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//				glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//				
////				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_MODE_ARB, ARBShadow.GL_COMPARE_R_TO_TEXTURE_ARB);
////				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, ARBShadow.GL_TEXTURE_COMPARE_FUNC_ARB, GL11.GL_LEQUAL);
//			}
//
//			glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, internalFormat[i], width, height, 0, format[i], GL_UNSIGNED_BYTE, data[i]);
//			
//			if(filters[i] == GL_NEAREST_MIPMAP_NEAREST ||
//					filters[i] == GL_NEAREST_MIPMAP_LINEAR ||
//					filters[i] == GL_LINEAR_MIPMAP_NEAREST ||
//					filters[i] == GL_LINEAR_MIPMAP_LINEAR){
//				GL30.glGenerateMipmap(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i);
//				float maxAnisotropy = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
//				glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, maxAnisotropy)/*Clamp(0.0f, 8.0f, maxAnisotropy)*/);
//			}else{
//				glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL12.GL_TEXTURE_BASE_LEVEL, 0);
//				glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, GL12.GL_TEXTURE_MAX_LEVEL, 0);
//			}
//		}
		textureID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		
//		RenderingEngine.textureBound.put(0, textureID);
		
		for(int i = 0; i < 6; i++){
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, internalFormat, width[i], height[i], 0, format, type, data[i]);
		}
		
		GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, filters);
		GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, filters);
		
		if(clamp){
			GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		}
		
		if(filters == GL11.GL_NEAREST_MIPMAP_NEAREST || filters == GL11.GL_NEAREST_MIPMAP_LINEAR || filters == GL11.GL_LINEAR_MIPMAP_NEAREST || filters == GL11.GL_LINEAR_MIPMAP_LINEAR){
			GL30.glGenerateMipmap(GL13.GL_TEXTURE_CUBE_MAP);
			GL11.glTexParameterf(GL13.GL_TEXTURE_CUBE_MAP, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)));
		}else{
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_BASE_LEVEL, 0);
			GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL12.GL_TEXTURE_MAX_LEVEL, 0);
		}
	}
	
	public void glGenerateMipmap(){
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		GL30.glGenerateMipmap(GL13.GL_TEXTURE_CUBE_MAP);
	}
	
	private void initRenderTargets(int attachments){
		if(attachments == 0){
			return;
		}
		
		final IntBuffer drawBuffers = DataUtil.createIntBuffer(/*32*/1);
//		if(1 > 32){
//			NeonEngine.throwError("Error: Too many textures");
//		}
		
		boolean hasDepth = false;
		for(int i = 0; i < 1; i++){
			if(attachments == GL30.GL_DEPTH_ATTACHMENT){
				drawBuffers.put(i, GL11.GL_NONE);
				hasDepth = true;
			}else{
				drawBuffers.put(i, attachments); 
			}
			
			if(attachments == GL11.GL_NONE){
				continue;
			}
			
			if(frameBuffer == 0){
				frameBuffer = GL30.glGenFramebuffers();
				GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
			}
			
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachments, textureTarget, textureID, 0);
		}
		
		if(frameBuffer == 0){
			return;
		}
		
		if(!hasDepth){
			renderBuffer = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width[0], height[0]);
			GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, renderBuffer);
		}
		
		GL20.glDrawBuffers(drawBuffers);
		
//		glDrawBuffer(GL_NONE);
//		glReadBuffer(GL_NONE);
		
		if(GL30.glCheckFramebufferStatus(GL30.GL_FRAMEBUFFER) != GL30.GL_FRAMEBUFFER_COMPLETE){
			NeonEngine.throwError("Error: Framebuffer creation failed.");
		}
		
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}
	
	public void dispose(){
//		if(textureID != MemoryUtil.NULL){
			GL11.glDeleteTextures(textureID);
			
//			textureID = MemoryUtil.NULL;
//		}
//		if(frameBuffer != MemoryUtil.NULL){
//			GL30.glDeleteFramebuffers(frameBuffer);
//			
//			frameBuffer = MemoryUtil.NULL;
//		}
//		if(renderBuffer != MemoryUtil.NULL){
//			GL30.glDeleteRenderbuffers(renderBuffer);
//			
//			renderBuffer = MemoryUtil.NULL;
//		}
	}
}
