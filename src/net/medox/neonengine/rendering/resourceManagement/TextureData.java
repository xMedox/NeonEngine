package net.medox.neonengine.rendering.resourceManagement;

import java.nio.ByteBuffer;

import net.medox.neonengine.core.ReferenceCounter;

public class TextureData extends ReferenceCounter{
	public TextureData(int textureTarget, int width, int height, int numTextures, ByteBuffer[] data, int[] filters, int[] internalFormat, int[] format, boolean clamp, int[] attachments){
		super();
	}
	
	public void bind(int samplerSlot){
		
	}
	
	public void bind(int samplerSlot, int id){
		
	}
	
    public void bindAsRenderTarget(){
    	
    }
	
	public int getWidth(){
		return -1;
	}
	
	public int getHeight(){
		return -1;
	}
	
	public int getID(){
		return -1;
	}
	
	public void dispose(){
		
	}
}
