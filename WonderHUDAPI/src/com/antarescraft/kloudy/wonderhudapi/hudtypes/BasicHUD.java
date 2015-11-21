package com.antarescraft.kloudy.wonderhudapi.hudtypes;

import com.antarescraft.kloudy.wonderhudapi.HUDPosition;

public class BasicHUD extends BaseHUDType
{
	private double distance;
	private double angle;
	
	public BasicHUD(HUDPosition position)
	{
		this(position, 8, Math.PI / 118);
	}
	
	public BasicHUD(HUDPosition position, double distance, double angle)
	{
		super(position);
		
		this.distance = distance;//HUD display distance
		this.angle = angle;//HUD line height
	}
	
	@Override
	public void setDistance(double distance)
	{
		this.distance = distance;
	}
	
	@Override
	public double getDistance()
	{
		return distance;
	}
	
	@Override
	public void setDeltaTheta(double angle)
	{
		this.angle = angle;
	}
	
	@Override 
	public double getDeltaTheta()
	{
		return angle;
	}
	
	@Override
	public double getVerticalAngle()
	{
		return position.getVerticalAngle();
	}
	
	@Override
	public double getHorizontalAngle()
	{
		return position.getHorizontalAngle();
	}
}