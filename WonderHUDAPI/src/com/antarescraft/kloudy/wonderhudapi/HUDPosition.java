package com.antarescraft.kloudy.wonderhudapi;

/**
 * Represents the different locations a HUD can be on the screen
 */

public enum HUDPosition
{
	TOP_CENTER(Math.PI/6.2, 0),
	CENTER(Math.PI/20, 0),
	BOTTOM_CENTER(-Math.PI/20, 0);
	
	private double verticalAngle;
	private double horizontalAngle;
	
	HUDPosition(double verticalAngle, double horizontalAngle)
	{
		this.verticalAngle = verticalAngle;
		this.horizontalAngle = horizontalAngle;
	}
	
	public double getVerticalAngle()
	{
		return verticalAngle;
	}
	
	public double getHorizontalAngle()
	{
		return horizontalAngle;
	}
}