package net.medox.neonengine.rendering;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;
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
				NeonEngine.throwError("Error: the mesh name:" + meshName + "is already in use");
			}
		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return resource.inFrustum(transform, camera);
	}
	
//	public MeshShape getMeshShape(){
//		return resource.getMeshShape();
//	}
	
	public void cleanUp(){
		if(fileName.equals("")){
			resource.dispose();
			customModels.remove(resource);
		}else{
			if(resource.removeReference()){
				resource.dispose();
				loadedModels.remove(fileName);
			}
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
	
	private Mesh loadMesh(String fileName/*, boolean createShape*/){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("obj")){
			final AIScene scene = Assimp.aiImportFile(("./res/models/" + fileName), 
                    Assimp.aiProcess_Triangulate |
                    Assimp.aiProcess_GenSmoothNormals |
                    Assimp.aiProcess_FlipUVs |
                    Assimp.aiProcess_CalcTangentSpace);
			
			if(scene == null){
				NeonEngine.throwError("Error: Mesh load failed!: " + fileName);
			}
			
			final AIMesh model = AIMesh.create(scene.mMeshes().get(0))/*scene.mMeshes().get(0)*/;//*scene.mMeshes[0]*/;
			
			final List<Vector3f> positions = new ArrayList<Vector3f>();
			final List<Vector2f> texCoords = new ArrayList<Vector2f>();
			final List<Vector3f> normals = new ArrayList<Vector3f>();
			final List<Vector3f> tangents = new ArrayList<Vector3f>();
			final List<Integer> indices = new ArrayList<Integer>();
			
//			final AIVector3D aiZeroVector/* = (0.0f, 0.0f, 0.0f)*/;
			
			for(int i = 0; i < model.mNumVertices(); i++){
				final AIVector3D pos = model.mVertices().get(i);
				final AIVector3D normal = model.mNormals().get(i);
				final AIVector3D texCoord = /*model.HasTextureCoords(0) ? */model.mTextureCoords(0).get(i)/* : aiZeroVector*/;
				final AIVector3D tangent = model.mTangents().get(i);
				
				positions.add(new Vector3f(pos.x(), pos.y(), pos.z()));
				texCoords.add(new Vector2f(texCoord.x(), texCoord.y()));
				normals.add(new Vector3f(normal.x(), normal.y(), normal.z()));
				tangents.add(new Vector3f(tangent.x(), tangent.y(), tangent.z()));
				
//				pos.free();
//				normal.free();
//				texCoord.free();
//				tangent.free();
			}
			
			for(int i = 0; i < model.mNumFaces(); i++){
				final AIFace face = model.mFaces().get(i);
				
				indices.add(face.mIndices().get(0));
				indices.add(face.mIndices().get(1));
				indices.add(face.mIndices().get(2));
				
//				face.free();
			}
			
			final IndexedModel indexedModel = new IndexedModel(indices, positions, texCoords, normals, tangents);
			indexedModel.calcRadius();
			
			resource = new MeshData(indexedModel);
			
//			model.free();
//			
//			Assimp.aiReleaseImport(scene);
//			scene.free();
			
//			resource = new MeshData(new OBJModel("./res/models/" + fileName).toIndexedModel()/*, createShape*/);
		}else{
			NeonEngine.throwError("Error: '" + ext + "' file format not supported for mesh data.");
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
