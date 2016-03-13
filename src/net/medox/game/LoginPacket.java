package net.medox.game;

public class LoginPacket{
	public static class Packet00LoginRequest{ public String name; public String password; }
	public static class Packet01LoginAnswer{ public boolean accepted; }
	public static class Packet02AddAcount{ public String name; public String password; }
	public static class Packet03AddAnswer{ public boolean accepted; }
	public static class Packet04DownloadRequest{ }
	public static class Packet05DownloadAnswer{ public String url; public String filename; }
}
