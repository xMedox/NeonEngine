package net.medox.game.server;

import net.medox.neonengine.core.Entity;

public class Player extends Entity{
	public String name = null;
	public int id = -1;
	
	public int ping = 999;
	public int weaponID = -1;
	
//	@Override
//	public void input(float delta){
//		if(!remove){
//			super.input(delta);
//		}
//	}
//	
//	@Override
//	public void update(float delta){
//		if(!remove){
//			super.update(delta);
//		}
//	}
//	
//	@Override
//	public void render(Shader shader, RenderingEngine renderingEngine){
//		if(!remove){
//			super.render(shader, renderingEngine);
//		}
//	}
}
