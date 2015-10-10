package com.antarescraft.kloudy.wonderhudapi.hudtypes;

import com.antarescraft.kloudy.wonderhudapi.HUDPosition;

public class ImageHUD extends BaseHUDType
{
	private double distance, deltaTheta;
	
	public ImageHUD(HUDPosition position, int width, int height)
	{
		super(position);
		
		distance = 20;
		//deltaTheta = Math.PI / 288;
		deltaTheta = Math.PI/ 800;
		//offsetAngle = Math.PI / 5.7;
	}
	
	@Override
	public double getDistance()
	{
		return distance;
	}
	
	@Override
	public double getDeltaTheta()
	{
		return deltaTheta;
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