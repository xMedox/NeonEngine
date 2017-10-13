package net.medox.neonengine.rendering.resourceManagement;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
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

public class TextureData extends ReferenceCounter{
	private final int textureTarget;
	private final int numTextures;
	
	private int[] textureID;
	private int frameBuffer;
	private int renderBuffer;
	private int width;
	private int height;
	
	public TextureData(int textureTarget, int width, int height, int numTextures, ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachments){
		super();
		
		textureID = new int[numTextures];
		this.textureTarget = textureTarget;
		this.numTextures = numTextures;
		
		if(!NeonEngine.is2x2TextureEnabled()){
			this.width = width;
			this.height = height;
		}else{
			this.width = 2;
			this.height = 2;
		}
		
		frameBuffer = 0;
		renderBuffer = 0;
		
		initTextures(data, filters, internalFormat, format, type, clamp);
		initRenderTargets(attachments);
	}
	
	public TextureData(int textureTarget, int width, int height, int numTextures, FloatBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachments){
		super();
		
		textureID = new int[numTextures];
		this.textureTarget = textureTarget;
		this.numTextures = numTextures;
		
		if(!NeonEngine.is2x2TextureEnabled()){
			this.width = width;
			this.height = height;
		}else{
			this.width = 2;
			this.height = 2;
		}
		
		frameBuffer = 0;
		renderBuffer = 0;
		
		initTexturesFloat(data, filters, internalFormat, format, type, clamp);
		initRenderTargets(attachments);
	}
	
	public void bind(int samplerSlot){
		if(samplerSlot < 0 || samplerSlot > 31){
			NeonEngine.throwError("Error: The sampler slot is too high or too low.");
		}
		
//		if(RenderingEngine.textureBound.get(samplerSlot) != textureID[0]){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
			GL11.glBindTexture(textureTarget, textureID[0]);
			
//			RenderingEngine.textureBound.put(samplerSlot, textureID[0]);
//		}
	}
	
	public void bind(int samplerSlot, int id){
		if(samplerSlot < 0 || samplerSlot > 31){
			NeonEngine.throwError("Error: The sampler slot is too high or too low.");
		}
		
//		if(RenderingEngine.textureBound.get(samplerSlot) != textureID[id]){
			GL13.glActiveTexture(GL13.GL_TEXTURE0 + samplerSlot);
			GL11.glBindTexture(textureTarget, textureID[id]);
			
//			RenderingEngine.textureBound.put(samplerSlot, textureID[id]);
//		}
	}
	
	public void bindAsReadTarget(){
		GL30.glBindFramebuffer(GL30.GL_READ_FRAMEBUFFER, frameBuffer);
	}
	
	public void bindAsDrawTarget(){
		GL30.glBindFramebuffer(GL30.GL_DRAW_FRAMEBUFFER, frameBuffer);
	}
	
    public void bindAsRenderTarget(){
//		for(int i = 0; i < 32; i++){
//			RenderingEngine.textureBound.put(i, -1);
//		}
		
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
    	
    	if(!NeonEngine.is1x1ViewportEnabled()){
    		GL11.glViewport(0, 0, width, height);
    	}else{
    		GL11.glViewport(0, 0, 1, 1);
    	}
    }
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	private void initTextures(ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp){
		for(int i = 0; i < numTextures; i++){
			textureID[i] = GL11.glGenTextures();
			
			GL11.glBindTexture(textureTarget, textureID[i]);
//			RenderingEngine.textureBound.put(0, textureID[i]);
			
			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MIN_FILTER, filters[i]);
			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MAG_FILTER, filters[i]);
			
			if(clamp){
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			}
			
			GL11.glTexImage2D(textureTarget, 0, internalFormat[i], width, height, 0, format[i], type[i], data[i]);
			
			if(filters[i] == GL11.GL_NEAREST_MIPMAP_NEAREST || filters[i] == GL11.GL_NEAREST_MIPMAP_LINEAR || filters[i] == GL11.GL_LINEAR_MIPMAP_NEAREST || filters[i] == GL11.GL_LINEAR_MIPMAP_LINEAR){
				GL30.glGenerateMipmap(textureTarget);
				GL11.glTexParameterf(textureTarget, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)));
			}else{
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_BASE_LEVEL, 0);
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_MAX_LEVEL, 0);
			}
		}
	}
	
	private void initTexturesFloat(FloatBuffer[] data, int[] filters, int[] internalFormat, int[] format, int[] type, boolean clamp){
		for(int i = 0; i < numTextures; i++){
			textureID[i] = GL11.glGenTextures();
			
			GL11.glBindTexture(textureTarget, textureID[i]);
//			RenderingEngine.textureBound.put(0, textureID[i]);
			
			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MIN_FILTER, filters[i]);
			GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_MAG_FILTER, filters[i]);
			
			if(clamp){
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
				GL11.glTexParameterf(textureTarget, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
			}
			
			GL11.glTexImage2D(textureTarget, 0, internalFormat[i], width, height, 0, format[i], type[i], data[i]);
			
			if(filters[i] == GL11.GL_NEAREST_MIPMAP_NEAREST || filters[i] == GL11.GL_NEAREST_MIPMAP_LINEAR || filters[i] == GL11.GL_LINEAR_MIPMAP_NEAREST || filters[i] == GL11.GL_LINEAR_MIPMAP_LINEAR){
				GL30.glGenerateMipmap(textureTarget);
				GL11.glTexParameterf(textureTarget, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Util.clamp(0.0f, 8.0f, GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT)));
			}else{
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_BASE_LEVEL, 0);
				GL11.glTexParameteri(textureTarget, GL12.GL_TEXTURE_MAX_LEVEL, 0);
			}
		}
	}
	
	private void initRenderTargets(int[] attachments){
		if(attachments == null){
			return;
		}
		
		final IntBuffer drawBuffers = DataUtil.createIntBuffer(numTextures);
		if(numTextures > 32){
			NeonEngine.throwError("Error: Too many textures.");
		}
		
		boolean hasDepth = false;
		for(int i = 0; i < numTextures; i++){
			if(attachments[i] == GL30.GL_DEPTH_ATTACHMENT){
				drawBuffers.put(i, GL11.GL_NONE);
				hasDepth = true;
			}else{
				drawBuffers.put(i, attachments[i]); 
			}
			
			if(attachments[i] == GL11.GL_NONE){
				continue;
			}
			
			if(frameBuffer == 0){
				frameBuffer = GL30.glGenFramebuffers();
				GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);
			}
			
			GL30.glFramebufferTexture2D(GL30.GL_FRAMEBUFFER, attachments[i], textureTarget, textureID[i], 0);
		}
		
		if(frameBuffer == 0){
			return;
		}
		
		if(!hasDepth){
			renderBuffer = GL30.glGenRenderbuffers();
			GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, renderBuffer);
			GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
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
	
	public int getID(){
		return textureID[0];
	}
	
	public void dispose(){
//		if(textureID != null){
			GL11.glDeleteTextures(textureID);
			
//			textureID = null;
//		}
//		if(frameBuffer != MemoryUtil.NULL){
			GL30.glDeleteFramebuffers(frameBuffer);
			
//			frameBuffer = MemoryUtil.NULL;
//		}
//		if(renderBuffer != MemoryUtil.NULL){
			GL30.glDeleteRenderbuffers(renderBuffer);
			
//			renderBuffer = MemoryUtil.NULL;
//		}
	}
}
