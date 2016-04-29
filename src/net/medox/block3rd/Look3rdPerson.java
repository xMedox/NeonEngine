package net.medox.block3rd;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class Look3rdPerson extends EntityComponent{
	private Entity entity;
	
	public Look3rdPerson(Entity entity){
		this.entity = entity;
	}
	
	@Override
	public void input(float delta){
		if(Input.getMouse(Input.BUTTON_RIGHT) || Input.getKey(Input.KEY_W) || Input.getKey(Input.KEY_A) || Input.getKey(Input.KEY_S) || Input.getKey(Input.KEY_D)){
			getTransform().setRot(new Quaternion(new Matrix4f().initRotation(entity.getTransform().getTransformedRot().getForward().mul(new Vector3f(1, 0, 1)), new Vector3f(0, 1, 0))));
		}
	}
}
