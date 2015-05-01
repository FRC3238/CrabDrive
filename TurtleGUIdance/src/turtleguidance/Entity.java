package turtleguidance;

import java.awt.Graphics;
import java.awt.Image;

public class Entity 
{
    int x;
    int y;
    int xVel;
    int yVel;
    Image spriteIndex;
    
    public Entity()
    {
        x = 0;
        y = 0;
        xVel = 0;
        yVel = 0;
        spriteIndex = null;
    }
    
    public void step()
    {
        this.x+=xVel;
        this.y+=yVel;
    }
    
    public void draw(Graphics g)
    {
        g.drawImage(this.spriteIndex, this.x, this.y, null);
    }
    
    public int getX()
    {
        return this.x;
    }
    
    public int getY()
    {
        return this.y;
    }
    
}
