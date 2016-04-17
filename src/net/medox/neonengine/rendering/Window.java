package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.core.Util.ImageData;
import net.medox.neonengine.math.Vector2f;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
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
import org.lwjgl.system.MemoryUtil;

public class Window{
	public static final int DONT_CARE = GLFW.GLFW_DONT_CARE;
	
    public static long window;
	private static long cursor;
    
    private static GLFWErrorCallback errorCallback;
    private static GLFWKeyCallback key;
    private static GLFWMouseButtonCallback mouseButton;
    private static GLFWCursorPosCallback mousePos;
	private static GLFWScrollCallback scroll;
	private static GLFWWindowPosCallback pos;
	private static GLFWFramebufferSizeCallback size;
	private static GLFWCharCallback text;
	
	private static String title = "NeonEngine";
	private static int oldXPos;
	private static int oldYPos;
	private static int oldWidth = 854;
	private static int oldHeight = 480;
	
	private static int xPos;
	private static int yPos;
	private static int width = 854;
	private static int height = 480;
	
	private static int minWidth = GLFW.GLFW_DONT_CARE;
	private static int minHeight = GLFW.GLFW_DONT_CARE;
	private static int maxWidth = GLFW.GLFW_DONT_CARE;
	private static int maxHeight = GLFW.GLFW_DONT_CARE;
	
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
		
		if(GLFW.glfwInit() != GLFW.GLFW_TRUE){
            throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
//		if(CoreEngine.OPTION_ENABLE_VSYNC == 1){
//			GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).refreshRate());
//		}else{
//			GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, GLFW.GLFW_DONT_CARE);
//		}
		
		initContext();
        
        setResizable(startResizable);
        
		if(isFullscreen){
			final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			window = GLFW.glfwCreateWindow(vidMode.width(), vidMode.height(), title, GLFW.glfwGetPrimaryMonitor(), MemoryUtil.NULL);
			
			width = vidMode.width();
			height = vidMode.height();
			
			oldXPos = (vidMode.width() - oldWidth) / 2;
			oldYPos = (vidMode.height() - oldHeight) / 2;
			oldWidth = 854;
			oldHeight = 480;
			
			gotResized = true;
		}else{
			window = GLFW.glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
		}
		
		if(window == MemoryUtil.NULL){
			GLFW.glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
		}
		
		if(!isFullscreen){
			final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		    
			xPos = (vidMode.width() - width) / 2;
			yPos = (vidMode.height() - height) / 2;
	        
			GLFW.glfwSetWindowPos(window, xPos, yPos);
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		
		if(startIcon16.equals("") || startIcon32.equals("")/* || startIcon128.equals("")*/){
			setIcon(Util.getDefaultIcon16(), Util.getDefaultIcon32()/*, Util.getDefaultIcon128()*/);
		}else{
			setIcon(startIcon16, startIcon32/*, startIcon128*/);
		}
		
		if(!startCursor.equals("")){
			setCursor(startCursor, startCursorX, startCursorY);
		}
		
		GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
		
		GLFW.glfwMakeContextCurrent(window);
		
		if(CoreEngine.OPTION_ENABLE_VSYNC == 1){
			GLFW.glfwSwapInterval(1);
		}else{
			GLFW.glfwSwapInterval(0);
		}
		
		key = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods){
				Input.key(key, scancode, action, mods);
			}
		}.set(window);
		
		mouseButton = new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods){
				Input.mouseButton(button, action, mods);
			}
		}.set(window);
		
		mousePos = new GLFWCursorPosCallback(){
			@Override
			public void invoke(long window, double xpos, double ypos){
				Input.mousePos(xpos, ypos);
			}
		}.set(window);
		
		scroll = new GLFWScrollCallback(){
			@Override
			public void invoke(long window, double xoffset, double yoffset){
				Input.scroll(/*xoffset, */yoffset);
			}
		}.set(window);
		
		pos = new GLFWWindowPosCallback(){
			@Override
			public void invoke(long window, int xpos, int ypos){
				Window.xPos = xpos;
				Window.yPos = ypos;
			}
		}.set(window);
		
		size = new GLFWFramebufferSizeCallback(){
			@Override
			public void invoke(long window, int width, int height){
				gotResized = true;
				
				Window.width = width;
				Window.height = height;
			}
		}.set(window);
		
		text = new GLFWCharCallback(){
			@Override
			public void invoke(long window, int codepoint){
				Input.chars(codepoint);
			}
		}.set(window);
		
		GLFW.glfwShowWindow(window);
		
		createContext();
		
		gotCreated = true;
	}
	
	private static void initContext(){
//		System.out.println(GL.getCapabilities().OpenGL33);
		if(CoreEngine.OPTION_FORCE_RENDERING_MODE == -1){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW.GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				initContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				initContextGL();
			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.OPENGL){
//			if(1 == 1){
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				initContextGL();
//			/*}else */if(GLFWVulkan.glfwVulkanSupported() == GLFW_TRUE){
//				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
//				initContextVK();
//			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.VULKAN){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW.GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				initContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				initContextGL();
			}
		}
	}
	
	private static void initContextGL(){
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
	}
	
	private static void initContextVK(){
		
	}
	
	private static void createContext(){
//		System.out.println(GL.getCapabilities().OpenGL33);
		if(CoreEngine.OPTION_FORCE_RENDERING_MODE == -1){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW.GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				createContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createContextGL();
			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.OPENGL){
//			if(1 == 1){
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createContextGL();
//			/*}else */if(GLFWVulkan.glfwVulkanSupported() == GLFW_TRUE){
//				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
//				createContextVK();
//			}
		}else if(CoreEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.VULKAN){
			if(GLFWVulkan.glfwVulkanSupported() == GLFW.GLFW_TRUE){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				createContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createContextGL();
			}
		}
	}
	
	private static void createContextGL(){
		GL.createCapabilities();
	}
	
	private static void createContextVK(){
		
	}
	
	public static void dispose(){
		GLFW.glfwDestroyWindow(window);
	    key.free();
	    mouseButton.free();
		mousePos.free();
		scroll.free();
		pos.free();
		size.free();
		text.free();
		
		if(cursor != MemoryUtil.NULL){
			GLFW.glfwDestroyCursor(cursor);
		}
        
		GLFW.glfwTerminate();
        errorCallback.free();
	}
	
	public static void takeScreenshot(){
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			takeScreenshotGL();
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			takeScreenshotVK();
		}
	}
	
	private static void takeScreenshotGL(){
		GL11.glReadBuffer(GL11.GL_FRONT);
	    final int width = getWidth();
	    final int height = getHeight();
	    final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * /*bpp*/4);
	    GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	    
	    new ScreenshotSaver("screenshots/", buffer, width, height).start();
	}
	
	private static void takeScreenshotVK(){
		
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
		final GLFWImage.Buffer icons = GLFWImage.malloc(/*3*/2);
		
		final ImageData pixels16 = Util.imageToByteBuffer(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = Util.imageToByteBuffer(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		
//		final ImageData pixels128 = Util.ioImageResourceToByteBuffer(icon128);
//		icons.position(2).width(pixels128.width).height(pixels128.height).pixels(pixels128.data);
	    
	    icons.position(0);
		
	    GLFW.glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	private static void setIcon(BufferedImage icon16, BufferedImage icon32/*, BufferedImage icon128*/){
		final GLFWImage.Buffer icons = GLFWImage.malloc(/*3*/2);
		
		final ImageData pixels16 = Util.bufferedImageToByteBuffer(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = Util.bufferedImageToByteBuffer(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		
//	  	final ImageData pixels128 = Util.bufferedImageToByteBuffer(icon128);
//		icons.position(2).width(pixels128.width).height(pixels128.height).pixels(pixels128.data);
		
	    icons.position(0);
		
	    GLFW.glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	public static void setCursor(String fileName, int xPos, int yPos){
		if(fileName.equals("")){
			if(cursor != MemoryUtil.NULL){
				GLFW.glfwSetCursor(window, MemoryUtil.NULL);
				
				GLFW.glfwDestroyCursor(cursor);
				
				cursor = MemoryUtil.NULL;
			}
		}else{
			if(cursor != MemoryUtil.NULL){
				GLFW.glfwDestroyCursor(cursor);
			}
			
			final ImageData pixels = Util.imageToByteBuffer("./res/textures/" + fileName);
			final GLFWImage img = GLFWImage.malloc().set(pixels.width, pixels.height, pixels.data);
			
			cursor = GLFW.glfwCreateCursor(img, -xPos, yPos);
			img.free();
			
			if(cursor == MemoryUtil.NULL){
	            throw new RuntimeException("Failed to create the GLFW cursor");
			}
			
			GLFW.glfwSetCursor(window, cursor);
		}
	}
	
	public static void render(){
		gotResized = false;
		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}
	
	public static boolean isCloseRequested(){
		return GLFW.glfwWindowShouldClose(window) == GLFW.GLFW_TRUE;
	}
	
	public static void setTitle(String title){
		Window.title = title;
		
		GLFW.glfwSetWindowTitle(window, title);
	}
	
	public static void setResizable(boolean value){
		if(value){
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		}else{
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		}
	}
	
	public static void setFullscreen(boolean value){
		if(value){
			if(!isFullscreen){
				oldXPos = xPos;
				oldYPos = yPos;
				oldWidth = width;
				oldHeight = height;
				
				final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
				GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
				
				if(CoreEngine.OPTION_ENABLE_VSYNC == 1){
					GLFW.glfwSwapInterval(1);
				}else{
					GLFW.glfwSwapInterval(0);
				}
				
				isFullscreen = true;
			}
		}else{
			if(isFullscreen){
				xPos = oldXPos;
				yPos = oldYPos;
				width = oldWidth;
				height = oldHeight;
				
				GLFW.glfwSetWindowMonitor(window, MemoryUtil.NULL, oldXPos, oldYPos, oldWidth, oldHeight, 0);
				
				if(CoreEngine.OPTION_ENABLE_VSYNC == 1){
					GLFW.glfwSwapInterval(1);
				}else{
					GLFW.glfwSwapInterval(0);
				}
				
				isFullscreen = false;
			}
		}
	}
	
	public static void setMinSizeLimit(int width, int height){
		minWidth = width;
		minHeight = height;
		GLFW.glfwSetWindowSizeLimits(window, width, height, maxWidth, maxHeight);
	}
	
	public static void setMaxSizeLimit(int width, int height){
		maxWidth = width;
		maxHeight = height;
		GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, width, height);
	}
	
	public static void setSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight){
		Window.minWidth = minWidth;
		Window.minHeight = minHeight;
		Window.maxWidth = maxWidth;
		Window.maxHeight = maxHeight;
		GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
	}
	
    public static void bindAsRenderTarget(){
    	if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
    		bindAsRenderTargetGL();
    	}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
    		bindAsRenderTargetVK();
    	}
    }
    
    private static void bindAsRenderTargetGL(){
    	GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    	ARBFramebufferObject.glBindFramebuffer(ARBFramebufferObject.GL_FRAMEBUFFER, 0);
    	if(CoreEngine.PROFILING_SET_1x1_VIEWPORT == 0){
    		GL11.glViewport(0, 0, getWidth(), getHeight());
    	}else{
    		GL11.glViewport(0, 0, 1, 1);
    	}
    }
    
    private static void bindAsRenderTargetVK(){
    	
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
		return GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_RESIZABLE) == 1;
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
