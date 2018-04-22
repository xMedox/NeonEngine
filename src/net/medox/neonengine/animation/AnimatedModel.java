package net.medox.neonengine.animation;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.rendering.AnimatedMesh;

public class AnimatedModel{
	private final AnimatedMesh mesh;
	private final Joint rootJoint;
	private final int jointCount;
	private final Animator animator;
	
	public AnimatedModel(AnimatedMesh mesh, Joint rootJoint, int jointCount){
		this.mesh = mesh;
		this.rootJoint = rootJoint;
		this.jointCount = jointCount;
		animator = new Animator(this);
		
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}
	
	public Joint getRootJoint(){
		return rootJoint;
	}
	
	public void doAnimation(Animation animation){
		animator.doAnimation(animation);
	}
	
	public void update(float delta){
		animator.update(delta);
	}
	
	public Matrix4f[] getJointTransforms(){
		Matrix4f[] jointMatrices = new Matrix4f[jointCount];
		addJointsToArray(rootJoint, jointMatrices);
		return jointMatrices;
	}
	
	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices){
		jointMatrices[headJoint.index] = headJoint.getAnimatedTransform();
		
		for(Joint childJoint : headJoint.children){
			addJointsToArray(childJoint, jointMatrices);
		}
	}
}
