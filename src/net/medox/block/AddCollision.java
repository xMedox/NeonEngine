package net.medox.block;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.Box;
import net.medox.neonengine.physics.PhysicsComponent;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Texture;

public class AddCollision extends EntityComponent{
	private Material bricks;
	private Material bricks2;
	
	private Mesh crateM;
	
	public AddCollision(){
		bricks = new Material();
//		bricks.setTexture("diffuse", new Texture("block61.png", true));
		bricks.setDiffuseMap(new Texture("block61.png", true));
		bricks2 = new Material();
//		bricks2.setTexture("diffuse", new Texture("block71.png", true));
		bricks2.setDiffuseMap(new Texture("block71.png", true));
		
		crateM = new Mesh("block.obj");
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_N)){
			Entity entity = new Entity();
			entity.getTransform().setPos(new Vector3f(8, 10, 8));
			
//			Transform t = new Transform();
//			t.setPos(new Vector3f(8, 10, 8));
			
			Box box = new Box(new Vector3f(0.5f, 0.5f, 0.5f));
			box.setMassProps(1);
			
			box.setTransform(/*t*/entity.getTransform());
			
			entity.addComponent(new PhysicsComponent(box));
			
			if(Util.randomInt(0, 1) == 0){
				entity.addComponent(new MeshRenderer(crateM, bricks));
			}else{
				entity.addComponent(new MeshRenderer(crateM, bricks2));
			}
			
			getParent().addChild(entity);/*.addComponent(new PointLight(new Vector3f(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat()), 3f, new Attenuation(0, 0, 1)))*/
		}
	}
}
