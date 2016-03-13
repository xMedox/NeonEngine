package net.medox.game;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;

public class RotateComponent extends EntityComponent{
	private Vector3f axis;
	private float rotateAmout;
	
	public RotateComponent(){
		axis = new Vector3f(0, 1, 0);
		rotateAmout = 0.0005f;
	}
	
	public RotateComponent(Vector3f axis, float rotateAmout){
		this.axis = axis;
		this.rotateAmout = rotateAmout;
	}
	
	public void update(float delta){
		getTransform().rotate(axis, rotateAmout);
	}
}
