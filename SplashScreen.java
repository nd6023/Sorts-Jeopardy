import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Displays flashing screens.
 *
 * @author Neha Parmar
 * @version 
 */
public class SplashScreen extends Actor {
    public static final int FLASH_WIDTH = 85;
    public static final int FLASH_HEIGHT = 61;
    public static final Color FLASH_COLOR = new Color(0, 0x99, 0xFF);
  
    private static GreenfootImage onImage;
    private static GreenfootImage offImage;

    private static final long DELAY = 70;
    private long lastFlashTime;
    private GameManager world;
    private Label title;
    private Flasher[][] grid = new Flasher[GameManager.ROWS][GameManager.COLS];

    public SplashScreen() { }

    public void act() {
        if (System.currentTimeMillis() - lastFlashTime < DELAY) {
            return;
        }
        lastFlashTime = System.currentTimeMillis();
        int row = Greenfoot.getRandomNumber(GameManager.ROWS);
        int col = Greenfoot.getRandomNumber(GameManager.COLS);
        grid[row][col].startFlash(100);
    }

    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw();
    }


    public void clear() {
        world.removeObject(title);
        for (int row = 0; row < GameManager.ROWS; row++) {
            for (int col = 0; col < GameManager.COLS; col++) {
                world.removeObject(grid[row][col]);
            }
        }
        world.removeObject(this);
    }

    private void draw() {
        if (onImage == null) {
            onImage = new GreenfootImage(FLASH_WIDTH, FLASH_HEIGHT);
            onImage.setColor(FLASH_COLOR);
            onImage.fill();
        }
        if (offImage == null) {
            offImage = new GreenfootImage(FLASH_WIDTH, FLASH_HEIGHT);
        }
        title = new Label(new GreenfootImage("greenfoot_jeopardy.gif"));
        world.addObject(title, 302, 45);
        for (int row = 0; row < GameManager.ROWS; row++) {
            for (int col = 0; col < GameManager.COLS; col++) {
                grid[row][col] = new Flasher();
                world.addObject(grid[row][col], 66 + 94 * col, 128 + row * 68);
            }
        }
    }

    public class Flasher extends Actor {
        private long flashTime;
        private long startTime;

        public Flasher() {
            setImage(offImage);
        }

   
        public void act() {
            if (System.currentTimeMillis() - startTime < flashTime) {
                return;
            }
            setImage(offImage);
        }

      
        public void startFlash(long duration) {
            flashTime = duration;
            setImage(onImage);
            startTime = System.currentTimeMillis();
        }
    }
}
