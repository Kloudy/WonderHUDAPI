package com.antarescraft.kloudy.wonderhudapi.hudtypes;

import com.antarescraft.kloudy.wonderhudapi.HUDPosition;

public class ImageHUD extends BaseHUDType
{
	private double distance, angle;
	
	public ImageHUD(HUDPosition position, int width, int height)
	{
		this(position, width, height, 20, Math.PI / 800);
	}
	
	public ImageHUD(HUDPosition position, int width, int height, double distance, double angle)
	{
		super(position);
		
		this.distance = distance;
		this.angle = angle;
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
		return position.getVerticalAngle() * 1.1;
	}
	
	@Override
	public double getHorizontalAngle()
	{
		return position.getHorizontalAngle();
	}
}