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
			final float vertexMin = -50.0f/2;
			final float vertexMax = 50.0f/2;
			
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
			
			final IndexedModel model = new IndexedModel();
			
			for(int i = 0; i < vertices.length; i++){
				model.addVertex(vertices[i]);
			}
			
			model.addFace(0, 1, 2);
			model.addFace(0, 2, 3);
			
			model.addFace(4, 5, 6);
			model.addFace(4, 6, 7);
			
			model.addFace(8, 9, 10);
			model.addFace(8, 10, 11);
			
			model.addFace(12, 13, 14);
			model.addFace(12, 14, 15);
			
			model.addFace(16, 17, 18);
			model.addFace(16, 18, 19);
			
			model.addFace(20, 21, 22);
			model.addFace(20, 22, 23);
			
			mesh = new Mesh("", model.finalizeModel());
		}
		
		material = new Material();
		material.setCubeMap("cubeMap", new CubeMap(new String[]{right, left, top, bottom, front, back}, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR, RenderingEngine.RGBA, RenderingEngine.RGBA, RenderingEngine.UNSIGNED_BYTE, true));
	}
	
	public void draw(Shader shader, Camera camera){
		transform.setPos(RenderingEngine.getMainCamera().getTransform().getTransformedPos());
		
		shader.bind();
		shader.updateUniforms(transform, material, camera);
		mesh.draw();
	}
}
