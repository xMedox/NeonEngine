package net.medox.neonengine.audio.audioLoading;

import java.nio.ByteBuffer;

public class IndexedSound{
	private ByteBuffer data;
	private int format;
	private int samplerate;
	
	public void dispose(){
		data.clear();
	}
	
	public ByteBuffer getData(){
		return data;
	}
	
	public int getFormat(){
		return format;
	}
	
	public int getSamplerate(){
		return samplerate;
	}
	
	public void setData(ByteBuffer data){
		this.data = data;
	}
	
	public void setFormat(int format){
		this.format = format;
	}
	
	public void setSamplerate(int samplerate){
		this.samplerate = samplerate;
	}
}
