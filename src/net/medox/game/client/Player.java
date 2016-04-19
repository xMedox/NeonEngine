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
			materialBox.setDiffuseMap(new Texture("bricks.jpg"));
			materialBox.setNormalMap(new Texture("bricks_normal.jpg"));
			materialBox.setSpecularIntensity(0.5f);
			materialBox.setSpecularPower(2);
			
			MeshRenderer meshRendererBox = new MeshRenderer(meshBox, materialBox);
			
			addComponent(meshRendererBox);
			
			Mesh akmesh = new Mesh("AK-47 15.obj");
			Material akmaterial = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
			akmaterial.setDiffuseMap(new Texture("test_tl.png"));
			akmaterial.setSpecularIntensity(0.75f);
			akmaterial.setSpecularPower(2);
			
			Material akmaterialGold = new Material();//new Texture("test2.png"), new Vector3f(1, 1, 1), 1, 8
			akmaterialGold.setDiffuseMap(new Texture("bricks2.jpg"));
			akmaterialGold.setNormalMap(new Texture("bricks2_normal.jpg"));
			akmaterialGold.setSpecularIntensity(0.75f);
			akmaterialGold.setSpecularPower(2);
			
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
