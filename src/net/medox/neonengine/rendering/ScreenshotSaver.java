package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

import javax.imageio.ImageIO;

public class ScreenshotSaver extends Thread{
	private String destination;
	private ByteBuffer buffer;
	private int width;
	private int height;
	
	public ScreenshotSaver(String destination, ByteBuffer buffer, int width, int height){
		this.destination = destination;
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
//	@SuppressWarnings("deprecation")
	public void run(){
		if(width > 0 && height > 0){
//		    int bpp = 4;
		    
//		    int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
//		    int month = Calendar.getInstance().get(Calendar.MONTH)+1;
//		    int year = Calendar.getInstance().get(Calendar.YEAR);
//		    
//		    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
//		    int minute = Calendar.getInstance().get(Calendar.MINUTE);
//		    int second = Calendar.getInstance().get(Calendar.SECOND);
		    
//		    String a2 = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
//		    String b2 = String.valueOf(hour) + "." + String.valueOf(minute) + "." + String.valueOf(second);
		    final String a2 = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1) + "-" + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		    final String b2 = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) + "." + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)) + "." + String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
			
//		    File test = new File(destination);
//		    
//		    test.mkdir();
		    new File(destination).mkdir();
		    
//		    final File file = new File(destination + (Window.getTitle() + " " + a2 + "_" + b2) + ".png");
//		    String format = "PNG";
		    final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		     
		    for(int x = 0; x < width; x++){
			    for(int y = 0; y < height; y++){
				    final int i = (x + (width * y)) * /*bpp*/4;
//				    int r = buffer.get(i) & 0xFF;
//				    int g = buffer.get(i + 1) & 0xFF;
//				    int b = buffer.get(i + 2) & 0xFF;
//				    image.setRGB(x, height - (y + 1), (0xFF << 24) | (r << 16) | (g << 8) | b);
				    image.setRGB(x, height - (y + 1), (0xFF << 24) | ((buffer.get(i) & 0xFF) << 16) | ((buffer.get(i + 1) & 0xFF) << 8) | (buffer.get(i + 2) & 0xFF));
			    }
		    }
		     
		    try{
			   	ImageIO.write(image, /*format*/"PNG", new File(destination + (Window.getTitle() + " " + a2 + "_" + b2) + ".png"));
		    }catch(IOException e){
		    	e.printStackTrace();
		    }
		    
//		    this.stop();
//		    this.destroy();
		}
	}
}
