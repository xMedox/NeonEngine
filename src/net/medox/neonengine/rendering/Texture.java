package net.medox.neonengine.rendering;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.resourceManagement.TextureData;

public class Texture{
	private static final Map<String, TextureData> loadedTextures = new ConcurrentHashMap<String, TextureData>();
	private static final List<TextureData> customTextures = new ArrayList<TextureData>();
	
	private final String fileName;
	
	private int width;
	private int height;
	
	private TextureData resource;
	private boolean cleanedUp;
	
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		this.fileName = fileName;
		resource = loadedTextures.get(fileName);
		
		if(resource == null){
			final ByteBuffer texture = loadTexture(fileName);
			
			resource = new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
			loadedTextures.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp){
		this(fileName, textureTarget, filter, internalFormat, format, type, clamp, RenderingEngine.NONE);
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format, int type){
		this(fileName, textureTarget, filter, internalFormat, format, type, false);
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format){
		this(fileName, textureTarget, filter, internalFormat, format, RenderingEngine.UNSIGNED_BYTE);
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat){
		this(fileName, textureTarget, filter, internalFormat, RenderingEngine.RGBA);
	}
	public Texture(String fileName, int textureTarget, int filter){
		this(fileName, textureTarget, filter, RenderingEngine.RGBA);
	}
	public Texture(String fileName, int textureTarget){
		this(fileName, textureTarget, RenderingEngine.LINEAR_MIPMAP_LINEAR);
	}
	public Texture(String fileName){
		this(fileName, RenderingEngine.TEXTURE_2D);
	}
	
	public Texture(String fileName, boolean nearest){
		this(fileName, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR);
	}
	
	public Texture(BufferedImage image){		
		this(image, true);
	}
	
	public Texture(BufferedImage image, boolean nearest){
		final ByteBuffer texture = loadTexture(image);
		
		fileName = "";
		resource = new TextureData(RenderingEngine.TEXTURE_2D, width, height, 1, new ByteBuffer[]{texture}, new int[]{nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR}, new int[]{RenderingEngine.RGBA}, new int[]{RenderingEngine.RGBA}, new int[]{RenderingEngine.UNSIGNED_BYTE}, false, new int[]{RenderingEngine.NONE});
		customTextures.add(resource);
	}
	
	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		fileName = "";
		resource = new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{data}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
		customTextures.add(resource);
	}
	
	public Texture(int width, int height, ByteBuffer[] data, int textureTarget, int[] filter, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachment){
		fileName = "";
		resource = new TextureData(textureTarget, width, height, data.length, data, filter, internalFormat, format, type, clamp, attachment);
		customTextures.add(resource);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(fileName.equals("")){
				resource.dispose();
				customTextures.remove(resource);
			}else if(resource.removeReference()){
				resource.dispose();
				loadedTextures.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
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
	
	public int getWidth(){
		return resource.getWidth();
	}
	
	public int getHeight(){
		return resource.getHeight();
	}
	
	private ByteBuffer loadTexture(String fileName){
		BufferedImage image = null;
		
		try{
			image = ImageIO.read(new File("./res/textures/" + fileName));
		}catch(Exception e){
			NeonEngine.throwError("Error: unable to read " + fileName);
		}
		
		if(NeonEngine.getTextureQuality() >= 1){
			BufferedImage before = image;
			int w = before.getWidth()/2;
			int h = before.getHeight()/2;
			
			if(w <= 0){
				w = 1;
			}
			if(h <= 0){
				h = 1;
			}
			
			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(before, after);
			
			image = after;
			
			if(NeonEngine.getTextureQuality() >= 2){
				before = image;
				w = before.getWidth()/2;
				h = before.getHeight()/2;
				
				if(w <= 0){
					w = 1;
				}
				if(h <= 0){
					h = 1;
				}
				
				after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
				at = new AffineTransform();
				at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
				scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				after = scaleOp.filter(before, after);
				
				image = after;
			}
		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		final ByteBuffer buffer = DataUtil.createByteBuffer(height * width * 4);
		final boolean hasAlpha = image.getColorModel().hasAlpha();
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				final int pixel = pixels[y * width + x];
				
				buffer.put((byte)((pixel >> 16) & 0xFF));
				buffer.put((byte)((pixel >> 8) & 0xFF));
				buffer.put((byte)((pixel) & 0xFF));
				
				if(hasAlpha){
					buffer.put((byte)((pixel >> 24) & 0xFF));
				}else{
					buffer.put((byte)(0xFF));
				}
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	private ByteBuffer loadTexture(BufferedImage image){
//		if(NeonEngine.getTextureQuality() >= 1){
//			BufferedImage before = image;
//			int w = before.getWidth()/2;
//			int h = before.getHeight()/2;
//			
//			if(w <= 0){
//				w = 1;
//			}
//			if(h <= 0){
//				h = 1;
//			}
//			
//			BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//			AffineTransform at = new AffineTransform();
//			at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
//			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//			after = scaleOp.filter(before, after);
//			
//			image = after;
//			
//			if(NeonEngine.getTextureQuality() >= 2){
//				before = image;
//				w = before.getWidth()/2;
//				h = before.getHeight()/2;
//				
//				if(w <= 0){
//					w = 1;
//				}
//				if(h <= 0){
//					h = 1;
//				}
//				
//				after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//				at = new AffineTransform();
//				at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
//				scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//				after = scaleOp.filter(before, after);
//				
//				image = after;
//			}
//		}
		
		width = image.getWidth();
		height = image.getHeight();
		
		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		final ByteBuffer buffer = DataUtil.createByteBuffer(height * width * 4);
		final boolean hasAlpha = image.getColorModel().hasAlpha();
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				final int pixel = pixels[y * width + x];
				
				buffer.put((byte)((pixel >> 16) & 0xFF));
				buffer.put((byte)((pixel >> 8) & 0xFF));
				buffer.put((byte)((pixel) & 0xFF));
				
				if(hasAlpha){
					buffer.put((byte)((pixel >> 24) & 0xFF));
				}else{
					buffer.put((byte)(0xFF));
				}
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public int getID(){
		return resource.getID();
	}
	
	public static void dispose(){
		for(final TextureData data : loadedTextures.values()){
			data.dispose();
		}
		
		for(final TextureData data : customTextures){
			data.dispose();
		}
	}
}
