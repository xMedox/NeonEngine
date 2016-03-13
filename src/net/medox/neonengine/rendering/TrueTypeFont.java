package net.medox.neonengine.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.awt.GraphicsEnvironment;
 
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
 
/**
 * A TrueType font implementation originally for Slick, edited for Bobjob's Engine
 * 
 * @original author James Chambers (Jimmy)
 * @original author Jeremy Adams (elias4444)
 * @original author Kevin Glass (kevglass)
 * @original author Peter Korzuszek (genail)
 * 
 * @new version edited by David Aaron Muhar (bobjob)
 */
public class TrueTypeFont{
    public final static int
        ALIGN_LEFT = 0,
        ALIGN_RIGHT = 1,
        ALIGN_CENTER = 2;
    /** Array that holds necessary information about the font characters */
    private IntObject[] charArray = new IntObject[256];
     
    /** Map of user defined font characters (Character <-> IntObject) */
    @SuppressWarnings("rawtypes")
	private Map customChars = new HashMap();
 
    /** Boolean flag on whether AntiAliasing is enabled or not */
    private boolean antiAlias;
 
    /** Font's size */
    private int fontSize = 0;
 
    /** Font's height */
    private int fontHeight = 0;
 
    /** Texture used to cache the font 0-255 characters */
    private Texture fontTexture;
     
    /** Default font texture width */
    private int textureWidth = 512;
 
    /** Default font texture height */
    private int textureHeight = 512;
 
    /** A reference to Java's AWT Font that we create our font texture from */
    private Font font;
 
    /** The font metrics for our Java AWT font */
    private FontMetrics fontMetrics;
 
     
    private int correctL = 9, correctR = 8;
     
    private class IntObject {
        /** Character's width */
        public int width;
 
        /** Character's height */
        public int height;
 
        /** Character's stored x position */
        public int storedX;
 
        /** Character's stored y position */
        public int storedY;
    }
 
 
    public TrueTypeFont(Font font, boolean antiAlias, char[] additionalChars) {
        this.font = font;
        this.fontSize = font.getSize()+3;
        this.antiAlias = antiAlias;
 
        createSet( additionalChars );
         
        fontHeight -= 1;
        if (fontHeight <= 0) fontHeight = 1;
    }
 
    public TrueTypeFont(Font font, boolean antiAlias) {
        this( font, antiAlias, null );
    }
    public void setCorrection(boolean on) {
        if (on) {
            correctL = 2;
            correctR = 1;
        } else {
            correctL = 0;
            correctR = 0;
        }
    }
    private BufferedImage getFontImage(char ch) {
        // Create a temporary image to extract the character's size
        BufferedImage tempfontImage = new BufferedImage(1, 1,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) tempfontImage.getGraphics();
        if (antiAlias == true) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        g.setFont(font);
        fontMetrics = g.getFontMetrics();
        int charwidth = fontMetrics.charWidth(ch)+8;
 
        if (charwidth <= 0) {
            charwidth = 7;
        }
        int charheight = fontMetrics.getHeight()+3;
        if (charheight <= 0) {
            charheight = fontSize;
        }
 
        // Create another image holding the character we are creating
        BufferedImage fontImage;
        fontImage = new BufferedImage(charwidth, charheight,
                BufferedImage.TYPE_INT_ARGB);
        Graphics2D gt = (Graphics2D) fontImage.getGraphics();
        if (antiAlias == true) {
            gt.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
        }
        gt.setFont(font);
 
        gt.setColor(Color.WHITE);
        int charx = 3;
        int chary = 1;
        gt.drawString(String.valueOf(ch), (charx), (chary)
                + fontMetrics.getAscent());
 
        return fontImage;
 
    }
 
    @SuppressWarnings("unchecked")
	private void createSet( char[] customCharsArray ) {
        // If there are custom chars then I expand the font texture twice       
        if  (customCharsArray != null && customCharsArray.length > 0) {
            textureWidth *= 2;
        }
         
        // In any case this should be done in other way. Texture with size 512x512
        // can maintain only 256 characters with resolution of 32x32. The texture
        // size should be calculated dynamicaly by looking at character sizes. 
         
        try {
             
            BufferedImage imgTemp = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = (Graphics2D) imgTemp.getGraphics();
 
            g.setColor(new Color(0,0,0,1));
            g.fillRect(0,0,textureWidth,textureHeight);
             
            int rowHeight = 0;
            int positionX = 0;
            int positionY = 0;
             
            int customCharsLength = ( customCharsArray != null ) ? customCharsArray.length : 0; 
 
            for (int i = 0; i < 256 + customCharsLength; i++) {
                 
                // get 0-255 characters and then custom characters
                char ch = ( i < 256 ) ? (char) i : customCharsArray[i-256];
                 
                BufferedImage fontImage = getFontImage(ch);
 
                IntObject newIntObject = new IntObject();
 
                newIntObject.width = fontImage.getWidth();
                newIntObject.height = fontImage.getHeight();
 
                if (positionX + newIntObject.width >= textureWidth) {
                    positionX = 0;
                    positionY += rowHeight;
                    rowHeight = 0;
                }
 
                newIntObject.storedX = positionX;
                newIntObject.storedY = positionY;
 
                if (newIntObject.height > fontHeight) {
                    fontHeight = newIntObject.height;
                }
 
                if (newIntObject.height > rowHeight) {
                    rowHeight = newIntObject.height;
                }
 
                // Draw it here
                g.drawImage(fontImage, positionX, positionY, null);
 
                positionX += newIntObject.width;
 
                if( i < 256 ) { // standard characters
                    charArray[i] = newIntObject;
                } else { // custom characters
                    customChars.put( new Character( ch ), newIntObject );
                }
 
                fontImage = null;
            }
            
            fontTexture = new Texture(imgTemp);
             
 
 
                    //.getTexture(font.toString(), imgTemp);
 
        }catch(Exception e){
            System.err.println("Failed to create font.");
            e.printStackTrace();
        }
    }
     
    private void drawQuad(float drawX, float drawY, float drawX2, float drawY2,
            float srcX, float srcY, float srcX2, float srcY2, Vector3f color) {
        float DrawWidth = drawX2 - drawX;
        float DrawHeight = drawY2 - drawY;
        float TextureSrcX = srcX / textureWidth;
        float TextureSrcY = srcY / textureHeight;
        float SrcWidth = srcX2 - srcX;
        float SrcHeight = srcY2 - srcY;
        float RenderWidth = (SrcWidth / textureWidth);
        float RenderHeight = (SrcHeight / textureHeight);
        
//		shader.bind();
		
//		RenderingEngine.setVector2f("xMax", new Vector2f(-RenderWidth, TextureSrcX));
//		RenderingEngine.setVector2f("yMax", new Vector2f(-RenderHeight, TextureSrcY));
		
//		System.out.println((drawX+DrawWidth/2) + "|" + (drawY+DrawHeight/2) + "|" + -DrawWidth + "|" + DrawHeight);
		
//		t.setPos(new Vector3f((int)(drawX-DrawWidth/2), (int)(drawY+DrawHeight/2), 0.5f));
		//t.setPos(new Vector3f((int)(drawX+DrawWidth), (int)(drawY), 0.5f));
//		t.setPos(new Vector3f((int)0, (int)0, 0.5f));
		//t.setScale(new Vector3f((int)-DrawWidth, (int)DrawHeight, 1));
		
		Transform2D t2 = new Transform2D();
		
		t2.setPos(new Vector2f((drawX+DrawWidth), (drawY)));
		t2.setScale(new Vector2f(-DrawWidth, DrawHeight));
		
//		RenderingEngine.add2DMesh(t2, fontTexture, new Vector2f(TextureSrcX, TextureSrcY), new Vector2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight));
		RenderingEngine.add2DMesh(t2, fontTexture, color, new Vector2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight), new Vector2f(TextureSrcX, TextureSrcY));

		
//		m.setTexture("diffuse", fontTexture);
//		
//		shader.updateUniforms(t, m, camera);
//		
//		ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
//		
//		texCoords.add(new Vector2f(TextureSrcX, TextureSrcY + RenderHeight));
//		texCoords.add(new Vector2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight));
//		texCoords.add(new Vector2f(TextureSrcX, TextureSrcY));
//		texCoords.add(new Vector2f(TextureSrcX + RenderWidth, TextureSrcY));
		
//		System.out.println(TextureSrcX + "|" + TextureSrcY + "|" + RenderWidth + "|" + RenderHeight);
		
//		mesh.lel(texCoords);
		
//		createSet(null);
		
//		mesh.draw();
    }
 
    public int getWidth(String whatchars) {
        int totalwidth = 0;
        IntObject intObject = null;
        int currentChar = 0;
        for (int i = 0; i < whatchars.length(); i++) {
            currentChar = whatchars.charAt(i);
            if (currentChar < 256) {
                intObject = charArray[currentChar];
            } else {
                intObject = (IntObject)customChars.get( new Character( (char) currentChar ) );
            }
             
            if( intObject != null )
                totalwidth += intObject.width;
        }
        return totalwidth;
    }
 
    public int getHeight(){
        return fontHeight;
    }
 
 
    public int getHeight(String HeightString){
        return fontHeight;
    }
 
    public int getLineHeight(){
        return fontHeight;
    }
 
    public void drawString(float x, float y,
            String whatchars, float scaleX, float scaleY){
        drawString(x,y,whatchars, 0, whatchars.length()-1, scaleX, scaleY, new Vector3f(1, 1, 1), ALIGN_LEFT);
    }
    
    public void drawString(float x, float y,
            String whatchars, float scaleX, float scaleY, Vector3f color){
        drawString(x,y,whatchars, 0, whatchars.length()-1, scaleX, scaleY, color, ALIGN_LEFT);
    }
    
    public void drawString(float x, float y,
            String whatchars, float scaleX, float scaleY, Vector3f color, int format){
        drawString(x,y,whatchars, 0, whatchars.length()-1, scaleX, scaleY, color, format);
    }
 
 
    public void drawString(float x, float y,
            String whatchars, int startIndex, int endIndex,
            float scaleX, float scaleY, Vector3f color,
            int format
            ){
        IntObject intObject = null;
        int charCurrent;
         
 
        int totalwidth = 0;
        int i = startIndex, d, c;
        float startY = 0;
 
 
         
        switch (format) {
            case ALIGN_RIGHT: {
                d = -1;
                c = correctR;
             
                while (i < endIndex) {
                    if (whatchars.charAt(i) == '\n') startY -= fontHeight;
                    i++;
                }
                break;
            }
            case ALIGN_CENTER: {
                for (int l = startIndex; l <= endIndex; l++) {
                    charCurrent = whatchars.charAt(l);
                    if (charCurrent == '\n') break;
                    if (charCurrent < 256) {
                        intObject = charArray[charCurrent];
                    } else {
                        intObject = (IntObject)customChars.get( new Character( (char) charCurrent ) );
                    }
                    totalwidth += intObject.width-correctL;
                }
                totalwidth /= -2;
            }
            case ALIGN_LEFT:
            default: {
                d = 1;
                c = correctL;
                break;
            }
         
        }
         
        while (i >= startIndex && i <= endIndex) {
             
            charCurrent = whatchars.charAt(i);
            if (charCurrent < 256) {
                intObject = charArray[charCurrent];
            } else {
                intObject = (IntObject)customChars.get( new Character( (char) charCurrent ) );
            } 
             
            if( intObject != null ) {
                if (d < 0) totalwidth += (intObject.width-c) * d;
                    if (charCurrent == '\n') {
                        startY -= fontHeight * d;
                        totalwidth = 0;
                        if (format == ALIGN_CENTER) {
                            for (int l = i+1; l <= endIndex; l++) {
                                charCurrent = whatchars.charAt(l);
                                if (charCurrent == '\n') break;
                                if (charCurrent < 256) {
                                    intObject = charArray[charCurrent];
                                } else {
                                    intObject = (IntObject)customChars.get( new Character( (char) charCurrent ) );
                                }
                                totalwidth += intObject.width-correctL;
                            }
                            totalwidth /= -2;
                        }
                        //if center get next lines total width/2;
                    }
                    else {
                        drawQuad((totalwidth + intObject.width) * scaleX + x, startY * scaleY + y,
                            totalwidth * scaleX + x,
                            (startY + intObject.height) * scaleY + y, intObject.storedX + intObject.width,
                            intObject.storedY + intObject.height,intObject.storedX, 
                            intObject.storedY, color);
                        if (d > 0) totalwidth += (intObject.width-c) * d + 1; //TODO remove +1?
                    }
                    i += d;
                 
            }
        }
        
//		RenderingEngine.setVector2f("xMax", new Vector2f(1f, 0f));
//		RenderingEngine.setVector2f("yMax", new Vector2f(1f, 0f));
        
//        ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
//		
//		texCoords.add(new Vector2f(1f, 0f));
//		texCoords.add(new Vector2f(0f, 0f));
//		texCoords.add(new Vector2f(1f, 1f));
//		texCoords.add(new Vector2f(0f, 1f));
//		
//		mesh.lel(texCoords);
		
//       shader.bind();
//		
//		t.setPos(new Vector3f((int)250, (int)250, 0.5f));
////	        t.setPos(new Vector3f((int)100, (int)100, 0.5f));
//		t.setScale(new Vector3f((int)500, (int)500, 1));
//		
//		m.setTexture("diffuse", fontTexture);
//		
////			ArrayList<Vector2f> texCoords = new ArrayList<Vector2f>();
////			
////			texCoords.add(new Vector2f(TextureSrcX, TextureSrcY + RenderHeight));
////			texCoords.add(new Vector2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight));
////			texCoords.add(new Vector2f(TextureSrcX, TextureSrcY));
////			texCoords.add(new Vector2f(TextureSrcX + RenderWidth, TextureSrcY));
//		
////			Vector2f[] t12 = new Vector2f[]{new Vector2f(TextureSrcX, TextureSrcY + RenderHeight),
////					   new Vector2f(TextureSrcX + RenderWidth, TextureSrcY + RenderHeight),
////					   new Vector2f(TextureSrcX, TextureSrcY),
////					   new Vector2f(TextureSrcX + RenderWidth, TextureSrcY)};
//		
////			mesh.lel(texCoords);
//		
//		shader.updateUniforms(t, m, camera);
//		mesh.draw();
    }
    
    public static boolean isSupported(String fontname) {
        Font font[] = getFonts();
        for (int i = font.length-1; i >= 0; i--) {
            if (font[i].getName().equalsIgnoreCase(fontname))
                return true;
        }
        return false;
    }
    
    public static Font[] getFonts() {
        return GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
    }
    
    public static byte[] intToByteArray(int value) {
        return new byte[] {
                (byte)(value >>> 24),
                (byte)(value >>> 16),
                (byte)(value >>> 8),
                (byte)value};
    }
    
//    public void destroy() {
//        IntBuffer scratch = BufferUtils.createIntBuffer(1);
//        scratch.put(0, fontTextureID);
//        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
//        GL11.glDeleteTextures(scratch);
//    }
}
