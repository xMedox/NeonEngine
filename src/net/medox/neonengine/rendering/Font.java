package net.medox.neonengine.rendering;

import java.awt.Color;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.awt.GraphicsEnvironment;
 
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

public class Font{
	public static final int ALIGN_LEFT = 0, ALIGN_RIGHT = 1, ALIGN_CENTER = 2;
	
	private static final Transform2D transform = new Transform2D();
	
	private final IntObject[] charArray = new IntObject[256];
	private final Map<Character, IntObject> customChars = new ConcurrentHashMap<Character, IntObject>();
	private final boolean antiAlias;
	private final int fontSize;
	
	private int fontHeight;
	
	private java.awt.Font font;
	private FontMetrics fontMetrics;
	private Texture fontTexture;
	
	private int textureWidth = 512;
	private int textureHeight = 512;
	
	public Font(String fileName, int size, boolean antiAlias){
		this(fileName, size, antiAlias, null);
	}
	
	public Font(String fileName, int size, boolean antiAlias, char[] additionalChars){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("ttf")){
			java.awt.Font customFont = null;
			
			try{
				customFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("./res/" + fileName)).deriveFont((float)size);
				final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
				
				ge.registerFont(java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new File("./res/" + fileName)));
			}catch(IOException | FontFormatException e){
	            e.printStackTrace();
	        }
			
			font = customFont;
		}else{
			System.err.println("Error: '" + ext + "' file format not supported for font data.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		this.fontSize = font.getSize()+3;
		this.antiAlias = antiAlias;
		
		createSet(additionalChars);
		
		fontHeight -= 1;
		if(fontHeight <= 0){
			fontHeight = 1;
		}
	}
	
	private BufferedImage getFontImage(char ch){
		final BufferedImage tempfontImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D)tempfontImage.getGraphics();
		
		if(antiAlias){
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		g.setFont(font);
		
		fontMetrics = g.getFontMetrics();
		
		int charwidth = fontMetrics.charWidth(ch) + 8;
		if(charwidth <= 0){
			charwidth = 7;
		}
		
		int charheight = fontMetrics.getHeight() + 3;
		if(charheight <= 0){
			charheight = fontSize;
		}
		
		BufferedImage fontImage;
		fontImage = new BufferedImage(charwidth, charheight, BufferedImage.TYPE_INT_ARGB);
		
		final Graphics2D gt = (Graphics2D)fontImage.getGraphics();
		
		if(antiAlias){
			gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		
		gt.setFont(font);
		gt.setColor(Color.WHITE);
		
		final int charx = 3;
		final int chary = 1;
		gt.drawString(String.valueOf(ch), charx, chary + fontMetrics.getAscent());
		
		return fontImage;
	}
	
	private void createSet(char[] customCharsArray){
		if(customCharsArray != null && customCharsArray.length > 0){
			textureWidth *= 2;
		}
    	
		try{
			final BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
			final Graphics2D g = (Graphics2D)imgTemp.getGraphics();
			
			g.setColor(new Color(0, 0, 0, 1));
			g.fillRect(0, 0, textureWidth, textureHeight);
			
			int rowHeight = 0;
			int positionX = 0;
			int positionY = 0;
			
			final int customCharsLength = (customCharsArray != null) ? customCharsArray.length : 0; 
			
			for(int i = 0; i < 256 + customCharsLength; i++){
				final char ch = i < 256 ? (char)i : customCharsArray[i-256];
				
				BufferedImage fontImage = getFontImage(ch);
				
				final IntObject newIntObject = new IntObject();
				
				newIntObject.width = fontImage.getWidth();
				newIntObject.height = fontImage.getHeight();
				
				if(positionX + newIntObject.width >= textureWidth){
					positionX = 0;
					positionY += rowHeight;
					rowHeight = 0;
				}
				
				newIntObject.storedX = positionX;
				newIntObject.storedY = positionY;
				
				if(newIntObject.height > fontHeight){
					fontHeight = newIntObject.height;
				}
				
				if(newIntObject.height > rowHeight){
					rowHeight = newIntObject.height;
				}
				
				g.drawImage(fontImage, positionX, positionY, null);
				
				positionX += newIntObject.width;
				
				if(i < 256){
					charArray[i] = newIntObject;
				}else{
					customChars.put(new Character(ch), newIntObject);
				}
				
				fontImage = null;
			}
			
			fontTexture = new Texture(imgTemp);
		}catch(Exception e){
			System.err.println("Failed to create font.");
			e.printStackTrace();
		}
	}
	
	private void drawQuad(float drawX, float drawY, float drawX2, float drawY2, float srcX, float srcY, float srcX2, float srcY2, Vector3f color){
		final float drawWidth = drawX2 - drawX;
		final float drawHeight = drawY2 - drawY;
		final float textureSrcX = srcX / textureWidth;
		final float textureSrcY = srcY / textureHeight;
		final float renderWidth = (srcX2 - srcX) / textureWidth;
		final float renderHeight = (srcY2 - srcY) / textureHeight;
		
		transform.setPos(new Vector2f(drawX + drawWidth, drawY));
		transform.setScale(new Vector2f(-drawWidth, drawHeight));
		
		if(RenderingEngine.mesh2DInFrustum(transform)){
			RenderingEngine.add2DMesh(transform, fontTexture, color, new Vector2f(textureSrcX + renderWidth, textureSrcY + renderHeight), new Vector2f(textureSrcX, textureSrcY));
		}
	}
    
	public int getWidth(String text){
		int totalWidth = 0;
		IntObject intObject = null;
		int currentChar = 0;
		
		for(int i = 0; i < text.length(); i++){
			currentChar = text.charAt(i);
			
			if(currentChar < 256){
				intObject = charArray[currentChar];
			}else{
				intObject = (IntObject)customChars.get(new Character((char)currentChar));
			}
			
			if(intObject != null){
				totalWidth += intObject.width;
			}
		}
		return totalWidth;
	}
	
	public int getHeight(){
		return fontHeight;
	}
	
	public void drawString(float x, float y, String text, Vector3f color, float scaleX, float scaleY){
		drawString(x, y, text, 0, text.length()-1, color, scaleX, scaleY, ALIGN_LEFT);
	}
	
	public void drawString(float x, float y, String text, Vector3f color, float scaleX, float scaleY, int format){
		drawString(x, y, text, 0, text.length()-1, color, scaleX, scaleY, format);
	}
	
	public void drawString(float x, float y, String text, int startIndex, int endIndex, Vector3f color, float scaleX, float scaleY, int format){
		IntObject intObject = null;
		int charCurrent;
		
		int totalwidth = 0;
		int i = startIndex;
		int d;
		int c;
		float startY = 0;
		
		switch(format){
			case ALIGN_RIGHT:{
				d = -1;
				c = 8;
				
				while(i < endIndex){
					if(text.charAt(i) == '\n'){
						startY -= fontHeight;
					}
					i++;
				}
				break;
			}
			case ALIGN_CENTER:{
				for(int l = startIndex; l <= endIndex; l++){
					charCurrent = text.charAt(l);
					
					if(charCurrent == '\n'){
						break;
					}
					
					if(charCurrent < 256){
						intObject = charArray[charCurrent];
					}else{
						intObject = (IntObject)customChars.get(new Character((char) charCurrent));
					}
					
					totalwidth += intObject.width-8;
				}
				totalwidth /= -2;
			}
			case ALIGN_LEFT:
				default:{
					d = 1;
					c = 8;
					break;
	            }
		}
		
		while(i >= startIndex && i <= endIndex){
			charCurrent = text.charAt(i);
			if(charCurrent < 256){
				intObject = charArray[charCurrent];
			}else{
				intObject = (IntObject)customChars.get(new Character((char)charCurrent));
			} 
			
			if(intObject != null){
				if(d < 0){
					totalwidth += (intObject.width-c) * d;
				}
				
				if(charCurrent == '\n'){
					startY -= fontHeight * d;
					totalwidth = 0;
					if(format == ALIGN_CENTER){
						for(int l = i+1; l <= endIndex; l++){
							charCurrent = text.charAt(l);
							
							if(charCurrent == '\n'){
								break;
							}
							
							if(charCurrent < 256){
								intObject = charArray[charCurrent];
							}else{
								intObject = (IntObject)customChars.get(new Character((char)charCurrent));
							}
							
							totalwidth += intObject.width-8;
						}
						totalwidth /= -2;
					}
					
				}else{
					drawQuad((totalwidth + intObject.width) * scaleX + x, startY * scaleY + y, totalwidth * scaleX + x, (startY + intObject.height) * scaleY + y, intObject.storedX + intObject.width, intObject.storedY + intObject.height,intObject.storedX, intObject.storedY, color);
					
					if(d > 0){
						totalwidth += (intObject.width-c) * d;
					}
				}
				i += d;
			}
		}
	}
	
	private class IntObject{
		public int width;
		public int height;
		
		public int storedX;
		public int storedY;
	}
}
