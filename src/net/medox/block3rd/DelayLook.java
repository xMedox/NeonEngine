package net.medox.block3rd;

import net.medox.neonengine.components.LookComponent;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class DelayLook extends EntityComponent{
	private static Quaternion quaternion = new Quaternion(new Vector3f(0, 0, 0), 0);
	
	private LookComponent lock;
	
	public static final Vector3f yAxis = new Vector3f(0, 1, 0);
	
	private float x;
	private float x2;
	private float x3;
	private float x4;
	
	private float y;
	private float y2;
	private float y3;
	private float y4;
	
	private float div;
	
	private boolean invert;
	
	public DelayLook(LookComponent lock){
		this(lock, 40);
	}
	
	public DelayLook(LookComponent lock, float div){
		this(lock, div, false);
	}
	
	public DelayLook(LookComponent lock, boolean invert){
		this(lock, 40, invert);
	}
	
	public DelayLook(LookComponent lock, float div, boolean invert){
		this.lock = lock;
		this.div = div;
		this.invert = invert;
	}
	
	@Override
	public void update(float delta){
		getTransform().setRot(quaternion);
		
		if(invert){
			getTransform().rotate(yAxis, -y/div);
		}else{
			getTransform().rotate(yAxis, y/div);
		}
		
		y = (y+y2)/2;
		y2 = (y2+y3)/2;
		y3 = (y3+y4)/2;
		y4 = (y4+lock.getY()*(0.016666668f/delta))/2;
		
		if(invert){
			getTransform().rotate(getTransform().getRot().getRight(), -x/div);
		}else{
			getTransform().rotate(getTransform().getRot().getRight(), x/div);
		}
		
		x = (x+x2)/2;
		x2 = (x2+x3)/2;
		x3 = (x3+x4)/2;
		x4 = (x4+lock.getX()*(0.016666668f/delta))/2;
	}
}

