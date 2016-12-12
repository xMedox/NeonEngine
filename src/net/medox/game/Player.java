package net.medox.game;

import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.rendering.Camera;

public class Player extends Entity{
	private Camera camera;
	private LookComponent lookComponent;
	private SprintMove sprintMove;
	
	public Player(Camera camera, LookComponent lookComponent, SprintMove sprintMove){
		this.camera = camera;
		this.lookComponent = lookComponent;
		this.sprintMove = sprintMove;
		
		addComponent(camera);
		addComponent(lookComponent);
		addComponent(sprintMove);
	}

	public Camera getCamera(){
		return camera;
	}

	public LookComponent getLookComponent(){
		return lookComponent;
	}

	public SprintMove getSprintMove(){
		return sprintMove;
	}
}
