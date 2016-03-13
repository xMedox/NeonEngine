package net.medox.game.client;

import net.medox.neonengine.core.EntityComponent;
import net.medox.neonengine.core.Input;
import net.medox.neonengine.core.InputKey;
import net.medox.neonengine.math.Vector3f;

public class WeaponScript extends EntityComponent{
	private FPPlayer player;
	
	private float time = 12*0.016666668f;
	private float timeNow = time;
	
	private float timeChange = 16*0.016666668f;
	private float timeChangeNow = timeChange;
	
	private float modelScale = 2;
	
	private boolean aim = false;
	private boolean aimed = false;
	
	private Vector3f noPos = new Vector3f(0.325f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.0f*modelScale);
//	private Vector3f noPos = new Vector3f(0.275f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.0f*modelScale);
//	private Vector3f noPos = new Vector3f(0.375f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.1f*modelScale);
//	private Vector3f noPos = new Vector3f(0.425f*modelScale, /*-0.4f*modelScale*/-0.95f/2*modelScale, 1.1f*modelScale);
//	private Vector3f noPos = new Vector3f(0.275f*modelScale, /*-0.4f*modelScale*/-0.925f/2*modelScale, 1.0f*modelScale);
//	private Vector3f noPos = new Vector3f(0.34f*modelScale, /*-0.4f*modelScale*/-1.0f/2*modelScale, 1.0f*modelScale);
//	private Vector3f noPos = new Vector3f(0.58f*modelScale, /*-0.4f*modelScale*/-0.8f/2*modelScale, 1.2f*modelScale);
//	private Vector3f yes = new Vector3f(0*scale, -0.31f*scale, (1.2f/1.75f/1.25f)*scale);
//	private Vector3f noPos = new Vector3f(0.45f*modelScale, -0.4f*modelScale, 1.2f*modelScale);
	private Vector3f yesPos = new Vector3f(0*modelScale, /*-0.28f*modelScale*/-0.735f/2*modelScale, 0.5f*modelScale);
//	private Vector3f yesPos = new Vector3f(0*modelScale, /*-0.28f*modelScale*/-0.74f/2*modelScale, 0.6f*modelScale);
	
	private float noScale;
	private float yesScale = (float)Math.toRadians(50.0f);
	
//	private float noZoom = 12;
//	private float yesZoom = 8;
	
	private float noSense;
	private float yesSense = 0.1f;
	
	private Vector3f changePos = new Vector3f(0.325f*3.5f*modelScale, /*-0.4f*modelScale*/-0.95f*3.5f/2*modelScale, 1.0f/3.5f*modelScale);
	
	private Vector3f pos = changePos;
	private float scale;
//	private float zoom = noZoom;
	private float sense;
	
	private Vector3f plusPos;
	private float plusScale;
//	private float plusZoom;
	private float plusSense;
	private Vector3f plusChange;
	
	private Vector3f plusChangePos;
	private float plusChangeScale;
	private float plusChangeSense;
	
//	private boolean enableMoving = false;
//	private Vector3f movingPos = new Vector3f(0, 0, 0);
//	private boolean movingup = true;
//	private Vector3f movingUpPos = new Vector3f(0, 0.02f, 0);
//	private Vector3f movingUp = new Vector3f(0, 0, 0);
//	private float movingTime = 70;
	
	private InputKey aimKey;
	
	private int changeId;
	private Weapon changeWeapon;
	private Inventory changeInventory;
	private boolean change = true;
	private boolean changeIn = true;
	
	private float timeSave;
	
	public WeaponScript(FPPlayer player){
		this(player, new InputKey(Input.MOUSE, Input.BUTTON_RIGHT));
	}
	
	public WeaponScript(FPPlayer player, InputKey aimKey){
		this.player = player;
		
		timeSave = time;
		
		noScale = player.camera.getFov();
		scale = noScale;
		
		noSense = player.sensitivity;
		sense = noSense;
		
		plusPos = (noPos.sub(yesPos)).div(time);
		plusScale = (noScale - yesScale)/time;
//		plusZoom = (noZoom - yesZoom)/time;
		plusSense = (noSense - yesSense)/time;
		plusChange =  (noPos.sub(changePos)).div(timeChange);
		
//		movingUp = movingUpPos.div(movingTime);
		
		this.aimKey = aimKey;
	}
	
	@Override
	public void input(float delta){
		if(!change){
			if(Input.inputKeyDown(aimKey)){
				if(aimed == aim){
					aim = !aim;
				}
			}
		}
	}
	
	@Override
	public void update(float delta){
		if(!change){
	//		if(enableMoving){
	//			if(movingup){
	//				movingPos = movingPos.add(movingUp.div(deltaSave).mul(delta));
	//				
	//				if(movingPos.getX() > movingUpPos.getX()){
	//					movingPos.setX(movingUpPos.getX());
	//				}
	//				
	//				if(movingPos.getY() > movingUpPos.getY()){
	//					movingPos.setY(movingUpPos.getY());
	//				}
	//				
	//				if(movingPos.getZ() > movingUpPos.getZ()){
	//					movingPos.setZ(movingUpPos.getZ());
	//				}
	//				
	//				if(movingPos.equals(movingUpPos)){
	//					movingup = !movingup;
	//				}
	//			}else{
	//				movingPos = movingPos.sub(movingUp.div(deltaSave).mul(delta));
	//				
	//				if(movingPos.getX() < 0){
	//					movingPos.setX(0);
	//				}
	//				
	//				if(movingPos.getY() < 0){
	//					movingPos.setY(0);
	//				}
	//				
	//				if(movingPos.getZ() < 0){
	//					movingPos.setZ(0);
	//				}
	//				
	//				if(movingPos.equals(new Vector3f(0, 0, 0))){
	//					movingup = !movingup;
	//				}
	//			}
	//		}
			
			if(aim && !aimed){
				timeNow -= 1*delta;
				
				pos = pos.sub(plusPos.mul(delta));
				scale = scale - plusScale*delta;
//				zoom = zoom - plusZoom/deltaSave*delta;
				sense = sense - plusSense*delta;
				
				if(pos.getX() < yesPos.getX()){
					pos.setX(yesPos.getX());
				}
				
				if(pos.getY() > yesPos.getY()){
					pos.setY(yesPos.getY());
				}
				
				if(pos.getZ() < yesPos.getZ()){
					pos.setZ(yesPos.getZ());
				}
				
				if(scale < yesScale){
					scale = yesScale;
				}
				
//				if(zoom < yesZoom){
//					zoom = yesZoom;
//				}
				
				if(sense < yesSense){
					sense = yesSense;
				}
				
				if(timeNow <= 0){
					timeNow = 0;
					aimed = true;
					
					scale = yesScale;
					pos = yesPos;
//					zoom = yesZoom;
					sense = yesSense;
				}
			}
			
			if(!aim && aimed){
				timeNow += 1*delta;
				
				pos = pos.add(plusPos.mul(delta));
				scale = scale + plusScale*delta;
//				zoom = zoom + plusZoom/deltaSave*delta;
				sense = sense + plusSense*delta;
				
				if(pos.getX() > noPos.getX()){
					pos.setX(noPos.getX());
				}
				
				if(pos.getY() < noPos.getY()){
					pos.setY(noPos.getY());
				}
				
				if(pos.getZ() > noPos.getZ()){
					pos.setZ(noPos.getZ());
				}
				
				if(scale > noScale){
					scale = noScale;
				}
				
//				if(zoom > noZoom){
//					zoom = noZoom;
//				}
				
				if(sense > noSense){
					sense = noSense;
				}
				
				if(timeNow >= time){
					timeNow = time;
					aimed = false;
					
					scale = noScale;
					pos = noPos;
//					zoom = noZoom;
					sense = noSense;
				}
			}
		}else{
			if(changeIn){
				timeChangeNow -= 1*delta;
				
				pos = pos.add(plusChange.mul(delta));
				
				if(pos.getX() < noPos.getX()){
					pos.setX(noPos.getX());
				}
				
				if(pos.getY() > noPos.getY()){
					pos.setY(noPos.getY());
				}
				
				if(pos.getZ() > noPos.getZ()){
					pos.setZ(noPos.getZ());
				}
				
				if(timeChangeNow <= 0){
					timeChangeNow = 0;
					change = false;
					
					pos = noPos;
				}
			}else{
				timeChangeNow += 1*delta;
				
				pos = pos.sub(plusChangePos.mul(delta));
				scale = scale - plusChangeScale*delta;
				sense = sense - plusChangeSense*delta;
				
				if(pos.getX() > changePos.getX()){
					pos.setX(changePos.getX());
				}
				
				if(pos.getY() < changePos.getY()){
					pos.setY(changePos.getY());
				}
				
				if(pos.getZ() < changePos.getZ()){
					pos.setZ(changePos.getZ());
				}
				
				if(scale > noScale){
					scale = noScale;
				}
				
				if(scale > noScale){
					scale = noScale;
				}
				
				if(timeChangeNow >= timeChange){
					timeChangeNow = timeChange;
					change = false;
					
					pos = changePos;
					scale = noScale;
					sense = noSense;
					
					changeInventory.change(changeId, changeWeapon);
				}
			}
		}
		
		if(aimed || aim){
			player.enableSprint = false;
		}else{
			player.enableSprint = true;
		}
		
//		getTransform().setPos(pos.add(movingPos));
		getTransform().setPos(pos);
		player.camera.setFov(scale);
		player.sensitivity = sense;
	}
	
	public void change(int id, Weapon weapon, Inventory inventory){
		changeId = id;
		changeWeapon = weapon;
		changeInventory = inventory;
		
		change = true;
		changeIn = false;
		
		aim = false;
		aimed = false;
		
		time = timeSave;
		timeNow = time;
		
//		scale = noScale;
////		zoom = noZoom;
//		sense = noSense;
//		
//		pos = noPos;
		
		plusChangePos = (pos.sub(changePos)).div(timeChange);
		plusChangeScale = (scale - noScale)/time;
		plusChangeSense = (sense - noSense)/time;
	}
	
	public void changeIn(){
		change = true;
		changeIn = true;
	}

	public boolean isChange(){
		return change;
	}
}
