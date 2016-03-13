package net.medox.game;

import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class Packet{
	public static class Packet00LoginRequest{ public String name; }
	public static class Packet01LoginAnswer{ public boolean accepted; }
	public static class Packet02AddPlayer{ public int id; public String name; }
	public static class Packet03RemovePlayer{ public int id; }
	public static class Packet04MoveLookClient{ public Vector3f pos; public Quaternion rot; }
	public static class Packet05PlayerTransform{ public int id; public Vector3f pos; public Quaternion rot; }
	public static class Packet06MoveServer{ public Vector3f pos; }
	public static class Packet07Ping{ }
	public static class Packet08PlayerPing{ public int id; public int ping; }
	public static class Packet09WeaponID{ public int weaponID; }
	public static class Packet10PlayerWeaponID{ public int id; public int weaponID; }
	public static class Packet11ServerTime{ public int currentTime; }
}
