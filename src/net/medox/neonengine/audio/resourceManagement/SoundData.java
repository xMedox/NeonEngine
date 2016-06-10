package net.medox.neonengine.audio.resourceManagement;

import org.lwjgl.openal.AL10;

import net.medox.neonengine.audio.audioLoading.IndexedSound;
import net.medox.neonengine.core.ReferenceCounter;

public class SoundData extends ReferenceCounter{
	private final int buffer;
	
	private final float length;
	
	public SoundData(IndexedSound audio){
		super();
		
		buffer = AL10.alGenBuffers();
		
		try{
			AL10.alBufferData(buffer, audio.getFormat(), audio.getData(), audio.getSamplerate());
		}finally{
			audio.dispose();
		}
		
		length = (float)(AL10.alGetBufferi(buffer, AL10.AL_SIZE) * 8 / (AL10.alGetBufferi(buffer, AL10.AL_CHANNELS) * AL10.alGetBufferi(buffer, AL10.AL_BITS))) / (float)(AL10.alGetBufferi(buffer, AL10.AL_FREQUENCY));
	}
	
	public int getID(){
		return buffer;
	}
	
	public float getLength(){
		return length;
	}
	
	public void dispose(){
		AL10.alDeleteBuffers(buffer);
	}
}
