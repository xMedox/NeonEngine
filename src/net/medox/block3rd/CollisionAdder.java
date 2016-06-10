package net.medox.block3rd;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import net.medox.neonengine.components.StaticPhysicsComponent;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.physics.BoxCollider;

public class CollisionAdder extends EntityComponent{
	public static int worldWidth = 16;
	public static int worldHeight = 16;
	public static int worldLenght = 16;
	
	public CollisionAdder(String file, Entity e, Vector3f pos){
		load(file, e, pos);
	}
	
	private void load(String file, Entity e, Vector3f pos){
		try{
	    	FileInputStream fstream = new FileInputStream(file);
	    	DataInputStream in = new DataInputStream(fstream);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	String strLine = br.readLine();
	    	
	    	int x = 0;
	    	int y = 0;
	    	int z = 0;
	    	
	    	for(int i = 0; i < strLine.length(); i++){
    	    	int test = Integer.parseInt(String.valueOf(strLine.charAt(i)));
    	    	
				if(test == 1){
					addCollision(x+(int)pos.getX(), y+(int)pos.getY(), z+(int)pos.getZ(), e);
				}
        	    
    	    	if(z < worldWidth){
    	    		z ++;
    	    	}

    	    	if(z == worldWidth){
    	    		z = 0;
    	    		
          	    	if(y < worldHeight){
        	    		y ++;
        	    	}

          	    	if(y == worldHeight){
        	    		y = 0;
        	    		
              	    	if(x < worldLenght){
            	    		x ++;
            	    	}

              	    	if(x == worldLenght){
            	    		x = 0;
            	    	}
        	    	}
    	    	}
	    	}
	    	in.close();
	    }catch (Exception e1){
	    	
		}
	}
	
	private void addCollision(int x, int y, int z, Entity e){
		BoxCollider box = new BoxCollider(new Vector3f(0.5f, 0.5f, 0.5f));
		box.setMassProps(0);
		
		Transform t = new Transform();
		t.setPos(new Vector3f(x+0.5f, y+0.5f, z+0.5f));
		
		box.setTransform(t);
				
		e.addComponent(new StaticPhysicsComponent(box));
	}
}
