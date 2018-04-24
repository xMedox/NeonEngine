package net.medox.neonengine.rendering.meshLoading.colladaLoading.data;

import java.util.List;

import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

public class MeshData{
	private static final int DIMENSIONS = 3;
	
	private final List<Vector3f> vertices;
	private final List<Vector2f> textureCoords;
	private final List<Vector3f> normals;
	private final List<Integer> jointIds;
	private final List<Float> vertexWeights;
	private final List<Integer> indices;
	
	public MeshData(List<Vector3f> vertices, List<Vector2f> textureCoords, List<Vector3f> normals, List<Integer> indices, List<Integer> jointIds, List<Float> vertexWeights){
		this.vertices = vertices;
		this.textureCoords = textureCoords;
		this.normals = normals;
		this.indices = indices;
		this.jointIds = jointIds;
		this.vertexWeights = vertexWeights;
	}
	
	public List<Integer> getJointIds(){
		return jointIds;
	}
	
	public List<Float> getVertexWeights(){
		return vertexWeights;
	}
	
	public List<Vector3f> getVertices(){
		return vertices;
	}
	
	public List<Vector2f> getTextureCoords(){
		return textureCoords;
	}
	
	public List<Vector3f> getNormals(){
		return normals;
	}
	
	public List<Integer> getIndices(){
		return indices;
	}
	
	public int getVertexCount(){
		return vertices.size() / DIMENSIONS;
	}
}
