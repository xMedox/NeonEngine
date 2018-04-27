package net.medox.neonengine.rendering.meshLoading;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.animation.Joint;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.AnimatedMesh;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.GeometryLoader;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.SkeletonLoader;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.SkinLoader;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.XmlNode;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.XmlParser;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.JointData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.MeshData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.SkeletonData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.SkinningData;

public class ColladaModel{
	private MeshData meshData;
	private SkeletonData jointsData;
	
	public ColladaModel(String fileName){
		XmlNode node = XmlParser.loadXmlFile(fileName);
		
		SkinLoader skinLoader = new SkinLoader(node.getChild("library_controllers"), AnimatedMesh.MAX_WEIGHTS);
		SkinningData skinningData = skinLoader.extractSkinData();
		
		SkeletonLoader jointsLoader = new SkeletonLoader(node.getChild("library_visual_scenes"), skinningData.jointOrder);
		jointsData = jointsLoader.extractBoneData();
		
		GeometryLoader g = new GeometryLoader(node.getChild("library_geometries"), skinningData.verticesSkinData);
		meshData = g.extractModelData();
	}
	
	public Joint getRootJoint(){
		return createJoints(jointsData.headJoint);
	}
	
	public int getJointCount(){
		return jointsData.jointCount;
	}
	
	private static Joint createJoints(JointData data){
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for(JointData child : data.children){
			joint.addChild(createJoints(child));
		}
		return joint;
	}
	
	public IndexedAnimatedModel toIndexedModel(){
		List<Vector3f> positions = meshData.getVertices();
		List<Vector2f> texCoords = meshData.getTextureCoords();
		List<Vector3f> normals = meshData.getNormals();
		List<Vector3f> tangents = new ArrayList<Vector3f>();
		List<Integer> joints = meshData.getJointIds();
		List<Float> weights = meshData.getVertexWeights();
		List<Integer> indices = meshData.getIndices();
		
		IndexedAnimatedModel result = new IndexedAnimatedModel(positions, texCoords, normals, tangents, joints, weights, indices);
		
		float radius = -Float.MAX_VALUE;
		
		for(int i = 0; i < result.getPositions().size(); i++){
			radius = Math.max(radius, result.getPositions().get(i).length());
			
			result.getTangents().add(new Vector3f(0.0f, 0.0f, 0.0f));
		}
		
		result.calcTangents();
		
		result.setRadius(radius);
		
		return result;
	}
}
