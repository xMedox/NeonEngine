package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBodyHelpers;

import net.medox.neonengine.math.Vector3f;

public class SoftBody extends CollisionBase{
	private btSoftBody body;
	
	public SoftBody(){
		super(SOFTBODY);
		
		float width = 4;
		float height = 20;
		
		body = btSoftBodyHelpers.CreatePatch(PhysicsEngine.getWorldInfo(), 
				new Vector3(-width, height, -width),
				new Vector3(width, height, -width),
				new Vector3(-width, height, width),
				new Vector3(width, height, width),
				2, 10, 4+8, true);
		
//		body = new btSoftBody(worldInfo, meshPart.mesh.getVerticesBuffer(), meshPart.mesh.getVertexSize(), 0, 0, meshPart.mesh.getIndicesBuffer(), meshPart.offset, meshPart.size, indexMap, 0);
		
		
//		body.setConfig_viterations(50);
//		body.setConfig_piterations(50);
//		body.setConfig_kPR(1000);
		body.setTotalMass(10);
	}
	
	public btSoftBody getBody(){
		return body;
	}
	
//	public void render(){
//		 GL11.glColor3f(1, 0, 1);
//		 GL11.glBegin(GL11.GL_TRIANGLES);
////	        for(int i=0;i<body.getFaceCount();i++)
////	        {
////	                for(int j=0;j<3;j++)
////	                        glVertex3f(b->m_faces[i].m_n[j]->m_x.x(),
////	                                   b->m_faces[i].m_n[j]->m_x.y(),
////	                                   b->m_faces[i].m_n[j]->m_x.z());
////	                                 
////	        }
//		 		 
//		 for(int i = 0; i < body.getFaceCount(); i++){
////			 SWIGTYPE_p_p_btSoftBody__Node node = body.getFace(i).getN();
////			 
////			 Node n = (node == 0) ? null : new Node(node, false);
//			 
//			 GL11.glVertex3f(body.getNode(i).getX().getX(), body.getNode(i).getX().getY(), body.getNode(i).getX().getZ());
//			}
//	        GL11.glEnd();
//	}
	
	public int getVertexNumber(){
		return body.getNodeCount();
	}
	
	public List<Vector3f> getVertices(){
		List<Vector3f> vertices = new ArrayList<Vector3f>();
		
//		System.out.println("START");
		
		for(int i = 0; i < body.getNodeCount(); i++){
			vertices.add(new Vector3f(body.getNode(i).getX().getX(), body.getNode(i).getX().getY(), body.getNode(i).getX().getZ()));
			
//			System.out.println(new Vector3f(body.getNode(i).getX().getX(), body.getNode(i).getX().getY(), body.getNode(i).getX().getZ()));
		}
		
//		System.out.println("END");
		
		return vertices;
	}
	
//	public void updateDynamicMesh(DynamicMesh m){
//		body.getVertices(gh, body.getFaceCount()*3, 3, 0);
//	}
}
