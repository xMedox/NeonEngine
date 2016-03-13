package net.medox.neonengine.audio;

import net.medox.neonengine.core.EntityComponent;

public class Listener extends EntityComponent{
	@Override
	public void update(float delta){
		SoundEngine.setPosition(getTransform().getTransformedPos());
		SoundEngine.setOrientation(getTransform().getTransformedRot());
	}
}
