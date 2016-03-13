package net.medox.neonengine.rendering;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.rendering.resourceManagement.TextureData;
import net.medox.neonengine.rendering.resourceManagement.OpenGL.TextureDataGL;

public class Texture{
	private static Map<String, TextureData> loadedTextures = new HashMap<String, TextureData>();
	
	private final String fileName;
	
	private TextureData resource;
	
	private int width;
	private int height;
	
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format, boolean clamp, int attachment){
		this.fileName = fileName;
		resource = loadedTextures.get(fileName);
		
		if(resource == null){			
			final ByteBuffer texture = loadTexture(fileName);

			if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
				resource = new TextureDataGL(textureTarget, width, height, 1, new ByteBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, clamp, new int[]{attachment});
			}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
				resource = new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, clamp, new int[]{attachment});
			}
			loadedTextures.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format, boolean clamp){
		this(fileName, textureTarget, filter, internalFormat, format, clamp, RenderingEngine.NONE);
	}
	public Texture(String fileName, int textureTarget, int filter, int internalFormat, int format){
		this(fileName, textureTarget, filter, internalFormat, format, false);
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
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			resource = new TextureDataGL(RenderingEngine.TEXTURE_2D, width, height, 1, new ByteBuffer[]{texture}, new int[]{nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR}, new int[]{RenderingEngine.RGBA}, new int[]{RenderingEngine.RGBA}, false, new int[]{RenderingEngine.NONE});
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			resource = new TextureData(RenderingEngine.TEXTURE_2D, width, height, 1, new ByteBuffer[]{texture}, new int[]{nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR}, new int[]{RenderingEngine.RGBA}, new int[]{RenderingEngine.RGBA}, false, new int[]{RenderingEngine.NONE});
		}
	}
	
	public Texture(int width, int height, ByteBuffer data, int textureTarget, int filter, int internalFormat, int format, boolean clamp, int attachment){
		fileName = "";
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			resource = new TextureDataGL(textureTarget, width, height, 1, new ByteBuffer[]{data}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, clamp, new int[]{attachment});
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			resource = new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{data}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, clamp, new int[]{attachment});
		}
	}
	
	public Texture(int width, int height, ByteBuffer[] data, int textureTarget, int[] filter, int[] internalFormat, int[] format, boolean clamp, int[] attachment){
		fileName = "";
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			resource = new TextureDataGL(textureTarget, width, height, data.length, data, filter, internalFormat, format, clamp, attachment);
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			resource = new TextureData(textureTarget, width, height, data.length, data, filter, internalFormat, format, clamp, attachment);
		}
	}
	
	@Override
	protected void finalize(){
		if(resource.removeReference() && !fileName.isEmpty()){
			resource.dispose();
			loadedTextures.remove(fileName);
		}
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
	
	private ByteBuffer loadTexture(String fileName/*, boolean flipped*/){
//		String[] splitArray = fileName.split("\\.");
//		String ext = splitArray[splitArray.length - 1];
		
		BufferedImage image = null;
		
//		if(flipped){
//			image = flipBufferedImageVertical(ImageIO.read(new File("./res/textures/" + fileName)));
//		}else{
			try{
				image = ImageIO.read(new File("./res/textures/" + fileName));
			}catch(Exception e){
				e.printStackTrace();
				System.exit(1);
			}
//		}
		
		if(CoreEngine.OPTION_TEXTURE_QUALITY >= 1){
			BufferedImage before = image;
			int w = before.getWidth();
			int h = before.getHeight();
			BufferedImage after = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_ARGB);
			AffineTransform at = new AffineTransform();
			at.scale(0.5, 0.5);
			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
			after = scaleOp.filter(before, after);
			
			image = after;
			
			if(CoreEngine.OPTION_TEXTURE_QUALITY >= 2){
				before = image;
				w = before.getWidth();
				h = before.getHeight();
				after = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_ARGB);
				at = new AffineTransform();
				at.scale(0.5, 0.5);
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
	
	private ByteBuffer loadTexture(BufferedImage image/*, boolean flipped*/){
//		String[] splitArray = fileName.split("\\.");
//		String ext = splitArray[splitArray.length - 1];
		
//		if(flipped){
//			image = flipBufferedImageVertical(image);
//		}
		
//		if(Option.TEXTURE_QUALITY >= 1){
//			BufferedImage before = image;
//			int w = before.getWidth();
//			int h = before.getHeight();
//			BufferedImage after = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_ARGB);
//			AffineTransform at = new AffineTransform();
//			at.scale(0.5, 0.5);
//			AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//			after = scaleOp.filter(before, after);
//			
//			image = after;
//			
//			if(Option.TEXTURE_QUALITY >= 2){
//				before = image;
//				w = before.getWidth();
//				h = before.getHeight();
//				after = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_ARGB);
//				at = new AffineTransform();
//				at.scale(0.5, 0.5);
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
	}
}
