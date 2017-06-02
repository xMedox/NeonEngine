package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class Skybox{
	private static final Transform transform = new Transform();
	
	private static Mesh mesh;
	
	private final Material material;
	
	private final CubeMap irradianceMap;
	private final CubeMap prefilterMap;
	
	public Skybox(/*String right, String left, String top, String bottom, String front, String back*/String filename){
		this(/*right, left, top, bottom, front, back,*/filename, false);
	}
	
	public Skybox(/*String right, String left, String top, String bottom, String front, String back,*/String filename, boolean nearest){
		if(mesh == null){
			final float vertexMin = -0.5f;
			final float vertexMax = 0.5f;
			
			final Vector3f[] vertices = new Vector3f[]{new Vector3f(vertexMin, vertexMax, vertexMax),
														new Vector3f(vertexMin, vertexMin, vertexMax),
														new Vector3f(vertexMax, vertexMax, vertexMin),
														new Vector3f(vertexMin, vertexMax, vertexMin),
														new Vector3f(vertexMin, vertexMin, vertexMin),
														new Vector3f(vertexMax, vertexMin, vertexMin),
														new Vector3f(vertexMax, vertexMax, vertexMax),
														new Vector3f(vertexMax, vertexMin, vertexMax)};
			
			final IndexedModel model = new IndexedModel();
			
			for(int i = 0; i < vertices.length; i++){
				model.addVertex(vertices[i]);
			}
			
			model.addFace(2, 3, 4);
			model.addFace(2, 4, 5);
			
			model.addFace(3, 0, 1);
			model.addFace(3, 1, 4);
			
			model.addFace(6, 2, 5);
			model.addFace(6, 5, 7);
			
			model.addFace(0, 6, 7);
			model.addFace(0, 7, 1);
			
			model.addFace(3, 2, 6);
			model.addFace(3, 6, 0);
			
			model.addFace(5, 4, 1);
			model.addFace(5, 1, 7);
			
			mesh = new Mesh("", model.finalizeModel());
		}
		
//		IntBuffer w = BufferUtils.createIntBuffer(1);
//		IntBuffer h = BufferUtils.createIntBuffer(1);
//		IntBuffer comp = BufferUtils.createIntBuffer(1);
//		
//		FloatBuffer data = stbi_loadf("res/textures/" + filename + ".hdr", w, h, comp, 0);
//		
//		if(data == null){
//			System.out.println("ERROR");
//			//TODO call the error method
//		}
//		
//		int width = w.get();
//		int height = h.get();
//		
//		
//		int hdrTexture = GL11.glGenTextures();
//		
//		GL11.glBindTexture(GL11.GL_TEXTURE_2D, hdrTexture);
////		RenderingEngine.textureBound.put(0, textureID[i]);
//		
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
//		
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
//		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
//		
//		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL30.GL_RGB16F, width, height, 0, GL11.GL_RGB, GL11.GL_FLOAT, data);
//		
//		stbi_image_free(data);
		
		Texture hdrTexture = new Texture(filename + ".hdr", GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RGB16F, GL11.GL_RGB, GL11.GL_FLOAT, true);
		
		
		material = new Material();
		
		Entity e = new Entity();
		Camera camera = new Camera((float)Math.toRadians(90.0f), 1, 0.1f, 1.0f);
		e.addComponent(camera);
		
		Shader equirectangular = new Shader("equirectangular");
		
		CubeMap cubeMap = new CubeMap(512, 512, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_RGB16F, GL11.GL_RGB, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
		
		equirectangular.bind();
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				camera.getTransform().lookAt(new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
			}else if(i == 1){
				camera.getTransform().lookAt(new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
			}else if(i == 2){
				camera.getTransform().lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
			}else if(i == 3){
				camera.getTransform().lookAt(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
			}else if(i == 4){
				camera.getTransform().lookAt(new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
			}else if(i == 5){
				camera.getTransform().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
			}
			
			cubeMap.bindAsRenderTarget(i);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			equirectangular.updateUniforms(transform, material, camera);
			
//			GL13.glActiveTexture(GL13.GL_TEXTURE0 + 0);
//			GL11.glBindTexture(GL11.GL_TEXTURE_2D, hdrTexture);
			hdrTexture.bind(0);
			
			mesh.draw();
		}
		
		equirectangular.cleanUp();
		
//		GL11.glDeleteTextures(hdrTexture);
		hdrTexture.cleanUp();
		
		//TODO stop generating the mipmaps before
		cubeMap.generateMipmap();
		
		material.setCubeMap("cubeMap", cubeMap);
		
		
		Shader irradianceShader = new Shader("irradiance");
		
		irradianceMap = new CubeMap(32, 32, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RGB16F, GL11.GL_RGB, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
		
		irradianceShader.bind();
		
		for(int i = 0; i < 6; i++){
			if(i == 0){
				camera.getTransform().lookAt(new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
			}else if(i == 1){
				camera.getTransform().lookAt(new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
			}else if(i == 2){
				camera.getTransform().lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
			}else if(i == 3){
				camera.getTransform().lookAt(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
			}else if(i == 4){
				camera.getTransform().lookAt(new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
			}else if(i == 5){
				camera.getTransform().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
			}
			
			irradianceMap.bindAsRenderTarget(i);
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			irradianceShader.updateUniforms(transform, material, camera);
			mesh.draw();
		}
		
		irradianceShader.cleanUp();
		
		
		
		Shader prefilterShader = new Shader("prefilter");
		
		final int prefilterWidth = 256;
		final int prefilterHeight = 256;
		prefilterMap = new CubeMap(prefilterWidth, prefilterHeight, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR_MIPMAP_LINEAR, GL30.GL_RGB16F, GL11.GL_RGB, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
		
		prefilterShader.bind();
		
		material.setMetallic(material.getCubeMap("cubeMap").getWidth()[0]);
		
		int maxMipLevels = 5;
		for(int mip = 0; mip < maxMipLevels; mip++){
		    float roughness = (float)mip / (float)(maxMipLevels - 1);
		    material.setRoughness(roughness);
		    
		    prefilterMap.changeRenderBufferSize((int)(prefilterWidth * Math.pow(0.5, mip)), (int)(prefilterHeight * Math.pow(0.5, mip)));
			for(int i = 0; i < 6; i++){
				if(i == 0){
					camera.getTransform().lookAt(new Vector3f(-1, 0, 0), new Vector3f(0, -1, 0));
				}else if(i == 1){
					camera.getTransform().lookAt(new Vector3f(1, 0, 0), new Vector3f(0, -1, 0));
				}else if(i == 2){
					camera.getTransform().lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
				}else if(i == 3){
					camera.getTransform().lookAt(new Vector3f(0, -1, 0), new Vector3f(0, 1, 0));
				}else if(i == 4){
					camera.getTransform().lookAt(new Vector3f(0, 0, 1), new Vector3f(0, -1, 0));
				}else if(i == 5){
					camera.getTransform().lookAt(new Vector3f(0, 0, -1), new Vector3f(0, -1, 0));
				}
				
				prefilterMap.bindAsRenderTarget(i, mip);
				
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				prefilterShader.updateUniforms(transform, material, camera);
				mesh.draw();
			}
		}
		
		prefilterShader.cleanUp();
		
//		material.setCubeMap("cubeMap", irradianceMap);
	}
	
	public CubeMap getCubeMap(){
		return material.getCubeMap("cubeMap");
	}
	
	public CubeMap getIrradianceMap(){
		return irradianceMap;
	}
	
	public CubeMap getPrefilterMap(){
		return prefilterMap;
	}
	
	public void render(Shader shader, Camera camera){
		transform.setPos(RenderingEngine.getMainCamera().getTransform().getTransformedPos());
		
		shader.bind();
		shader.updateUniforms(transform, material, camera);
		mesh.draw();
	}
}
