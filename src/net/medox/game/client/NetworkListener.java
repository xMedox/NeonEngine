package net.medox.game.client;

import net.medox.game.Packet.*;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;

public class NetworkListener extends Listener{
	private MPClient client;
	
	public void init(MPClient client){
		this.client = client;
	}
	
	@Override
	public void connected(Connection c){
//		Log.info(Integer.toString(c.getReturnTripTime()));
		Log.info("[CLIENT] connected");
		
		Packet00LoginRequest loginRequest = new Packet00LoginRequest();
		loginRequest.name = client.name;
		
		client.client.sendTCP(loginRequest);
	}
	
	@Override
	public void disconnected(Connection c){
//		Log.info(Integer.toString(c.getReturnTripTime()));
		Log.info("[CLIENT] disconnected");
	}
	
	@Override
	public void received(Connection c, Object o){
		if(o instanceof Packet01LoginAnswer){
			boolean answer = ((Packet01LoginAnswer) o).accepted;
			
			if(answer){
				client.id = c.getID();
				client.game.addEntity(client.player);
				
//				Log.info("Please enter your message");
//				
//				Chat chat = new Chat(client);
//				chat.start();
			}else{
				client.open = false;
				client.client.close();
			}
		}
		
		if(o instanceof Packet05PlayerTransform){
			int id = ((Packet05PlayerTransform) o).id;
			
			if(id == client.id){
//				Vector3f pos = ((Packet05PlayerTransform) o).pos;
//				Quaternion rot = ((Packet05PlayerTransform) o).rot;
//				
//				client.player.getTransform().setPos(pos);
//				client.player.getTransform().setRot(rot);
			}else{
				if(client.players.get(id) != null){
					Vector3f pos = ((Packet05PlayerTransform) o).pos;
					Quaternion rot = ((Packet05PlayerTransform) o).rot;
					
					client.players.get(id).getTransform().setPos(pos);
					client.players.get(id).getTransform().setRot(rot);
				}
			}
		}
		
		if(o instanceof Packet02AddPlayer){
			int id = ((Packet02AddPlayer) o).id;
			
			if(id != client.id){
				String name = ((Packet02AddPlayer) o).name;
				
				Player player = new Player();
				player.id = id;
				player.name = name;
				
				client.players.put(id, player);
				client.game.addEntity(player);
			}
		}
		
		if(o instanceof Packet03RemovePlayer){
			int id = ((Packet03RemovePlayer) o).id;
			
			if(id != client.id){
				client.game.removeEntity(client.players.get(id));
				client.players.remove(id);
			}
		}
		
		if(o instanceof Packet08PlayerPing){
			int id = ((Packet08PlayerPing) o).id;
			int ping = ((Packet08PlayerPing) o).ping;
			
			if(id == client.id){
				client.ping = ping;
			}else{
				if(client.players.get(id) != null){
					client.players.get(id).ping = ping;
				}
			}
		}
		
		if(o instanceof Packet10PlayerWeaponID){
			int id = ((Packet10PlayerWeaponID) o).id;
			int weapon = ((Packet10PlayerWeaponID) o).weaponID;
			
			if(id == client.id){
				
			}else{
				if(client.players.get(id) != null){
					client.players.get(id).changeWeaponID(weapon);
				}
			}
		}
		
		if(o instanceof Packet11ServerTime){
			client.serverTime = ((Packet11ServerTime) o).currentTime;
		}
	}
}
