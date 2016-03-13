package net.medox.game.client;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Shader;

public class PlayerWeapon extends Entity{
	public boolean isActive;
	
	@Override
	public void inputAll(float delta){
		if(isActive){
			super.inputAll(delta);
		}
	}
	
	@Override
	public void updateAll(float delta){
		if(isActive){
			super.updateAll(delta);
		}
	}
	
	@Override
	public void renderAll(Shader shader, Camera camera){
		if(isActive){
			super.renderAll(shader, camera);
		}
	}
}
