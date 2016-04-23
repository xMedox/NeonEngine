package net.medox.neonengine.audio;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;
import org.lwjgl.system.MemoryUtil;

public class SoundEngine{
	private static long context;
	private static long device;
	
	private static FloatBuffer listenerPosition;
	private static FloatBuffer listenerVelocity;
	private static FloatBuffer listenerOrientation;
	
	public static void init(){
		device = ALC10.alcOpenDevice((ByteBuffer)null);
		if(device == MemoryUtil.NULL){
			throw new IllegalStateException("Failed to open the default OpenAL device.");
		}
				
		context = ALC10.alcCreateContext(device, null);
		if(context == MemoryUtil.NULL){
			throw new IllegalStateException("Failed to create an OpenAL context.");
		}
		
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(ALC.createCapabilities(device));
		
//		ALCCapabilities capabilities = device.getCapabilities();
//		
//		if(!capabilities.OpenALC10){
//		    throw new RuntimeException("OpenAL Context Creation failed");
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
//		IntBuffer attribs = BufferUtils.createIntBuffer(16);
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
		
		listenerPosition = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerPosition.flip();
		
		listenerVelocity = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerVelocity.flip();
		
		listenerOrientation = BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f});
		listenerOrientation.flip();
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
	
	public static void setOrientation(Quaternion value){
		listenerOrientation.put(0, -value.getForward().getX());
		listenerOrientation.put(1, -value.getForward().getY());
		listenerOrientation.put(2, -value.getForward().getZ());
		listenerOrientation.put(3, value.getUp().getX());
		listenerOrientation.put(4, value.getUp().getY());
		listenerOrientation.put(5, value.getUp().getZ());
		
		AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOrientation);
	}
	
	public static void setGain(float value){
		AL10.alListenerf(AL10.AL_GAIN, value);
	}
	
	public static void dispose(){
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
