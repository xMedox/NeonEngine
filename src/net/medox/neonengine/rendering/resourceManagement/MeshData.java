package net.medox.neonengine.rendering.resourceManagement;

import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class MeshData extends ReferenceCounter{
	public static final int POSITION_VB = 0;
	public static final int TEXCOORD_VB = 1;
	public static final int NORMAL_VB = 2;
	public static final int TANGENT_VB = 3;
	
	public static final int INDEX_VB = 4;
	
	public static final int NUM_BUFFERS = 5;
	
	private final int vertexArrayObject;
	private final IntBuffer vertexArrayBuffers;
	private final int drawCount;
	
//	private Vector3f minVertex;
//	private Vector3f maxVertex;
	private final float radius;
//	private MeshShape shape;
	
	public MeshData(IndexedModel model/*, boolean create*/){
		if(!model.isValid()){
			System.err.println("Error: Invalid mesh! Must have same number of positions, texCoords, normals, and tangents! (Maybe you forgot to Finalize() your IndexedModel?)");
			System.exit(1);
		}
		
//		if(create){
//			shape = model.createShape();
//		}
		
		radius = model.getRadius();
		
//		model.updateBoundingBox();
//		
//		minVertex = model.getMinVertex();
//		maxVertex = model.getMaxVertex();
		
		vertexArrayBuffers = DataUtil.createIntBuffer(NUM_BUFFERS);
		
		vertexArrayObject = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayObject);
				
		GL15.glGenBuffers(vertexArrayBuffers);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(POSITION_VB));
		final Vector3f[] vertexData = new Vector3f[model.getPositions().size()];
		model.getPositions().toArray(vertexData);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData), GL15.GL_STATIC_DRAW);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TEXCOORD_VB));
		final Vector2f[] vertexData2 = new Vector2f[model.getTexCoords().size()];
		model.getTexCoords().toArray(vertexData2);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData2), GL15.GL_STATIC_DRAW);
		
		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(NORMAL_VB));
		final Vector3f[] vertexData3 = new Vector3f[model.getNormals().size()];
		model.getNormals().toArray(vertexData3);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData3), GL15.GL_STATIC_DRAW);
		
		GL20.glEnableVertexAttribArray(2);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TANGENT_VB));
		final Vector3f[] vertexData4 = new Vector3f[model.getTangents().size()];
		model.getTangents().toArray(vertexData4);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData4), GL15.GL_STATIC_DRAW);
		
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexArrayBuffers.get(INDEX_VB));
		final Integer[] vertexData5 = new Integer[model.getIndices().size()];
		model.getIndices().toArray(vertexData5);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData5), GL15.GL_STATIC_DRAW);
		
		drawCount = model.getIndices().size();
		
		GL30.glBindVertexArray(0);
	}
	
	public void draw(){
//		if(RenderingEngine.meshBound != vertexArrayObject){
			GL30.glBindVertexArray(vertexArrayObject);
			
			if(NeonEngine.PROFILING_DISABLE_MESH_DRAWING == 0){
				GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);
			}
			
//			RenderingEngine.meshBound = vertexArrayObject;
//		}else{
//			if(NeonEngine.PROFILING_DISABLE_MESH_DRAWING == 0){
//				GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);
//			}
//		}
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return camera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max() * /*mesh.getRadius()*/ radius);
	}
	
//	public MeshShape getMeshShape(){
//		return shape;
//	}
	
	public void dispose(){
		GL15.glDeleteBuffers(vertexArrayBuffers);
		GL30.glDeleteVertexArrays(vertexArrayObject);
	}
}
