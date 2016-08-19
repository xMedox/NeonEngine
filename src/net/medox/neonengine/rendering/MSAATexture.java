package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.rendering.resourceManagement.MSAATextureData;

public class MSAATexture{
	private static final Map<String, MSAATextureData> loadedTextures = new ConcurrentHashMap<String, MSAATextureData>();
	
	private final String fileName;
	
	private MSAATextureData resource;
	
	public MSAATexture(int width, int height, ByteBuffer data, int samples, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		fileName = "";
		resource = new MSAATextureData(samples, width, height, 1, new ByteBuffer[]{data}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
	}
	
	public MSAATexture(int width, int height, ByteBuffer[] data, int samples, int[] filter, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachment){
		fileName = "";
		resource = new MSAATextureData(samples, width, height, data.length, data, filter, internalFormat, format, type, clamp, attachment);
	}
	
	@Override
	protected void finalize() throws Throwable{
		if(resource.removeReference() && !fileName.isEmpty()){
			resource.dispose();
			loadedTextures.remove(fileName);
		}
		
		super.finalize();
	}
	
	public void bind(int samplerSlot){
		resource.bind(samplerSlot);
	}
	
//	public void clear(int textureNum, int mask){
//		resource.clear(textureNum, mask);
//	}
	
	public void bind(int samplerSlot, int id){
		resource.bind(samplerSlot, id);
	}
	
	public void bindAsRenderTarget(){
		resource.bindAsRenderTarget();
	}
	
	public void transferToTexture(Texture texture){
		resource.transferToTexture(texture.getFrameBuffer());
	}
	
	public int getWidth(){
		return resource.getWidth();
	}
	
	public int getHeight(){
		return resource.getHeight();
	}
	
	public int getID(){
		return resource.getID();
	}
	
	public static void dispose(){
		for(final MSAATextureData data : loadedTextures.values()){
			data.dispose();
		}
	}
}
