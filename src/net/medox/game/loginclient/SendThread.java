package net.medox.game.loginclient;

public class SendThread extends Thread{
	public LoginClient client;
	
	public SendThread(LoginClient client){
		this.client = client;
	}
	
	@Override
	public void run(){
		while(true){
			
		}
	}
}
