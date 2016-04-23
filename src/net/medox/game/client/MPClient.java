package net.medox.game.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

import net.medox.game.Packet.*;
import net.medox.game.client.Player;
import net.medox.game.client.TestGame;
import net.medox.neonengine.core.CoreEngine;
import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Window;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class MPClient{
	public Client client;
//	public Scanner scanner;
	
	public int tcpPort = 48563;
	public int udpPort = 48564;
	
	public FPPlayer player;
	
	public boolean open = true;
	
	public String name;
	public int id;
	public int ping;
	public int weaponID;
	
	public TestGame game;
	
	public Map<Integer, Player> players = new HashMap<Integer, Player>();
	
	public Inventory inventory;
	
	public int serverTime = 0;
	
	public MPClient(){
//		scanner = new Scanner(System.in);
		
		client = new Client();
		registerPackets();
		
		NetworkListener nl = new NetworkListener();
		nl.init(this);
		client.addListener(nl);
		
		game = new TestGame(this);
		CoreEngine.init(game, 60);
		
		Window.setStartTitle("Project Guns");
		Window.setStartDimensions(854, 480);
		Window.setStartFullscreen(false);
		Window.setStartResizable(true);
		Window.setStartIcon("./res/icon_16.png", "./res/icon_32.png");
		Window.setStartCursor("cursor.png", 0, 0);
		
//		Log.info("Please enter your Username");
//		name = scanner.nextLine();
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
		
		CoreEngine.createWindow();
		
		player = new FPPlayer();
		
//		System.out.println("--------------------------------------------------------------");
////		System.out.println("Game version:   " + "Alpha 0.3");
////		System.out.println("Engine version: " + CoreEngine.getVersion());
//		System.out.println("OS name:        " + System.getProperty("os.name"));
//		System.out.println("OS version:     " + System.getProperty("os.version"));
//		System.out.println("OS arch:        " + System.getProperty("os.arch"));
//		System.out.println("OS arch:        " + System.getProperty("sun.arch.data.model"));
//		System.out.println("Java version:   " + System.getProperty("java.version"));
//		System.out.println("LWJGL version:  " + Version.getVersion());
////		System.out.println("LWJGL version:  " + Sys.VERSION_MAJOR + "|" + Sys.VERSION_MINOR + "|" + Sys.VERSION_REVISION);
//		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
//		System.out.println("--------------------------------------------------------------");
		
		client.start();
		
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
//			Log.info("Please enter the IP");
//			client.connect(5000, scanner.nextLine(), 48563, 48564);
			client.connect(5000, ip, tcpPort, udpPort);
		}catch(IOException e){
			System.out.println("ERROR: Unable to connect to: /" + ip);
			client.stop();
			System.exit(1);
		}
		
		SendThread send = new SendThread(this);
		send.start();
		
		CoreEngine.start();
	}
	
	private void registerPackets(){
		Kryo kryo = client.getKryo();
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
		new MPClient();
		Log.set(Log.LEVEL_INFO);
	}
}
