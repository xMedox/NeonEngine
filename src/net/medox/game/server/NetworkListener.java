package net.medox.game.server;

import net.medox.game.Packet.*;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{
	private MPServer server;
	
	public void init(MPServer server){
		this.server = server;
	}
	
	@Override
	public void connected(Connection c){
//		Log.info(Integer.toString(c.getReturnTripTime()));
		if(server.players.toArray().length <= server.playerMax){
			Log.info("[SERVER] client connected");
		}
	}
	
	@Override
	public void disconnected(Connection c){
		for(int i = 0; i < server.players.toArray().length; i ++){
			if(server.players.get(i).connection.equals(c)){
				server.game.removeEntity(server.players.get(i).player);
				
				server.players.remove(i);
				
				Log.info("[SERVER] client disconnected");
				Log.info(Integer.toString(server.players.toArray().length) + "/" + Integer.toString(server.playerMax));
				
				Packet03RemovePlayer remove = new Packet03RemovePlayer();
				remove.id = c.getID();
				
				server.server.sendToAllTCP(remove);
			}
		}
	}
	
	@Override
	public void received(Connection c, Object o){
		if(o instanceof Packet00LoginRequest){
			String name = ((Packet00LoginRequest) o).name;
			
			Packet01LoginAnswer loginAnswer = new Packet01LoginAnswer();
			loginAnswer.accepted = server.players.toArray().length < server.playerMax;
			c.sendTCP(loginAnswer);
			
			if(server.players.toArray().length >= server.playerMax){
				c.close();
			}else{
				Player player = new Player();
				player.name = name;
				player.id = c.getID();
				
//				server.players.put(c.getID(), player);
				server.game.addEntity(player);
				
				Client client = new Client();
				client.connection = c;
				client.player = player;
				
				server.players.add(client);
				
				Log.info(Integer.toString(server.players.toArray().length) + "/" + Integer.toString(server.playerMax));
				
				for(int i = 0; i < server.players.toArray().length; i ++){
					Packet02AddPlayer add = new Packet02AddPlayer();
					add.id = server.players.get(i).player.id;
					add.name = name;
					
					c.sendTCP(add);
				}
				
				Packet02AddPlayer add = new Packet02AddPlayer();
				add.id = c.getID();
				add.name = name;
				
				server.server.sendToAllTCP(add);
			}
		}
		
		if(o instanceof Packet04MoveLookClient){
			for(int i = 0; i < server.players.toArray().length; i ++){
				if(server.players.get(i).connection.equals(c)){
					server.players.get(i).player.getTransform().setPos(((Packet04MoveLookClient) o).pos);
					server.players.get(i).player.getTransform().setRot(((Packet04MoveLookClient) o).rot);
				}
			}
		}
		
		if(o instanceof Packet07Ping){
			int ping = c.getReturnTripTime();
			c.updateReturnTripTime();
			
			Packet08PlayerPing playerPing = new Packet08PlayerPing();
			
			playerPing.id = c.getID();
			playerPing.ping = ping;
			
			for(int i = 0; i < server.players.toArray().length; i ++){
				if(server.players.get(i).connection.equals(c)){
					server.players.get(i).player.ping = ping;
				}
			}
			
			server.server.sendToAllTCP(playerPing);
		}
		
		if(o instanceof Packet09WeaponID){
			int id = ((Packet09WeaponID) o).weaponID;
			
			Packet10PlayerWeaponID playerWeapon = new Packet10PlayerWeaponID();
			
			playerWeapon.id = c.getID();
			playerWeapon.weaponID = id;
			
			for(int i = 0; i < server.players.toArray().length; i ++){
				if(server.players.get(i).connection.equals(c)){
					server.players.get(i).player.weaponID = id;
				}
			}
			
			server.server.sendToAllTCP(playerWeapon);
		}
	}
}
