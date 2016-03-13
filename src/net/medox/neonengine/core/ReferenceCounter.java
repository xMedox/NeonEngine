package net.medox.neonengine.core;

public class ReferenceCounter{
	private int refCount;
	
	public ReferenceCounter(){
		refCount = 1;
	}
	
	public int getReferenceCount(){
		return refCount;
	}
	
	public void addReference(){
		refCount++;
	}
	
	public boolean removeReference(){
		refCount--;
		return refCount == 0;
	}
}
