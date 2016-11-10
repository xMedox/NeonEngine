package net.medox.neonengine.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;
import net.medox.neonengine.rendering.meshLoading.OBJModel;
import net.medox.neonengine.rendering.resourceManagement.MeshData;

public class Mesh{
	private static final Map<String, MeshData> loadedModels = new ConcurrentHashMap<String, MeshData>();
	
	private static final List<MeshData> customModels = new ArrayList<MeshData>();
	
	private final String fileName;
	
	private MeshData resource;
	
	public Mesh(String fileName/*, boolean createShape*/){
		this.fileName = fileName;
		resource = loadedModels.get(fileName);
		
		if(resource == null){
			loadMesh(fileName/*, createShape*/);
			loadedModels.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	
//	public Mesh(String meshName, IndexedModel model){
//		this(meshName, model, false);
//	}
	
	public Mesh(String meshName, IndexedModel model/*, boolean createShape*/){
		this.fileName = meshName;
		
		if(fileName.equals("")){
			model.calcRadius();
			
			resource = new MeshData(model/*, createShape*/);
			customModels.add(resource);
		}else{
			if(loadedModels.get(fileName) == null){
				model.calcRadius();
				
				resource = new MeshData(model/*, createShape*/);
				loadedModels.put(fileName, resource);
			}else{
				System.err.println("Error adding mesh " + meshName + ": Mesh already exists by the same name!");
				System.exit(1);
			}
		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
//	public MeshShape getMeshShape(){
//		return resource.getMeshShape();
//	}
	
	@Override
	protected void finalize() throws Throwable{
		if(fileName.equals("")){
			resource.dispose();
			customModels.remove(resource);
		}else{
			if(resource.removeReference()){
				resource.dispose();
				loadedModels.remove(fileName);
			}
		}
		
		super.finalize();
	}
	
	public void draw(){
		resource.draw();
	}
	
	private Mesh loadMesh(String fileName/*, boolean createShape*/){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("obj")){
			resource = new MeshData(new OBJModel("./res/models/" + fileName).toIndexedModel()/*, createShape*/);
		}else{
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		return this;
	}
	
	public static void dispose(){
		for(final MeshData data : loadedModels.values()){
			data.dispose();
		}
		
		for(final MeshData data : customModels){
			data.dispose();
		}
	}
}
