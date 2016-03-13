package net.medox.game.loginclient;

import net.medox.game.LoginPacket.Packet00LoginRequest;
import net.medox.game.LoginPacket.Packet01LoginAnswer;
import net.medox.game.LoginPacket.Packet03AddAnswer;
import net.medox.game.LoginPacket.Packet04DownloadRequest;
import net.medox.game.LoginPacket.Packet05DownloadAnswer;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{
	private LoginClient client;
	
	public void init(LoginClient client){
		this.client = client;
	}
	
	@Override
	public void connected(Connection c){
		Log.info("[CLIENT] connected");
		
		Packet00LoginRequest loginRequest = new Packet00LoginRequest();
		loginRequest.name = client.name;
		loginRequest.password = client.password;
		
		client.client.sendTCP(loginRequest);
		
//		Packet02AddAcount addRequest = new Packet02AddAcount();
//		addRequest.name = client.name;
//		addRequest.password = client.password;
//		
//		client.client.sendTCP(addRequest);
	}
	
	@Override
	public void disconnected(Connection c){
		Log.info("[CLIENT] disconnected");
	}
	
	@Override
	public void received(Connection c, Object o){
		if(o instanceof Packet01LoginAnswer){
			boolean accepted = ((Packet01LoginAnswer) o).accepted;
			
			if(accepted){
				Log.info("[CLIENT] login accepted");
			}else{
				Log.info("[CLIENT] login denied");
			}
			
			Packet04DownloadRequest downloadRequest = new Packet04DownloadRequest();
			
			client.client.sendTCP(downloadRequest);
		}
		
		if(o instanceof Packet03AddAnswer){
			boolean accepted = ((Packet03AddAnswer) o).accepted;
			
			if(accepted){
				Log.info("[CLIENT] add accepted");
			}else{
				Log.info("[CLIENT] add denied");
			}
		}
		
		if(o instanceof Packet05DownloadAnswer){
			String url = ((Packet05DownloadAnswer) o).url;
			String filename = ((Packet05DownloadAnswer) o).filename;
			
			if(!url.equals("")){
				Log.info("[CLIENT] got download link");
				
				LoginClient.download(url, filename);
			}else{
				Log.info("[CLIENT] not got download link");
			}
		}
	}
}
