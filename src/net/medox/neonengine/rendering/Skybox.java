package net.medox.neonengine.rendering;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class Skybox extends EntityComponent{	
	private Mesh mesh;
	private Material material;
	
	private float size;
	
	public Skybox(String right, String left, String top, String bottom, String front, String back){
		this(right, left, top, bottom, front, back, false);
	}
	
	public Skybox(String right, String left, String top, String bottom, String front, String back, boolean nearest){
		this.size = 50.0f;
		
		material = new Material();
		
		String[] s = new String[6];
		
		s[0] = right;
		s[1] = left;
		s[2] = top;
		s[3] = bottom;
		s[4] = front;
		s[5] = back;
		
		final CubeMap cube = new CubeMap(s, RenderingEngine.TEXTURE_2D, nearest ? RenderingEngine.NEAREST : RenderingEngine.LINEAR, RenderingEngine.RGBA, RenderingEngine.RGBA, true);
		
		material.setCubeMap("cubeMap", cube);
		
		final float vm = -size/2-1f;
		final float vp = size/2+1f;
		
		final Vector3f[] v = new Vector3f[]{new Vector3f(vm, vp, vm),
								   new Vector3f(vm, vp, vp),
								   new Vector3f(vm, vm, vp),
								   new Vector3f(vm, vm, vm),
								   new Vector3f(vp, vp, vm),
								   new Vector3f(vm, vp, vm),
								   new Vector3f(vm, vm, vm),
								   new Vector3f(vp, vm, vm),
								   new Vector3f(vp, vp, vp),
					  			   new Vector3f(vp, vp, vm),
					  			   new Vector3f(vp, vm, vm),
					  			   new Vector3f(vp, vm, vp),
					  			   new Vector3f(vm, vp, vp),
								   new Vector3f(vp, vp, vp),
								   new Vector3f(vp, vm, vp),
								   new Vector3f(vm, vm, vp),
								   new Vector3f(vm, vp, vm),
								   new Vector3f(vp, vp, vm),
								   new Vector3f(vp, vp, vp),
								   new Vector3f(vm, vp, vp),
								   new Vector3f(vp, vm, vm),
								   new Vector3f(vm, vm, vm),
								   new Vector3f(vm, vm, vp),
								   new Vector3f(vp, vm, vp)};
		
		mesh = getMesh(v);
		
		RenderingEngine.setMainSkybox(this);
	}
	
	public Mesh getMesh(Vector3f[] vertices){
		final IndexedModel mesh = new IndexedModel();
		
		for(int i = 0; i < vertices.length; i++){
			mesh.addVertex(vertices[i]);
		}
		
		mesh.addFace(0, 1, 2);
		mesh.addFace(0, 2, 3);
		
		mesh.addFace(0+4, 1+4, 2+4);
		mesh.addFace(0+4, 2+4, 3+4);
		
		mesh.addFace(0+8, 1+8, 2+8);
		mesh.addFace(0+8, 2+8, 3+8);
		
		mesh.addFace(0+12, 1+12, 2+12);
		mesh.addFace(0+12, 2+12, 3+12);
		
		mesh.addFace(0+16, 1+16, 2+16);
		mesh.addFace(0+16, 2+16, 3+16);
		
		mesh.addFace(0+20, 1+20, 2+20);
		mesh.addFace(0+20, 2+20, 3+20);
				
		return new Mesh("", mesh.finalizeModel());
	}
	 
	public void draw(Shader shader, Camera camera){
		shader.bind();
		shader.updateUniforms(getTransform(), material, camera);
		mesh.draw();
	}
}
