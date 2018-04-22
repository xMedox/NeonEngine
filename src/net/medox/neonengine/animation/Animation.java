package net.medox.neonengine.animation;

public class Animation{
	private final float length;
	private final KeyFrame[] keyFrames;
	
	public Animation(float lengthInSeconds, KeyFrame[] frames){
		this.length = lengthInSeconds;
		this.keyFrames = frames;
	}
	
	public float getLength(){
		return length;
	}
	
	public KeyFrame[] getKeyFrames(){
		return keyFrames;
	}
}
