package net.medox.neonengine.audio.audioLoading;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.openal.AL10;

import net.medox.neonengine.core.NeonEngine;

public class WAVSound{
	private ByteBuffer data;
	private int format;
	private int samplerate;
	
	public WAVSound(String fileName){
		FileInputStream fin = null;
		try{
			fin = new FileInputStream(fileName);
		}catch(FileNotFoundException e){
			NeonEngine.throwError("Error: unable to read " + fileName);
		}
		
		create(new BufferedInputStream(fin));
	}
	
	public IndexedSound toIndexedSound(){
		final IndexedSound result = new IndexedSound();
		
		result.setData(data);
		result.setFormat(format);
		result.setSamplerate(samplerate);
		
		data.clear();
		
		return result;
	}
	
	private void create(InputStream is){
		try{
			create(AudioSystem.getAudioInputStream(is));
		}catch(UnsupportedAudioFileException | IOException e){
			NeonEngine.throwError("Error: unable to create from inputstream, " + e.getMessage());
		}
	}
	
	private void create(AudioInputStream ais){
		final AudioFormat audioformat = ais.getFormat();
		
		int channels = 0;
		if(audioformat.getChannels() == 1){
			if(audioformat.getSampleSizeInBits() == 8){
				channels = AL10.AL_FORMAT_MONO8;
			}else if(audioformat.getSampleSizeInBits() == 16){
				channels = AL10.AL_FORMAT_MONO16;
			}else{
				NeonEngine.throwError("Error: Illegal sample size.");
			}
		}else if(audioformat.getChannels() == 2){
			if(audioformat.getSampleSizeInBits() == 8){
				channels = AL10.AL_FORMAT_STEREO8;
			}else if(audioformat.getSampleSizeInBits() == 16){
				channels = AL10.AL_FORMAT_STEREO16;
			}else{
				NeonEngine.throwError("Error: Illegal sample size: " + audioformat.getSampleSizeInBits() + ".");
			}
		}else{
			NeonEngine.throwError("Error: Only mono or stereo is supported.");
		}
		
		ByteBuffer buffer = null;
		try{
			int available = ais.available();
			if(available <= 0){
				available = ais.getFormat().getChannels() * (int)ais.getFrameLength() * ais.getFormat().getSampleSizeInBits() / 8;
			}
			
			final byte[] buf = new byte[available];
			int read = 0;
			int total = 0;
			
			while((read = ais.read(buf, total, buf.length - total)) != -1 && total < buf.length){
				total += read;
			}
			
			buffer = convertAudioBytes(buf, audioformat.getSampleSizeInBits() == 16);
		}catch(IOException e){
			NeonEngine.throwError("Error: Failed to create the buffer.");
		}
		
		data = buffer;
		format = channels;
		samplerate = (int)audioformat.getSampleRate();
		
		try{
			ais.close();
		}catch(IOException e){
			NeonEngine.throwError("Error: Failed to close the AudioInputStream.");
		}
	}
	
	private static ByteBuffer convertAudioBytes(byte[] audioBytes, boolean twoBytesData){
		final ByteBuffer dest = ByteBuffer.allocateDirect(audioBytes.length);
		dest.order(ByteOrder.nativeOrder());
		
		final ByteBuffer src = ByteBuffer.wrap(audioBytes);
		src.order(ByteOrder.LITTLE_ENDIAN);
		
		if(twoBytesData){
			final ShortBuffer destShort = dest.asShortBuffer();
			final ShortBuffer srcShort = src.asShortBuffer();
			
			while(srcShort.hasRemaining()){
				destShort.put(srcShort.get());
			}
		}else{
			while(src.hasRemaining()){
				dest.put(src.get());
			}
		}
		
		dest.rewind();
		return dest;
	}
}
