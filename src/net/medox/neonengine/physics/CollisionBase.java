package net.medox.neonengine.physics;

import java.util.ArrayList;
import java.util.List;

public class CollisionBase{
	public static final int COLLIDER = 0;
	public static final int SOFTBODY = 1;
	
	private final List<CollisionBase> hitList;
	
	private int type;
	
	private int group;
	private Object object;
	
	public CollisionBase(int type){
		this.type = type;
		
		hitList = new ArrayList<CollisionBase>();
	}
	
	public int getType(){
		return type;
	}
	
	public int getGroup(){
		return group;
	}
	
	public void setGroup(int group){
		this.group = group;
	}
	
	public Object getObject(){
		return object;
	}
	
	public void setObject(Object object){
		this.object = object;
	}
	
	public void add(CollisionBase c){
		hitList.add(c);
	}
	
	public void clearList(){
		hitList.clear();
	}
	
	public boolean collidesWith(CollisionBase c){
		return hitList.contains(c);
	}
}
