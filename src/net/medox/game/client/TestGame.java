package net.medox.game.client;

import java.util.ArrayList;

import net.medox.game.LookAtComponent;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Attenuation;
import net.medox.neonengine.rendering.DirectionalLight;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.PointLight;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.SpotLight;
import net.medox.neonengine.rendering.Texture;

public class TestGame extends Game{
	public MPClient client;
	
	public TestGame(MPClient client){
		this.client = client;
	}
	
	@Override
	public void init(){
//		RenderingEngine.setSkybox(new Skybox("left.png", "back.png", "right.png", "front.png", "top.png", "bottom.png"));
//		RenderingEngine.useSkybox(true);
		
		Entity skyboxEntity = new Entity();
		Skybox skybox = new Skybox("left.png", "back.png", "right.png", "front.png", "top.png", "bottom.png");
		
		skyboxEntity.addComponent(skybox);
		addEntity(skyboxEntity);
		
		Entity gameObject = new Entity().addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker());
		
		addEntity(gameObject);
		
//		float fieldDepth = 4.0f;
//		float fieldWidth = 4.0f;
//		
//		Vertex[] vertices = new Vertex[]{new Vertex(new Vector3f(-fieldWidth, 0.0f, -fieldDepth), new Vector2f(0.0f, 1.0f)),
//									new Vertex(new Vector3f(-fieldWidth, 0.0f, fieldDepth * 3), new Vector2f(0.0f, 0.0f)),
//									new Vertex(new Vector3f(fieldWidth * 3, 0.0f, -fieldDepth), new Vector2f(1.0f, 1.0f)),
//									new Vertex(new Vector3f(fieldWidth * 3, 0.0f, fieldDepth * 3), new Vector2f(1.0f, 0.0f))};
//		
//		int[] indices = new int[]{0, 1, 2, 2, 1, 3};
		
//		Mesh mesh = new Mesh(vertices, indices, true);
//		Mesh mesh = new Mesh("backdrop(2.59).obj");
		Mesh mesh = new Mesh("plane.obj");
		Material material = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
		material.setDiffuseMap(new Texture("bricks2.jpg"));
		material.setNormalMap(new Texture("bricks2_normal.jpg"));
		material.setSpecularIntensity(0.75f);
		material.setSpecularPower(2f);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		Entity planeObject = new Entity();
		planeObject.getTransform().getPos().set(0, -1, 5);
		planeObject.getTransform().setScale(new Vector3f(40f, 40f, 40f));
//		planeObject.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));
		planeObject.addComponent(meshRenderer);
		
		Mesh meshBox = new Mesh("box.obj");
		Material materialBox = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		materialBox.setDiffuseMap(new Texture("bricks.jpg"));
		materialBox.setNormalMap(new Texture("bricks_normal.jpg"));
		materialBox.setSpecularIntensity(0.25f);
		materialBox.setSpecularPower(2f);
		
		MeshRenderer meshRendererBox = new MeshRenderer(meshBox, materialBox);
		
		Entity planeObjectBox = new Entity();
		planeObjectBox.addComponent(meshRendererBox);
		planeObjectBox.getTransform().getPos().set(15, 4, 5);
		
		
		Entity directionalLightObject = new Entity();
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.00002f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.0000001f);
		
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 10.0f, 1.0f, 0.9f, 0.000001f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 80.0f, 1.0f, 0.9f, 0.000001f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, 60.0f, 1.0f, 0.9f, 0.000001f);
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, 20.0f, 1.0f, 0.7f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		
		directionalLight.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(/*-45*/-55)));
		directionalLight.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		
		Entity pointLightObject = new Entity();
//		PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 3f, new Vector3f(0, 0, 1));
		PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 1f, new Attenuation(0, 0, 1), 5, 1.0f, 0.5f, 0.000001f);
		pointLightObject.addComponent(pointLight);
		
		Entity spotLightObject = new Entity();
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 3f, new Vector3f(0, 0, 0.1f), 0.7f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 0.4f, new Attenuation(0, 0, 0.1f), 0.7f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 0.4f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(91.1f), 7, 1.0f, 0.5f, 0.00002f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 1f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 7, 1.0f, 0.5f, 0.0000001f);
		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 1f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
		spotLightObject.addComponent(spotLight);
		
		pointLightObject.getTransform().getPos().set(5, 0, 5);
		
		spotLightObject.getTransform().getPos().set(10, 0, 10);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		Mesh mesh2 = new Mesh("untitled.obj");
		Material material2 = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 1, 8
		material2.setDiffuseMap(new Texture("untitled.png"));
		material2.setSpecularIntensity(0.5f);
		material2.setSpecularPower(1);
		
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh2, material2);
		
		Entity monkeyObject = new Entity().addComponent(new LookAtComponent()).addComponent(meshRenderer2);
		monkeyObject.getTransform().getPos().set(0, 0, 5);
		
		Mesh kpmesh = new Mesh("kp.obj");
		Material kpmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		kpmaterial.setDiffuseMap(new Texture("kp.png"));
		kpmaterial.setSpecularIntensity(3);
		kpmaterial.setSpecularPower(16);
		
		MeshRenderer kpRenderer = new MeshRenderer(kpmesh, kpmaterial);
		
		Entity kpObject = new Entity();
		kpObject.getTransform().getPos().set(-15, 0f, 15);
		
		kpObject.addComponent(kpRenderer);
		
		Mesh swordmesh = new Mesh("sword.obj");
		Material swordmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		swordmaterial.setDiffuseMap(new Texture("sword.png"));
		swordmaterial.setSpecularIntensity(2.5f);
		swordmaterial.setSpecularPower(11);
		
		Material sword4material = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		sword4material.setDiffuseMap(new Texture("S.png"));
		sword4material.setSpecularIntensity(3);
		sword4material.setSpecularPower(16);
		
		Mesh swordmesh2 = new Mesh("SSword.obj");
		
		MeshRenderer swordmeshRenderer = new MeshRenderer(swordmesh, swordmaterial);
		MeshRenderer swordmeshRenderer2 = new MeshRenderer(swordmesh2, sword4material);
		
		Entity swordObject = new Entity();
		swordObject.getTransform().setScale(new Vector3f(0.0125f*12, 0.0125f*12, 0.0125f*12));
		swordObject.getTransform().getPos().set(0.2f*12, /*-0.08f*/-0.0625f*12, 0.25f*12);
		swordObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
		
		Entity swordObject2 = new Entity();
		swordObject2.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
		swordObject2.getTransform().getPos().set(3f, 3f, 0f);
		
		swordObject.addComponent(swordmeshRenderer);
		
		swordObject2.addComponent(swordmeshRenderer2);
		
		addEntity(swordObject);
		
		addEntity(planeObjectBox);
		
		Mesh humanmesh = new Mesh("Human.obj");
		Material humanmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		humanmaterial.setDiffuseMap(new Texture("white.png"));
		humanmaterial.setSpecularIntensity(0);
		humanmaterial.setSpecularPower(0);
		
		MeshRenderer humanmeshRenderer = new MeshRenderer(humanmesh, humanmaterial);
		
		Entity humanObject = new Entity();
		humanObject.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
		humanObject.getTransform().getPos().set(12f, 10f, 10f);
		
		humanObject.addComponent(humanmeshRenderer);
		
		addEntity(humanObject);
		
//		Mesh akmesh = new Mesh("ak-74.obj");
//		Mesh akmesh = new Mesh("Assault.obj");
		Mesh akmesh = new Mesh("AK-47 15.obj");
		Material akmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		akmaterial.setDiffuseMap(new Texture("test_tl.png"));
		akmaterial.setSpecularIntensity(0.75f);
		akmaterial.setSpecularPower(2);
		
//		MeshRenderer akmeshRenderer = new MeshRenderer(akmesh, akmaterial);
		
//		Entity akObject = new Entity();
////		akObject.getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
//		akObject.getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
//		akObject.getTransform().rotate(new Vector3f(0, 1, 0),  (float)Math.toRadians(90));
		
		WeaponScript weaponScript = new WeaponScript(client.player);
		WeaponScript weaponScript2 = new WeaponScript(client.player);
		
		ArrayList<Quaternion> quaternions = new ArrayList<Quaternion>();
		
		DelayLook delayLook = new DelayLook(client.player, quaternions, 120/2, false);
		DelayLook delayLook2 = new DelayLook(client.player, quaternions, 120/2, false);
		
//		Entity cameraAtt = new Entity();
//		
//		akObject.addComponent(akmeshRenderer);
//		akObject.addComponent(weaponScript);
//		
//		cameraAtt.addComponent(delayLook);
//		cameraObject.addChild(cameraAtt);
//		
//		cameraAtt.addChild(akObject);
		
		Mesh meshZ = new Mesh("AK-47 15.obj");
		Material materialZ = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
		materialZ.setDiffuseMap(new Texture("bricks2.jpg"));
		materialZ.setNormalMap(new Texture("bricks2_normal.jpg"));
		materialZ.setSpecularIntensity(0.75f);
		materialZ.setSpecularPower(2);
		
		Weapon weapon = new Weapon(client.player, akmesh, akmaterial, weaponScript, delayLook);
		
		weapon.getEntity().getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
		weapon.getEntity().getTransform().rotate(new Vector3f(0, 1, 0),  (float)Math.toRadians(90));
		weapon.setID(0);
		
		Weapon weapon2 = new Weapon(client.player, meshZ, materialZ, weaponScript2, delayLook2);
		
		weapon2.getEntity().getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
		weapon2.getEntity().getTransform().rotate(new Vector3f(0, 1, 0),  (float)Math.toRadians(90));
		weapon2.setID(1);
		
		Inventory inventory = new Inventory(weapon, weapon2);
		
		client.inventory = inventory;
		
		addEntity(inventory);
		
//		akObject.addComponent(d);
//		cameraObject.addChild(akObject);
		
//		SpotLight flashLight = new SpotLight(new Vector3f(1, 1, 1), 2f, new Attenuation(0.2f, 0.2f, 0.2f), 0.7f);
//		GameObject flashLightObject = new GameObject().addComponent(flashLight);
		
//		cameraObject.addChild(flashLightObject);
		
//		addObject(ar_15Object);
//		addObject(us_assaultObject);
//		addObject(sword2Object);
		addEntity(kpObject);
		addEntity(swordObject2);
		
		addEntity(planeObject);
		addEntity(directionalLightObject);
		addEntity(pointLightObject);
		addEntity(spotLightObject);
		addEntity(monkeyObject);
	}
}
