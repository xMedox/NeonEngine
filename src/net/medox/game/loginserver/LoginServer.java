package net.medox.game.loginserver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

import net.medox.game.LoginPacket.Packet00LoginRequest;
import net.medox.game.LoginPacket.Packet01LoginAnswer;
import net.medox.game.LoginPacket.Packet02AddAcount;
import net.medox.game.LoginPacket.Packet03AddAnswer;
import net.medox.game.LoginPacket.Packet04DownloadRequest;
import net.medox.game.LoginPacket.Packet05DownloadAnswer;

public class LoginServer{
	public Server server;
	
	public ArrayList<Client> players = new ArrayList<Client>();
	
	public int tcpPort = 48561;
	public int udpPort = 48562;
	
	public String url = "https://www.dropbox.com/s/f3xejw5d6qhl0bo/test.txt?dl=1";
	public String filename = "test.txt";
	
	public LoginServer() throws IOException{
		File f = new File("accounts");
		
		if(!f.exists() || !f.isDirectory()){
			f.mkdirs();
		}
		
		server = new Server();
		registerPackets();
		
		NetworkListener nl = new NetworkListener();
		nl.init(this);
		server.addListener(nl);
		
		server.bind(tcpPort, udpPort);
		
		server.start();
		
		SendThread send = new SendThread(this);
		send.start();
	}
	
	private void registerPackets(){
		Kryo kryo = server.getKryo();
		kryo.register(Packet00LoginRequest.class);
		kryo.register(Packet01LoginAnswer.class);
		kryo.register(Packet02AddAcount.class);
		kryo.register(Packet03AddAnswer.class);
		kryo.register(Packet04DownloadRequest.class);
		kryo.register(Packet05DownloadAnswer.class);
	}
	
	public static void main(String[] args){
		try{
			new LoginServer();
			Log.set(Log.LEVEL_INFO);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

class Client{
	public Connection connection;
	public String name;
}
