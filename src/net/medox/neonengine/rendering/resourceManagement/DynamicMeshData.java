package net.medox.neonengine.rendering.resourceManagement;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;

public class DynamicMeshData extends ReferenceCounter{
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
	
	public DynamicMeshData(int vertices, int textureCoords, int normals, int tangents, int indicies){
		radius = 100000000;
		
		vertexArrayBuffers = DataUtil.createIntBuffer(NUM_BUFFERS);
		
		vertexArrayObject = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayObject);
		
		GL15.glGenBuffers(vertexArrayBuffers);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(POSITION_VB));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices*4*4, GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TEXCOORD_VB));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, textureCoords*4*4, GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(NORMAL_VB));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normals*4*4, GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(2);
		GL20.glVertexAttribPointer(2, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TANGENT_VB));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, tangents*4*4, GL15.GL_DYNAMIC_DRAW);
		
//		GL20.glEnableVertexAttribArray(3);
//		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);
//		
//		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexArrayBuffers.get(INDEX_VB));
//		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData5), GL15.GL_DYNAMIC_DRAW);
		
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);
		
		
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexArrayBuffers.get(INDEX_VB));
		
		
		final ArrayList<Integer> indices = new ArrayList<Integer>();
		
		int offset = 0;
		for (int i = 0; i < vertices/4; i += 1){
			indices.add(offset + 0);
			indices.add(offset + 1);
			indices.add(offset + 3);
			
			indices.add(offset + 0);
			indices.add(offset + 3);
			indices.add(offset + 2);
			
			System.out.println(offset + 1);
			System.out.println(offset + 0);
			System.out.println(offset + 2);
			
			System.out.println(offset + 3);
			System.out.println(offset + 1);
			System.out.println(offset + 2);
			
			offset += 4;
			
//			indices.add(i);
		}
		
		
		final Integer[] vertexData = new Integer[indices.size()];
		indices.toArray(vertexData);
		
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData), GL15.GL_STATIC_DRAW);
		
		drawCount = indicies;
		
		GL30.glBindVertexArray(0);
	}
	
	public void draw(){
//		if(RenderingEngine.meshBound != vertexArrayObject){
			GL30.glBindVertexArray(vertexArrayObject);
			
			if(!NeonEngine.isMeshDrawingDisabled()){
				GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);
			}
			
//			RenderingEngine.meshBound = vertexArrayObject;
//		}else{
//			if(!NeonEngine.isMeshDrawingDisabled()){
//				GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);
//			}
//		}
	}
	
	public void updatePositions(List<Vector3f> vertices){
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(POSITION_VB));
		
		final Vector3f[] vertexData = new Vector3f[vertices.size()];
		vertices.toArray(vertexData);
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, DataUtil.createFlippedBuffer(vertexData));
	}
	
	public void updateTexCoords(){
		
	}
	
	public void updateNormals(){
		
	}
	
	public void updateTangents(){
		
	}
	
	public void updateIndices(){
		
	}
	
	public boolean inFrustum(Transform transform, Camera camera){
		return camera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max() * /*mesh.getRadius()*/ radius);
	}
	
	public void dispose(){
		GL15.glDeleteBuffers(vertexArrayBuffers);
		GL30.glDeleteVertexArrays(vertexArrayObject);
	}
}
