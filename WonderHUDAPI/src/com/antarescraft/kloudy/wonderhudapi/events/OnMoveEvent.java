package com.antarescraft.kloudy.wonderhudapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.antarescraft.kloudy.wonderhudapi.HUD;
import com.antarescraft.kloudy.wonderhudapi.PlayerHUD;
import com.antarescraft.kloudy.wonderhudapi.protocol.FakeDisplay;

public class OnMoveEvent implements Listener
{
	@EventHandler(priority = EventPriority.MONITOR)
	public void onMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		
		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getUniqueId());
		
		if(playerHUD != null)
		{
			for(HUD hud : playerHUD.getHUDs().values())
			{
				FakeDisplay.updateDisplayLocation(hud);
			}
		}
	}
}