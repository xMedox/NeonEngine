package net.medox.game;

//import java.util.Random;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.BaseLight;

public class Light extends EntityComponent{
	private BaseLight light;
	private float timer = 0;
	private float timer2 = 0;
	private Vector3f goColor;
	private int colorInt = 0;
	
	public Light(BaseLight light){
		this.light = light;
		
		goColor = new Vector3f(1, 1, 1);
	}
	
	@Override
	public void update(float delta){
		if(timer > 0){
			timer -= delta;
			
			Vector3f color = light.getColor();
			
//			color = color.sub(((color.sub(goColor)).div(20*0.016666668f)).mul(delta));
//			color = color.lerp(goColor, 2*60*0.016666668f*delta);
//			
//			light.setColor(color);
//			
//			timer2 = 1*60*0.016666668f;
			color = color.lerp(goColor, 2*60*0.016666668f*delta);
			
			light.setColor(color);
			
			timer2 = 60*0.016666668f;
		}else{
			light.setColor(goColor);
//			Random rand = new Random();
//			
//			goColor = new Vector3f(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
			
//			timer = 50*0.016666668f;
//			timer2 = 25*0.016666668f;
			
			if(timer2 > 0){
				timer2 -= delta;
			}else{
				timer = 2*60*0.016666668f;
				
				if(colorInt == 0){
					goColor = new Vector3f(1, 0, 0);
					
					colorInt = 1;
				}else if(colorInt == 1){
					goColor = new Vector3f(0, 1, 0);
					
					colorInt = 2;
				}else if(colorInt == 2){
					goColor = new Vector3f(0, 0, 1);
					
					colorInt = 0;
				}
			}
		}
	}
}
