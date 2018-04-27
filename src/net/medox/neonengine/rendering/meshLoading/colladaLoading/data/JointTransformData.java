package net.medox.neonengine.rendering.meshLoading.colladaLoading.data;

import net.medox.neonengine.math.Matrix4f;

public class JointTransformData{
	public final String jointNameId;
	public final Matrix4f jointLocalTransform;
	
	public JointTransformData(String jointNameId, Matrix4f jointLocalTransform){
		this.jointNameId = jointNameId;
		this.jointLocalTransform = jointLocalTransform;
	}
}
