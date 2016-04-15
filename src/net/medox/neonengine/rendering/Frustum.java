package net.medox.neonengine.rendering;

import net.medox.neonengine.math.Vector3f;

public class Frustum{
	private final Plane[] pl;
	
	public Frustum(Camera camera){		
		final Vector3f p = camera.getTransform().getTransformedPos();
		
		final Vector3f z = camera.getTransform().getTransformedRot().getForward();
		final Vector3f x = camera.getTransform().getTransformedRot().getRight();
		final Vector3f y = camera.getTransform().getTransformedRot().getUp();
		
		 if(camera.getFov() == -1){
			final float near = camera.getNear();
			final float far = camera.getFar();
			
			final float height = camera.getTop() - camera.getBottom();
			final float width = camera.getRight() - camera.getLeft();
			
			final Vector3f nearClipping = p.add(z.mul((near - far)*2)); //add offset to remove clipping bug
			final Vector3f farClipping = p.add(z.mul((far - near)*2)); //add offset to remove clipping bug
			
			
			final Vector3f n1 = y.mul(height);
			final Vector3f n2 = x.mul(width);
			
			final Vector3f f1 = y.mul(height);
			final Vector3f f2 = x.mul(width);
			
			final Vector3f ntl = nearClipping.add(n1).sub(n2);
			final Vector3f ntr = nearClipping.add(n1).add(n2);
			final Vector3f nbl = nearClipping.sub(n1).sub(n2);
			final Vector3f nbr = nearClipping.sub(n1).add(n2);
			
			final Vector3f ftl = farClipping.add(f1).sub(f2);
			final Vector3f ftr = farClipping.add(f1).add(f2);
			final Vector3f fbl = farClipping.sub(f1).sub(f2);
			final Vector3f fbr = farClipping.sub(f1).add(f2);
			
			pl = new Plane[]{new Plane(ntr, ntl, ftl), new Plane(nbl, nbr, fbr), new Plane(ntl, nbl, fbl), new Plane(nbr, ntr, fbr), new Plane(ntl, ntr, nbr), new Plane(ftr, ftl, fbl)};
		}else{
			final float zNear = camera.getZNear();
			final float zFar = camera.getZFar();
			final float ratio = camera.getAspectRatio();
			
			final float tang = (float)Math.tan(camera.getFov()/* * (float)Math.PI/180.0*/ * 0.5f);
			
			final float nearHeight = zNear * tang;
			final float farHeight = zFar * tang;
			
			final Vector3f nearClipping = p.add(z.mul(zNear));
			final Vector3f farClipping = p.add(z.mul(zFar));
			
			
			final Vector3f n1 = y.mul(nearHeight);
			final Vector3f n2 = x.mul(nearHeight * ratio);
			
			final Vector3f f1 = y.mul(farHeight);
			final Vector3f f2 = x.mul(farHeight * ratio);
			
			final Vector3f ntl = nearClipping.add(n1).sub(n2);
			final Vector3f ntr = nearClipping.add(n1).add(n2);
			final Vector3f nbl = nearClipping.sub(n1).sub(n2);
			final Vector3f nbr = nearClipping.sub(n1).add(n2);
			
			final Vector3f ftl = farClipping.add(f1).sub(f2);
			final Vector3f ftr = farClipping.add(f1).add(f2);
			final Vector3f fbl = farClipping.sub(f1).sub(f2);
			final Vector3f fbr = farClipping.sub(f1).add(f2);
			
			pl = new Plane[]{new Plane(ntr, ntl, ftl), new Plane(nbl, nbr, fbr), new Plane(ntl, nbl, fbl), new Plane(nbr, ntr, fbr), new Plane(ntl, ntr, nbr), new Plane(ftr, ftl, fbl)};
		}
		
//		pl[0] = new Plane(ntr, ntl, ftl);
//		pl[1] = new Plane(nbl, nbr, fbr);
//		pl[2] = new Plane(ntl, nbl, fbl);
//		pl[3] = new Plane(nbr, ntr, fbr);
//		pl[4] = new Plane(ntl, ntr, nbr);
//		pl[5] = new Plane(ftr, ftl, fbl);
		
//		pl[NEARP] = new Plane(z.mul(-1),nc);
//		pl[FARP] = new Plane(z,fc);
//
//		Vector3f aux,normal;
//
////		aux = (nc + y*nh) - p;
//		aux = (nc.add(y.mul(nh))).sub(p);
//		aux = aux.normalized();
//		normal = aux.mul(x);
////		pl[TOP] = new Plane(normal,nc+y*nh);
//		pl[TOP] = new Plane(normal,nc.add(y.mul(nh)));
//
////		aux = (nc - y*nh) - p;
//		aux = (nc.sub(y.mul(nh))).sub(p);
//		aux = aux.normalized();
//		normal = x.mul(aux);
////		pl[BOTTOM] = new Plane(normal,nc-y*nh);
//		pl[BOTTOM] = new Plane(normal,nc.sub(y.mul(nh)));
//		
////		aux = (nc - x*nw) - p;
//		aux = (nc.sub(x.mul(nw))).sub(p);
//		aux = aux.normalized();
//		normal = aux.mul(y);
////		pl[LEFT] = new Plane(normal,nc-x*nw);
//		pl[LEFT] = new Plane(normal,nc.sub(x.mul(nw)));
//
////		aux = (nc + x*nw) - p;
//		aux = (nc.add(x.mul(nw))).sub(p);
//		aux = aux.normalized();
//		normal = y.mul(aux);
////		pl[RIGHT] = new Plane(normal,nc+x*nw);
//		pl[RIGHT] = new Plane(normal,nc.add(x.mul(nw)));
	}
	
	public boolean sphereInFrustum(Vector3f p, float raio){
		for(int i = 0; i < 6; i++){
			if(pl[i].distance(p) < -raio){
				return false;
			}
		}
		
		return true;
	}
	
	public boolean boxInFrustum(Vector3f min, Vector3f max){
	    for(int i = 0; i < 6; i++){
	        int in = 0;
	        
	        if(pl[i].distance(new Vector3f(min.getX(), min.getY(), min.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(max.getX(), min.getY(), min.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(min.getX(), min.getY(), max.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(max.getX(), min.getY(), max.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(min.getX(), max.getY(), min.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(max.getX(), max.getY(), min.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(min.getX(), max.getY(), max.getZ())) < 0){
	            in++;
	        }
	        
	        if(pl[i].distance(new Vector3f(max.getX(), max.getY(), max.getZ())) < 0){
	            in++;
	        }
	        
	        if(in == 8){
	        	return false;
	        }
	    }

	    return true;
	 }
	
	public boolean pointInFrustum(Vector3f p){
		for(int i = 0; i < 6; i++){
			if(pl[i].distance(p) < 0){
				return false;
			}
		}
		return true;
	}
}

class Plane{
	private final Vector3f normal;
	private final float d;
	
	public Plane(Vector3f v1, Vector3f v2, Vector3f v3){
//		Vector3f aux1 = v1.sub(v2);
//		Vector3f aux2 = v2.sub(v3);
//		
//		normal = aux2.cross(aux1);
//		
//		normal = normal.normalized();
//		d = -(normal.dot(v2));
		
//		Vector3f aux1 = v1.sub(v3);
//		Vector3f aux2 = v2.sub(v1);
		
		normal = /*aux2*/v2.sub(v1).cross(/*aux1*/v1.sub(v3)).normalized();
		
//		normal = normal.normalized();
		d = -(normal.dot(v2));
	}
	
//	public Plane(Vector3f normal, Vector3f point){
//		this.normal = normal;
//		this.normal = this.normal.normalized();
//		d = -(this.normal.dot(point));
//	}
	
	public float distance(Vector3f p){
		return d + normal.dot(p);
	}
}

