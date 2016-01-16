import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bomb  extends Enemy
{
    private int direction = 1;
    private int speed = 3;
    private int max, min;
    
    public Bomb (int mn, int mx)
    {
        this.min = mn;
        this.max = mx;
    }
    
    public void act() 
    {
        move(speed * direction);
        if (getX() >= max)
            direction = direction * -1;
        else if (getX() <= min)
            direction = direction * -1;
            
    }    
    public int getPoints()
    {
        return -1;
    }
    
}
