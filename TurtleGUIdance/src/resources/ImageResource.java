package resources;

import java.awt.Image;

public class ImageResource 
{
    Image myImage;
    String myName;
    
    public ImageResource(String name, Image image)
    {
        myName = name;
        myImage = image;
    }
    
    public String getName()
    {
        return myName;
    }
    
    public Image getImage()
    {
        return myImage;
    }
}
