package net.medox.neonengine.physics;

import com.badlogic.gdx.physics.bullet.dynamics.btTypedConstraint;

public class Constraint{
	private btTypedConstraint constraint;
	
	public void setConstraint(btTypedConstraint constraint){
		this.constraint = constraint;
	}
	
	public btTypedConstraint getConstraint(){
		return constraint;
	}
}
