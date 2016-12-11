package net.medox.game;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Time;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;

public class Smoke extends EntityComponent{
	private float timer;
	
	private Vector3f move;
	
	public Smoke(){
		timer = 8*Time.getSecond();
		
		move = new Vector3f(Util.randomFloat()*0.4f-0.2f, 1f, Util.randomFloat()*0.4f-0.2f);
	}
	
	@Override
	public void update(float delta){
		if(timer > 0){
			timer -= delta;
			
			getTransform().move(move.mul(delta));
			
			if(timer < (6*Time.getSecond())){
				getTransform().setScale(new Vector3f(2, 2, 2));
			}else{
				getTransform().scale(new Vector3f(1.5f*delta*0.5f, 1.5f*delta*0.5f, 1.5f*delta*0.5f));
			}
		}else{
			getParent().removeSelf();
		}
	}
}
