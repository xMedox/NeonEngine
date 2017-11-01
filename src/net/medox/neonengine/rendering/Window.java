package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.ImageUtil.ImageData;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Window{
	public static final int DONT_CARE = GLFW.GLFW_DONT_CARE;
	
	private static String title = "NeonEngine";
	
	private static long window;
	private static Cursor cursor;
	
	private static int oldXPos;
	private static int oldYPos;
	private static int oldWidth = 854;
	private static int oldHeight = 480;
	
	private static int xPos;
	private static int yPos;
	private static int width = 854;
	private static int height = 480;
	
	private static int minWidth = DONT_CARE;
	private static int minHeight = DONT_CARE;
	private static int maxWidth = DONT_CARE;
	private static int maxHeight = DONT_CARE;
	
	private static boolean fullscreen;
	private static boolean gotResized;
	
	private static boolean startResizable;
	
	private static String startIcon16 = "";
	private static String startIcon32 = "";
	
	private static int centerPositionX;
	private static int centerPositionY;
	
	private static boolean startedCreating;
	private static boolean gotCreated;
	
	public static void createWindow(){
		startedCreating = true;
		
		GLFW.glfwSetErrorCallback((error, description) -> NeonEngine.throwErrorWindow("Error: " + GLFWErrorCallback.getDescription(description)));
		
		if(!GLFW.glfwInit()){
			NeonEngine.throwError("Error: Unable to initialize GLFW.");
		}
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
		
//		if(NeonEngine.OPTION_ENABLE_VSYNC == 1){
//			GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()).refreshRate());
//		}else{
//			GLFW.glfwWindowHint(GLFW.GLFW_REFRESH_RATE, GLFW.GLFW_DONT_CARE);
//		}
		
		initContext();
        
		if(startResizable){
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		}else{
			GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		}
        
		if(fullscreen){
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
		
		centerPositionX = (int)width/2;
		centerPositionY = (int)height/2;
		
		if(window == MemoryUtil.NULL){
//			GLFW.glfwTerminate();
			NeonEngine.throwError("Error: Failed to create the GLFW window.");
		}
		
		if(!fullscreen){
			final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		    
			xPos = (vidMode.width() - width) / 2;
			yPos = (vidMode.height() - height) / 2;
	        
			GLFW.glfwSetWindowPos(window, xPos, yPos);
		}
		
		if(startIcon16.equals("") || startIcon32.equals("")){
			setIcon(ImageUtil.valuesToImageData(ImageUtil.getDefaultIconRed(), ImageUtil.getDefaultIconGreen(), ImageUtil.getDefaultIconBlue()), ImageUtil.valuesToImageData(ImageUtil.getDefaultIcon32Red(), ImageUtil.getDefaultIcon32Green(), ImageUtil.getDefaultIcon32Blue()));
		}else{
			setIcon(startIcon16, startIcon32);
		}
		
		if(cursor != null){
			cursor.create();
			
			setCursor(cursor);
		}
		
		GLFW.glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
		
		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> Input.key(key, scancode, action, mods));
		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> Input.mouseButton(button, action, mods));
		GLFW.glfwSetCursorPosCallback(window, (window, xPos, yPos) -> Input.mousePos(xPos, yPos));
		GLFW.glfwSetScrollCallback(window, (window, xOffset, yOffset) -> Input.scroll(yOffset));
		GLFW.glfwSetWindowPosCallback(window, (window, xPos, yPos) -> setLocalPos(xPos, yPos));
		GLFW.glfwSetFramebufferSizeCallback(window, (window, width, height) -> setFramebufferSize(width, height));
		GLFW.glfwSetCharCallback(window, (window, codepoint) -> Input.chars(codepoint));
		GLFW.glfwSetCursorEnterCallback(window, (window, entered) -> Input.enter(entered));
		
		GLFW.glfwMakeContextCurrent(window);
		createContext();
		
		if(NeonEngine.isVSyncEnabled()){
			GLFW.glfwSwapInterval(1);
		}else{
			GLFW.glfwSwapInterval(0);
		}
		
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_TRUE);
		
		GLFW.glfwShowWindow(window);
		
		gotCreated = true;
	}
	
	private static void initContext(){
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GLFW.GLFW_TRUE);
		
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
	}
	
	private static void createContext(){
		GL.createCapabilities();
	}
	
	private static void setLocalPos(int xPos, int yPos){
		Window.xPos = xPos;
		Window.yPos = yPos;
	}
	
	private static void setFramebufferSize(int width, int height){
		gotResized = true;
		
		Window.width = width;
		Window.height = height;
		Window.centerPositionX = (int)width/2;
		Window.centerPositionY = (int)height/2;
	}
	
	public static void dispose(){
		Cursor.dispose();
		
		if(window != MemoryUtil.NULL){
			Callbacks.glfwFreeCallbacks(window);
			GLFW.glfwDestroyWindow(window);
		}
        
		GLFW.glfwTerminate();
		
		if(startedCreating){
			GLFW.glfwSetErrorCallback(null).free();
		}
	}
	
	public static void takeScreenshot(){
		GL11.glReadBuffer(GL11.GL_FRONT);
		final ByteBuffer buffer = DataUtil.createByteBuffer(width * height * 4);
		GL11.glReadPixels(0, 0, width, height, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	    
		new ScreenshotSaver("screenshots/", buffer, width, height).start();
	}
	
	public static void setStartTitle(String title){
		if(!gotCreated){
			Window.title = title;
		}
	}
	
	public static void setStartSize(int width, int height){
		if(!gotCreated){
			Window.width = width;
			Window.height = height;
		}
	}
	
	public static void setStartFullscreen(boolean value){
		if(!gotCreated){
			fullscreen = value;
		}
	}
	
	public static void setStartResizable(boolean value){
		if(!gotCreated){
			startResizable = value;
		}
	}
	
	public static void setStartIcon(String icon16, String icon32){
		if(!gotCreated){
			startIcon16 = icon16;
			startIcon32 = icon32;
		}
	}
	
	public static void setStartCursor(Cursor cursor){
		if(!gotCreated){
			Window.cursor = cursor;
		}
	}
	
	public static void setStartMinSizeLimit(int width, int height){
		if(!gotCreated){
			minWidth = width;
			minHeight = height;
		}
	}
	
	public static void setStartMaxSizeLimit(int width, int height){
		if(!gotCreated){
			maxWidth = width;
			maxHeight = height;
		}
	}
	
	public static void setStartSizeLimits(int minWidth, int minHeight, int maxWidth, int maxHeight){
		if(!gotCreated){
			Window.minWidth = minWidth;
			Window.minHeight = minHeight;
			Window.maxWidth = maxWidth;
			Window.maxHeight = maxHeight;
		}
	}
	
	public static void setIcon(String icon16, String icon32){
		final GLFWImage.Buffer icons = GLFWImage.malloc(2);
		
		final ImageData pixels16 = ImageUtil.imageToImageData(icon16);
		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		
		final ImageData pixels32 = ImageUtil.imageToImageData(icon32);
		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
	    
		icons.position(0);
		
		GLFW.glfwSetWindowIcon(window, icons);
		
		icons.free();
		pixels16.cleanUp();
		pixels32.cleanUp();
	}
	
	private static void setIcon(ImageData icon16, ImageData icon32){
		final GLFWImage.Buffer icons = GLFWImage.malloc(2);
		
//		final ImageData pixels16 = ImageUtil.bufferedImageToByteBuffer(icon16);
//		icons.position(0).width(pixels16.width).height(pixels16.height).pixels(pixels16.data);
		icons.position(0).width(icon16.width).height(icon16.height).pixels(icon16.data);
		
//		final ImageData pixels32 = ImageUtil.bufferedImageToByteBuffer(icon32);
//		icons.position(1).width(pixels32.width).height(pixels32.height).pixels(pixels32.data);
		icons.position(1).width(icon32.width).height(icon32.height).pixels(icon32.data);
		
		icons.position(0);
		
		GLFW.glfwSetWindowIcon(window, icons);
		
		icons.free();
	}
	
	public static void setCursor(Cursor cursor){
		if(cursor == null){
			GLFW.glfwSetCursor(window, MemoryUtil.NULL);
		}else{
			Window.cursor = cursor;
			
			GLFW.glfwSetCursor(window, cursor.getId());
		}
	}
	
	public static void render(){
		gotResized = false;
		GLFW.glfwSwapBuffers(window);
	}
	
	public static void updateInput(){
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
			GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);
		}else{
			GLFW.glfwSetWindowAttrib(window, GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
		}
	}
	
	public static void setFullscreen(boolean value){
		if(value){
			if(!fullscreen){
				oldXPos = xPos;
				oldYPos = yPos;
				oldWidth = width;
				oldHeight = height;
				
				final GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
				GLFW.glfwSetWindowMonitor(window, GLFW.glfwGetPrimaryMonitor(), 0, 0, vidMode.width(), vidMode.height(), vidMode.refreshRate());
				
				if(NeonEngine.isVSyncEnabled()){
					GLFW.glfwSwapInterval(1);
				}else{
					GLFW.glfwSwapInterval(0);
				}
				
				fullscreen = true;
			}
		}else{
			if(fullscreen){
				xPos = oldXPos;
				yPos = oldYPos;
				width = oldWidth;
				height = oldHeight;
				
				GLFW.glfwSetWindowMonitor(window, MemoryUtil.NULL, oldXPos, oldYPos, oldWidth, oldHeight, 0);
				
				if(NeonEngine.isVSyncEnabled()){
					GLFW.glfwSwapInterval(1);
				}else{
					GLFW.glfwSwapInterval(0);
				}
				
				fullscreen = false;
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
//		for(int i = 0; i < 32; i++){
//			RenderingEngine.textureBound.put(i, -1);
//		}
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		if(!NeonEngine.is1x1ViewportEnabled()){
			GL11.glViewport(0, 0, width, height);
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
		return fullscreen;
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
		return new Vector2f(centerPositionX, centerPositionY);
	}
	
	public static void setPos(int xPos, int yPos){
		GLFW.glfwSetWindowPos(window, xPos, yPos);
	}
	
	public static void setSize(int width, int height){
		GLFW.glfwSetWindowSize(window, width, height);
	}
	
	public static Cursor getCurrentCursor(){
		return cursor;
	}
	
	public static void requestAttention(){
		GLFW.glfwRequestWindowAttention(window);
	}
	
	
	public static void setCursorPos(int xPos, int yPos){
		GLFW.glfwSetCursorPos(window, (double)xPos, (double)yPos);
	}
	
	public static void setInputMode(int mode, int value){
		GLFW.glfwSetInputMode(window, mode, value);
	}
	
	public static int getInputMode(int mode){
		return GLFW.glfwGetInputMode(window, mode);
	}
}
