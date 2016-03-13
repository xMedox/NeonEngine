package net.medox.neonengine.rendering;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class Skybox extends EntityComponent{	
	private Mesh mesh;
	private Material material;
	
	public Skybox(String right, String left, String top, String bottom, String front, String back){
		this(right, left, top, bottom, front, back, false);
	}
	
	public Skybox(String right, String left, String top, String bottom, String front, String back, boolean nearest){
		float size = 50.0f;
		
		material = new Material();
		
		material.setCubeMap("cubeMap", new CubeMap(new String[]{right, left, top, bottom, front, back}, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR, RenderingEngine.RGBA, RenderingEngine.RGBA, true));
		
		final float vertexMin = -size/2-1f;
		final float vertexMax = size/2+1f;
		
		final Vector3f[] vertices = new Vector3f[]{new Vector3f(vertexMin, vertexMax, vertexMin),
												   new Vector3f(vertexMin, vertexMax, vertexMax),
												   new Vector3f(vertexMin, vertexMin, vertexMax),
												   new Vector3f(vertexMin, vertexMin, vertexMin),
												   new Vector3f(vertexMax, vertexMax, vertexMin),
												   new Vector3f(vertexMin, vertexMax, vertexMin),
												   new Vector3f(vertexMin, vertexMin, vertexMin),
												   new Vector3f(vertexMax, vertexMin, vertexMin),
												   new Vector3f(vertexMax, vertexMax, vertexMax),
												   new Vector3f(vertexMax, vertexMax, vertexMin),
												   new Vector3f(vertexMax, vertexMin, vertexMin),
												   new Vector3f(vertexMax, vertexMin, vertexMax),
												   new Vector3f(vertexMin, vertexMax, vertexMax),
												   new Vector3f(vertexMax, vertexMax, vertexMax),
												   new Vector3f(vertexMax, vertexMin, vertexMax),
												   new Vector3f(vertexMin, vertexMin, vertexMax),
												   new Vector3f(vertexMin, vertexMax, vertexMin),
												   new Vector3f(vertexMax, vertexMax, vertexMin),
												   new Vector3f(vertexMax, vertexMax, vertexMax),
												   new Vector3f(vertexMin, vertexMax, vertexMax),
												   new Vector3f(vertexMax, vertexMin, vertexMin),
												   new Vector3f(vertexMin, vertexMin, vertexMin),
												   new Vector3f(vertexMin, vertexMin, vertexMax),
												   new Vector3f(vertexMax, vertexMin, vertexMax)};
		
		mesh = getMesh(vertices);
		
		RenderingEngine.setMainSkybox(this);
	}
	
	public Mesh getMesh(Vector3f[] vertices){
		final IndexedModel mesh = new IndexedModel();
		
		for(int i = 0; i < vertices.length; i++){
			mesh.addVertex(vertices[i]);
		}
		
		mesh.addFace(0, 1, 2);
		mesh.addFace(0, 2, 3);
		
		mesh.addFace(4, 5, 6);
		mesh.addFace(4, 6, 7);
		
		mesh.addFace(8, 9, 10);
		mesh.addFace(8, 10, 11);
		
		mesh.addFace(12, 13, 14);
		mesh.addFace(12, 14, 15);
		
		mesh.addFace(16, 17, 18);
		mesh.addFace(16, 18, 19);
		
		mesh.addFace(20, 21, 22);
		mesh.addFace(20, 22, 23);
				
		return new Mesh("", mesh.finalizeModel());
	}
	 
	public void draw(Shader shader, Camera camera){
		shader.bind();
		shader.updateUniforms(getTransform(), material, camera);
		mesh.draw();
	}
}
