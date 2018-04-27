package net.medox.neonengine.animation;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.math.Matrix4f;

public class Animator{
	private final AnimatedModel entity;
	
	private Animation currentAnimation;
	private float animationTime = 0;
	private boolean active;
	
	public Animator(AnimatedModel entity){
		this.entity = entity;
	}
	
	public void doAnimation(Animation animation){
		animationTime = 0;
		currentAnimation = animation;
		active = true;
	}
	
	public void start(){
		active = true;
	}
	
	public void stop(){
		active = false;
	}
	
	public void reset(){
		animationTime = 0;
	}
	
	public void update(float delta){
		if(currentAnimation == null){
			return;
		}
		if(active){
			increaseAnimationTime(delta);
		}
		Map<String, Matrix4f> currentPose = calculateCurrentAnimationPose();
		applyPoseToJoints(currentPose, entity.getRootJoint(), new Matrix4f().initIdentity());
	}
	
	private void increaseAnimationTime(float delta){
		animationTime += delta;
		if(animationTime > currentAnimation.getLength()) {
			this.animationTime %= currentAnimation.getLength();
		}
	}
	
	private Map<String, Matrix4f> calculateCurrentAnimationPose(){
		KeyFrame[] frames = getPreviousAndNextFrames();
		float progression = calculateProgression(frames[0], frames[1]);
		return interpolatePoses(frames[0], frames[1], progression);
	}
	
	private void applyPoseToJoints(Map<String, Matrix4f> currentPose, Joint joint, Matrix4f parentTransform){
		Matrix4f currentLocalTransform = currentPose.get(joint.name);
		Matrix4f currentTransform = Matrix4f.mul(parentTransform, currentLocalTransform);
		for(Joint childJoint : joint.children){
			applyPoseToJoints(currentPose, childJoint, currentTransform);
		}
		currentTransform = Matrix4f.mul(currentTransform, joint.getInverseBindTransform());
		joint.setAnimationTransform(currentTransform);
	}
	
	private KeyFrame[] getPreviousAndNextFrames(){
		KeyFrame[] allFrames = currentAnimation.getKeyFrames();
		KeyFrame previousFrame = allFrames[0];
		KeyFrame nextFrame = allFrames[0];
		for(int i = 1; i < allFrames.length; i++){
			nextFrame = allFrames[i];
			if(nextFrame.getTimeStamp() > animationTime){
				break;
			}
			previousFrame = allFrames[i];
		}
		return new KeyFrame[] { previousFrame, nextFrame };
	}
	
	private float calculateProgression(KeyFrame previousFrame, KeyFrame nextFrame){
		float totalTime = nextFrame.getTimeStamp() - previousFrame.getTimeStamp();
		float currentTime = animationTime - previousFrame.getTimeStamp();
		return currentTime / totalTime;
	}
	
	private Map<String, Matrix4f> interpolatePoses(KeyFrame previousFrame, KeyFrame nextFrame, float progression){
		Map<String, Matrix4f> currentPose = new HashMap<String, Matrix4f>();
		for(String jointName : previousFrame.getJointKeyFrames().keySet()){
			JointTransform previousTransform = previousFrame.getJointKeyFrames().get(jointName);
			JointTransform nextTransform = nextFrame.getJointKeyFrames().get(jointName);
			JointTransform currentTransform = JointTransform.interpolate(previousTransform, nextTransform, progression);
			currentPose.put(jointName, currentTransform.getLocalTransform());
		}
		return currentPose;
	}
}
