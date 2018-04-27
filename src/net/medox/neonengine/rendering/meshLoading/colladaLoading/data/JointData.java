package net.medox.neonengine.rendering.meshLoading.colladaLoading.data;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.math.Matrix4f;

public class JointData{
	public final int index;
	public final String nameId;
	public final Matrix4f bindLocalTransform;
	
	public final List<JointData> children = new ArrayList<JointData>();
	
	public JointData(int index, String nameId, Matrix4f bindLocalTransform){
		this.index = index;
		this.nameId = nameId;
		this.bindLocalTransform = bindLocalTransform;
	}
	
	public void addChild(JointData child){
		children.add(child);
	}
}
