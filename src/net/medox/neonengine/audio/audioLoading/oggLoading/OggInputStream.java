package net.medox.neonengine.audio.audioLoading.oggLoading;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.Block;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.Comment;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.DspState;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.Info;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.Packet;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.Page;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.StreamState;
import net.medox.neonengine.audio.audioLoading.oggLoading.JOrbis.SyncState;

import org.lwjgl.BufferUtils;

public class OggInputStream extends InputStream implements AudioInputStream{
	private int convsize = 4096 * 4;
	final private byte[] convbuffer = new byte[convsize];
	final private InputStream input;
	final private Info oggInfo = new Info();
	private boolean endOfStream;

	final private SyncState syncState = new SyncState();
	final private StreamState streamState = new StreamState();
	final private Page page = new Page();
	final private Packet packet = new Packet();

	final private Comment comment = new Comment();
	final private DspState dspState = new DspState();
	final private Block vorbisBlock = new Block(dspState);
	
	byte[] buffer;
	int bytes = 0;
	final boolean bigEndian = ByteOrder.nativeOrder().equals(ByteOrder.BIG_ENDIAN);
	boolean endOfBitStream = true;
	boolean inited = false;
	
	private int readIndex;
	final private ByteBuffer pcmBuffer = BufferUtils.createByteBuffer(4096 * 500);
	final private int total;
	
	public OggInputStream(InputStream input) throws IOException{
		this.input = input;
		total = input.available();
		
		init();
	}
	
	public int getLength(){
		return total;
	}
	
	public int getChannels(){
		return oggInfo.channels;
	}
	
	public int getRate(){
		return oggInfo.rate;
	}
	
	private void init() throws IOException{
		initVorbis();
		readPCM();
	}
		
	public int available(){
		return endOfStream ? 0 : 1;
	}
	
	private void initVorbis(){
		syncState.init();
	}
	
	private boolean getPageAndPacket(){
		int index = syncState.buffer(4096);
		
		buffer = syncState.data;
		if(buffer == null){
			endOfStream = true;
			return false;
		}
		
		try{
			bytes = input.read(buffer, index, 4096);
		}catch(Exception e){
			org.lwjgl.system.APIUtil.apiLog("Failure reading in vorbis");
			endOfStream = true;
			return false;
		}
		syncState.wrote(bytes);

		if(syncState.pageout(page) != 1){
			if(bytes < 4096){
				return false;
			}
			
			org.lwjgl.system.APIUtil.apiLog("Input does not appear to be an Ogg bitstream.");
			endOfStream = true;
			return false;
		}
		
		streamState.init(page.serialno());
		
		oggInfo.init();
		comment.init();
		if(streamState.pagein(page) < 0){
			org.lwjgl.system.APIUtil.apiLog("Error reading first page of Ogg bitstream data.");
			endOfStream = true;
			return false;
		}
		
		if(streamState.packetout(packet) != 1){
			org.lwjgl.system.APIUtil.apiLog("Error reading initial header packet.");
			endOfStream = true;
			return false;
		}
		
		if(oggInfo.synthesis_headerin(comment, packet) < 0){
			org.lwjgl.system.APIUtil.apiLog("This Ogg bitstream does not contain Vorbis audio data.");
			endOfStream = true;
			return false;
		}
		
		int i = 0;
		while(i < 2){
			while(i < 2){

				int result = syncState.pageout(page);
				if(result == 0){
					break;
				}
				
				if(result == 1){
					streamState.pagein(page);
					while(i < 2){
						result = streamState.packetout(packet);
						if(result == 0){
							break;
						}
						if(result == -1){
							org.lwjgl.system.APIUtil.apiLog("Corrupt secondary header.  Exiting.");
							endOfStream = true;
							return false;
						}
						
						oggInfo.synthesis_headerin(comment, packet);
						i++;
					}
				}
			}
			index = syncState.buffer(4096);
			buffer = syncState.data;
			try{
				bytes = input.read(buffer, index, 4096);
			}catch(Exception e){
				org.lwjgl.system.APIUtil.apiLog("Failed to read Vorbis: ");
				endOfStream = true;
				return false;
			}
			if(bytes == 0 && i < 2){
				org.lwjgl.system.APIUtil.apiLog("End of file before finding all Vorbis headers!");
				endOfStream = true;
				return false;
			}
			syncState.wrote(bytes);
		}

		convsize = 4096 / oggInfo.channels;

		dspState.synthesis_init(oggInfo);
		vorbisBlock.init(dspState);
		
		return true;
	}
	
	private void readPCM() throws IOException{
		boolean wrote = false;
		
		while(true){
			if(endOfBitStream){
				if(!getPageAndPacket()){
					break;
				}
				endOfBitStream = false;
			}

			if(!inited){
				inited = true;
				return;
			}
			
			final float[][][] _pcm = new float[1][][];
			final int[] _index = new int[oggInfo.channels];
			while(!endOfBitStream){
				while(!endOfBitStream){
					int result = syncState.pageout(page);
					
					if(result == 0){
						break;
					}
					
					if(result == -1){
						org.lwjgl.system.APIUtil.apiLog("Corrupt or missing data in bitstream; continuing...");
					}else{
						streamState.pagein(page);
						while(true){
							result = streamState.packetout(packet);

							if(result == 0){
								break;
							}
							if(result == -1){
								
							}else{
								int samples;
								if(vorbisBlock.synthesis(packet) == 0){
									dspState.synthesis_blockin(vorbisBlock);
								}
								
								while((samples = dspState.synthesis_pcmout(_pcm, _index)) > 0){
									final float[][] pcm = _pcm[0];
									final int bout =(samples < convsize ? samples : convsize);
									
									for(int i = 0; i < oggInfo.channels; i++){
										int ptr = i * 2;
										final int mono = _index[i];
										for(int j = 0; j < bout; j++){
											int val = (int) (pcm[i][mono + j] * 32767.);
											if(val > 32767){
												val = 32767;
											}
											if(val < -32768){
												val = -32768;
											}
											if(val < 0){
												val = val | 0x8000;
											}
				
											if(bigEndian){
												convbuffer[ptr] = (byte)(val >>> 8);
												convbuffer[ptr + 1] = (byte)(val);
											}else{
												convbuffer[ptr] = (byte)(val);
												convbuffer[ptr + 1] = (byte)(val >>> 8);
											}
											ptr += 2 * (oggInfo.channels);
										}
									}

									final int bytesToWrite = 2 * oggInfo.channels * bout;
									if(bytesToWrite >= pcmBuffer.remaining()){
										org.lwjgl.system.APIUtil.apiLog("Read block from OGG that was too big to be buffered: " + bytesToWrite);
									}else{
										pcmBuffer.put(convbuffer, 0, bytesToWrite);
									}
									
									wrote = true;
									dspState.synthesis_read(bout);
								}
							}
						}
						if(page.eos() != 0){
							endOfBitStream = true;
						} 
						
						if(!endOfBitStream && wrote){
							return;
						}
					}
				}

				if(!endOfBitStream){
					bytes = 0;
					final int index = syncState.buffer(4096);
					if(index >= 0){
						buffer = syncState.data;
						try{
							bytes = input.read(buffer, index, 4096);
						}catch(Exception e){
							org.lwjgl.system.APIUtil.apiLog("Failure during vorbis decoding");
							endOfStream = true;
							return;
						}
					}else{
						bytes = 0;
					}
					syncState.wrote(bytes);
					if(bytes == 0){
						endOfBitStream = true;
					}
				}
			}
			
			streamState.clear();
			
			vorbisBlock.clear();
			dspState.clear();
			oggInfo.clear();
		}
		
		syncState.clear();
		endOfStream = true;
	}
	
	public int read() throws IOException{
		if(readIndex >= pcmBuffer.position()){
			pcmBuffer.clear();
			readPCM();
			readIndex = 0;
		}
		if(readIndex >= pcmBuffer.position()){
			return -1;
		}

		int value = pcmBuffer.get(readIndex);
		if(value < 0){
			value = 256 + value;
		}
		readIndex++;
		
		return value;
	}
	
	public boolean atEnd(){
		return endOfStream && readIndex >= pcmBuffer.position();
	}
	
	public int read(byte[] b, int off, int len) throws IOException{
		for(int i = 0;i < len; i++){
			try{
				final int value = read();
				if(value >= 0){
					b[i] = (byte) value;
				}else{
					if(i == 0){						
						return -1;
					}else{
						return i;
					}
				}
			}catch(IOException e){
				org.lwjgl.system.APIUtil.apiLog(e.getMessage());
				return i;
			}
		}
		
		return len;
	}
	
	public int read(byte[] b) throws IOException{
		return read(b, 0, b.length);
	}
	
	public void close() throws IOException{
		
	}
}
