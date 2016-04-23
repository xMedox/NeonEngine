package net.medox.game.server;

import java.io.IOException;
import java.util.ArrayList;

import net.medox.game.Packet.*;
import net.medox.game.server.SendThread;
import net.medox.game.server.TestGame;
import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;

public class MPServer{
	public Server server;
	
	public int tcpPort = 48563;
	public int udpPort = 48564;
	
	public final int playerMax = 2;
	
	public TestGame game;
	
	public int time = 0;
	
	public ArrayList<Client> players = new ArrayList<Client>();
	
	public MPServer() throws IOException{
		server = new Server();
		registerPackets();
		
		NetworkListener nl = new NetworkListener();
		nl.init(this);
		server.addListener(nl);
		
		server.bind(tcpPort, udpPort);
		
		server.start();
		
		game = new TestGame();
		game.addEntity(new Entity().addComponent(new TimeAdder(this)));
		CoreEngine.init(game, 60);
		
		SendThread send = new SendThread(this);
		send.start();
		
		CoreEngine.start();
	}
	
	private void registerPackets(){
		Kryo kryo = server.getKryo();
		kryo.register(Packet00LoginRequest.class);
		kryo.register(Packet01LoginAnswer.class);
		kryo.register(Packet02AddPlayer.class);
		
		kryo.register(float[].class);
		kryo.register(float[][].class);
		kryo.register(Matrix4f.class);
		kryo.register(Quaternion.class);
		kryo.register(Vector3f.class);
		
		kryo.register(Packet03RemovePlayer.class);
		kryo.register(Packet04MoveLookClient.class);
		kryo.register(Packet05PlayerTransform.class);
		kryo.register(Packet06MoveServer.class);
		kryo.register(Packet07Ping.class);
		kryo.register(Packet08PlayerPing.class);
		kryo.register(Packet09WeaponID.class);
		kryo.register(Packet10PlayerWeaponID.class);
		kryo.register(Packet11ServerTime.class);
	}
	
	public static void main(String[] args){
		try{
			new MPServer();
			Log.set(Log.LEVEL_INFO);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}

class Client{
	public Connection connection;
	public Player player;
}
