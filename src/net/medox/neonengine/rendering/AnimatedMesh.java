package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.animation.Joint;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.physics.StaticMeshCollider;
import net.medox.neonengine.rendering.meshLoading.ColladaModel;
import net.medox.neonengine.rendering.resourceManagement.AnimatedMeshData;

public class AnimatedMesh{
	public static final int MAX_WEIGHTS = 4;
	public static final int MAX_JOINTS = 50;
	
	private static final Map<String, AnimatedMeshData> loadedModels = new HashMap<String, AnimatedMeshData>();
//	private static final List<AnimatedMeshData> customModels = new ArrayList<MeshData>();
	
	private final String fileName;
	
	private AnimatedMeshData resource;
	private boolean cleanedUp;
	
	private Joint rootJoint;
	private int jointCount;
	
	public AnimatedMesh(String fileName){
		this.fileName = fileName;
		resource = loadedModels.get(fileName);
		
		if(resource == null){
			loadMesh(fileName);
			loadedModels.put(fileName, resource);
		}else{
			resource.addReference();
		}
	}
	
//	public AnimatedMesh(String meshName, IndexedModel model){
//		this.fileName = meshName;
//		
//		if(fileName.equals("")){
//			model.calcRadius();
//			
//			resource = new MeshData(model);
//			customModels.add(resource);
//		}else{
//			if(loadedModels.get(fileName) == null){
//				model.calcRadius();
//				
//				resource = new MeshData(model);
//				loadedModels.put(fileName, resource);
//			}else{
//				NeonEngine.throwError("Error: the mesh name:" + meshName + "is already in use.");
//			}
//		}
//	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			/*if(fileName.equals("")){
				resource.dispose();
				customModels.remove(resource);
			}else */if(resource.removeReference()){
				resource.dispose();
				loadedModels.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void draw(){
		resource.draw();
	}
	
	public StaticMeshCollider generateCollider(){
		return resource.generateCollider();
	}
	
	private AnimatedMesh loadMesh(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("dae")){
			ColladaModel model = new ColladaModel("./res/models/" + fileName);
			
			rootJoint = model.getRootJoint();
			jointCount = model.getJointCount();
			resource = new AnimatedMeshData(model.toIndexedModel());
		}else{
			NeonEngine.throwError("Error: '" + ext + "' file format not supported for mesh data.");
		}
		
		return this;
	}
	
	public Joint getRootJoint(){
		return rootJoint;
	}
	
	public int getJointCount(){
		return jointCount;
	}
	
	public static void dispose(){
		for(final AnimatedMeshData data : loadedModels.values()){
			data.dispose();
		}
		
//		for(final AnimatedMeshData data : customModels){
//			data.dispose();
//		}
	}
}
