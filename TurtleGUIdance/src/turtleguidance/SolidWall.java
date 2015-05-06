package turtleguidance;

import java.awt.Graphics;
import resources.ImageManager;

public class SolidWall extends Entity
{
    static int SolidWallCount;
    
    boolean hardness;
    
    public SolidWall()
    {
        super();
        SolidWallCount++;
        x = 0;
        y = 0;
        xVel = 0;
        yVel = 0;
        hardness = true;
        spriteIndex = ImageManager.staticImageManager.getImage("sprStone");
    }
    public SolidWall(int initX, int initY)
    {
        super();
        SolidWallCount++;
        x = initX;
        y = initY;
        xVel = 1;
        yVel = 0;
        hardness = false;
        spriteIndex = ImageManager.staticImageManager.getImage("sprStone");
        
    }
    
    @Override
    public void step()
    {
        x+=xVel;
        y+=yVel;
    }
    
    @Override
    public void draw(Graphics g)
    {
        //g.fillRect(x, y, 16, 16);
        g.drawImage(spriteIndex, x, y, null);
        
    }
}
