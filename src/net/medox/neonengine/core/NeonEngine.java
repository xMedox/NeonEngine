package net.medox.neonengine.core;

import net.medox.neonengine.audio.Sound;
import net.medox.neonengine.audio.SoundEngine;
import net.medox.neonengine.physics.CharacterController;
import net.medox.neonengine.physics.Collider;
import net.medox.neonengine.physics.Constraint;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.CubeMap;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class NeonEngine{
	private static final String VERSION = "1.0.0b Build 20";
	
	private static ProfileTimer sleepTimer;
	private static ProfileTimer swapBufferTimer;
	private static ProfileTimer windowUpdateTimer;
	private static ProfileTimer engineInputTimer;
	private static ProfileTimer enginePhysicTimer;
	
	private static boolean optionEnableProfiling = false;
	private static boolean optionEnableVSync = true;
	private static boolean optionEnableFXAA = true;
	private static boolean optionEnableShadows = true;
	private static boolean optionEnable2D = true;
	private static boolean optionEnableParticles = true;
	private static boolean optionEnableBloom = true;
	private static int optionTextureQuality = 0; //0 = best 1 = medium 2 = lowest
	private static int optionShadowQuality = 0; //0 = best 1 = medium 2 = lowest
	
	private static boolean profilingDisableMeshDrawing = false;
	private static boolean profilingDisableShading = false;
	private static boolean profilingEnable1x1Viewport = false;
	private static boolean profilingEnable2x2Texture = false;
	
	private static Game game;
	private static boolean isRunning;
	private static double frameTime;
	private static int fps;
	
	public static void init(Game game, int framerate){
		if(optionEnableProfiling){
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
					if(optionEnableProfiling){
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
				
				if(optionEnableProfiling){
					windowUpdateTimer.startInvocation();
				}
				if(Window.gotCreated() && Window.isCloseRequested()){
					stop();
				}
				if(optionEnableProfiling){
					windowUpdateTimer.stopInvocation();
				}
				
				game.input((float)frameTime);
				
				if(optionEnableProfiling){
					enginePhysicTimer.startInvocation();
				}
				PhysicsEngine.update((float)frameTime);
				if(optionEnableProfiling){
					enginePhysicTimer.stopInvocation();
				}
								
				game.update((float)frameTime);
				
				if(optionEnableProfiling){
					engineInputTimer.startInvocation();
				}
				if(Window.gotCreated()){
					Input.update();
				}
				if(optionEnableProfiling){
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
				
				if(optionEnableProfiling){
					swapBufferTimer.startInvocation();
				}
				Window.render();
				if(optionEnableProfiling){
					swapBufferTimer.stopInvocation();
				}
				frames++;
			}else{
				if(optionEnableProfiling){
					sleepTimer.startInvocation();
				}
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				if(optionEnableProfiling){
					sleepTimer.stopInvocation();
				}
			}
		}
		
		if(optionEnableProfiling){
			System.out.println("--------------------------------------------------------------");
			System.out.println("Shutting down");
		}
		
		Window.dispose();
		
		game.cleanUp();
		game.dispose();
		
		cleanUp();
		
		System.exit(0);
	}
	
	public static void enableProfiling(boolean value){
		optionEnableProfiling = value;
	}
	public static void enableVSync(boolean value){
		optionEnableVSync = value;
	}
	public static void enableFXAA(boolean value){
		optionEnableFXAA = value;
	}
	public static void enableShadows(boolean value){
		optionEnableShadows = value;
	}
	public static void enable2D(boolean value){
		optionEnable2D = value;
	}
	public static void enableParticles(boolean value){
		optionEnableParticles = value;
	}
	public static void enableBloom(boolean value){
		optionEnableBloom = value;
	}
	public static void setTextureQuality(int value){
		optionTextureQuality = value;
	}
	public static void setShadowQuality(int value){
		optionShadowQuality = value;
	}
	
	public static boolean isProfilingEnabled(){
		return optionEnableProfiling;
	}
	public static boolean isVSyncEnabled(){
		return optionEnableVSync;
	}
	public static boolean isFXAAEnabled(){
		return optionEnableFXAA;
	}
	public static boolean areShadowsEnabled(){
		return optionEnableShadows;
	}
	public static boolean is2DEnabled(){
		return optionEnable2D;
	}
	public static boolean areParticlesEnabled(){
		return optionEnableParticles;
	}
	public static boolean isBloomEnabled(){
		return optionEnableBloom;
	}
	public static int getTextureQuality(){
		return optionTextureQuality;
	}
	public static int getShadowQuality(){
		return optionShadowQuality;
	}
	
	public static void disableMeshDrawing(boolean value){
		profilingDisableMeshDrawing = value;
	}
	public static void disableShading(boolean value){
		profilingDisableShading = value;
	}
	public static void enable1x1Viewport(boolean value){
		profilingEnable1x1Viewport = value;
	}
	public static void enable2x2Texture(boolean value){
		profilingEnable2x2Texture = value;
	}
	
	public static boolean isMeshDrawingDisabled(){
		return profilingDisableMeshDrawing;
	}
	public static boolean isMeshShadingDisabled(){
		return profilingDisableShading;
	}
	public static boolean is1x1ViewportEnabled(){
		return profilingEnable1x1Viewport;
	}
	public static boolean is2x2TextureEnabled(){
		return profilingEnable2x2Texture;
	}
	
	private static void cleanUp(){
		Texture.dispose();
		CubeMap.dispose();
		Mesh.dispose();
		Shader.dispose();
		
		Sound.dispose();
		
		Collider.dispose();
		Constraint.dispose();
		CharacterController.dispose();
		
		RenderingEngine.dispose();
		SoundEngine.dispose();
		PhysicsEngine.dispose();
	}
}
