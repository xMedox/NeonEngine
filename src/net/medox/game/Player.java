package net.medox.game;

import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.rendering.Camera;

public class Player extends Entity{
	private Camera camera;
	private LookComponent freeLook;
	private SprintMove sprintMove;
	
	public Player(Camera camera, LookComponent freeLook, SprintMove sprintMove){
		this.camera = camera;
		this.freeLook = freeLook;
		this.sprintMove = sprintMove;
		
		addComponent(camera);
		addComponent(freeLook);
		addComponent(sprintMove);
	}

	public Camera getCamera(){
		return camera;
	}

	public LookComponent getFreeLook(){
		return freeLook;
	}

	public SprintMove getSprintMove(){
		return sprintMove;
	}
}
