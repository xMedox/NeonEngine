package net.medox.game;

import net.medox.neonengine.core.Entity;
import net.medox.neonengine.core.Input;

public class Inventory extends Entity{
	private int invLength = 2;
	private Weapon[] invBar = new Weapon[invLength];
	private int selected = 0;
	
	public Weapon currentWeapon;
	
	public Inventory(Weapon w, Weapon w2){
		invBar[0] = w;
		invBar[0].setActive(true);
		
		invBar[1] = w2;
		invBar[1].setActive(false);
		
		currentWeapon = invBar[0];
	}
	
	@Override
	public void inputAll(float delta){
		super.inputAll(delta);
		
		if(Input.getKeyDown(Input.KEY_1)){
			if(!currentWeapon.isChange()){
				if(selected != 0){
					int selectedChange = 0;
					
					currentWeapon.change(selectedChange, invBar[selectedChange], this);
				}
//				invBar[selected].setActive(true);
//				invBar[selectedSave].setActive(false);
//				
//				currentWeapon = invBar[selected];
			}
		}
		
		if(Input.getKeyDown(Input.KEY_2)){
			if(!currentWeapon.isChange()){
				if(selected != 1){
					int selectedChange = 1;
					
					currentWeapon.change(selectedChange, invBar[selectedChange], this);
				}
//				invBar[selected].setActive(true);
//				invBar[selectedSave].setActive(false);
//				
//				currentWeapon = invBar[selected];
			}
		}
		
		if(Input.isMouseWheelDirection(Input.WHEEL_DOWN)){
			if(!currentWeapon.isChange()){
				int selectedChange = selected - 1;
				
				if(selectedChange == -1){
					selectedChange = invLength-1;
				}
				
				currentWeapon.change(selectedChange, invBar[selectedChange], this);
			}
//			invBar[selected].setActive(true);
//			invBar[selectedSave].setActive(false);
//			
//			currentWeapon = invBar[selected];
		}
		
		if(Input.isMouseWheelDirection(Input.WHEEL_UP)){
			if(!currentWeapon.isChange()){
				int selectedChange = selected + 1;
				
				if(selectedChange == invLength){
					selectedChange = 0;
				}
				
				currentWeapon.change(selectedChange, invBar[selectedChange], this);
			}
//			invBar[selected].setActive(true);
//			invBar[selectedSave].setActive(false);
//			
//			currentWeapon = invBar[selected];
		}
	}
	
//	@Override
//	public void updateAll(float delta){
//		super.updateAll(delta);
//	}
//	
//	@Override
//	public void renderAll(Shader shader, RenderingEngine renderingEngine){
//		super.renderAll(shader, renderingEngine);
//	}

	public Weapon getCurrentWeapon(){
		return currentWeapon;
	}
	
	public void change(int id, Weapon weapon){
		int selectedSave = selected;
		
		selected = id;
		
		invBar[selected].setActive(true);
		invBar[selectedSave].setActive(false);
		
		currentWeapon = invBar[selected];
		
		currentWeapon.changeIn();
	}
}
