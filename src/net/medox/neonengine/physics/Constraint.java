package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.bullet.dynamics.btTypedConstraint;

public abstract class Constraint{
	private static final List<Constraint> constraints = new ArrayList<Constraint>();
	
	private btTypedConstraint constraint;
	private boolean cleanedUp;
	
	public Constraint(){
		constraints.add(this);
	}
	
	public void setConstraint(btTypedConstraint constraint){
		this.constraint = constraint;
	}
	
	public btTypedConstraint getConstraint(){
		return constraint;
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			disposeData();
			constraints.remove(this);
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	private void disposeData(){
		constraint.dispose();
	}
	
	public static void dispose(){
		for(final Constraint data : constraints){
			data.disposeData();
		}
	}
}
