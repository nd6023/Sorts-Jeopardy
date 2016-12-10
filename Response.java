import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.*;

public class Response extends Actor implements ActionListener {
    private static final int LINE_LENGTH = 60;
    private static GreenfootImage img;
    private Question question;
    private Type type;
    private Button right;
    private Button wrong;
    private GameManager world;
   
    public enum Type { RIGHT, WRONG, SELF }

  
    public Response(Question q, Type t) {
        setQuestion(question);
        setType(t);
        right = new Button("Right", 1);
        right.setFont(GameManager.SMALL_FONT);
        right.addActionListener(this);
        wrong = new Button("Wrong", 0);
        wrong.setFont(GameManager.SMALL_FONT);
        wrong.addActionListener(this);
    }

  
    public void act() {
        if (type != Type.SELF && (Greenfoot.isKeyDown("space")
            || Greenfoot.mouseClicked(this))) {
            world.endQuestion();
        }
    }

    public void setQuestion(Question newQuestion) {
        question = newQuestion;
    }


    public void setType(Type t) {
        type = t;
    }

 
    public void actionPerformed(GUIComponent c) {
        int id = c.getID();
        if (id == 0) {
            world.answerSelf(false);
        } else if (id == 1) {
            world.answerSelf(true);
        }
    }

    public void clear() {
        GreenfootImage image = getImage();
        image.setColor(Color.BLUE);
        image.fill();
        if (type == Type.SELF) {
            world.removeObject(right);
            world.removeObject(wrong);
        }
        world.removeObject(this);
    }

    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw();
    }

 
    private void draw() {
        final int BORDER = 30;
        String answer = question.getFirstCorrectAnswerText();
        String[] explanation =
            GameManager.wordWrap(question.getExplanation(), LINE_LENGTH);
        int amount = question.getValue();
        if (img == null) {
            World w = getWorld();
            img = new GreenfootImage(w.getWidth(), w.getHeight());
        }
        img.setColor(Color.BLUE);
        img.fill();
        img.setColor(Color.WHITE);

        String text = "";
        if (type == Type.RIGHT) {
            text = "Congratulations!";
        } else if (type == Type.WRONG) {
            text = "Sorry!";
        }
        int y = printCentered(text, BORDER + 10, GameManager.LARGE_FONT);
        y = printCentered("The correct question is...", y,
            GameManager.MED_FONT);
        if (answer != null) {
            text = "\"What is " + answer + "\"";
        } else {
            text = "unknown (no answer prepared)";
        }
        y = printCentered(text, y, GameManager.MED_FONT);
        if (type != Type.SELF) {
            text = "You ";
            if (type == Type.RIGHT) {
                text += "won";
            } else {
                text += "lost";
            }
            text += " " + amount;
            y = printCentered(text, y, GameManager.MED_FONT);
        }

        int x = BORDER;
        y += 10;
        img.setFont(GameManager.SMALL_FONT);
        Graphics g = img.getAwtImage().getGraphics();
        g.setFont(GameManager.SMALL_FONT);
        FontMetrics fm = g.getFontMetrics();
        int height = Math.round(fm.getHeight());
        for (int i = 0; i < explanation.length; i++) {
            img.drawString(explanation[i], x, y);
            y += height;
        }

        if (type != Type.SELF) {
            img.drawString("Press space or click to continue..." , x,
                img.getHeight() - BORDER);
        } else {
            img.drawString("My answer was: " , x, y + 30);
            World w = getWorld();
            w.addObject(right, x + 190, y + 23);
            w.addObject(wrong, x + 270, y + 23);
        }

        setImage(img);
    }

    private int printCentered(String line, int y, Font font) {
        final float LINE_HEIGHT_MULT = 1.15f;
        img.setFont(font);
        Graphics g = img.getAwtImage().getGraphics();
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();
        int x = img.getWidth() / 2 - fm.stringWidth(line) / 2;
        img.drawString(line, x, y);
        y += Math.round(fm.getHeight() * LINE_HEIGHT_MULT);
        return y;
    }
}
