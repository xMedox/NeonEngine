package net.medox.neonengine.components;

import net.medox.neonengine.core.Entity2DComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.Transform2D;
import net.medox.neonengine.math.Vector2f;
import net.medox.neonengine.math.Vector3f;
import net.medox.neonengine.rendering.RenderingEngine;

public class TextBox extends Entity2DComponent{
	private final int max;
	private final Vector3f color;
	private final Vector3f colorText;
	
	private String text;
	private boolean pressed;
	
	public TextBox(int max){
		this(max, "");
	}
	
	public TextBox(int max, String text){
		this(max, text, new Vector3f(1, 1, 1));
	}
	
	public TextBox(int max, String text, Vector3f color){
		this(max, text, color, new Vector3f(1, 1, 1));
	}
	
	public TextBox(int max, String text, Vector3f color, Vector3f colorText){
		this.max = max;
		this.text = text;
		this.color = color;
		this.colorText = colorText;
	}
	
	@Override
	public void input(float delta){
		if(!Input.isGrabbed() && Input.getMouseDown(1)){
			if(Input.getMousePosition().getX() >= getTransform().getTransformedPos().getX() && Input.getMousePosition().getY() >= getTransform().getTransformedPos().getY() && Input.getMousePosition().getX() < getTransform().getTransformedPos().getX() + getTransform().getScale().getX() && Input.getMousePosition().getY() < getTransform().getTransformedPos().getY() + getTransform().getScale().getY()){
				pressed = true;
				
//				Input.setTextInputMode(pressed);
			}else{
				pressed = false;
				
//				Input.setTextInputMode(pressed);
			}
		}
		
		if(Input.isGrabbed()){
			pressed = false;
			
//			Input.setTextInputMode(pressed);
		}
		
		if(pressed){
			if(text.length() <= max){
				text += Input.getChars();
				
				if(text.length() > max){
					text = text.substring(0, max);
				}
			}
			
			if(!text.equals("")){
				if(Input.getKeyDown(Input.KEY_BACKSPACE)){
					text = text.substring(0, text.length()-1);
				}
			}
			
			if(!Input.isGrabbed() && Input.getKeyDown(Input.KEY_ENTER)){
				pressed();
			}
		}
	}
	
	public void pressed(){
		
	}
	
	public String getText(){
		return text;
	}
	
	public void setProgress(String text){
		this.text = text;
	}
	
	@Override
	public void render(){
		RenderingEngine.add2DMesh(getTransform(), -1, new Vector3f(0f, 0f, 0f));
		
		final Transform2D transform = new Transform2D();
		transform.setPos(getTransform().getTransformedPos().add(1));
		transform.setScale(getTransform().getScale().sub(2).mul(new Vector2f(1, 1)));
		
		RenderingEngine.add2DMesh(transform, -1, color);
		
		RenderingEngine.drawString(getTransform().getTransformedPos().getX(), getTransform().getTransformedPos().getY()+2, text, 1, 1, colorText);
	}
}
