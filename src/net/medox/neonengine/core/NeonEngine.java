package net.medox.neonengine.core;

import java.io.File;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.audio.SoundEngine;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.CubeMap;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class NeonEngine{
	private static final String VERSION = "1.0.0a";
	
	public static final ProfileTimer swapBufferTimer = new ProfileTimer();
	
	public static int OPTION_ENABLE_VSYNC = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_FXAA = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_MIPMAPPING = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_SHADOWS = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_2D = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_PARTICLES = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_BLOOM = 1; //0 = false 1 = true
	public static int OPTION_MSAA_MULTIPLIER = 1; //texture size multiplier
	public static int OPTION_TEXTURE_QUALITY = 0; //0 = best 1 = medium 2 = lowest
	public static int OPTION_SHADOW_QUALITY = 0; //0 = best 1 = medium 2 = lowest
	public static int OPTION_FORCE_RENDERING_MODE = RenderingEngine.OPENGL; //-1 = dont care 0 = GL 1 = VK
	
	public static int PROFILING_DISABLE_MESH_DRAWING = 0; //0 = false 1 = true
	public static int PROFILING_DISABLE_SHADING = 0; //0 = false 1 = true
	public static int PROFILING_SET_1x1_VIEWPORT = 0; //0 = false 1 = true
	public static int PROFILING_SET_2x2_TEXTURE = 0; //0 = false 1 = true
	
	private static UpdateThread updateThread;
	
	public static int fps;
	private static Game game;
	private static double frameTime;
	
	public static boolean render;
	
	public static void init(Game game, int framerate){
		System.out.println("Starting up");
		
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		
		Util.init();
		
		NeonEngine.game = game;
		
		frameTime = 1.0/(double)framerate;
		
		updateThread = new UpdateThread(game, frameTime);
		updateThread.setRunning(true);
		
		SoundEngine.init();
		PhysicsEngine.init();
	}
	
	public static String getVersion(){
		return VERSION;
	}
	
	public static void changeFramerate(int framerate){
		frameTime = 1.0/(double)framerate;
		updateThread.changeFrameTime(frameTime);
	}
	
	public static void createWindow(){
		Window.createWindow();
		
		RenderingEngine.init();
	}
	
	public static void start(){
		updateThread.startUpdate();
		
		run();
	}
	
	public static void stop(){
		updateThread.stopUpdate();
	}
	
	private static void run(){
		game.init();
		
		updateThread.start();
		
		while(updateThread.isRunning()){
//			System.out.println("S+");
			
//			if(render){
				if(Window.gotResized()){
					RenderingEngine.updateViewport();
				}
				
				game.render();
				
				swapBufferTimer.startInvocation();
				Window.render();
				swapBufferTimer.stopInvocation();
				updateThread.frames++;
//			}else{
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
//			}
		}
		
		System.out.println("--------------------------------------------------------------");
		System.out.println("Shutting down");
		
		cleanUp();
		
		game.cleanUp();
		game.dispose();
		
		System.exit(0);
	}
	
	private static void cleanUp(){
		Texture.dispose();
		CubeMap.dispose();
		Mesh.dispose();
		Shader.dispose();
		
		Sound.dispose();
		
		RenderingEngine.dispose();
		SoundEngine.dispose();
		Window.dispose();
	}
}
