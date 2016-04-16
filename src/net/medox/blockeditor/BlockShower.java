package net.medox.blockeditor;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Texture;

public class BlockShower extends Entity2DComponent{
	private final Texture texture;
	private final World world;
	
	public BlockShower(Texture texture, World world){
		this.texture = texture;
		this.world = world;
	}
	
	@Override
	public void render(){		
		if(RenderingEngine.mesh2DInFrustum(getTransform())){
			RenderingEngine.add2DMesh(getTransform(), texture, new Vector3f(1, 1, 1), new Vector2f(world.getSelectedTexture()[0]/10f, world.getSelectedTexture()[1]/10f), new Vector2f((world.getSelectedTexture()[0]+1)/10f, (world.getSelectedTexture()[1]+1)/10f));
		}
	}
}
