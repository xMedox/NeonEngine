package net.medox.pbr;

import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.FullscreenSetter;
import net.medox.neonengine.components.MoveComponent;
import net.medox.neonengine.components.ScreenshotTaker;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Game;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.lighting.DirectionalLight;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Cursor;
import net.medox.neonengine.rendering.Font;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Skybox;
import net.medox.neonengine.rendering.Texture;
import net.medox.neonengine.rendering.Window;

public class TestGame extends Game{
	public static void main(String[] args){
		NeonEngine.enableProfiling(true);
		NeonEngine.enableVSync(false);
		NeonEngine.enableFXAA(true);
		NeonEngine.enableShadows(true);
		NeonEngine.enable2D(true);
		NeonEngine.enableParticles(true);
		NeonEngine.enableBloom(true);
		NeonEngine.setTextureQuality(0);
		NeonEngine.setShadowQuality(0);
		NeonEngine.setRenderQuality(1);
		
		NeonEngine.init(new TestGame(), /*600*/60);
		
		Window.setStartTitle("PBR Test");
		Window.setStartSize(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("iconAk16.png", "iconAk32.png");
		Window.setStartCursor(new Cursor("cursor.png", 0, 0));
		
		NeonEngine.createWindow();
		
		NeonEngine.start();
	}
	
	@Override
	public void init(){
		super.init();
		
		RenderingEngine.setMainFont(new Font("font.ttf", 16, false));
//		RenderingEngine.setMainSkybox(new Skybox("Arches_E_PineTree_3k"));
		RenderingEngine.setMainSkybox(new Skybox("newport_loft"));
//		RenderingEngine.setMainSkybox(new Skybox("Milkyway_small"));
//		RenderingEngine.setMainSkybox(new Skybox("WinterForest_Ref"));
//		RenderingEngine.setMainSkybox(new Skybox("Tropical_Beach_3k"));
//		RenderingEngine.setMainSkybox(new Skybox("Frozen_Waterfall_Ref"));
//		RenderingEngine.setMainSkybox(new Skybox("hdrvfx_0012_sand_v11_Ref"));
//		RenderingEngine.setMainSkybox(new Skybox("untitled4"));
//		RenderingEngine.setMainSkybox(new Skybox("skyboxTest"));
		
		Entity gameObject = new Entity().addComponent(new FullscreenSetter()).addComponent(new ScreenshotTaker()).addComponent(new ChangeMode());
		addEntity(gameObject);
		
		Entity player = new Entity();
		Camera cam = new Camera((float)Math.toRadians(65.0f), 0.01f, 400.0f);
		player.addComponent(cam).addComponent(new LookComponent(0.15f)).addComponent(new MoveComponent(15f));
		addEntity(player);
		
		Mesh sphereModel = new Mesh("sphere.obj");
		
		int size = 7;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				Entity sphere = new Entity();
				
				Material sphereMaterial = new Material();
				sphereMaterial.setDiffuseMap(new Texture("R.png"));
				
				sphereMaterial.setRoughness((1.0f - (i)/(float)(size-1)));
				sphereMaterial.setMetallic((j)/(float)(size-1));
				
				MeshRenderer sphereRenderer = new MeshRenderer(sphereModel, sphereMaterial);
				
				sphere.getTransform().setPos(i*2.5f, j*2.5f, 0);
				sphere.addComponent(sphereRenderer);
				addEntity(sphere);
			}
		}
		
		Entity directionalLightObject = new Entity();
		DirectionalLight directionalLight = new DirectionalLight(new Vector3f(1, 1, 1), 1.0f, 10, 8.0f, 1.0f, 0.7f, 0.000001f);
		directionalLightObject.addComponent(directionalLight);
		
//		directionalLightObject.getTransform().setRot(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(-45)));
//		directionalLightObject.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(45));
		
		addEntity(directionalLightObject);
	}
	
	@Override
	public void cleanUp(){
		
	}
}
