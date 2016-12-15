package net.medox.game;

import net.medox.neonengine.audio.Listener;
import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.components.DynamicPlayerComponent;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.ParticleRenderer;
import net.medox.neonengine.components.PhysicsComponent;
import net.medox.neonengine.components.PlayerComponent;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.components.StaticPhysicsComponent;
import net.medox.neonengine.components2D.Lock2D;
import net.medox.neonengine.components2D.MeshRenderer2D;
import net.medox.neonengine.components2D.Slider;
import net.medox.neonengine.components2D.TextBox;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.lighting.Attenuation;
import net.medox.neonengine.lighting.DirectionalLight;
import net.medox.neonengine.lighting.PointLight;
import net.medox.neonengine.lighting.SpotLight;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;
import net.medox.neonengine.physics.CapsuleCollider;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.PointConstraint;
import net.medox.neonengine.physics.SphereCollider;
import net.medox.neonengine.physics.StaticMeshCollider;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Cursor;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.ParticleMaterial;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class TestGame extends Game{
	public static void main(String[] args){
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(true);
		NeonEngine.enableFXAA(true);
		NeonEngine.enableShadows(true);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(true);
		NeonEngine.enableBloom(true);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		NeonEngine.setRenderQuality(1);
		
		NeonEngine.init(new TestGame(), /*600*/60);
		
		Window.setStartTitle("Project Knight");
		Window.setStartSize(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("icon_16.png", "icon_32.png");
		Window.setStartCursor(new Cursor("cursor test 2.png", 0, 0));
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		super.init();
//		RenderingEngine.addFilter(new Shader("filterInvert"));
//		RenderingEngine.addFilter(new Shader("filterBits"));
//		RenderingEngine.addFilter(new Shader("filterGrey"));
//		RenderingEngine.addFilter(new Shader("filterFlip"));
		
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
		RenderingEngine.setMainSkybox(new Skybox("right.png", "left.png", "top.png", "bottom.png", "front.png", "back.png"));
		
		Entity gameObject = new Entity().addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker()).addComponent(new ChangeMode());
		
		addEntity(gameObject);
		
////		Camera mainCamera = new Camera((float)Math.toRadians(70.0f), 0.01f, 1000.0f);
//		Camera mainCamera = new Camera((float)Math.toRadians(65.0f), 0.01f, 1000.0f);
////		Camera mainCamera = new Camera((float)Math.toRadians(70.0f), (float)(Window.getWidth() / 4) / (float)(Window.getHeight() / 4), 0.01f, 1000.0f, false);
////		Entity cameraObject = new Entity().addComponent(new MoveComponent(10f*0.016666668f)).addComponent(mainCamera);
////		Entity cameraObject = new Entity().addComponent(new MoveComponent(15f*0.016666668f)).addComponent(mainCamera);
//		Entity cameraObject = new Entity().addComponent(new SprintMove(15f*0.016666668f, 15f*0.016666668f*2)).addComponent(mainCamera);
//		
//		LookComponent lookComponent = new LookComponent(0.25f);
//		
//		cameraObject.addComponent(lookComponent);
//		
//		addObject(cameraObject);
		
//		Player player = new Player(new Camera((float)Math.toRadians(65.0f), 0.01f, 500.0f), new LookComponent(0.15f), new SprintMove(15f, 15f*2));
		
		
		Entity player = new Entity();
		Entity playerHead = new Entity();
		
		player.getTransform().setPos(new Vector3f(0, 2, 0));
		playerHead.getTransform().setPos(new Vector3f(0, 0.75f, 0));
		
		Camera cam = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		
		playerHead.addComponent(cam);
		playerHead.addComponent(new LookComponent(0.15f));
		
		SphereCollider sphere2 = new SphereCollider(1);
		sphere2.setMassProps(1f);
		PhysicsComponent testphys = new PhysicsComponent(sphere2);
//		testphys.getSphere().setMassProps(0);
		
		CapsuleCollider capsule = new CapsuleCollider(0.5f, 1f);
		
//		capsule.setMassProps(2.5f, new Vector3f(0, 0, 0));
		capsule.setMassProps(70.8f);
//		capsule.setRestitution(0f);
//		capsule.setAngularFactor(1f);
		capsule.setAngularFactor(0);
//		capsule.setFriction(0.5f);
		capsule.setSleepingThresholds(0, 0);
//		controlBall.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
		
//		PhysicsEngine.addObject(cylinder);
		
		DynamicPlayerComponent p = new DynamicPlayerComponent(capsule, cam);
		
		player.addComponent(p);
		
		Listener listener = new Listener();
		
		playerHead.addComponent(listener);
		
		player.addChild(playerHead);
		
//		player.addComponent(new Camera((float)Math.toRadians(65.0f), 0.01f, 1000.0f)).addComponent(new LookComponent(0.15f)).addComponent(new SprintMove(15f, 15f*2));
		
		addEntity(player);
				
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
		Mesh mesh = new Mesh(/*"Level test.obj"*//*"castle.obj"*/"banjo.obj", true);
//		Mesh mesh = new Mesh("planeScale.obj"));
		Material material = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
		material.setDiffuseMap(new Texture(/*"Level test.png"*//*"princess peaches castle (outside)_grp.png"*/"banjo.png"));
//		material.setDiffuseMap(new Texture("bricks2.jpg"));
//		material.setNormalMap(new Texture("bricks2_normal.jpg"));
//		material.setSpecularMap(new Texture("bricks2_spec.jpg"));
//		material.setSpecularIntensity(0.5f + 0.15f/2);
//		material.setSpecularPower(4f + 1f/2);
		
		MeshRenderer meshRenderer = new MeshRenderer(mesh, material);
		
		Entity planeObject = new Entity();
		planeObject.getTransform().getPos().set(0, /*-1*/-32.5f, /*0*//*10*/100);
//		planeObject.getTransform().setScale(new Vector3f(25f, 25f, 25f));
//		planeObject.getTransform().setScale(20);
		planeObject.getTransform().setScale(/*2*/10);
//		planeObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(180));
		planeObject.addComponent(meshRenderer);
		
		StaticMeshCollider coll = mesh.getCollider();
		
		coll.setPos(planeObject.getTransform().getTransformedPos());
		coll.setScale(/*2*/10);
		
		coll.setMassProps(0);
		
//		StaticPhysicsComponent physic = new StaticPhysicsComponent(/*new Box(new Vector3f(100, 1f, 100))*/new StaticPlaneCollider(new Vector3f(0, 1, 0), -1));
		StaticPhysicsComponent physic = new StaticPhysicsComponent(coll);
		
		physic.getCollider().setMassProps(0);
		
		planeObject.addComponent(physic);
		
		Material materialx = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
		materialx.setDiffuseMap(new Texture("wood2.png"));
		materialx.setSpecularIntensity(0.5f + 0.15f/2);
		materialx.setSpecularPower(4f + 1f/2);
		
		Mesh meshx = new Mesh("plane.obj");
		MeshRenderer meshRendererx = new MeshRenderer(meshx, materialx);
		
		Entity planeObjectx = new Entity();
		planeObjectx.getTransform().getPos().set(0-20+3, 2.5f, 5-20+3);
//		planeObject.getTransform().setScale(new Vector3f(25f, 25f, 25f));
		planeObjectx.getTransform().setScale(new Vector3f(1, 1, 1));
//		planeObject.getTransform().rotate(new Vector3f(1, 0, -1), (float)Math.toRadians(90));
//		planeObjectx.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));
		planeObjectx.getTransform().rotate(new Vector3f(1, 0, 0), (float)Math.toRadians(90));
		planeObjectx.getTransform().rotate(new Vector3f(0, 0, 1), (float)Math.toRadians(-90));
		planeObjectx.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
//		planeObjectx.getTransform().rotate(new Vector3f(1, 0, 1), (float)Math.toRadians(45));
		planeObjectx.addComponent(meshRendererx);
		
		Mesh meshDrake = new Mesh("Dragon 3.obj");
		Material materialDrake = new Material();
		materialDrake.setDiffuseMap(new Texture("Dragon 3.png"));
		materialDrake.setEmissiveMap(new Texture("white.png"));
		materialDrake.setSpecularIntensity(0.25f);
		materialDrake.setSpecularPower(2);
		
		MeshRenderer meshRendererDrake = new MeshRenderer(meshDrake, materialDrake);
		
		Entity planeObjectDrake = new Entity();
		planeObjectDrake.addComponent(meshRendererDrake);
		planeObjectDrake.getTransform().getPos().set(14, -1, 15);
		planeObjectDrake.getTransform().setScale(new Vector3f(4, 4, 4));
		
		Mesh meshRock = new Mesh("rock 7.obj");
		Material materialRock = new Material();
		materialRock.setDiffuseMap(new Texture("rock232.jpg"));
		materialRock.setNormalMap(new Texture("rock2_normal322.png"));
		materialRock.setSpecularIntensity(0.25f);
		materialRock.setSpecularPower(2);
		
		MeshRenderer meshRendererRock = new MeshRenderer(meshRock, materialRock);
		
		Entity planeObjectRock = new Entity();
		planeObjectRock.addComponent(meshRendererRock);
		planeObjectRock.getTransform().getPos().set(8, -1, 15);
		planeObjectRock.getTransform().setScale(new Vector3f(2, 2, 2));
		
		Entity directionalLightObject = new Entity();
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.00002f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.4f, 12, 80.0f, 1.0f, 0.2f, 0.0000001f);
		
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 10.0f, 1.0f, 0.9f, 0.000001f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 12, 80.0f, 1.0f, 0.9f, 0.000001f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, 60.0f, 1.0f, 0.9f, 0.000001f);
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 0.6f, 10, /*10.0f*/8.0f, 1.0f, 0.7f, 0.000001f);
//		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 0.975f, 0.95f), 0.6f, 10, 20.0f, 1.0f, 0.7f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		
		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		
		Entity pointLightObject = new Entity();
//		PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 3f, new Vector3f(0, 0, 1));
//		PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 1f, new Attenuation(0, 0, 1));
		PointLight pointLight = new PointLight(new Vector3f(0, 1, 0), 4f, new Attenuation(0, 0, 1)/*, 8, 1.0f, 0.5f, 0.000001f*/);
		pointLightObject.addComponent(pointLight);
		
//		Entity pointLightObjectw = new Entity();
//		SpotLight spotLightw = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjectw.addComponent(spotLightw);
////		pointLightObjectw.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90)));
//		pointLightObject.addChild(pointLightObjectw);
//		
//		Entity pointLightObjecta = new Entity();
//		SpotLight spotLighta = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjecta.addComponent(spotLighta);
//		pointLightObjecta.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
//		pointLightObject.addChild(pointLightObjecta);
//		
//		Entity pointLightObjects = new Entity();
//		SpotLight spotLights = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjects.addComponent(spotLights);
//		pointLightObjects.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(180)));
//		pointLightObject.addChild(pointLightObjects);
//		
//		Entity pointLightObjectd = new Entity();
//		SpotLight spotLightd = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjectd.addComponent(spotLightd);
//		pointLightObjectd.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(270)));
//		pointLightObject.addChild(pointLightObjectd);
//		
//		Entity pointLightObjectf = new Entity();
//		SpotLight spotLightf = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjectf.addComponent(spotLightf);
//		pointLightObjectf.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90)));
//		pointLightObject.addChild(pointLightObjectf);
//		
//		Entity pointLightObjectg = new Entity();
//		SpotLight spotLightg = new SpotLight(new Vector3f(0, 1, 0), 2f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 5, 1.0f, 0.5f, 0.000001f);
//		pointLightObjectg.addComponent(spotLightg);
//		pointLightObjectg.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-90)));
//		pointLightObject.addChild(pointLightObjectg);
		
		Entity spotLightObject = new Entity();
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 3f, new Vector3f(0, 0, 0.1f), 0.7f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 0.4f, new Attenuation(0, 0, 0.1f), 0.7f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 0.4f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(91.1f), 7, 1.0f, 0.5f, 0.00002f);
//		SpotLight spotLight = new SpotLight(new Vector3f(1, 0, 1), 1f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 7, 1.0f, 0.5f, 0.0000001f);
		SpotLight spotLight = new SpotLight(new Vector3f(1, 1, 1), 4f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 8, 1.0f, 0.5f, 0.000001f);
		spotLightObject.addComponent(spotLight);
		spotLightObject.addComponent(new Light(spotLight));
		
		Entity spotLightObject2 = new Entity();
		
		SpotLight spotLight2 = new SpotLight(new Vector3f(1, 1, 1), 4f, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 8, 1.0f, 0.5f, 0.000001f);
		spotLightObject2.addComponent(spotLight2);
		
		pointLightObject.getTransform().getPos().set(5, 0, 5);
		
		spotLightObject.getTransform().getPos().set(10, 0, 6.6f);
		spotLightObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		spotLightObject2.getTransform().getPos().set(10, 0, 0);
		spotLightObject2.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90.0f)));
		
		Mesh mesh2 = new Mesh("untitled.obj");
		Material material2 = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 1, 8
		material2.setDiffuseMap(new Texture("untitled.png"));
		material2.setEmissiveMap(new Texture("untitled_glow22.png"));
		material2.setSpecularIntensity(0.5f);
		material2.setSpecularPower(1);
		
		MeshRenderer meshRenderer2 = new MeshRenderer(mesh2, material2);
		
		Entity monkeyObject = new Entity().addComponent(new LookAtComponent()).addComponent(meshRenderer2);
		monkeyObject.getTransform().getPos().set(0, 0, 5);
		
		Mesh kpmesh = new Mesh("kp.obj");
		Material kpmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		kpmaterial.setDiffuseMap(new Texture("kp.png"));
		kpmaterial.setSpecularIntensity(2);
		kpmaterial.setSpecularPower(16);
		
		MeshRenderer kpRenderer = new MeshRenderer(kpmesh, kpmaterial);
		
		Entity kpObject = new Entity();
		kpObject.getTransform().getPos().set(-15, 0f, 15);
		
		kpObject.addComponent(kpRenderer);
		
//		Mesh swordmesh = new Mesh("sword.obj");
		Mesh swordmesh = new Mesh("Sword R.obj");
		Material swordmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		swordmaterial.setDiffuseMap(new Texture("Sword R.png"));
		swordmaterial.setSpecularIntensity(2f);
		swordmaterial.setSpecularPower(8f);
		
		Mesh swordmesh2 = new Mesh("Shield R.obj");
		Material sword4material = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		sword4material.setDiffuseMap(new Texture("Shield R.png"));
		sword4material.setSpecularIntensity(2f);
		sword4material.setSpecularPower(8f);
		
		Mesh swordmesh3 = new Mesh("Round Shield R.obj");
		Material sword5material = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		sword5material.setDiffuseMap(new Texture("Round Shield R.png"));
		sword5material.setSpecularIntensity(2f);
		sword5material.setSpecularPower(8f);
		
		MeshRenderer swordmeshRenderer = new MeshRenderer(swordmesh, swordmaterial);
		MeshRenderer swordmeshRenderer2 = new MeshRenderer(swordmesh2, sword4material);
		MeshRenderer swordmeshRenderer3 = new MeshRenderer(swordmesh3, sword5material);
		
		Entity swordObject = new Entity();
//		swordObject.getTransform().setScale(new Vector3f(0.0125f*12, 0.0125f*12, 0.0125f*12));
		swordObject.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
//		swordObject.getTransform().getPos().set(0.2f*12, /*-0.08f*/-0.0625f*12, 0.25f*12);
		swordObject.getTransform().getPos().set(0.2f*12, 1, 0.25f*12);
		swordObject.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90)));
		
		Entity swordObject2 = new Entity();
		swordObject2.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
		swordObject2.getTransform().getPos().set(3f, 3f, 0f);
		
		Entity swordObject3 = new Entity();
		swordObject3.getTransform().setScale(new Vector3f(0.5f, 0.5f, 0.5f));
		swordObject3.getTransform().getPos().set(6f, 3f, 0f);
		
		swordObject.addComponent(swordmeshRenderer);
		
		swordObject2.addComponent(swordmeshRenderer2);
		
		swordObject3.addComponent(swordmeshRenderer3);
		
		addEntity(swordObject);
		
		addEntity(swordObject3);
		
		addEntity(planeObjectDrake);
		addEntity(planeObjectRock);
//		addEntity(planeObjectTree);
		
//		addEntity(planeObjectBox);
		
//		addEntity(planeObjectBox2);
		
//		Mesh m = new Mesh("Human.obj", true);
//		PhysicsComponent physc = new PhysicsComponent(m.getMeshShape());
		
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
//		humanObject.addComponent(physc);
		
		addEntity(humanObject);
		
//		addObject(ar_15Object);
//		addObject(us_assaultObject);
//		addObject(sword2Object);
		addEntity(kpObject);
		addEntity(swordObject2);
		
		addEntity(planeObject);
		addEntity(planeObjectx);
		addEntity(directionalLightObject);
		addEntity(pointLightObject);
		addEntity(spotLightObject);
		addEntity(spotLightObject2);
		addEntity(monkeyObject);
		
//		physicsEngine.addObject(new PhysicsObject(new BoundingSphere(new Vector3f(-1.414f/4.0f * 7.0f, 0.0f, -1.414f/4.0f * 7.0f), 1.0f), new Vector3f(1.414f/2.0f, 0.0f, 1.414f/2.0f))); 
		
//		PhysicsEngineComponent physicsEngineComponent = new PhysicsEngineComponent(physicsEngine);
		
//		PhysicsComponent physicsEngineComponent = new PhysicsComponent();
		
		Material bricks = new Material();
		bricks.setDiffuseMap(new Texture("Lava22.png"));
		bricks.setNormalMap(new Texture("Lava_normal22.jpg"));
		bricks.setEmissiveMap(new Texture("Lava_glow22.png"));
		bricks.setSpecularIntensity(0.25f);
		bricks.setSpecularPower(2);

		
//		for(int i = 0; i < physicsEngineComponent.getPhysicsEngine().getNumObjects(); i++){
//			Entity entity = new Entity();
////			entity.getTransform().setScale(new Vector3f(physicsEngineComponent.getPhysicsEngine().getObject(i).getRadius(), physicsEngineComponent.getPhysicsEngine().getObject(i).getRadius(), physicsEngineComponent.getPhysicsEngine().getObject(i).getRadius()));
//			
//			addEntity(entity.addComponent(new PhysicsComponent(physicsEngineComponent.getPhysicsEngine().getObject(i))).addComponent(new MeshRenderer(new Mesh("sphere.obj"), bricks)));
//		}
		
		Entity entity = new Entity();
		
		addEntity(entity.addComponent(testphys).addComponent(new MeshRenderer(new Mesh("sphere.obj"), bricks)).addComponent(new SoundComponent())/*.addComponent(new PointLight(new Vector3f(1, 0, 0), 3f, new Attenuation(0, 0, 1), 5, 1.0f, 0.5f, 0.000001f))*/);
		
		SphereCollider sphere = new SphereCollider(1);
		sphere.setMassProps(1f);
		PhysicsComponent testphys2 = new PhysicsComponent(sphere);
		
		Entity entity5 = new Entity();
		
		addEntity(entity5.addComponent(testphys2).addComponent(new MeshRenderer(new Mesh("sphere.obj"), bricks)).addComponent(new SoundComponent())/*.addComponent(new PointLight(new Vector3f(1, 0, 0), 3f, new Attenuation(0, 0, 1), 5, 1.0f, 0.5f, 0.000001f))*/);
		
		
		Transform tk = new Transform();
		tk.setPos(new Vector3f(3, 15, 0));
		
		sphere.setTransform(tk);
//		testphys2.getSphere().setMassProps(0);
		
		tk.setPos(new Vector3f(5, 15, 0));
		
		sphere2.setTransform(tk);
//		testphys2.getSphere().setMassProps(0);
		
		
		Transform tkA = new Transform();
		tkA.setPos(new Vector3f(1, 0, 0));
		
		Transform tkB = new Transform();
		tkB.setPos(new Vector3f(-1, 0, 0));
		
		PointConstraint constraint = new PointConstraint(sphere2, sphere, new Vector3f(-1, 0, 0), new Vector3f(1, 0, 0));
//		SliderConstraint constraint = new SliderConstraint(testphys.getSphere(), testphys2.getSphere(), tkA, tkB, false);
		
		PhysicsEngine.addConstraint(constraint);
		
//		testphys.getSphere().setMassProps(4);
//		testphys2.getSphere().setMassProps(4);
		
		
		
		Entity entity2 = new Entity();
		entity2.addComponent(new AddComponent());
		
		addEntity(entity2);
		
		
		Entity stair1 = new Entity();
		
		BoxCollider box1 = new BoxCollider(new Vector3f(0.5f, 0.15f, 0.75f));
		
		stair1.getTransform().setPos(new Vector3f(5, -1 + 0.15f, 10));
		stair1.getTransform().setScale(new Vector3f(0.5f, 0.15f, 0.75f));
		
		box1.setPos(stair1.getTransform().getTransformedPos());
		
		box1.setMassProps(0);
				
		stair1.addComponent(new StaticPhysicsComponent(box1)).addComponent(new MeshRenderer(new Mesh("crate 2 b.obj"), bricks));
		
		addEntity(stair1);
		
		
		Entity stair2 = new Entity();
		
		BoxCollider box2 = new BoxCollider(new Vector3f(0.5f, 0.3f, 0.75f));
		
		stair2.getTransform().setPos(new Vector3f(6, -1 + 0.3f, 10));
		stair2.getTransform().setScale(new Vector3f(0.5f, 0.3f, 0.75f));
		
		box2.setPos(stair2.getTransform().getTransformedPos());
		
		box2.setMassProps(0);
		
		stair2.addComponent(new StaticPhysicsComponent(box2)).addComponent(new MeshRenderer(new Mesh("crate 2 b.obj"), bricks));
		
		addEntity(stair2);
		
		
		Entity stair3 = new Entity();
		
		BoxCollider box3 = new BoxCollider(new Vector3f(0.5f, 0.45f, 0.75f));
		
		stair3.getTransform().setPos(new Vector3f(7, -1 + 0.45f, 10));
		stair3.getTransform().setScale(new Vector3f(0.5f, 0.45f, 0.75f));
		
		box3.setPos(stair3.getTransform().getTransformedPos());
		
		box3.setMassProps(0);
				
		stair3.addComponent(new StaticPhysicsComponent(box3)).addComponent(new MeshRenderer(new Mesh("crate 2 b.obj"), bricks));
		
		addEntity(stair3);
		
		
		Entity stair4 = new Entity();
		
		BoxCollider box4 = new BoxCollider(new Vector3f(0.5f, 0.6f, 0.75f));
		
		stair4.getTransform().setPos(new Vector3f(8, -1 + 0.6f, 10));
		stair4.getTransform().setScale(new Vector3f(0.5f, 0.6f, 0.75f));
		
		box4.setPos(stair4.getTransform().getTransformedPos());
		
		box4.setMassProps(0);
				
		stair4.addComponent(new StaticPhysicsComponent(box4)).addComponent(new MeshRenderer(new Mesh("crate 2 b.obj"), bricks));
		
		addEntity(stair4);
		
		
//		addEntity((new Entity()).addComponent(physicsEngineComponent));
		
		Entity2D e = new Entity2D();
		
//		Move2D m = new Move2D();
//		e.addComponent(m);
		
//		MeshRenderer2D c = new MeshRenderer2D(new Texture("testeroonie2.jpg"));
		MeshRenderer2D c = new MeshRenderer2D(new Texture("testeroonie2.png", true));
		Lock2D l = new Lock2D(-16/2, -16/2, new Vector2f(0.5f, 0.5f));
		e.addComponent(c).addComponent(l);
		
		e.getTransform().setPos(new Vector2f(Window.getWidth()/2-16/2, Window.getHeight()/2-16/2));
		e.getTransform().setScale(new Vector2f(16, 16));
//		e.getTransform().setRot((float)Math.toRadians(45));
		
		addEntity2D(e);
		
		Entity2D e2 = new Entity2D();
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png"));
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		Lock2D l2 = new Lock2D(-256, -40, new Vector2f(1f, 1f));
//		e2.addComponent(c2);
		e2.addComponent(l2);
		
		e2.getTransform().setPos(new Vector2f(Window.getWidth()-256, Window.getHeight()-40));
		e2.getTransform().setScale(new Vector2f(256, 40));
		
		Slider slider = new Slider(1, new Vector3f(1, 0, 0), new Vector3f(1, 1, 1), 0, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT));
		
		e2.addComponent(slider);
		
		Entity b = new Entity();
		b.addComponent(new VolumeSetter(slider));
		
		addEntity(b);
		
		addEntity2D(e2);
		
		Entity2D e22 = new Entity2D();
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png"));
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		Lock2D l22 = new Lock2D(-256, -40-29, new Vector2f(1f, 1f));
//		e2.addComponent(c2);
		e22.addComponent(l22).addComponent(new TextBox(10, "", new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), new InputKey(Input.MOUSE, Input.BUTTON_RIGHT)));
		
		e22.getTransform().setPos(new Vector2f(Window.getWidth()-256, Window.getHeight()-40-29));
		e22.getTransform().setScale(new Vector2f(256, 29));
//		e2.getTransform().
		
//		e.addChild(e2);
		addEntity2D(e22);
		
		Entity2D e3 = new Entity2D();
//		MeshRenderer2D c3 = new MeshRenderer2D(new Texture("test_tl2.png"));
//		MeshRenderer2D c3 = new MeshRenderer2D(new Texture("test_tl2.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		MeshRenderer2D c3 = new MeshRenderer2D(new Texture("Medox 5.png", true));
		Lock2D l3 = new Lock2D(0, 0, new Vector2f(0f, 0f));
		e3.addComponent(c3).addComponent(l3);
		
		e3.getTransform().setPos(new Vector2f(0, 0));
		e3.getTransform().setScale(new Vector2f(256, 256));
//		e2.getTransform().
		
//		e2.addChild(e3);
		addEntity2D(e3);
		
		
		Slider s1 = new Slider(1, new Vector3f(1, 0, 0), new Vector3f(1, 1, 1), 0, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT));
		Slider s2 = new Slider(1, new Vector3f(1, 0, 0), new Vector3f(1, 1, 1), 0, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT));
		Slider s3 = new Slider(1, new Vector3f(1, 0, 0), new Vector3f(1, 1, 1), 0, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT));
		
		
		Entity2D e10 = new Entity2D();
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png"));
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		Lock2D l10 = new Lock2D(0, -40, new Vector2f(0f, 1f));
//		e2.addComponent(c2);
		e10.addComponent(l10).addComponent(s1);
		
		e10.getTransform().setPos(new Vector2f(0, Window.getHeight()-40));
		e10.getTransform().setScale(new Vector2f(256, 40));
//		e2.getTransform().
		
//		e.addChild(e2);
		addEntity2D(e10);
		
		
		Entity2D e11 = new Entity2D();
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png"));
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		Lock2D l11 = new Lock2D(0, -80, new Vector2f(0f, 1f));
//		e2.addComponent(c2);
		e11.addComponent(l11).addComponent(s2);
		
		e11.getTransform().setPos(new Vector2f(0, Window.getHeight()-80));
		e11.getTransform().setScale(new Vector2f(256, 40));
//		e2.getTransform().
		
//		e.addChild(e2);
		addEntity2D(e11);
		
		
		Entity2D e12 = new Entity2D();
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png"));
//		MeshRenderer2D c2 = new MeshRenderer2D(new Texture("Medox 5.png", GL11.GL_TEXTURE_2D, GL11.GL_NEAREST));
		Lock2D l12 = new Lock2D(0, -120, new Vector2f(0f, 1f));
//		e2.addComponent(c2);
		e12.addComponent(l12).addComponent(s3);
		
		e12.getTransform().setPos(new Vector2f(0, Window.getHeight()-120));
		e12.getTransform().setScale(new Vector2f(256, 40));
//		e2.getTransform().
		
//		e.addChild(e2);
		addEntity2D(e12);
		
		
		spotLightObject2.addComponent(new LightSlider(spotLight2, s1, s2, s3));
		
		
//		MeshRenderer meshRendererBox3 = new MeshRenderer(meshBox, materialBox);
//		
//		Entity planeObjectBox3 = new Entity();
//		planeObjectBox3.addComponent(meshRendererBox3);
//		planeObjectBox3.getTransform().getPos().set(10, 0, 0);
////		planeObjectBox3.getTransform().rotate(new Vector3f(0, 0, 1), (float)Math.toRadians(45));
//		
////		addEntity(planeObjectBox3);
//		
//		MeshRenderer meshRendererBox4 = new MeshRenderer(meshBox, materialBox);
//		
//		Entity planeObjectBox4 = new Entity();
//		planeObjectBox4.addComponent(meshRendererBox4);
//		planeObjectBox4.getTransform().getPos().set(2, 0, 0);
//		
//		planeObjectBox3.addChild(planeObjectBox4);
//		
//		MeshRenderer meshRendererBox5 = new MeshRenderer(meshBox, materialBox);
//		
//		Entity planeObjectBox5 = new Entity();
//		planeObjectBox5.addComponent(meshRendererBox5);
//		planeObjectBox5.getTransform().getPos().set(2, 0, 0);
//		
//		planeObjectBox4.addChild(planeObjectBox5);
		
		Entity particle2 = new Entity();
		
		particle2.addComponent(new SmokeComponent()).addComponent(new SmokeComponent());
		
//		particle2.getTransform().setPos(new Vector3f(0, 0.5f, 0));
		
		addEntity(particle2);
		
//		for(int i = 0; i < 5; i++){
//			for(int j = 0; j < 5; j++){
				Entity particle = new Entity();
				
				ParticleMaterial pMat = new ParticleMaterial();
				pMat.setDiffuseMap(new Texture("fire.png", true));
				pMat.setEmissive(1);
				
				particle.addComponent(new ParticleRenderer(pMat));
				
//				particle.getTransform().setScale(new Vector3f(1.5f, 1.5f, 1.5f));
				particle.getTransform().setScale(new Vector3f(1, 1, 1));
//				particle.getTransform().setPos(new Vector3f(i*2-2.5f*2, 5, 1+j*2));
				particle.getTransform().setPos(new Vector3f(0, -0.5f, 0));
				
				addEntity(particle);
				
				Entity particle4 = new Entity();
				
				ParticleMaterial pMat2 = new ParticleMaterial();
				pMat2.setDiffuseMap(new Texture("rock2222.jpg"));
				pMat2.setEmissive(1);
				
				particle4.addComponent(new ParticleRenderer(pMat2));
				
//				particle.getTransform().setScale(new Vector3f(1.5f, 1.5f, 1.5f));
				particle4.getTransform().setScale(new Vector3f(1, 1, 1));
//				particle.getTransform().setPos(new Vector3f(i*2-2.5f*2, 5, 1+j*2));
				particle4.getTransform().setPos(new Vector3f(12, 0, 0));
				
				addEntity(particle4);
//			}
//		}
	}
	
	@Override
	public void cleanUp(){
		
	}
}
