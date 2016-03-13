package net.medox.game.client;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;
import net.medox.neonengine.rendering.Texture;

public class Player extends Entity{
	public String name = null;
	public int id = -1;
	public int ping = 999;
	
	public boolean created = false;
	
	public PlayerWeapon AK;
	public PlayerWeapon AKGold;
	
	public int weaponID = 0;
	
	@Override
	public void render(Shader shader, Camera camera){
		if(!created){
			Mesh meshBox = new Mesh("box.obj");
			Material materialBox = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
			materialBox.setTexture("diffuse", new Texture("bricks.jpg"));
			materialBox.setTexture("dispMap", new Texture("bricks_disp.png"));
			materialBox.setTexture("normalMap", new Texture("bricks_normal.jpg"));
			materialBox.setFloat("specularIntensity", 0.5f);
			materialBox.setFloat("specularPower", 2);
			materialBox.setFloat("dispMapScale", 0.02f);
			materialBox.setFloat("dispMapBias", -1.0f);
			
			MeshRenderer meshRendererBox = new MeshRenderer(meshBox, materialBox);
			
			addComponent(meshRendererBox);
			
			Mesh akmesh = new Mesh("AK-47 15.obj");
			Material akmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
//				akmaterial./*setTexture("diffuse", new Texture("white.png"));*/setTexture("diffuse", new Texture("bricks.jpg"));
//				try{
//					akmaterial.setTexture("diffuse", new Texture(ImageIO.read(new File("res/textures/Assault.png"))));
//				}catch(IOException e){
//					e.printStackTrace();
//				}
//				akmaterial.setTexture("diffuse", new Texture("bricks.jpg"));
//				akmaterial.setTexture("dispMap", new Texture("bricks_disp.png"));
//				akmaterial.setTexture("normalMap", new Texture("bricks_normal.jpg"));
//				akmaterial.setTexture("diffuse", new Texture("bricks2.jpg"));
//				akmaterial.setTexture("dispMap", new Texture("bricks2_disp.jpg"));
//				akmaterial.setTexture("normalMap", new Texture("bricks2_normal.jpg"));
//				akmaterial.setTexture("diffuse", new Texture("Ger - Kopie.png"));
			akmaterial.setTexture("diffuse", new Texture("test_tl.png"));
//			akmaterial.setTexture("diffuse", new Texture("AK-47.png"));
//			akmaterial.setTexture("normalMap", new Texture("AK-47_normal2.png"));
//			akmaterial.setTexture("specMap", new Texture("AK-47_specular.png"));
//				akmaterial.setFloat("specularIntensity", 0.5f);
//				akmaterial.setFloat("specularPower", 4);
			akmaterial.setFloat("specularIntensity", 0.75f);
			akmaterial.setFloat("specularPower", 2);
//				akmaterial.setVector3f("color", new Vector3f(1.0f, 0.2f, 0.2f));
			
//				MeshRenderer akmeshRenderer = new MeshRenderer(akmesh, akmaterial);
			
//				Entity akObject = new Entity();
////				akObject.getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
//				akObject.getTransform().setScale(new Vector3f(0.25f*2, 0.25f*2, 0.25f*2));
//				akObject.getTransform().rotate(new Vector3f(0, 1, 0),  (float)Math.toRadians(90));
			
			Material akmaterialGold = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
//			akmaterialGold.setTexture("diffuse", new Texture("bricks.jpg"));
//			akmaterialGold.setTexture("normalMap", new Texture("bricks_normal.jpg"));
//			akmaterialGold.setTexture("dispMap", new Texture("bricks_disp.png"));
//			akmaterialGold.setTexture("diffuse", new Texture("test.png"));
//			akmaterialGold.setTexture("diffuse", new Texture("AK-47 Gold.png"));
//			akmaterialGold.setTexture("normalMap", new Texture("AK-47 Gold_normal2.png"));
//			akmaterialGold.setTexture("specMap", new Texture("AK-47 Gold_specular.png"));
			
			akmaterialGold.setTexture("diffuse", new Texture("bricks2.jpg"));
			akmaterialGold.setTexture("dispMap", new Texture("bricks2_disp.jpg"));
			akmaterialGold.setTexture("normalMap", new Texture("bricks2_normal.jpg"));
			akmaterialGold.setFloat("dispMapScale", 0.02f);
			akmaterialGold.setFloat("dispMapBias", -1.0f);
			
//			try{
//				akmaterialGold.setTexture("diffuse", new Texture(ImageIO.read(new File("res/textures/Assault.png"))));
//			}catch(IOException e){
//				e.printStackTrace();
//			}
//			akmaterialGold.setFloat("specularIntensity", 1);
//			akmaterialGold.setFloat("specularPower", 8);
			akmaterialGold.setFloat("specularIntensity", 0.75f);
			akmaterialGold.setFloat("specularPower", 2);
//			akmaterialGold.setFloat("dispMapScale", 0.02f);
//			akmaterialGold.setFloat("dispMapBias", -1.0f);
//			akmaterialGold.setVector3f("color", new Vector3f(0.2f, 0.2f, 1.0f));
			
			float modelScale = 2f;
			
			AK = new PlayerWeapon();
			AKGold = new PlayerWeapon();
			
			AK.getTransform().setScale(new Vector3f(1f/2f, 1f/2f, 1f/2f));
			AK.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90f)));
			AK.getTransform().setPos(new Vector3f(0.325f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.0f*modelScale));
			
			AKGold.getTransform().setScale(new Vector3f(1f/2f, 1f/2f, 1f/2f));
			AKGold.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(90f)));
			AKGold.getTransform().setPos(new Vector3f(0.325f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.0f*modelScale));
			
			AK.addComponent(new MeshRenderer(akmesh, akmaterial));
			AKGold.addComponent(new MeshRenderer(akmesh, akmaterialGold));
			
			AK.isActive = true;
			AKGold.isActive = false;
			
			addChild(AK);
			addChild(AKGold);
			
			created = true;
		}
		
		super.render(shader, camera);
	}
	
	public void changeWeaponID(int ID){
		if(weaponID != ID){
			weaponID = ID;
			
			if(AK != null && AKGold != null){
				if(weaponID == 0){
					AK.isActive = true;
					AKGold.isActive = false;
				}
				
				if(weaponID == 1){
					AK.isActive = false;
					AKGold.isActive = true;
				}
			}
		}
	}
	
	public int getWeaponID(){
		return weaponID;
	}
}
