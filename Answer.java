import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;


public class Answer extends Actor {
    private static final int WIDTH = 500;
    private static final int HEIGHT = 25;
    private static final int ASCENT = 20;
    private String text;
    private boolean correct;
    private GameManager world;

    public Answer(String answerText, boolean answerCorrect) {
        text = answerText;
        correct = answerCorrect;
    }


    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            if (correct) {
                world.answerResponse(true);
            } else {
                world.answerResponse(false);
            }
        }

        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return;

        if (mouse.getActor() == this) {
            draw(Color.GREEN);
        } else {
            draw(Color.WHITE);
        }
    }


    public boolean isCorrect() {
        return correct;
    }


    public String getText() {
        return text;
    }


    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw(Color.WHITE);
    }


    private void draw(Color colour) {
        Question question = world.getCurrentQuestion();
        int answerNumber = question.getIndex(this);
        String answerID = Character.toString((char) ('a' + answerNumber));
        GreenfootImage image = new GreenfootImage(WIDTH, HEIGHT);
        image.setColor(colour);
        image.setFont(GameManager.SMALL_FONT);
        image.drawString(answerID + ". " + text, 0, ASCENT);
        setImage(image);
    }
}
