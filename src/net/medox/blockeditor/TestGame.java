package net.medox.blockeditor;

import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.components.FreeMove;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.DirectionalLight;
import net.medox.neonengine.rendering.Skybox;

public class TestGame extends Game{
	@Override
	public void init(){
		Entity skybox = new Entity();
		skybox.addComponent(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		addEntity(skybox);
		
		Entity changeMode = new Entity();
		changeMode/*.addComponent(new FullscreenSetter())*/.addComponent(new ScreenshotTaker()).addComponent(new ChangeMode());
		addEntity(changeMode);
		
		Entity camera = new Entity();
		camera.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f));
		camera.addComponent(new FreeLook(0.15f));
		camera.addComponent(new FreeMove(10.0f));
		addEntity(camera);
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*8.0f*/16.0f, 1.0f, /*0.7f*/0.2f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
//		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(/*-45*/-55)));
//		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
//		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(0.5f, 0.8f, 0.5f), 1));
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		addEntity(directionalLightObject);
		
		Entity world = new Entity();
		world.addComponent(new World());
		addEntity(world);
	}
	
	@Override
	public void cleanUp(){
		System.out.println("--------------------------------------------------------------");
		System.out.println("Shutting down");
	}
}
