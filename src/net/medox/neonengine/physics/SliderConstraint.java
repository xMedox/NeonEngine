package net.medox.neonengine.physics;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.linearmath.Transform;

public class SliderConstraint extends Constraint{
	public SliderConstraint(Collider cA, Collider cB, net.medox.neonengine.core.Transform frameInA, net.medox.neonengine.core.Transform frameInB, boolean useLinearReferenceFrameA){
//		super();
		
//		pointConstraint = new Point2PointConstraint(cA.getBody(), cB.getBody(), new Vector3f(pivotInA.getX(), pivotInA.getY(), pivotInA.getZ()), new Vector3f(pivotInB.getX(), pivotInB.getY(), pivotInB.getZ()));
		
		final net.medox.neonengine.math.Vector3f posA = frameInA.getPos();
		final net.medox.neonengine.math.Quaternion rotA = frameInA.getRot();
//		net.medox.neonengine.core.Vector3f scaleA = frameInA.getScale();
		
//		Transform resultA = new Transform(new Transform(new Matrix4f(new Quat4f(rotA.getX(), rotA.getY(), rotA.getZ(), rotA.getW()), new Vector3f(posA.getX(), posA.getY(), posA.getZ()), 1.0f)));
		
		
		final net.medox.neonengine.math.Vector3f posB = frameInB.getPos();
		final net.medox.neonengine.math.Quaternion rotB = frameInB.getRot();
//		net.medox.neonengine.core.Vector3f scaleB = frameInB.getScale();
		
//		Transform resultB = new Transform(new Transform(new Matrix4f(new Quat4f(rotB.getX(), rotB.getY(), rotB.getZ(), rotB.getW()), new Vector3f(posB.getX(), posB.getY(), posB.getZ()), 1.0f)));
		
//		setConstraint(new com.bulletphysics.dynamics.constraintsolver.SliderConstraint(cA.getBody(), cB.getBody(), resultA, resultB, useLinearReferenceFrameA));
		setConstraint(new com.bulletphysics.dynamics.constraintsolver.SliderConstraint(cA.getBody(), cB.getBody(), new Transform(new Transform(new Matrix4f(new Quat4f(rotA.getX(), rotA.getY(), rotA.getZ(), rotA.getW()), new Vector3f(posA.getX(), posA.getY(), posA.getZ()), 1.0f))), new Transform(new Transform(new Matrix4f(new Quat4f(rotB.getX(), rotB.getY(), rotB.getZ(), rotB.getW()), new Vector3f(posB.getX(), posB.getY(), posB.getZ()), 1.0f))), useLinearReferenceFrameA));
	}
	
//	@Override
//	public TypedConstraint getPointConstraint(){
//		return pointConstraint;
//	}
	
//	public void remove(){
//		PhysicsEngine.removeConstraint(this);
//	}
}
