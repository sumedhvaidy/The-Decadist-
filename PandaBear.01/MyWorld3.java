import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld2 here.
 * 
 * @author Jordan Cohen
 * @version 1.0
 * 
 * Thanks to the Greenfoot team for providing the art and 
 * beginnings of this scenario.
 */
public class MyWorld3 extends World
{
    // level AFTER last level is the "WINNING" level
    public final int WINNING_LEVEL = 5;

    private Pengu3 hero;
    private int points = 0;
    private Ground3[] platforms3;
    private MovingSurface3[] elev;
    private Enemy3[] enemies3;
    private Diamond3[] diamonds3;
    private Bomb3[] bombs3;
    private int levelCompletePoints;

    private int timeCount = 0;
   
    private int currentLevel;
    private GreenfootSound bgSound;
    private int score;
    
   

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld3()
    {    
        //super(650, 400, 1);
        super(780, 360, 1);
        // Load the first level
        currentLevel = 1;
        levelCompletePoints = loadLevel(currentLevel);
        score = 0;
        showScore();
    }
    /**
     * Add some points to our current score. (May be negative.)
     * If the score falls below 0, game's up.
     */
    public void addScore(int points)
    {
        score = score + points;
        showScore();
        if (score < 0) 
        {
            Greenfoot.playSound("NewLevel.wav");
            Greenfoot.stop();
        }
    }
    /**
     * Show our current score on screen.
     */
    private void showScore()
    {
        showText("Score: " + score, 80, 25);
    }
   

    public void act()
    {
        // The world needs to take care of some business every act()

        // Forced movement results from standing on a moving platform
        checkForcedMovement();
        // Enemy hit can be a good or bad enemy (better planning would have
        // made things clearer here!)
        checkEnemy2Hit();    

        // Check for death, level completion and game completion:
        if (checkDeath())
        {
            endGame();
            Greenfoot.playSound("NewLevel.wav");
            
        }
        else if (checkLevelComplete())
        {
            // If level is complete, call purge() to remove all objects
            purge();
            // Increase level
            currentLevel++;
            // Set level clear goal
            levelCompletePoints = loadLevel(currentLevel);
            Greenfoot.playSound("LevelWin.wav");
        }
        // Crude way to "win" the game
        else if (currentLevel == WINNING_LEVEL)
        {
            winGame();
        }
        // Increment counter
        timeCount++;
    }    

    /**
     * Calls getSurface2() from the Pengu3 class. If the hero is standing on
     * a moving platform, and nothing is blocking their path, this method
     * will move the hero.
     */
    public void checkForcedMovement()
    {
        Actor temp = hero.getSurface3();
        if (temp != null)
        {
            int currX = hero.getX();
            int currY = hero.getY();
            if (temp instanceof Brick3)
            {
                Brick3 surface3UnderMe = (Brick3)temp;  
                int move = surface3UnderMe.getMove();
                //hero.setLocation(currX + move, currY);
                hero.tryMove(move);
            }
            else if (temp instanceof UpDown2)
            {
                UpDown2 surface2UnderMe = (UpDown2)temp;
                int move = surface2UnderMe.getMove();
                hero.verticalMove(move);
            }
        }   
    }

    /**
     * This method checks for an intersecting enemy
     * If it finds either an edible enemy (good) or a killer enemy (bad)
     * it acts appropriately - either adding a point, or killing the player
     */
    public void checkEnemy2Hit()
    {
        Enemy3 hit = hero.getEnemy3();
        if (hit != null)
        {
            int res = hit.getPoints();
            if (res > 0)
            {    
                points += hit.getPoints();
                removeObject(hit);
            }
            // enemies that return values under 0 are dangerous and remove HP
            // but for now, since there are no HP, Pengu dies!
            else if (res < 0)
            {
                hero.death();
            }
        }
    }

    public boolean checkDeath()
    {
        return hero.checkDeath();
    }

    public boolean checkLevelComplete()
    {
        if (points >= levelCompletePoints)
            return true;
        else
            return false;
    }

    /**
     * Triggers the losing screen - tell player how many bees they were successful
     * in eating.
     */
    public void endGame()
    {
        ScoreBoard2 s = new ScoreBoard2(points);
        addObject(s,320,200);
        Greenfoot.stop();
    }

    /**
     * Triggers the Winning screen - gives time. Time only given
     * when game is beaten! (mostly because scoreboard can't show both
     * without more significant modification).
     */
    public void winGame()
    {
        ScoreBoard2 x = new ScoreBoard2(timeCount, "You Win!", "Time:");
        addObject(x,320,200);
        Greenfoot.stop();
    }

    /**
     * Purges all of the arrays which hold the objects, thus "erasing"
     * the level.
     */
    public void purge()
    {
        if (platforms3 != null)
        {
            for (int i = 0; i < platforms3.length; i++)
            {
                removeObject(platforms3[i]);
            }
        }
        if (elev != null)
        {
            for (int i = 0; i < elev.length; i++)
            {
                removeObject(elev[i]);
            }
        }
        if (enemies3 != null)
        {
            for (int i = 0; i < enemies3.length; i++)
            {
                removeObject(enemies3[i]);
            }
        }
        if (diamonds3 != null)
        {
            for (int i = 0; i < diamonds3.length; i++)
            {
                removeObject(diamonds3[i]);
            }
        }
        if (bombs3!= null)
        {
            for (int i = 0; i < bombs3.length; i++)
            {
                removeObject(bombs3[i]);
            }
        }
        //removeObject (hero);
    }

    // temporary - to allow level skipping for testing
    // Delete after testing!
    public void increaseLevel()
    {
        points = levelCompletePoints;
    }

    /*
     * Super crude level loader. Need to figure out how to do this
     */
    public int loadLevel (int lvl)
    {
        if (lvl == 1)
        {

            platforms3 = new Ground3[4];
            elev = new Brick3[2];
            enemies3= new Enemy3[5];
            diamonds3 = new Diamond3[1];

            // Items with constructors must be done manually!
            elev[0] = new Brick3(150,500);
            elev[1] = new Brick3(50,550);
        

            for (int i = 0; i < platforms3.length; i++)
            {
                platforms3[i] = new Ground3 ();
            }   

            for (int i = 0; i < enemies3.length; i++)
            {
                enemies3[i] = new Bee3();
            }
            for (int i = 0; i < diamonds3.length; i++)
            {
               diamonds3[i] = new Diamond3();
            }

            addObject(enemies3[0], 60, 159);
            addObject(enemies3[1], 500, 300);
            addObject(enemies3[2], 410, 60);
            addObject(enemies3[3], 60, 259);
            addObject(diamonds3[0], 295, 325);
            addObject(platforms3[0], 100, 100);
            addObject(platforms3[1], 600, 250);
            addObject(platforms3[2], 410, 120);
            addObject(platforms3[3], 500, 340);
            addObject(elev[0],75,200);
            addObject(elev[1],100,295);

            hero = new Pengu3();
            hero.setFloor(getHeight() - 1);
            addObject(hero, 35, 35);

            return 4;
        }
        if (lvl == 2)
        {

            platforms3 = new Ground3[4];
            elev = new Brick3[2];
            enemies3 = new Enemy3[4];
            bombs3 = new Bomb3[1];

            for (int i = 0; i < platforms3.length; i++)
            {
                platforms3[i] = new Ground3 ();
            }   

            for (int i = 0; i < enemies3.length; i++)
            {
                enemies3[i] = new Bee3();
            }
             for (int i = 0; i < diamonds3.length; i++)
            {
               diamonds3[i] = new Diamond3();
            }

            elev[0] = new Brick3(150,500);
            elev[1] = new Brick3(50,550);
            bombs3[0] = new Bomb3(100,600);

            addObject(enemies3[0], 48, 159);
            addObject(enemies3[1], 509, 335);
            addObject(enemies3[2], 486, 60);
            addObject(enemies3[3], 45, 259);
            addObject(platforms3[0], 320, 100);
            addObject(diamonds3[0], 700, 200);
            //addObject(platforms[1], 200, 220);
            //addObject(platforms[2], 350, 180);
            //addObject(platforms[3], 100, 360);
            addObject(elev[0],75,200);
            addObject(elev[1],100,300);
            addObject(bombs3[0], 300, 300);

            //hero = new Pengu();
            //hero.setFloor(getHeight() - 1);
            hero.setLocation(320,20);

            return 8;
        }
        if (lvl == 3)
        {
            //platforms = new Ground[];
            elev = new Brick3[6];
            enemies3 = new Enemy3[3];
            bombs3 = new Bomb3[2];

            for (int i = 0; i < platforms3.length; i++)
            {
                platforms3[i] = new Ground3 ();
            }   

            for (int i = 0; i < enemies3.length; i++)
            {
                enemies3[i] = new Bee3();
            }
             for (int i = 0; i < diamonds3.length; i++)
            {
               diamonds3[i] = new Diamond3();
            }

            elev[0] = new Brick3(50,100);
            elev[1] = new Brick3(100,150);
            elev[2] = new Brick3(150,200);
            elev[3] = new Brick3(300,350);
            elev[4] = new Brick3(355,405);
            elev[5] = new Brick3(413,450);
            bombs3[0] = new Bomb3(50,450);
            bombs3[1] = new Bomb3(400,600);
            addObject(diamonds3[0], 700, 200);

            addObject(elev[0],75,100);
            addObject(elev[1],125,200);
            addObject(elev[2],175,300);
            addObject(elev[3],275,100);
            addObject(elev[4],340,200);
            addObject(elev[5],384,300);

            addObject(enemies3[0], 175, 250);
            addObject(enemies3[1], 340, 150);
            addObject(enemies3[2], 500, 100);

            addObject(bombs3[0], 300, 280);
            addObject(bombs3[1], 510,84);

            hero.setLocation(75,50);

            return 11;

        }

        if (lvl == 4)
        {
            //platforms = new Ground[];
            elev = new MovingSurface3[1];
            enemies3= new Enemy3[4];
            bombs3 = new Bomb3[2];
            platforms3 = new Ground3[4];

            for (int i = 0; i < platforms3.length; i++)
            {
                platforms3[i] = new Ground3 ();
            }   

            for (int i = 0; i < enemies3.length; i++)
            {
                enemies3[i] = new Bee3();
            }
            for (int i = 0; i < diamonds3.length; i++)
            {
               diamonds3[i] = new Diamond3();
            }

            elev[0] = new UpDown3(100,380);

            bombs3[0] = new Bomb3(50,550);
            bombs3[1] = new Bomb3(100,500);

            addObject(elev[0],335,200);

            addObject(enemies3[0], 118, 53);
            addObject(enemies3[1], 117, 308);
            addObject(enemies3[2], 344, 372);
            addObject(enemies3[3], 493, 308);
            addObject(diamonds3[0], 700, 200);

            addObject(bombs3[0], 300, 200);
            addObject(bombs3[1], 200, 320);

            addObject(platforms3[0], 113, 110);
            addObject(platforms3[1], 168, 351);
            addObject(platforms3[2], 560, 370);
            
            hero.setLocation(602,61);

            return 15;

        }
        GreenfootSound music = new GreenfootSound("Music.wav");
        if(!music.isPlaying())
        {
            music.play();
        }
        if (lvl == 5)
        {
            platforms3 = new Ground3[1];
            platforms3[0] = new Ground3 ();
            addObject(platforms3[0], 320, 350);
            hero.setLocation(320,48);

            // Crude way to trigger winning message
            return 16;
        }
        return 0;
        
    }
}
