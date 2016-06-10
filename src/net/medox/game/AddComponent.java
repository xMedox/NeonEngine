package net.medox.game;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.PhysicsComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;
import net.medox.neonengine.physics.ConeCollider;
import net.medox.neonengine.physics.CylinderCollider;
import net.medox.neonengine.physics.SphereCollider;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Texture;

public class AddComponent extends EntityComponent{
	private Transform boxTransform;
	private Transform sphereTransform;
	private Transform cylinderTransform;
	private Transform coneTransform;
	
	private Material bricks;
	
	private Mesh crateM;
	private Mesh sphereM;
	private Mesh cylinderM;
	private Mesh coneM;
	
	public AddComponent(){
		bricks = new Material();//new Texture("white.png"), new Vector3f(1, 1, 1), 0, 4
		bricks.setDiffuseMap(new Texture("rock2222.jpg"));
		bricks.setNormalMap(new Texture("rock2_normal322.png"));
		bricks.setEmissiveMap(new Texture("rock22_glow22.png"));
		bricks.setSpecularIntensity(0.25f);
		bricks.setSpecularPower(2);
		
		crateM = new Mesh("crate 2 c.obj");
		sphereM = new Mesh("sphere.obj");
		cylinderM = new Mesh("Cylinder.obj");
		coneM = new Mesh("Cone.obj");
		
		boxTransform = new Transform();
		boxTransform.setPos(new Vector3f(-5, 15, 0));
		
		sphereTransform = new Transform();
		sphereTransform.setPos(new Vector3f(0, 15, 0));
		
		cylinderTransform = new Transform();
		cylinderTransform.setPos(new Vector3f(5, 15, 0));
		
		coneTransform = new Transform();
		coneTransform.setPos(new Vector3f(10, 15, 0));
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_N)){
			Entity entity = new Entity();
			
			BoxCollider box = new BoxCollider(new Vector3f(1f, 1f, 1f));
			box.setMassProps(4);
			
			box.setTransform(boxTransform);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(box)).addComponent(new MeshRenderer(crateM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_B)){
			Entity entity = new Entity();
			
			SphereCollider sphere = new SphereCollider(1);
			sphere.setMassProps(4);
			
			sphere.setTransform(sphereTransform);
						
			getParent().addChild(entity.addComponent(new PhysicsComponent(sphere)).addComponent(new MeshRenderer(sphereM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_V)){
			Entity entity = new Entity();
			
			entity.getTransform().setScale(new Vector3f(2, 2, 2));
			
			CylinderCollider cylinder = new CylinderCollider(new Vector3f(1f, 1f, 1f));
			cylinder.setMassProps(4);
			
			cylinder.setTransform(cylinderTransform);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(cylinder)).addComponent(new MeshRenderer(cylinderM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_C)){
			Entity entity = new Entity();
			
			ConeCollider cone = new ConeCollider(1f, 2f);
			cone.setMassProps(4);
			
			cone.setTransform(coneTransform);
			
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
