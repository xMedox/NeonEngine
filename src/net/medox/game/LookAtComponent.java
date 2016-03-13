package net.medox.game;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class LookAtComponent extends EntityComponent{
	private float moveAmt = 3f;
	
	@Override
	public void update(float delta){
//		if(renderingEngine != null){
			Quaternion newRot = getTransform().getLookAtRotation(RenderingEngine.getMainCamera().getTransform().getTransformedPos(),
					new Vector3f(0, 1, 0));
					//getTransform().getRot().getUp());

			getTransform().setRot(getTransform().getRot().nlerp(newRot, delta * 5.0f, true));
//			getTransform().setRot(getTransform().getRot().slerp(newRot, delta * 5.0f, true));
			
//			getTransform().setRot(newRot);
			
			if(getTransform().getTransformedPos().sub(RenderingEngine.getMainCamera().getTransform().getTransformedPos()).length() >= 5){
				move(getTransform().getRot().getForward(), moveAmt*delta);
			}
//		}
	}
	
	public void move(Vector3f dir, float amt){
		getTransform().move(dir.mul(amt));
	}
}