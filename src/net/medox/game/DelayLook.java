package net.medox.game;

import java.util.ArrayList;

import net.medox.neonengine.components.FreeLook;
import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.math.Quaternion;
import net.medox.neonengine.math.Vector3f;

public class DelayLook extends EntityComponent{
	private static Quaternion quaternion = new Quaternion(new Vector3f(0, 0, 0), 0);
	
	private FreeLook look;
	private ArrayList<Quaternion> quaternions;
	
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
	
	public DelayLook(FreeLook look){
		this(look, null);
	}
	
	public DelayLook(FreeLook look, float div){
		this(look, null, div);
	}
	
	public DelayLook(FreeLook look, ArrayList<Quaternion> quaternions){
		this(look, quaternions, 40);
	}
	
	public DelayLook(FreeLook look, ArrayList<Quaternion> quaternions, float div){
		this(look, quaternions, div, false);
	}
	
	public DelayLook(FreeLook look, ArrayList<Quaternion> quaternions, boolean invert){
		this(look, quaternions, 40, invert);
	}
	
	public DelayLook(FreeLook look, ArrayList<Quaternion> quaternions, float div, boolean invert){
		this.look = look;
		this.quaternions = quaternions;
		this.div = div;
		this.invert = invert;
	}
	
	@Override
	public void update(float delta){
		getTransform().setRot(quaternion);
		
		if(quaternions != null){
			for(int i = 0; i < quaternions.toArray().length; i ++){
				getTransform().rotate(quaternions.get(i));
			}
		}
//		getTransform().setRot(new Quaternion(new Vector3f(0, 1, 0), (float)Math.toRadians(180)));
//		getTransform().setRot(new Quaternion(new Vector3f(0, 0, 0), 0));
		
		if(invert){
			getTransform().rotate(yAxis, -y/div);
		}else{
			getTransform().rotate(yAxis, y/div);
		}
		
		y = (y+y2)/2;
		y2 = (y2+y3)/2;
		y3 = (y3+y4)/2;
		
		if(look != null){
			y4 = (y4+look.getY())/2;
		}
		
		
		if(invert){
			getTransform().rotate(getTransform().getRot().getRight(), -x/div);
		}else{
			getTransform().rotate(getTransform().getRot().getRight(), x/div);
		}
		
		x = (x+x2)/2;
		x2 = (x2+x3)/2;
		x3 = (x3+x4)/2;
		
		if(look != null){
			x4 = (x4+look.getX())/2;
		}
	}
}

