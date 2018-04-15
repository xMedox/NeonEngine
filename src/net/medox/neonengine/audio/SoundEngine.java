package net.medox.neonengine.audio;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.medox.neonengine.core.DataUtil;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.EXTThreadLocalContext;
import org.lwjgl.openal.SOFTHRTF;
import org.lwjgl.system.MemoryUtil;

public class SoundEngine{
	private static long context;
	private static long device;
	
	private static FloatBuffer listenerPosition;
	private static FloatBuffer listenerRotation;
	private static FloatBuffer listenerVelocity;
	
	public static void init(){
		//setting the realtek format to 48000bits and adding the hrtfs in a folder in appdata makes it work
		
		device = ALC10.alcOpenDevice((ByteBuffer)null);
		if(device == MemoryUtil.NULL){
			NeonEngine.throwError("Error: Failed to open the default OpenAL device.");
		}
		
		final ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		
		context = ALC10.alcCreateContext(device, (IntBuffer)null);
		if(context == MemoryUtil.NULL){
			NeonEngine.throwError("Error: Failed to create an OpenAL context.");
		}
		
//		ALC10.alcMakeContextCurrent(context);
		EXTThreadLocalContext.alcSetThreadContext(context);
		AL.createCapabilities(deviceCaps);
		
		if(NeonEngine.isHRTFEnabled() && deviceCaps.ALC_SOFT_HRTF){
			System.out.println("HRTF");
			
			int num_hrtf = ALC10.alcGetInteger(device, SOFTHRTF.ALC_NUM_HRTF_SPECIFIERS_SOFT);
			
			if(num_hrtf == 0){
				System.out.println("No HRTFs found");
			}else{
				int index = -1;
				
				System.out.println("Available HRTFs:");
				for(int i = 0; i < num_hrtf; i++){
					String name = SOFTHRTF.alcGetStringiSOFT(device, SOFTHRTF.ALC_HRTF_SPECIFIER_SOFT, i);
					System.out.format("    %d: %s\n", i, name);
					
//					if(hrtfname != null && name.equals(hrtfname)){
						index = i;
//					}
				}
				
//				index = 1;
				
				IntBuffer attr = DataUtil.createIntBuffer(10).put(SOFTHRTF.ALC_HRTF_SOFT).put(ALC10.ALC_TRUE);
				
				if(index == -1){
//					if(hrtfname != null){
//						System.out.format("HRTF \"%s\" not found\n", hrtfname);
//					}
					
					System.out.format("Using default HRTF...\n");
				}else{
					System.out.format("Selecting HRTF %d...\n", index);
					attr.put(SOFTHRTF.ALC_HRTF_ID_SOFT).put(index);
				}
				
				attr.put(0);
				attr.flip();
				
				if(!SOFTHRTF.alcResetDeviceSOFT(device, attr)){
					System.out.format("Failed to reset device: %s\n", ALC10.alcGetString(device, ALC10.alcGetError(device)));
				}
				
				int hrtf_state = ALC10.alcGetInteger(device, SOFTHRTF.ALC_HRTF_SOFT);
				if(hrtf_state == 0){
					System.out.format("HRTF not enabled!\n");
				}else{
					String name = ALC10.alcGetString(device, SOFTHRTF.ALC_HRTF_SPECIFIER_SOFT);
					System.out.format("HRTF enabled, using %s\n", name);
				}
			}
		}
		
//		ALCCapabilities capabilities = device.getCapabilities();
//		
//		if(!capabilities.OpenALC10){
//		   NeonEngine.throwError("Error: OpenAL Context Creation failed");
//		}
//		
//		System.out.println("OpenALC10: " + capabilities.OpenALC10);
//		System.out.println("OpenALC11: " + capabilities.OpenALC11);
//		System.out.println("caps.ALC_EXT_EFX = " + capabilities.ALC_EXT_EFX);
//		
//		String defaultDeviceSpecifier = ALC10.alcGetString(0L, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);
//		assert(defaultDeviceSpecifier != null);
//		System.out.println("Default device: " + defaultDeviceSpecifier);
//		
//		IntBuffer attribs = DataUtil.createIntBuffer(16);
//
//		attribs.put(ALC10.ALC_FREQUENCY);
//		attribs.put(44100);
//
//		attribs.put(ALC10.ALC_REFRESH);
//		attribs.put(60);
//
//		attribs.put(ALC10.ALC_SYNC);
//		attribs.put(ALC10.ALC_FALSE);
//
//		attribs.put(0);
//		attribs.flip();
//
//		long contextHandle = ALC10.alcCreateContext(device.getPointer(), attribs);
//		assert(contextHandle != 0L);
//		
//		context = new ALContext(device, contextHandle);
//		
//		context.makeCurrent();
		
		listenerPosition = DataUtil.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerPosition.flip();
		
		listenerVelocity = DataUtil.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerVelocity.flip();
		
		listenerRotation = DataUtil.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f});
		listenerRotation.flip();
	}
	
	public static void setPosition(Vector3f value){
		listenerPosition.put(0, value.getX());
		listenerPosition.put(1, value.getY());
		listenerPosition.put(2, value.getZ());
		
		AL10.alListenerfv(AL10.AL_POSITION, listenerPosition);
	}
	
	public static void setVelocity(Vector3f value){
		listenerVelocity.put(0, value.getX());
		listenerVelocity.put(1, value.getY());
		listenerVelocity.put(2, value.getZ());
		
		AL10.alListenerfv(AL10.AL_VELOCITY, listenerVelocity);
	}
	
	public static void setRotation(Quaternion value){
		listenerRotation.put(0, -value.getForward().getX());
		listenerRotation.put(1, -value.getForward().getY());
		listenerRotation.put(2, -value.getForward().getZ());
		listenerRotation.put(3, value.getUp().getX());
		listenerRotation.put(4, value.getUp().getY());
		listenerRotation.put(5, value.getUp().getZ());
		
		AL10.alListenerfv(AL10.AL_ORIENTATION, listenerRotation);
	}
	
	public static void setGain(float value){
		AL10.alListenerf(AL10.AL_GAIN, value);
	}
	
	public static void dispose(){
		if(context != 0){
			ALC10.alcDestroyContext(context);
		}
		
		if(device != 0){
			ALC10.alcCloseDevice(device);
		}
	}
}
