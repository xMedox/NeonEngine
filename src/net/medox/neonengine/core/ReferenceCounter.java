package net.medox.neonengine.core;

public class ReferenceCounter{
	private int referenceCount;
	
	public ReferenceCounter(){
		referenceCount = 1;
	}
	
	public int getReferenceCount(){
		return referenceCount;
	}
	
	public void addReference(){
		referenceCount++;
	}
	
	public boolean removeReference(){
		referenceCount--;
		return referenceCount <= 0;
	}
}
