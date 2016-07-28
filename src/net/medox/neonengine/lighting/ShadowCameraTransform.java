package net.medox.neonengine.lighting;

import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class ShadowCameraTransform{
	public Vector3f pos;
	public Quaternion rot;
	
	public ShadowCameraTransform(Vector3f pos, Quaternion rot){
		this.pos = pos;
		this.rot = rot;
	}
}
