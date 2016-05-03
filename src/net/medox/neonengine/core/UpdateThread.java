package net.medox.neonengine.core;

import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Window;

public class UpdateThread extends Thread{
	private static final ProfileTimer sleepTimer = new ProfileTimer();
	private static final ProfileTimer windowUpdateTimer = new ProfileTimer();
	private static final ProfileTimer engineInputTimer = new ProfileTimer();
	private static final ProfileTimer enginePhysicTimer = new ProfileTimer();
	
	public int frames = 0;
	
	private Game game;
	private boolean isRunning;
	private double frameTime;
	
	public UpdateThread(Game game, double frameTime){
		this.game = game;
		this.frameTime = frameTime;
	}
	
	public void startUpdate(){
		if(!isRunning){
			run();
		}
	}
	
	public void stopUpdate(){
		if(isRunning){
			isRunning = false;
		}
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public void setRunning(boolean value){
		isRunning = value;
	}
	
	public void changeFrameTime(double frameTime){
		this.frameTime = frameTime;
	}
	
	public void run(){
		double lastTime = Time.getTime();
		double unprocssedTime = 0;
		double frameCounter = 0;
		
		while(isRunning){
			boolean render = false;
			NeonEngine.render = false;
			
			final double startTime = Time.getTime();
			final double passedTime = startTime - lastTime;
			lastTime = startTime;
			
			unprocssedTime += passedTime;
			frameCounter += passedTime;
			
			if(frameCounter >= 1.0){
				if(Window.gotCreated()){
					final double totalTime = (1000.0 * frameCounter)/((double)frames);
					double totalMeasuredTime = 0.0;
					
					totalMeasuredTime += game.displayInputTime((double)frames);
					totalMeasuredTime += game.displayUpdateTime((double)frames);
					totalMeasuredTime += RenderingEngine.displayRenderTime((double)frames);
					totalMeasuredTime += RenderingEngine.display2DRenderTime((double)frames);
					totalMeasuredTime += enginePhysicTimer.displayAndReset("Physics Time: ", (double)frames);
					totalMeasuredTime += sleepTimer.displayAndReset("Sleep Time: ", (double)frames);
					totalMeasuredTime += windowUpdateTimer.displayAndReset("Window Update Time: ", (double)frames);
					totalMeasuredTime += NeonEngine.swapBufferTimer.displayAndReset("Buffer Swap Time: ", (double)frames);
					totalMeasuredTime += engineInputTimer.displayAndReset("Engine Input Time: ", (double)frames);
					totalMeasuredTime += RenderingEngine.displayWindowSyncTime((double)frames);
					
					System.out.println("Other Time:                             " + (totalTime - totalMeasuredTime) + " ms");
					System.out.println("Total Time:                             " + totalTime + " ms (" + frames + "fps)");
					System.out.println("");
					
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
					
					NeonEngine.fps = frames;
				}
				frames = 0;
				frameCounter = 0;
			}
			
			while(unprocssedTime > frameTime){
				windowUpdateTimer.startInvocation();
				if(Window.gotCreated() && Window.isCloseRequested()){
					stopUpdate();
				}
				windowUpdateTimer.stopInvocation();
				
				game.input((float)frameTime);
				
				engineInputTimer.startInvocation();
				if(Window.gotCreated()){
					Input.update();
				}
				engineInputTimer.stopInvocation();
				
				
				enginePhysicTimer.startInvocation();
				PhysicsEngine.update((float)frameTime);
				enginePhysicTimer.stopInvocation();
				
				
				game.update((float)frameTime);
				
				if(Window.gotCreated()){
					render = true;
				}
				
				unprocssedTime -= frameTime;
			}
			
			if(render){
				NeonEngine.render = true;
			}else{
				sleepTimer.startInvocation();
				try{
					Thread.sleep(1);
				}catch(InterruptedException e){
					e.printStackTrace();
				}
				sleepTimer.stopInvocation();
			}
		}
	}
}
