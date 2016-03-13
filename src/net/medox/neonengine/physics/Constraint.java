package net.medox.neonengine.physics;

import com.bulletphysics.dynamics.constraintsolver.TypedConstraint;

public class Constraint{
	private TypedConstraint constraint;
	
	public void setConstraint(TypedConstraint constraint){
		this.constraint = constraint;
//		body.setUserPointer(this);
//		body.setCollisionFlags(body.getCollisionFlags() | CollisionFlags.CUSTOM_MATERIAL_CALLBACK);
	}
	
	public TypedConstraint getConstraint(){
		return constraint;
	}
}
