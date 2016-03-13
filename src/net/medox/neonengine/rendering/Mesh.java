package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;
import net.medox.neonengine.rendering.meshLoading.OBJModel;
import net.medox.neonengine.rendering.resourceManagement.MeshData;
import net.medox.neonengine.rendering.resourceManagement.OpenGL.MeshDataGL;

public class Mesh{
	private static Map<String, MeshData> loadedModels = new HashMap<String, MeshData>();
	
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
		
		if(!fileName.equals("")){
			if(loadedModels.get(fileName) == null){
				if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
					resource = new MeshDataGL(model/*, createShape*/);
				}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
					resource = new MeshData(model/*, createShape*/);
				}
				loadedModels.put(fileName, resource);
			}else{
				System.err.println("Error adding mesh " + meshName + ": Mesh already exists by the same name!");
				System.exit(1);
			}
		}else{
			if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
				resource = new MeshDataGL(model/*, createShape*/);
			}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
				resource = new MeshData(model/*, createShape*/);
			}
			loadedModels.put(fileName, resource);
		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
//	public MeshShape getMeshShape(){
//		return resource.getMeshShape();
//	}
	
	@Override
	protected void finalize(){
		if(resource.removeReference() && !fileName.isEmpty()){
			resource.dispose();
			loadedModels.remove(fileName);
		}
	}
	
	public void draw(){
		resource.draw();
	}
	
	private Mesh loadMesh(String fileName/*, boolean createShape*/){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(!ext.equals("obj")){
			System.err.println("Error: '" + ext + "' file format not supported for mesh data.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
//		OBJModel test = new OBJModel("./res/models/" + fileName);
//		IndexedModel model = test.toIndexedModel();
		
//		model.calcNormals();
//		model.calcTangents();

//		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
//
//		for(int i = 0; i < model.getPositions().size(); i++){
//			vertices.add(new Vertex(model.getPositions().get(i),
//					model.getTexCoords().get(i),
//					model.getNormals().get(i),
//					model.getTangents().get(i)));
//		}
//
//		Vertex[] vertexData = new Vertex[vertices.size()];
//		vertices.toArray(vertexData);
//
//		Integer[] indexData = new Integer[model.getIndices().size()];
//		model.getIndices().toArray(indexData);

//		addVertices(vertexData, Util.toIntArray(indexData), false);
//		resource = new MeshData(model/*, createShape*/);
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			resource = new MeshDataGL(new OBJModel("./res/models/" + fileName).toIndexedModel()/*, createShape*/);
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			resource = new MeshData(new OBJModel("./res/models/" + fileName).toIndexedModel()/*, createShape*/);
		}

		return this;
	}
	
	public static void dispose(){
		for(final MeshData data : loadedModels.values()){
			data.dispose();
		}
	}
}
