package net.medox.neonengine.core;

public class ProfileTimer{
	private int numInvocations;
	private double totalTime;
	private double startTime;
	
	public void startInvocation(){
		startTime = Time.getTime();
	}
	
	public void stopInvocation(){
		if(startTime == 0){
			NeonEngine.throwError("Error: StopInvocation called without matching start invocation");
		}
		
		numInvocations++;
		totalTime += (Time.getTime() - startTime);
		startTime = 0;
	}
	
	public double getTimeAndReset(double divisor){
		divisor = (divisor == 0) ? numInvocations : divisor;
		final double result = totalTime == 0 && divisor == 0.0 ? 0.0 : (1000.0 * totalTime)/(double)divisor;
		totalTime = 0.0;
		numInvocations = 0;
		
		return result;
	}
	
	public double displayAndReset(String message, double divisor, int messageLength){
		final StringBuilder whiteSpace = new StringBuilder();
		for(int i = message.length(); i < messageLength; i++){
			whiteSpace.append(' ');
		}
		
		final double time = getTimeAndReset(divisor);
		System.out.println(message + whiteSpace + time + " ms");
		return time;
	}
	
	public double displayAndReset(String message, double divisor){
		return this.displayAndReset(message, divisor, 40);
	}
	
	public double displayAndReset(String message){
		return this.displayAndReset(message, 0, 40);
	}
}
