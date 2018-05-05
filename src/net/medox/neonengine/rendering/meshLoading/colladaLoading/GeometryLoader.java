package net.medox.neonengine.rendering.meshLoading.colladaLoading;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.MeshData;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.Vertex;
import net.medox.neonengine.rendering.meshLoading.colladaLoading.data.VertexSkinData;

public class GeometryLoader{
	private static final Matrix4f CORRECTION = new Matrix4f().initIdentity().rotate((float)Math.toRadians(-90), new Vector3f(1, 0, 0));
//	private static final Matrix4f CORRECTION = new Matrix4f().initIdentity().initRotation(90, 0, 0);
	
	private final XmlNode meshData;
	
	private final List<VertexSkinData> vertexWeights;
	
	private List<Vector3f> verticesList;
	private List<Vector2f> texCoordsList;
	private List<Vector3f> normalsList;
	private List<Integer> jointIdsList;
	private List<Float> vertexWeightsList;
	
	private List<Vertex> vertices = new ArrayList<Vertex>();
	private List<Vector2f> textures = new ArrayList<Vector2f>();
	private List<Vector3f> normals = new ArrayList<Vector3f>();
	private List<Integer> indices = new ArrayList<Integer>();
	
	public GeometryLoader(XmlNode geometryNode, List<VertexSkinData> vertexWeights){
		this.vertexWeights = vertexWeights;
		this.meshData = geometryNode.getChild("geometry").getChild("mesh");
	}
	
	public MeshData extractModelData(){
		readRawData();
		assembleVertices();
		removeUnusedVertices();
		
		verticesList = new ArrayList<Vector3f>();
		texCoordsList = new ArrayList<Vector2f>();
		normalsList = new ArrayList<Vector3f>();
		jointIdsList = new ArrayList<Integer>();
		vertexWeightsList = new ArrayList<Float>();
		
		for(int i = 0; i < vertices.size(); i++){
			Vertex currentVertex = vertices.get(i);
			Vector3f position = currentVertex.getPosition();
			Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
			Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
			verticesList.add(position);
			texCoordsList.add(new Vector2f(textureCoord.getX(), 1.0f - textureCoord.getY()));
			normalsList.add(normalVector);
			VertexSkinData weights = currentVertex.getWeightsData();
			jointIdsList.add(weights.jointIds.get(0));
			jointIdsList.add(weights.jointIds.get(1));
			jointIdsList.add(weights.jointIds.get(2));
			jointIdsList.add(weights.jointIds.get(3));
			vertexWeightsList.add(weights.weights.get(0));
			vertexWeightsList.add(weights.weights.get(1));
			vertexWeightsList.add(weights.weights.get(2));
			vertexWeightsList.add(weights.weights.get(3));
		}
		
		return new MeshData(verticesList, texCoordsList, normalsList, indices, jointIdsList, vertexWeightsList);
	}
	
	private void readRawData(){
		readPositions();
		readNormals();
		readTextureCoords();
	}
	
	private void readPositions(){
		String positionsId = meshData.getChild("vertices").getChild("input").getAttribute("source").substring(1);
		XmlNode positionsData = meshData.getChildWithAttribute("source", "id", positionsId).getChild("float_array");
		int count = Integer.parseInt(positionsData.getAttribute("count"));
		String[] posData = positionsData.getData().split(" ");
		for(int i = 0; i < count/3; i++){
			float x = Float.parseFloat(posData[i * 3]);
			float y = Float.parseFloat(posData[i * 3 + 1]);
			float z = Float.parseFloat(posData[i * 3 + 2]);
			Quaternion position = new Quaternion(x, y, z, 1);
			position = Matrix4f.transform(CORRECTION, position);
			vertices.add(new Vertex(vertices.size(), new Vector3f(position.getX(), position.getY(), position.getZ()), vertexWeights.get(vertices.size())));
		}
	}
	
	private void readNormals(){
		String normalsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "NORMAL").getAttribute("source").substring(1);
		XmlNode normalsData = meshData.getChildWithAttribute("source", "id", normalsId).getChild("float_array");
		int count = Integer.parseInt(normalsData.getAttribute("count"));
		String[] normData = normalsData.getData().split(" ");
		for(int i = 0; i < count/3; i++){
			float x = Float.parseFloat(normData[i * 3]);
			float y = Float.parseFloat(normData[i * 3 + 1]);
			float z = Float.parseFloat(normData[i * 3 + 2]);
			Quaternion norm = new Quaternion(x, y, z, 0f);
			norm = Matrix4f.transform(CORRECTION, norm);
			normals.add(new Vector3f(norm.getX(), norm.getY(), norm.getZ()));
		}
	}
	
	private void readTextureCoords(){
		String texCoordsId = meshData.getChild("polylist").getChildWithAttribute("input", "semantic", "TEXCOORD").getAttribute("source").substring(1);
		XmlNode texCoordsData = meshData.getChildWithAttribute("source", "id", texCoordsId).getChild("float_array");
		int count = Integer.parseInt(texCoordsData.getAttribute("count"));
		String[] texData = texCoordsData.getData().split(" ");
		for(int i = 0; i < count/2; i++){
			float s = Float.parseFloat(texData[i * 2]);
			float t = Float.parseFloat(texData[i * 2 + 1]);
			textures.add(new Vector2f(s, t));
		}
	}
	
	private void assembleVertices(){
		XmlNode poly = meshData.getChild("polylist");
		int typeCount = poly.getChildren("input").size();
		String[] indexData = poly.getChild("p").getData().split(" ");
		for(int i=0;i<indexData.length/typeCount;i++){
			int positionIndex = Integer.parseInt(indexData[i * typeCount]);
			int normalIndex = Integer.parseInt(indexData[i * typeCount + 1]);
			int texCoordIndex = Integer.parseInt(indexData[i * typeCount + 2]);
			processVertex(positionIndex, normalIndex, texCoordIndex);
		}
	}
	
	private Vertex processVertex(int posIndex, int normIndex, int texIndex) {
		Vertex currentVertex = vertices.get(posIndex);
		if(!currentVertex.isSet()){
			currentVertex.setTextureIndex(texIndex);
			currentVertex.setNormalIndex(normIndex);
			indices.add(posIndex);
			return currentVertex;
		}else{
			return dealWithAlreadyProcessedVertex(currentVertex, texIndex, normIndex);
		}
	}
	
	private Vertex dealWithAlreadyProcessedVertex(Vertex previousVertex, int newTextureIndex, int newNormalIndex){
		if(previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)){
			indices.add(previousVertex.getIndex());
			return previousVertex;
		}else{
			Vertex anotherVertex = previousVertex.getDuplicateVertex();
			if(anotherVertex != null){
				return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex);
			}else{
				Vertex duplicateVertex = new Vertex(vertices.size(), previousVertex.getPosition(), previousVertex.getWeightsData());
				duplicateVertex.setTextureIndex(newTextureIndex);
				duplicateVertex.setNormalIndex(newNormalIndex);
				previousVertex.setDuplicateVertex(duplicateVertex);
				vertices.add(duplicateVertex);
				indices.add(duplicateVertex.getIndex());
				return duplicateVertex;
			}
		}
	}
	
	private void removeUnusedVertices(){
		for(Vertex vertex : vertices){
			vertex.averageTangents();
			if(!vertex.isSet()){
				vertex.setTextureIndex(0);
				vertex.setNormalIndex(0);
			}
		}
	}
}
