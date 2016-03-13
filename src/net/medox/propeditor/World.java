package net.medox.propeditor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Shader;

public class World extends EntityComponent{
	private static int worldWidth = 32;
	private static int worldHeight = 32;
	private static int worldLenght = 32;
	
	private Block[][][] blocks;
	
	private BigBlock block;
	private Template template;
	
	private int selX = 0;
	private int selY = 0;
	private int selZ = 0;
	
//	private int triangles = 0;
	
	public World(){
		blocks = new Block[worldWidth][worldHeight][worldLenght];
		
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					int[] texture = {-1, 0};
					Transform transform = new Transform();
					transform.setPos(x*0.03125f+0.03125f/2-1, y*0.03125f+0.03125f/2, z*0.03125f+0.03125f/2-1);
					
					blocks[x][y][z] = new Block(texture, transform, false);
					
					if(x == 0 && y == 0 && z == 0){
						blocks[x][y][z].seleted = true;
					}
					
//					triangles += 12;
				}
			}
		}
		
		int[] texture = {0, 0};
		
		Transform transform = new Transform();
		transform.setPos(-0.5f, -0.5f, -0.5f);
		
		block = new BigBlock(texture, transform, false);
		
		template = new Template();
	}
	
//	@Override
//	public void update(float delta){
//		for(int x = 0; x < worldWidth; x++){
//			for(int y = 0; y < worldHeight; y++){
//				for(int z = 0; z < worldLenght; z++){
//					blocks[x][y][z].update(delta);
//				}
//			}
//		}
//	}
	
	public void load(){
		 try{
	    	FileInputStream fstream = new FileInputStream("World/BlocksProp.txt");
	    	DataInputStream in = new DataInputStream(fstream);
	    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
	    	String strLine = br.readLine();
	    	
	    	int x = 0;
	    	int y = 0;
	    	int z = 0;
		
	    	Pattern p = Pattern.compile(",");
            String[] article2 = p.split(strLine);
	    	
	    	for(int a2 = 0; a2 < article2.length; a2++){
                String article = article2[a2];
                
    	    	int test = Integer.parseInt(article);
    	    	
    	    	int a = -1;
    	    	int b = -1;
    	    	boolean solid = false;
    	    	
				if(test == 0){
					a = -1;
					b = 0;
					
					solid = false;
				}else if(test == 1){
					a = 0;
					b = 0;
					
					solid = true;
				}
    	    	
    	    	int[] ab = {a, b};
    	    	
    	    	blocks[x][y][z].texture = ab;
    	    	blocks[x][y][z].solid = solid;
        	    
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
	    }catch (Exception e){
	    	
		}
	}
	
	public void save(){
		String output = "";
		
		boolean start = true;
    	
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					int save = (blocks[x][y][z].texture[0]+1)+(blocks[x][y][z].texture[1]*10);
					
					String s = "";
					
					if(start){
						s = String.valueOf(save);
						
						start = false;
					}else{
						s = "," + String.valueOf(save);
					}
					
					output += s;
				}
			}
    	}
		
		Util.saveToFile("World/BlocksProp.txt", output);
	}
	
	public void saveModel(){
		String output = "";
		
		ArrayList<Vector3f> vertices = new ArrayList<Vector3f>();
    	ArrayList<Integer[]> faces = new ArrayList<Integer[]>();
    	
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					if(blocks[x][y][z].solid){
//						int texture = (blocks[x][y][z].texture[0]+1)*(blocks[x][y][z].texture[1]+1)*4-4;
						int texture = ((blocks[x][y][z].texture[0]+1)+(blocks[x][y][z].texture[1])*10)*4-4;
						
						if(x > 0){
							if(!blocks[x-1][y][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x, y+1, z+1));
								
								Integer[] fa = {id+3, 4+texture, 5, id+1, 3+texture, 5, id, 2+texture, 5};
								Integer[] fb = {id, 2+texture, 5, id+2, 1+texture, 5, id+3, 4+texture, 5};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x, y+1, z+1));
							
							Integer[] fa = {id+3, 4+texture, 5, id+1, 3+texture, 5, id, 2+texture, 5};
							Integer[] fb = {id, 2+texture, 5, id+2, 1+texture, 5, id+3, 4+texture, 5};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(x < worldWidth-1){
							if(!blocks[x+1][y][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								vertices.add(new Vector3f(x+1, y, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id, 1+texture, 3, id+1, 4+texture, 3, id+3, 3+texture, 3};
								Integer[] fb = {id+3, 3+texture, 3, id+2, 2+texture, 3, id, 1+texture, 3};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							vertices.add(new Vector3f(x+1, y, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id, 1+texture, 3, id+1, 4+texture, 3, id+3, 3+texture, 3};
							Integer[] fb = {id+3, 3+texture, 3, id+2, 2+texture, 3, id, 1+texture, 3};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(y > 0){
							if(!blocks[x][y-1][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x+1, y, z+1));
								
								Integer[] fa = {id, 4+texture, 1, id+1, 3+texture, 1, id+3, 2+texture, 1};
								Integer[] fb = {id+3, 2+texture, 1, id+2, 1+texture, 1, id, 4+texture, 1};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x+1, y, z+1));
							
							Integer[] fa = {id, 4+texture, 1, id+1, 3+texture, 1, id+3, 2+texture, 1};
							Integer[] fb = {id+3, 2+texture, 1, id+2, 1+texture, 1, id, 4+texture, 1};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(y < worldHeight-1){
							if(!blocks[x][y+1][z].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								vertices.add(new Vector3f(x, y+1, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id+3, 3+texture, 2, id+1, 2+texture, 2, id, 1+texture, 2};
								Integer[] fb = {id, 1+texture, 2, id+2, 4+texture, 2, id+3, 3+texture, 2};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							vertices.add(new Vector3f(x, y+1, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id+3, 3+texture, 2, id+1, 2+texture, 2, id, 1+texture, 2};
							Integer[] fb = {id, 1+texture, 2, id+2, 4+texture, 2, id+3, 3+texture, 2};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(z > 0){
							if(!blocks[x][y][z-1].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z));
								vertices.add(new Vector3f(x+1, y, z));
								vertices.add(new Vector3f(x, y+1, z));
								vertices.add(new Vector3f(x+1, y+1, z));
								
								Integer[] fa = {id+3, 3+texture, 6, id+1, 2+texture, 6, id, 1+texture, 6};
								Integer[] fb = {id, 1+texture, 6, id+2, 4+texture, 6, id+3, 3+texture, 6};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z));
							vertices.add(new Vector3f(x+1, y, z));
							vertices.add(new Vector3f(x, y+1, z));
							vertices.add(new Vector3f(x+1, y+1, z));
							
							Integer[] fa = {id+3, 3+texture, 6, id+1, 2+texture, 6, id, 1+texture, 6};
							Integer[] fb = {id, 1+texture, 6, id+2, 4+texture, 6, id+3, 3+texture, 6};
							
							faces.add(fa);
							faces.add(fb);
						}
						
						if(z < worldLenght-1){
							if(!blocks[x][y][z+1].solid){
								int id = vertices.size()+1;
								
								vertices.add(new Vector3f(x, y, z+1));
								vertices.add(new Vector3f(x+1, y, z+1));
								vertices.add(new Vector3f(x, y+1, z+1));
								vertices.add(new Vector3f(x+1, y+1, z+1));
								
								Integer[] fa = {id, 2+texture, 4, id+1, 1+texture, 4, id+3, 4+texture, 4};
								Integer[] fb = {id+3, 4+texture, 4, id+2, 3+texture, 4, id, 2+texture, 4};
								
								faces.add(fa);
								faces.add(fb);
							}
						}else{
							int id = vertices.size()+1;
							
							vertices.add(new Vector3f(x, y, z+1));
							vertices.add(new Vector3f(x+1, y, z+1));
							vertices.add(new Vector3f(x, y+1, z+1));
							vertices.add(new Vector3f(x+1, y+1, z+1));
							
							Integer[] fa = {id, 2+texture, 4, id+1, 1+texture, 4, id+3, 4+texture, 4};
							Integer[] fb = {id+3, 4+texture, 4, id+2, 3+texture, 4, id, 2+texture, 4};
							
							faces.add(fa);
							faces.add(fb);
						}
					}
				}
			}
		}
		
		String s = "";
		
		for(int i = 0; i < vertices.size(); i++){
			s = "v " + vertices.get(i).getX() + " " + vertices.get(i).getY() + " " + vertices.get(i).getZ() + "\n";
			
			output += s;
		}
		
		s = "vt 0.0 0.9\nvt 0.1 0.9\nvt 0.1 1.0\nvt 0.0 1.0\n";
		output += s;
		s = "vt 0.1 0.9\nvt 0.2 0.9\nvt 0.2 1.0\nvt 0.1 1.0\n";
		output += s;
		s = "vt 0.2 0.9\nvt 0.3 0.9\nvt 0.3 1.0\nvt 0.2 1.0\n";
		output += s;
		s = "vt 0.3 0.9\nvt 0.4 0.9\nvt 0.4 1.0\nvt 0.3 1.0\n";
		output += s;
		s = "vt 0.4 0.9\nvt 0.5 0.9\nvt 0.5 1.0\nvt 0.4 1.0\n";
		output += s;
		s = "vt 0.5 0.9\nvt 0.6 0.9\nvt 0.6 1.0\nvt 0.5 1.0\n";
		output += s;
		s = "vt 0.6 0.9\nvt 0.7 0.9\nvt 0.7 1.0\nvt 0.6 1.0\n";
		output += s;
		s = "vt 0.7 0.9\nvt 0.8 0.9\nvt 0.8 1.0\nvt 0.7 1.0\n";
		output += s;
		s = "vt 0.8 0.9\nvt 0.9 0.9\nvt 0.9 1.0\nvt 0.8 1.0\n";
		output += s;
		s = "vt 0.9 0.9\nvt 1.0 0.9\nvt 1.0 1.0\nvt 0.9 1.0\n";
		output += s;
		s = "vt 0.0 0.8\nvt 0.1 0.8\nvt 0.1 0.9\nvt 0.0 0.9\n";
		output += s;
		s = "vt 0.1 0.8\nvt 0.2 0.8\nvt 0.2 0.9\nvt 0.1 0.9\n";
		output += s;
		s = "vt 0.2 0.8\nvt 0.3 0.8\nvt 0.3 0.9\nvt 0.2 0.9\n";
		output += s;
		s = "vt 0.3 0.8\nvt 0.4 0.8\nvt 0.4 0.9\nvt 0.3 0.9\n";
		output += s;
		s = "vt 0.4 0.8\nvt 0.5 0.8\nvt 0.5 0.9\nvt 0.4 0.9\n";
		output += s;
		s = "vt 0.5 0.8\nvt 0.6 0.8\nvt 0.6 0.9\nvt 0.5 0.9\n";
		output += s;
		
		s = "vn 0.0 -1.0 0.0\nvn 0.0 1.0 0.0\nvn 1.0 0.0 0.0\nvn 0.0 0.0 1.0\nvn -1.0 0.0 0.0\nvn 0.0 0.0 -1.0\ns off\n";
		output += s;
		
		for(int i = 0; i < faces.size(); i++){
			s = "f " + faces.get(i)[0] + "/" + faces.get(i)[1] + "/" + faces.get(i)[2] + " " +
				faces.get(i)[3] + "/" + faces.get(i)[4] + "/" + faces.get(i)[5] + " " +
				faces.get(i)[6] + "/" + faces.get(i)[7] + "/" + faces.get(i)[8] +
				"\n";
			
			output += s;
		}
		
		Util.saveToFile("res/models/blocksProp.obj", output);
	}
	
	@Override
	public void input(float delta){
		if(Input.getKeyDown(Input.KEY_LEFT_CONTROL)){
			if(selY > 0){
				blocks[selX][selY][selZ].seleted = false;
				
				selY -= 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}else if(Input.getKeyDown(Input.KEY_SPACE)){
			if(selY < worldHeight-1){
				blocks[selX][selY][selZ].seleted = false;
				
				selY += 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}else if(Input.getKeyDown(Input.KEY_LEFT)){
			if(selX > 0){
				blocks[selX][selY][selZ].seleted = false;
				
				selX -= 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}else if(Input.getKeyDown(Input.KEY_RIGHT)){
			if(selX < worldWidth-1){
				blocks[selX][selY][selZ].seleted = false;
				
				selX += 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}else if(Input.getKeyDown(Input.KEY_UP)){
			if(selZ < worldLenght-1){
				blocks[selX][selY][selZ].seleted = false;
				
				selZ += 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}else if(Input.getKeyDown(Input.KEY_DOWN)){
			if(selZ > 0){
				blocks[selX][selY][selZ].seleted = false;
				
				selZ -= 1;
				
				blocks[selX][selY][selZ].seleted = true;
			}
		}
		
		if(Input.getKeyDown(Input.KEY_BACKSPACE)){
			blocks[selX][selY][selZ].texture = new int[]{-1, 0};
			blocks[selX][selY][selZ].solid = false;
		}else if(Input.getKeyDown(Input.KEY_1)){
			blocks[selX][selY][selZ].texture = new int[]{0, 0};
			blocks[selX][selY][selZ].solid = true;
		}
		
		if(Input.getKeyDown(Input.KEY_N)){
			load();
			System.out.println("DONE LOADING");
		}
		
		if(Input.getKeyDown(Input.KEY_M)){
			save();
			saveModel();
//			saveCollision();
			System.out.println("DONE SAVING");
		}
		
		template.input(delta);
	}
	
	@Override
	public void render(Shader shader, Camera camera){
//		System.out.println(triangles);
		
		for(int x = 0; x < worldWidth; x++){
			for(int y = 0; y < worldHeight; y++){
				for(int z = 0; z < worldLenght; z++){
					blocks[x][y][z].render(shader, camera);
				}
			}
		}
		
		block.render(shader, camera);
		
		template.render(shader, camera);
	}
}
