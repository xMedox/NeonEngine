package net.medox.sandbox2d;

import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.rendering.Camera;

public class Sandbox2D extends Game{
	@Override
	public void init(){
		addEntity(new Entity().addComponent(new Camera(0, 0, 0)).addComponent(new ScreenshotTaker()));
		
//		Entity2D player = new Entity2D();
//		MeshRenderer2D playerRenderer = new MeshRenderer2D(new Texture("wolf.png", true));
//		Player playerComponent = new Player();
//		player.addComponent(playerComponent);
//		player.addComponent(playerRenderer);
//		player.getTransform().setScale(new Vector2f(32, 64));
//		addEntity2D(player);
		
		Entity2D worldEntity = new Entity2D();
		World world = new World();
		worldEntity.addComponent(world);
		addEntity2D(worldEntity);
	}
	
	@Override
	public void cleanUp(){
		System.out.println("--------------------------------------------------------------");
		System.out.println("Shutting down");
	}
}
