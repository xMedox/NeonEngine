package net.medox.neonengine.animation;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.math.Matrix4f;

public class Joint{
	public final int index;
	public final String name;
	public final List<Joint> children;
	
	private final Matrix4f localBindTransform;
	
	private Matrix4f animatedTransform = new Matrix4f().initIdentity();
	private Matrix4f inverseBindTransform = new Matrix4f().initIdentity();
	
	public Joint(int index, String name, Matrix4f bindLocalTransform){
		this.index = index;
		this.name = name;
		this.localBindTransform = bindLocalTransform;
		
		children = new ArrayList<Joint>();
		
//		animatedTransform = new Matrix4f().initIdentity();
//		inverseBindTransform = new Matrix4f().initIdentity();
	}
	
	protected void calcInverseBindTransform(Matrix4f parentBindTransform){
		Matrix4f bindTransform = Matrix4f.mul(parentBindTransform, localBindTransform);		
		inverseBindTransform = bindTransform.invert();
		for(Joint child : children){
			child.calcInverseBindTransform(bindTransform);
		}
	}
	
	public void addChild(Joint child){
		children.add(child);
	}
	
	public Matrix4f getAnimatedTransform(){
		return animatedTransform;
	}
	
	public Matrix4f getInverseBindTransform(){
		return inverseBindTransform;
	}
	
	public void setAnimationTransform(Matrix4f animationTransform){
		animatedTransform = animationTransform;
	}
}
