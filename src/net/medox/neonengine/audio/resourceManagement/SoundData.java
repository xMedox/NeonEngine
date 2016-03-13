package net.medox.neonengine.audio.resourceManagement;

import org.lwjgl.openal.AL10;

import net.medox.neonengine.audio.audioLoading.IndexedSound;
import net.medox.neonengine.core.ReferenceCounter;

public class SoundData extends ReferenceCounter{
	private final int buffer;
	
	public SoundData(IndexedSound audio){
		super();
		
		buffer = AL10.alGenBuffers();
		
		try{
			AL10.alBufferData(buffer, audio.getFormat(), audio.getData(), audio.getSamplerate());
		}finally{
			audio.dispose();
		}
	}
	
	public int getID(){
		return buffer;
	}
	
	public void dispose(){
		AL10.alDeleteBuffers(buffer);
	}
}
