package net.medox.blockeditor;

import java.util.HashMap;

import net.medox.neonengine.core.Transform;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class Block{
	private static Mesh mesh;
	
	private static Material materialS;
	private static HashMap<Integer, Material> materialMap;
	
	public int[] texture;
	private Transform transform;
	
	public boolean seleted;
	public boolean solid;
	
	public Block(int[] texture, Transform transfrom, boolean solid){
		if(mesh == null){
			mesh = new Mesh("block.obj");
			
			materialMap = new HashMap<Integer, Material>();
			
			Material material00 = new Material();
			material00.setDiffuseMap(new Texture("block00.png", true));
			materialMap.put(1, material00);
			
			Material material10 = new Material();
			material10.setDiffuseMap(new Texture("block10.png", true));
			materialMap.put(2, material10);
			
			Material material20 = new Material();
			material20.setDiffuseMap(new Texture("block20.png", true));
			materialMap.put(3, material20);
			
			Material material30 = new Material();
			material30.setDiffuseMap(new Texture("block30.png", true));
			materialMap.put(4, material30);
			
			Material material40 = new Material();
			material40.setDiffuseMap(new Texture("block40.png", true));
			material40.setGlowMap(new Texture("block40_glow.png", true));
			materialMap.put(5, material40);
			
			Material material50 = new Material();
			material50.setDiffuseMap(new Texture("block50.png", true));
			materialMap.put(6, material50);
			
			Material material60 = new Material();
			material60.setDiffuseMap(new Texture("block60.png", true));
			material60.setGlowMap(new Texture("block60_glow.png", true));
			materialMap.put(7, material60);
			
			Material material70 = new Material();
			material70.setDiffuseMap(new Texture("block70.png", true));
			materialMap.put(8, material70);
			
			Material material80 = new Material();
			material80.setDiffuseMap(new Texture("block80.png", true));
			materialMap.put(9, material80);
			
			Material material90 = new Material();
			material90.setDiffuseMap(new Texture("block90.png", true));
			materialMap.put(10, material90);
			
			Material material01 = new Material();
			material01.setDiffuseMap(new Texture("block01.png", true));
			materialMap.put(11, material01);
			
			Material material11 = new Material();
			material11.setDiffuseMap(new Texture("block11.png", true));
			materialMap.put(12, material11);
			
			Material material21 = new Material();
			material21.setDiffuseMap(new Texture("block21.png", true));
			materialMap.put(13, material21);
			
			Material material31 = new Material();
			material31.setDiffuseMap(new Texture("block31.png", true));
			materialMap.put(14, material31);
			
			Material material41 = new Material();
			material41.setDiffuseMap(new Texture("block41.png", true));
			materialMap.put(15, material41);
			
			Material material51 = new Material();
			material51.setDiffuseMap(new Texture("block51.png", true));
			materialMap.put(16, material51);
			
			Material material61 = new Material();
			material61.setDiffuseMap(new Texture("block61.png", true));
			materialMap.put(17, material61);
			
			Material material71 = new Material();
			material71.setDiffuseMap(new Texture("block71.png", true));
			materialMap.put(18, material71);
			
			Material material81 = new Material();
			material81.setDiffuseMap(new Texture("block81.png", true));
			materialMap.put(19, material81);
			
			materialS = new Material();
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
				}else{
					shader.bind();
					shader.updateUniforms(transform, materialMap.get((texture[0]+1)+(texture[1]*10)), camera);
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
