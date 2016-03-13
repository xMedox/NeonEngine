package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.core.Util.ImageData;
import net.medox.neonengine.math.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCharCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.glfw.GLFWVulkan;
import org.lwjgl.glfw.GLFWWindowPosCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window{
	public static final int DONT_CARE = GLFW_DONT_CARE;
	
    public static long window;
    
    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback key;
    private static GLFWMouseButtonCallback mouseButton;
    private static GLFWCursorPosCallback mousePos;
	private static GLFWScrollCallback scroll;
	private static GLFWWindowPosCallback pos;
	private static GLFWFramebufferSizeCallback size;
	private static GLFWCharCallback text;
	
	private static long cursor;
	
	private static String title = "NeonEngine";
	private static int xPos;
	private static int yPos;
	private static int width = 854;
	private static int height = 480;
	
	private static int minWidth = DONT_CARE;
	private static int minHeight = DONT_CARE;
	private static int maxWidth = DONT_CARE;
	private static int maxHeight = DONT_CARE;
	
	private static boolean isFullscreen;
	private static boolean gotResized;
	
	private static boolean startResizable;
	
	private static String startIcon16 = "";
	private static String startIcon32 = "";
//	private static String startIcon128 = "";
	
	private static String startCursor = "";
	private static int startCursorX;
	private static int startCursorY;
	
	private static boolean gotCreated;
	
	public static void createWindow(){
		errorCallback = GLFWErrorCallback.createPrint().set();
		
		if(glfwInit() != GLFW_TRUE){
            throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
        setResizable(startResizable);
        
		if(!isFullscreen){
			window = glfwCreateWindow(width, height, title, NULL, NULL);
		}else{
			final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			
			window = glfwCreateWindow(vidMode.width(), vidMode.height(), title, glfwGetPrimaryMonitor(), NULL);
			
			Window.width = vidMode.width();
			Window.height = vidMode.height();
			
			gotResized = true;
		}
		
		if(window == NULL){
	        glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
		}
		
		if(!isFullscreen){
			final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		    
			Window.xPos = (vidmode.width() - Window.width) / 2;
			Window.yPos = (vidmode.height() - Window.height) / 2;
	        
	        glfwSetWindowPos(window, Window.xPos, Window.yPos);
		}
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		
		if(!startIcon16.equals("") && !startIcon32.equals("")/* && !startIcon128.equals("")*/){
			setIcon(startIcon16, startIcon32/*, startIcon128*/);
		}else{
			setIcon(Util.getDefaultIcon16(), Util.getDefaultIcon32()/*, Util.getDefaultIcon128()*/);
		}
		
		if(!startCursor.equals("")){
			setCursor(startCursor, startCursorX, startCursorY);
		}
		
		glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
		
		glfwMakeContextCurrent(window);
		
		if(CoreEngine.OPTION_ENABLE_VSYNC == 1){
			glfwSwapInterval(1);
		}else{
			glfwSwapInterval(0);
		}
		
//		glfwSetKeyCallback(window, key = new GLFWKeyCallback(){
		key = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods){
				Input.key(key, scancode, action, mods);
			}
//		});
		}.set(window);
		
//		glfwSetMouseButtonCallback(window, mouseButton = new GLFWMouseButtonCallback(){
		mouseButton = new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods){
				Input.mouseButton(button, action, mods);
			}
//		});
		}.set(window);
		
//		glfwSetCursorPosCallback(window, mousePos = new GLFWCursorPosCallback(){
		mousePos = new GLFWCursorPosCallback(){
			@Override
			public void invoke(long window, double xpos, double ypos){
				Input.mousePos(xpos, ypos);
			}
//		});
		}.set(window);
		
//		glfwSetScrollCallback(window, scroll = new GLFWScrollCallback(){
		scroll = new GLFWScrollCallback(){
			@Override
			public void invoke(long window, double xoffset, double yoffset){
				Input.scroll(/*xoffset, */yoffset);
			}
//		});
		}.set(window);
		
//		glfwSetWindowPosCallback(window, pos = new GLFWWindowPosCallback(){
		pos = new GLFWWindowPosCallback(){
			@Override
			public void invoke(long window, int xpos, int ypos){
				Window.xPos = xpos;
				Window.yPos = ypos;
			}
//		});
		}.set(window);
		
//		glfwSetFramebufferSizeCallback(window, size = new GLFWFramebufferSizeCallback(){
		size = new GLFWFramebufferSizeCallback(){
			@Override
			public void invoke(long window, int width, int height){
				Window.gotResized = true;
				
				Window.width = width;
				Window.height = height;
			}
//		});
		}.set(window);
		
//		glfwSetCharCallback(window, text = new GLFWCharCallback(){
		text = new GLFWCharCallback(){
			@Override
			public void invoke(long window, int codepoint){
				Input.chars(codepoint);
			}
//		});
		}.set(window);
		
		glfwShowWindow(window);
		
		createContext();
		
//		Window.gotResized = true;
		
		gotCreated = true;
	}
	
	private static void createContext(){
//		System.out.println(GL.getCapabilities().OpenGL33);
		if(CoreEngine.OPTION_FORCE_RENDERING_MODE == -1){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				createWindowVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createWindowGL();
			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.OPENGL){
//			if(1 == 1){
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createWindowGL();
//			/*}else */if(GLFWVulkan.glfwVulkanSupported() == GLFW_TRUE){
//				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
//				createWindowVK();
//			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.VULKAN){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				createWindowVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createWindowGL();
			}
		}
	}
	
	private static void createWindowGL(){
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		
		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
		
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
		GL.createCapabilities();
	}
	
	private static void createWindowVK(){
		
	}
	
	public static void dispose(){
		glfwDestroyWindow(window);
	    key.free();
	    mouseButton.free();
		mousePos.free();
		scroll.free();
		pos.free();
		size.free();
		text.free();
		
		if(cursor != NULL){
			glfwDestroyCursor(cursor);
		}
        
        glfwTerminate();
        errorCallback.free();
	}
	
	public static void takeScreenshot(){
	    GL11.glReadBuffer(GL11.GL_FRONT);
	    final int width = getWidth();
	    final int height = getHeight();
//	    final int bpp = 4;
	    final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * /*bpp*/4);
	    GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	    
//	    GL11.glFlush();
		
//	    ScreenshotSaver screen = new ScreenshotSaver("screenshots/", buffer, width, height);
//		
//		screen.start();
	    new ScreenshotSaver("screenshots/", buffer, width, height).start();
		
//		screen = null;
	}
	
	public static void setStartTitle(String title){
		if(!gotCreated){
			Window.title = title;
		}
	}
	
	public static void setStartDimensions(int width, int height){
		if(!gotCreated){
			Window.width = width;
			Window.height = height;
		}
	}
	
	public static void setStartFullscreen(boolean value){
		if(!gotCreated){
			isFullscreen = value;
		}
	}
	
	public static void setStartResizable(boolean value){
		startResizable = value;
	}
	
	public static void setStartIcon(String icon16, String icon32/*, String icon128*/){
		startIcon16 = icon16;
		startIcon32 = icon32;
//		startIcon128 = icon128;
	}
	
	public static void setStartCursor(String fileName, int xPos, int yPos){
		startCursor = fileName;
		startCursorX = xPos;
		startCursorY = yPos;
	}
	
	public static void setStartMinSizeLimit(int width, int height){
		minWidth = width;
		minHeight = height;
	}
	
	public static void setStartMaxSizeLimit(int width, int height){
		maxWidth = width;
		maxHeight = height;
	}
	
	public static void setStartSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight){
		Window.minWidth = minWidth;
		Window.minHeight = minHeight;
		Window.maxWidth = maxWidth;
		Window.maxHeight = maxHeight;
	}
	
	public static void setIcon(String icon16, String icon32/*, String icon128*/){
		GLFWImage.Buffer icons = GLFWImage.malloc(/*3*/2);
		
		final ImageData pixels16 = Util.ioImageResourceToByteBuffer(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = Util.ioImageResourceToByteBuffer(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		
//		final ImageData pixels128 = Util.ioImageResourceToByteBuffer(icon128);
//		icons.position(2).width(pixels128.width).height(pixels128.height).pixels(pixels128.data);
	    
	    icons.position(0);
		
		glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	private static void setIcon(BufferedImage icon16, BufferedImage icon32/*, BufferedImage icon128*/){
		GLFWImage.Buffer icons = GLFWImage.malloc(/*3*/2);
		
		final ImageData pixels16 = Util.bufferedImageToByteBuffer(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = Util.bufferedImageToByteBuffer(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		
//	  	final ImageData pixels128 = Util.bufferedImageToByteBuffer(icon128);
//		icons.position(2).width(pixels128.width).height(pixels128.height).pixels(pixels128.data);
		
	    icons.position(0);
		
		glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	public static void setCursor(String fileName, int xPos, int yPos){
		if(!fileName.equals("")){
			if(cursor != NULL){
				glfwDestroyCursor(cursor);
			}
			
			final ImageData pixels = Util.ioImageResourceToByteBuffer("./res/textures/" + fileName);
	
//			ByteBuffer img = GLFWimage.malloc(pixels.width, pixels.height, pixels.data);
//			
//			cursor = glfwCreateCursor(img, -x, y);
			final GLFWImage img = GLFWImage.malloc().set(pixels.width, pixels.height, pixels.data);
			
			cursor = glfwCreateCursor(img, -xPos, yPos);
			img.free();
			
			if(cursor == NULL){
	            throw new RuntimeException("Failed to create the GLFW cursor");
			}
			
			glfwSetCursor(window, cursor);
		}else{
			if(cursor != NULL){
				glfwSetCursor(window, NULL);
				
				glfwDestroyCursor(cursor);
				
				cursor = NULL;
			}
		}
	}
	
	public static void render(){
		gotResized = false;
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public static boolean isCloseRequested(){
		return glfwWindowShouldClose(window) == GLFW_TRUE;
	}
	
	public static void setTitle(String title){
		Window.title = title;
		
		glfwSetWindowTitle(window, title);
	}
	
	public static void setResizable(boolean value){
		if(value){
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		}else{
			glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
		}
	}
	
	public static void setFullscreen(boolean value){
		if(value){
			if(!isFullscreen){
				
			}
		}else{
			if(isFullscreen){
				
			}
		}
	}
	
	public static void setMinSizeLimit(int width, int height){
		minWidth = width;
		minHeight = height;
		glfwSetWindowSizeLimits(window, width, height, maxWidth, maxHeight);
	}
	
	public static void setMaxSizeLimit(int width, int height){
		maxWidth = width;
		maxHeight = height;
		glfwSetWindowSizeLimits(window, minWidth, minHeight, width, height);
	}
	
	public static void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight){
		Window.minWidth = minWidth;
		Window.minHeight = minHeight;
		Window.maxWidth = maxWidth;
		Window.maxHeight = maxHeight;
		glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
	}
	
    public static void bindAsRenderTarget(){
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
    	if(CoreEngine.PROFILING_SET_1x1_VIEWPORT == 0){
    		GL11.glViewport(0, 0, getWidth(), getHeight());
    	}else{
    		GL11.glViewport(0, 0, 1, 1);
    	}
    }
	
	public static int getX(){
		return xPos;
	}
	
	public static int getY(){
		return yPos;
	}
	
	public static int getWidth(){
		return width;
	}
	
	public static int getHeight(){
		return height;
	}
	
	public static String getTitle(){
		return title;
	}
	
	public static boolean isFullscreen(){
		return isFullscreen;
	}
	
	public static boolean isResizable(){
		return glfwGetWindowAttrib(window, GLFW_RESIZABLE) == 1;
	}
	
	public static boolean gotResized(){
		return gotResized;
	}
	
	public static boolean gotCreated(){
		return gotCreated;
	}
	
	public static Vector2f getCenter(){
		return new Vector2f(width/2, height/2);
	}
}
