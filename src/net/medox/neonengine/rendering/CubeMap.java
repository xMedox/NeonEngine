package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.stb.STBImageResize;
import org.lwjgl.system.MemoryUtil;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.resourceManagement.CubeMapData;

public class CubeMap{
	private static final Map<String, CubeMapData> loadedCubeMaps = new HashMap<String, CubeMapData>();
	
	private final String fileNames[];
	
	private int width[] = new int[6];
	private int height[] = new int[6];
	
	private CubeMapData resource;
	private boolean cleanedUp;
	
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		this.fileNames = fileNames;
		resource = loadedCubeMaps.get(fileNames[0] + " " + fileNames[1] + " " + fileNames[2] + " " + fileNames[3] + " " + fileNames[4] + " " + fileNames[5]);
		
		if(resource == null){
			final ByteBuffer[] textures = loadTexture(fileNames);
			
			resource = new CubeMapData(textureTarget, width, height, textures, filter, internalFormat, format, type, clamp, attachment);
			loadedCubeMaps.put(fileNames[0] + " " + fileNames[1] + " " + fileNames[2] + " " + fileNames[3] + " " + fileNames[4] + " " + fileNames[5], resource);
			
			if(NeonEngine.getTextureQuality() >= 1){
				MemoryUtil.memFree(textures[0]);
				MemoryUtil.memFree(textures[1]);
				MemoryUtil.memFree(textures[2]);
				MemoryUtil.memFree(textures[3]);
				MemoryUtil.memFree(textures[4]);
				MemoryUtil.memFree(textures[5]);
			}else{
				STBImage.stbi_image_free(textures[0]);
				STBImage.stbi_image_free(textures[1]);
				STBImage.stbi_image_free(textures[2]);
				STBImage.stbi_image_free(textures[3]);
				STBImage.stbi_image_free(textures[4]);
				STBImage.stbi_image_free(textures[5]);
			}
		}else{
			resource.addReference();
		}
	}
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp){
		this(fileNames, textureTarget, filter, internalFormat, format, type, clamp, RenderingEngine.NONE);
	}
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type){
		this(fileNames, textureTarget, filter, internalFormat, format, type, false);
	}
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat, int format){
		this(fileNames, textureTarget, filter, internalFormat, format, RenderingEngine.UNSIGNED_BYTE);
	}
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat){
		this(fileNames, textureTarget, filter, internalFormat, RenderingEngine.RGBA);
	}
	public CubeMap(String[] fileNames, int textureTarget, int filter){
		this(fileNames, textureTarget, filter, RenderingEngine.RGBA);
	}
	public CubeMap(String[] fileNames, int textureTarget){
		this(fileNames, textureTarget, RenderingEngine.LINEAR_MIPMAP_LINEAR);
	}
	public CubeMap(String[] fileNames){
		this(fileNames, RenderingEngine.TEXTURE_2D);
	}
	
	public CubeMap(String[] fileNames, boolean nearest){
		this(fileNames, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR);
	}
	
	public CubeMap(int width, int height, ByteBuffer[] data, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		fileNames = new String[]{"", "", "", "", "", ""};
		resource = new CubeMapData(textureTarget, new int[]{width, width, width, width, width, width}, new int[]{height, height, height, height, height, height}, data, filter, internalFormat, format, type, clamp, attachment);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(resource.removeReference() && !fileNames[0].equals("") && !fileNames[1].equals("") && !fileNames[2].equals("") && !fileNames[3].equals("") && !fileNames[4].equals("") && !fileNames[5].equals("")){
				resource.dispose();
				loadedCubeMaps.remove(fileNames);
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
	
	public void bindAsRenderTarget(int face){
		resource.bindAsRenderTarget(face);
	}
	
	public void changeRenderBufferSize(int width, int height){
		resource.changeRenderBufferSize(width, height);
	}
	
	public void bindAsRenderTarget(int face, int mip){
		resource.bindAsRenderTarget(face, mip);
	}
	
	public int[] getWidth(){
		return resource.getWidth();
	}
	
	public int[] getHeight(){
		return resource.getHeight();
	}
	
	private ByteBuffer[] loadTexture(String[] fileNames){
		ByteBuffer[] byteBuffer = new ByteBuffer[6];
		
		for(int i = 0; i < fileNames.length; i++){
//			BufferedImage image = null;
//			
//			try{
//				image = ImageIO.read(new File("./res/textures/" + fileNames[i]));
//			}catch(Exception e){
//				NeonEngine.throwError("Error: unable to read " + fileNames[i]);
//			}
//			
//			if(NeonEngine.getTextureQuality() >= 1){
//				BufferedImage before = image;
//				int w = before.getWidth()/2;
//				int h = before.getHeight()/2;
//				
//				if(w <= 0){
//					w = 1;
//				}
//				if(h <= 0){
//					h = 1;
//				}
//				
//				BufferedImage after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//				AffineTransform at = new AffineTransform();
//				at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
//				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//				after = scaleOp.filter(before, after);
//				
//				image = after;
//				
//				if(NeonEngine.getTextureQuality() >= 2){
//					before = image;
//					w = before.getWidth()/2;
//					h = before.getHeight()/2;
//					
//					if(w <= 0){
//						w = 1;
//					}
//					if(h <= 0){
//						h = 1;
//					}
//					
//					after = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
//					at = new AffineTransform();
//					at.scale((double)w/(double)image.getWidth(), (double)h/(double)image.getHeight());
//					scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
//					after = scaleOp.filter(before, after);
//					
//					image = after;
//				}
//			}
//			
//			width[i] = image.getWidth();
//			height[i] = image.getHeight();
//			
//			final int[] pixels = image.getRGB(0, 0, width[i], height[i], null, 0, width[i]);
//			
//			final ByteBuffer buffer = DataUtil.createByteBuffer(height[i] * width[i] * 4);
//			final boolean hasAlpha = image.getColorModel().hasAlpha();
//			
//			for(int y = 0; y < height[i]; y++){
//				for(int x = 0; x < width[i]; x++){
//					final int pixel = pixels[y * width[i] + x];
//					
//					buffer.put((byte)((pixel >> 16) & 0xFF));
//					buffer.put((byte)((pixel >> 8) & 0xFF));
//					buffer.put((byte)((pixel) & 0xFF));
//					
//					if(hasAlpha){
//						buffer.put((byte)((pixel >> 24) & 0xFF));
//					}else{
//						buffer.put((byte)(0xFF));
//					}
//				}
//			}
//			
//			buffer.flip();
			
			IntBuffer w = BufferUtils.createIntBuffer(1);
			IntBuffer h = BufferUtils.createIntBuffer(1);
			IntBuffer comp = BufferUtils.createIntBuffer(1);
			
			ByteBuffer data = STBImage.stbi_load("./res/textures/" + fileNames[i], w, h, comp, STBImage.STBI_rgb_alpha);
			
			width[i] = w.get();
			height[i] = h.get();
			
			if(data == null){
				NeonEngine.throwError("Error: unable to read " + fileNames[i] + " " + STBImage.stbi_failure_reason());
			}
			
			if(NeonEngine.getTextureQuality() >= 1){
				int widthSave = width[i]/2;
				int heightSave = height[i]/2;
				
				if(widthSave <= 0){
					widthSave = 1;
				}
				if(heightSave <= 0){
					heightSave = 1;
				}
				
			    ByteBuffer tmp = MemoryUtil.memAlloc(widthSave * heightSave * STBImage.STBI_rgb_alpha);
				
			    STBImageResize.stbir_resize_uint8(data, width[i], height[i], 0, tmp, widthSave, heightSave, 0, STBImage.STBI_rgb_alpha);
			    
			    STBImage.stbi_image_free(data);
			    
			    width[i] = widthSave;
			    height[i] = heightSave;
			    
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
					
				    ByteBuffer tmp2 = MemoryUtil.memAlloc(widthSave2 * heightSave2 * STBImage.STBI_rgb_alpha);
					
				    STBImageResize.stbir_resize_uint8(tmp, widthSave, heightSave, 0, tmp2, widthSave2, heightSave2, 0, STBImage.STBI_rgb_alpha);
				    
				    MemoryUtil.memFree(tmp);
				    
				    width[i] = widthSave2;
				    height[i] = heightSave2;
				    
				    data = tmp2;
				}
			}
			
			byteBuffer[i] = data;
		}
		
		return byteBuffer;
	}
	
	public void generateMipmap(){
		resource.generateMipmap();
	}
	
	public static void dispose(){
		for(final CubeMapData data : loadedCubeMaps.values()){
			data.dispose();
		}
	}
}
