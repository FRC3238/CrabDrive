package turtleguidance;

import java.awt.Graphics;
import java.awt.Point;

public class Cursor extends Entity
{   
    public Point position;
    public boolean isClicked;
    public boolean justReleased;
    public boolean justPressed;
    
    
    public Cursor()
    {
        super();
        position = new Point();
        isClicked = false;
        justReleased = false;
        justPressed = false;
    }
    
    public void step()
    {
        //this.x=;
        //this.y=yVel;
        
        //do something
        
        //justReleased = false;
        //justPressed = false;
    }
    
    public void setPosition(Point p)
    {
        //position.setLocation(p.x, p.y);
        //position = p;
    }
    
    public void draw(Graphics g)
    {
        if(justPressed)
        {
            g.fillOval(this.position.x, this.position.y, 4, 4);
        }
        g.drawString("isClicked = "+isClicked + "     justPressed = "+justPressed+"    justReleased = "+justReleased, 16, 16);
    }
}
