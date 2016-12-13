package net.medox.neonengine.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBodyHelpers;

public class SoftBody extends CollisionBase{
	private btSoftBody body;
	
	public SoftBody(){
		super(SOFTBODY);
		
		float width = 4;
		float height = 20;
		
		body = btSoftBodyHelpers.CreatePatch(PhysicsEngine.getWorldInfo(), 
				new Vector3(-width, height, -width),
				new Vector3(width, height, -width),
				new Vector3(-width, height, width),
				new Vector3(width, height, width),
				50, 50, 4+8, true);
	}
	
	public btSoftBody getBody(){
		return body;
	}
}
