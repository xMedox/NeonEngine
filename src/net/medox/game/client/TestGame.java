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
		material.setTexture("diffuse", new Texture("bricks2.jpg"));
		material.setTexture("normalMap", new Texture("bricks2_normal.jpg"));
		material.setTexture("dispMap", new Texture("bricks2_disp.jpg"));
//		material.setTexture("specMap", new Texture("bricks2_spec.jpg"));
		material.setFloat("specularIntensity", 0.75f);
		material.setFloat("specularPower", 2f);
		material.setFloat("dispMapScale", 0.02f);
		material.setFloat("dispMapBias", -1.0f);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		Entity planeObject = new Entity();
		planeObject.getTransform().getPos().set(0, -1, 5);
		planeObject.getTransform().setScale(new Vector3f(40f, 40f, 40f));
//		planeObject.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));
		planeObject.addComponent(meshRenderer);
		
		Mesh meshBox = new Mesh("box.obj");
		Material materialBox = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		materialBox.setTexture("diffuse", new Texture("bricks.jpg"));
		materialBox.setTexture("dispMap", new Texture("bricks_disp.png"));
		materialBox.setTexture("normalMap", new Texture("bricks_normal.jpg"));
		materialBox.setFloat("specularIntensity", 0.25f);
		materialBox.setFloat("specularPower", 2f);
		materialBox.setFloat("dispMapScale", 0.02f);
		materialBox.setFloat("dispMapBias", -1.0f);
		
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
//		material2.addTexture("diffuse", new Texture("untitled.png"));
		material2.setTexture("diffuse", new Texture("untitled.png"));
//		material2.addTexture("normalMap", new Texture("bricks_normal.jpg"));
//		material2.addTexture("dispMap", new Texture("bricks_disp.png"));
		material2.setFloat("specularIntensity", 0.5f);
		material2.setFloat("specularPower", 1);
//		material2.addFloat("dispMapScale", 0.03f);
//		material2.addFloat("dispMapBias", -0.5f);
		
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh2, material2);
		
		Entity monkeyObject = new Entity().addComponent(new LookAtComponent()).addComponent(meshRenderer2);
		monkeyObject.getTransform().getPos().set(0, 0, 5);
		
//		Mesh mesh3 = new Mesh("Ak-47.obj");
//		Material material3 = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 2, 8
//		material3.setTexture("diffuse", new Texture("white.png"));
//		material3.setFloat("specularIntensity", 2);
//		material3.setFloat("specularPower", 8);
//		
//		MeshRenderer meshRenderer3 = new MeshRenderer(mesh3, material3);
		
//		Weapon weapon = new Weapon(meshRenderer3, mainCamera);
//		
//		getRootObject().addChild(weapon);
		
//		Entity ar_15Object = new Entity();
//		ar_15Object.addComponent(meshRenderer3);
//		
//		ar_15Object.getTransform().setScale(new Vector3f(0.01f, 0.01f, 0.01f));
//		ar_15Object.getTransform().getPos().set(12, -1f, 8);
//		ar_15Object.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
//		ar_15Object.getTransform().setScale(new Vector3f(0.000001f, 0.000001f, 0.000001f));
//		ar_15Object.getTransform().getPos().set(0.7394085f, -1.0639834f, 1.9975371f);
//		ar_15Object.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
//		
//		cameraObject.addChild(ar_15Object);
		
		
//		Mesh mesh4 = new Mesh("us_assault.obj");
//		Material material4 = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
//		material4.setTexture("diffuse", new Texture("white.png"));
//		material4.setFloat("specularIntensity", 0);
//		material4.setFloat("specularPower", 4);
//		
//		MeshRenderer meshRenderer4 = new MeshRenderer(mesh4, material4);
//		
//		Entity us_assaultObject = new Entity();
//		us_assaultObject.getTransform().setScale(new Vector3f(0.035f, 0.035f, 0.035f));
//		us_assaultObject.getTransform().getPos().set(5, -1f, 2);
//		
//		us_assaultObject.addComponent(meshRenderer4);
		
//		Mesh sword2mesh = new Mesh("sword 2.obj");
//		Material sword2material = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
//		sword2material.setTexture("diffuse", new Texture("sword2_final_cm.jpg"));
//		sword2material.setFloat("specularIntensity", 4);
//		sword2material.setFloat("specularPower", 16);
//		
//		MeshRenderer swordmesh2Renderer = new MeshRenderer(sword2mesh, sword2material);
//		
//		Entity sword2Object = new Entity();
//		sword2Object.getTransform().setScale(new Vector3f(0.4f, 0.4f, 0.4f));
//		sword2Object.getTransform().getPos().set(15, 3f, 15);
//		
//		sword2Object.addComponent(swordmesh2Renderer);
		
		Mesh kpmesh = new Mesh("kp.obj");
		Material kpmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		kpmaterial.setTexture("diffuse", new Texture("kp.png"));
		kpmaterial.setFloat("specularIntensity", 3);
		kpmaterial.setFloat("specularPower", 16);
		
		MeshRenderer kpRenderer = new MeshRenderer(kpmesh, kpmaterial);
		
		Entity kpObject = new Entity();
		kpObject.getTransform().getPos().set(-15, 0f, 15);
		
		kpObject.addComponent(kpRenderer);
		
		Mesh swordmesh = new Mesh("sword.obj");
		Material swordmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		swordmaterial.setTexture("diffuse", new Texture("sword.png"));
		swordmaterial.setFloat("specularIntensity", 2.5f);
		swordmaterial.setFloat("specularPower", 11);
		
		Material sword4material = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		sword4material.setTexture("diffuse", new Texture("S.png"));
		sword4material.setFloat("specularIntensity", 3);
		sword4material.setFloat("specularPower", 16);
		
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
		humanmaterial.setTexture("diffuse", new Texture("white.png"));
		humanmaterial.setFloat("specularIntensity", 0);
		humanmaterial.setFloat("specularPower", 0);
//		humanmaterial.addVector3f("color", new Vector3f(1, 0, 0));
		
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
//		akmaterial./*setTexture("diffuse", new Texture("white.png"));*/setTexture("diffuse", new Texture("bricks.jpg"));
//		try{
//			akmaterial.setTexture("diffuse", new Texture(ImageIO.read(new File("res/textures/Assault.png"))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		akmaterial.setTexture("diffuse", new Texture("bricks.jpg"));
//		akmaterial.setTexture("dispMap", new Texture("bricks_disp.png"));
//		akmaterial.setTexture("normalMap", new Texture("bricks_normal.jpg"));
//		akmaterial.setTexture("diffuse", new Texture("bricks2.jpg"));
//		akmaterial.setTexture("dispMap", new Texture("bricks2_disp.jpg"));
//		akmaterial.setTexture("normalMap", new Texture("bricks2_normal.jpg"));
//		akmaterial.setTexture("diffuse", new Texture("Ger - Kopie.png"));
		akmaterial.setTexture("diffuse", new Texture("test_tl.png"));
//		akmaterial.setTexture("diffuse", new Texture("AK-47.png"));
//		akmaterial.setTexture("normalMap", new Texture("AK-47_normal2.png"));
//		akmaterial.setTexture("specMap", new Texture("AK-47_specular.png"));
//		akmaterial.setFloat("specularIntensity", 0.5f);
//		akmaterial.setFloat("specularPower", 4);
		akmaterial.setFloat("specularIntensity", 0.75f);
		akmaterial.setFloat("specularPower", 2);
//		akmaterial.setVector3f("color", new Vector3f(1.0f, 0.2f, 0.2f));
		
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
//		materialZ.setTexture("diffuse", new Texture("bricks.jpg"));
//		materialZ.setTexture("normalMap", new Texture("bricks_normal.jpg"));
//		materialZ.setTexture("dispMap", new Texture("bricks_disp.png"));
//		materialZ.setTexture("diffuse", new Texture("test.png"));
//		materialZ.setTexture("diffuse", new Texture("AK-47 Gold.png"));
//		materialZ.setTexture("normalMap", new Texture("AK-47 Gold_normal2.png"));
//		materialZ.setTexture("specMap", new Texture("AK-47 Gold_specular.png"));
		
		materialZ.setTexture("diffuse", new Texture("bricks2.jpg"));
		materialZ.setTexture("dispMap", new Texture("bricks2_disp.jpg"));
		materialZ.setTexture("normalMap", new Texture("bricks2_normal.jpg"));
		materialZ.setFloat("dispMapScale", 0.02f);
		materialZ.setFloat("dispMapBias", -1.0f);
		
//		try{
//			materialZ.setTexture("diffuse", new Texture(ImageIO.read(new File("res/textures/Assault.png"))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		materialZ.setFloat("specularIntensity", 1);
//		materialZ.setFloat("specularPower", 8);
		materialZ.setFloat("specularIntensity", 0.75f);
		materialZ.setFloat("specularPower", 2);
//		materialZ.setFloat("dispMapScale", 0.02f);
//		materialZ.setFloat("dispMapBias", -1.0f);
//		materialZ.setVector3f("color", new Vector3f(0.2f, 0.2f, 1.0f));
		
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
		
//		Material gui = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
//		try{
//			gui.setTexture("diffuse", new Texture(ImageIO.read(new File("icon_16.png"))));
//		}catch(IOException e){
//			e.printStackTrace();
//		}
//		gui.setVector3f("color", new Vector3f(1, 0f, 0f));
//		
//		GuiEntity guiObject = new GuiEntity();
//		guiObject.setMaterial(gui);
//		
//		guiObject.getTransform().setPos(new Vector2f(0, 0));
//		guiObject.getTransform().setDimension(new Vector2f(128*2, 128*2));
//		guiObject.getTransform().setScale(new Vector2f(1, 1));
//		guiObject.getTransform().setRotation((float)Math.toRadians(45));
//		
//		addGuiEntity(guiObject);
		
//		RenderingEngine.setClearColor(new Vector3f(135f/4f/255f, 206f/4f/255f, 250f/4f/255f));
	}
}
