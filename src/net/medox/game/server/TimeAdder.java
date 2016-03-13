package net.medox.game.server;

import net.medox.neonengine.core.EntityComponent;

public class TimeAdder extends EntityComponent{
	private MPServer server;
	
	public TimeAdder(MPServer server){
		this.server = server;
	}
	
	@Override
	public void update(float delta){
		server.time += 1;
	}
}
