package net.medox.neonengine.rendering;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import net.medox.neonengine.core.NeonEngine;
import net.medox.neonengine.core.Time;

public class ScreenshotSaver extends Thread{
	private final String destination;
	private final ByteBuffer buffer;
	private final int width;
	private final int height;
	
	public ScreenshotSaver(String destination, ByteBuffer buffer, int width, int height){
		this.destination = destination;
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	public void run(){
		if(width > 0 && height > 0){
			final String YMD = Time.getCurrentYear() + "-" + Time.getCurrentMonth() + "-" + Time.getCurrentDay();
			final String HMS = Time.getCurrentHour() + "." + Time.getCurrentMinute() + "." + Time.getCurrentSecond();
			
			new File(destination).mkdir();
			
			final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			for(int x = 0; x < width; x++){
				for(int y = 0; y < height; y++){
					final int index = (x + (width * y)) * 4;
					image.setRGB(x, height - (y + 1), (0xFF << 24) | ((buffer.get(index) & 0xFF) << 16) | ((buffer.get(index + 1) & 0xFF) << 8) | (buffer.get(index + 2) & 0xFF));
				}
			}
			
			try{
				ImageIO.write(image, "PNG", new File(destination + (Window.getTitle() + " " + YMD + "_" + HMS) + ".png"));
			}catch(IOException e){
				NeonEngine.throwError("Error: couldn't save the screenshot.");
			}
		}
	}
}
