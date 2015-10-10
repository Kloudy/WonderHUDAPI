package com.antarescraft.kloudy.wonderhudapi.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.antarescraft.kloudy.wonderhudapi.PlayerHUD;

public class OnPlayerQuitEvent implements Listener
{
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();

		PlayerHUD.NextEntityId.remove(player.getUniqueId());
		PlayerHUD.PlayerHUDs.remove(player.getUniqueId());
	}
}