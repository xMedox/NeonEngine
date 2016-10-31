package net.medox.neonengine.components2D;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.rendering.Window;

public class Lock2D extends Entity2DComponent{
	private final float xOffset;
	private final float yOffset;
	
	private int pos;
		
	public Lock2D(float xOffset, float yOffset, Vector2f point){
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		
		if(point.equals(new Vector2f(0.5f, 0.5f))){
			pos = 0;
		}else if(point.equals(new Vector2f(1f, 1f))){
			pos = 1;
		}else if(point.equals(new Vector2f(0f, 1f))){
			pos = 2;
		}else if(point.equals(new Vector2f(1f, 0f))){
			pos = 3;
		}else if(point.equals(new Vector2f(0f, 0f))){
			pos = 4;
		}else if(point.equals(new Vector2f(0.5f, 1f))){
			pos = 5;
		}else if(point.equals(new Vector2f(0.5f, 0f))){
			pos = 6;
		}else if(point.equals(new Vector2f(1f, 0.5f))){
			pos = 7;
		}else if(point.equals(new Vector2f(0f, 0.5f))){
			pos = 8;
		}else{
			pos = 4;
		}
	}
	
	@Override
	public void update(float delta){
		if(Window.gotResized()){
			if(pos == 0){
				getTransform().setPos(new Vector2f((int)(Window.getWidth()/2)+xOffset, (int)(Window.getHeight()/2)+yOffset));
			}else if(pos == 1){
				getTransform().setPos(new Vector2f((int)(Window.getWidth())+xOffset, (int)(Window.getHeight())+yOffset));
			}else if(pos == 2){
				getTransform().setPos(new Vector2f(xOffset, (int)(Window.getHeight())+yOffset));
			}else if(pos == 3){
				getTransform().setPos(new Vector2f((int)(Window.getWidth())+xOffset, yOffset));
			}else if(pos == 4){
				getTransform().setPos(new Vector2f(xOffset, yOffset));
			}else if(pos == 5){
				getTransform().setPos(new Vector2f((int)(Window.getWidth()/2)+xOffset, (int)(Window.getHeight())+yOffset));
			}else if(pos == 6){
				getTransform().setPos(new Vector2f((int)(Window.getWidth()/2)+xOffset, yOffset));
			}else if(pos == 7){
				getTransform().setPos(new Vector2f((int)(Window.getWidth())+xOffset, (int)(Window.getHeight()/2)+yOffset));
			}else if(pos == 8){
				getTransform().setPos(new Vector2f(xOffset, (int)(Window.getHeight()/2)+yOffset));
			}
		}
	}
}
