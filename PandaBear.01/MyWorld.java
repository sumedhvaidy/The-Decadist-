import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author Jordan Cohen
 * @version 1.0
 * 
 * Thanks to the Greenfoot team for providing the art and 
 * beginnings of this scenario.
 */
public class MyWorld extends World
{
    // level AFTER last level is the "WINNING" level
    private final int WINNING_LEVEL = 5;

    private Pengu hero;
    private int points = 0;
    private Ground[] platforms;
    private MovingSurface[] elev;
    private Enemy[] enemies;
    private Diamond[] diamonds;
    private Bomb[] bombs;
    private int levelCompletePoints;

    private int timeCount = 0;
   
    private int currentLevel;
    private GreenfootSound bgSound;
    private int score;
    
   

    /**
     * Constructor for objects of class MyWorld.
     * 
     */
    public MyWorld()
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
        checkEnemyHit();    

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
     * Calls getSurface() from the Pengu class. If the hero is standing on
     * a moving platform, and nothing is blocking their path, this method
     * will move the hero.
     */
    public void checkForcedMovement()
    {
        Actor temp = hero.getSurface();
        if (temp != null)
        {
            int currX = hero.getX();
            int currY = hero.getY();
            if (temp instanceof Brick)
            {
                Brick surfaceUnderMe = (Brick)temp;  
                int move = surfaceUnderMe.getMove();
                //hero.setLocation(currX + move, currY);
                hero.tryMove(move);
            }
            else if (temp instanceof UpDown)
            {
                UpDown surfaceUnderMe = (UpDown)temp;
                int move = surfaceUnderMe.getMove();
                hero.verticalMove(move);
            }
        }   
    }

    /**
     * This method checks for an intersecting enemy
     * If it finds either an edible enemy (good) or a killer enemy (bad)
     * it acts appropriately - either adding a point, or killing the player
     */
    public void checkEnemyHit()
    {
        Enemy hit = hero.getEnemy();
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
        ScoreBoard s = new ScoreBoard(points);
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
        ScoreBoard x = new ScoreBoard(timeCount, "You Win!", "Time:");
        addObject(x,320,200);
        Greenfoot.stop();
    }

    /**
     * Purges all of the arrays which hold the objects, thus "erasing"
     * the level.
     */
    public void purge()
    {
        if (platforms != null)
        {
            for (int i = 0; i < platforms.length; i++)
            {
                removeObject(platforms[i]);
            }
        }
        if (elev != null)
        {
            for (int i = 0; i < elev.length; i++)
            {
                removeObject(elev[i]);
            }
        }
        if (enemies != null)
        {
            for (int i = 0; i < enemies.length; i++)
            {
                removeObject(enemies[i]);
            }
        }
        if (diamonds != null)
        {
            for (int i = 0; i < diamonds.length; i++)
            {
                removeObject(diamonds[i]);
            }
        }
        if (bombs != null)
        {
            for (int i = 0; i < bombs.length; i++)
            {
                removeObject(bombs[i]);
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

            platforms = new Ground[4];
            elev = new Brick[2];
            enemies = new Enemy[5];
            diamonds = new Diamond[1];

            // Items with constructors must be done manually!
            elev[0] = new Brick(150,500);
            elev[1] = new Brick(50,550);
        

            for (int i = 0; i < platforms.length; i++)
            {
                platforms[i] = new Ground ();
            }   

            for (int i = 0; i < enemies.length; i++)
            {
                enemies[i] = new Bee();
            }
            for (int i = 0; i < diamonds.length; i++)
            {
               diamonds[i] = new Diamond();
            }

            addObject(enemies[0], 60, 159);
            addObject(enemies[1], 500, 300);
            addObject(enemies[2], 410, 60);
            addObject(enemies[3], 60, 259);
            addObject(diamonds[0], 295, 325);
            addObject(platforms[0], 100, 100);
            addObject(platforms[1], 600, 250);
            addObject(platforms[2], 410, 120);
            addObject(platforms[3], 500, 340);
            addObject(elev[0],75,200);
            addObject(elev[1],100,295);

            hero = new Pengu();
            hero.setFloor(getHeight() - 1);
            addObject(hero, 35, 35);

            return 4;
        }
        if (lvl == 2)
        {

            platforms = new Ground[4];
            elev = new Brick[2];
            enemies = new Enemy[4];
            bombs = new Bomb[1];

            for (int i = 0; i < platforms.length; i++)
            {
                platforms[i] = new Ground ();
            }   

            for (int i = 0; i < enemies.length; i++)
            {
                enemies[i] = new Bee();
            }
             for (int i = 0; i < diamonds.length; i++)
            {
               diamonds[i] = new Diamond();
            }

            elev[0] = new Brick(150,500);
            elev[1] = new Brick(50,550);
            bombs[0] = new Bomb(100,600);

            addObject(enemies[0], 48, 159);
            addObject(enemies[1], 509, 335);
            addObject(enemies[2], 486, 60);
            addObject(enemies[3], 45, 259);
            addObject(platforms[0], 320, 100);
            addObject(diamonds[0], 700, 200);
            //addObject(platforms[1], 200, 220);
            //addObject(platforms[2], 350, 180);
            //addObject(platforms[3], 100, 360);
            addObject(elev[0],75,200);
            addObject(elev[1],100,300);
            addObject(bombs[0], 300, 300);

            //hero = new Pengu();
            //hero.setFloor(getHeight() - 1);
            hero.setLocation(320,20);

            return 8;
        }
        if (lvl == 3)
        {
            //platforms = new Ground[];
            elev = new Brick[6];
            enemies = new Enemy[3];
            bombs = new Bomb[2];

            for (int i = 0; i < platforms.length; i++)
            {
                platforms[i] = new Ground ();
            }   

            for (int i = 0; i < enemies.length; i++)
            {
                enemies[i] = new Bee();
            }
             for (int i = 0; i < diamonds.length; i++)
            {
               diamonds[i] = new Diamond();
            }

            elev[0] = new Brick(50,100);
            elev[1] = new Brick(100,150);
            elev[2] = new Brick(150,200);
            elev[3] = new Brick(300,350);
            elev[4] = new Brick(355,405);
            elev[5] = new Brick(413,450);
            bombs[0] = new Bomb(50,450);
            bombs[1] = new Bomb(400,600);
            addObject(diamonds[0], 700, 200);

            addObject(elev[0],75,100);
            addObject(elev[1],125,200);
            addObject(elev[2],175,300);
            addObject(elev[3],275,100);
            addObject(elev[4],340,200);
            addObject(elev[5],384,300);

            addObject(enemies[0], 175, 250);
            addObject(enemies[1], 340, 150);
            addObject(enemies[2], 500, 100);

            addObject(bombs[0], 300, 280);
            addObject(bombs[1], 510,84);

            hero.setLocation(75,50);

            return 11;

        }

        if (lvl == 4)
        {
            //platforms = new Ground[];
            elev = new MovingSurface[1];
            enemies = new Enemy[4];
            bombs = new Bomb[2];
            platforms = new Ground[4];

            for (int i = 0; i < platforms.length; i++)
            {
                platforms[i] = new Ground ();
            }   

            for (int i = 0; i < enemies.length; i++)
            {
                enemies[i] = new Bee();
            }
            for (int i = 0; i < diamonds.length; i++)
            {
               diamonds[i] = new Diamond();
            }

            elev[0] = new UpDown(100,380);

            bombs[0] = new Bomb(50,550);
            bombs[1] = new Bomb(100,500);

            addObject(elev[0],335,200);

            addObject(enemies[0], 118, 53);
            addObject(enemies[1], 117, 308);
            addObject(enemies[2], 344, 372);
            addObject(enemies[3], 493, 308);
            addObject(diamonds[0], 700, 200);

            addObject(bombs[0], 300, 200);
            addObject(bombs[1], 200, 320);

            addObject(platforms[0], 113, 110);
            addObject(platforms[1], 168, 351);
            addObject(platforms[2], 560, 370);
            
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
            platforms = new Ground[1];
            platforms[0] = new Ground ();
            addObject(platforms[0], 320, 350);
            hero.setLocation(320,48);

            // Crude way to trigger winning message
            return 16;
        }
        return 0;
        
    }
}
