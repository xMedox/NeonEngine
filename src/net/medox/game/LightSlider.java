package net.medox.game;

//import java.util.Random;

import net.medox.neonengine.components.Slider;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.BaseLight;

public class LightSlider extends EntityComponent{
	private BaseLight light;
	private Slider slider;
	private Slider slider2;
	private Slider slider3;
	
	public LightSlider(BaseLight light, Slider slider, Slider slider2, Slider slider3){
		this.light = light;
		this.slider = slider;
		this.slider2 = slider2;
		this.slider3 = slider3;
	}
	
	@Override
	public void update(float delta){
		light.setColor(new Vector3f(slider.getProgress(), slider2.getProgress(), slider3.getProgress()));
	}
}
