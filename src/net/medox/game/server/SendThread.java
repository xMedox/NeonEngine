package net.medox.game.server;

import net.medox.game.Packet.Packet05PlayerTransform;
import net.medox.game.Packet.Packet11ServerTime;

public class SendThread extends Thread{
	public MPServer server;
	
	public int sleepTime = 10;
	
	public SendThread(MPServer server){
		this.server = server;
	}
	
	@Override
	public void run(){
		while(true){
			for(int i = 0; i < server.players.toArray().length; i ++){
				Packet05PlayerTransform transform = new Packet05PlayerTransform();
				transform.id = server.players.get(i).player.id;
				
				transform.pos = server.players.get(i).player.getTransform().getPos();
				transform.rot = server.players.get(i).player.getTransform().getRot();
				
				server.server.sendToAllUDP(transform);
			}
			
			Packet11ServerTime time = new Packet11ServerTime();
			time.currentTime = server.time;
			
			server.server.sendToAllUDP(time);
			
			try{
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}
}
