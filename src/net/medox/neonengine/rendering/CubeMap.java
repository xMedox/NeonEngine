package net.medox.neonengine.rendering;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.rendering.resourceManagement.CubeMapData;

public class CubeMap{
	private static final Map<String, CubeMapData> loadedCubeMaps = new ConcurrentHashMap<String, CubeMapData>();
	
	private final String fileNames[];
	
	private CubeMapData resource;
	
	private int width[] = new int[6];
	private int height[] = new int[6];
	
	public CubeMap(String[] fileNames, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
		this.fileNames = fileNames;
		resource = loadedCubeMaps.get(fileNames[0] + " " + fileNames[1] + " " + fileNames[2] + " " + fileNames[3] + " " + fileNames[4] + " " + fileNames[5]);
		
		if(resource == null){
			final ByteBuffer[] textures = loadTexture(fileNames);
			
			resource = new CubeMapData(textureTarget, width, height, textures, filter, internalFormat, format, type, clamp, attachment);
			loadedCubeMaps.put(fileNames[0] + " " + fileNames[1] + " " + fileNames[2] + " " + fileNames[3] + " " + fileNames[4] + " " + fileNames[5], resource);
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
	
//	public CubeMap(int width, int height, ByteBuffer data, int textureTarget, int filter, int internalFormat, int format, int type, boolean clamp, int attachment){
//		fileNames = new String[]{"", "", "", "", "", ""};
//		resource = new CubeMapData(textureTarget, new int[]{width, width, width, width, width, width}, new int[]{height, height, height, height, height, height}, new ByteBuffer[]{data, data, data, data, data, data}, filter, internalFormat, format, type, clamp, attachment);
//	}
	
	@Override
	protected void finalize() throws Throwable{
		if(resource.removeReference() && !fileNames[0].equals("") && !fileNames[1].equals("") && !fileNames[2].equals("") && !fileNames[3].equals("") && !fileNames[4].equals("") && !fileNames[5].equals("")){
			resource.dispose();
			loadedCubeMaps.remove(fileNames);
		}
		
		super.finalize();
	}
	
	public void bind(int samplerSlot){
		resource.bind(samplerSlot);
	}
	
	public void bindAsRenderTarget(int face){
		resource.bindAsRenderTarget(face);
	}
	
	public int[] getWidth(){
		return resource.getWidth();
	}
	
	public int[] getHeight(){
		return resource.getHeight();
	}
	
	private ByteBuffer[] loadTexture(String[] fileNames/*, boolean flipped*/){
		ByteBuffer[] byteBuffer = new ByteBuffer[6];
		
		for(int i = 0; i < fileNames.length; i++){
			BufferedImage image = null;
				
//			if(flipped){
//				image = flipBufferedImageVertical(ImageIO.read(new File("./res/textures/" + fileName)));
//			}else{
				try{
					image = ImageIO.read(new File("./res/textures/" + fileNames[i]));
				}catch(Exception e){
					e.printStackTrace();
					System.exit(1);
				}
//			}
			
			if(NeonEngine.OPTION_TEXTURE_QUALITY >= 1){
				BufferedImage before = image;
				int w = before.getWidth();
				int h = before.getHeight();
				BufferedImage after = new BufferedImage(w/2, h/2, BufferedImage.TYPE_INT_ARGB);
				AffineTransform at = new AffineTransform();
				at.scale(0.5, 0.5);
				AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
				after = scaleOp.filter(before, after);
				
				image = after;
				
				if(NeonEngine.OPTION_TEXTURE_QUALITY >= 2){
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
			
			width[i] = image.getWidth();
			height[i] = image.getHeight();
			
			final int[] pixels = image.getRGB(0, 0, width[i], height[i], null, 0, width[i]);
			
			final ByteBuffer buffer = DataUtil.createByteBuffer(height[i] * width[i] * 4);
			final boolean hasAlpha = image.getColorModel().hasAlpha();
			
			for(int y = 0; y < height[i]; y++){
				for(int x = 0; x < width[i]; x++){
					final int pixel = pixels[y * width[i] + x];
					
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
			
			byteBuffer[i] = buffer;
		}
		
		return byteBuffer;
	}
	
	public static void dispose(){
		for(final CubeMapData data : loadedCubeMaps.values()){
			data.dispose();
		}
	}
}
