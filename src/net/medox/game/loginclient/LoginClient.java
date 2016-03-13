package net.medox.game.loginclient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import net.medox.game.LoginPacket.Packet00LoginRequest;
import net.medox.game.LoginPacket.Packet01LoginAnswer;
import net.medox.game.LoginPacket.Packet02AddAcount;
import net.medox.game.LoginPacket.Packet03AddAnswer;
import net.medox.game.LoginPacket.Packet04DownloadRequest;
import net.medox.game.LoginPacket.Packet05DownloadAnswer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class LoginClient{
	public Client client;
	
	public int tcpPort = 48561;
	public int udpPort = 48562;
	
	public String name;
	public String password;
	
	public LoginClient(){
		client = new Client();
		registerPackets();
		
		NetworkListener nl = new NetworkListener();
		nl.init(this);
		client.addListener(nl);
		
		client.start();
		
		name = JOptionPane.showInputDialog(null, "Please enter your Username", "Project Guns", 1);
				
		if(name == null){
			name = "";
		}
		
		while(name.equals("")){
			name = JOptionPane.showInputDialog(null, "Please enter your Username", "Project Guns", 1);
			
			if(name == null){
				name = "";
			}
		}
		
		password = JOptionPane.showInputDialog(null, "Please enter your Password", "Project Guns", 1);
		
		if(password == null){
			password = "";
		}
		
		while(password.equals("")){
			password = JOptionPane.showInputDialog(null, "Please enter your Password", "Project Guns", 1);
			
			if(password == null){
				password = "";
			}
		}
		
		String ip = JOptionPane.showInputDialog(null, "Please enter the IP", "Project Guns", 1);
		
		if(ip == null){
			ip = "";
		}
		
		while(ip.equals("")){
			ip = JOptionPane.showInputDialog(null, "Please enter the IP", "Project Guns", 1);
			
			if(ip == null){
				ip = "";
			}
		}
		
		try{
			client.connect(5000, ip, tcpPort, udpPort);
		}catch(IOException e){
			System.out.println("ERROR: Unable to connect to: /" + ip);
			client.stop();
			System.exit(1);
		}
		
		SendThread send = new SendThread(this);
		send.start();
	}
	
	private void registerPackets(){
		Kryo kryo = client.getKryo();
		kryo.register(Packet00LoginRequest.class);
		kryo.register(Packet01LoginAnswer.class);
		kryo.register(Packet02AddAcount.class);
		kryo.register(Packet03AddAnswer.class);
		kryo.register(Packet04DownloadRequest.class);
		kryo.register(Packet05DownloadAnswer.class);
	}
	
	public static void main(String[] args){
		new LoginClient();
		Log.set(Log.LEVEL_INFO);
	}
	
	public static void download(String url, String filename){
		try{
			File f = new File(filename);
	        
	        f.delete();
	        
	        URL URL = new URL(url);
	        URL.openConnection();
	        InputStream reader = URL.openStream();
	        
	        FileOutputStream writer = new FileOutputStream(filename);
	        byte[] buffer = new byte[153600];
	        int bytesRead = 0;
	        
	        while((bytesRead = reader.read(buffer)) > 0){  
	           writer.write(buffer, 0, bytesRead);
	           buffer = new byte[153600];
	        }
	        
	        writer.close();
	        reader.close();
	    }catch(Exception e){
	    	
		}
	}
	
	public static boolean deleteFolder(File directory){
	    if(directory.exists()){
	        File[] files = directory.listFiles();
	        if(null != files){
	            for(int i = 0; i < files.length; i++){
	                if(files[i].isDirectory()) {
	                	deleteFolder(files[i]);
	                }else{
	                    files[i].delete();
	                }
	            }
	        }
	    }
	    
	    return(directory.delete());
	}
}
