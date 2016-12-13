package net.medox.neonengine.rendering;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.resourceManagement.DynamicMeshData;

public class DynamicMesh{
	private static final Map<Integer, DynamicMeshData> loadedModels = new ConcurrentHashMap<Integer, DynamicMeshData>();
	private static int nextId;
	
	private int id;
	
	private DynamicMeshData resource;
	
	public DynamicMesh(int vertices, int textureCoords, int normals, int tangents, int indicies){
		resource = new DynamicMeshData(vertices, textureCoords, normals, tangents, indicies);
		
		id = nextId;
		
		loadedModels.put(id, resource);
		nextId += 1;
	}
	
	public void cleanUp(){
		if(resource.removeReference()){
			resource.dispose();
			loadedModels.remove(id);
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void updatePositions(List<Vector3f> vertices){
		resource.updatePositions(vertices);
	}
	
	public void updateTexCoords(){
		resource.updateTexCoords();
	}
	
	public void updateNormals(){
		resource.updateNormals();
	}
	
	public void updateTangents(){
		resource.updateTangents();
	}
	
	public void updateIndices(){
		resource.updateIndices();
	}
	
	public void draw(){
		resource.draw();
	}
	
	public static void dispose(){
		for(final DynamicMeshData data : loadedModels.values()){
			data.dispose();
		}
	}
}
