package net.medox.neonengine.math;

public class Matrix4f{
	private float[][] m;
	
	public Matrix4f(){
		m = new float[4][4];
	}
	
	public Matrix4f initIdentity(){
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initTranslation(float x, float y, float z){
		m[0][0] = 1;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = x;
		m[1][0] = 0;	m[1][1] = 1;	m[1][2] = 0;	m[1][3] = y;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = 1;	m[2][3] = z;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(float x, float y, float z){
		final Matrix4f rx = new Matrix4f();
		final Matrix4f ry = new Matrix4f();
		final Matrix4f rz = new Matrix4f();
		
		final float xNew = (float)Math.toRadians(x);
		final float yNew = (float)Math.toRadians(y);
		final float zNew = (float)Math.toRadians(z);
		
		rz.m[0][0] = (float)Math.cos(zNew);rz.m[0][1] = -(float)Math.sin(zNew);	rz.m[0][2] = 0;						rz.m[0][3] = 0;
		rz.m[1][0] = (float)Math.sin(zNew);rz.m[1][1] = (float)Math.cos(zNew);	rz.m[1][2] = 0;						rz.m[1][3] = 0;
		rz.m[2][0] = 0;					   rz.m[2][1] = 0;						rz.m[2][2] = 1;						rz.m[2][3] = 0;
		rz.m[3][0] = 0;					   rz.m[3][1] = 0;						rz.m[3][2] = 0;						rz.m[3][3] = 1;
		
		rx.m[0][0] = 1;					   rx.m[0][1] = 0;						rx.m[0][2] = 0;						rx.m[0][3] = 0;
		rx.m[1][0] = 0;					   rx.m[1][1] = (float)Math.cos(xNew);	rx.m[1][2] = -(float)Math.sin(xNew);rx.m[1][3] = 0;
		rx.m[2][0] = 0;					   rx.m[2][1] = (float)Math.sin(xNew);	rx.m[2][2] = (float)Math.cos(xNew);	rx.m[2][3] = 0;
		rx.m[3][0] = 0;					   rx.m[3][1] = 0;						rx.m[3][2] = 0;						rx.m[3][3] = 1;
		
		ry.m[0][0] = (float)Math.cos(yNew);ry.m[0][1] = 0;						ry.m[0][2] = -(float)Math.sin(yNew);ry.m[0][3] = 0;
		ry.m[1][0] = 0;					   ry.m[1][1] = 1;						ry.m[1][2] = 0;						ry.m[1][3] = 0;
		ry.m[2][0] = (float)Math.sin(yNew);ry.m[2][1] = 0;						ry.m[2][2] = (float)Math.cos(yNew);	ry.m[2][3] = 0;
		ry.m[3][0] = 0;					   ry.m[3][1] = 0;						ry.m[3][2] = 0;						ry.m[3][3] = 1;
		
		m = rz.mul(ry.mul(rx)).getM();

		return this;
	}
	
	public Matrix4f initScale(float x, float y, float z){
		m[0][0] = x;	m[0][1] = 0;	m[0][2] = 0;	m[0][3] = 0;
		m[1][0] = 0;	m[1][1] = y;	m[1][2] = 0;	m[1][3] = 0;
		m[2][0] = 0;	m[2][1] = 0;	m[2][2] = z;	m[2][3] = 0;
		m[3][0] = 0;	m[3][1] = 0;	m[3][2] = 0;	m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initPerspective(float fov, float aspectRatio, float zNear, float zFar){
		final float tanHalfFOV = (float)Math.tan(fov / 2);
		final float zRange = zNear - zFar;
		
		m[0][0] = 1.0f / (tanHalfFOV * aspectRatio);m[0][1] = 0;				m[0][2] = 0;					m[0][3] = 0;
		m[1][0] = 0;								m[1][1] = 1.0f / tanHalfFOV;m[1][2] = 0;					m[1][3] = 0;
		m[2][0] = 0;								m[2][1] = 0;				m[2][2] = (-zNear -zFar)/zRange;m[2][3] = 2 * zFar * zNear / zRange;
		m[3][0] = 0;								m[3][1] = 0;				m[3][2] = 1;					m[3][3] = 0;
		
		return this;
	}
	
	public Matrix4f initOrthographic(float left, float right, float bottom, float top, float near, float far){
		final float width = right - left;
		final float height = top - bottom;
		final float depth = far - near;
		
		m[0][0] = 2/width;	m[0][1] = 0;		m[0][2] = 0;		m[0][3] = -(right + left)/width;
		m[1][0] = 0;		m[1][1] = 2/height;	m[1][2] = 0;		m[1][3] = -(top + bottom)/height;
		m[2][0] = 0;		m[2][1] = 0;		m[2][2] = -2/depth;	m[2][3] = -(far + near)/depth;
		m[3][0] = 0;		m[3][1] = 0;		m[3][2] = 0;		m[3][3] = 1;
		
		return this;
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up){
		final Vector3f f = forward.normalized();
		
		Vector3f r = up.normalized();
		r = r.cross(f);
		
		return initRotation(f, f.cross(r), r);
	}
	
	public Matrix4f initRotation(Vector3f forward, Vector3f up, Vector3f right){
		m[0][0] = right.getX();		m[0][1] = right.getY();		m[0][2] = right.getZ();		m[0][3] = 0;
		m[1][0] = up.getX();		m[1][1] = up.getY();		m[1][2] = up.getZ();		m[1][3] = 0;
		m[2][0] = forward.getX();	m[2][1] = forward.getY();	m[2][2] = forward.getZ();	m[2][3] = 0;
		m[3][0] = 0;				m[3][1] = 0;				m[3][2] = 0;				m[3][3] = 1;
		
		return this;
	}
	
	public Vector3f transform(Vector3f v){
		return new Vector3f(m[0][0] * v.getX() + m[0][1] * v.getY() + m[0][2] * v.getZ() + m[0][3], 
							m[1][0] * v.getX() + m[1][1] * v.getY() + m[1][2] * v.getZ() + m[1][3],
							m[2][0] * v.getX() + m[2][1] * v.getY() + m[2][2] * v.getZ() + m[2][3]);
	}
	
	public Matrix4f mul(Matrix4f m){
		final Matrix4f res = new Matrix4f();
		
		for(int i = 0; i < 4; i ++){
			for(int j = 0; j < 4; j ++){
				res.set(i, j, this.m[i][0] * m.get(0, j) + 
								this.m[i][1] * m.get(1, j) +
								this.m[i][2] * m.get(2, j) +
								this.m[i][3] * m.get(3, j));
			}
		}
		
		return res;
	}

	public float[][] getM(){
		float[][] res = new float[4][4];
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				res[i][j] = m[i][j];
			}
		}
		
		return res;
	}
	
//	public float[] getMList(){
//		float[] res = new float[16];
//
//		for(int j = 0; j < 4; j++){
//			for(int i = 0; i < 4; i++){
//				res[i+j] = m[i][j];
//			}
//		}
//
//		return res;
//	}

	public float get(int x, int y){
		return m[x][y];
	}

	public void setM(float[][] m){
		this.m = m;
	}

	public void set(int x, int y, float value){
		m[x][y] = value;
	}
	
	@Override
	public String toString(){
		return "{(" + m[0][0] + ", " + m[0][1] + ", " + m[0][2] + ", " + m[0][3] + "), " +
			   "(" + m[1][0] + ", " + m[1][1] + ", " + m[1][2] + ", " + m[1][3] + "), " +
			   "(" + m[2][0] + ", " + m[2][1] + ", " + m[2][2] + ", " + m[2][3] + "), " +
			   "(" + m[3][0] + ", " + m[3][1] + ", " + m[3][2] + ", " + m[3][3] + ")}";
	}
}
