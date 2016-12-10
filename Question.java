import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.awt.Color;

public class Question extends Actor {
    private static final GreenfootImage DEFAULT_IMG = new GreenfootImage(1, 1);
    private static final int LINE_LENGTH = 60;
    private String[] text;
    private List<Answer> answers;
    private String explanation = "";
    private int value;
    private static GreenfootImage img;
    private boolean displayAnswers = true;
    private GameManager world;

 
    public Question(String question, int questionValue) {
        text = GameManager.wordWrap(question, LINE_LENGTH);
        answers = new ArrayList<Answer>();
        value = questionValue;
    }

    public Question(final Question original) {
        text = new String[original.text.length];
        for (int i = 0; i < original.text.length; i++) {
            text[i] = original.text[i];
        }
        answers = new ArrayList(original.answers);
        explanation = original.explanation;
        value = original.value;
        img = new GreenfootImage(original.img);
    }

    public void act() {
        if ((!displayAnswers || answers.size() == 0)
            && (Greenfoot.isKeyDown("space")
            || Greenfoot.mouseClicked(this))) {
            world.selfResponse();
        }
    }

 
    public void addAnswer(String answerText, boolean correct) {
        answers.add(new Answer(answerText, correct));
    }

    public void shuffleAnswers() {
        Collections.shuffle(answers);
    }


    public int getIndex(Answer answer) {
        return answers.indexOf(answer);
    }

 
    public void setDisplayAnswers(boolean b) {
        displayAnswers = b;
    }

    public void setExplanation(String explanatoryText) {
        if (explanatoryText == null) explanatoryText = "";
        explanation = explanatoryText;
    }

    public String getExplanation() {
        return explanation;
    }


    public boolean hasExplanation() {
        return explanation != null;
    }


    public String getFirstCorrectAnswerText() {
        for (int i = 0; i < answers.size(); i++) {
            Answer answer = answers.get(i);
            if (answer.isCorrect()) {
                return answer.getText();
            }
        }
        return null; 
    }

  
    public void setValue(int newValue) {
        value = newValue;
    }

 
    public int getValue() {
        return value;
    }

  
    public String toString() {
        return "" + value + ": " + text[0];
    }

   
    public void clear() {
        getWorld().removeObjects(answers);
        for (int i = 0; i < answers.size(); i++) {
            Answer a = answers.get(i);
            a.setImage(DEFAULT_IMG);
        }
        img.setColor(Color.BLUE);
        img.fill();
        getWorld().removeObject(this);
    }

   
    public void addedToWorld(World w) {
        world = (GameManager) w;
        draw();
        world.startTimer();
    }

   
    private void draw() {
        final int BORDER = 30;
        if (img == null) {
            World w = getWorld();
            img = new GreenfootImage(w.getWidth(), w.getHeight());
        }
        img.setColor(Color.BLUE);
        img.fill();
        img.setFont(GameManager.SMALL_FONT);
        img.setColor(Color.WHITE);

        for (int i = 0; i < text.length; i++) {
            img.drawString(text[i], 30, 40 + (i * 20));
        }

        setImage(img);

        if (displayAnswers && answers.size() != 0) {
            World w = getWorld();
            for (int i = 0; i < answers.size(); i++) {
                w.addObject(answers.get(i), world.getWidth() / 2,
                    (text.length * 25 + LINE_LENGTH) + i * 40);
            }
        } else {
            img.drawString("Press space or click to continue..." , BORDER,
                img.getHeight() - BORDER);
        }
    }
}
