package net.medox.game.loginserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

import net.medox.game.LoginPacket.Packet00LoginRequest;
import net.medox.game.LoginPacket.Packet01LoginAnswer;
import net.medox.game.LoginPacket.Packet02AddAcount;
import net.medox.game.LoginPacket.Packet03AddAnswer;
import net.medox.game.LoginPacket.Packet04DownloadRequest;
import net.medox.game.LoginPacket.Packet05DownloadAnswer;

public class NetworkListener extends Listener{
	private LoginServer server;
	
	public void init(LoginServer server){
		this.server = server;
	}
	
	@Override
	public void connected(Connection c){
		Log.info("[SERVER] client connected");
	}
	
	@Override
	public void disconnected(Connection c){
		for(int i = 0; i < server.players.toArray().length; i ++){
			if(server.players.get(i).connection.equals(c)){
				System.out.println(server.players.get(i).name);
				
				server.players.remove(i);
			}
		}
		
		System.out.println(server.players.toArray().length);
		
//			server.players.remove(c.getID());
		
		Log.info("[SERVER] client disconnected");
	}
	
	@Override
	public void received(Connection c, Object o){
		if(o instanceof Packet00LoginRequest){
			String name = ((Packet00LoginRequest) o).name;
			String password = ((Packet00LoginRequest) o).password;
			
			Packet01LoginAnswer loginAnswer = new Packet01LoginAnswer();
			
			loginAnswer.accepted = false;
			
			boolean exists = false;
			
			for(int i = 0; i < server.players.toArray().length; i ++){
				if(server.players.get(i).name.equals(name)){
					exists = true;
				}
			}
			
			if(exists){
				Log.info("[SERVER] client: " + name + " tried to connect again");
				
				c.sendTCP(loginAnswer);
			}else{
				File f = new File("accounts/" + name + ".txt");
				
				if(f.isFile()){
					try{
				    	FileInputStream fstream = new FileInputStream("accounts/" + name + ".txt");
				    	DataInputStream in = new DataInputStream(fstream);
				    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
				    	
			    	    if(br.readLine().equals(password)){
			    	    	loginAnswer.accepted = true;
			    	    }
				    	
				    	in.close();
				    }catch(Exception e){
				    	e.printStackTrace();
					}
				}
				
				c.sendTCP(loginAnswer);
				
				if(loginAnswer.accepted){
					Log.info("[SERVER] client: " + name + " has connected with the password: " + password);
					
//					server.players.put(c.getID(), name);
					
					Client client = new Client();
					client.connection = c;
					client.name = name;
					
					server.players.add(client);
				}else{
					Log.info("[SERVER] client: " + name + " has tried to connect with the password: " + password);
				}
			}
		}
		
		if(o instanceof Packet02AddAcount){
			String name = ((Packet02AddAcount) o).name;
			String password = ((Packet02AddAcount) o).password;
			
			Packet03AddAnswer addAnswer = new Packet03AddAnswer();
			
			addAnswer.accepted = false;
			
			File f = new File("accounts/" + name + ".txt");
			
			if(f.isFile()){
				Log.info("[SERVER] client: " + name + " already exists");
			}else{
				Log.info("[SERVER] client: " + name + " is getting created");
				
				addAnswer.accepted = true;
				
				try{
					f.createNewFile();
					
			    	FileWriter fstream = new FileWriter("accounts/" + name + ".txt");
					BufferedWriter out = new BufferedWriter(fstream);
					
					out.write(password);
					
					out.close();
			    }catch(Exception e){
					addAnswer.accepted = false;
					
					Log.info("[SERVER] client: " + name + " creation failed");
				}
				
				Log.info("[SERVER] client: " + name + " creation successful");
			}
			
			c.sendTCP(addAnswer);
		}
		
		if(o instanceof Packet04DownloadRequest){
			Packet05DownloadAnswer answer = new Packet05DownloadAnswer();
			
			boolean found = false;
			
			for(int i = 0; i < server.players.toArray().length; i ++){
				if(server.players.get(i).connection.equals(c)){
					found = true;
					
					answer.url = server.url;
					answer.filename = server.filename;
					
					Log.info("[SERVER] send download link");
				}
			}
			
			if(!found){
				answer.url = "";
				answer.filename = "";
				
				Log.info("[SERVER] not send download link");
			}
			
			c.sendTCP(answer);
		}
	}
}
