package net.medox.editor;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.DirectionalLight;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;

public class AddComponent extends EntityComponent{
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_N)){
			Mesh mesh = new Mesh("plane.obj");
			Material material = new Material();
//			material.setTexture("diffuse", new Texture("white.png"));
			
			MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
			
			Entity planeObject = new Entity();
			planeObject.getTransform().getPos().set(0, -1, 0);
//			planeObject.getTransform().setScale(new Vector3f(40f, 40f, 40f));
			planeObject.addComponent(meshRenderer);
			
			getParent().addChild(planeObject);
		}
		
		if(Input.getKeyDown(Input.KEY_M)){
			Entity directionalLightObject = new Entity();
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.00002f);
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.0000001f);
			
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 10.0f, 1.0f, 0.9f, 0.000001f);
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 80.0f, 1.0f, 0.9f, 0.000001f);
//			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, 60.0f, 1.0f, 0.9f, 0.000001f);
			DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, 20.0f, 1.0f, 0.7f, 0.000001f);
			directionalLightObject.addComponent(directionalLight);
			
			directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(/*-45*/-55)));
			directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
			
			getParent().addChild(directionalLightObject);
		}
	}
}
