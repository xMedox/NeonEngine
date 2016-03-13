package net.medox.game;

//import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.Cone;
import net.medox.neonengine.physics.Cylinder;
import net.medox.neonengine.physics.PhysicsComponent;
import net.medox.neonengine.physics.Sphere;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Texture;

public class AddComponent extends EntityComponent{
	private Material bricks;
	
	private Mesh crateM;
	private Mesh sphereM;
	private Mesh cylinderM;
	private Mesh coneM;
	
	public AddComponent(){
		bricks = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		bricks.setTexture("diffuse", new Texture("rock2222.jpg"));
//		bricks.setTexture("diffuse", new Texture("white.png"));
//		bricks.setTexture("dispMap", new Texture("rock2_disp.jpg"));
		bricks.setTexture("normalMap", new Texture("rock2_normal322.png"));
		bricks.setTexture("glowMap", new Texture("rock22_glow22.png"));
		bricks.setFloat("specularIntensity", 0.25f);
		bricks.setFloat("specularPower", 2);
//		bricks.setFloat("dispMapScale", 0.01f);
//		bricks.setFloat("dispMapBias", -0.5f);
		
		crateM = new Mesh("crate 2 c.obj");
		sphereM = new Mesh("sphere.obj");
		cylinderM = new Mesh("Cylinder.obj");
		coneM = new Mesh("Cone.obj");
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_N)){
			Entity entity = new Entity();
			
			Box box = new Box(new Vector3f(1f, 1f, 1f));
			box.setMassProps(4);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(box)).addComponent(new MeshRenderer(crateM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_B)){
			Entity entity = new Entity();
			
			Sphere sphere = new Sphere(1);
			sphere.setMassProps(4);
						
			getParent().addChild(entity.addComponent(new PhysicsComponent(sphere)).addComponent(new MeshRenderer(sphereM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_V)){
			Entity entity = new Entity();
			
			entity.getTransform().setScale(new Vector3f(2, 2, 2));
			
			Cylinder cylinder = new Cylinder(new Vector3f(1f, 1f, 1f));
			cylinder.setMassProps(4);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(cylinder)).addComponent(new MeshRenderer(cylinderM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_C)){
			Entity entity = new Entity();
			
			Cone cone = new Cone(1f, 2f);
			cone.setMassProps(4);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(cone)).addComponent(new MeshRenderer(coneM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_O)){
//			getParent().removeComponent(this);
//			parent.removeChild(getParent());
//			getParent().removeSelf();
			getParent().removeChildren();
		}
	}
	
//	@Override
//	public void cleanUp(){
//		System.out.println("WOW ich bin tot");
//	}
}
