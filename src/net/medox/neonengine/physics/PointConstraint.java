package net.medox.neonengine.physics;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.dynamics.btPoint2PointConstraint;

public class PointConstraint extends Constraint{
	public PointConstraint(Collider cA, net.medox.neonengine.math.Vector3f pivotInA){
//		super();
//		pointConstraint = new Point2PointConstraint(cA.getBody(), cB.getBody(), new Vector3f(pivotInA.getX(), pivotInA.getY(), pivotInA.getZ()), new Vector3f(pivotInB.getX(), pivotInB.getY(), pivotInB.getZ()));
		
		setConstraint(new btPoint2PointConstraint(cA.getBody(), new Vector3(pivotInA.getX(), pivotInA.getY(), pivotInA.getZ())));
	}
	
	public PointConstraint(Collider cA, Collider cB, net.medox.neonengine.math.Vector3f pivotInA, net.medox.neonengine.math.Vector3f pivotInB){
//		super();
//		pointConstraint = new Point2PointConstraint(cA.getBody(), cB.getBody(), new Vector3f(pivotInA.getX(), pivotInA.getY(), pivotInA.getZ()), new Vector3f(pivotInB.getX(), pivotInB.getY(), pivotInB.getZ()));
		
		setConstraint(new btPoint2PointConstraint(cA.getBody(), cB.getBody(), new Vector3(pivotInA.getX(), pivotInA.getY(), pivotInA.getZ()), new Vector3(pivotInB.getX(), pivotInB.getY(), pivotInB.getZ())));
	}
	
//	@Override
//	public TypedConstraint getPointConstraint(){
//		return pointConstraint;
//	}
	
//	public void remove(){
//		PhysicsEngine.removeConstraint(this);
//	}
}
