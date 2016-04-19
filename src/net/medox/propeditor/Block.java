package net.medox.propeditor;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class Block{
	private static Mesh mesh;
	private static Material materialS;
	
	public int[] texture;
	private Transform transform;
	
	public boolean seleted;
	public boolean solid;
	
	public Block(int[] texture, Transform transfrom, boolean solid){
		if(mesh == null){
			mesh = new Mesh("block.obj");
			
			materialS = new Material();
			materialS.setDiffuseMap(new Texture("white.png", true));
		}
		
		this.texture = texture;
		this.transform = transfrom;
		this.solid = solid;
		
		seleted = false;
		
		transfrom.setScale(0.03125f);
	}
	
//	public void update(float delta){
//		
//	}
//	
//	public void input(float delta){
//		
//	}
	
	public void render(Shader shader, Camera camera){
		if(texture[0] != -1){
			if(mesh.inFrustum(transform, camera)){
				shader.bind();
				shader.updateUniforms(transform, materialS, camera);
				mesh.draw();
			}
		}else if(seleted){
			if(mesh.inFrustum(transform, camera)){
				shader.bind();
				shader.updateUniforms(transform, materialS, camera);
				mesh.draw();
			}
		}
	}
}
