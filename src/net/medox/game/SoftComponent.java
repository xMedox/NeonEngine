package net.medox.game;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.physics.PhysicsEngine;
import net.medox.neonengine.physics.SoftBody;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.DynamicMesh;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Shader;

public class SoftComponent extends EntityComponent{
	private SoftBody body;
	private DynamicMesh dy;
	private final Material material;
	
	public SoftComponent(){
		body = new SoftBody();
		
		dy = new DynamicMesh(body.getVertexNumber(), body.getVertexNumber(), body.getVertexNumber(), body.getVertexNumber(), body.getVertexNumber());
		
		material = new Material();
	}
	
	@Override
	public void render(Shader shader, Camera camera){
//		if(mesh.inFrustum(getTransform(), camera)){
			dy.updatePositions(body.getVertices());
			
			RenderingEngine.renderDynamicMesh(shader, getTransform(), dy, material, camera);
//		}
		
//		body.render();
	}
	
	@Override
	public void addToEngine(){
		PhysicsEngine.addSoftBody(body);
	}
	
	@Override
	public void cleanUp(){
		PhysicsEngine.removeSoftBody(body);
	}
}
