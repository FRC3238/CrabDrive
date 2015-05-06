package turtleguidance;

import java.awt.Graphics;

import resources.ImageManager;

public class GameField extends Entity
{
	private int windowWidth;
	private int windowHeight;
	
	public GameField(int w, int h)
	{
		super();
		spriteIndex = ImageManager.staticImageManager.getImage("sprField");
		windowWidth = w;
		windowHeight = h;
	}
	
	public void draw(Graphics g)
	{
		g.drawImage(this.spriteIndex, this.x, this.y, windowWidth, windowHeight, null);
	}
}
