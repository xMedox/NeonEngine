package net.medox.vulkan;

import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.File;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWVulkan;

public class Vulkan{
    public static long window;
    
	private static boolean isRunning;
    
	private static String title = "Vulkan";
    
	private static int width = 854;
	private static int height = 480;
    
    private static GLFWErrorCallback errorCallback;
    
//    private static long instance;
//    private static VkAllocationCallbacks allocator;
//    private static ByteBuffer surface;
    
	public static void main(String[] args){
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		
		new HelloVulkan().run();
		
//		isRunning = false;
//		
//		createWindow();
//		
//		start();
	}
	
	public static void createWindow(){
		errorCallback = GLFWErrorCallback.createPrint().set();
		
		if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
//		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
//		
//		glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
//		
//        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
		
        window = glfwCreateWindow(width, height, title, NULL, NULL);
        
		if(window == NULL){
	        glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window");
		}
		
		final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		
		glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
		
		glfwMakeContextCurrent(window);
		
//		if(CoreEngine.OPTIONS_ENABLE_VSYNC == 1){
//			glfwSwapInterval(1);
//		}else{
//			glfwSwapInterval(0);
//		}
		
		glfwShowWindow(window);
		
//		GL.createCapabilities();
//		glfwCreateWindowSurface(demo->inst, demo->window, NULL, &demo->surface);
//		GLFWVulkan.glfwCreateWindowSurface(instance, window, allocator, surface);
	}
	
	public static void start(){
		if(!isRunning){
			run();
		}
	}
	
	public static void stop(){
		if(isRunning){
			isRunning = false;
		}
	}
	
	private static void run(){
		isRunning = true;
		
		if(GLFWVulkan.glfwVulkanSupported()){
			System.out.println("Vulkan is supported(" + /*VK10.VK_VERSION_MAJOR + "." + VK10.VK_VERSION_MINOR + "." + VK10.VK_VERSION_PATCH + */")");
		}else{
			System.out.println("Vulkan is not supported");
		}
		
		while(isRunning){
			if(glfwWindowShouldClose(window)){
				stop();
			}
			
			render();
		}
		
		cleanUp();
		
		System.exit(0);
	}
	
	public static void render(){
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public static void cleanUp(){
		glfwDestroyWindow(window);
        
        glfwTerminate();
        errorCallback.free();
	}
}
