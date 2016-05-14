package net.medox.neonengine.rendering;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.resourceManagement.ShaderData;
import net.medox.neonengine.rendering.resourceManagement.opengl.ShaderDataGL;

public class Shader{
	private static final Map<String, ShaderData> loadedShaders = new ConcurrentHashMap<String, ShaderData>();
	
	private final String fileName;
	private ShaderData resource;
	
	public Shader(String fileName){
		this.fileName = fileName;
		
		String actualFileName = fileName;
		if(NeonEngine.PROFILING_DISABLE_SHADING != 0){
			actualFileName = "nullShader";
		}
		
		resource = loadedShaders.get(actualFileName);
		
		if(resource == null){
			if(RenderingEngine.RENDERING_MODE == RenderingEngine.OPENGL){
				resource = new ShaderDataGL(fileName);
			}else if(RenderingEngine.RENDERING_MODE == RenderingEngine.VULKAN){
				resource = new ShaderData(fileName);
			}
			loadedShaders.put(actualFileName, resource);
		}else{
			resource.addReference();
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		if(resource.removeReference() && !fileName.isEmpty()){
			resource.dispose();
			loadedShaders.remove(fileName);
		}
		
		super.finalize();
	}
	
	public void bind(){
		resource.bind();
	}
	
	public void updateAmbientUniforms(Transform transform, Material material, Camera camera){
		setUniformMatrix4f("T_MVP", camera.getViewProjection().mul(transform.getTransformation()));
		
		setUniformVector3f("R_ambient", RenderingEngine.getVector3f("ambient"));
		
		final int samplerSlot = RenderingEngine.getSamplerSlot("diffuseMap");
		material.getTexture("diffuseMap").bind(samplerSlot);
		setUniformi("diffuseMap", samplerSlot);
		
		final int samplerSlot2 = RenderingEngine.getSamplerSlot("emissiveMap");
		material.getTexture("emissiveMap").bind(samplerSlot2);
		setUniformi("emissiveMap", samplerSlot2);
	}
	
	public void updateShadowUniforms(Transform transform, Material material, Camera camera){
		setUniformMatrix4f("T_MVP", camera.getViewProjection().mul(transform.getTransformation()));
		
		final int samplerSlot = RenderingEngine.getSamplerSlot("diffuseMap");
		material.getTexture("diffuseMap").bind(samplerSlot);
		setUniformi("diffuseMap", samplerSlot);
	}
	
	public void updateSkyboxUniforms(Transform transform, Material material, Camera camera){
		setUniformMatrix4f("T_MVP", camera.getViewProjection().mul(transform.getTransformation()));
		
		final int samplerSlot = RenderingEngine.getSamplerSlot("cubeMap");
		material.getCubeMap("cubeMap").bind(samplerSlot);
		setUniformi("cubeMap", samplerSlot);
	}
	
	public void updateBatchRendererUniforms(Transform transform, Camera camera){
		setUniformMatrix4f("T_MVP", camera.getViewProjection().mul(transform.getTransformation()));
		
		setUniformi("T0_texture", 0);
		setUniformi("T1_texture", 1);
		setUniformi("T2_texture", 2);
		setUniformi("T3_texture", 3);
		setUniformi("T4_texture", 4);
		setUniformi("T5_texture", 5);
		setUniformi("T6_texture", 6);
		setUniformi("T7_texture", 7);
		setUniformi("T8_texture", 8);
		setUniformi("T9_texture", 9);
		setUniformi("T10_texture", 10);
		setUniformi("T11_texture", 11);
		setUniformi("T12_texture", 12);
		setUniformi("T13_texture", 13);
		setUniformi("T14_texture", 14);
		setUniformi("T15_texture", 15);
		setUniformi("T16_texture", 16);
		setUniformi("T17_texture", 17);
		setUniformi("T18_texture", 18);
		setUniformi("T19_texture", 19);
		setUniformi("T20_texture", 20);
		setUniformi("T21_texture", 21);
		setUniformi("T22_texture", 22);
		setUniformi("T23_texture", 23);
		setUniformi("T24_texture", 24);
		setUniformi("T25_texture", 25);
		setUniformi("T26_texture", 26);
		setUniformi("T27_texture", 27);
		setUniformi("T28_texture", 28);
		setUniformi("T29_texture", 29);
		setUniformi("T30_texture", 30);
		setUniformi("T31_texture", 31);
	}
	
	public void updateUniforms(Transform transform, Material material, Camera camera){
		final Matrix4f worldMatrix = transform.getTransformation();
		final Matrix4f MVPMatrix = camera.getViewProjection().mul(worldMatrix);
		
		for(int i = 0; i < resource.getUniformNames().size(); i++){
			final String uniformName = resource.getUniformNames().get(i);
			final String uniformType = resource.getUniformTypes().get(i);
			
			if(uniformName.charAt(0) == 'R'){
				final String unprefixedName = uniformName.substring(1);
				
				if(unprefixedName.charAt(0) == '_'){
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
					}else{
						RenderingEngine.updateUniformStruct(transform, material, this, unprefixedUniformName, uniformType);
					}
				}else if(unprefixedName.charAt(1) == '_'){
					if(unprefixedName.charAt(0) == '0'){
						final String unprefixedUniformName = uniformName.substring(3);
						
						if(uniformType.equals("sampler2D")){
//							int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getTexture(unprefixedUniformName).bind(10, 0);
							setUniformi(uniformName, 10);
						}
					}else if(unprefixedName.charAt(0) == '1'){
						final String unprefixedUniformName = uniformName.substring(3);
						
						if(uniformType.equals("sampler2D")){
//							int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getTexture(unprefixedUniformName).bind(11, 1);
							setUniformi(uniformName, 11);
						}
					}/*else if(unprefixedName.charAt(0) == '2'){
						final String unprefixedUniformName = uniformName.substring(3);
						
						if(uniformType.equals("sampler2D")){
//							int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getTexture(unprefixedUniformName).bind(12, 2);
							setUniformi(uniformName, 12);
						}
					}else if(unprefixedName.charAt(0) == '3'){
						final String unprefixedUniformName = uniformName.substring(3);
						
						if(uniformType.equals("sampler2D")){
//							int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getTexture(unprefixedUniformName).bind(13, 3);
							setUniformi(uniformName, 13);
						}
					}else if(unprefixedName.charAt(0) == '4'){
						final String unprefixedUniformName = uniformName.substring(3);
						
						if(uniformType.equals("sampler2D")){
//							int samplerSlot = RenderingEngine.getSamplerSlot(unprefixedUniformName);
							RenderingEngine.getTexture(unprefixedUniformName).bind(14, 4);
							setUniformi(uniformName, 14);
						}
					}*/
				}
			}else if(uniformType.equals("sampler2D")){
				final int samplerSlot = RenderingEngine.getSamplerSlot(uniformName);
				material.getTexture(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}else if(uniformType.equals("samplerCube")){
				final int samplerSlot = RenderingEngine.getSamplerSlot(uniformName);
				material.getCubeMap(uniformName).bind(samplerSlot);
				setUniformi(uniformName, samplerSlot);
			}else if(uniformName.startsWith("T_")){
				final String unprefixedUniformName = uniformName.substring(2);
				
				if(unprefixedUniformName.equals("MVP")){
					setUniformMatrix4f(uniformName, MVPMatrix);
				}else if(unprefixedUniformName.equals("model")){
					setUniformMatrix4f(uniformName, worldMatrix);
				}else{
					throw new IllegalArgumentException(uniformName + " is not a valid component of Transform");
				}
			}/*else if(uniformName.startsWith("C0_")){
				if(uniformName.equals("C0_eyePos")){
					setUniformVector3f(uniformName, RenderingEngine.getMainCamera().getTransform().getTransformedPos());
				}else{
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			}*/else if(uniformName.startsWith("C_")){
				final String unprefixedUniformName = uniformName.substring(2);
				
				if(unprefixedUniformName.equals("eyePos")){
					setUniformVector3f(uniformName, camera.getTransform().getTransformedPos());
				}else{
					throw new IllegalArgumentException(uniformName + " is not a valid component of Camera");
				}
			}else{
				if(uniformType.equals("vec3")){
					setUniformVector3f(uniformName, material.getVector3f(uniformName));
				}else if(uniformType.equals("float")){
					setUniformf(uniformName, material.getFloat(uniformName));
				}else{
					throw new IllegalArgumentException(uniformType + " is not a supported type in Material");
				}
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
