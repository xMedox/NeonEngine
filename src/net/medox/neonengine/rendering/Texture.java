package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryUtil;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.resourceManagement.TextureData;

public class Texture{
	private static final Map<String, TextureData> loadedTextures = new HashMap<String, TextureData>();
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
			final String[] splitArray = fileName.split("\\.");
			final String ext = splitArray[splitArray.length - 1];
			
			resource = null;
			
			if(ext.equals("hdr")){
				final FloatBuffer texture = loadHDRTexture(fileName);
				
				resource = new TextureData(textureTarget, width, height, 1, new FloatBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
				
				if(NeonEngine.getTextureQuality() >= 1){
					MemoryUtil.memFree(texture);
				}else{
					STBImage.stbi_image_free(texture);
				}
			}else{
				final ByteBuffer texture = loadTexture(fileName);
				
				resource = new TextureData(textureTarget, width, height, 1, new ByteBuffer[]{texture}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
				
				if(NeonEngine.getTextureQuality() >= 1){
					MemoryUtil.memFree(texture);
				}else{
					STBImage.stbi_image_free(texture);
				}
			}
			
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
	
	public Texture(int width, int height, FloatBuffer data, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		fileName = "";
		resource = new TextureData(textureTarget, width, height, 1, new FloatBuffer[]{data}, new int[]{filter}, new int[]{internalFormat}, new int[]{format}, new int[]{type}, clamp, new int[]{attachment});
		customTextures.add(resource);
	}
	
	public Texture(int width, int height, FloatBuffer[] data, int textureTarget, int[] filter, int[] internalFormat, int[] format, int[] type, boolean clamp, int[] attachment){
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
//		BufferedImage image = null;
//		
//		try{
//			image = ImageIO.read(new File("./res/textures/" + fileName));
//		}catch(Exception e){
//			NeonEngine.throwError("Error: unable to read " + fileName);
//		}
//		
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
//		
//		width = image.getWidth();
//		height = image.getHeight();
//		
//		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
//		
//		final ByteBuffer buffer = DataUtil.createByteBuffer(height * width * 4);
//		final boolean hasAlpha = image.getColorModel().hasAlpha();
//		
//		for(int y = 0; y < height; y++){
//			for(int x = 0; x < width; x++){
//				final int pixel = pixels[y * width + x];
//				
//				buffer.put((byte)((pixel >> 16) & 0xFF));
//				buffer.put((byte)((pixel >> 8) & 0xFF));
//				buffer.put((byte)((pixel) & 0xFF));
//				
//				if(hasAlpha){
//					buffer.put((byte)((pixel >> 24) & 0xFF));
//				}else{
//					buffer.put((byte)(0xFF));
//				}
//			}
//		}
//		
//		buffer.flip();
		
		final IntBuffer w = DataUtil.createIntBuffer(1);
		final IntBuffer h = DataUtil.createIntBuffer(1);
		final IntBuffer comp = DataUtil.createIntBuffer(1);
		
		ByteBuffer data = STBImage.stbi_load("./res/textures/" + fileName, w, h, comp, STBImage.STBI_rgb_alpha);
		
		width = w.get();
		height = h.get();
		
		if(data == null){
			NeonEngine.throwError("Error: unable to read " + fileName + " " + STBImage.stbi_failure_reason());
		}
		
		if(NeonEngine.getTextureQuality() >= 1){
			int widthSave = width/2;
			int heightSave = height/2;
			
			if(widthSave <= 0){
				widthSave = 1;
			}
			if(heightSave <= 0){
				heightSave = 1;
			}
			
			final ByteBuffer tmp = MemoryUtil.memAlloc(widthSave * heightSave * STBImage.STBI_rgb_alpha);
			
			STBImageResize.stbir_resize_uint8(data, width, height, 0, tmp, widthSave, heightSave, 0, STBImage.STBI_rgb_alpha);
			
			STBImage.stbi_image_free(data);
			
			width = widthSave;
			height = heightSave;
			
			data = tmp;
			
			if(NeonEngine.getTextureQuality() >= 2){
				int widthSave2 = widthSave/2;
				int heightSave2 = heightSave/2;
				
				if(widthSave2 <= 0){
					widthSave2 = 1;
				}
				if(heightSave2 <= 0){
					heightSave2 = 1;
				}
				
				final ByteBuffer tmp2 = MemoryUtil.memAlloc(widthSave2 * heightSave2 * STBImage.STBI_rgb_alpha);
				
				STBImageResize.stbir_resize_uint8(tmp, widthSave, heightSave, 0, tmp2, widthSave2, heightSave2, 0, STBImage.STBI_rgb_alpha);
				
				MemoryUtil.memFree(tmp);
				
				width = widthSave2;
				height = heightSave2;
				
				data = tmp2;
			}
		}
		
		return data;
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
	
	private FloatBuffer loadHDRTexture(String fileName){
		final IntBuffer w = DataUtil.createIntBuffer(1);
		final IntBuffer h = DataUtil.createIntBuffer(1);
		final IntBuffer comp = DataUtil.createIntBuffer(1);
		
		FloatBuffer data = STBImage.stbi_loadf("./res/textures/" + fileName, w, h, comp, STBImage.STBI_rgb);
		
		if(data == null){
			NeonEngine.throwError("Error: unable to read " + fileName + " " + STBImage.stbi_failure_reason());
		}
		
		width = w.get();
		height = h.get();
		
		if(NeonEngine.getTextureQuality() >= 1){
			int widthSave = width/2;
			int heightSave = height/2;
			
			if(widthSave <= 0){
				widthSave = 1;
			}
			if(heightSave <= 0){
				heightSave = 1;
			}
			
			final FloatBuffer tmp = MemoryUtil.memAllocFloat(widthSave * heightSave * STBImage.STBI_rgb);
			
			STBImageResize.stbir_resize_float(data, width, height, 0, tmp, widthSave, heightSave, 0, STBImage.STBI_rgb);
			
			STBImage.stbi_image_free(data);
			
			width = widthSave;
			height = heightSave;
			
			data = tmp;
			
			if(NeonEngine.getTextureQuality() >= 2){
				int widthSave2 = widthSave/2;
				int heightSave2 = heightSave/2;
				
				if(widthSave2 <= 0){
					widthSave2 = 1;
				}
				if(heightSave2 <= 0){
					heightSave2 = 1;
				}
				
				final FloatBuffer tmp2 = MemoryUtil.memAllocFloat(widthSave2 * heightSave2 * STBImage.STBI_rgb);
				
				STBImageResize.stbir_resize_float(tmp, widthSave, heightSave, 0, tmp2, widthSave2, heightSave2, 0, STBImage.STBI_rgb);
				
				MemoryUtil.memFree(tmp);
				
				width = widthSave2;
				height = heightSave2;
				
				data = tmp2;
			}
		}
		
		return data;
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
