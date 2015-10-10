package com.antarescraft.kloudy.wonderhudapi;

import java.util.Hashtable;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PlayerHUD 
{
	private Player player;
	private Hashtable<HUDPosition, HUD> huds;
	public static Hashtable<UUID, Integer> NextEntityId;
	public static Hashtable<UUID, PlayerHUD> PlayerHUDs;
	
	public PlayerHUD(Player player)
	{
		this.player = player;
		huds = new Hashtable<HUDPosition, HUD>();
		
		PlayerHUDs.put(player.getUniqueId(), this);
	}
	
	public void destroy()
	{
		for(HUD hud : huds.values())
		{
			hud.destroyDisplay();
		}
		
		huds.clear();
	}
	
	public boolean hudExists(HUDPosition position)
	{
		return huds.containsKey(position);
	}
	
	public void addHUD(HUD hud)
	{
		hud.spawnDisplay();
		huds.put(hud.getHudType().getPosition(), hud);
	}
	
	public HUD getHUD(HUDPosition position)
	{
		return huds.get(position);
	}
	
	public void removeHUD(HUDPosition position)
	{
		HUD hud = huds.get(position);
		if(hud != null)
		{
			hud.destroyDisplay();
		}
		
		huds.remove(position);
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Hashtable<HUDPosition, HUD> getHUDs()
	{
		return huds;
	}
}