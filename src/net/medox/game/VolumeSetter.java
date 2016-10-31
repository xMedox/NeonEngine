package net.medox.game;

import net.medox.neonengine.audio.SoundEngine;
import net.medox.neonengine.components2D.Slider;
import net.medox.neonengine.core.EntityComponent;

public class VolumeSetter extends EntityComponent{
	private Slider slider;
	
	public VolumeSetter(Slider slider){
		this.slider = slider;
	}
	
	@Override
	public void update(float delta){
		SoundEngine.setGain(slider.getProgress());
	}
}
