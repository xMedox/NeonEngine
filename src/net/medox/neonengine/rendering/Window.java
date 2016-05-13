package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.ImageUtil.ImageData;

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
    
	private static GLFWErrorCallback errorCallback;
	private static GLFWKeyCallback keyCallback;
	private static GLFWMouseButtonCallback mouseButtonCallback;
	private static GLFWCursorPosCallback mousePosCallback;
	private static GLFWScrollCallback scrollCallback;
	private static GLFWWindowPosCallback posCallback;
	private static GLFWFramebufferSizeCallback sizeCallback;
	private static GLFWCharCallback textCallback;
	
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
	
	private static String startCursor = "";
	private static int startCursorX;
	private static int startCursorY;
	
	private static long cursor;
	
	private static int centerPositionWidth;
	private static int centerPositionHeight;
	
	private static boolean gotCreated;
	
	public static void createWindow(){
		errorCallback = GLFWErrorCallback.createPrint().set();
		
		if(!GLFW.glfwInit()){
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
//		if(NeonEngine.OPTION_ENABLE_VSYNC == 1){
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
		
		centerPositionWidth = (int)width/2;
		centerPositionHeight = (int)height/2;
		
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
		
		if(startIcon16.equals("") || startIcon32.equals("")){
			setIcon(ImageUtil.getDefaultIcon16(), ImageUtil.getDefaultIcon32());
		}else{
			setIcon(startIcon16, startIcon32);
		}
		
		if(!startCursor.equals("")){
			setCursor(startCursor, startCursorX, startCursorY);
		}
		
		if(minWidth != GLFW.GLFW_DONT_CARE || minHeight != GLFW.GLFW_DONT_CARE || maxWidth != GLFW.GLFW_DONT_CARE || maxHeight != GLFW.GLFW_DONT_CARE){
			GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
		}
		
		GLFW.glfwMakeContextCurrent(window);
		
		if(NeonEngine.OPTION_ENABLE_VSYNC == 1){
			GLFW.glfwSwapInterval(1);
		}else{
			GLFW.glfwSwapInterval(0);
		}
		
		keyCallback = new GLFWKeyCallback(){
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods){
				Input.key(key, scancode, action, mods);
			}
		}.set(window);
		
		mouseButtonCallback = new GLFWMouseButtonCallback(){
			@Override
			public void invoke(long window, int button, int action, int mods){
				Input.mouseButton(button, action, mods);
			}
		}.set(window);
		
		mousePosCallback = new GLFWCursorPosCallback(){
			@Override
			public void invoke(long window, double xPos, double yPos){
				Input.mousePos(xPos, yPos);
			}
		}.set(window);
		
		scrollCallback = new GLFWScrollCallback(){
			@Override
			public void invoke(long window, double xOffset, double yOffset){
				Input.scroll(yOffset);
			}
		}.set(window);
		
		posCallback = new GLFWWindowPosCallback(){
			@Override
			public void invoke(long window, int xPos, int yPos){
				Window.xPos = xPos;
				Window.yPos = yPos;
			}
		}.set(window);
		
		sizeCallback = new GLFWFramebufferSizeCallback(){
			@Override
			public void invoke(long window, int width, int height){
				gotResized = true;
				
				Window.width = width;
				Window.height = height;
				Window.centerPositionWidth = (int)width/2;
				Window.centerPositionHeight = (int)height/2;
			}
		}.set(window);
		
		textCallback = new GLFWCharCallback(){
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
		if(NeonEngine.OPTION_FORCE_RENDERING_MODE == -1){
			if(GLFWVulkan.glfwVulkanSupported()){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				initContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				initContextGL();
			}
		}else if(NeonEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.OPENGL){
			RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
			initContextGL();
		}else if(NeonEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.VULKAN){
			if(GLFWVulkan.glfwVulkanSupported()){
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
		if(NeonEngine.OPTION_FORCE_RENDERING_MODE == -1){
			if(GLFWVulkan.glfwVulkanSupported()){
				RenderingEngine.changeRenderingMode(RenderingEngine.VULKAN);
				createContextVK();
			}else{
				RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
				createContextGL();
			}
		}else if(NeonEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.OPENGL){
			RenderingEngine.changeRenderingMode(RenderingEngine.OPENGL);
			createContextGL();
		}else if(NeonEngine.OPTION_FORCE_RENDERING_MODE == RenderingEngine.VULKAN){
			if(GLFWVulkan.glfwVulkanSupported()){
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
		keyCallback.free();
		mouseButtonCallback.free();
		mousePosCallback.free();
		scrollCallback.free();
		posCallback.free();
		sizeCallback.free();
		textCallback.free();
		
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
		final ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
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
	
	public static void setStartIcon(String icon16, String icon32){
		startIcon16 = icon16;
		startIcon32 = icon32;
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
	
	public static void setIcon(String icon16, String icon32){
		final GLFWImage.Buffer icons = GLFWImage.malloc(2);
		
		final ImageData pixels16 = ImageUtil.imageToByteBuffer("./res/" + icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = ImageUtil.imageToByteBuffer("./res/" + icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
	    
		icons.position(0);
		
		GLFW.glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	private static void setIcon(BufferedImage icon16, BufferedImage icon32){
		final GLFWImage.Buffer icons = GLFWImage.malloc(2);
		
		final ImageData pixels16 = ImageUtil.bufferedImageToByteBuffer(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = ImageUtil.bufferedImageToByteBuffer(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		
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
			
			final ImageData pixels = ImageUtil.imageToByteBuffer("./res/" + fileName);
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
		return GLFW.glfwWindowShouldClose(window);
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
				
				if(NeonEngine.OPTION_ENABLE_VSYNC == 1){
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
				
				if(NeonEngine.OPTION_ENABLE_VSYNC == 1){
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
		if(NeonEngine.PROFILING_SET_1x1_VIEWPORT == 0){
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
	
	public static Vector2f getCenterPosition(){
		return new Vector2f(centerPositionWidth, centerPositionHeight);
	}
}
