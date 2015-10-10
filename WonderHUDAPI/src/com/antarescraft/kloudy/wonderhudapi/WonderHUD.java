package com.antarescraft.kloudy.wonderhudapi;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhudapi.hudtypes.BasicHUD;
import com.antarescraft.kloudy.wonderhudapi.hudtypes.ImageHUD;
import com.antarescraft.kloudy.wonderhudapi.imageprocessing.ImageProcessor;
import com.antarescraft.kloudy.wonderhudapi.protocol.FakeDisplay;

/**
 * 
 *Copyright (c) 2015 Kloudy

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 * 
 * @author Kloudy - playminecraft.net
 *
 */

public class WonderHUD
{
	private static PlayerHUD initPlayerHUD(Player player)
	{
		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getUniqueId());
		if(playerHUD == null)
		{
			playerHUD = new PlayerHUD(player);
			
			PlayerHUD.PlayerHUDs.put(player.getUniqueId(), playerHUD);
		}
		
		return playerHUD;
	}
	
	private static void updateHUD(HUD hud, ArrayList<String> lines)
	{
		//cut off lines that exeed the maximum
		if(lines.size() > hud.getMaxLines())
		{
			ArrayList<String> truncatedLines = new ArrayList<String>();
			for(int i = 0; i < hud.getMaxLines(); i++)
			{
				truncatedLines.add(lines.get(i));
			}
			
			lines = truncatedLines;
		}
		
		if(lines.size() < hud.getLines().size())//hide lines that are no longer being used
		{
			int dif = hud.getLines().size() - lines.size();
			
			for(int i = 0; i < dif; i++)
			{
				FakeDisplay.hideDisplayLine(hud, hud.getLines().size() - 1 - i);
			}
		}
		else if(lines.size() > hud.getLines().size())//show lines that are now being used
		{
			int dif = lines.size() - hud.getLines().size();
			
			for(int i = 0; i < dif; i++)
			{
				FakeDisplay.showDisplayLine(hud, lines.size() - 1 - i);
			}
		}
		
		hud.setLines(lines);
		hud.updateDisplay();
		FakeDisplay.updateDisplayLocation(hud);
	}
	
	/**
	 * Spawns a HUD into the player's view with the given text lines at the given position.
	 * The method will do nothing if the player already has a hud at the given position.
	 * 
	 * @param player The player for whom to spawn the HUD
	 * @param position The position of the HUD on the screen
	 * @param maxLines The maximum number of lines the HUD can have. Any additional lines exeeding this value will not be displayed.
	 * @param lines The lines to text to display on the HUD
	 * 
	 * Throws IllegalArgumentException if maxLength is less than 1
	 */
	public static void spawnHUD(Player player, HUDPosition position, int maxLines, ArrayList<String> lines)
	{
		if(maxLines < 1)
		{
			throw new IllegalArgumentException("maxLength must be > 0");
		}
		
		PlayerHUD playerHUD = initPlayerHUD(player);
		
		if(!playerHUD.hudExists(position))
		{
			HUD hud = new HUD(player, maxLines, lines, new BasicHUD(position));
			
			playerHUD.addHUD(hud);
		}
	}
	
	/**
	 * Spawns a HUD into the player's view displaying the given image at the given position. 
	 * The method will do nothing if the player already has a hud at the given position.
	 * 
	 * @param player The player for whom to spawn the HUD
	 * @param maxLines The maximum number of lines the HUD can have. If image's height is greater than this value then the additional lines of the image will be cut off.
	 * @param position The position of the HUD on the screen
	 * @param image The image to display on the HUD
	 * @param width The width of the HUD (The input image width will be resized to match this value)
	 * @param height The heigiht of the HUD (The input image height will be resized to match this value)
	 * 
	 * Throws IllegalArgumentException if maxLength is less than 1 or if width or height is less than 1
	 */
	public static void spawnHUD(Player player, HUDPosition position, int maxLines, BufferedImage image, int width, int height)
	{
		if(maxLines < 1)
		{
			throw new IllegalArgumentException("maxLength must be > 0");
		}
		
		if(width < 1)
		{
			throw new IllegalArgumentException("width must be > 0");
		}
		
		if(height < 1)
		{
			throw new IllegalArgumentException("height must be > 0");
		}
		
		PlayerHUD playerHUD = initPlayerHUD(player);
		
		if(!playerHUD.hudExists(position))
		{
			String[][] pixels = ImageProcessor.processImage(new BufferedImage[]{image}, width, height);
			
			if(pixels != null && pixels[0] != null)
			{
				ArrayList<String> lines = new ArrayList<String>();
				for(String line : pixels[0])
				{
					lines.add(line);
				}
				
				HUD hud = new HUD(player, maxLines, lines, new ImageHUD(position, width, height));
				
				playerHUD.addHUD(hud);
			}
		}
	}
	
	/**
	 * Updates a HUD for a player at the given position if it exists with the input lines of text
	 * The method does nothing if a HUD does not exist at the given position for the given player.
	 * 
	 * @param player The player whose HUD is getting updated
	 * @param position The position of the HUD to update
	 * @param lines Lines of text to display on the HUD
	 */
	public static void updateHUD(Player player, HUDPosition position, ArrayList<String> lines)
	{
		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getUniqueId());
		if(playerHUD != null)
		{
			HUD hud = playerHUD.getHUD(position);
			if(hud != null)
			{
				hud.setHUDType(new BasicHUD(position));
				updateHUD(hud, lines);
			}
		}
	}
	
	/**
	 * Updates a HUD for a player at the given position if it exists with the input image.
	 * The method does nothing if a HUD does not exist at the given position for the given player.
	 * 
	 * @param player The player whose HUD is getting updated
	 * @param position The position of the HUD to update
	 * @param image The image to display on the HUD
	 * @param width The width of the HUD (The input image width will be resized to match this value)
	 * @param height The heigiht of the HUD (The input image height will be resized to match this value)
	 * 
	 * Throws IllegalArgumentException if width or height is less than 1
	 */
	public static void updateHUD(Player player, HUDPosition position, BufferedImage image, int width, int height)
	{
		if(width < 1)
		{
			throw new IllegalArgumentException("width must be > 0");
		}
		
		if(height < 1)
		{
			throw new IllegalArgumentException("height must be > 0");
		}

		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getPlayer().getUniqueId());
		if(playerHUD != null)
		{
			HUD hud = playerHUD.getHUD(position);
			if(hud != null)
			{
				String[][] pixels = ImageProcessor.processImage(new BufferedImage[]{image}, width, height);
				
				if(pixels != null && pixels[0] != null)
				{
					ArrayList<String> lines = new ArrayList<String>();
					for(String line : pixels[0])
					{
						lines.add(line);
					}
					
					hud.setHUDType(new ImageHUD(position, width, height));
					updateHUD(hud, lines);
				}
			}
		}
	}
	
	/**
	 * Removes a HUD from a player's view at the given position if it exists
	 * 
	 * @param player The player for whom the HUD will be destroyed
	 * @param position The position of the HUD to remove
	 */
	public static void removeHUD(Player player, HUDPosition position)
	{
		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getUniqueId());
		if(playerHUD != null)
		{
			playerHUD.removeHUD(position);
		}
	}
	
	/**
	 * Remove all of a player's HUDs from view
	 * 
	 * @param player The player for whom all HUDs will be removed
	 */
	public static void removeAllHUDs(Player player)
	{
		PlayerHUD playerHUD = PlayerHUD.PlayerHUDs.get(player.getUniqueId());
		if(playerHUD != null)
		{
			playerHUD.destroy();
			
			PlayerHUD.PlayerHUDs.remove(player.getUniqueId());
		}
	}
}