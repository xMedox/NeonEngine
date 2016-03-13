package net.medox.neonengine.audio.audioLoading.oggLoading;

import java.io.IOException;

interface AudioInputStream{
	public int getChannels();
	public int getRate();
	public int read() throws IOException;
	public int read(byte[] data) throws IOException;
	public int read(byte[] data, int ofs, int len) throws IOException;
	public boolean atEnd();
	public void close() throws IOException;
}
