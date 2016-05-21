package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.Version;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBTextureRG;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.ProfileTimer;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class RenderingEngine{
	private static final int NUM_SHADOW_MAPS = 10;
	private static final Matrix4f BIAS_MATRIX = new Matrix4f().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));
	
	private static final ProfileTimer renderProfileTimer = new ProfileTimer();
	private static final ProfileTimer renderProfileTimer2D = new ProfileTimer();
	private static final ProfileTimer windowSyncProfileTimer = new ProfileTimer();
	
	public static final int TEXTURE_2D				= GL11.GL_TEXTURE_2D;
	public static final int LINEAR					= GL11.GL_LINEAR;
	public static final int NEAREST					= GL11.GL_NEAREST;
	public static final int LINEAR_MIPMAP_LINEAR	= GL11.GL_LINEAR_MIPMAP_LINEAR;
	public static final int RGBA					= GL11.GL_RGBA;
	public static final int NONE					= GL11.GL_NONE;
	public static final int UNSIGNED_BYTE			= GL11.GL_UNSIGNED_BYTE;
	
	private static BatchRenderer batchRenderer;
	
	private static Map<String, Texture> textureMap;
//	private static Map<String, CubeMap> cubeMapMap;
	private static Map<String, Vector3f> vector3fMap;
//	private static Map<String, Vector2f> vector2fMap;
	private static Map<String, Float> floatMap;
	
	private static Map<String, Integer> samplerMap;
	
	private static Camera mainCamera;
	private static Camera camera2D;
	
	private static List<BaseLight> lights;
	private static BaseLight activeLight;
	private static Camera lightCamera;
	private static Matrix4f lightMatrix;
	
	private static Skybox skybox;
	private static Font font;
	
	private static Shader forwardAmbientShader;
	private static Shader forwardParticleAmbientShader;
	private static Shader bloomCombineShader;
	private static Shader bloomSwitchShader;
	private static Shader forwardParticleShader;
	private static Shader shadowMappingShader;
	private static Shader particleShadowMappingShader;
	private static Shader nullFilter;
	private static Shader gausBlurFilter;
	private static Shader fxaaFilter;
	private static Shader shader2D;
	private static Shader skyboxShader;
	
	private static Camera particleCamera;
	private static Shader particleShader;
	private static boolean particleFlipFaces;
	
	private static Camera filterCamera;
	private static Mesh filterPlane;
	private static Material filterMaterial;
	private static Transform filterTransform;
	
	private static Texture[] shadowMaps = new Texture[NUM_SHADOW_MAPS];
	private static Texture[] shadowMapTempTargets = new Texture[NUM_SHADOW_MAPS];
	
//	private static CubeMap[] shadowCubeMaps = new CubeMap[NUM_SHADOW_MAPS];
//	private static CubeMap[] shadowCubeMapTempTargets = new CubeMap[NUM_SHADOW_MAPS];
	
	private static boolean wireframeMode; //TODO remove this
	
	public static void init(){
		//TODO remove this
		System.out.println("--------------------------------------------------------------");
		System.out.println("Engine version:   " + NeonEngine.getVersion());
		System.out.println("OS name:          " + System.getProperty("os.name"));
		System.out.println("OS version:       " + System.getProperty("os.version"));
		System.out.println("OS arch:          " + System.getProperty("os.arch"));
		System.out.println("Arch data model:  " + System.getProperty("sun.arch.data.model"));
		System.out.println("Java version:     " + System.getProperty("java.version"));
		System.out.println("Rendering Mode:   OpenGL(" + GL11.glGetString(GL11.GL_VERSION) + ")");
		System.out.println("Max Texture size: " + GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
		System.out.println("LWJGL version:    " + Version.getVersion());
		System.out.println("--------------------------------------------------------------");
		
		batchRenderer = new BatchRenderer();
		
		textureMap = new HashMap<String, Texture>();
//		cubeMapHashMap = new HashMap<String, CubeMap>();
		vector3fMap = new HashMap<String, Vector3f>();
//		vector2fHashMap = new  HashMap<String, Vector2f>();
		floatMap = new HashMap<String, Float>();
		
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("filterTexture", 0);
		samplerMap.put("cubeMap", 0);
		
		samplerMap.put("diffuseMap", 0);
		samplerMap.put("normalMap", 1);
//		samplerMap.put("dispMap", 2);
//		samplerMap.put("specMap", 3);
//		samplerMap.put("emissiveMap", 4);
//		samplerMap.put("shadowMap", 5);
		samplerMap.put("specMap", 2);
		samplerMap.put("emissiveMap", 3);
		samplerMap.put("shadowMap", 4);
		
		lights = new ArrayList<BaseLight>();
		
		setVector3f("ambient", new Vector3f(0.15f, 0.15f, 0.15f));
		
		setFloat("fxaaSpanMax", 8.0f);
		setFloat("fxaaReduceMin", 1.0f/128.0f);
		setFloat("fxaaReduceMul", 1.0f/8.0f);
		setFloat("fxaaAspectDistortion", 150.0f);
		
		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		camera2D = new Camera(0, Window.getWidth(), 0, Window.getHeight(), -1, 1);
		new Entity().addComponent(camera2D);
		
		setTexture("displayTexture", new Texture(Window.getWidth(), Window.getHeight(), new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_UNSIGNED_BYTE, GL11.GL_UNSIGNED_BYTE}, true, new int[]{ARBFramebufferObject.GL_COLOR_ATTACHMENT0, ARBFramebufferObject.GL_COLOR_ATTACHMENT1}));
		
		setTexture("bloomTexture1", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
		setTexture("bloomTexture2", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
		
		forwardAmbientShader = new Shader("forwardAmbient");
		forwardParticleAmbientShader = new Shader("forwardParticleAmbient");
		bloomCombineShader = new Shader("bloomCombine");
		bloomSwitchShader = new Shader("bloomSwitch");
		forwardParticleShader = new Shader("forwardParticleForward");
		shadowMappingShader =  new Shader("shadowMapping");
		particleShadowMappingShader = new Shader("paricleShadowMapping");
		nullFilter = new Shader("filterNull");
		gausBlurFilter = new Shader("filterGausBlur");
		fxaaFilter = new Shader("filterFxaa");
		shader2D = new Shader("shader2D");
		skyboxShader = new Shader("skyboxShader");
		
		lightCamera = new Camera();
		new Entity().addComponent(lightCamera);
		
		filterCamera = new Camera();
		new Entity().addComponent(filterCamera);
		filterCamera.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(180.0f));
		
		filterTransform = new Transform();
		filterTransform.rotate(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90.0f)));
		filterTransform.rotate(new Quaternion(new Vector3f(0, 0, 1), (float)Math.toRadians(180.0f)));
		
		filterMaterial = new Material();
		
		final IndexedModel filterIndexed = new IndexedModel();
		
		filterIndexed.addVertex(new Vector3f(1f, 0f, 1f));
		filterIndexed.addTexCoord(new Vector2f(1f, 1f));
		
		filterIndexed.addVertex(new Vector3f(-1f, 0f, 1f));
		filterIndexed.addTexCoord(new Vector2f(0f, 1f));
		
		filterIndexed.addVertex(new Vector3f(1f, 0f, -1f));
		filterIndexed.addTexCoord(new Vector2f(1f, 0f));
		
		filterIndexed.addVertex(new Vector3f(-1f, 0f, -1f));
		filterIndexed.addTexCoord(new Vector2f(0f, 0f));
		
		filterIndexed.addFace(1, 0, 2);
		filterIndexed.addFace(3, 1, 2);
		
		filterPlane = new Mesh("", filterIndexed.finalizeModel());
		
		for(int i = 0; i < NUM_SHADOW_MAPS; i++){
			final int shadowMapSize = 1 << (i + 1);
			
			shadowMaps[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, ARBTextureRG.GL_RG32F, GL11.GL_RGBA, GL11.GL_FLOAT, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
			shadowMapTempTargets[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, ARBTextureRG.GL_RG32F, GL11.GL_RGBA, GL11.GL_FLOAT, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
			
//			shadowCubeMaps[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, GL11.GL_FLOAT, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
//			shadowCubeMapTempTargets[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, GL11.GL_FLOAT, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
		}
	}
	
	public static double displayRenderTime(double dividend){
		return renderProfileTimer.displayAndReset("Render Time: ", dividend);
	}
	
	public static double display2DRenderTime(double dividend){
		return renderProfileTimer2D.displayAndReset("2DRender Time: ", dividend);
	}
	
	public static double displayWindowSyncTime(double dividend){
		return windowSyncProfileTimer.displayAndReset("Window Sync Time: ", dividend);
	}
	
	public static void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType){
		throw new IllegalArgumentException(uniformType + " is not a supported type in Rendering Engine");
	}
	
	public static int getFPS(){
		return NeonEngine.fps;
	}
	
	public static void render(Entity object){
		renderProfileTimer.startInvocation();
		
		mainCamera.updateFrustum();
		
		//TODO remove this
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
		
		getTexture("displayTexture").bindAsRenderTarget();
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		renderSkybox();
		
		object.renderAll(forwardAmbientShader, mainCamera);
		
		if(NeonEngine.OPTION_ENABLE_PARTICLES == 1){
			particleCamera = mainCamera;
			particleShader = forwardParticleAmbientShader;
			particleFlipFaces = false;
			
			batchRenderer.render(particleShader, mainCamera);
		}
		
		for(int i = 0; i < lights.size(); i++){
			activeLight = lights.get(i);
			final ShadowInfo shadowInfo = activeLight.getShadowInfo();
			
			if(NeonEngine.OPTION_ENABLE_SHADOWS == 1){
				int shadowMapIndex = 0;
				
				if(shadowInfo.getShadowMapSizeAsPowerOf2() != 0){
					shadowMapIndex = shadowInfo.getShadowMapSizeAsPowerOf2() - 1;
				}
				
				setTexture("shadowMap", shadowMaps[shadowMapIndex]);
				shadowMaps[shadowMapIndex].bindAsRenderTarget();
				
				GL11.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
				
				if(shadowInfo.getShadowMapSizeAsPowerOf2() == 0){
					lightMatrix = new Matrix4f().initScale(0, 0, 0);
					setFloat("shadowVarianceMin", 0.00002f);
					setFloat("shadowLightBleedingReduction", 0.0f);
				}else{
					lightCamera.changeMode(shadowInfo.getBase());
					
					final ShadowCameraTransform shadowCameraTransform = activeLight.calcShadowCameraTransform(mainCamera.getTransform().getTransformedPos(), mainCamera.getTransform().getTransformedRot());
					lightCamera.getTransform().setPos(shadowCameraTransform.pos);
					lightCamera.getTransform().setRot(shadowCameraTransform.rot);
					
					lightMatrix = BIAS_MATRIX.mul(lightCamera.getViewProjection());
					
					lightCamera.updateFrustum();
					
					setFloat("shadowVarianceMin", shadowInfo.getMinVariance());
					setFloat("shadowLightBleedingReduction", shadowInfo.getLightBleedReductionAmount());
					
					if(shadowInfo.getFlipFaces()){
						GL11.glCullFace(GL11.GL_FRONT);
					}
					
					GL11.glEnable(GL32.GL_DEPTH_CLAMP);
					
					object.renderAll(shadowMappingShader, lightCamera);
					
					if(NeonEngine.OPTION_ENABLE_PARTICLES == 1){
						particleCamera = lightCamera;
						particleShader = particleShadowMappingShader;
						particleFlipFaces = shadowInfo.getFlipFaces();
						
						batchRenderer.render(particleShader, lightCamera);
					}
					
					GL11.glDisable(GL32.GL_DEPTH_CLAMP);
					
					if(shadowInfo.getFlipFaces()){
						GL11.glCullFace(GL11.GL_BACK);
					}
					
					final float shadowSoftness = shadowInfo.getShadowSoftness();
					
					if(shadowSoftness != 0){
						blurShadowMap(shadowMapIndex, shadowSoftness);
					}
				}
				
				getTexture("displayTexture").bindAsRenderTarget();
			}
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthMask(false);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			
			object.renderAll(activeLight.getShader(), mainCamera);
			
			if(NeonEngine.OPTION_ENABLE_PARTICLES == 1){
				particleCamera = mainCamera;
				particleShader = forwardParticleShader;
				particleFlipFaces = false;
				
				batchRenderer.render(particleShader, mainCamera);
			}
			
			GL11.glDepthMask(true);
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		//TODO remove this
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		
		if(NeonEngine.OPTION_ENABLE_BLOOM == 1){
			applyFilter(bloomSwitchShader, getTexture("displayTexture"), getTexture("bloomTexture1"));
			
			blurBloomMap(8f);
			blurBloomMap(2f);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//			GL11.glDepthMask(false);
//			GL11.glDepthFunc(GL11.GL_EQUAL);
			
			applyFilter(bloomCombineShader, getTexture("bloomTexture1"), getTexture("displayTexture"));
			
//			GL11.glDepthMask(true);
//			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		renderProfileTimer.stopInvocation();
		
		windowSyncProfileTimer.startInvocation();
		
		if(NeonEngine.OPTION_ENABLE_FXAA == 1){
			setVector3f("inverseFilterTextureSize", new Vector3f(1.0f/(float)getTexture("displayTexture").getWidth(), 1.0f/((float)getTexture("displayTexture").getHeight() + (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight() * getFloat("fxaaAspectDistortion")), 0.0f));
			
			applyFilter(fxaaFilter, getTexture("displayTexture"), null);
		}else{
			applyFilter(nullFilter, getTexture("displayTexture"), null);
		}
		
		windowSyncProfileTimer.stopInvocation();
	}
	
	private static void renderSkybox(){
		if(skybox != null){
			GL11.glDepthMask(false);
			
			skybox.render(skyboxShader, mainCamera);
			
			GL11.glDepthMask(true);
		}
	}
	
	private static void applyFilter(Shader filter, Texture source, Texture dest){
		if(dest == null){
			Window.bindAsRenderTarget();
		}else{
			dest.bindAsRenderTarget();
		}
		
		setTexture("filterTexture", source);
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		filter.bind();
		filter.updateUniforms(filterTransform, filterMaterial, filterCamera);
		filterPlane.draw();
		
		setTexture("filterTexture", null);
	}
	
	private static void blurShadowMap(int shadowMapIndex, float blurAmount){
		setVector3f("blurScale", new Vector3f(blurAmount/(shadowMaps[shadowMapIndex].getWidth()), 0.0f, 0.0f));
		applyFilter(gausBlurFilter, shadowMaps[shadowMapIndex], shadowMapTempTargets[shadowMapIndex]);
		
		setVector3f("blurScale", new Vector3f(0.0f, blurAmount/(shadowMaps[shadowMapIndex].getHeight()), 0.0f));
		applyFilter(gausBlurFilter, shadowMapTempTargets[shadowMapIndex], shadowMaps[shadowMapIndex]);
	}
	
	private static void blurBloomMap(float blurAmount){
		setVector3f("blurScale", new Vector3f(blurAmount/getTexture("displayTexture").getWidth(), blurAmount/getTexture("displayTexture").getHeight(), 0.0f));
		applyFilter(gausBlurFilter, getTexture("bloomTexture1"), getTexture("bloomTexture2"));
		
		setVector3f("blurScale", new Vector3f(-blurAmount/getTexture("displayTexture").getWidth(), blurAmount/getTexture("displayTexture").getHeight(), 0.0f));
		applyFilter(gausBlurFilter, getTexture("bloomTexture2"), getTexture("bloomTexture1"));
	}
	
	public static void render(Entity2D object){
		renderProfileTimer2D.startInvocation();
		if(NeonEngine.OPTION_ENABLE_2D == 1){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			object.renderAll();
			
			batchRenderer.render(shader2D, camera2D);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		renderProfileTimer2D.stopInvocation();
	}
	
	public static boolean particleInFrustum(Transform transform, Camera camera){
		return camera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max());
	}
	
	public static boolean mesh2DInFrustum(Transform2D transform){
//		if((int)transform.getScale().getX() >= 0 && (int)transform.getScale().getY() >= 0){
//			return (int)transform.getTransformedPos().getX() + (int)transform.getScale().getX() >= 0 && (int)transform.getTransformedPos().getX() <= Window.getWidth() &&
//				   (int)transform.getTransformedPos().getY() + (int)transform.getScale().getY() >= 0 && (int)transform.getTransformedPos().getY() <= Window.getHeight();
//		}else if((int)transform.getScale().getX() >= 0 && (int)transform.getScale().getY() < 0){
//			
//		}else if((int)transform.getScale().getX() < 0 && (int)transform.getScale().getY() >= 0){
//			
//		}else if((int)transform.getScale().getX() < 0 && (int)transform.getScale().getY() < 0){
//			
//		}
//		
//		return false;
		return (int)transform.getTransformedPos().getX() + (int)transform.getScale().getX() >= 0 && (int)transform.getTransformedPos().getX() <= Window.getWidth() && (int)transform.getTransformedPos().getY() + (int)transform.getScale().getY() >= 0 && (int)transform.getTransformedPos().getY() <= Window.getHeight();
	}
	
	public static void renderParticle(Transform trans, ParticleMaterial material){
		renderParticle(trans, material, new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public static void renderParticle(Transform trans, ParticleMaterial material, Vector2f minUV, Vector2f maxUV){
		if(NeonEngine.OPTION_ENABLE_PARTICLES == 1){
			batchRenderer.addMesh(particleShader, particleCamera, particleFlipFaces, trans, material, minUV, maxUV);
		}
	}
	
	public static void render2DMesh(Transform2D trans, Texture texture){
		render2DMesh(trans, texture, new Vector3f(1, 1, 1), new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public static void render2DMesh(Transform2D trans, Texture texture, Vector3f color){
		render2DMesh(trans, texture, color, new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public static void render2DMesh(Transform2D trans, Texture texture, Vector3f color, Vector2f minUV, Vector2f maxUV){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, texture, color, minUV, maxUV);
	}
	
	public static void render2DMesh(Transform2D trans, int id){
		render2DMesh(trans, id, new Vector3f(1, 1, 1));
	}
	
	public static void render2DMesh(Transform2D trans, int id, Vector3f color){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, id, color);
	}
	
	public static void renderMesh(Shader shader, Transform trans, Mesh mesh, Material material, Camera camera){
		shader.bind();
		shader.updateUniforms(trans, material, camera);
		mesh.draw();
	}
	
	public static void drawString(float xPos, float yPos, String text, Vector3f color){
		drawString(xPos, yPos, text, color, 1, 1);
	}
	
	public static void drawString(float xPos, float yPos, String text, Vector3f color, float scaleX, float scaleY){
		if(font != null){
			font.drawString(xPos, yPos, text, color, scaleX, scaleY);
		}
	}
	
	public static void addLight(BaseLight baseLight){
		lights.add(baseLight);
	}
	
	public static void removeLight(BaseLight baseLight){
		lights.remove(baseLight);
	}
	
	public static int getSamplerSlot(String samplerName){
		return samplerMap.get(samplerName);
	}
	
	public static BaseLight getActiveLight(){
		return activeLight;
	}
	
	public static Camera getMainCamera(){
		return mainCamera;
	}
	
	public static Skybox getMainSkybox(){
		return skybox;
	}
	
	public static Font getMainFont(){
		return font;
	}
	
	public static Shader getForwardAmbient(){
		return forwardAmbientShader;
	}
	
	public static void setMainCamera(Camera mainCamera){
		RenderingEngine.mainCamera = mainCamera;
	}
	
	public static void setMainSkybox(Skybox skybox){
		RenderingEngine.skybox = skybox;
	}
	
	public static void setMainFont(Font font){
		RenderingEngine.font = font;
	}
	
	public static Matrix4f getLightMatrix(){
		return lightMatrix;
	}
	
	public static void updateViewport(){
		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		mainCamera.update();
		camera2D.update();
		
		setTexture("displayTexture", new Texture(Window.getWidth(), Window.getHeight(), new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_UNSIGNED_BYTE, GL11.GL_UNSIGNED_BYTE}, true, new int[]{ARBFramebufferObject.GL_COLOR_ATTACHMENT0, ARBFramebufferObject.GL_COLOR_ATTACHMENT1}));
		
		setTexture("bloomTexture1", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
		setTexture("bloomTexture2", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
	}
	
	public static void setTexture(String name, Texture texture){
		textureMap.remove(name);
		textureMap.put(name, texture);
	}
	
//	public void setCubeMap(String name, CubeMap cubeMap){
//		cubeMapHashMap.remove(name);
//		cubeMapHashMap.put(name, cubeMap);
//	}
	
	public static void setVector3f(String name, Vector3f vector3f){
		vector3fMap.remove(name);
		vector3fMap.put(name, vector3f);
	}
	
//	public static void setVector2f(String name, Vector2f vector2f){
//		vector2fHashMap.remove(name);
//		vector2fHashMap.put(name, vector2f);
//	}
	
	public static void setFloat(String name, float floatValue){
		floatMap.remove(name);
		floatMap.put(name, floatValue);
	}
	
	public static Texture getTexture(String name){
		return textureMap.get(name);
	}
	
//	public static CubeMap getCubeMap(String name){
//		return cubeMapHashMap.get(name);
//	}
	
	public static Vector3f getVector3f(String name){
		return vector3fMap.get(name);
	}
	
//	public static Vector2f getVector2f(String name){
//		return vector2fHashMap.get(name);
//	}
	
	public static float getFloat(String name){
		return floatMap.get(name);
	}
	
	public static void setAmbiet(Vector3f ambient){
		setVector3f("ambient", ambient);
	}
	
	public static Vector3f getAmbiet(){
		return getVector3f("ambient");
	}
	
	public static void setWireframeMode(boolean wire){ //TODO remove this
		wireframeMode = wire;
	}
	
	public static boolean isWireframeMode(){ //TODO remove this
		return wireframeMode;
	}
	
	public static void dispose(){
		batchRenderer.dispose();
	}
}
