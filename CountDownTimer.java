import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;


public class CountDownTimer extends Actor {
    private static final double ASCENT_MULT = .80;
    private static final int WIDTH = 40;
    private static final int HEIGHT = 50;
    private static final int MS_SEC = 1000;
    private static final int DISPLAY_TIME = 5;

    private long delay;
    private Timer timer;
    private int count;
    private boolean running;
    private final Color textColor = Color.WHITE;
    private final Font font = new Font("SansSerif", Font.BOLD, 48);

    public CountDownTimer(int seconds) {
        setDuration(seconds);
        setImage(new GreenfootImage(WIDTH, HEIGHT));
    }

    public void setDuration(int seconds) {
        delay = (seconds - DISPLAY_TIME) * MS_SEC;
    }


    public void start() {
        timer = new Timer(true);
        timer.scheduleAtFixedRate(new Task(), delay, MS_SEC);
        count = DISPLAY_TIME;
        running = true;
    }

  
    public boolean isRunning() {
        return running;
    }


    public void stop() {
        running = false;
        if (timer != null) timer.cancel();
        getImage().clear();
    }

    private class Task extends TimerTask {
     
        public void run() {
            GreenfootImage img = getImage();
            img.clear();
            if (count <= 0) {
                stop();
                ((GameManager) getWorld()).timeout();
            } else {
                Graphics g = getImage().getAwtImage().getGraphics();
                g.setFont(font);
                FontMetrics fm = g.getFontMetrics();
                img.setColor(Color.WHITE);
                img.fill();
                img.setColor(Color.BLACK);
                img.setFont(font);
                String text = "" + count;
                int x = img.getWidth() / 2 - fm.stringWidth(text) / 2;
                int y = img.getHeight() / 2 + (int) (fm.getAscent()
                    * ASCENT_MULT) / 2;
                img.drawString(text, x, y);
            }
            setImage(img);
            count--;
        }
    }
}
