package net.medox.sandbox2d;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class World extends Entity2DComponent{
	public static final int worldWidth = 25;
	public static final int worldHeight = 15;
	
	public static final int blockSize = 32;
	
	private Block[][] blocks;
	
	public World(){
		blocks = new Block[worldWidth][worldHeight];
		
		if(!Util.fileExists("Blocks.txt")){
			for(int x = 0; x < worldWidth; x++){
				for(int y = 0; y < worldHeight; y++){
					Transform2D transform = new Transform2D();
					transform.setPos(new Vector2f(x*blockSize, y*blockSize));
					transform.setScale(new Vector2f(blockSize, blockSize));
					Block block = new Block(transform);
					blocks[x][y] = block;
				}
			}
		}else{
			String text = Util.decrypt(Util.loadFromFile("Blocks.txt").get(0));
			
			for(int x = 0; x < worldWidth; x++){
				for(int y = 0; y < worldHeight; y++){
					Transform2D transform = new Transform2D();
					transform.setPos(new Vector2f(x*blockSize, y*blockSize));
					transform.setScale(new Vector2f(blockSize, blockSize));
					Block block = new Block(transform);
					block.setId(Character.getNumericValue(text.charAt(x*worldHeight+y)));
					blocks[x][y] = block;
				}
			}
		}
	}
	
	@Override
	public void update(float delta){
		
	}
	
	@Override
	public void input(float delta){
		if(Input.getMouse(Input.BUTTON_LEFT)){
			if((int)(Input.getMousePosition().getX()/blockSize) >= 0 && (int)(Input.getMousePosition().getX()/blockSize) < worldWidth && (int)(Input.getMousePosition().getY()/blockSize) >= 0 && (int)(Input.getMousePosition().getY()/blockSize) < worldHeight){
				blocks[(int)(Input.getMousePosition().getX()/blockSize)][(int)(Input.getMousePosition().getY()/blockSize)].setId(0);
			}
		}else if(Input.getMouse(Input.BUTTON_MIDDLE)){
			if((int)(Input.getMousePosition().getX()/blockSize) >= 0 && (int)(Input.getMousePosition().getX()/blockSize) < worldWidth && (int)(Input.getMousePosition().getY()/blockSize) >= 0 && (int)(Input.getMousePosition().getY()/blockSize) < worldHeight){
				blocks[(int)(Input.getMousePosition().getX()/blockSize)][(int)(Input.getMousePosition().getY()/blockSize)].setId(1);
			}
		}else if(Input.getMouse(Input.BUTTON_RIGHT)){
			if((int)(Input.getMousePosition().getX()/blockSize) >= 0 && (int)(Input.getMousePosition().getX()/blockSize) < worldWidth && (int)(Input.getMousePosition().getY()/blockSize) >= 0 && (int)(Input.getMousePosition().getY()/blockSize) < worldHeight){
				blocks[(int)(Input.getMousePosition().getX()/blockSize)][(int)(Input.getMousePosition().getY()/blockSize)].setId(2);
			}
		}
	}
	
	@Override
	public void render(){
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				blocks[x][y].render();
			}
		}
		
		if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
			RenderingEngine.drawString(10, 10, "OpenGL", 1, 1, new Vector3f(1, 1, 1));
		}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
			RenderingEngine.drawString(10, 10, "Vulkan", 1, 1, new Vector3f(1, 1, 1));
		}
	}
	
	@Override
	public void cleanUp(){
		String content = "";
		
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				content += blocks[x][y].getId();
			}
		}
		
		Util.saveToFile("Blocks.txt", Util.encrypt(content));
	}
}
