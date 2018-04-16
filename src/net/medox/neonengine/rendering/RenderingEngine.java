package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.ProfileTimer;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.lighting.BaseLight;
import net.medox.neonengine.lighting.ShadowCameraTransform;
import net.medox.neonengine.lighting.ShadowInfo;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class RenderingEngine{
	public static final int DIFFUSE_STATE			= 0;
	public static final int SHADOW_STATE			= 1;
	public static final int LIGHTING_STATE			= 2;
	
	public static final int TEXTURE_2D				= GL11.GL_TEXTURE_2D;
	public static final int LINEAR					= GL11.GL_LINEAR;
	public static final int NEAREST					= GL11.GL_NEAREST;
	public static final int LINEAR_MIPMAP_LINEAR	= GL11.GL_LINEAR_MIPMAP_LINEAR;
	public static final int NEAREST_MIPMAP_LINEAR	= GL11.GL_NEAREST_MIPMAP_LINEAR;
	public static final int RGBA					= GL11.GL_RGBA;
	public static final int NONE					= GL11.GL_NONE;
	public static final int UNSIGNED_BYTE			= GL11.GL_UNSIGNED_BYTE;
	
	private static final int NUM_SHADOW_MAPS = 10;
	private static final Matrix4f BIAS_MATRIX = new Matrix4f().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));
	private static final Matrix4f NO_SHADOW_MATRIX = new Matrix4f().initScale(0, 0, 0);
	
	public static int maxTextureImageUnits;
	public static int[] textureArray;
	
	private static ProfileTimer renderProfileTimer;
	private static ProfileTimer renderProfileTimer2D;
	private static ProfileTimer windowSyncProfileTimer;
	
//	public static int meshBound;
//	public static int shaderBound;
//	public static Map<Integer, Integer> textureBound;
	
	private static int renderingState;
	
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
	
	private static List<Shader> filters;
	
	private static Shader forwardAmbientShader;
	private static Shader forwardParticleAmbientShader;
	private static Shader ambientShader;
	private static Shader shadowMappingShader;
	private static Shader particleShadowMappingShader;
	private static Shader skyboxShader;
	private static Shader bloomCombineShader;
	private static Shader bloomSwitchShader;
	private static Shader shader2D;
	private static Shader gausBlurFilter;
	private static Shader nullFilter;
	private static Shader fxaaFilter;
	private static Shader hdrFilter;
	
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
	
	private static boolean wireframeMode;
	
//	private static CubeMap irradiance;
	
	private static Mesh sphereM;
	private static Material materialM;
	private static Shader testShader;
	
	public static void init(){
		sphereM = new Mesh("sphere.obj");
		materialM = new Material();
		testShader = new Shader("testShader");
		
		maxTextureImageUnits = GL11.glGetInteger(GL20.GL_MAX_TEXTURE_IMAGE_UNITS);
		
		if(NeonEngine.isProfilingEnabled()){
			System.out.println("--------------------------------------------------------------");
			System.out.println("Engine version:          " + NeonEngine.getVersion());
			System.out.println("OS name:                 " + System.getProperty("os.name"));
			System.out.println("OS version:              " + System.getProperty("os.version"));
			System.out.println("OS arch:                 " + System.getProperty("os.arch"));
			System.out.println("Arch data model:         " + System.getProperty("sun.arch.data.model"));
			System.out.println("Java version:            " + System.getProperty("java.version"));
			System.out.println("Renderer:                " + GL11.glGetString(GL11.GL_RENDERER));
			System.out.println("OpenGL version:          " + GL11.glGetString(GL11.GL_VERSION));
			System.out.println("Max Texture size:        " + GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
			System.out.println("Max Texture image units: " + maxTextureImageUnits);
			System.out.println("--------------------------------------------------------------");
			
			renderProfileTimer = new ProfileTimer();
			renderProfileTimer2D = new ProfileTimer();
			windowSyncProfileTimer = new ProfileTimer();
		}
				
		textureArray = new int[maxTextureImageUnits];
		
		for(int i = 0; i < maxTextureImageUnits; i++){
			textureArray[i] = i;
		}
		
//		meshBound = -1;
//		shaderBound = -1;
//		textureBound = new HashMap<Integer, Integer>();
//		
//		for(int i = 0; i < 32; i++){
//			textureBound.put(i, -1);
//		}
		
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
		samplerMap.put("roughnessMap", 2);
		samplerMap.put("metallicMap", 3);
		samplerMap.put("emissiveMap", 4);
		samplerMap.put("shadowMap", 5);
		samplerMap.put("irradianceMap", 5);
		samplerMap.put("prefilterMap", 6);
		samplerMap.put("brdfLUT", 7);
		
		lights = new ArrayList<BaseLight>();
		
		setFloat("fxaaSpanMax", 8.0f);
		setFloat("fxaaReduceMin", 1.0f/128.0f);
		setFloat("fxaaReduceMul", 1.0f/8.0f);
		setFloat("fxaaAspectDistortion", 150.0f);
		
//		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		
		camera2D = new Camera(0, Window.getWidth(), 0, Window.getHeight(), -1, 1);
		new Entity().addComponent(camera2D);
		
		setRenderTextures();
		
		forwardAmbientShader = new Shader("geometryPass");
		forwardParticleAmbientShader = new Shader("geometryPassParticle");
		ambientShader = new Shader("deferredAmbient");
		shadowMappingShader =  new Shader("shadowMapping");
		particleShadowMappingShader = new Shader("particleShadowMapping");
		skyboxShader = new Shader("skyboxShader");
		bloomCombineShader = new Shader("bloomCombine");
		bloomSwitchShader = new Shader("bloomSwitch");
		shader2D = new Shader("shader2D");
		gausBlurFilter = new Shader("filterGausBlur");
		nullFilter = new Shader("filterNull");
		fxaaFilter = new Shader("filterFxaa");
		hdrFilter = new Shader("filterHdr");
		
		filters = new ArrayList<Shader>();
		
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
		
		
		Shader brdfShader = new Shader("brdf");
		Texture brdf = new Texture(512, 512, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RG16F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
		
		brdf.bindAsRenderTarget();
		
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		brdfShader.bind();
		brdfShader.updateUniforms(filterTransform, filterMaterial, filterCamera);
		filterPlane.draw();
		
		brdfShader.cleanUp();
		
		setTexture("brdfLUT", brdf);
		
		
		for(int i = 0; i < NUM_SHADOW_MAPS; i++){
			final int shadowMapSize = 1 << (i + 1);
			
			shadowMaps[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
			shadowMapTempTargets[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
			
//			shadowCubeMaps[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
//			shadowCubeMapTempTargets[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, GL30.GL_RG32F, GL30.GL_RG, GL11.GL_FLOAT, true, GL30.GL_COLOR_ATTACHMENT0);
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
		NeonEngine.throwError("Error: " + uniformType + " is not a supported type in Rendering Engine.");
	}
	
	public static int getRenderingState(){
		return renderingState;
	}
	
	public static int getFPS(){
		return NeonEngine.getFPS();
	}
	
	public static void render(Entity object){
		if(NeonEngine.isProfilingEnabled()){
			renderProfileTimer.startInvocation();
		}
		
		mainCamera.updateFrustum();
		
		getTexture("renderTexture").bindAsRenderTarget();
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
		
		renderingState = DIFFUSE_STATE;
		
		if(NeonEngine.areParticlesEnabled()){
			particleCamera = mainCamera;
			particleShader = forwardParticleAmbientShader;
			particleFlipFaces = false;
		}
		
		object.renderAll(forwardAmbientShader, mainCamera);
		
		if(NeonEngine.areParticlesEnabled()){
			batchRenderer.render(particleShader, mainCamera);
		}
		
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		
		getTexture("displayTexture").bindAsRenderTarget();
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		
		applyFilter(ambientShader, getTexture("renderTexture"), getTexture("displayTexture"));
		
		
//		getTexture("displayTexture").bindAsRenderTarget();
//		
//		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
//		ambientShader.bind();
//		ambientShader.updateUniforms(filterTransform, filterMaterial, filterCamera);
//		filterPlane.draw();
		
		
//		activeLight = lights.get(0);
//		
//		GL11.glEnable(GL11.GL_BLEND);
//		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//		GL11.glDepthMask(false);
//		GL11.glDepthFunc(GL11.GL_EQUAL);
//		
//		renderingState = LIGHTING_STATE;
//		
//		applyFilter(activeLight.getShader(), getTexture("displayTexture"), getTexture("renderTexture"));
//		
//		GL11.glDepthMask(true);
//		GL11.glDepthFunc(GL11.GL_LESS);
//		GL11.glDisable(GL11.GL_BLEND);
		
		getTexture("renderTexture").bindAsReadTarget();
		getTexture("displayTexture").bindAsDrawTarget();
		GL30.glBlitFramebuffer(0, 0, getTexture("displayTexture").getWidth(), getTexture("displayTexture").getHeight(), 0, 0, getTexture("displayTexture").getWidth(), getTexture("displayTexture").getHeight(), GL11.GL_DEPTH_BUFFER_BIT, GL11.GL_NEAREST);
		
		getTexture("displayTexture").bindAsRenderTarget();
		
		for(int i = 0; i < lights.size(); i++){
			activeLight = lights.get(i);
			
			final ShadowInfo shadowInfo = activeLight.getShadowInfo();
			
			if(NeonEngine.areShadowsEnabled()){
				int shadowMapIndex = 0;
				
				if(shadowInfo.getShadowMapSizeAsPowerOf2() != 0){
					shadowMapIndex = shadowInfo.getShadowMapSizeAsPowerOf2() - 1;
				}
				
				setTexture("shadowMap", shadowMaps[shadowMapIndex]);
				
				if(shadowInfo.getShadowMapSizeAsPowerOf2() == 0){
					lightMatrix = NO_SHADOW_MATRIX;
					setFloat("shadowVarianceMin", 0.00002f);
					setFloat("shadowLightBleedingReduction", 0.0f);
				}else{
					shadowMaps[shadowMapIndex].bindAsRenderTarget();
					
					GL11.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
					GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
					
					lightCamera.changeMode(shadowInfo.getBase());
					
					final ShadowCameraTransform shadowCameraTransform = activeLight.calcShadowCameraTransform(mainCamera.getTransform().getTransformedPos(), mainCamera.getTransform().getTransformedRot());
					lightCamera.getTransform().setPos(shadowCameraTransform.pos);
					lightCamera.getTransform().setRot(shadowCameraTransform.rot);
					
					lightMatrix = BIAS_MATRIX.mul(lightCamera.getViewProjection());
					
					lightCamera.updateFrustum();
					
					setFloat("shadowVarianceMin", shadowInfo.getMinVariance());
					setFloat("shadowLightBleedingReduction", shadowInfo.getLightBleedReductionAmount());
					
					if(shadowInfo.shouldFlipFaces()){
						GL11.glCullFace(GL11.GL_FRONT);
					}
					
					GL11.glEnable(GL32.GL_DEPTH_CLAMP);
					
					renderingState = SHADOW_STATE;
					
					if(NeonEngine.areParticlesEnabled()){
						particleCamera = lightCamera;
						particleShader = particleShadowMappingShader;
						particleFlipFaces = shadowInfo.shouldFlipFaces();
					}
					
					object.renderAll(shadowMappingShader, lightCamera);
					
					if(NeonEngine.areParticlesEnabled()){
						batchRenderer.render(particleShader, lightCamera);
					}
					
					GL11.glDisable(GL32.GL_DEPTH_CLAMP);
					
					if(shadowInfo.shouldFlipFaces()){
						GL11.glCullFace(GL11.GL_BACK);
					}
					
					final float shadowSoftness = shadowInfo.getShadowSoftness();
					if(shadowSoftness != 0){
						blurShadowMap(shadowMapIndex, shadowSoftness);
					}
				}
				
				getTexture("displayTexture").bindAsRenderTarget();
			}else{
				if(shadowInfo.getShadowMapSizeAsPowerOf2() != 0){
					lightCamera.changeMode(shadowInfo.getBase());
					
					final ShadowCameraTransform shadowCameraTransform = activeLight.calcShadowCameraTransform(mainCamera.getTransform().getTransformedPos(), mainCamera.getTransform().getTransformedRot());
					lightCamera.getTransform().setPos(shadowCameraTransform.pos);
					lightCamera.getTransform().setRot(shadowCameraTransform.rot);
										
					lightCamera.updateFrustum();
				}
				
				setTexture("shadowMap", shadowMaps[0]);
				lightMatrix = NO_SHADOW_MATRIX;
				setFloat("shadowVarianceMin", 0.00002f);
				setFloat("shadowLightBleedingReduction", 0.0f);
			}
			
			if(activeLight.getType() != BaseLight.POINT_LIGHT){
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
				GL11.glDepthMask(false);
				GL11.glDepthFunc(GL11.GL_ALWAYS);
				
				renderingState = LIGHTING_STATE;
				
	//			if(NeonEngine.areParticlesEnabled()){
	//				particleCamera = mainCamera;
	//				particleShader = forwardParticleShader;
	//				particleFlipFaces = false;
	//			}
				
	//			object.renderAll(activeLight.getShader(), mainCamera);
				applyFilter(activeLight.getShader(), getTexture("renderTexture"), getTexture("displayTexture"));
				
	//			if(NeonEngine.areParticlesEnabled()){
	//				batchRenderer.render(particleShader, mainCamera);
	//			}
				
				GL11.glDepthMask(true);
				GL11.glDepthFunc(GL11.GL_LESS);
				GL11.glDisable(GL11.GL_BLEND);
			}else{
				float size = activeLight.getRange();
				
				Transform t = new Transform();
				t.setPos(activeLight.getTransform().getPos());
				t.setScale(size);
				
				if(Math.pow(t.getTransformedPos().getX() - mainCamera.getTransform().getTransformedPos().getX(), 2) + Math.pow(t.getTransformedPos().getY() - mainCamera.getTransform().getTransformedPos().getY(), 2) + Math.pow(t.getTransformedPos().getZ() - mainCamera.getTransform().getTransformedPos().getZ(), 2) <= Math.pow(size, 2)){
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthMask(false);
					GL11.glDepthFunc(GL11.GL_GEQUAL);
//					GL11.glDisable(GL11.GL_CULL_FACE);
					GL11.glCullFace(GL11.GL_FRONT);
					
					renderMesh(activeLight.getShader(), t, sphereM, materialM, mainCamera);
					
					GL11.glDepthMask(true);
					GL11.glDepthFunc(GL11.GL_LESS);
					GL11.glDisable(GL11.GL_BLEND);
//					GL11.glEnable(GL11.GL_CULL_FACE);
					GL11.glCullFace(GL11.GL_BACK);
				}else{
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
					GL11.glDepthMask(false);
//					GL11.glDepthFunc(GL11.GL_GEQUAL);
					GL11.glDepthFunc(GL11.GL_LEQUAL);
//					GL11.glDisable(GL11.GL_CULL_FACE);
//					GL11.glCullFace(GL11.GL_FRONT);
					
					renderMesh(activeLight.getShader(), t, sphereM, materialM, mainCamera);
					
					GL11.glDepthMask(true);
					GL11.glDepthFunc(GL11.GL_LESS);
					GL11.glDisable(GL11.GL_BLEND);
//					GL11.glEnable(GL11.GL_CULL_FACE);
//					GL11.glCullFace(GL11.GL_BACK);
				}
			}
		}
		
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		}
		
		renderSkybox();
		
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		
		float size = 4;
		
		Transform t = new Transform();
		t.setPos(0, 0, 0);
		t.setScale(size);
		
		boolean truesa = Math.pow(t.getTransformedPos().getX() - mainCamera.getTransform().getTransformedPos().getX(), 2) + Math.pow(t.getTransformedPos().getY() - mainCamera.getTransform().getTransformedPos().getY(), 2) + Math.pow(t.getTransformedPos().getZ() - mainCamera.getTransform().getTransformedPos().getZ(), 2) <= Math.pow(size, 2);
		
		if(truesa){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthMask(false);
			GL11.glDepthFunc(GL11.GL_GEQUAL);
//			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_FRONT);
			
			renderMesh(testShader, t, sphereM, materialM, mainCamera);
			
			GL11.glDepthMask(true);
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
//			GL11.glEnable(GL11.GL_CULL_FACE);
			GL11.glCullFace(GL11.GL_BACK);
		}else{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthMask(false);
//			GL11.glDepthFunc(GL11.GL_GEQUAL);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
//			GL11.glDisable(GL11.GL_CULL_FACE);
//			GL11.glCullFace(GL11.GL_FRONT);
			
			renderMesh(testShader, t, sphereM, materialM, mainCamera);
			
			GL11.glDepthMask(true);
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
//			GL11.glEnable(GL11.GL_CULL_FACE);
//			GL11.glCullFace(GL11.GL_BACK);
		}
		
		if(NeonEngine.isBloomEnabled()){
			applyFilter(bloomSwitchShader, getTexture("displayTexture"), getTexture("bloomTexture1"));
			
			blurBloomMap(0.004f);
			blurBloomMap(0.001f);
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
//			GL11.glDepthMask(false);
//			GL11.glDepthFunc(GL11.GL_EQUAL);
			
			applyFilter(bloomCombineShader, getTexture("bloomTexture1"), getTexture("displayTexture"));
			
//			GL11.glDepthMask(true);
//			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
		if(NeonEngine.isProfilingEnabled()){
			renderProfileTimer.stopInvocation();
			
			windowSyncProfileTimer.startInvocation();
		}
		
//		applyFilter(ambientShader, getTexture("renderTexture"), getTexture("displayTexture"));
		
		applyFilter(hdrFilter, getTexture("displayTexture"), getTexture("postFilterTexture"));
//		applyFilter(nullFilter, getTexture("displayTexture"), getTexture("postFilterTexture"));
		
		renderFilters();
//		applyFilter(nullFilter, getTexture("brdfLUT"), null);
		
		if(NeonEngine.isProfilingEnabled()){
			windowSyncProfileTimer.stopInvocation();
		}
	}
	
	private static void renderSkybox(){
		if(skybox != null){
			GL11.glDepthMask(false);
			
			skybox.render(skyboxShader, mainCamera);
			
			GL11.glDepthMask(true);
		}
	}
	
	private static void renderFilters(){
		if(filters.isEmpty()){
			if(NeonEngine.isFXAAEnabled()){
				setVector3f("inverseFilterTextureSize", new Vector3f(1.0f/(float)getTexture("displayTexture").getWidth(), 1.0f/((float)getTexture("displayTexture").getHeight() + (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight() * getFloat("fxaaAspectDistortion")), 0.0f));
				
				applyFilter(fxaaFilter, getTexture("postFilterTexture"), null);
			}else{
				applyFilter(nullFilter, getTexture("postFilterTexture"), null);
			}
		}else{
			if(NeonEngine.isFXAAEnabled()){
				setVector3f("inverseFilterTextureSize", new Vector3f(1.0f/(float)getTexture("displayTexture").getWidth(), 1.0f/((float)getTexture("displayTexture").getHeight() + (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight() * getFloat("fxaaAspectDistortion")), 0.0f));
				
				applyFilter(fxaaFilter, getTexture("postFilterTexture"), getTexture("displayTexture"));
				
				boolean evenNumber = true;
				for(int i = 0; i < filters.size(); i++){
					if(evenNumber){
						if(filters.size()-1 == i){
							applyFilter(filters.get(i), getTexture("displayTexture"), null);
						}else{
							applyFilter(filters.get(i), getTexture("displayTexture"), getTexture("postFilterTexture"));
						}
					}else{
						if(filters.size()-1 == i){
							applyFilter(filters.get(i), getTexture("postFilterTexture"), null);
						}else{
							applyFilter(filters.get(i), getTexture("postFilterTexture"), getTexture("displayTexture"));
						}
					}
					
					evenNumber = !evenNumber;
				}
			}else{
				boolean evenNumber = true;
				for(int i = 0; i < filters.size(); i++){
					if(evenNumber){
						if(filters.size()-1 == i){
							applyFilter(filters.get(i), getTexture("postFilterTexture"), null);
						}else{
							applyFilter(filters.get(i), getTexture("postFilterTexture"), getTexture("displayTexture"));
						}
					}else{
						if(filters.size()-1 == i){
							applyFilter(filters.get(i), getTexture("displayTexture"), null);
						}else{
							applyFilter(filters.get(i), getTexture("displayTexture"), getTexture("postFilterTexture"));
						}
					}
					
					evenNumber = !evenNumber;
				}
			}
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
		setVector3f("blurScale", new Vector3f(blurAmount, blurAmount * ((float)getTexture("bloomTexture1").getWidth()/(float)getTexture("bloomTexture1").getHeight()), 0.0f));
		applyFilter(gausBlurFilter, getTexture("bloomTexture1"), getTexture("bloomTexture2"));
		
		setVector3f("blurScale", new Vector3f(-blurAmount, blurAmount * ((float)getTexture("bloomTexture1").getWidth()/(float)getTexture("bloomTexture1").getHeight()), 0.0f));
		applyFilter(gausBlurFilter, getTexture("bloomTexture2"), getTexture("bloomTexture1"));
	}
	
	public static void render2D(Entity2D object){
		if(NeonEngine.isProfilingEnabled()){
			renderProfileTimer2D.startInvocation();
		}
		if(NeonEngine.is2DEnabled()){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			object.renderAll();
			
			batchRenderer.render(shader2D, camera2D);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		if(NeonEngine.isProfilingEnabled()){
			renderProfileTimer2D.stopInvocation();
		}
	}
	
	public static boolean particleInFrustum(Transform transform, Camera camera){
		final boolean inCameraFrustum = camera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max());
		
		if(renderingState == LIGHTING_STATE){
			if(activeLight.getType() == BaseLight.SPOT_LIGHT){
				return inCameraFrustum && lightCamera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max());
			}else{
				return inCameraFrustum;
			}
		}else{
			return inCameraFrustum;
		}
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
	
	public static boolean meshInFrustum(Transform transform, Mesh mesh, Camera camera){
		final boolean inCameraFrustum = mesh.inFrustum(transform, camera);
		
		if(renderingState == LIGHTING_STATE){
			if(activeLight.getType() == BaseLight.SPOT_LIGHT){
				return inCameraFrustum && mesh.inFrustum(transform, lightCamera);
			}else{
				return inCameraFrustum;
			}
		}else{
			return inCameraFrustum;
		}
	}
	
	public static void renderParticle(Transform trans, ParticleMaterial material){
		renderParticle(trans, material, new Vector2f(0, 0), new Vector2f(1, 1));
	}
	
	public static void renderParticle(Transform trans, ParticleMaterial material, Vector2f minUV, Vector2f maxUV){
		if(NeonEngine.areParticlesEnabled()){
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
		drawString(xPos, yPos, text, color, scaleX, scaleY, Font.ALIGN_LEFT);
	}
	
	public static void drawString(float xPos, float yPos, String text, Vector3f color, float scaleX, float scaleY, int format){
		if(font != null){
			font.drawString(xPos, yPos, text, color, scaleX, scaleY, format);
		}
	}
	
	public static void addLight(BaseLight baseLight){
		lights.add(baseLight);
	}
	
	public static void removeLight(BaseLight baseLight){
		lights.remove(baseLight);
	}
	
	public static void addFilter(Shader filter){
		filters.add(filter);
	}
	
	public static void removeFilter(Shader filter){
		filters.remove(filter);
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
	
//	public static CubeMap getIrradiance(){
//		return irradiance;
//	}
	
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
//		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		mainCamera.update();
		camera2D.update();
		
		getTexture("renderTexture").cleanUp();
		
		getTexture("displayTexture").cleanUp();
		getTexture("postFilterTexture").cleanUp();
		
		getTexture("bloomTexture1").cleanUp();
		getTexture("bloomTexture2").cleanUp();
		
		setRenderTextures();
	}
	
	private static void setRenderTextures(){
		float quality = NeonEngine.getRenderQuality();
		
		if(quality <= 0){
			quality = 1;
		}
		
		int width = (int)((float)Window.getWidth()/quality);
		int height = (int)((float)Window.getHeight()/quality);
		
		if(width <= 0){
			width = 1;
		}
		if(height <= 0){
			height = 1;
		}
		
		int width2 = (int)((float)Window.getWidth()/quality/2);
		int height2 = (int)((float)Window.getHeight()/quality/2);
		
		if(width2 <= 0){
			width2 = 1;
		}
		if(height2 <= 0){
			height2 = 1;
		}
		
		final ByteBuffer[] data = new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null};
		final int[] filter = new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_LINEAR};
		final int[] internalFormat = new int[]{GL11.GL_RGBA, GL30.GL_RGB32F, GL30.GL_RGB32F, GL30.GL_RG32F, GL14.GL_DEPTH_COMPONENT32};
		final int[] format = new int[]{GL11.GL_RGBA, GL11.GL_RGB, GL11.GL_RGB, GL30.GL_RG, GL11.GL_DEPTH_COMPONENT};
		final int[] type = new int[]{GL11.GL_UNSIGNED_BYTE, GL11.GL_FLOAT, GL11.GL_FLOAT, GL11.GL_UNSIGNED_BYTE, GL11.GL_FLOAT};
		final int[] attachment = new int[]{GL30.GL_COLOR_ATTACHMENT0, GL30.GL_COLOR_ATTACHMENT1, GL30.GL_COLOR_ATTACHMENT2, GL30.GL_COLOR_ATTACHMENT3, GL30.GL_DEPTH_ATTACHMENT};
		
		setTexture("renderTexture", new Texture(width, height, data, GL11.GL_TEXTURE_2D, filter, internalFormat, format, type, true, attachment));
		
//		setTexture("displayTexture", new Texture(width, height, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_UNSIGNED_BYTE, GL11.GL_UNSIGNED_BYTE}, true, new int[]{GL30.GL_COLOR_ATTACHMENT0, GL30.GL_COLOR_ATTACHMENT1}));
		setTexture("displayTexture", new Texture(width, height, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL30.GL_RGBA16F, GL11.GL_RGBA, GL14.GL_DEPTH_COMPONENT32}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_DEPTH_COMPONENT}, new int[]{GL11.GL_FLOAT, GL11.GL_UNSIGNED_BYTE, GL11.GL_FLOAT}, true, new int[]{GL30.GL_COLOR_ATTACHMENT0, GL30.GL_COLOR_ATTACHMENT1, GL30.GL_DEPTH_ATTACHMENT}));
		setTexture("postFilterTexture", new Texture(width, height, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, GL30.GL_COLOR_ATTACHMENT0));
		
		setTexture("bloomTexture1", new Texture(width2, height2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, GL30.GL_COLOR_ATTACHMENT0));
		setTexture("bloomTexture2", new Texture(width2, height2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, true, GL30.GL_COLOR_ATTACHMENT0));
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
	
	public static void setWireframeMode(boolean wire){
		wireframeMode = wire;
	}
	
	public static boolean isWireframeMode(){
		return wireframeMode;
	}
	
	public static void dispose(){
		if(batchRenderer != null){
			batchRenderer.dispose();
		}
	}
}
