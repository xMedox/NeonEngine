package net.medox.neonengine.rendering;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class Skybox{
	private static final Transform transform = new Transform();
	
	private static Mesh mesh;
	
	private final Material material;
	
	public Skybox(String right, String left, String top, String bottom, String front, String back){
		this(right, left, top, bottom, front, back, false);
	}
	
	public Skybox(String right, String left, String top, String bottom, String front, String back, boolean nearest){
		if(mesh == null){
			final float vertexMin = -0.5f;
			final float vertexMax = 0.5f;
			
			final Vector3f[] vertices = new Vector3f[]{new Vector3f(vertexMin, vertexMax, vertexMax),
														new Vector3f(vertexMin, vertexMin, vertexMax),
														new Vector3f(vertexMax, vertexMax, vertexMin),
														new Vector3f(vertexMin, vertexMax, vertexMin),
														new Vector3f(vertexMin, vertexMin, vertexMin),
														new Vector3f(vertexMax, vertexMin, vertexMin),
														new Vector3f(vertexMax, vertexMax, vertexMax),
														new Vector3f(vertexMax, vertexMin, vertexMax)};
			
			final IndexedModel model = new IndexedModel();
			
			for(int i = 0; i < vertices.length; i++){
				model.addVertex(vertices[i]);
			}
			
			model.addFace(2, 3, 4);
			model.addFace(2, 4, 5);
			
			model.addFace(3, 0, 1);
			model.addFace(3, 1, 4);
			
			model.addFace(6, 2, 5);
			model.addFace(6, 5, 7);
			
			model.addFace(0, 6, 7);
			model.addFace(0, 7, 1);
			
			model.addFace(3, 2, 6);
			model.addFace(3, 6, 0);
			
			model.addFace(5, 4, 1);
			model.addFace(5, 1, 7);
			
			mesh = new Mesh("", model.finalizeModel());
		}
		
		material = new Material();
		material.setCubeMap("cubeMap", new CubeMap(new String[]{right, left, top, bottom, front, back}, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR, RenderingEngine.RGBA, RenderingEngine.RGBA, RenderingEngine.UNSIGNED_BYTE, true));
	}
	
	public void render(Shader shader, Camera camera){
		transform.setPos(RenderingEngine.getMainCamera().getTransform().getTransformedPos());
		
		shader.bind();
		shader.updateUniforms(transform, material, camera);
		mesh.draw();
	}
}
