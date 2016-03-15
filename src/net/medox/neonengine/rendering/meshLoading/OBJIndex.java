package net.medox.neonengine.rendering.meshLoading;

public class OBJIndex{
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
