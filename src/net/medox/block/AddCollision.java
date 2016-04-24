package net.medox.block;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.PhysicsComponent;
import net.medox.neonengine.rendering.Attenuation;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.PointLight;
import net.medox.neonengine.rendering.Texture;

public class AddCollision extends EntityComponent{
	private Material red;
	private Material blue;
	
	private Mesh crateM;
	
	public AddCollision(){
		red = new Material();
		red.setDiffuseMap(new Texture("R.png", true));
		red.setGlowMap(new Texture("G.png", true));
		
		blue = new Material();
		blue.setDiffuseMap(new Texture("B.png", true));
		blue.setGlowMap(new Texture("G.png", true));
		
		crateM = new Mesh("block.obj");
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_N)){
			Entity entity = new Entity();
			entity.getTransform().setPos(new Vector3f(8+0.5f, 10+0.5f, 8+0.5f));
			entity.getTransform().setScale(0.5f);
			
//			Transform t = new Transform();
//			t.setPos(new Vector3f(8, 10, 8));
			
			Box box = new Box(new Vector3f(0.5f, 0.5f, 0.5f));
			box.setMassProps(1);
			
			box.setTransform(/*t*/entity.getTransform());
			
			entity.addComponent(new PhysicsComponent(box));
			
			if(Util.randomInt(0, 1) == 0){
				entity.addComponent(new MeshRenderer(crateM, red));
				
				entity.addComponent(new PointLight(new Vector3f(1, 0, 0), 4f, new Attenuation(0, 0, 1)));
			}else{
				entity.addComponent(new MeshRenderer(crateM, blue));
				
				entity.addComponent(new PointLight(new Vector3f(0, 0, 1), 4f, new Attenuation(0, 0, 1)));
			}
			
			getParent().addChild(entity);
		}
		
		if(Input.getKeyDown(Input.KEY_M)){
			getParent().removeChildren();
		}
	}
}
