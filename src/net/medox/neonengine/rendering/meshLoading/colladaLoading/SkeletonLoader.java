package net.medox.neonengine.rendering.meshLoading.colladaLoading;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.JointData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.SkeletonData;

public class SkeletonLoader{
	private static final Matrix4f CORRECTION = new Matrix4f().initIdentity().rotate((float)Math.toRadians(-90), new Vector3f(1, 0, 0));
//	private static final Matrix4f CORRECTION = new Matrix4f().initIdentity().initRotation(90, 0, 0);
	
	private XmlNode armatureData;
	
	private List<String> boneOrder;
	
	private int jointCount = 0;
	
	public SkeletonLoader(XmlNode visualSceneNode, List<String> boneOrder){
		this.armatureData = visualSceneNode.getChild("visual_scene").getChildWithAttribute("node", "id", "Armature");
		this.boneOrder = boneOrder;
	}
	
	public SkeletonData extractBoneData(){
		XmlNode headNode = armatureData.getChild("node");
		JointData headJoint = loadJointData(headNode, true);
		return new SkeletonData(jointCount, headJoint);
	}
	
	private JointData loadJointData(XmlNode jointNode, boolean isRoot){
		JointData joint = extractMainJointData(jointNode, isRoot);
		for(XmlNode childNode : jointNode.getChildren("node")){
			joint.addChild(loadJointData(childNode, false));
		}
		return joint;
	}
	
	private JointData extractMainJointData(XmlNode jointNode, boolean isRoot){
		String nameId = jointNode.getAttribute("id");
		int index = boneOrder.indexOf(nameId);
		String[] matrixData = jointNode.getChild("matrix").getData().split(" ");
		Matrix4f matrix = new Matrix4f();
		matrix.load(convertData(matrixData));
		matrix = matrix.transpose();
		if(isRoot){
			matrix = Matrix4f.mul(CORRECTION, matrix);
		}
		jointCount++;
		return new JointData(index, nameId, matrix);
	}
	
	private FloatBuffer convertData(String[] rawData){
		float[] matrixData = new float[16];
		for(int i=0;i<matrixData.length;i++){
			matrixData[i] = Float.parseFloat(rawData[i]);
		}
		FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
		buffer.put(matrixData);
		buffer.flip();
		return buffer;
	}
}
