import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;


public class BetScreen extends Actor implements ActionListener {
    private static final int ERROR_LOC_X = 300;
    private static GreenfootImage img;
    private String category;
    private TextField betTF;
    private Button done;
    private GameManager world;
    private Label errorMsg;


    public BetScreen(String cat) {
        category = cat;
    }

    public void clear() {
        if (betTF != null) world.removeObject(betTF);
        if (done != null) world.removeObject(done);
        if (errorMsg != null) world.removeObject(errorMsg);
        world.removeObject(this);
    }

    public void actionPerformed(GUIComponent c) {
        int bet = 0;
        try {
            bet = Integer.parseInt(betTF.getText());
        } catch (NumberFormatException nfe) {
            printErrorMessage("Please enter an integer number.");
            return;
        }
        if (bet < 0) {
            printErrorMessage("Enter a number greater than zero.");
        } else if (bet > world.getScore()) {
            printErrorMessage("You cannot bet more than your score.");
        } else {
            world.finalRound(bet);
        }
    }

    public void printErrorMessage(String msg) {
        if (errorMsg == null) {
            errorMsg = new Label(msg, GameManager.MED_FONT, Color.WHITE);
            world.addObject(errorMsg, ERROR_LOC_X, world.getWidth() / 2);
        } else {
            errorMsg.setText(msg);
        }
    }

    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw();
    }


    private void draw() {
        final int BORDER = 30;
        img = new GreenfootImage(world.getWidth(), world.getHeight());
        img.setColor(Color.BLUE);
        img.fill();
        img.setColor(Color.WHITE);

        String text = "Final Round";
        int y = printCentered(text, GameManager.LARGE_FONT, 0, BORDER);

        if (category != null && !category.equals("")) {
            text = "Category: " + category;
            y = printCentered(text, GameManager.MED_FONT, 0, y);
        }

        text = "Your current score is " + world.getScore();
        y = printCentered(text, GameManager.MED_FONT, 0, y);

        text = "Enter your bet: ";
        y = printCentered(text, GameManager.MED_FONT, -50, y);
        setImage(img);

        betTF = new TextField("", 4, GameManager.MED_FONT);
        betTF.addActionListener(this);
        betTF.requestFocus();
        world.addObject(betTF, 392, 184);
        done = new Button("Continue", GameManager.MED_FONT, Color.BLACK,
            Color.CYAN);
        done.addActionListener(this);
        world.addObject(done, 311, 240);
    }


    private int printCentered(String line, Font font, int x, int y) {
        img.setFont(font);
        Graphics g = img.getAwtImage().getGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        x += (img.getWidth() - fm.stringWidth(line)) / 2;
        y += Math.round(fm.getHeight() * 1.2f);
        img.drawString(line, x, y);
        return y;
    }
}
