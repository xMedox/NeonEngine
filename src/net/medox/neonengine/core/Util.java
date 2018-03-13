package net.medox.neonengine.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.CameraBase;
import net.medox.neonengine.rendering.RenderingEngine;
import net.medox.neonengine.rendering.Window;

public class Util{
	private static Random random;
	
	private static Key key;
	private static Cipher cipher;
	
	public static void init(){
		random = new Random();
		
		key = new SecretKeySpec("JRAgZaRWQReyehIY".getBytes(), "AES");
		
		try{
			cipher = Cipher.getInstance("AES");
		}catch(NoSuchAlgorithmException | NoSuchPaddingException e){
			NeonEngine.throwError("Error: failed to create the cipher.");
		}
	}
	
	public static String encrypt(String text){
		try{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
//			return new String(cipher.doFinal(text.getBytes()));
		}catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
			NeonEngine.throwError("Error: failed to encrypt the string.");
		}
		
		return null;
	}
	
	public static String decrypt(String text){
		try{
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			return new String(cipher.doFinal(Base64.getDecoder().decode(text.getBytes())));
//			return new String(cipher.doFinal(text.getBytes()));
		}catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
			NeonEngine.throwError("Error: failed to decrypt the string.");
		}
		
		return null;
	}
	
	public static float randomFloat(){
		return random.nextFloat();
	}
	
	public static boolean randomBoolean(){
		return random.nextBoolean();
	}
	
	public static int randomInt(){
		return random.nextInt();
	}
	
	public static int randomInt(int min, int max){
	    return random.nextInt((max - min) + 1) + min;
	}
	
//	public static float toRadians(float value){
//		return (float)Math.toRadians(value);
//	}
//	
//	public static float toDegrees(float value){
//		return (float)Math.toDegrees(value);
//	}
	
	public static boolean doesFileExist(String filePath){
		final File file = new File(filePath);
		
		return file.exists() && !file.isDirectory();
	}
	
	public static boolean doesDirectoryExist(String filePath){
		final File file = new File(filePath);
		
		return file.exists() && file.isDirectory();
	}
	
	public static void saveToFile(String filePath, String text){
		try{
			final BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
	    	
	    	out.write(text);
			
	    	out.close();
		}catch(IOException e){
			NeonEngine.throwError("Error: unable to save " + filePath);
		}
	}
	
	public static void createDirectory(String filePath){
		new File(filePath).mkdirs();
	}
	
	public static List<String> loadFromFile(String filePath){
		final List<String> output = new ArrayList<String>();
		
		try{
			final BufferedReader br = new BufferedReader(new FileReader(filePath));
			
			String x;
            while((x = br.readLine()) != null){
                output.add(x);
            } 
			
			br.close();
		}catch(IOException e){
			NeonEngine.throwError("Error: unable to read " + filePath);
		}
		
		return output;
	}
	
	public static String[] removeEmptyStrings(String... data){
		final ArrayList<String> result = new ArrayList<String>();
		
		for(final String filtering : data){
			if(!filtering.isEmpty()){
				result.add(filtering);
			}
		}
		
		return result.toArray(new String[result.size()]);
	}
	
//	public static int[] toIntArray(Integer... data){
//		int[] result = new int[data.length];
//		
//		for(int i = 0; i < data.length; i++){
//			result[i] = data[i];
//		}
//		
//		return result;
//	}
	
	public static float clamp(float value, float min, float max){
		return value < min ? min : value > max ? max : value;
	}
	
	public static Vector3f mouseToRay(){
		Vector3f result = new Vector3f(0, 0, 0);
		
		final Camera camera = RenderingEngine.getMainCamera();
		
		if(camera.getMode() == CameraBase.PERSPECTIVE_MODE){
			final float vLength = (float)Math.tan(camera.getFov() / 2) * camera.getZNear();
			final float hLength = vLength * ((float)Window.getWidth() / (float)Window.getHeight());
			
			final Vector3f up = camera.getTransform().getTransformedRot().getUp().mul(vLength);
			final Vector3f right = camera.getTransform().getTransformedRot().getRight().mul(hLength);
			
			final float mouseX = (Input.getMousePosition().getX() - ((float)Window.getWidth() / 2)) / ((float)Window.getWidth() / 2);
			final float mouseY = (Input.getMousePosition().getY() - ((float)Window.getHeight() / 2)) / ((float)Window.getHeight() / 2);
			
			final Vector3f pos = camera.getTransform().getTransformedPos().add(camera.getTransform().getTransformedRot().getForward().mul(camera.getZNear())).add(right.mul(mouseX)).add(up.mul(mouseY));
			
			result = pos.sub(camera.getTransform().getTransformedPos()).normalized();
		}
		
		return result;
	}
	
	private static float interpolate(float x0, float x1, float alpha){
		return x0 * (1 - alpha) + alpha * x1;
	}
	
	public static float[][] generateWhiteNoise(int width, int height){
		final float[][] noise = new float[width][height];
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				noise[x][y] = randomFloat();
			}
		}
		return noise;
	}
	
	public static float[][] generateSmoothNoise(int width, int height, int octave){
		final float[][] baseNoise = generateWhiteNoise(width, height);
		
		final float[][] smoothNoise = new float[width][height];
		
		final int samplePeriod = 1 << octave;
		final float sampleFrequency = 1.0f / samplePeriod;
		for(int i = 0; i < width; i++){
			final int sampleI0 = (i / samplePeriod) * samplePeriod;
			final int sampleI1 = (sampleI0 + samplePeriod) % width;
			final float horizontalBlend = (i - sampleI0) * sampleFrequency;
			
			for(int j = 0; j < height; j++){
				final int sampleJ0 = (j / samplePeriod) * samplePeriod;
				final int sampleJ1 = (sampleJ0 + samplePeriod) % height;
				final float verticalBlend = (j - sampleJ0) * sampleFrequency;
				final float top = interpolate(baseNoise[sampleI0][sampleJ0], baseNoise[sampleI1][sampleJ0], horizontalBlend);
				final float bottom = interpolate(baseNoise[sampleI0][sampleJ1], baseNoise[sampleI1][sampleJ1], horizontalBlend);
				smoothNoise[i][j] = interpolate(top, bottom, verticalBlend);
			}
		}
		
		return smoothNoise;
	}
	
	public static float[][] generatePerlinNoise(int width, int height, int octaveCount){
		final float[][][] smoothNoise = new float[octaveCount][][];
		final float persistance = 0.7f;
		
		for(int i = 0; i < octaveCount; i++){
			smoothNoise[i] = generateSmoothNoise(width, height, i);
		}
		
		final float[][] perlinNoise = new float[width][height];
		
		float amplitude = 1.0f;
		float totalAmplitude = 0.0f;
		
		for(int octave = octaveCount - 1; octave >= 0; octave--){
			amplitude *= persistance;
			totalAmplitude += amplitude;
			
			for(int i = 0; i < width; i++){
				for(int j = 0; j < height; j++){
					perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
				}
			}
		}
		
		for(int i = 0; i < width; i++){
			for(int j = 0; j < height; j++){
				perlinNoise[i][j] /= totalAmplitude;
			}
		}
		
		return perlinNoise;
	}
	
	public static float lerp(float a, float b, float f){
	    return a + f * (b - a);
	}
}
