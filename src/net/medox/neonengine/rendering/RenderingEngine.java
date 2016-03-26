package net.medox.neonengine.rendering;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
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
import org.lwjgl.vulkan.VK10;

import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Entity2D;
import net.medox.neonengine.core.ProfileTimer;
import net.medox.neonengine.core.Transform;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.OpenGL.BatchRendererGL;
import net.medox.neonengine.rendering.meshLoading.IndexedModel;

public class RenderingEngine{
	public static final int OPENGL = 0;
	public static final int VULKAN = 1;
	
	private static final int NUM_SHADOW_MAPS = 10;
	private static final Matrix4f BIAS_MATRIX = new Matrix4f().initScale(0.5f, 0.5f, 0.5f).mul(new Matrix4f().initTranslation(1.0f, 1.0f, 1.0f));
	
	private static final ProfileTimer renderProfileTimer = new ProfileTimer();
	private static final ProfileTimer renderProfileTimer2D = new ProfileTimer();
	private static final ProfileTimer windowSyncProfileTimer = new ProfileTimer();
	
	public static int RENDERING_MODE = 0;
	
	public static int TEXTURE_2D = GL11.GL_TEXTURE_2D;
	public static int LINEAR = GL11.GL_LINEAR;
	public static int NEAREST = GL11.GL_NEAREST;
	public static int LINEAR_MIPMAP_LINEAR = GL11.GL_LINEAR_MIPMAP_LINEAR;
	public static int RGBA = GL11.GL_RGBA;
	public static int NONE = GL11.GL_NONE;
	
	private static BatchRenderer batchRenderer;
	
	private static Map<String, Texture> textureMap;
//	private static Map<String, CubeMap> cubeMapMap;
	private static Map<String, Vector3f> vector3fMap;
//	private static Map<String, Vector2f> vector2fMap;
	private static Map<String, Float> floatMap;
	
	private static Map<String, Integer> samplerMap;
	private static List<BaseLight> lights;
	private static BaseLight activeLight;
	
	private static Shader forwardAmbientShader;
	private static Shader forwardParticleAmbientShader;
	private static Shader bloomCombineShader;
	private static Shader bloomSwitchShader;
	private static Shader forwardParticleShader;
	private static Shader shadowMapShader;
	private static Shader particleShadowMapShader;
	private static Shader nullFilter;
	private static Shader gausBlurFilter;
	private static Shader fxaaFilter;
	private static Shader shader2D;
	private static Shader skyboxShader;
	
	private static Camera particleCamera;
	private static Shader particleShader;
	private static boolean particleFlipFaces;
	
	private static Camera mainCamera;
	private static Skybox skybox;
	private static TrueTypeFont font; //TODO remove this
	
//	private static Texture tempTarget;
	private static Material planeMaterial;
	private static Mesh plane;
	private static Transform planeTransform;
	
	private static Camera lightCamera;
	private static Camera filterCamera;
	
	private static Matrix4f lightMatrix;
	
	private static Texture[] shadowMaps = new Texture[NUM_SHADOW_MAPS];
	private static Texture[] shadowMapTempTargets = new Texture[NUM_SHADOW_MAPS];
	
//	private static CubeMap[] shadowCubeMaps = new CubeMap[NUM_SHADOW_MAPS];
//	private static CubeMap[] shadowCubeMapTempTargets = new CubeMap[NUM_SHADOW_MAPS];
	
	private static Camera camera2D;
	private static Entity entity2D;
//	public static Mesh plane2D;
	
	private static boolean wireframeMode; //TODO remove this
	
	public static void init(){
		//TODO remove this
		System.out.println("--------------------------------------------------------------");
		System.out.println("Engine version:   " + CoreEngine.getVersion());
		System.out.println("OS name:          " + System.getProperty("os.name"));
		System.out.println("OS version:       " + System.getProperty("os.version"));
		System.out.println("OS arch:          " + System.getProperty("os.arch"));
		System.out.println("Arch data model:  " + System.getProperty("sun.arch.data.model"));
		System.out.println("Java version:     " + System.getProperty("java.version"));
		if(RENDERING_MODE == 0){
			System.out.println("Rendering Mode:   OpenGL(" + GL11.glGetString(GL11.GL_VERSION) + ")");
//			System.out.println("Max Texture size: " + GL11.glGetInteger(GL11.GL_MAX_TEXTURE_SIZE));
		}else if(RENDERING_MODE == 1){
			System.out.println("Rendering Mode:   Vulkan(" + VK10.VK_VERSION_MAJOR + "." + VK10.VK_VERSION_MINOR + "." + VK10.VK_VERSION_PATCH + ")");
//			System.out.println("Max Texture size: " + VK10.VK_WHOLE_SIZE);
		}
		System.out.println("LWJGL version:    " + Version.getVersion());
		System.out.println("--------------------------------------------------------------");
		
		if(RENDERING_MODE == OPENGL){
			batchRenderer = new BatchRendererGL();
		}else if(RENDERING_MODE == VULKAN){
			batchRenderer = new BatchRenderer();
		}
		
		textureMap = new HashMap<String, Texture>();
//		cubeMapHashMap = new HashMap<String, CubeMap>();
		vector3fMap = new HashMap<String, Vector3f>();
//		vector2fHashMap = new  HashMap<String, Vector2f>();
		floatMap = new HashMap<String, Float>();
		
		lights = new ArrayList<BaseLight>();
		samplerMap = new HashMap<String, Integer>();
		samplerMap.put("diffuse", 0);
		samplerMap.put("normalMap", 1);
//		samplerMap.put("dispMap", 2);
//		samplerMap.put("specMap", 3);
//		samplerMap.put("glowMap", 4);
//		samplerMap.put("shadowMap", 5);
		samplerMap.put("specMap", 2);
		samplerMap.put("glowMap", 3);
		samplerMap.put("shadowMap", 4);
		
		samplerMap.put("filterTexture", 0);
		
		samplerMap.put("cubeMap", 0);
		
		setVector3f("ambient", new Vector3f(0.15f, 0.15f, 0.15f));
		
		setFloat("fxaaSpanMax", 8.0f);
		setFloat("fxaaReduceMin", 1.0f/128.0f);
		setFloat("fxaaReduceMul", 1.0f/8.0f);
		setFloat("fxaaAspectDistortion", 150.0f);
		
		setTexture("displayTexture", new Texture(Window.getWidth()*CoreEngine.OPTION_MSAA_MULTIPLIER, Window.getHeight()*CoreEngine.OPTION_MSAA_MULTIPLIER, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, true, new int[]{ARBFramebufferObject.GL_COLOR_ATTACHMENT0, ARBFramebufferObject.GL_COLOR_ATTACHMENT1}));
		
		if(CoreEngine.OPTION_ENABLE_BLOOM == 1){
			setTexture("bloomTexture1", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
			setTexture("bloomTexture2", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
		}
		
		forwardAmbientShader = new Shader("forwardAmbient");
		forwardParticleAmbientShader = new Shader("forwardParticleAmbient");
		bloomCombineShader = new Shader("bloomCombine");
		bloomSwitchShader = new Shader("bloomSwitch");
		forwardParticleShader = new Shader("forwardParticleForward");
		shadowMapShader =  new Shader("shadowMapGenerator");
		particleShadowMapShader = new Shader("paricleShadowMapGenerator");
		nullFilter = new Shader("filterNull");
		gausBlurFilter = new Shader("filterGausBlur7x1");
		fxaaFilter = new Shader("filterFxaa");
		shader2D = new Shader("shader2D");
		skyboxShader = new Shader("skyboxShader");
		
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		GL11.glFrontFace(GL11.GL_CW);
		GL11.glCullFace(GL11.GL_BACK);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		glEnable(GL_DEPTH_CLAMP);
//		glEnable(GL13.GL_MULTISAMPLE);
//		glEnable(GL_FRAMEBUFFER_SRGB);
		
		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		lightCamera = new Camera();
		new Entity().addComponent(lightCamera);
		lightCamera.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(180.0f));
		
		filterCamera = new Camera();
		new Entity().addComponent(filterCamera);
		filterCamera.getTransform().rotate(new Vector3f(0, 1, 0), (float)Math.toRadians(180.0f));
		
		planeTransform = new Transform();
		
		planeMaterial = new Material();
		
		planeTransform.rotate(new Quaternion(new Vector3f(1, 0, 0), (float)Math.toRadians(90.0f)));
		planeTransform.rotate(new Quaternion(new Vector3f(0, 0, 1), (float)Math.toRadians(180.0f)));
		
		final IndexedModel meshIndexed = new IndexedModel();
		
		meshIndexed.addVertex(new Vector3f(1f, 0f, 1f));
		meshIndexed.addTexCoord(new Vector2f(1f, 1f - 0f));
		
		meshIndexed.addVertex(new Vector3f(-1f, 0f, 1f));
		meshIndexed.addTexCoord(new Vector2f(0f, 1f - 0f));
		
		meshIndexed.addVertex(new Vector3f(1f, 0f, -1f));
		meshIndexed.addTexCoord(new Vector2f(1f, 1f - 1f));
		
		meshIndexed.addVertex(new Vector3f(-1f, 0f, -1f));
		meshIndexed.addTexCoord(new Vector2f(0f, 1f - 1f));
		
		
		meshIndexed.addFace(1, 0, 2);
		meshIndexed.addFace(3, 1, 2);
		
		plane = new Mesh("", meshIndexed.finalizeModel());
		
		for(int i = 0; i < NUM_SHADOW_MAPS; i++){
			final int shadowMapSize = 1 << (i + 1);
			shadowMaps[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, ARBTextureRG.GL_RG32F, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
			shadowMapTempTargets[i] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, ARBTextureRG.GL_RG32F, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
			
//			shadowCubeMaps[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
//			shadowCubeMapTempTargets[i] = new CubeMap(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
		}
		
		lightMatrix = new Matrix4f().initScale(0, 0, 0);
		
		entity2D = new Entity();
		camera2D = new Camera(0, Window.getWidth(), 0, Window.getHeight(), -1, 1);
		
		entity2D.addComponent(camera2D);
		
		//TODO remove this
//		String fontName = null;
//		
//		if (!TrueTypeFont.isSupported(fontName)) fontName = "serif";
//	    Font fontOld = new Font(fontName, /*Font.PLAIN | */Font.PLAIN, /*40*/18 /*32*/);
		
//	    trueTypeFont = new TrueTypeFont(font, true, c);
				
		
		//TODO remove this
		Font customFont = null;
		
		try{
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("./res/" + "font.ttf")).deriveFont(/*Font.ITALIC, *//*64f*/18f);
			final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			//register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("./res/" + "font.ttf")));
		}catch(IOException | FontFormatException e){
            e.printStackTrace();
        }
		
		font = new TrueTypeFont(customFont, false/*, new char[]{'�', '�', '�', '�'}*/);
//		font = new TrueTypeFont(fontOld, true/*, new char[]{'�', '�', '�', '�'}*/);
	}
	
	public static void changeRenderingMode(int RenderingMode){
		RENDERING_MODE = RenderingMode;
		
		if(RENDERING_MODE == OPENGL){
			TEXTURE_2D = GL11.GL_TEXTURE_2D;
			LINEAR = GL11.GL_LINEAR;
			NEAREST = GL11.GL_NEAREST;
			LINEAR_MIPMAP_LINEAR = GL11.GL_LINEAR_MIPMAP_LINEAR;
			RGBA = GL11.GL_RGBA;
			NONE = GL11.GL_NONE;
		}/*else if(RENDERING_MODE == VULKAN){*/
//			TEXTURE_2D = VK10.VK_IMAGE_TYPE_2D;
//			LINEAR = VK10.VK_FILTER_LINEAR;
//			NEAREST = VK10.VK_FILTER_NEAREST;
//			LINEAR_MIPMAP_LINEAR = VK10.VK_;
//			RGBA = VK10.VK_
//			NONE = VK10.VK_
//		}
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
	
	private static void blurShadowMap(int shadowMapIndex, float blurAmount){
		setVector3f("blurScale", new Vector3f(blurAmount/(shadowMaps[shadowMapIndex].getWidth()), 0.0f, 0.0f));
		applyFilter(gausBlurFilter, shadowMaps[shadowMapIndex], shadowMapTempTargets[shadowMapIndex]);
		
		setVector3f("blurScale", new Vector3f(0.0f, blurAmount/(shadowMaps[shadowMapIndex].getHeight()), 0.0f));
		applyFilter(gausBlurFilter, shadowMapTempTargets[shadowMapIndex], shadowMaps[shadowMapIndex]);
	}
	
	private static void applyFilter(Shader filter, Texture source, Texture dest){
//		assert(source != dest);
		
		if(dest == null){
			Window.bindAsRenderTarget();
		}else{
			dest.bindAsRenderTarget();
		}
		
		setTexture("filterTexture", source);
		
//		camera.setProjection(new Matrix4f().initIdentity());
//		camera.getTransform().setPos(new Vector3f(0, 0, 0));
//		camera.getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(180.0f)));
		
//		Camera temp = mainCamera;
//		mainCamera = camera;
		
//		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		filter.bind();
		filter.updateUniforms(planeTransform, planeMaterial, filterCamera);
		plane.draw();
		
//		mainCamera = temp;
		setTexture("filterTexture", null);
	}
	
	public static void updateUniformStruct(Transform transform, Material material, Shader shader, String uniformName, String uniformType){
		throw new IllegalArgumentException(uniformType + " is not a supported type in Rendering Engine");
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
		
		if(CoreEngine.OPTION_ENABLE_PARTICLES == 1){
			particleCamera = mainCamera;
			particleShader = forwardParticleAmbientShader;
			particleFlipFaces = false;
			
			batchRenderer.draw(particleShader, mainCamera);
		}
		
//		System.out.println(MeshRenderer.count + "|" + MeshRenderer.countMax);
		
		for(int i = 0; i < lights.size(); i++){
			activeLight = lights.get(i);
			final ShadowInfo shadowInfo = activeLight.getShadowInfo();
			
			if(CoreEngine.OPTION_ENABLE_SHADOWS == 1){
				int shadowMapIndex = 0;
				
				if(shadowInfo != null){
					shadowMapIndex = shadowInfo.getShadowMapSizeAsPowerOf2() - 1;
				}
				
//				assert(shadowMapIndex >= 0 && shadowMapIndex < NUM_SHADOW_MAPS);
				
//				int shadowMapSize = 1 << (shadowMapIndex + 1);
//				shadowMaps[shadowMapIndex] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
//				shadowMapTempTargets[shadowMapIndex] = new Texture(shadowMapSize, shadowMapSize, (ByteBuffer)null, GL_TEXTURE_2D, GL_LINEAR, ARBTextureRG.GL_RG32F, GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0);
				
//				shadowMapTempTargets[shadowMapIndex].clear(0, GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
//				shadowMapTempTargets[shadowMapIndex].clear(0, GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
				
				setTexture("shadowMap", shadowMaps[shadowMapIndex]);
				shadowMaps[shadowMapIndex].bindAsRenderTarget();
				
//				shadowMaps[shadowMapIndex].bind(7);
				GL11.glClearColor(1.0f, 1.0f, 0.0f, 0.0f);
				GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
				
//				shadowMaps[shadowMapIndex].clear(0, GL_DEPTH_BUFFER_BIT | GL_COLOR_BUFFER_BIT);
				
				if(shadowInfo == null){
					lightMatrix = new Matrix4f().initScale(0, 0, 0);
					setFloat("shadowVarianceMin", 0.00002f);
				}else{
					setFloat("shadowLightBleedingReduction", 0.0f);
//					lightCamera.setProjection(shadowInfo.getProjection());
					lightCamera.changeMode(shadowInfo.getBase());
					
					final ShadowCameraTransform shadowCameraTransform = activeLight.calcShadowCameraTransform(mainCamera.getTransform().getTransformedPos(), mainCamera.getTransform().getTransformedRot());
					lightCamera.getTransform().setPos(shadowCameraTransform.pos);
					lightCamera.getTransform().setRot(shadowCameraTransform.rot);
					
					lightMatrix = BIAS_MATRIX.mul(lightCamera.getViewProjection());
					
					lightCamera.updateFrustum();
					
					setFloat("shadowVarianceMin", shadowInfo.getMinVariance());
					setFloat("shadowLightBleedingReduction", shadowInfo.getLightBleedReductionAmount());
//					final boolean flipFaces = shadowInfo.getFlipFaces();
					
					if(shadowInfo.getFlipFaces()){
						GL11.glCullFace(GL11.GL_FRONT);
					}
					
//					camera.updateFrustum();
					
					GL11.glEnable(GL32.GL_DEPTH_CLAMP);
					
					object.renderAll(shadowMapShader, lightCamera);
					
//					BatchRenderer.addMesh(shadowMapShader, lightCamera, new Transform(), getTexture("displayTexture")/*shadowMaps[9]*/, new Vector3f(1, 1, 1), new Vector2f(0, 1), new Vector2f(1, 0));
					if(CoreEngine.OPTION_ENABLE_PARTICLES == 1){
						particleCamera = lightCamera;
						particleShader = particleShadowMapShader;
						particleFlipFaces = shadowInfo.getFlipFaces();
						
						batchRenderer.draw(particleShader, lightCamera);
					}
					
					GL11.glDisable(GL32.GL_DEPTH_CLAMP);
					
					if(shadowInfo.getFlipFaces()){
						GL11.glCullFace(GL11.GL_BACK);
					}
					
					final float shadowSoftness = shadowInfo.getShadowSoftness();
					
					if(shadowSoftness != 0){
						blurShadowMap(shadowMapIndex, shadowSoftness);
//						applyFilter(gausBlurFilter, getTexture("shadowMap"), getTexture("shadowMap");
					}
				}
				
				getTexture("displayTexture").bindAsRenderTarget();
			}
			
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDepthMask(false);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			
			object.renderAll(activeLight.getShader(), mainCamera);
			
			if(CoreEngine.OPTION_ENABLE_PARTICLES == 1){
				particleCamera = mainCamera;
				particleShader = forwardParticleShader;
				particleFlipFaces = false;
				
				batchRenderer.draw(particleShader, mainCamera);
			}
			
			GL11.glDepthMask(true);
			GL11.glDepthFunc(GL11.GL_LESS);
			GL11.glDisable(GL11.GL_BLEND);
		}
		
//		float displayTextureAspect = (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight();
//		float displayTextureHeightAdditive = (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight() * getFloat("fxaaAspectDistortion");
//		setVector3f("inverseFilterTextureSize", new Vector3f(1.0f/(float)getTexture("displayTexture").getWidth(), 1.0f/((float)getTexture("displayTexture").getHeight() + displayTextureHeightAdditive), 0.0f));
		setVector3f("inverseFilterTextureSize", new Vector3f(1.0f/(float)getTexture("displayTexture").getWidth(), 1.0f/((float)getTexture("displayTexture").getHeight() + (float)getTexture("displayTexture").getWidth()/(float)getTexture("displayTexture").getHeight() * getFloat("fxaaAspectDistortion")), 0.0f));
		//TODO remove this
		if(wireframeMode){
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		
		renderProfileTimer.stopInvocation();
		
		windowSyncProfileTimer.startInvocation();
		
		if(CoreEngine.OPTION_ENABLE_BLOOM == 1){
			applyFilter(bloomSwitchShader, getTexture("displayTexture"), getTexture("bloomTexture1"));
			
			setVector3f("blurScale", new Vector3f(1f/(getTexture("displayTexture").getWidth()/8f), 1f/(getTexture("displayTexture").getHeight()/8f), 0.0f));
			applyFilter(gausBlurFilter, getTexture("bloomTexture1"), getTexture("bloomTexture2"));
			
			setVector3f("blurScale", new Vector3f(-1f/(getTexture("displayTexture").getWidth()/8f), 1f/(getTexture("displayTexture").getHeight()/8f), 0.0f));
			applyFilter(gausBlurFilter, getTexture("bloomTexture2"), getTexture("bloomTexture1"));
			
			setVector3f("blurScale", new Vector3f(1f/(getTexture("displayTexture").getWidth()/2f), 1f/(getTexture("displayTexture").getHeight()/2f), 0.0f));
			applyFilter(gausBlurFilter, getTexture("bloomTexture1"), getTexture("bloomTexture2"));
			
			setVector3f("blurScale", new Vector3f(-1f/(getTexture("displayTexture").getWidth()/2f), 1f/(getTexture("displayTexture").getHeight()/2f), 0.0f));
			applyFilter(gausBlurFilter, getTexture("bloomTexture2"), getTexture("bloomTexture1"));
			
			applyFilter(bloomCombineShader, getTexture("bloomTexture1"), getTexture("displayTexture"));
		}
		
		if(CoreEngine.OPTION_ENABLE_FXAA == 1){
			applyFilter(fxaaFilter, getTexture("displayTexture"), null);
		}else{
			applyFilter(nullFilter, getTexture("displayTexture"), null);
		}
		
		windowSyncProfileTimer.stopInvocation();
	}
	
	public static void render(Entity2D object){
		renderProfileTimer2D.startInvocation();
		if(CoreEngine.OPTION_ENABLE_2D == 1){
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			
			object.renderAll();
			
			batchRenderer.draw(shader2D, camera2D);
			
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
		}
		renderProfileTimer2D.stopInvocation();
	}
	
	public static boolean particleInFrustum(Transform transform, Camera camera){
		return camera.getFrustum() == null ? true : camera.getFrustum().sphereInFrustum(transform.getTransformedPos(), transform.getScale().max());
	}
	
	public static void addParticle(Transform trans, ParticleMaterial material){
		if(CoreEngine.OPTION_ENABLE_PARTICLES == 1){
			batchRenderer.addMesh(particleShader, particleCamera/*mainCamera*/, particleFlipFaces, trans, material);
		}
	}
	
	public static void addParticle(Transform trans, ParticleMaterial material, Vector2f minUV, Vector2f maxUV){
		if(CoreEngine.OPTION_ENABLE_PARTICLES == 1){
			batchRenderer.addMesh(particleShader, particleCamera/*mainCamera*/, particleFlipFaces, trans, material, minUV, maxUV);
		}
	}
	
	public static void add2DMesh(Transform2D trans, Texture texture){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, texture);
	}
	
	public static void add2DMesh(Transform2D trans, Texture texture, Vector3f color){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, texture, color);
	}
	
	public static void add2DMesh(Transform2D trans, Texture texture, Vector3f color, Vector2f minUV, Vector2f maxUV){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, texture, color, minUV, maxUV);
	}
	
	public static void add2DMesh(Transform2D trans, int id){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, id);
	}
	
	public static void add2DMesh(Transform2D trans, int id, Vector3f color){
		batchRenderer.add2DMesh(shader2D, camera2D, trans, id, color);
	}
	
//	public static void drawString(Transform2D trans, String text, Vector3f color){
//		font.drawString(trans.getTransformedPos().getX(), trans.getTransformedPos().getY(), text, trans.getScale().getX(), trans.getScale().getY(), color);
//	}
	
	public static void drawString(float x, float y, String whatchars, float scaleX, float scaleY, Vector3f color){
		font.drawString(x, y, whatchars, scaleX, scaleY, color);
	}
	
	public static void renderSkybox(){
		if(skybox != null){
			GL11.glDepthMask(false);
			
			skybox.getTransform().setPos(mainCamera.getTransform().getTransformedPos());
			
			skybox.draw(skyboxShader, mainCamera);
			
//			GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glDepthMask(true);
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
	
	public static Shader getForwardAmbient(){
		return forwardAmbientShader;
	}

	public static void setMainCamera(Camera mainCamera){
		RenderingEngine.mainCamera = mainCamera;
//		mainCamera.changeAspect();
	}
	
	public static void setMainSkybox(Skybox skybox){
		RenderingEngine.skybox = skybox;
	}
	
	public static Matrix4f getLightMatrix(){
		return lightMatrix;
	}
	
	public static void updateViewport(){
		GL11.glScissor(0, 0, Window.getWidth(), Window.getHeight());
		GL11.glViewport(0, 0, Window.getWidth(), Window.getHeight());
		
		mainCamera.update();
		
		camera2D.update();
		
//		getTexture("displayTexture").finalize();
		
		setTexture("displayTexture", new Texture(Window.getWidth()*CoreEngine.OPTION_MSAA_MULTIPLIER, Window.getHeight()*CoreEngine.OPTION_MSAA_MULTIPLIER, new ByteBuffer[]{(ByteBuffer)null, (ByteBuffer)null}, GL11.GL_TEXTURE_2D, new int[]{GL11.GL_LINEAR, GL11.GL_LINEAR}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, new int[]{GL11.GL_RGBA, GL11.GL_RGBA}, true, new int[]{ARBFramebufferObject.GL_COLOR_ATTACHMENT0, ARBFramebufferObject.GL_COLOR_ATTACHMENT1}));
		
		if(CoreEngine.OPTION_ENABLE_BLOOM == 1){
//			getTexture("bloomTexture1").finalize();
//			getTexture("bloomTexture2").finalize();
			
			setTexture("bloomTexture1", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
			setTexture("bloomTexture2", new Texture(Window.getWidth()/2, Window.getHeight()/2, (ByteBuffer)null, GL11.GL_TEXTURE_2D, GL11.GL_LINEAR, GL11.GL_RGBA, GL11.GL_RGBA, true, ARBFramebufferObject.GL_COLOR_ATTACHMENT0));
		}
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
		final Texture result = textureMap.get(name);
		
		return result == null ? Material.DEFAULT_DIFFUSE_TEXTURE : result;
	}
	
//	public static CubeMap getCubeMap(String name){
//		final CubeMap result = cubeMapHashMap.get(name);
//		
//		return result == null ? Material.DEFAULT_CUBE_MAP : result;
//	}
	
	public static Vector3f getVector3f(String name){
		final Vector3f result = vector3fMap.get(name);
		
		return result == null ? new Vector3f(0, 0, 0) : result;
	}
	
//	public static Vector2f getVector2f(String name){
//		final Vector2f result = vector2fHashMap.get(name);
//		
//		return result == null ? new Vector2f(0, 0) : result;
//	}
	
	public static float getFloat(String name){
		final Float result = floatMap.get(name);
		
		return result == null ? 0 : result;
	}
	
	public static void setAmbiet(Vector3f ambient){
		setVector3f("ambient", new Vector3f(ambient.getX(), ambient.getY(), ambient.getZ()));
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
	
	public static int getFPS(){
		return CoreEngine.fps;
	}
	
	public static void dispose(){
		batchRenderer.dispose();
	}
}
