import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;

public class OverScreen extends Actor implements ActionListener {
    private static final Font OVER_FONT =
    GameManager.LARGE_FONT.deriveFont(52f);
    private int score;
    private GameManager world;
    private Button yesBtn;
    private Button noBtn;
    private Label againLabel;
    private Label finalMsg;
    private Label g, a, m, e, o, v, e2, r;
    private boolean playedFinal;

   
    public OverScreen(int finalScore, boolean playedFinalRound) {
        score = finalScore;
        playedFinal = playedFinalRound;
    }

  
    public void actionPerformed(GUIComponent c) {
        if (c == yesBtn) {
            world.startGame();
        } else if (c == noBtn) {
            Greenfoot.stop();
        }
    }

   
    public void clear() {
        world.removeObject(againLabel);
        world.removeObject(finalMsg);
        world.removeObject(yesBtn);
        world.removeObject(noBtn);
        world.removeObject(g);
        world.removeObject(a);
        world.removeObject(m);
        world.removeObject(e);
        world.removeObject(o);
        world.removeObject(v);
        world.removeObject(e2);
        world.removeObject(r);
        world.removeObject(this);
    }

   
    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw();
    }

   
    private void draw() {
        final int Y_ROW1 = 190;
        final int Y_ROW2 = 260;
        final int X_COL1 = 162;
        final int X_COL2 = 255;
        final int X_COL3 = 349;
        final int X_COL4 = 442;
        g = new Label("G", OVER_FONT, Color.WHITE);
        a = new Label("A", OVER_FONT, Color.WHITE);
        m = new Label("M", OVER_FONT, Color.WHITE);
        e = new Label("E", OVER_FONT, Color.WHITE);
        o = new Label("O", OVER_FONT, Color.CYAN);
        v = new Label("V", OVER_FONT, Color.CYAN);
        e2 = new Label("E", OVER_FONT, Color.CYAN);
        r = new Label("R", OVER_FONT, Color.CYAN);
        world.addObject(g, X_COL1, Y_ROW1);
        world.addObject(a, X_COL2, Y_ROW1);
        world.addObject(m, X_COL3, Y_ROW1);
        world.addObject(e, X_COL4, Y_ROW1);
        world.addObject(o, X_COL1, Y_ROW2);
        world.addObject(v, X_COL2, Y_ROW2);
        world.addObject(e2, X_COL3, Y_ROW2);
        world.addObject(r, X_COL4, Y_ROW2);
        String msg = "Thanks for playing, you won " + score + "!";
        if (!playedFinal && score <= 0) {
            msg = "Sorry, you need a positive score to play the final round.";
        } else if (score <= 0) {
            msg = "Thanks for playing!";
        }
        finalMsg = new Label(msg, GameManager.LARGE_FONT, Color.YELLOW);
        finalMsg.setSize(new Dimension(world.getWidth() - 60, 100));
        world.addObject(finalMsg, world.getWidth() / 2, 125);

        againLabel = new Label("Play another round?",
            GameManager.LARGE_FONT, Color.YELLOW);
        world.addObject(againLabel, world.getWidth() / 2,
            world.getHeight() - 126);

        yesBtn = new Button("Yes", GameManager.LARGE_FONT, Color.BLACK);
        yesBtn.addActionListener(this);
        world.addObject(yesBtn, 256, world.getHeight() - 53);
        noBtn = new Button("No", GameManager.LARGE_FONT, Color.BLACK);
        noBtn.addActionListener(this);
        world.addObject(noBtn, 350, world.getHeight() - 53);
    }
}
