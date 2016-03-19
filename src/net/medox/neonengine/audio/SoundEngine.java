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
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.system.MemoryUtil;

public class SoundEngine{
	private static long context;
	private static long device;
	
	private static FloatBuffer listenerPos;
	private static FloatBuffer listenerVel;
	private static FloatBuffer listenerOri;
	
	public static void init(){
		device = ALC10.alcOpenDevice((ByteBuffer)null);
		if(device == MemoryUtil.NULL){
			throw new IllegalStateException("Failed to open the default device.");
		}
		
		ALCCapabilities deviceCaps = ALC.createCapabilities(device);
		
		context = ALC10.alcCreateContext(device, (ByteBuffer)null);
		if(context == MemoryUtil.NULL){
			throw new IllegalStateException("Failed to create an OpenAL context.");
		}
		
		ALC10.alcMakeContextCurrent(context);
		AL.createCapabilities(deviceCaps);
		
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
		
		listenerPos = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerPos.flip();
		
		listenerVel = BufferUtils.createFloatBuffer(3).put(new float[]{0.0f, 0.0f, 0.0f});
		listenerVel.flip();
		
		listenerOri = BufferUtils.createFloatBuffer(6).put(new float[]{0.0f, 0.0f, 0.0f,  0.0f, 1.0f, 0.0f});
		listenerOri.flip();
	}
	
	public static void setPosition(Vector3f value){
		listenerPos.put(0, value.getX());
		listenerPos.put(1, value.getY());
		listenerPos.put(2, value.getZ());
		AL10.alListenerfv(AL10.AL_POSITION, listenerPos);
	}
	
	public static void setVelocity(Vector3f value){
		listenerVel.put(0, value.getX());
		listenerVel.put(1, value.getY());
		listenerVel.put(2, value.getZ());
		AL10.alListenerfv(AL10.AL_VELOCITY, listenerVel);
	}
	
	public static void setOrientation(Quaternion value){
		listenerOri.put(0, -value.getForward().getX());
		listenerOri.put(1, -value.getForward().getY());
		listenerOri.put(2, -value.getForward().getZ());
		listenerOri.put(3, value.getUp().getX());
		listenerOri.put(4, value.getUp().getY());
		listenerOri.put(5, value.getUp().getZ());
		AL10.alListenerfv(AL10.AL_ORIENTATION, listenerOri);
	}
	
	public static void setGain(float value){
		AL10.alListenerf(AL10.AL_GAIN, value);
	}
	
	public static void dispose(){
		ALC10.alcDestroyContext(context);
		ALC10.alcCloseDevice(device);
	}
}
