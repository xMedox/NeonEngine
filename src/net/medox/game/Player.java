package net.medox.game;

import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.rendering.Camera;

public class Player extends Entity{
	private Camera camera;
	private FreeLook freeLook;
	private SprintMove sprintMove;
	
	public Player(Camera camera, FreeLook freeLook, SprintMove sprintMove){
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

	public FreeLook getFreeLook(){
		return freeLook;
	}

	public SprintMove getSprintMove(){
		return sprintMove;
	}
}
