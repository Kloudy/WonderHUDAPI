package com.antarescraft.kloudy.wonderhudapi.hudtypes;

import com.antarescraft.kloudy.wonderhudapi.HUDPosition;

public class BasicHUD extends BaseHUDType
{
	public BasicHUD(HUDPosition position)
	{
		super(position);
	}
	
	@Override
	public double getDistance()
	{
		return 8;
	}
	
	@Override 
	public double getDeltaTheta()
	{
		return Math.PI / 118;
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