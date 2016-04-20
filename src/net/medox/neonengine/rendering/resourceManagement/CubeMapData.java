package net.medox.neonengine.rendering.resourceManagement;

import java.nio.ByteBuffer;

import net.medox.neonengine.core.ReferenceCounter;

public class CubeMapData extends ReferenceCounter{
	public CubeMapData(int textureTarget, int[] width, int[] height, ByteBuffer[] data, int filters, int internalFormat, int format, int type, boolean clamp, int attachments){
		super();
	}
	
	public void bind(int samplerSlot){
		
	}
	
	public int[] getWidth(){
		return new int[]{-1, -1, -1, -1, -1, -1};
	}
	
	public int[] getHeight(){
		return new int[]{-1, -1, -1, -1, -1, -1};
	}
	
	public void dispose(){
		
	}
}
