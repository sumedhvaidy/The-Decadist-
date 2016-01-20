import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Brick here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Brick  extends MovingSurface
{
    private int X;
    private int direction = -1;
    private static final int SPEED = 3;
    private int maxX, minX;

    // Create a new Brick with a min and max X
    public Brick (int mnx, int mx)
    {
        this.maxX = mx;
        this.minX = mnx;
    }
    
    /**
     * Act - do whatever the Brick wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        X = getX();
        if (direction == 1)
        {
            //if (X < getWorld().getWidth()-1)
            if (X < maxX)
              setLocation (X + SPEED, getY());
            else
              direction = -1;
        }
        else if (direction == -1)
        {
            if (X >= minX)
                setLocation (X - SPEED, getY());
            else
                direction = 1;
             
        }
    }    
    public int getMove ()
    {
        return (direction * (SPEED));
    }
      public int getNewHeight() {return 0;}
}
