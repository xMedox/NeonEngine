package net.medox.game.client;

import net.medox.game.Packet.Packet04MoveLookClient;
import net.medox.game.Packet.Packet07Ping;
import net.medox.game.Packet.Packet09WeaponID;

import com.esotericsoftware.kryonet.FrameworkMessage;

public class SendThread extends Thread{
	public MPClient client;
	public int timer = 0;
	
	public int sleepTime = 10;
	
	public SendThread(MPClient client){
		this.client = client;
	}
	
	@Override
	public void run(){
		while(client.open){
			if(true){
				Packet04MoveLookClient move = new Packet04MoveLookClient();
				move.pos = client.player.getTransform().getPos();
				move.rot = client.player.getTransform().getRot();
				
				client.client.sendUDP(move);
				
				if(client.inventory != null){
					client.weaponID = client.inventory.currentWeapon.getID();
				}
				
				Packet09WeaponID weapon = new Packet09WeaponID();
				weapon.weaponID = client.weaponID;
				
				client.client.sendUDP(weapon);
			}
			
			if(timer == 8000/sleepTime){
				client.client.sendTCP(FrameworkMessage.keepAlive);
				timer = 0;
			}
			
			if(timer == 1000/sleepTime || timer == 2000/sleepTime || timer == 3000/sleepTime || timer == 4000/sleepTime || timer == 5000/sleepTime || timer == 6000/sleepTime || timer == 7000/sleepTime || timer == 8000/sleepTime){
				Packet07Ping ping = new Packet07Ping();
				
				client.client.sendTCP(ping);
			}
			
			timer++;
			
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
