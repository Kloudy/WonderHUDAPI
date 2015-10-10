package com.antarescraft.kloudy.wonderhudapi;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.antarescraft.kloudy.wonderhudapi.hudtypes.BaseHUDType;
import com.antarescraft.kloudy.wonderhudapi.protocol.FakeDisplay;


/**
 * Represents a single HUD Display in the player's view
 */

public class HUD
{
	private final int STARTING_ENTITY_ID = -15000;
	
	private Player player;
	private int maxLines;
	private ArrayList<Integer> entityIds;
	private BaseHUDType hudType;
	private ArrayList<String> lines;
	
	public HUD(Player player, int maxLines, ArrayList<String> lines, BaseHUDType hudType)
	{
		this.player = player;
		this.maxLines = maxLines;
		this.hudType = hudType;
		this.lines = lines;
		entityIds = new ArrayList<Integer>();
	}

	public void spawnDisplay()
	{
		for(int i = 0; i < maxLines; i++)
		{
			if(!PlayerHUD.NextEntityId.containsKey(player.getUniqueId()))
			{
				PlayerHUD.NextEntityId.put(player.getUniqueId(), STARTING_ENTITY_ID);
			}
			
			int entityId = PlayerHUD.NextEntityId.get(player.getUniqueId());
			PlayerHUD.NextEntityId.put(player.getUniqueId(), entityId - 1);
			
			entityIds.add(entityId);
		}
		
		FakeDisplay.spawnFakeDisplay(this);
	}
	
	public void updateDisplay()
	{
		for(int i = 0; i < lines.size(); i++)
		{
			FakeDisplay.updateDisplayLine(this, i, lines.get(i));
		}
	}
	
	public void destroyDisplay()
	{
		FakeDisplay.destroyDisplay(this);
	}
	
	public ArrayList<String> getLines()
	{
		return lines;
	}
	
	public void setLines(ArrayList<String> lines)
	{
		this.lines = lines;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public int getMaxLines()
	{
		return maxLines;
	}
	
	public ArrayList<Integer> getEntityIds()
	{
		return entityIds;
	}
	
	public BaseHUDType getHudType()
	{
		return hudType;
	}
	
	public void setHUDType(BaseHUDType hudType)
	{
		this.hudType = hudType;
	}
	
	public Location calculateNewLocation(int rowIndex)
	{
		//Vector zVector = new Vector(0, 0, 1);
		
		double distance = hudType.getDistance();
		double deltaTheta = hudType.getDeltaTheta();
		double verticalAngle = hudType.getVerticalAngle();
		double horizontalOffset = hudType.getHorizontalAngle();
		
		Location l = player.getLocation();
		double xi = l.getX();
		double yi = l.getY();
		double zi = l.getZ();
		
		//Vector playerDirection = player.getLocation().getDirection();//player direction unit vector
		
		double pitch = -l.getPitch() / 90;
		double yaw = Math.toRadians(player.getLocation().getYaw() + 180);
		
		//Vector perp = new Vector(-1, pitch, 0);
		
		
		//double yawAngle =  (Math.PI/2) - (zVector.angle(new Vector(playerDirection.getX(), 0, playerDirection.getZ())));
		
		//double yawAngle = playerDirection.angle(perp);
		
		
		
		//if(playerDirection.getZ() < 0)
		//{
			
			//yawAngle *= -1;
		//}
		//playerDirection = rotateAroundYAxis(playerDirection, -yawAngle + Math.PI/2);
		
		//System.out.println(yawAngle);
		
		//doing rotation relative origin <0,0,0> will add xi,yi,zi in at the end
		double x0 = -1;
		double y0 = pitch;
		double z0 = horizontalOffset;
		
		//cartesian conversion to cylindrical coordinates of where player is looking 'distance' blocks away
		//double r = Math.sqrt((x0*x0)+(y0*y0)+(z0*z0));
		double theta = Math.atan2(y0, x0) + (rowIndex * deltaTheta * 3) - verticalAngle;
		
		//convert cylindrical coordinates back into cartesian coordinates
		double y1 = Math.sin(theta);
		double x1 = Math.cos(theta);
		double z1 = z0;
		
		Vector v = new Vector(x1, y1, z1);
		v = rotateAroundYAxis(v, yaw);
		
		//System.out.println("Rotated:" + v.toString());
		
		//Vector printVect = new Vector(x1, y1, z1);
		//System.out.println("Non-rotated: " + printVect.toString());

		x1 = v.getZ() * distance;
		y1 = v.getY() * distance;
		z1 = v.getX() * distance;
		
		double sum = + v.getX() + v.getZ();
		System.out.println("Sum: " + sum);
		
		y1 = (1 - v.getX() - v.getZ()) * distance;
		
		x1 += xi;// + horizontalOffset;//(((playerDirection.getZ() + playerDirection.getY())/2) * horizontalOffset);
		y1 += yi;
		z1 += zi;//+ horizontalOffset;//(((playerDirection.getX() + playerDirection.getY())/2) * horizontalOffset);
		
		return new Location(l.getWorld(), x1, y1,  z1);
	}
	
	/*public Location calculateNewLocation(int rowIndex)
	{	
		double distance = hudType.getDistance();
		double deltaTheta = hudType.getDeltaTheta();
		double verticalAngle = hudType.getVerticalAngle();
		
		Location l = player.getLocation();
		double xi = l.getX();
		double yi = l.getY();
		double zi = l.getZ();
		
		Vector playerDirection = player.getLocation().getDirection();//player direction unit vector
		
		//doing rotation relative origin <0,0,0> will add xi,yi,zi in at the end
		double x0 = (playerDirection.getX() * distance);
		double y0 = (playerDirection.getY() * distance);
		double z0 = (playerDirection.getZ() * distance);
		
		//cartesian conversion to spherical coordinates of where player is looking 'distance' blocks away
		double r = Math.sqrt((x0*x0)+(y0*y0)+(z0*z0));
		double theta0 = Math.acos((y0 / r));
		double phi0 = Math.atan2(z0,  x0);
		
		//rotate theta0 by deltaTheta
		double theta1 = theta0 + (rowIndex * deltaTheta) - verticalAngle;
		double phi1 = phi0;
		
		//convert spherical coordinates back into cartesian coordinates
		double y1 = r * Math.cos(theta1);
		double x1 = r * Math.cos(phi1) * Math.sin(theta1);
		double z1 = r * Math.sin(theta1) * Math.sin(phi1);
		x1 += xi;
		y1 += yi;
		z1 += zi;
		
		return new Location(l.getWorld(), x1, y1,  z1);
	}*/
	
	private Vector rotateAroundYAxis(Vector vector, double theta)
	{
		double x0 = vector.getX();
		double y0 = vector.getY();
		double z0 = vector.getZ();
		
		double x = (z0 * Math.sin(theta)) + (x0 * Math.cos(theta));
		double y = y0;
		double z = (z0 * Math.cos(theta)) - (x0 * Math.sin(theta));
		
		return new Vector(x, y, z);
	}
}