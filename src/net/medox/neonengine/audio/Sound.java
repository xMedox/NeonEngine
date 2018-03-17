package net.medox.neonengine.audio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.medox.neonengine.audio.audioLoading.OGGSound;
import net.medox.neonengine.audio.audioLoading.WAVSound;
import net.medox.neonengine.audio.resourceManagement.SoundData;
import net.medox.neonengine.audio.resourceManagement.SourceData;
import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.math.Vector3f;

public class Sound{
	private static final Map<String, SoundData> loadedSounds = new HashMap<String, SoundData>();
	private static final List<SourceData> loadedSources = new ArrayList<SourceData>();
	
	private final SourceData source;
	private final String fileName;
	
	private SoundData resource;
	private boolean cleanedUp;
	
	public Sound(String fileName){
		this.fileName = fileName;
		
		source = new SourceData();
		
		resource = loadedSounds.get(fileName);
		
		if(resource == null){
			loadSound(fileName);
			loadedSounds.put(fileName, resource);
		}else{
			resource.addReference();
		}
		loadedSources.add(source);
		
		source.setBuffer(resource);
	}
	
	public void cleanUp(){
		if(!cleanedUp){
			source.dispose();
			loadedSources.remove(source);
			if(resource.removeReference() && !fileName.equals("")){
				resource.dispose();
				loadedSounds.remove(fileName);
			}
			
			cleanedUp = true;
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		cleanUp();
		
		super.finalize();
	}
	
	public void play(){
		source.play();
	}
	
	public void pause(){
		source.pause();
	}
	
	public void rewind(){
		source.rewind();
	}
	
	public void stop(){
		source.stop();
	}
	
	public void setPitch(float value){
		source.setPitch(value);
	}
	
	public void setGain(float value){
		source.setGain(value);
	}
	
	public void setPosition(Vector3f value){
		source.setPosition(value);
	}
	
	public void setVelocity(Vector3f value){
		source.setVelocity(value);
	}
	
	public void setLooping(boolean value){
		source.setLooping(value);
	}
	
//	public void setMinGain(float value){
//		source.setMinGain(value);
//	}
//	
//	public void setMaxGain(float value){
//		source.setMaxGain(value);
//	}
	
	public void setRolloffFactor(float value){
		source.setRolloffFactor(value);
	}
	
	public boolean isPlaying(){
		return source.isPlaying();
	}
	
	public boolean isPaused(){
		return source.isPaused();
	}
	
	public float getTimeOffset(){
		return source.getTimeOffset();
	}
	
	public void setTimeOffset(float time){
		source.setTimeOffset(time);
	}
	
	private Sound loadSound(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(ext.equals("wav")){
			resource = new SoundData(new WAVSound("./res/sounds/" + fileName).toIndexedSound());
		}else if(ext.equals("ogg")){
			resource = new SoundData(new OGGSound("./res/sounds/" + fileName).toIndexedSound());
		}else{
			NeonEngine.throwError("Error: '" + ext + "' file format not supported for audio data.");
		}
		
		return this;
	}
	
	public float getLength(){
		return resource.getLength();
	}
	
	public static void dispose(){
		for(final SoundData data : loadedSounds.values()){
			data.dispose();
		}
		
		for(final SourceData data : loadedSources){
			data.dispose();
		}
	}
}
