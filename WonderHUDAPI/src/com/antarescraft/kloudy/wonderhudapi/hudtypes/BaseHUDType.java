package com.antarescraft.kloudy.wonderhudapi.hudtypes;

import com.antarescraft.kloudy.wonderhudapi.HUDPosition;

public abstract class BaseHUDType 
{
	protected String location;
	protected HUDPosition position;
	
	public BaseHUDType(HUDPosition position)
	{
		this.position = position;
	}
	
	public abstract void setDistance(double distance);
	public abstract double getDistance();
	public abstract void setDeltaTheta(double angle);
	public abstract double getDeltaTheta();
	public abstract double getVerticalAngle();
	public abstract double getHorizontalAngle();
	
	public HUDPosition getPosition()
	{
		return position;
	}
}