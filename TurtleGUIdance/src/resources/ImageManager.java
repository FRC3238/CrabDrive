package resources;

import java.awt.Image;
import java.util.ArrayList;


public class ImageManager 
{
    static ArrayList<ImageResource> gameImages;
    
    static public ImageManager staticImageManager = new ImageManager();
    
    public ImageManager()
    {
        
    }
    
    static public void populateImageList()
    {
        gameImages = new ArrayList<ImageResource>();
        
        gameImages.add(new ImageResource("sprField",ResourceLoader.getImage("sprField.png")));
        gameImages.add(new ImageResource("sprIconSave",ResourceLoader.getImage("sprIconSave.png")));
        gameImages.add(new ImageResource("sprIconOpen",ResourceLoader.getImage("sprIconOpen.png")));
        gameImages.add(new ImageResource("sprTurtle",ResourceLoader.getImage("sprTurtle.png")));
    }
    
    public Image getImage(String name)
    {
        Image temp = null;
        
        for(int i = 0; i <gameImages.size(); i++)
        {
            if(name.equals(gameImages.get(i).getName()))
            {
                temp = gameImages.get(i).getImage();
                break;
            }
        }
        
        return temp;
    }
}
