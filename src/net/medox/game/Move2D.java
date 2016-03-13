package net.medox.game;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector2f;

public class Move2D extends Entity2DComponent{
	@Override
	public void input(float delta){
		float moveAmt = (int)2;
		
		if(Input.getKey(Input.KEY_UP)){
			getTransform().setPos(new Vector2f(getTransform().getPos().getX(), getTransform().getPos().getY()+moveAmt));
		}
		if(Input.getKey(Input.KEY_DOWN)){
			getTransform().setPos(new Vector2f(getTransform().getPos().getX(), getTransform().getPos().getY()-moveAmt));
		}
		if(Input.getKey(Input.KEY_RIGHT)){
			getTransform().setPos(new Vector2f(getTransform().getPos().getX()+moveAmt, getTransform().getPos().getY()));
		}
		if(Input.getKey(Input.KEY_LEFT)){
			getTransform().setPos(new Vector2f(getTransform().getPos().getX()-moveAmt, getTransform().getPos().getY()));
		}
	}
}
