package net.medox.game.client;

import net.medox.neonengine.components.MeshRenderer;
import net.medox.neonengine.core.Entity;
import net.medox.neonengine.rendering.Camera;
import net.medox.neonengine.rendering.Material;
import net.medox.neonengine.rendering.Mesh;
import net.medox.neonengine.rendering.Shader;

public class Weapon extends Entity{
	private Mesh mesh;
	private Material material;
	private WeaponScript weaponscript;
	private DelayLook delaylook;
	
	private Entity entity;
	
	private boolean isActive;
	private int id;
	
	public Weapon(Entity playerEntity, Mesh mesh, Material material, WeaponScript weaponscript, DelayLook delaylook){
		this(playerEntity, mesh, material, weaponscript, delaylook, true);
	}
	
	public Weapon(Entity playerEntity, Mesh mesh, Material material, WeaponScript weaponscript, DelayLook delaylook, boolean isActive){
		this.mesh = mesh;
		this.material = material;
		this.weaponscript = weaponscript;
		this.delaylook = delaylook;
		
		this.isActive = isActive;
		
		entity = new Entity();
		
		entity.addComponent(new MeshRenderer(this.mesh, this.material));
		entity.addComponent(this.weaponscript);
		
		addComponent(this.delaylook);
		playerEntity.addChild(this);
		
		addChild(entity);
	}
	
	@Override
	public void inputAll(float delta){
		if(isActive){
			super.inputAll(delta);
		}
	}
	
	@Override
	public void updateAll(float delta){
		if(isActive){
			super.updateAll(delta);
		}
	}
	
	@Override
	public void renderAll(Shader shader, Camera camera){
		if(isActive){
			super.renderAll(shader, camera);
		}
	}

	public Mesh getMesh(){
		return mesh;
	}

	public Material getMaterial(){
		return material;
	}

	public WeaponScript getWeaponscript(){
		return weaponscript;
	}

	public Entity getEntity(){
		return entity;
	}

	public boolean isActive(){
		return isActive;
	}

	public void setActive(boolean isActive){
		this.isActive = isActive;
	}
	
	public void change(int id, Weapon weapon, Inventory inventory){
		weaponscript.change(id, weapon, inventory);
	}
	
	public void changeIn(){
		weaponscript.changeIn();
	}
	
	public boolean isChange(){
		return weaponscript.isChange();
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
}
