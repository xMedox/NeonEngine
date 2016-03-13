package net.medox.neonengine.rendering;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

public class BatchRenderer{
	public BatchRenderer(){
		
	}
	
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, int textureID){
		add2DMesh(shader, camera, trans, textureID, new Vector3f(1, 1, 1));
	}
	
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, int textureID, Vector3f color){
		
	}
	
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, Texture texture){
		add2DMesh(shader, camera, trans, texture, new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, Texture texture, Vector3f color){
		add2DMesh(shader, camera, trans, texture, color, new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public void add2DMesh(Shader shader, Camera camera, Transform2D trans, Texture texture, Vector3f color, Vector2f minUV, Vector2f maxUV){
		
	}
	
	public void addMesh(Shader shader, Camera camera, boolean flipFaces, Transform trans, ParticleMaterial material){
		addMesh(shader, camera, flipFaces, trans, material, new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public void addMesh(Shader shader, Camera camera, boolean flipFaces, Transform trans, ParticleMaterial material, Vector2f minUV, Vector2f maxUV){
		
	}
	
	public void draw(Shader shader, Camera camera){
		
	}
	
	public void dispose(){
		
	}
}
