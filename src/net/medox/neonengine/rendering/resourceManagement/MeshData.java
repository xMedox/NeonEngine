package net.medox.neonengine.rendering.resourceManagement;

import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class MeshData extends ReferenceCounter{
	public MeshData(IndexedModel model/*, boolean create*/){
		super();
	}
	
	public void draw(){
		
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return false;
	}
	
	public void dispose(){
		
	}
}

