package com.antarescraft.kloudy.wonderhudapi.imageprocessing;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class ImageProcessor 
{
	private static final String pixel = "▇";
	private static final String reset = "§r";
	private static final String space = "  ";
	
	public static String[][] processImage(BufferedImage[] frames, int width, int height)
	{
		String[][] lines = new String[frames.length][height];
		
		//process each frame
		for(int i = 0; i < frames.length; i++)
		{
			BufferedImage frame = frames[i];

			int type = frame.getType() == 0? BufferedImage.TYPE_INT_ARGB : frame.getType();
			frame = resizeImageWithHint(frame, type, width, height);
			
			//process each rbg value of each pixel
			for(int y = 0; y < height; y++)
			{
				MinecraftColor lastColor = MinecraftColor.TRANSPARENT;
				int numTrans = 0;
				for(int x = 0; x < width; x++)
				{
					int rgb = frame.getRGB(x, y);
					MinecraftColor closestColor = null;
					
					int mask = 0x0000FF;
					int alpha = (rgb >> 24) & mask;
					int red = (rgb >> 16) & mask;
					int green = (rgb >> 8) & mask;
					int blue = rgb & mask;
					
					int difRGB = Integer.MAX_VALUE;
					
					for(MinecraftColor color: MinecraftColor.values())
					{
						int r = color.red();
						int g = color.green();
						int b = color.blue();
						int dif = Math.abs(r - red) + Math.abs(g - green) + Math.abs(b - blue);
						
						if(dif < difRGB)
						{
							difRGB = dif;
							closestColor = color;
						}
					}
					
					if(alpha < 255)
					{
						closestColor = MinecraftColor.TRANSPARENT;
					}
					
					//repeat color
					if(x != 0 && closestColor == lastColor)
					{
						if(lastColor == MinecraftColor.TRANSPARENT)
						{
							lines[i][y] += space;
							numTrans += 1;
						}
						else
						{
							lines[i][y] += pixel;
						}					
					}
					//different color or first character in the row
					else
					{
						//first character
						if(x == 0)
						{
							lines[i][y] = new String();
							
							if(closestColor == MinecraftColor.TRANSPARENT)
							{
								lines[i][y] += closestColor.symbol() + space;
								numTrans += 1;
							}
							else
							{
								lines[i][y] += closestColor.symbol() + pixel;
							}
						}
						//different color
						else
						{
							//last color was transparent, insert format reset
							if(lastColor == MinecraftColor.TRANSPARENT)
							{
								int l = lines[i][y].length();
								StringBuilder strbuilder = new StringBuilder(lines[i][y]);
								lines[i][y] = strbuilder.insert(l - numTrans, reset).toString();
								numTrans = 0;
							}
							
							//new color is transparent
							if(closestColor == MinecraftColor.TRANSPARENT)
							{
								lines[i][y] += closestColor.symbol() + space;
								numTrans += 1;
							}
							else
							{
								lines[i][y] += closestColor.symbol() + pixel;
							}
						}
					}
					
					lastColor = closestColor;
				}
				
				//if transparent pixel string goes to the end of the row
				if(numTrans > 0)
				{
					int l = lines[i][y].length();
					StringBuilder strbuilder = new StringBuilder(lines[i][y]);
					lines[i][y] = strbuilder.insert(l - numTrans, reset).toString();
					numTrans = 0;
				}
			}
		}
		
		return lines;
	}
	
    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type, int IMG_WIDTH, int IMG_HEIGHT)
    {
		BufferedImage resizedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, IMG_WIDTH, IMG_HEIGHT, null);
		g.dispose();	
		g.setComposite(AlphaComposite.Src);
	 
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
	 
		return resizedImage;
    }	
}