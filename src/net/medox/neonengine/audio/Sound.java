package net.medox.neonengine.audio;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.medox.neonengine.audio.audioLoading.OGGSound;
import net.medox.neonengine.audio.audioLoading.WAVSound;
import net.medox.neonengine.audio.resourceManagement.SoundData;
import net.medox.neonengine.audio.resourceManagement.SourceData;
import net.medox.neonengine.math.Vector3f;

public class Sound{
	private static final Map<String, SoundData> loadedSounds = new ConcurrentHashMap<String, SoundData>();
	private static final Map<String, SourceData> loadedSources = new ConcurrentHashMap<String, SourceData>();
	
	private final SourceData source;
	private final String fileName;
	
	private SoundData resource;
	
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
		
		source.setBuffer(resource);
	}
	
	@Override
	protected void finalize() throws Throwable{
		source.dispose();
		if(resource.removeReference() && !fileName.equals("")){
			resource.dispose();
			loadedSounds.remove(fileName);
		}
		
		super.finalize();
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
	
	private Sound loadSound(String fileName){
		final String[] splitArray = fileName.split("\\.");
		final String ext = splitArray[splitArray.length - 1];

		switch (ext) {
			case "wav":
				resource = new SoundData(new WAVSound("./res/sounds/" + fileName).toIndexedSound());
				break;
			case "ogg":
				resource = new SoundData(new OGGSound("./res/sounds/" + fileName).toIndexedSound());
				break;
			default:
				System.err.println("Error: '" + ext + "' file format not supported for audio data.");
				new Exception().printStackTrace();
				System.exit(1);
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
		
		for(final SourceData data : loadedSources.values()){
			data.dispose();
		}
	}
}
