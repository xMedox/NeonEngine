package net.medox.propeditor;

import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class Template{
	private static Mesh mesh;
	private static Material material;
	
	private Transform transform;
	
	private boolean visable;
	
	public Template(){
		mesh = new Mesh("template.obj");
		material = new Material();
		material.setTexture("diffuse", new Texture("white.png", true));
		
		transform = new Transform();
		transform.setPos(-0.5f, 0, -0.5f);
	}
	
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_KP_1)){
			visable = !visable;
		}
	}
	
	public void render(Shader shader, Camera camera){
		if(visable){
			if(mesh.inFrustum(transform, camera)){
				shader.bind();
				shader.updateUniforms(transform, material, camera);
				mesh.draw();
			}
		}
	}
}
