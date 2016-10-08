package net.medox.neonengine.core;

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
	private static final String VERSION = "1.0.0b Build 17";
	
	private static ProfileTimer sleepTimer;
	private static ProfileTimer swapBufferTimer;
	private static ProfileTimer windowUpdateTimer;
	private static ProfileTimer engineInputTimer;
	private static ProfileTimer enginePhysicTimer;
	
	public static int OPTION_ENABLE_PROFILING = 0; //0 = false 1 = true
	public static int OPTION_ENABLE_VSYNC = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_FXAA = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_SHADOWS = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_2D = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_PARTICLES = 1; //0 = false 1 = true
	public static int OPTION_ENABLE_BLOOM = 1; //0 = false 1 = true
	public static int OPTION_TEXTURE_QUALITY = 0; //0 = best 1 = medium 2 = lowest
	public static int OPTION_SHADOW_QUALITY = 0; //0 = best 1 = medium 2 = lowest
	
	public static int PROFILING_DISABLE_MESH_DRAWING = 0; //0 = false 1 = true
	public static int PROFILING_DISABLE_SHADING = 0; //0 = false 1 = true
	public static int PROFILING_SET_1x1_VIEWPORT = 0; //0 = false 1 = true
	public static int PROFILING_SET_2x2_TEXTURE = 0; //0 = false 1 = true
	
	private static Game game;
	private static boolean isRunning;
	private static double frameTime;
	private static int fps;
	
	public static void init(Game game, int framerate){
		if(OPTION_ENABLE_PROFILING == 1){
			System.out.println("Starting up");
		
			sleepTimer = new ProfileTimer();
			swapBufferTimer = new ProfileTimer();
			windowUpdateTimer = new ProfileTimer();
			engineInputTimer = new ProfileTimer();
			enginePhysicTimer = new ProfileTimer();
		}
		
		Util.init();
		
		NeonEngine.game = game;
		
		isRunning = false;
		frameTime = 1.0/(double)framerate;
		
		SoundEngine.init();
		PhysicsEngine.init();
	}
	
	public static String getVersion(){
		return VERSION;
	}
	
	public static void changeFramerate(int framerate){
		frameTime = 1.0/(double)framerate;
	}
	
	public static void createWindow(){
		Window.createWindow();
		
		RenderingEngine.init();
	}
	
	public static int getFPS(){
		return fps;
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
		
//		final ProfileTimer test = new ProfileTimer();
//		
//		test.startInvocation();
		game.init();
//		test.stopInvocation();
//		
//		test.displayAndReset("Start Time:");
//		System.out.println("--------------------------------------------------------------");
		
		boolean render;
		
		double startTime;
		double passedTime;
		double lastTime = Time.getTime();
		double unprocssedTime = 0;
		double frameCounter = 0;
		int frames = 0;
		
		double totalTime;
		double totalMeasuredTime;
		
		while(isRunning){
			render = false;
			
			startTime = Time.getTime();
			passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocssedTime += passedTime;
			frameCounter += passedTime;
			
			if(frameCounter >= 1.0){
				if(Window.gotCreated()){
					if(OPTION_ENABLE_PROFILING == 1){
						totalTime = (1000.0 * frameCounter)/((double)frames);
						totalMeasuredTime = 0.0;
						
						totalMeasuredTime += game.displayInputTime((double)frames);
						totalMeasuredTime += game.displayUpdateTime((double)frames);
						totalMeasuredTime += RenderingEngine.displayRenderTime((double)frames);
						totalMeasuredTime += RenderingEngine.display2DRenderTime((double)frames);
						totalMeasuredTime += enginePhysicTimer.displayAndReset("Physics Time: ", (double)frames);
						totalMeasuredTime += sleepTimer.displayAndReset("Sleep Time: ", (double)frames);
						totalMeasuredTime += windowUpdateTimer.displayAndReset("Window Update Time: ", (double)frames);
						totalMeasuredTime += swapBufferTimer.displayAndReset("Buffer Swap Time: ", (double)frames);
						totalMeasuredTime += engineInputTimer.displayAndReset("Engine Input Time: ", (double)frames);
						totalMeasuredTime += RenderingEngine.displayWindowSyncTime((double)frames);
						
						System.out.println("Other Time:                             " + (totalTime - totalMeasuredTime) + " ms");
						System.out.println("Total Time:                             " + totalTime + " ms (" + frames + "fps)");
						System.out.println("");
					}
					
//					Runtime runtime = Runtime.getRuntime();
//					
//					NumberFormat format = NumberFormat.getInstance();
//					
//					long maxMemory = runtime.maxMemory();
//					long allocatedMemory = runtime.totalMemory();
//					long freeMemory = runtime.freeMemory();
//					
//					System.out.println("free memory: " + format.format(freeMemory / 1024));
//					System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
//					System.out.println("max memory: " + format.format(maxMemory / 1024));
//					System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
					
					fps = frames;
				}
				frames = 0;
				frameCounter = 0;
			}
			
			while(unprocssedTime > frameTime){
				if(Window.gotCreated()){
					Window.updateInput();
				}
				
				if(OPTION_ENABLE_PROFILING == 1){
					windowUpdateTimer.startInvocation();
				}
				if(Window.gotCreated() && Window.isCloseRequested()){
					stop();
				}
				if(OPTION_ENABLE_PROFILING == 1){
					windowUpdateTimer.stopInvocation();
				}
				
				game.input((float)frameTime);
				
				if(OPTION_ENABLE_PROFILING == 1){
					enginePhysicTimer.startInvocation();
				}
				PhysicsEngine.update((float)frameTime);
				if(OPTION_ENABLE_PROFILING == 1){
					enginePhysicTimer.stopInvocation();
				}
								
				game.update((float)frameTime);
				
				if(OPTION_ENABLE_PROFILING == 1){
					engineInputTimer.startInvocation();
				}
				if(Window.gotCreated()){
					Input.update();
				}
				if(OPTION_ENABLE_PROFILING == 1){
					engineInputTimer.stopInvocation();
				}
				
				if(Window.gotCreated()){
					render = true;
				}
				
				unprocssedTime -= frameTime;
			}
			
			if(render){
				if(Window.gotResized()){
					RenderingEngine.updateViewport();
				}
				
				game.render();
				
				if(OPTION_ENABLE_PROFILING == 1){
					swapBufferTimer.startInvocation();
				}
				Window.render();
				if(OPTION_ENABLE_PROFILING == 1){
					swapBufferTimer.stopInvocation();
				}
				frames++;
			}else{
				if(OPTION_ENABLE_PROFILING == 1){
					sleepTimer.startInvocation();
				}
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				if(OPTION_ENABLE_PROFILING == 1){
					sleepTimer.stopInvocation();
				}
			}
		}
		
		if(OPTION_ENABLE_PROFILING == 1){
			System.out.println("--------------------------------------------------------------");
			System.out.println("Shutting down");
		}
		
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
