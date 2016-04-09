package net.medox.neonengine.rendering.meshLoading;

import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

public class IndexedModel{
	private final List<Vector3f> positions;
	private final List<Vector2f> texCoords;
	private final List<Vector3f> normals;
	private final List<Vector3f> tangents;
	private final List<Integer> indices;
	
//	private Vector3f minVertex;
//	private Vector3f maxVertex;
//	private float radius;
	
	public IndexedModel(){
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		tangents = new ArrayList<Vector3f>();
		indices = new ArrayList<Integer>();
		
//		minVertex = new Vector3f(Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
//		maxVertex = new Vector3f(-Float.MAX_VALUE, -Float.MAX_VALUE, -Float.MAX_VALUE);
	}
	
//	public MeshShape createShape(){
//		return new MeshShape(positions);
//	}
	
	public float getRadius(){
		float radiusMax = -Float.MAX_VALUE;
		
		for(int i = 0; i < positions.size(); i++){
			radiusMax = Math.max(radiusMax, positions.get(i).length());
		}
		
		return radiusMax;
	}
	
//	public void updateBoundingBox(){
//		for(int i = 0; i < positions.size(); i++){
//			minVertex.setX(Math.min(minVertex.getX(), positions.get(i).getX()));
//			minVertex.setY(Math.min(minVertex.getY(), positions.get(i).getY()));
//			minVertex.setZ(Math.min(minVertex.getZ(), positions.get(i).getZ()));
//			
//			maxVertex.setX(Math.max(maxVertex.getX(), positions.get(i).getX()));
//			maxVertex.setY(Math.max(maxVertex.getY(), positions.get(i).getY()));
//			maxVertex.setZ(Math.max(maxVertex.getZ(), positions.get(i).getZ()));
//		}
//	}
//	
//	public Vector3f getMinVertex(){
//		return minVertex;
//	}
//	
//	public Vector3f getMaxVertex(){
//		return maxVertex;
//	}
	
	public IndexedModel finalizeModel(){
		if(isValid()){
			return this;
		}
		
		if(texCoords.isEmpty()){
			for(int i = texCoords.size(); i < positions.size(); i++){
				texCoords.add(new Vector2f(0.0f, 0.0f));
			}
		}
		
		if(normals.isEmpty()){
			for(int i = normals.size(); i < positions.size(); i++){
				normals.add(new Vector3f(0.0f, 0.0f, 0.0f));
			}
			calcNormals();
		}
		
		if(tangents.isEmpty()){
			for(int i = tangents.size(); i < positions.size(); i++){
				tangents.add(new Vector3f(0.0f, 0.0f, 0.0f));
			}
			calcTangents();
		}
		
		return this;
	}
	
	public boolean isValid(){
		return positions.size() == texCoords.size() && texCoords.size() == normals.size() && normals.size() == tangents.size();
	}
	
	public void calcNormals(){
		for(int i = 0; i < indices.size(); i += 3){
			final int i0 = indices.get(i);
			final int i1 = indices.get(i + 1);
			final int i2 = indices.get(i + 2);
			
			final Vector3f normal = positions.get(i1).sub(positions.get(i0)).cross(positions.get(i2).sub(positions.get(i0))).normalized();
			
			normals.get(i0).set(normals.get(i0).add(normal));
			normals.get(i1).set(normals.get(i1).add(normal));
			normals.get(i2).set(normals.get(i2).add(normal));
		}
		
		for(int i = 0; i < normals.size(); i++){
			normals.get(i).set(normals.get(i).normalized());
		}
	}
	
	public void calcTangents(){
		for(int i = 0; i < indices.size(); i += 3){
			final int i0 = indices.get(i);
			final int i1 = indices.get(i + 1);
			final int i2 = indices.get(i + 2);
			
			final Vector3f edge1 = positions.get(i1).sub(positions.get(i0));
			final Vector3f edge2 = positions.get(i2).sub(positions.get(i0));
			
			final float deltaV1 = texCoords.get(i1).getY() - texCoords.get(i0).getY();
			final float deltaV2 = texCoords.get(i2).getY() - texCoords.get(i0).getY();
			
			final float dividend = (texCoords.get(i1).getX() - texCoords.get(i0).getX()) * deltaV2 - (texCoords.get(i2).getX() - texCoords.get(i0).getX()) * deltaV1;
			final float f = dividend == 0 ? 0.0f : 1.0f/dividend;
			
			final Vector3f tangent = new Vector3f(0.0f, 0.0f, 0.0f);
			
			tangent.setX(f * (deltaV2 * edge1.getX() - deltaV1 * edge2.getX()));
			tangent.setY(f * (deltaV2 * edge1.getY() - deltaV1 * edge2.getY()));
			tangent.setZ(f * (deltaV2 * edge1.getZ() - deltaV1 * edge2.getZ()));
			
			tangents.get(i0).set(tangents.get(i0).add(tangent));
			tangents.get(i1).set(tangents.get(i1).add(tangent));
			tangents.get(i2).set(tangents.get(i2).add(tangent));
		}
		
		for(int i = 0; i < tangents.size(); i++){
			tangents.get(i).set(tangents.get(i).normalized());
		}
	}
	
	public void addVertex(Vector3f vert){
		positions.add(vert);
	}
	
	public void addVertex(float x, float y, float z){
		addVertex(new Vector3f(x, y, z));
	}
	
	public void addTexCoord(Vector2f texCoord){
		texCoords.add(texCoord);
	}
	
	public void addTexCoord(float x, float y){
		addTexCoord(new Vector2f(x, y));
	}
	
	public void addNormal(Vector3f normal){
		normals.add(normal);
	}
	
	public void addNormal(float x, float y, float z){
		addNormal(new Vector3f(x, y, z));
	}
	
	public void addTangent(Vector3f tangent){
		tangents.add(tangent);
	}
	
	public void addTangent(float x, float y, float z){
		addTangent(new Vector3f(x, y, z));
	}
	
	public void addFace(int vertIndex0, int vertIndex1, int vertIndex2){
		indices.add(vertIndex0);
		indices.add(vertIndex1);
		indices.add(vertIndex2);
	}
	
	public List<Vector3f> getPositions(){
		return positions;
	}

	public List<Vector2f> getTexCoords(){
		return texCoords;
	}

	public List<Vector3f> getNormals(){
		return normals;
	}
	
	public List<Vector3f> getTangents(){
		return tangents;
	}
	
	public List<Integer> getIndices(){
		return indices;
	}
}
