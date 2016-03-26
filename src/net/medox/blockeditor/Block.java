package net.medox.blockeditor;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class Block{
	private static Mesh mesh;
	private static Material materialS;
	private static Material material00;
	private static Material material10;
	private static Material material20;
	private static Material material30;
	private static Material material40;
	private static Material material50;
	private static Material material60;
	private static Material material70;
	private static Material material80;
	private static Material material90;
	private static Material material01;
	private static Material material11;
	private static Material material21;
	private static Material material31;
	private static Material material41;
	private static Material material51;
	private static Material material61;
	private static Material material71;
	private static Material material81;
	
	public int[] texture;
	private Transform transform;
	
	public boolean seleted;
	public boolean solid;
	
	public Block(int[] texture, Transform transfrom, boolean solid){
		if(mesh == null){
			mesh = new Mesh("block.obj");
			
			material00 = new Material();
//			material00.setTexture("diffuse", new Texture("block00.png", true));
//			material00.setTexture("diffuse", new Texture("blocks.png", true));
//			material00.setTexture("glowMap", new Texture("blocks_glow.png", true));
			material00.setDiffuseMap(new Texture("block00.png", true));
			
			material10 = new Material();
//			material10.setTexture("diffuse", new Texture("block10.png", true));
			material10.setDiffuseMap(new Texture("block10.png", true));
			
			material20 = new Material();
//			material20.setTexture("diffuse", new Texture("block20.png", true));
			material20.setDiffuseMap(new Texture("block20.png", true));
			
			material30 = new Material();
//			material30.setTexture("diffuse", new Texture("block30.png", true));
			material30.setDiffuseMap(new Texture("block30.png", true));
			
			material40 = new Material();
//			material40.setTexture("diffuse", new Texture("block40.png", true));
//			material40.setTexture("glowMap", new Texture("block40_glow.png", true));
			material40.setDiffuseMap(new Texture("block40.png", true));
			material40.setGlowMap(new Texture("block40_glow.png", true));
			
			material50 = new Material();
//			material50.setTexture("diffuse", new Texture("block50.png", true));
			material50.setDiffuseMap(new Texture("block50.png", true));
			
			material60 = new Material();
//			material60.setTexture("diffuse", new Texture("block60.png", true));
//			material60.setTexture("glowMap", new Texture("block60_glow.png", true));
			material60.setDiffuseMap(new Texture("block60.png", true));
			material60.setGlowMap(new Texture("block60_glow.png", true));
			
			material70 = new Material();
//			material70.setTexture("diffuse", new Texture("block70.png", true));
			material70.setDiffuseMap(new Texture("block70.png", true));
			
			material80 = new Material();
//			material80.setTexture("diffuse", new Texture("block80.png", true));
			material80.setDiffuseMap(new Texture("block80.png", true));
			
			material90 = new Material();
//			material90.setTexture("diffuse", new Texture("block90.png", true));
			material90.setDiffuseMap(new Texture("block90.png", true));
			
			material01 = new Material();
//			material01.setTexture("diffuse", new Texture("block01.png", true));
			material01.setDiffuseMap(new Texture("block01.png", true));
			
			material11 = new Material();
//			material11.setTexture("diffuse", new Texture("block11.png", true));
			material11.setDiffuseMap(new Texture("block11.png", true));
			
			material21 = new Material();
//			material21.setTexture("diffuse", new Texture("block21.png", true));
			material21.setDiffuseMap(new Texture("block21.png", true));
			
			material31 = new Material();
//			material31.setTexture("diffuse", new Texture("block31.png", true));
			material31.setDiffuseMap(new Texture("block31.png", true));
			
			material41 = new Material();
//			material41.setTexture("diffuse", new Texture("block41.png", true));
			material41.setDiffuseMap(new Texture("block41.png", true));
			
			material51 = new Material();
//			material51.setTexture("diffuse", new Texture("block51.png", true));
			material51.setDiffuseMap(new Texture("block51.png", true));
			
			material61 = new Material();
//			material61.setTexture("diffuse", new Texture("block61.png", true));
			material61.setDiffuseMap(new Texture("block61.png", true));
			
			material71 = new Material();
//			material71.setTexture("diffuse", new Texture("block71.png", true));
			material71.setDiffuseMap(new Texture("block71.png", true));
			
			material81 = new Material();
//			material81.setTexture("diffuse", new Texture("block81.png", true));
			material81.setDiffuseMap(new Texture("block81.png", true));
			
			materialS = new Material();
//			materialS.setTexture("diffuse", new Texture("white.png", true));
			materialS.setDiffuseMap(new Texture("white.png", true));
		}
		
		this.texture = texture;
		this.transform = transfrom;
		this.solid = solid;
		
		seleted = false;
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
				if(seleted){
					shader.bind();
					shader.updateUniforms(transform, materialS, camera);
					mesh.draw();
				}else if(texture[0] == 0 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material00, camera);
					mesh.draw();
				}else if(texture[0] == 1 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material10, camera);
					mesh.draw();
				}else if(texture[0] == 2 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material20, camera);
					mesh.draw();
				}else if(texture[0] == 3 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material30, camera);
					mesh.draw();
				}else if(texture[0] == 4 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material40, camera);
					mesh.draw();
				}else if(texture[0] == 5 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material50, camera);
					mesh.draw();
				}else if(texture[0] == 6 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material60, camera);
					mesh.draw();
				}else if(texture[0] == 7 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material70, camera);
					mesh.draw();
				}else if(texture[0] == 8 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material80, camera);
					mesh.draw();
				}else if(texture[0] == 9 && texture[1] == 0){
					shader.bind();
					shader.updateUniforms(transform, material90, camera);
					mesh.draw();
				}else if(texture[0] == 0 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material01, camera);
					mesh.draw();
				}else if(texture[0] == 1 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material11, camera);
					mesh.draw();
				}else if(texture[0] == 2 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material21, camera);
					mesh.draw();
				}else if(texture[0] == 3 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material31, camera);
					mesh.draw();
				}else if(texture[0] == 4 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material41, camera);
					mesh.draw();
				}else if(texture[0] == 5 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material51, camera);
					mesh.draw();
				}else if(texture[0] == 6 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material61, camera);
					mesh.draw();
				}else if(texture[0] == 7 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material71, camera);
					mesh.draw();
				}else if(texture[0] == 8 && texture[1] == 1){
					shader.bind();
					shader.updateUniforms(transform, material81, camera);
					mesh.draw();
				}
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
