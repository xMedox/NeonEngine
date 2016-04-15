package net.medox.neonengine.core;

public class InputKey{
	private int device;
	private int keycode;

	public InputKey(){
		this(-1, -1);
	}
	
	public InputKey(int device, int keycode){
		this.device = device;
		this.keycode = keycode;
	}
	
	public boolean isEmpty(){
		return device == -1 || keycode == -1;
	}

	public int getDevice(){
		return device;
	}

	public int getKeycode(){
		return keycode;
	}

	public void setDevice(int device){
		this.device = device;
	}

	public void setKeycode(int keycode){
		this.keycode = keycode;
	}
}
