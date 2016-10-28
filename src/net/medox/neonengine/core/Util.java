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
			e.printStackTrace();
		}
	}
	
	public static String encrypt(String text){
		try{
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
			return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes()));
//			return new String(cipher.doFinal(text.getBytes()));
		}catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String decrypt(String text){
		try{
			cipher.init(Cipher.DECRYPT_MODE, key);
			
			return new String(cipher.doFinal(Base64.getDecoder().decode(text.getBytes())));
//			return new String(cipher.doFinal(text.getBytes()));
		}catch(InvalidKeyException | IllegalBlockSizeException | BadPaddingException e){
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
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
	
	public static int[] toIntArray(Integer... data){
		int[] result = new int[data.length];
		
		for(int i = 0; i < data.length; i++){
			result[i] = data[i];
		}
		
		return result;
	}
	
	public static float clamp(float value, float min, float max){
		return value < min ? min : value > max ? max : value;
	}
	
	public static Vector3f mouseToRay(){
		Vector3f result = new Vector3f();
		
		Camera camera = RenderingEngine.getMainCamera();
		
		if(camera.getMode() == 0){
			float vLength = (float)Math.tan(camera.getFov() / 2) * camera.getZNear();
			float hLength = vLength * ((float)Window.getWidth() / (float)Window.getHeight());
			
			Vector3f up = camera.getTransform().getTransformedRot().getUp().mul(vLength);
			Vector3f right = camera.getTransform().getTransformedRot().getRight().mul(hLength);
			
			float mouseX = Input.getMousePosition().getX();
			float mouseY = Input.getMousePosition().getY();
			
			mouseX -= ((float)Window.getWidth() / 2);
			mouseY -= ((float)Window.getHeight() / 2);
			
			mouseX /= ((float)Window.getWidth() / 2);
			mouseY /= ((float)Window.getHeight() / 2);
			
			Vector3f pos = camera.getTransform().getTransformedPos().add(camera.getTransform().getTransformedRot().getForward().mul(camera.getZNear())).add(right.mul(mouseX)).add(up.mul(mouseY));
			
			result = pos.sub(camera.getTransform().getTransformedPos());
			
			result = result.normalized();
		}
		
		return result;
	}
}
