package net.medox.neonengine.rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import net.medox.neonengine.math.Matrix4f;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;

import org.lwjgl.BufferUtils;

public class DataUtil{
	public static FloatBuffer createFloatBuffer(int size){
		return BufferUtils.createFloatBuffer(size);
	}
	
	public static IntBuffer createIntBuffer(int size){
		return BufferUtils.createIntBuffer(size);
	}
	
	public static ByteBuffer createByteBuffer(int size){
		return BufferUtils.createByteBuffer(size);
	}
	
	public static IntBuffer createFlippedBuffer(int... values){
		final IntBuffer buffer = createIntBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(float... values){
		final FloatBuffer buffer = createFloatBuffer(values.length);
		buffer.put(values);
		buffer.flip();
		
		return buffer;
	}
	
//	public static IntBuffer createFlippedBufferInt(Integer[] vertices){
//		IntBuffer buffer = createIntBuffer(vertices.length);
//
//		for(int i = 0; i < vertices.length; i++){
//			buffer.put(vertices[i]);
//		}
//		
//		buffer.flip();
//		
//		return buffer;
//	}
	
	public static IntBuffer createFlippedBufferTimes3(Integer... vertices){
		final IntBuffer buffer = createIntBuffer(vertices.length * 4);
		
		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i]);
			
			buffer.put(vertices[i]);
			
			buffer.put(vertices[i]);
			
			buffer.put(vertices[i]);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Float... vertices){
		final FloatBuffer buffer = createFloatBuffer(vertices.length);

		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i]);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector3f... vertices){
		final FloatBuffer buffer = createFloatBuffer(vertices.length * 3);

		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
			buffer.put(vertices[i].getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBufferTimes3(Vector3f... vertices){
		final FloatBuffer buffer = createFloatBuffer(vertices.length * 3 * 4);

		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
			buffer.put(vertices[i].getZ());
			
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
			buffer.put(vertices[i].getZ());
			
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
			buffer.put(vertices[i].getZ());
			
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
			buffer.put(vertices[i].getZ());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Vector2f... vertices){
		final FloatBuffer buffer = createFloatBuffer(vertices.length * 2);

		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i].getX());
			buffer.put(vertices[i].getY());
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static IntBuffer createFlippedBuffer(Integer... vertices){
		final IntBuffer buffer = createIntBuffer(vertices.length);

		for(int i = 0; i < vertices.length; i++){
			buffer.put(vertices[i]);
		}
		
		buffer.flip();
		
		return buffer;
	}
	
	public static FloatBuffer createFlippedBuffer(Matrix4f value){
		final FloatBuffer buffer = createFloatBuffer(4 * 4);
		
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 4; j++){
				buffer.put(value.get(i, j));
			}
		}
		
		buffer.flip();
		
		return buffer;
	}
}
