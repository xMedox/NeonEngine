package net.medox.neonengine.core;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import javax.imageio.ImageIO;

import net.medox.neonengine.rendering.DataUtil;
import net.medox.neonengine.rendering.Texture;

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
			
			return Base64.getEncoder().encodeToString((cipher.doFinal(text.getBytes())));
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
	
	public static boolean fileExists(String filePath){
		final File file = new File(filePath);
		
		return file.exists() && !file.isDirectory();
	}
	
	public static boolean directoryExists(String filePath){
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
	
	public static void createDiretory(String filePath){
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
		final List<String> result = new ArrayList<String>();
		
		for(int i = 0; i < data.length; i++){
			if(!data[i].equals("")){
				result.add(data[i]);
			}
		}
		
		final String[] res = new String[result.size()];
		result.toArray(res);
		
		return res;
	}
	
	public static int[] toIntArray(Integer... data){
		int[] result = new int[data.length];
		
		for(int i = 0; i < data.length; i++){
			result[i] = data[i].intValue();
		}
		
		return result;
	}
	
	public static BufferedImage createImage(int[][] red, int[][] green, int[][] blue, int width, int height){
		final BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	    
		final WritableRaster raster = ret.getRaster();
		
	    for(int i = 0; i < height; i++){
	        for(int j = 0; j < width; j++){
                raster.setSample(j, i, 0, red[i][j]);
                raster.setSample(j, i, 1, green[i][j]);
                raster.setSample(j, i, 2, blue[i][j]);
	        }
	    }
	    
	    return ret;
	}
	
	public static Texture createWhite(){
		return new Texture(createImage(new int[][]{{255, 255}, {255, 255}}, new int[][]{{255, 255}, {255, 255}}, new int[][]{{255, 255}, {255, 255}}, 2, 2));
	}
	
	public static Texture createDefaultDiffuseMap(){
		return new Texture(createImage(new int[][]{{255, 0}, {0, 255}}, new int[][]{{0, 0}, {0, 0}}, new int[][]{{0, 255}, {255, 0}}, 2, 2));
	}
	
	public static Texture createDefaultNormalMap(){
		return new Texture(createImage(new int[][]{{128, 128}, {128, 128}}, new int[][]{{128, 128}, {128, 128}}, new int[][]{{255, 255}, {255, 255}}, 2, 2));
	}
	
//	public static Texture createDefaultDisplacementMap(){	
//		return new Texture(createImage(new int[][]{{128, 128}, {128, 128}}, new int[][]{{128, 128}, {128, 128}}, new int[][]{{128, 128}, {128, 128}}, 2, 2));
//	}
	
	public static Texture createDefaultSpecularMap(){	
		return new Texture(createImage(new int[][]{{255, 255}, {255, 255}}, new int[][]{{255, 255}, {255, 255}}, new int[][]{{255, 255}, {255, 255}}, 2, 2));
	}
	
	public static Texture createDefaultGlowMap(){
		return new Texture(createImage(new int[][]{{0, 0}, {0, 0}}, new int[][]{{0, 0}, {0, 0}}, new int[][]{{0, 0}, {0, 0}}, 2, 2));
	}
	
//	public static Texture createDefaultCubeMap(){
//		int[][] r = new int[2][2];
//		int[][] g = new int[2][2];
//		int[][] b = new int[2][2];
//		int width = 2;
//		int height = 2;
//		
//		r[0][0] = 0;
//		r[1][0] = 0;
//		r[0][1] = 0;
//		r[1][1] = 0;
//		
//		g[0][0] = 0;
//		g[1][0] = 0;
//		g[0][1] = 0;
//		g[1][1] = 0;
//		
//		b[0][0] = 0;
//		b[1][0] = 0;
//		b[0][1] = 0;
//		b[1][1] = 0;
//		
//		Texture result = new Texture(createImage(r, g, b, width, height));
//		
//		return result;
//	}
	
	public static BufferedImage getDefaultIcon16(){
		return createImage(getDefaultIconRed(), getDefaultIconGreen(), getDefaultIconBlue(), 16, 16);
	}
	
	public static BufferedImage getDefaultIcon32(){
		return createImage(getDefaultIcon32Red(), getDefaultIcon32Green(), getDefaultIcon32Blue(), 32, 32);
	}
	
//	public static BufferedImage getDefaultIcon128(){
//		final BufferedImage before = getDefaultIcon16();
//		final AffineTransform affineTransform = new AffineTransform();
//		affineTransform.scale(8.0, 8.0);
//		return new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR).filter(before, new BufferedImage(before.getWidth(), before.getHeight(), BufferedImage.TYPE_INT_ARGB));
//	}
	
	public static int[][] getDefaultIconRed(){
		return new int[][]{{  0,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1}, 
						   {  1,  0,  0,  0,  0,  0,  2,  2,  0,  0,  0,  0,  0,  0,  0,  1}, 
						   {  1,  0,  0,  0,  0,  0, 23, 23,  1,  0,  0,  0,  0,  0,  0,  2}, 
						   {  1,  0,  0,  0,  0,  2, 30, 30,  2,  1,  3, 32, 28, 35,  4,  2}, 
						   {  1,  0,  0,  0,  1, 20, 12, 13, 19,  2, 23,  9,  2, 30,  0,  3}, 
						   {  2,  0,  0,  0,  1, 31,  2,  2, 30,  2, 30,  2, 18, 14,  0,  3}, 
						   {  2,  0,  0,  1, 16, 16,  2,  2, 18, 29, 15,  2, 30,  1,  0,  3}, 
						   {  2,  0,  0,  1, 30,  2,  8,  8,  2, 37,  2, 12, 20,  1,  0,  3}, 
						   {  2,  0,  1, 12, 20,  2, 28, 28,  2,  3,  2, 30,  2,  0,  0,  3}, 
						   {  3,  0,  1, 30,  3,  6, 26, 27,  5,  2,  7, 25,  1,  0,  0,  2}, 
						   {  3,  0,  8, 24,  2, 27,  6,  7, 25,  2, 27,  5,  0,  0,  0,  2}, 
						   {  2,  0, 30, 12, 12, 27,  1,  2, 29,  5, 28,  1,  0,  0,  0,  1}, 
						   {  2,  0, 17, 19, 19,  5,  1,  1, 10, 41,  8,  0,  0,  0,  0,  1}, 
						   {  1,  0,  0,  0,  0,  0,  0,  0,  0, 22,  0,  0,  0,  0,  0,  1}, 
						   {  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1}, 
						   {  0,  1,  1,  1,  1,  2,  2,  2,  2,  2,  1,  1,  1,  1,  0,  0}};
	}
	
	public static int[][] getDefaultIconGreen(){
		return new int[][]{{  5,  5,  6,  8, 10, 12, 14, 14, 15, 15, 14, 14, 12, 10,  8,  7}, 
						   {  5,  0,  1,  1,  2,  3,  8,  9,  3,  3,  3,  3,  3,  2,  1,  8}, 
						   {  6,  1,  1,  2,  3,  5, 59, 59,  6,  6,  6,  6,  5,  4,  2, 11}, 
						   {  8,  1,  2,  3,  5, 10, 77, 77, 12,  9, 15, 82, 71, 88, 13, 15}, 
						   {  9,  1,  2,  4,  6, 53, 38, 40, 52, 13, 62, 30, 13, 77,  5, 18}, 
						   { 11,  2,  3,  5,  8, 79, 13, 14, 79, 16, 78, 14, 49, 40,  5, 19}, 
						   { 13,  2,  4,  6, 44, 46, 13, 14, 52, 78, 45, 14, 79,  8,  4, 19}, 
						   { 15,  3,  5,  8, 79, 14, 28, 28, 16, 93, 15, 36, 54,  6,  4, 17}, 
						   { 16,  3,  6, 34, 56, 14, 74, 73, 15, 17, 13, 77, 11,  5,  3, 16}, 
						   { 17,  4,  7, 77, 16, 25, 70, 73, 22, 13, 25, 65,  7,  4,  2, 14}, 
						   { 16,  4, 25, 63, 14, 71, 23, 27, 67, 13, 71, 17,  5,  3,  2, 12}, 
						   { 15,  3, 77, 37, 37, 71, 11, 12, 76, 21, 73,  7,  4,  2,  1, 10}, 
						   { 12,  2, 44, 50, 51, 18,  7,  8, 31,103, 25,  5,  3,  2,  1,  8}, 
						   {  9,  1,  2,  4,  4,  4,  4,  5,  5, 57,  4,  3,  2,  1,  1,  6}, 
						   {  6,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1,  0,  5}, 
						   {  5,  6,  8, 10, 11, 12, 12, 12, 12, 11, 10,  9,  7,  6,  5,  5}};
	}
	
	public static int[][] getDefaultIconBlue(){
		return new int[][]{{ 21, 21, 24, 28, 32, 37, 39, 41, 42, 41, 40, 39, 36, 32, 27, 26}, 
						   { 20,  4,  5,  8, 10, 14, 26, 26, 17, 16, 15, 15, 13, 11,  7, 28}, 
						   { 23,  5,  7, 11, 15, 21,122,121, 25, 23, 23, 23, 22, 17, 11, 35}, 
						   { 27,  7, 10, 14, 20, 32,155,155, 36, 31, 42,164,146,175, 35, 42}, 
						   { 30,  9, 13, 18, 25,111, 84, 88,110, 38,127, 69, 39,154, 20, 49}, 
						   { 33, 10, 15, 21, 29,158, 39, 41,159, 44,157, 41,105, 87, 21, 50}, 
						   { 37, 13, 18, 25, 94, 99, 39, 41,109,156, 97, 41,158, 29, 20, 49}, 
						   { 41, 15, 21, 29,158, 40, 66, 67, 45,184, 42, 82,113, 25, 18, 46}, 
						   { 43, 16, 24, 77,116, 41,150,148, 42, 46, 39,154, 35, 22, 16, 43}, 
						   { 46, 18, 27,154, 44, 61,142,147, 55, 39, 61,133, 26, 19, 13, 39}, 
						   { 45, 19, 60,129, 41,144, 57, 64,136, 40,144, 46, 22, 16, 11, 35}, 
						   { 41, 17,153, 83, 82,144, 35, 36,152, 54,147, 26, 19, 13,  9, 31}, 
						   { 35, 13, 92,104,106, 47, 27, 29, 72,200, 61, 21, 15, 10,  7, 27}, 
						   { 29,  8, 13, 17, 19, 19, 19, 20, 22,118, 19, 15, 11,  7,  5, 24}, 
						   { 23,  5,  8, 10, 12, 12, 13, 13, 13, 13, 11,  9,  7,  5,  4, 20}, 
						   { 22, 22, 27, 30, 33, 35, 35, 36, 35, 34, 32, 29, 26, 23, 19, 21}};
	}
	
	public static int[][] getDefaultIcon32Red(){
		return new int[][]{{  0,  0,  1,  1,  1,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  3,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1,  1,  0}, 
						   {  0,  0,  0,  0,  1,  1,  1,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1,  1,  1,  1,  1}, 
						   {  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  8,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 34, 33,  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  6, 51, 51,  5,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0,  0,  0,  1,  2}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, 30, 31, 32, 28,  1,  1,  1,  1,  1,  1, 23, 28, 28, 28, 28, 27, 10,  0,  2,  2}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  1,  1,  4, 51,  7,  8, 50,  3,  1,  1,  1,  1, 10, 49, 28, 28, 27, 31, 54,  5,  0,  2,  3}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  1,  1, 26, 35,  2,  2, 37, 24,  2,  2,  2,  2, 34, 26,  2,  2,  1, 27, 34,  0,  0,  3,  3}, 
						   {  2,  1,  0,  0,  0,  0,  0,  0,  1,  1,  2, 49, 11,  2,  2, 13, 48,  3,  2,  2,  6, 51,  5,  2,  2,  3, 49, 10,  1,  0,  3,  3}, 
						   {  2,  1,  0,  0,  0,  0,  0,  0,  1,  1, 22, 39,  2,  2,  2,  2, 42, 19,  2,  2, 27, 33,  2,  2,  2, 21, 40,  1,  1,  0,  3,  4}, 
						   {  2,  1,  0,  0,  0,  0,  0,  1,  1,  1, 47, 14,  2,  2,  2,  2, 17, 44,  2,  3, 49, 10,  2,  2,  2, 45, 16,  1,  1,  0,  3,  4}, 
						   {  2,  1,  0,  0,  0,  0,  0,  1,  1, 18, 43,  2,  2,  2,  2,  2,  2, 46, 15, 21, 40,  2,  2,  2, 15, 46,  1,  1,  0,  0,  3,  4}, 
						   {  2,  2,  0,  0,  0,  0,  1,  1,  1, 43, 18,  2,  2,  2,  2,  2,  2, 22, 39, 44, 16,  2,  2,  2, 39, 21,  1,  1,  0,  0,  3,  3}, 
						   {  2,  2,  0,  0,  0,  0,  1,  1, 14, 47,  2,  2,  2,  4,  4,  2,  2,  3, 50, 46,  2,  2,  2, 10, 50,  3,  1,  1,  0,  0,  3,  3}, 
						   {  3,  2,  0,  0,  0,  0,  1,  1, 39, 22,  2,  2,  2, 24, 24,  2,  2,  2, 27, 23,  2,  2,  2, 33, 27,  1,  1,  0,  0,  0,  2,  3}, 
						   {  3,  2,  0,  0,  0,  1,  1, 10, 49,  3,  2,  2,  3, 48, 48,  3,  2,  2,  5,  4,  2,  2,  6, 51,  5,  1,  1,  0,  0,  0,  2,  3}, 
						   {  3,  2,  0,  0,  0,  1,  1, 35, 27,  2,  2,  2, 22, 40, 42, 19,  2,  2,  2,  2,  2,  2, 28, 33,  1,  1,  0,  0,  0,  0,  2,  3}, 
						   {  3,  2,  0,  0,  1,  1,  7, 51,  5,  2,  2,  3, 47, 14, 18, 43,  2,  2,  2,  2,  2,  3, 50,  9,  1,  1,  0,  0,  0,  0,  2,  3}, 
						   {  3,  3,  0,  0,  1,  1, 31, 31,  2,  2,  2, 19, 42,  2,  2, 46, 14,  2,  2,  2,  2, 21, 39,  1,  1,  1,  0,  0,  0,  0,  2,  2}, 
						   {  3,  3,  0,  0,  1,  4, 51,  7,  2,  2,  2, 45, 16,  2,  2, 22, 38,  2,  2,  2,  2, 46, 15,  1,  1,  0,  0,  0,  0,  0,  1,  2}, 
						   {  3,  2,  0,  0,  1, 26, 35,  2,  2,  2, 16, 45,  2,  2,  2,  3, 49, 10,  2,  2, 16, 45,  1,  1,  1,  0,  0,  0,  0,  0,  1,  2}, 
						   {  3,  2,  0,  0,  2, 50, 10,  2,  2,  2, 42, 19,  2,  2,  2,  2, 27, 34,  2,  2, 40, 21,  1,  1,  0,  0,  0,  0,  0,  0,  1,  2}, 
						   {  2,  2,  0,  0, 22, 47, 19, 19, 19, 25, 46,  2,  1,  1,  1,  1,  5, 51,  6, 10, 50,  2,  1,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  2,  2,  0,  0, 29, 37, 38, 38, 38, 38, 17,  1,  1,  1,  1,  1,  1, 31, 28, 34, 27,  1,  1,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  2,  1,  0,  0,  0,  0,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  8, 50, 51,  5,  1,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1, 36, 33,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0, 11,  8,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1,  1}, 
						   {  1,  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1}, 
						   {  1,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  1}, 
						   {  0,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0,  0,  0,  0}, 
						   {  0,  0,  1,  1,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1,  1,  1,  1,  1,  1,  0,  0,  0}};
	}
	
	public static int[][] getDefaultIcon32Green(){
		return new int[][]{{  5,  5,  6,  7,  7,  8,  9, 10, 11, 12, 13, 14, 14, 15, 15, 16, 16, 16, 16, 16, 16, 16, 15, 15, 14, 13, 12, 11,  9,  8,  7,  6}, 
						   {  5,  4,  3,  4,  4,  5,  6,  7,  8, 10, 10, 12, 13, 13, 13, 14, 13, 14, 13, 13, 13, 13, 13, 12, 11, 11,  9,  9,  7,  6,  7,  7}, 
						   {  6,  3,  0,  0,  1,  1,  1,  1,  1,  2,  2,  2,  2,  3,  3,  3,  3,  3,  3,  3,  2,  3,  2,  2,  2,  2,  2,  1,  1,  1,  6,  9}, 
						   {  7,  3,  0,  1,  1,  1,  1,  1,  2,  2,  3,  3,  4, 25, 24,  4,  4,  4,  4,  3,  4,  3,  3,  3,  3,  3,  2,  2,  2,  1,  7, 10}, 
						   {  8,  4,  1,  1,  1,  1,  1,  2,  2,  3,  4,  5,  5, 86, 84,  6,  6,  5,  5,  5,  5,  5,  5,  5,  5,  4,  3,  3,  2,  1,  9, 12}, 
						   {  8,  5,  1,  1,  1,  1,  2,  2,  3,  4,  5,  6, 21,126,125, 20,  8,  7,  7,  6,  6,  7,  7,  7,  6,  6,  5,  4,  3,  2, 12, 14}, 
						   {  9,  5,  1,  1,  1,  2,  2,  3,  4,  5,  6,  8, 77, 80, 82, 74, 10,  9,  8,  8,  8,  9, 62, 72, 72, 71, 71, 70, 29,  3, 13, 16}, 
						   { 10,  6,  1,  1,  2,  2,  3,  3,  4,  6,  7, 17,125, 26, 29,123, 17, 11, 10, 10, 10, 33,121, 72, 71, 71, 79,131, 17,  3, 15, 17}, 
						   { 11,  7,  1,  1,  2,  2,  3,  4,  5,  7,  9, 68, 89, 13, 14, 94, 64, 12, 12, 12, 13, 87, 69, 13, 13, 12, 71, 85,  5,  4, 17, 19}, 
						   { 12,  7,  1,  2,  2,  3,  4,  5,  6,  7, 13,121, 34, 14, 14, 39,118, 15, 14, 14, 24,125, 22, 14, 14, 16,122, 30,  6,  4, 18, 20}, 
						   { 13,  8,  2,  2,  2,  3,  4,  5,  7,  8, 59, 99, 13, 13, 14, 14,105, 54, 15, 15, 73, 85, 15, 14, 14, 58,100,  8,  6,  4, 18, 20}, 
						   { 14,  9,  2,  2,  3,  4,  5,  6,  7, 10,116, 42, 13, 13, 14, 14, 50,109, 16, 18,121, 34, 15, 14, 14,112, 44,  8,  6,  4, 17, 21}, 
						   { 15, 10,  2,  2,  3,  4,  5,  7,  8, 49,108, 13, 13, 13, 14, 14, 16,115, 44, 58,101, 15, 15, 14, 45,113, 10,  7,  5,  4, 17, 21}, 
						   { 16, 11,  2,  3,  4,  5,  6,  8,  9,108, 51, 13, 13, 13, 14, 14, 15, 61, 98,110, 48, 15, 15, 14, 99, 58,  9,  7,  5,  4, 17, 21}, 
						   { 16, 12,  2,  3,  4,  5,  7,  8, 40,116, 14, 13, 13, 18, 18, 14, 15, 19,122,115, 16, 15, 14, 32,123, 14,  8,  6,  5,  3, 16, 20}, 
						   { 17, 13,  3,  3,  4,  6,  7,  9, 98, 61, 13, 14, 14, 65, 65, 15, 15, 15, 72, 63, 15, 14, 14, 86, 72,  9,  7,  5,  4,  3, 15, 19}, 
						   { 18, 13,  3,  4,  5,  6,  8, 31,122, 17, 14, 14, 17,119,118, 17, 15, 14, 21, 19, 13, 13, 23,126, 20,  8,  6,  5,  4,  3, 14, 18}, 
						   { 18, 14,  3,  4,  5,  7,  9, 89, 70, 14, 14, 15, 60,101,105, 54, 15, 14, 14, 13, 13, 13, 73, 85,  9,  7,  5,  4,  3,  2, 13, 17}, 
						   { 19, 15,  3,  4,  6,  8, 24,125, 21, 14, 15, 17,116, 44, 51,108, 15, 14, 14, 13, 13, 16,122, 30,  8,  6,  5,  4,  3,  2, 12, 16}, 
						   { 18, 16,  3,  5,  6,  9, 79, 79, 14, 14, 15, 54,106, 16, 16,115, 43, 14, 14, 13, 13, 58, 99,  9,  7,  6,  4,  4,  3,  2, 11, 16}, 
						   { 19, 15,  3,  5,  7, 17,126, 26, 14, 14, 15,111, 48, 15, 15, 61, 98, 14, 14, 13, 13,113, 43,  8,  6,  5,  4,  3,  2,  2,  9, 15}, 
						   { 18, 15,  3,  5,  6, 69, 88, 13, 14, 14, 47,111, 14, 14, 13, 17,122, 32, 14, 13, 45,113, 10,  7,  6,  4,  3,  3,  2,  2,  9, 14}, 
						   { 17, 14,  3,  4, 11,122, 31, 13, 13, 14,106, 52, 12, 12, 12, 13, 71, 86, 14, 13,101, 57,  8,  6,  5,  4,  3,  2,  2,  1,  8, 13}, 
						   { 15, 12,  3,  4, 58,116, 52, 53, 54, 66,115, 12, 11, 10, 10, 11, 21,125, 24, 33,122, 12,  7,  5,  4,  3,  2,  2,  2,  1,  7, 12}, 
						   { 14, 12,  2,  3, 74, 93, 94, 94, 94, 94, 48,  9,  8,  8,  8,  9, 10, 81, 74, 86, 70,  8,  6,  5,  4,  3,  2,  2,  1,  1,  6, 11}, 
						   { 12,  9,  2,  2,  4,  5,  6,  7,  7,  7,  8,  7,  7,  6,  7,  7,  8, 25,124,126, 17,  6,  5,  4,  3,  2,  2,  1,  1,  1,  6,  9}, 
						   { 11,  8,  1,  2,  3,  3,  4,  5,  5,  5,  5,  5,  5,  5,  5,  6,  6,  7, 91, 83,  5,  5,  4,  3,  2,  2,  2,  1,  1,  1,  5,  9}, 
						   {  9,  6,  1,  1,  2,  2,  3,  3,  4,  3,  4,  3,  4,  4,  4,  4,  5,  5, 30, 24,  4,  3,  3,  2,  2,  1,  1,  1,  1,  1,  4,  8}, 
						   {  8,  5,  1,  1,  1,  2,  2,  2,  2,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  3,  2,  2,  2,  1,  1,  1,  1,  1,  0,  4,  7}, 
						   {  6,  5,  0,  1,  1,  1,  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2,  1,  1,  1,  1,  1,  1,  0,  0,  3,  6}, 
						   {  6,  5,  5,  5,  6,  7,  8,  8,  9, 10, 10, 10, 10, 10, 11, 10, 10, 10, 10,  9,  9,  8,  8,  7,  7,  5,  5,  4,  4,  3,  4,  5}, 
						   {  5,  5,  6,  7,  8,  9, 11, 11, 12, 13, 13, 14, 14, 14, 14, 14, 13, 13, 12, 12, 12, 11, 11, 10,  9,  8,  8,  7,  6,  5,  5,  4}};
	}
	
	public static int[][] getDefaultIcon32Blue(){
		return new int[][]{{ 21, 23, 24, 26, 28, 29, 31, 33, 35, 37, 39, 41, 41, 43, 44, 44, 44, 44, 45, 45, 45, 44, 43, 42, 41, 39, 37, 34, 31, 29, 26, 24}, 
						   { 22, 19, 15, 17, 19, 20, 23, 25, 27, 30, 32, 35, 36, 37, 38, 39, 38, 39, 38, 37, 36, 36, 36, 35, 33, 32, 29, 28, 25, 22, 25, 27}, 
						   { 24, 14,  3,  4,  5,  5,  6,  7,  8, 10, 11, 13, 13, 14, 14, 15, 15, 14, 14, 14, 13, 14, 13, 13, 12, 11, 10,  9,  7,  6, 23, 30}, 
						   { 26, 15,  4,  5,  5,  6,  7,  9, 10, 12, 14, 16, 17, 58, 58, 19, 19, 18, 17, 17, 17, 17, 17, 17, 16, 15, 13, 11,  9,  7, 25, 33}, 
						   { 28, 17,  5,  5,  6,  8,  9, 11, 13, 15, 17, 20, 23,170,167, 25, 24, 22, 21, 21, 21, 21, 21, 21, 20, 19, 17, 14, 11,  9, 29, 36}, 
						   { 30, 18,  5,  6,  7,  9, 11, 13, 15, 18, 21, 24, 53,241,240, 51, 29, 26, 25, 25, 25, 25, 26, 26, 25, 24, 21, 18, 14, 11, 35, 40}, 
						   { 32, 20,  6,  7,  9, 10, 12, 15, 18, 21, 24, 28,155,160,164,149, 33, 30, 29, 29, 30, 30,129,147,146,146,144,143, 66, 14, 38, 44}, 
						   { 33, 22,  7,  8, 10, 12, 14, 17, 20, 24, 28, 47,240, 64, 69,237, 46, 34, 34, 33, 33, 76,233,147,146,145,161,249, 44, 16, 41, 46}, 
						   { 35, 24,  8,  9, 11, 13, 15, 18, 22, 26, 30,140,177, 39, 40,184,132, 38, 36, 37, 38,173,141, 39, 38, 36,144,170, 23, 17, 45, 50}, 
						   { 37, 25,  8, 10, 12, 15, 17, 20, 24, 28, 39,234, 78, 40, 41, 88,227, 43, 40, 40, 59,240, 56, 41, 40, 44,234, 69, 23, 18, 48, 52}, 
						   { 39, 26,  9, 11, 13, 16, 19, 22, 25, 30,123,194, 39, 40, 40, 41,204,115, 42, 42,147,170, 42, 41, 40,121,196, 29, 24, 19, 48, 53}, 
						   { 41, 28, 10, 12, 15, 17, 20, 24, 27, 34,223, 93, 39, 39, 40, 41,108,211, 44, 49,234, 78, 42, 41, 40,217, 97, 28, 24, 19, 46, 54}, 
						   { 42, 31, 11, 13, 16, 19, 22, 26, 29,106,210, 38, 38, 39, 40, 41, 44,222, 97,122,197, 43, 42, 40, 98,219, 33, 27, 22, 18, 47, 54}, 
						   { 44, 32, 12, 14, 17, 20, 24, 28, 32,208,110, 38, 39, 39, 40, 41, 43,127,193,213,104, 42, 41, 40,195,121, 30, 26, 21, 17, 45, 53}, 
						   { 46, 34, 13, 15, 18, 22, 26, 30, 90,224, 41, 39, 40, 47, 49, 42, 43, 50,236,222, 44, 42, 41, 75,236, 40, 28, 24, 20, 17, 43, 51}, 
						   { 47, 36, 14, 17, 20, 23, 27, 32,192,126, 40, 40, 41,134,134, 42, 43, 43,146,130, 42, 40, 40,171,145, 31, 26, 23, 19, 16, 41, 51}, 
						   { 48, 38, 14, 17, 21, 25, 29, 73,235, 47, 40, 41, 47,230,227, 46, 43, 41, 55, 50, 40, 40, 57,242, 52, 29, 25, 21, 18, 15, 39, 49}, 
						   { 49, 38, 15, 19, 22, 27, 31,175,143, 40, 41, 42,125,197,205,115, 43, 41, 40, 39, 39, 39,147,170, 31, 27, 23, 19, 17, 14, 37, 47}, 
						   { 51, 41, 16, 20, 24, 28, 60,241, 54, 41, 42, 46,223, 96,110,210, 43, 41, 39, 39, 38, 45,235, 70, 29, 25, 21, 18, 15, 13, 34, 45}, 
						   { 49, 43, 17, 21, 25, 30,159,159, 40, 41, 42,114,206, 44, 45,223, 94, 41, 40, 39, 39,122,194, 31, 27, 24, 20, 17, 14, 12, 32, 44}, 
						   { 49, 42, 17, 21, 26, 47,241, 64, 40, 41, 43,215,104, 42, 42,127,192, 41, 40, 39, 39,219, 94, 29, 25, 22, 18, 15, 13, 11, 29, 42}, 
						   { 48, 41, 16, 21, 26,141,175, 39, 40, 41,103,215, 40, 40, 40, 47,235, 75, 40, 39, 99,218, 32, 27, 23, 20, 17, 14, 12, 10, 29, 40}, 
						   { 47, 39, 16, 20, 34,235, 73, 38, 39, 40,205,112, 37, 37, 37, 38,145,172, 41, 39,196,119, 29, 25, 22, 18, 15, 13, 11,  9, 27, 38}, 
						   { 43, 35, 14, 18,120,223,110,112,112,136,223, 38, 34, 33, 34, 35, 53,240, 59, 76,235, 38, 27, 23, 19, 16, 13, 11,  9,  8, 24, 36}, 
						   { 40, 35, 12, 16,149,183,184,184,185,185,102, 31, 30, 29, 30, 31, 33,162,150,172,143, 28, 24, 21, 17, 14, 12, 10,  8,  7, 23, 34}, 
						   { 37, 29, 10, 13, 17, 21, 24, 26, 27, 27, 27, 26, 25, 26, 26, 27, 30, 62,237,242, 47, 24, 21, 18, 15, 13, 10,  9,  7,  6, 21, 32}, 
						   { 34, 27,  8, 10, 13, 16, 19, 21, 22, 22, 22, 22, 21, 21, 22, 23, 25, 25,180,166, 23, 20, 18, 15, 13, 11,  9,  7,  6,  5, 19, 30}, 
						   { 31, 23,  7,  8, 10, 13, 14, 16, 17, 17, 17, 17, 18, 18, 18, 19, 20, 20, 70, 58, 18, 16, 14, 12, 11,  9,  7,  6,  5,  5, 17, 28}, 
						   { 28, 21,  5,  6,  8,  9, 11, 13, 13, 14, 14, 14, 14, 14, 15, 15, 15, 15, 15, 15, 14, 12, 11,  9,  8,  7,  6,  5,  5,  4, 16, 26}, 
						   { 26, 19,  4,  5,  6,  7,  8,  9, 10, 10, 11, 11, 11, 11, 11, 11, 11, 11, 11, 10, 10,  9,  8,  8,  7,  6,  5,  5,  4,  3, 14, 24}, 
						   { 23, 21, 18, 20, 22, 25, 26, 27, 29, 29, 30, 30, 30, 31, 31, 32, 31, 31, 31, 30, 29, 27, 26, 23, 23, 21, 19, 16, 16, 14, 19, 22}, 
						   { 21, 23, 25, 27, 29, 32, 34, 35, 37, 38, 39, 40, 40, 40, 40, 40, 39, 38, 37, 38, 36, 35, 34, 33, 32, 29, 28, 27, 25, 23, 22, 20}};
	}
	
	public static float clamp(float value, float min, float max){
		return value < min ? min : value > max ? max : value;
	}
	
	public static ImageData bufferedImageToByteBuffer(BufferedImage image){
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		final ByteBuffer buffer = DataUtil.createByteBuffer(height * width * 4);
		final boolean hasAlpha = image.getColorModel().hasAlpha();
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				final int pixel = pixels[y * width + x];
				
				buffer.put((byte)((pixel >> 16) & 0xFF));
				buffer.put((byte)((pixel >> 8) & 0xFF));
				buffer.put((byte)((pixel) & 0xFF));
				
				if(hasAlpha){
					buffer.put((byte)((pixel >> 24) & 0xFF));
				}else{
					buffer.put((byte)(0xFF));
				}
			}
		}
		
		buffer.flip();
		
		return new ImageData(width, height, buffer);
	}
	
	public static ImageData imageToByteBuffer(String file){
		BufferedImage image = null;
		
		try{
			image = ImageIO.read(new File(file));
		}catch(IOException e){
			e.printStackTrace();
		}
		
		final int width = image.getWidth();
		final int height = image.getHeight();
		
		final int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
		
		final ByteBuffer buffer = DataUtil.createByteBuffer(height * width * 4);
		final boolean hasAlpha = image.getColorModel().hasAlpha();
		
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				final int pixel = pixels[y * width + x];
				
				buffer.put((byte)((pixel >> 16) & 0xFF));
				buffer.put((byte)((pixel >> 8) & 0xFF));
				buffer.put((byte)((pixel) & 0xFF));
				
				if(hasAlpha){
					buffer.put((byte)((pixel >> 24) & 0xFF));
				}else{
					buffer.put((byte)(0xFF));
				}
			}
		}
		
		buffer.flip();
		
		return new ImageData(width, height, buffer);
	}
	
	public static class ImageData{
		public final int        width;
		public final int        height;
		public final ByteBuffer data;
		
		public ImageData(int width, int height, ByteBuffer data){
			this.width = width;
			this.height = height;
			this.data = data;
		}
	}
}
