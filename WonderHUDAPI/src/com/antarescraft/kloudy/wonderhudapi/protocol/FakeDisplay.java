package com.antarescraft.kloudy.wonderhudapi.protocol;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.antarescraft.kloudy.wonderhudapi.HUD;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

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

public class FakeDisplay
{
	private static final int ENTITY_FLAGS_INDEX = 0;
	private static final int ARMORSTAND_FLAGS_INDEX = 10;
	private static final int CUSTOM_NAME_INDEX = 2;
	private static final int CUSTOM_NAME_VISIBLE_INDEX = 3;
	
	public static void spawnFakeDisplay(HUD hud)
	{
		Player player = hud.getPlayer();

		for(int i = 0; i < hud.getMaxLines(); i++)
		{
			int entityId = hud.getEntityIds().get(i);
			
			WrapperPlayServerSpawnEntityLiving spawnEntityPacket = new WrapperPlayServerSpawnEntityLiving();
			
			spawnEntityPacket.setEntityID(entityId);
			spawnEntityPacket.setType(EntityType.ARMOR_STAND);
			
			Location location = hud.calculateNewLocation(i);
			spawnEntityPacket.setX(location.getX());
			spawnEntityPacket.setY(location.getY());
			spawnEntityPacket.setZ(location.getZ());
			
			sendPacket(player, spawnEntityPacket.handle);
			
			WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
			entityMetadataPacket.setEntityID(entityId);
			List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
			
			byte entityFlags = 32;//00100000 - Invisible flag is the 6th bit
			byte armorStandFlags = 1;//00000001 - Small armorStand is first bit
			
			metadata.add(new WrappedWatchableObject(ENTITY_FLAGS_INDEX, entityFlags));
			metadata.add(new WrappedWatchableObject(ARMORSTAND_FLAGS_INDEX, armorStandFlags));
			
			
			if(i < hud.getLines().size())
			{
				metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1));
				
				String customName = hud.getLines().get(i);
				metadata.add(new WrappedWatchableObject(CUSTOM_NAME_INDEX, customName));
			}
			else
			{
				metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)0));
			}
			
			entityMetadataPacket.setMetadata(metadata);

			sendPacket(player, entityMetadataPacket.handle);
		}
	}
	
	public static void updateDisplayLocation(HUD hud)
	{
		Player player = hud.getPlayer();
		
		for(int i = 0; i < hud.getMaxLines(); i++)
		{
			int entityId = hud.getEntityIds().get(i);
			
			Location location = hud.calculateNewLocation(i);

			WrapperPlayServerEntityTeleport teleportEntityPacket = new WrapperPlayServerEntityTeleport();
			teleportEntityPacket.setEntityID(entityId);
			teleportEntityPacket.setX(location.getX());
			teleportEntityPacket.setY(location.getY());
			teleportEntityPacket.setZ(location.getZ());
			
			sendPacket(player, teleportEntityPacket.handle);
		}
	}
	
	public static void updateDisplayLine(HUD hud, int rowIndex, String text)
	{
		WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
		entityMetadataPacket.setEntityID(hud.getEntityIds().get(rowIndex));
		
		List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
		metadata.add(new WrappedWatchableObject(CUSTOM_NAME_INDEX, text));
		
		entityMetadataPacket.setMetadata(metadata);
		
		sendPacket(hud.getPlayer(), entityMetadataPacket.handle);
	}
	
	public static void hideDisplayLine(HUD hud, int rowIndex)
	{
		WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
		entityMetadataPacket.setEntityID(hud.getEntityIds().get(rowIndex));
		
		List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
		metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)0));
		
		entityMetadataPacket.setMetadata(metadata);
		
		sendPacket(hud.getPlayer(), entityMetadataPacket.handle);
	}
	
	public static void showDisplayLine(HUD hud, int rowIndex)
	{
		WrapperPlayServerEntityMetadata entityMetadataPacket = new WrapperPlayServerEntityMetadata();
		entityMetadataPacket.setEntityID(hud.getEntityIds().get(rowIndex));
		
		List<WrappedWatchableObject> metadata = entityMetadataPacket.getMetadata();
		metadata.add(new WrappedWatchableObject(CUSTOM_NAME_VISIBLE_INDEX, (byte)1));
		
		entityMetadataPacket.setMetadata(metadata);
		
		sendPacket(hud.getPlayer(), entityMetadataPacket.handle);
	}
	
	public static void destroyDisplay(HUD hud)
	{
		WrapperPlayServerEntityDestroy destroyEntityPacket = new WrapperPlayServerEntityDestroy();
		
		int[] ids = new int[hud.getEntityIds().size()];
		
		for(int i = 0; i < hud.getEntityIds().size(); i++)
		{
			ids[i] = hud.getEntityIds().get(i);
		}
		
		destroyEntityPacket.setEntityIds(ids);
		
		sendPacket(hud.getPlayer(), destroyEntityPacket.handle);
	}
	
	private static void sendPacket(Player player, PacketContainer packet)
	{
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		try 
		{
			manager.sendServerPacket(player, packet);
		} 
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}
}