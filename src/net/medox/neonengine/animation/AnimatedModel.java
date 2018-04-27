package net.medox.neonengine.animation;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.rendering.AnimatedMesh;
import net.medox.neonengine.rendering.Camera;

public class AnimatedModel{
	private final AnimatedMesh mesh;
	private final Joint rootJoint;
	private final int jointCount;
	private final Animator animator;
	
	public AnimatedModel(AnimatedMesh mesh/*, Joint rootJoint, int jointCount*/){
		this.mesh = mesh;
		rootJoint = mesh.getRootJoint();
		jointCount = mesh.getJointCount();
		animator = new Animator(this);
		
		rootJoint.calcInverseBindTransform(new Matrix4f().initIdentity());
	}
	
	public Joint getRootJoint(){
		return rootJoint;
	}
	
	public void doAnimation(Animation animation){
		animator.doAnimation(animation);
	}
	
	public void start(){
		animator.start();
	}
	
	public void stop(){
		animator.stop();
	}
	
	public void reset(){
		animator.reset();
	}
	
	public void update(float delta){
		animator.update(delta);
	}
	
	public void draw(){
		mesh.draw();
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return mesh.inFrustum(transform, camera);
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
