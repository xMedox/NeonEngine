package net.medox.neonengine.audio;

import java.util.HashMap;
import java.util.Map;

import net.medox.neonengine.audio.audioLoading.OGGSound;
import net.medox.neonengine.audio.audioLoading.WAVSound;
import net.medox.neonengine.audio.resourceManagement.SoundData;
import net.medox.neonengine.audio.resourceManagement.SourceData;
import net.medox.neonengine.math.Vector3f;

public class Sound{
	private static Map<String, SoundData> loadedAudios = new HashMap<String, SoundData>();
	private static Map<String, SourceData> loadedSources = new HashMap<String, SourceData>();
	private SoundData resource;
	private final SourceData source;
	private final String fileName;
	
	public Sound(String fileName){
		this.fileName = fileName;
		
		source = new SourceData();
		
		resource = loadedAudios.get(fileName);
		
		if(resource == null){
			loadAudio(fileName);
			loadedAudios.put(fileName, resource);
		}else{
			resource.addReference();
		}
		
		source.setBuffer(resource);
	}
	
	@Override
	protected void finalize(){
		source.dispose();
		if(resource.removeReference() && !fileName.isEmpty()){
			resource.dispose();
			loadedAudios.remove(fileName);
		}
	}
	
	public void play(){
		source.play();
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
	
	private Sound loadAudio(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];
		
		if(!ext.equals("wav") && !ext.equals("ogg")){
			System.err.println("Error: '" + ext + "' file format not supported for audio data.");
			new Exception().printStackTrace();
			System.exit(1);
		}
		
		if(ext.equals("wav")){
			resource = new SoundData(new WAVSound("./res/sounds/" + fileName).toIndexedAudio());
		}else if(ext.equals("ogg")){
			resource = new SoundData(new OGGSound("./res/sounds/" + fileName).toIndexedAudio());
		}
		
		return this;
	}
	
	public static void dispose(){
		for(final SoundData data : loadedAudios.values()){
			data.dispose();
		}
		
		for(final SourceData data : loadedSources.values()){
			data.dispose();
		}
	}
}
