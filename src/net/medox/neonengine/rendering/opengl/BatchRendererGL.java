package net.medox.neonengine.rendering.opengl;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.BatchRenderer;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.DataUtil;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.ParticleMaterial;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class BatchRendererGL extends BatchRenderer{
	private static final int RENDERER_MAX_SPRITES = 1000;
	private static final int RENDERER_MAX_TEXTURES = 32;
	
	private static final int VERTEX_INDEX = 0;
	private static final int UV_INDEX = 1;
	private static final int TID_INDEX = 2;
	private static final int COLOR_INDEX = 3;
	private static final int INDEX_INDEX = 4;
	
	private static final int NUM_BUFFERS = 5;
	
	private static final Transform TRANSFORM = new Transform();
	private static final Material MATERIAL = new Material();
	
	private final List<Vector3f> vertices;
	private final List<Vector2f> uvs;
	private final List<Integer> texturesids;
	private final List<TextureSlot> textures;
	private final List<Vector3f> colors;
	
	private final int vertexArrayObject;
	private final IntBuffer vertexArrayBuffers;
	
	private int indexCount;
	
	public BatchRendererGL(){
		vertices = new ArrayList<Vector3f>();
		uvs = new ArrayList<Vector2f>();
		texturesids = new ArrayList<Integer>();
		textures = new ArrayList<TextureSlot>();
		colors = new ArrayList<Vector3f>();
		
		vertexArrayBuffers = BufferUtils.createIntBuffer(NUM_BUFFERS);
		
		vertexArrayObject = GL30.glGenVertexArrays();
		GL30.glBindVertexArray(vertexArrayObject);
				
		GL15.glGenBuffers(vertexArrayBuffers);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(VERTEX_INDEX));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RENDERER_MAX_SPRITES*3*4*4, GL15.GL_DYNAMIC_DRAW);
		
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(UV_INDEX));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RENDERER_MAX_SPRITES*2*4*4, GL15.GL_DYNAMIC_DRAW);
		
		
		GL20.glEnableVertexAttribArray(1);
		GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, 0, 0);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TID_INDEX));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RENDERER_MAX_SPRITES*4*4, GL15.GL_DYNAMIC_DRAW);
		
		
		GL20.glEnableVertexAttribArray(2);
		GL20.glVertexAttribPointer(2, 1, GL11.GL_FLOAT, false, 0, 0);
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(COLOR_INDEX));
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, RENDERER_MAX_SPRITES*3*4*4, GL15.GL_DYNAMIC_DRAW);
		
		
		GL20.glEnableVertexAttribArray(3);
		GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, 0, 0);
		
		
		
		
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vertexArrayBuffers.get(INDEX_INDEX));
		
		
		final ArrayList<Integer> indices = new ArrayList<Integer>();
		
		int offset = 0;
		for (int i = 0; i < RENDERER_MAX_SPRITES*6; i += 6){
			indices.add(offset + 1);
			indices.add(offset + 0);
			indices.add(offset + 2);
			
			indices.add(offset + 3);
			indices.add(offset + 1);
			indices.add(offset + 2);
			
			offset += 4;
		}
		
		
		final Integer[] vertexData = new Integer[indices.size()];
		indices.toArray(vertexData);
		
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, DataUtil.createFlippedBuffer(vertexData), GL15.GL_STATIC_DRAW);
		
		GL30.glBindVertexArray(0);
	}
	
	@Override
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, int textureID, Vector3f color){
		if(texturesids.size() == RENDERER_MAX_SPRITES){
			draw(shader, camera);
		}
		
		texturesids.add(textureID);
		
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX() + (int)trans.getScale().getX(), (int)trans.getTransformedPos().getY() + (int)trans.getScale().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX(), (int)trans.getTransformedPos().getY() + (int)trans.getScale().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX() + (int)trans.getScale().getX(), (int)trans.getTransformedPos().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX(), (int)trans.getTransformedPos().getY(), 0.5f));
		
		uvs.add(new Vector2f(1, 0));
		uvs.add(new Vector2f(0, 0));
		uvs.add(new Vector2f(1, 1));
		uvs.add(new Vector2f(0, 1));
		
		colors.add(color);
		
		indexCount += 6;
	}
	
	@Override
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, Texture texture, Vector3f color, Vector2f minUV, Vector2f maxUV){
		if(texturesids.size() == RENDERER_MAX_SPRITES){
			draw(shader, camera);
		}
		
		int id = -1;
		
		for(int i = 0; i < textures.size(); i++){
			if(textures.get(i).texture.getID() == texture.getID()){
				id = textures.get(i).id;
				
				texturesids.add(id);
			}
		}
		
		if(id == -1){
			id = textures.size();
			
			if(id < RENDERER_MAX_TEXTURES){
				texturesids.add(id);
				
				textures.add(new TextureSlot(texture, id));
			}else{
				draw(shader, camera);
				
				texturesids.add(0);
				
				textures.add(new TextureSlot(texture, 0));
			}
		}
		
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX() + (int)trans.getScale().getX(), (int)trans.getTransformedPos().getY() + (int)trans.getScale().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX(), (int)trans.getTransformedPos().getY() + (int)trans.getScale().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX() + (int)trans.getScale().getX(), (int)trans.getTransformedPos().getY(), 0.5f));
		vertices.add(new Vector3f((int)trans.getTransformedPos().getX(), (int)trans.getTransformedPos().getY(), 0.5f));
		
		uvs.add(new Vector2f(maxUV.getX(), minUV.getY()));
		uvs.add(new Vector2f(minUV.getX(), minUV.getY()));
		uvs.add(new Vector2f(maxUV.getX(), maxUV.getY()));
		uvs.add(new Vector2f(minUV.getX(), maxUV.getY()));
		
		colors.add(color);
		
		indexCount += 6;
	}
	
	@Override
	public void addMesh(Shader shader, Camera camera, boolean flipFaces, Transform trans, ParticleMaterial material, Vector2f minUV, Vector2f maxUV){
		final Texture texture = material.getTexture("diffuseMap");
//		float specularIntensity = material.getFloat("specularIntensity");
//		float specularPower = material.getFloat("specularPower");
//		float glow = material.getFloat("glow");
		
		if(texturesids.size() == RENDERER_MAX_SPRITES){
			draw(shader, camera);
		}
		
		int id = -1;
		
		for(int i = 0; i < textures.size(); i++){
			if(textures.get(i).texture.getID() == texture.getID()){
				id = textures.get(i).id;
				
				texturesids.add(id);
			}
		}
		
		if(id == -1){
			id = textures.size();
			
			if(id < RENDERER_MAX_TEXTURES){
				texturesids.add(id);
				
				textures.add(new TextureSlot(texture, id));
			}else{
				draw(shader, camera);
				
				texturesids.add(0);
				
				textures.add(new TextureSlot(texture, 0));
			}
		}
		
		final Vector3f plusRight = camera.getTransform().getTransformedRot().getRight().mul(trans.getScale().mul(0.5f));
		final Vector3f plusUp = camera.getTransform().getTransformedRot().getUp().mul(trans.getScale().mul(0.5f));
		
		if(flipFaces){
			vertices.add(trans.getTransformedPos().sub(plusRight).sub(plusUp));
			vertices.add(trans.getTransformedPos().sub(plusRight).add(plusUp));
			vertices.add(trans.getTransformedPos().add(plusRight).sub(plusUp));
			vertices.add(trans.getTransformedPos().add(plusRight).add(plusUp));
		}else{
			vertices.add(trans.getTransformedPos().add(plusRight).add(plusUp));
			vertices.add(trans.getTransformedPos().sub(plusRight).add(plusUp));
			vertices.add(trans.getTransformedPos().add(plusRight).sub(plusUp));
			vertices.add(trans.getTransformedPos().sub(plusRight).sub(plusUp));
		}
		
		if(flipFaces){
			uvs.add(new Vector2f(maxUV.getX(), maxUV.getY()));
			uvs.add(new Vector2f(maxUV.getX(), minUV.getY()));
			uvs.add(new Vector2f(minUV.getX(), maxUV.getY()));
			uvs.add(new Vector2f(minUV.getX(), minUV.getY()));
		}else{
			uvs.add(new Vector2f(maxUV.getX(), minUV.getY()));
			uvs.add(new Vector2f(minUV.getX(), minUV.getY()));
			uvs.add(new Vector2f(maxUV.getX(), maxUV.getY()));
			uvs.add(new Vector2f(minUV.getX(), maxUV.getY()));
		}
		
		colors.add(new Vector3f(material.getFloat("specularIntensity"), material.getFloat("specularPower"), material.getFloat("emissive")));
		
		indexCount += 6;
	}
	
	@Override
	public void draw(Shader shader, Camera camera){
		shader.bind();
		shader.updateUniforms(TRANSFORM, MATERIAL, camera);
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(VERTEX_INDEX));
		
		final Vector3f[] vertexData = new Vector3f[vertices.size()];
		vertices.toArray(vertexData);
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, DataUtil.createFlippedBuffer(vertexData));
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(UV_INDEX));
		
		final Vector2f[] vertexData2 = new Vector2f[uvs.size()];
		uvs.toArray(vertexData2);
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, DataUtil.createFlippedBuffer(vertexData2));
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(TID_INDEX));
		
		final Integer[] vertexData3 = new Integer[texturesids.size()];
		texturesids.toArray(vertexData3);
		
		
		for(int i = 0; i < textures.size(); i++){
			textures.get(i).texture.bind(i);
		}
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, DataUtil.createFlippedBufferTimes3(vertexData3));
		
		
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexArrayBuffers.get(COLOR_INDEX));
		
		final Vector3f[] vertexData4 = new Vector3f[colors.size()];
		colors.toArray(vertexData4);
		
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, DataUtil.createFlippedBufferTimes3(vertexData4));
		
		
		GL30.glBindVertexArray(vertexArrayObject);
		
		if(NeonEngine.PROFILING_DISABLE_MESH_DRAWING == 0){
			GL11.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_INT, 0);
		}
		
		vertices.clear();
		uvs.clear();
		textures.clear();
		texturesids.clear();
		colors.clear();
		
		indexCount = 0;
	}
	
	@Override
	public void dispose(){
		GL15.glDeleteBuffers(vertexArrayBuffers);
		GL30.glDeleteVertexArrays(vertexArrayObject);
	}
	
	private class TextureSlot{
		public Texture texture;
		public int id;
		
		public TextureSlot(Texture texture, int id){
			this.texture = texture;
			this.id = id;
		}
	}
}
