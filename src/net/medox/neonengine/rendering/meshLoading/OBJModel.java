package net.medox.neonengine.rendering.meshLoading;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.core.Util;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

public class OBJModel{
	private final List<Vector3f> positions;
	private final List<Vector2f> texCoords;
	private final List<Vector3f> normals;
	private final List<OBJIndex> indices;
	
	private boolean hasTexCoords;
	private boolean hasNormals;
	
	public OBJModel(String fileName){
		positions = new ArrayList<Vector3f>();
		texCoords = new ArrayList<Vector2f>();
		normals = new ArrayList<Vector3f>();
		indices = new ArrayList<OBJIndex>();
		hasTexCoords = false;
		hasNormals = false;
		
		BufferedReader meshReader = null;
		
		try{
			meshReader = new BufferedReader(new FileReader(fileName));
			String line;
			
			while((line = meshReader.readLine()) != null){
				String[] tokens = line.split(" ");
				tokens = Util.removeEmptyStrings(tokens);
				
				if(tokens.length == 0 || tokens[0].equals("#")){
					continue;
				}else if(tokens[0].equals("v")){
					positions.add(new Vector3f(Float.valueOf(tokens[1]),
											   Float.valueOf(tokens[2]),
											   Float.valueOf(tokens[3])));
				}else if(tokens[0].equals("vt")){
					texCoords.add(new Vector2f(Float.valueOf(tokens[1]),
											   1.0f - Float.valueOf(tokens[2])));
				}else if(tokens[0].equals("vn")){
					normals.add(new Vector3f(Float.valueOf(tokens[1]),
							   				   Float.valueOf(tokens[2]),
							   				   Float.valueOf(tokens[3])));
				}else if(tokens[0].equals("f")){
					for(int i = 0; i < tokens.length - 3; i++){
						indices.add(parseOBJIndex(tokens[1]));
						indices.add(parseOBJIndex(tokens[2 + i]));
						indices.add(parseOBJIndex(tokens[3 + i]));
					}
				}
			}
			
			meshReader.close();
		}catch(IOException e){
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public IndexedModel toIndexedModel(){
		final IndexedModel result = new IndexedModel();
		final IndexedModel normalModel = new IndexedModel();
		final Map<OBJIndex, Integer> resultIndexMap = new ConcurrentHashMap<OBJIndex, Integer>();
		final Map<Integer, Integer> normalIndexMap = new ConcurrentHashMap<Integer, Integer>();
		final Map<Integer, Integer> indexMap = new ConcurrentHashMap<Integer, Integer>();

		for(int i = 0; i < indices.size(); i++){
			final OBJIndex currentIndex = indices.get(i);

			final Vector3f currentPosition = positions.get(currentIndex.getVertexIndex());
			Vector2f currentTexCoord;
			Vector3f currentNormal;

			if(hasTexCoords){
				currentTexCoord = texCoords.get(currentIndex.getTexCoordIndex());
			}else{
				currentTexCoord = new Vector2f(0.0f, 0.0f);
			}
			
			if(hasNormals){
				currentNormal = normals.get(currentIndex.getNormalIndex());
			}else{
				currentNormal = new Vector3f(0.0f, 0.0f, 0.0f);
			}
			
			Integer modelVertexIndex = resultIndexMap.get(currentIndex);

			if(modelVertexIndex == null){
				modelVertexIndex = result.getPositions().size();
				resultIndexMap.put(currentIndex, modelVertexIndex);

				result.getPositions().add(currentPosition);
				result.getTexCoords().add(currentTexCoord);
				if(hasNormals){
					result.getNormals().add(currentNormal);
				}
			}

			Integer normalModelIndex = normalIndexMap.get(currentIndex.getVertexIndex());

			if(normalModelIndex == null){
				normalModelIndex = normalModel.getPositions().size();
				normalIndexMap.put(currentIndex.getVertexIndex(), normalModelIndex);

				normalModel.getPositions().add(currentPosition);
				normalModel.getTexCoords().add(currentTexCoord);
				normalModel.getNormals().add(currentNormal);
				normalModel.getTangents().add(new Vector3f(0.0f, 0.0f, 0.0f));
			}

			result.getIndices().add(modelVertexIndex);
			normalModel.getIndices().add(normalModelIndex);
			indexMap.put(modelVertexIndex, normalModelIndex);
		}

		if(!hasNormals){
			normalModel.calcNormals();

			for(int i = 0; i < result.getPositions().size(); i++){
				result.getNormals().add(normalModel.getNormals().get(indexMap.get(i)));
			}
		}

		normalModel.calcTangents();

		for(int i = 0; i < result.getPositions().size(); i++){
			result.getTangents().add(normalModel.getTangents().get(indexMap.get(i)));
		}

		return result;
	}
	
	private OBJIndex parseOBJIndex(String token){
		final String[] values = token.split("/");
		
		final OBJIndex result = new OBJIndex();
		result.setVertexIndex(Integer.parseInt(values[0]) - 1);
		
		if(values.length > 1){
			if(!values[1].isEmpty()){
				hasTexCoords = true;
				result.setTexCoordIndex(Integer.parseInt(values[1]) - 1);
			}
			
			if(values.length > 2){
				hasNormals = true;
				result.setNormalIndex(Integer.parseInt(values[2]) - 1);
			}
		}
		
		return result;
	}
	
	private class OBJIndex{
		private int vertexIndex;
		private int texCoordIndex;
		private int normalIndex;
		
		public int getVertexIndex(){
			return vertexIndex;
		}
		
		public int getTexCoordIndex(){
			return texCoordIndex;
		}
		
		public int getNormalIndex(){
			return normalIndex;
		}

		public void setVertexIndex(int val){
			vertexIndex = val;
		}
		
		public void setTexCoordIndex(int val){
			texCoordIndex = val;
		}
		
		public void setNormalIndex(int val){
			normalIndex = val;
		}
		
		@Override
		public boolean equals(Object obj){
			final OBJIndex index = (OBJIndex)obj;

			return vertexIndex == index.vertexIndex
					&& texCoordIndex == index.texCoordIndex
					&& normalIndex == index.normalIndex;
		}
		
		@Override
		public int hashCode(){
			int result = 17;
			
			result = 31 * result + vertexIndex;
			result = 31 * result + texCoordIndex;
			result = 31 * result + normalIndex;
			
			return result;
		}
	}
}
