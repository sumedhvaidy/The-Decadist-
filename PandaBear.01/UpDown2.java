import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class UpDown here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UpDown2  extends MovingSurface2
{
    private int Y;
    private int direction = -1;
    private static final int SPEED = 3;
    private int maxY, minY;
    
    // Generic constructor for testing
    public UpDown2 ()
    {
        this.maxY = 300;
        this.minY = 100;
    }
    
    // Create a new Brick with a min and max X
    
    public UpDown2 (int mny, int my)
    {
        this.maxY = my;
        this.minY = mny;
    }
    
    /**
     * Act - do whatever the UpDown wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        Y = getY();
        if (direction == 1)
        {
            //if (X < getWorld().getWidth()-1)
            if (Y < maxY)
              setLocation (getX(), Y + SPEED);
            else
              direction = -1;
        }
        else if (direction == -1)
        {
            if (Y >= minY)
                setLocation (getX(), Y - SPEED);
            else
                direction = 1;
             
        }
        // Check if player is standing on top of the elevator. If so, move the player
        // the same distance as the elevator
        Pengu p = null;
        int max = getImage().getWidth() / 2;
        int counter = 0;
//         while (p == null && counter < max)
//         {
//             p = (Pengu)getOneObjectAtOffset(max - counter, -1-getImage().getHeight()/2, Pengu.class);
//             counter++;
//         }
//         if (p != null)
//         {
//             p.verticalMove(this.getMove());
//         }
    }    
    public int getMove ()
    {
        return (direction * (SPEED));
    }  
    public int getNewHeight()
    {
        return this.getY() - (getImage().getHeight() / 2);
    }
}
