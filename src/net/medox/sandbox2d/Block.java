package net.medox.sandbox2d;

import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.RenderingEngine;

public class Block{
	private Transform2D tranform;
	private int id;
	
	public Block(Transform2D tranform){
		this.tranform = tranform;
		id = 0;
	}
	
//	public void input(float delta){
//		if(Input.getMousePosition().getX() >= tranform.getPos().getX() && Input.getMousePosition().getY() >= tranform.getPos().getY()){
//			if(Input.getMousePosition().getX() <= tranform.getPos().getX()+tranform.getScale().getX() && Input.getMousePosition().getY() <= tranform.getPos().getY()+tranform.getScale().getY()){
//				if(Input.getKey(Input.KEY_1)){
//					id = 0;
//				}else if(Input.getKey(Input.KEY_2)){
//					id = 1;
//				}else if(Input.getKey(Input.KEY_3)){
//					id = 2;
//				}
//			}
//		}
//	}
	
	public void render(){
		if(id == 0){
			RenderingEngine.add2DMesh(tranform, Material.DEFAULT_DIFFUSE_TEXTURE);
		}else if(id == 1){
			RenderingEngine.add2DMesh(tranform, Material.DEFAULT_SPECULAR_MAP_TEXTURE);
		}else if(id == 2){
			RenderingEngine.add2DMesh(tranform, Material.DEFAULT_NORMAL_MAP_TEXTURE);
		}
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
}
