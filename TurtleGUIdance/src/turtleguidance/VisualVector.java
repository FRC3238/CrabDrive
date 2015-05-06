/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package turtleguidance;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
    public boolean beingManipulated;
    
    public static int boundX;
    public static int boundY;
    
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
        beingManipulated = false;
    }
    
    
    public void setTailingVector(VisualVector v)
    {
        tailingVector = v;
    }
    
    public void setNextVector(VisualVector v)
    {
        nextVector = v;
    }
    
    public void step()
    {
    	if(beingManipulated == false)
    	{
	    	if(tailingVector!=null)
	    	{
	    		xCoord1 = tailingVector.xCoord2;
	    		yCoord1 = tailingVector.yCoord2;
	    	}
	    	
	    	if(nextVector!=null)
	    	{
	    		xCoord2 = nextVector.xCoord1;
	    		yCoord2 = nextVector.yCoord1;
	    	}
	    	
	    	int range = 3;
	    	if(xCoord1 < range)
	    	{
	    		xCoord1 = range;
	    	}
	    	if(xCoord2 < range)
	    	{
	    		xCoord2 = range;
	    	}
	    	if(yCoord1 < range)
	    	{
	    		yCoord1 = range;
	    	}
	    	if(yCoord2 < range)
	    	{
	    		yCoord2 = range;
	    	}
	    	
	    	range = boundX;
	    	if(xCoord1 > range)
	    	{
	    		xCoord1 = range;
	    	}
	    	if(xCoord2 > range)
	    	{
	    		xCoord2 = range;
	    	}
	    	
	    	range = boundY;
	    	
	    	if(yCoord1 > range)
	    	{
	    		yCoord1 = range;
	    	}
	    	if(yCoord2 > range)
	    	{
	    		yCoord2 = range;
	    	}
    	}
    	
        theta = Math.atan2(xCoord2-xCoord1, yCoord2-yCoord1);
        length = Math.sqrt(Math.pow(xCoord2-xCoord1, 2) + Math.pow(yCoord2-yCoord1, 2));
    }
    
    public void draw(Graphics g)
    {
    	Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(6));
        g2.drawLine((int)xCoord1, (int)yCoord1, (int)xCoord2, (int)yCoord2);
        g2.drawLine((int)(xCoord2+0.15*50*Math.cos(theta+Math.PI*0.69)), (int)(yCoord2-0.15*50*Math.sin(theta+Math.PI*0.69)), (int)xCoord2, (int)yCoord2);
        g2.drawLine((int)(xCoord2+0.15*50*Math.cos(theta-Math.PI*0.69+Math.PI)), (int)(yCoord2-0.15*50*Math.sin(theta-Math.PI*0.69+Math.PI)), (int)xCoord2, (int)yCoord2);
        //g.drawString(Double.toString(theta), (int)xCoord1, (int)yCoord1);
    }
    
    public void generateCoordinates(double newLength, double newTheta)
    {
    	xCoord2 = xCoord1+Math.cos(newTheta)*length;
    	yCoord2 = yCoord1-Math.sin(newTheta)*length;
    }
}
