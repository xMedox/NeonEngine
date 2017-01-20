package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.rendering.resourceManagement.TextureData;

public class AnimatedTexture extends Texture{
	private static final List<AnimatedTexture> animatedTextures = new ArrayList<AnimatedTexture>();
	
	private List<TextureData> resources;
	private final List<String> fileNames;
	
	private boolean autoUpdate;
	
	private int id;
	
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		resources = new ArrayList<TextureData>();
		this.fileNames = new ArrayList<String>();
		
		autoUpdate = true;
		
		id = 0;
		
		for(int i = 0; i < fileNames.length; i++){
			this.fileNames.add(fileNames[i]);
			resources.add(loadedTextures.get(fileNames[i]));
			
			if(resources.get(i) == null){
				final ByteBuffer texture = super.loadTexture(fileNames[i]);
				
				resources.set(i, new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment}));
				loadedTextures.put(fileNames[i], resources.get(i));
			}else{
				resources.get(i).addReference();
			}
			
			if(i == 0){
				super.resource = resources.get(i);
			}
		}
		
		animatedTextures.add(this);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp){
		this(fileNames, textureTarget, filter, internalFormat, format, type, clamp, RenderingEngine.NONE);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type){
		this(fileNames, textureTarget, filter, internalFormat, format, type, false);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter, int internalFormat, int format){
		this(fileNames, textureTarget, filter, internalFormat, format, RenderingEngine.UNSIGNED_BYTE);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter, int internalFormat){
		this(fileNames, textureTarget, filter, internalFormat, RenderingEngine.RGBA);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget, int filter){
		this(fileNames, textureTarget, filter, RenderingEngine.RGBA);
	}
	public AnimatedTexture(String[] fileNames, int textureTarget){
		this(fileNames, textureTarget, RenderingEngine.LINEAR_MIPMAP_LINEAR);
	}
	public AnimatedTexture(String[] fileNames){
		this(fileNames, RenderingEngine.TEXTURE_2D);
	}
	
	public static void updateAll(){
		for(AnimatedTexture data : animatedTextures){
			if(data.getAutoUpdate()){
				data.update();
			}
		}
	}
	
	public void setAutoUpdate(boolean autoUpdate){
		this.autoUpdate = autoUpdate;
	}
	
	public boolean getAutoUpdate(){
		return autoUpdate;
	}
	
	public void update(){
		if(id < resources.size()-1){
			id++;
		}else{
			id = 0;
		}
		super.resource = resources.get(id);
	}
	
	@Override
	public void cleanUp(){
		if(!cleanedUp){
			for(TextureData data : resources){
				if(data.removeReference()){
					data.dispose();
					loadedTextures.remove(fileNames.get(0));
				}
			}
			
			animatedTextures.remove(this);
			
			cleanedUp = true;
		}
	}
}
