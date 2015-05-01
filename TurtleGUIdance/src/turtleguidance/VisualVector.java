/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtleguidance;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Anders
 */
public class VisualVector extends Entity
{
    public double xCoord1, yCoord1, xCoord2, yCoord2;
    public double theta;//radians
    public double length;
    public VisualVector tailingVector;
    public VisualVector nextVector;
    
    public VisualVector(double xCoord1, double yCoord1, double xCoord2, double yCoord2)
    {
        super();
        this.xCoord1 = xCoord1;
        this.yCoord1 = yCoord1;
        this.xCoord2 = xCoord2;
        this.yCoord2 = yCoord2;
        theta = Math.atan2(xCoord2-xCoord1, yCoord2-yCoord1);
        length = Math.sqrt(Math.pow(xCoord2-xCoord1, 2) + Math.pow(yCoord2-yCoord1, 2));
        tailingVector = null;
        nextVector = null;
    }
    
    public void setTailingVector(VisualVector v)
    {
        tailingVector = v;
    }
    
    public void setNextVector(VisualVector v)
    {
        nextVector = v;
    }
    
    public void draw(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.drawLine((int)xCoord1, (int)yCoord1, (int)xCoord2, (int)yCoord2);
        g.drawLine((int)(xCoord2+0.15*length*Math.cos(theta+Math.PI*0.69)), (int)(yCoord2-0.15*length*Math.sin(theta+Math.PI*0.69)), (int)xCoord2, (int)yCoord2);
        g.drawLine((int)(xCoord2+0.15*length*Math.cos(theta-Math.PI*0.69+Math.PI)), (int)(yCoord2-0.15*length*Math.sin(theta-Math.PI*0.69+Math.PI)), (int)xCoord2, (int)yCoord2);
        //g.drawLine((int)(xCoord2+0.1*length*Math.cos(theta+Math.PI*0.69)), (int)(yCoord2-0.1*length*Math.sin(theta+Math.PI*0.69)), (int)xCoord2, (int)yCoord2);
    }
}
