package net.medox.game;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.components.PhysicsComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.lighting.Attenuation;
import net.medox.neonengine.lighting.PointLight;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;
import net.medox.neonengine.physics.ConeCollider;
import net.medox.neonengine.physics.CylinderCollider;
import net.medox.neonengine.physics.Ray;
import net.medox.neonengine.physics.SphereCollider;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Texture;

public class AddComponent extends EntityComponent{
//	private Entity selected;
//	private Collider selectedCollider;
	
	private Transform boxTransform;
	private Transform sphereTransform;
	private Transform cylinderTransform;
	private Transform coneTransform;
	
	private Material bricks;
	private Material metal;
	
	private Mesh crateM;
	private Mesh sphereM;
	private Mesh cylinderM;
	private Mesh coneM;
	
	public AddComponent(){
		bricks = new Material();
//		bricks.setDiffuseMap(new Texture("rockLava.jpg"));
//		bricks.setNormalMap(new Texture("rockNormal.png"));
//		bricks.setEmissiveMap(new Texture("rockLavaEmissive.png"));
//		bricks.setRoughness(1);
//		bricks.setMetallic(0);
		
//		bricks.setDiffuseMap(new Texture("plastic.png"));
//		bricks.setRoughnessMap(new Texture("plastic_rough.png"));
		
//		bricks.setDiffuseMap(new Texture("limestone-rock-albedo.png"));
//		bricks.setRoughnessMap(new Texture("limestone-rock-roughness.png"));
//		bricks.setNormalMap(new Texture("limestone-rock-normal.png"));
		
		bricks.setDiffuseMap(new Texture("bamboo-wood-semigloss-albedo.png"));
		bricks.setRoughnessMap(new Texture("bamboo-wood-semigloss-roughness.png"));
		bricks.setNormalMap(new Texture("bamboo-wood-semigloss-normal.png"));
		
//		bricks.setDiffuseMap(new Texture("old-textured-fabric-albedo3.png"));
//		bricks.setRoughnessMap(new Texture("old-textured-fabric-roughness2.png"));
//		bricks.setNormalMap(new Texture("old-textured-fabric-normal.png"));
		
//		bricks.setDiffuseMap(new Texture("greasy-pan-2-albedo.png"));
//		bricks.setRoughnessMap(new Texture("greasy-pan-2-roughness.png"));
//		bricks.setNormalMap(new Texture("greasy-pan-2-normal.png"));
//		bricks.setMetallicMap(new Texture("greasy-pan-2-metal.png"));
		
//		bricks.setDiffuseMap(new Texture("patchy_cement1_Base_Color.png"));
//		bricks.setRoughnessMap(new Texture("patchy_cement1_Roughness.png"));
//		bricks.setNormalMap(new Texture("patchy_cement1_Normal.png"));
		
//		bricks.setDiffuseMap(new Texture("granitesmooth1-albedo.png"));
//		bricks.setRoughnessMap(new Texture("granitesmooth1-roughness3.png"));
//		bricks.setNormalMap(new Texture("granitesmooth1-normal2.png"));
		
//		bricks.setDiffuseMap(new Texture("gold-scuffed_basecolor.png"));
//		bricks.setRoughnessMap(new Texture("gold-scuffed_roughness.png"));
//		bricks.setMetallic(1);
		
//		bricks.setDiffuseMap(new Texture("Iron-Scuffed_basecolor.png"));
//		bricks.setRoughnessMap(new Texture("Iron-Scuffed_roughness.png"));
//		bricks.setMetallic(1);
		
//		bricks.setDiffuseMap(new Texture("Diffuse.png"));
//		bricks.setEmissiveMap(new Texture("Emissive.png"));
//		bricks.setRoughnessMap(new Texture("Roughness.png"));
//		bricks.setMetallicMap(new Texture("Metallic.png"));
		
		metal = new Material();
//		metal.setDiffuseMap(new Texture("Diffuse.png"));
//		metal.setEmissiveMap(new Texture("Emissive.png"));
//		metal.setRoughnessMap(new Texture("Roughness.png"));
//		metal.setMetallicMap(new Texture("Metallic.png"));
		metal.setDiffuseMap(new Texture("streaked-marble-albedo2.png"));
		metal.setRoughnessMap(new Texture("streaked-marble-roughness1.png"));
		metal.setMetallic(0);
		
//		metal.setDiffuseMap(new Texture("plastic.png"));
//		metal.setRoughnessMap(new Texture("plastic_rough.png"));
		
		crateM = new Mesh("crate.obj");
		sphereM = new Mesh("sphere.obj");
		cylinderM = new Mesh("cylinder.obj");
		coneM = new Mesh("cone.obj");
		
		boxTransform = new Transform();
		boxTransform.setPos(-5, 9, 0);
		
		sphereTransform = new Transform();
		sphereTransform.setPos(0, 9, 0);
		
		cylinderTransform = new Transform();
		cylinderTransform.setPos(5, 9, 0);
		
		coneTransform = new Transform();
		coneTransform.setPos(10, 9, 0);
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_J)){
			float lightInten = 2.0f;
			
//			SpotLight light = new SpotLight(new Vector3f(Util.randomFloat(), Util.randomFloat(), Util.randomFloat()), 6f+lightInten, new Attenuation(0, 0, 0.1f), (float)Math.toRadians(90f), 8, 1.0f, 0.5f, 0.000001f);
			PointLight light = new PointLight(new Vector3f(Util.randomFloat(), Util.randomFloat(), Util.randomFloat()), 6f+lightInten, new Attenuation(0, 0, 1)/*, (float)Math.toRadians(90f), 8, 1.0f, 0.5f, 0.000001f*/);
			
			Entity entity = new Entity();
			
			entity.getTransform().setPos(RenderingEngine.getMainCamera().getTransform().getTransformedPos());
//			entity.getTransform().setRot(RenderingEngine.getMainCamera().getTransform().getTransformedRot());
			
			entity.addComponent(light);
			
			getParent().addChild(entity);
		}
		
		if(Input.getKeyDown(Input.KEY_E)){
			Entity entity = new Entity();
			
			SphereCollider sphere = new SphereCollider(0.25f);
			sphere.setMassProps(800);
			sphere.setRestitution(0.1f);
			
			sphere.setPos(RenderingEngine.getMainCamera().getTransform().getTransformedPos().add(RenderingEngine.getMainCamera().getTransform().getTransformedRot().getForward().mul(1f)));
			
			sphere.applyCentralImpulse(RenderingEngine.getMainCamera().getTransform().getTransformedRot().getForward().mul(8000f));
			
			entity.getTransform().setScale(0.25f);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(sphere/*, 4, 1 | 2 | 4*/)).addComponent(new MeshRenderer(sphereM, metal))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getMouseDown(Input.BUTTON_RIGHT)){
			Ray ray = new Ray(RenderingEngine.getMainCamera().getTransform().getTransformedPos(), RenderingEngine.getMainCamera().getTransform().getTransformedPos().add(Util.mouseToRay().mul(100)));
			
			if(ray.hasHit()){
				ray.getHitCollider().activate(true);
				ray.getHitCollider().applyCentralImpulse(new Vector3f(0, 2000, 0));
//				ray.getHitCollider().setLinearVelocity(new Vector3f(0, 10, 0));
				
//				if(ray.getHitCollider().getGroup() == 1){
//					if(selected != (Entity)(ray.getHitCollider().getObject())){
//						if(selected != null){
//							selected.getTransform().setScale(selected.getTransform().getScale().sub(0.1f));
//						}
//						
//						selected = (Entity)(ray.getHitCollider().getObject());
//						
//						selected.getTransform().setScale(selected.getTransform().getScale().add(0.1f));
//						
//						selectedCollider = ray.getHitCollider();
//					}else{
//						selected.getTransform().setScale(selected.getTransform().getScale().sub(0.1f));
//						
//						selected = null;
//						
//						selectedCollider = null;
//					}
//				}
			}
		}
		
//		if(Input.getKey(Input.KEY_F)){
//			if(selected != null){
//				selectedCollider.activate(true);
//				selectedCollider.applyCentralImpulse(RenderingEngine.getMainCamera().getTransform().getTransformedPos().sub(selected.getTransform().getTransformedPos()).normalized().mul(4));
////				selectedCollider.setLinearVelocity(RenderingEngine.getMainCamera().getTransform().getTransformedPos().sub(selected.getTransform().getTransformedPos()).normalized().mul(2));
//			}
//		}
		
		if(Input.getKeyDown(Input.KEY_N)){
			Entity entity = new Entity();
			
//			entity.getTransform().setScale(0.5f);
//			BoxCollider box = new BoxCollider(0.5f, 0.5f, 0.5f);
			BoxCollider box = new BoxCollider(1f, 1f, 1f);
//			box.setMassProps(200);
			box.setMassProps(400);
			box.setRestitution(0.1f);
			
			box.setTransform(boxTransform);
			
//			box.setGroup(1);
//			box.setObject(entity);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(box/*, 1, 1*/)).addComponent(new MeshRenderer(crateM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_B)){
			Entity entity = new Entity();
			
//			entity.getTransform().setScale(0.5f);
//			SphereCollider sphere = new SphereCollider(0.5f);
			SphereCollider sphere = new SphereCollider(1f);
//			sphere.setMassProps(200);
			sphere.setMassProps(400);
			sphere.setRestitution(0.1f);
			
			sphere.setTransform(sphereTransform);
			
//			sphere.setGroup(1);
//			sphere.setObject(entity);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(sphere)).addComponent(new MeshRenderer(sphereM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_V)){
			Entity entity = new Entity();
			
//			entity.getTransform().setScale(1f);
//			CylinderCollider cylinder = new CylinderCollider(0.5f, 0.5f, 0.5f);
			entity.getTransform().setScale(2f);
			CylinderCollider cylinder = new CylinderCollider(1f, 1f, 1f);
//			cylinder.setMassProps(200);
			cylinder.setMassProps(400);
			cylinder.setRestitution(0.1f);
			
			cylinder.setTransform(cylinderTransform);
			
//			cylinder.setGroup(1);
//			cylinder.setObject(entity);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(cylinder)).addComponent(new MeshRenderer(cylinderM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_C)){
			Entity entity = new Entity();
			
//			entity.getTransform().setScale(0.5f);
//			ConeCollider cone = new ConeCollider(0.5f, 1f);
			ConeCollider cone = new ConeCollider(1f, 2f);
//			cone.setMassProps(200);
			cone.setMassProps(400);
			cone.setRestitution(0.1f);
			
			cone.setTransform(coneTransform);
			
//			cone.setGroup(1);
//			cone.setObject(entity);
			
			getParent().addChild(entity.addComponent(new PhysicsComponent(cone)).addComponent(new MeshRenderer(coneM, bricks))/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/);
		}
		
		if(Input.getKeyDown(Input.KEY_O)){
//			getParent().removeComponent(this);
//			parent.removeChild(getParent());
//			getParent().removeSelf();
			getParent().removeChildren();
		}
	}
}
