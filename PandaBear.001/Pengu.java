import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A small demo of a jumping movement.
 * 
 * @author M Kšlling
 * @version 1.0
 * 
 * Features Added, Updated and Improved by Jordan Cohen
 */
public class Pengu extends Mover
{
    // "physics" variables
    private int speed = 7;      // running speed (sideways)
    private int vSpeed = 0;
    private int acceleration = 1;
    private int jumpStrength = 12;
    // Important game state info
    private boolean jumping = false;
    private boolean dead = false;
    // Bottom of screen, used for death
    private int floor;
    // Variables for reducing CPU load on enemy collision detection
    private int reduceCollisionDetection = 3;
    private int currCollDetection = 0;

    /**
     * Check keyboard input and act accordingly
     */
    public void act() 
    {
        checkKeys();
        checkFall();
    }    

    private void checkKeys()
    {
        if (Greenfoot.isKeyDown("left") )
        {
            //setImage("pengu-left.png");
            // added vSpeed < 0 as an alternative condition so that Pengu ignores collisions
            // with blocks on his way UP in a jump. Makes it feel more like Mario.
            if (!(checkLeft()) || vSpeed < 0)
                moveLeft();
        }
        if (Greenfoot.isKeyDown("right") )
        {
            //setImage("pengu-right.png");
            if (!(checkRight()) || vSpeed < 0)
                moveRight();
        }
        // Avoid double tapping space by using this trick, thanks Mike!
        if (Greenfoot.isKeyDown("space")&& jumping==false )
        {
            jump();
        }
        // Temporary method for skipping levels
        if ("q".equals(Greenfoot.getKey()))
        {            
            death();
//             MyWorld m = (MyWorld)getWorld();
//             m.increaseLevel();
        }
        // Basic way to track for death by falling (floor is 1 px from the bottom, set by MyWorld)
        if (getY() == floor)
        {
            death();
        }
    }

    /**
     * Provided jump() method. Only addition is jumping = true which is part of a system which prevents
     * jumping while already mid-jump
     */
    public void jump()
    {
        jumping = true;
        vSpeed = -jumpStrength;
        fall();
    }

    /**
     * Provided checkFall() method. Updated to set jumping to false if the player is on the ground,
     * thus allowing the player to jump again.
     */
    public void checkFall()
    {
        if(onGround()) {
            vSpeed = 0;
            jumping = false;
        }
        else {
            fall();
        }
    }

    /**
     * Improved onGround method avoids falling through floors due to high fall speed. Looks
     * up to vSpeed pixes below Pengu for a platform. 
     */
    public boolean onGround()
    {
        Surface under = null;
        int counter = 1;
        int max = vSpeed;
        //int variance;
        while (counter <= max && under == null)
        {
            under = (Surface)getOneObjectAtOffset ( 0, getImage().getHeight() / 2 + counter, Surface.class);
            counter++;
        }
        // If there is a platform, correct Pengu's height so that he always lands right on the platform
        // This avoids a wierd floating effect that was present otherwise
        if (under != null)
        {
            int newY;
            if (under instanceof UpDown)
            {
                newY = under.getNewHeight();
                newY -= this.getImage().getHeight() / 2 + 2;
                System.out.println("FALL DETECTED");
            }
            else
            {
                newY = under.getY() - (under.getImage().getHeight()/2) - ((getImage().getHeight() / 2))-1;
                
                //System.out.println(under.getY() + " " + under.getImage().getHeight()/2 + " " + newY + " "  + getY());
            }
            setLocation(getX(), newY);
        }
        return under != null;
    }

    /** Actor getSurface() method
     * Returns a surface that the player is on (or null). This is used by MyWorld to do automatic
     * movement if Pengu is standing on a moving platform
     */
    public Actor getSurface()
    {
        Actor under = getOneObjectAtOffset ( 0, getImage().getHeight() / 2, Surface.class);
        int counter = -1;
        int max = vSpeed + 2;
        while (counter <= max && under == null)
        {
            under = getOneObjectAtOffset ( 0, getImage().getHeight() / 2 + counter, Surface.class);
            counter++;
        }
        return under;
    }

    /**
     * Enemy getEnemy()
     * This method checks collision with Enemy objects. Note - enemies can be good (eat them for
     * points) or bad (kill you). See Enemy class for details.
     */
    public Enemy getEnemy()
    {
        // This loop avoids checking for close enemies on every act() to avoid performance issues
        // Instead, it does it once every reduceCollisionDetection times (3 at time of writing)
        if (currCollDetection == reduceCollisionDetection)
        {
            Actor temp = getOneIntersectingObject(Enemy.class);
            Enemy enemy = (Enemy)temp;
            currCollDetection = 0;
            return enemy;
        }
        else
        {
            currCollDetection++;
        }
        return null;
    }

    /** fall()
     * Exactly as provided by sample.
     */
    public void fall()
    {
        setLocation ( getX(), getY() + vSpeed);
        vSpeed = vSpeed + acceleration;
    }

    /**
     * tryMove(int)
     * This method is called by MyWorld for the purposes of moving the player when the
     * player is standing on a moving platform.Gets a positive distance for rightwards
     * movement or a negative distance for left
     * movement. Checks for collisions with the side of surfaces, and makes sure the player
     * is allowed to move. If so, this method performs the automatic (not player controlled) 
     * moving.
     */
    public void tryMove(int dist)
    {
        if (!(checkRight()) && dist > 0 )
            setLocation(getX() + dist, getY());
        if (!(checkLeft()) && dist < 0 )
            setLocation(getX() + dist, getY());
    }

    /**
     * verticalMove moves the player when pengu is standing on an up-down elevator
     */
    public void verticalMove(int dist)
    {
        setLocation(getX(), getY() + dist);
    }

    public void moveRight()
    {
        setLocation ( getX() + speed, getY() );
    }

    public void moveLeft()
    {
        setLocation ( getX() - speed, getY() );
    }

    // Floor is used to decide if Pengu hits the ground (dies)
    // Called by MyWorld
    public void setFloor(int inFloor)
    {
        this.floor = inFloor;
    }

    // Method for dying
    public void death()
    {
        setRotation(90);
        dead = true;
    }

    /**
     * Left and then Right facing collision detection.
     * Uses a loop to check for an offset object to either side up to
     * 1/2 the height of the penguin
     */
    public boolean checkLeft()
    {
        Actor bumper = null;
        int counter = 0;
        int max = (int)(getImage().getHeight() / 2);
        while (counter < max && bumper == null)
        {
            bumper = getOneObjectAtOffset (-1*( getImage().getWidth() / 2), max - counter, Surface.class);
            counter++;
        }
        return bumper != null;
    }

    public boolean checkRight()
    {
        Actor bumper = null;
        int counter = 0;
        int max = (int)(getImage().getHeight() / 2);
        while (counter < max && bumper == null)
        {
            bumper = getOneObjectAtOffset ( getImage().getWidth() / 2, max - counter , Surface.class);
            counter++;
        }
        return bumper != null;
    }

    public boolean checkDeath()
    {
        return dead;
    }
}
