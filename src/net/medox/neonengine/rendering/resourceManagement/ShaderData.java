package net.medox.neonengine.rendering.resourceManagement;

import java.util.List;

import net.medox.neonengine.core.ReferenceCounter;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;

public class ShaderData extends ReferenceCounter{
	public ShaderData(String fileName){
		super();
	}
	
	public List<String> getUniformNames(){
		return null;
	}
	
	public List<String> getUniformTypes(){
		return null;
	}
	
	public void bind(){
		
	}
	
	public void setUniformi(String uniformName, int value){
		
	}
	
	public void setUniformf(String uniformName, float value){
		
	}

	public void setUniformVector3f(String uniformName, Vector3f value){
		
	}
	
//	public void setUniformVector2f(String uniformName, Vector2f value){
//	
//	}

	public void setUniformMatrix4f(String uniformName, Matrix4f value){
		
	}
	
	public void dispose(){
		
	}
}
