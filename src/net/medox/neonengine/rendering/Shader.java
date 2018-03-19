package net.medox.neonengine.rendering;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.lighting.DirectionalLight;
import net.medox.neonengine.lighting.PointLight;
import net.medox.neonengine.lighting.SpotLight;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.resourceManagement.ShaderData;

public class Shader{
	private static final Map<String, ShaderData> loadedShaders = new HashMap<String, ShaderData>();
	
	private final String fileName;
	
	private ShaderData resource;
	private boolean cleanedUp;
	
	public Shader(String fileName){
		this.fileName = fileName;
		
		String actualFileName = fileName;
		if(NeonEngine.isMeshShadingDisabled()){
			actualFileName = "nullShader";
		}
		
		resource = loadedShaders.get(actualFileName);
		
		if(resource == null){
			resource = new ShaderData(fileName);
			loadedShaders.put(actualFileName, resource);
		}else{
			resource.addReference();
		}
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			if(resource.removeReference() && !fileName.equals("")){
				resource.dispose();
				loadedShaders.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void bind(){
		resource.bind();
	}
	
	public void updateUniforms(Transform transform, Material material, Camera camera){
		final Matrix4f worldMatrix = transform.getTransformation();
		final Matrix4f MVPMatrix = camera.getViewProjection().mul(worldMatrix);
		
		for(int i = 0; i < resource.getUniformNames().size(); i++){
			final String uniformName = resource.getUniformNames().get(i);
			final String uniformType = resource.getUniformTypes().get(i);
			
			if(uniformName.charAt(0) == 'X'){
				setUniformi(uniformName, 0);
			}else if(uniformName.charAt(0) == 'T'){
				if(uniformName.charAt(1) == '_'){
					final String unprefixedUniformName = uniformName.substring(2);
					
					if(unprefixedUniformName.equals("MVP")){
						setUniformMatrix4f(uniformName, MVPMatrix);
					}else if(unprefixedUniformName.equals("model")){
						setUniformMatrix4f(uniformName, worldMatrix);
					}else if(unprefixedUniformName.equals("projMatrixInv")){
						final Matrix4f projMatrixInv = RenderingEngine.getMainCamera().getProjection().invert();
						
						setUniformMatrix4f(uniformName, projMatrixInv);
					}else if(unprefixedUniformName.equals("viewMatrixInv")){
						final Matrix4f MVPMatrixInvert = RenderingEngine.getMainCamera().getView().invert();
						
						setUniformMatrix4f(uniformName, MVPMatrixInvert);
					}else if(unprefixedUniformName.equals("textures")){
						setUniformiVector(uniformName, RenderingEngine.textureArray);
					}else{
						NeonEngine.throwError("Error: " + uniformName + " is not a valid component of Transform.");
					}
				}
			}else if(uniformName.charAt(0) == 'R'){
				if(uniformName.charAt(1) == '_'){
					final String unprefixedUniformName = uniformName.substring(2);
					
					if(unprefixedUniformName.equals("lightMatrix")){
						setUniformMatrix4f(uniformName, RenderingEngine.getLightMatrix().mul(worldMatrix));
					}else if(uniformType.equals("sampler2D")){
						final int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(samplerSlot);
						setUniformi(uniformName, samplerSlot);
					}else if(uniformType.equals("vec3")){
						setUniformVector3f(uniformName, RenderingEngine.getVector3f(unprefixedUniformName));
					}/*else if(uniformType.equals("vec2")){
						setUniformVector2f(uniformName, RenderingEngine.getVector2f(unprefixedUniformName));
					}*/else if(uniformType.equals("float")){
						setUniformf(uniformName, RenderingEngine.getFloat(unprefixedUniformName));
					}else if(uniformType.equals("DirectionalLight")){
						setUniformDirectionalLight(uniformName, (DirectionalLight)RenderingEngine.getActiveLight());
					}else if(uniformType.equals("PointLight")){
						setUniformPointLight(uniformName, (PointLight)RenderingEngine.getActiveLight());
					}else if(uniformType.equals("SpotLight")){
						setUniformSpotLight(uniformName, (SpotLight)RenderingEngine.getActiveLight());
					}else if(uniformType.equals("samplerCube")){
						if(unprefixedUniformName.equals("prefilterMap")){
							final int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getMainSkybox().getPrefilterMap().bind(samplerSlot);
							setUniformi(uniformName, samplerSlot);
						}else if(unprefixedUniformName.equals("irradianceMap")){
							final int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getMainSkybox().getIrradianceMap().bind(samplerSlot);
							setUniformi(uniformName, samplerSlot);
						}else{
							NeonEngine.throwError("Error: " + uniformType + " is not a supported type in RenderingEngine.");
						}
					}else{
						RenderingEngine.updateUniformStruct(transform, material, this, unprefixedUniformName, uniformType);
					}
				}else if(uniformName.equals("R0_lightMatrix")){ 
						setUniformMatrix4f(uniformName, RenderingEngine.getLightMatrix()); 
				}else if(uniformName.charAt(2) == '_'){
					if(uniformName.charAt(1) == '0'){
						final String unprefixedUniformName = uniformName.substring(3);
						
//						int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(10, 0);
						setUniformi(uniformName, 10);
					}else if(uniformName.charAt(1) == '1'){
						final String unprefixedUniformName = uniformName.substring(3);
						
//						int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(11, 1);
						setUniformi(uniformName, 11);
					}else if(uniformName.charAt(1) == '2'){
						final String unprefixedUniformName = uniformName.substring(3);
						
//						int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(12, 2);
						setUniformi(uniformName, 12);
					}else if(uniformName.charAt(1) == '3'){
						final String unprefixedUniformName = uniformName.substring(3);
						
//						int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(13, 3);
						setUniformi(uniformName, 13);
					}else if(uniformName.charAt(1) == '4'){
						final String unprefixedUniformName = uniformName.substring(3);
						
//						int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
						RenderingEngine.getTexture(unprefixedUniformName).bind(14, 4);
						setUniformi(uniformName, 14);
					}
				}
			}else if(uniformName.charAt(0) == 'C'){
				if(uniformName.charAt(1) == '_'){
					final String unprefixedUniformName = uniformName.substring(2);
					
					if(unprefixedUniformName.equals("eyePos")){
						setUniformVector3f(uniformName, camera.getTransform().getTransformedPos());
					}else{
						NeonEngine.throwError("Error: " + uniformName + " is not a valid component of Camera.");
					}
				}else{
					if(uniformName.equals("C0_eyePos")){
						setUniformVector3f(uniformName, RenderingEngine.getMainCamera().getTransform().getTransformedPos());
					}
				}
			}else if(uniformType.equals("sampler2D")){
				final int samplerSlot = RenderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}else if(uniformType.equals("samplerCube")){
				final int samplerSlot = RenderingEngine.getSamplerSlot(uniformName);
				material.getCubeMap(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}else if(uniformType.equals("vec3")){
				setUniformVector3f(uniformName, material.getVector3f(uniformName));
			}else if(uniformType.equals("float")){
				setUniformf(uniformName, material.getFloat(uniformName));
			}else{
				NeonEngine.throwError("Error: " + uniformType + " is not a supported type in Material.");
			}
		}
	}
	
	private void setUniformi(String uniformName, int value){
		resource.setUniformi(uniformName, value);
	}
	
	private void setUniformf(String uniformName, float value){
		resource.setUniformf(uniformName, value);
	}
	
	private void setUniformVector3f(String uniformName, Vector3f value){
		resource.setUniformVector3f(uniformName, value);
	}
	
//	private void setUniformVector2f(String uniformName, Vector2f value){
//		resource.setUniformVector2f(uniformName, value);
//	}
	
	private void setUniformiVector(String uniformName, int[] value){
		resource.setUniformiVector(uniformName, value);
	}
	
	private void setUniformMatrix4f(String uniformName, Matrix4f value){
		resource.setUniformMatrix4f(uniformName, value);
	}
	
	private void setUniformDirectionalLight(String uniformName, DirectionalLight directionalLight){
		setUniformVector3f(uniformName + ".base.color", directionalLight.getColor());
		setUniformf(uniformName + ".base.intensity", directionalLight.getIntensity());
		setUniformVector3f(uniformName + ".direction", directionalLight.getTransform().getTransformedRot().getForward());
	}
	
	private void setUniformPointLight(String uniformName, PointLight pointLight){
		setUniformVector3f(uniformName + ".base.color", pointLight.getColor());
		setUniformf(uniformName + ".base.intensity", pointLight.getIntensity());
		setUniformf(uniformName + ".atten.constant", pointLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".atten.linear", pointLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".atten.exponent", pointLight.getAttenuation().getExponent());
		setUniformVector3f(uniformName + ".position", pointLight.getTransform().getTransformedPos());
		setUniformf(uniformName + ".range", pointLight.getRange());
	}
	
	private void setUniformSpotLight(String uniformName, SpotLight spotLight){
		setUniformVector3f(uniformName + ".pointLight.base.color", spotLight.getColor());
		setUniformf(uniformName + ".pointLight.base.intensity", spotLight.getIntensity());
		setUniformf(uniformName + ".pointLight.atten.constant", spotLight.getAttenuation().getConstant());
		setUniformf(uniformName + ".pointLight.atten.linear", spotLight.getAttenuation().getLinear());
		setUniformf(uniformName + ".pointLight.atten.exponent", spotLight.getAttenuation().getExponent());
		setUniformVector3f(uniformName + ".pointLight.position", spotLight.getTransform().getTransformedPos());
		setUniformf(uniformName + ".pointLight.range", spotLight.getRange());
		setUniformVector3f(uniformName + ".direction", spotLight.getTransform().getTransformedRot().getForward());
		setUniformf(uniformName + ".cutoff", spotLight.getCutoff());
	}
	
	public static void dispose(){
		for(final ShaderData data : loadedShaders.values()){
			data.dispose();
		}
	}
}
