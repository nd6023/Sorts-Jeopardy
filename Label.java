import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;


public class Label extends GUIComponent {
    
    private static final double ASCENT_MULT = .90;
    private static final Color TRANSPARENT = new Color(0, 0, 0, 0);
   private boolean hasBGImage;

    public Label(GreenfootImage img) {
        hasBGImage = true;
        setImage(img);
        setFocusable(false);
    }

   
    public Label(String text) {
        this (text, DEFAULT_FONT, DEFAULT_FG);
    }

   
    public Label(String text, Font font) {
        this (text, font, DEFAULT_FG);
    }

  
    public Label(String text, Color textColor) {
        this (text, DEFAULT_FONT, textColor);
    }

    
    public Label(String text, Font font, Color textColor) {
        super(text, font, textColor, TRANSPARENT);
        setFocusable(false);
        setBorder(null); 
    }

  
    public Label(String text, Font font, Color textColor, Color bgColor) {
        super(text, font, textColor, bgColor);
        setFocusable(false);
        repaint();
    }

   
    public void paintComponent(Graphics g) {
        if (hasBGImage) return;
        GreenfootImage img = null;
        if (isFixedSize()) {
            img = new GreenfootImage(getWidth(), getHeight());
        } else {
            Dimension d = getTextDimension(getText(), g);
            img = new GreenfootImage(d.width, d.height);
        }
        if (getBackground() != TRANSPARENT) {
            img.setColor(getBackground());
            img.fill();
        }
        setImage(img);
    }

   
    public void paintText(Graphics g) {
        String labelText = getText();
        if (labelText == null || labelText.length() == 0) return;
        String[] lines = splitLines(labelText, g);
        GreenfootImage img = getImage();
        if (getFont() != null) img.setFont(getFont());
        if (getForeground() != null) img.setColor(getForeground());

        FontMetrics fm = g.getFontMetrics();
        int lineHeight = (int) (fm.getHeight() * ASCENT_MULT);
        int y = lineHeight + (getHeight() - (lineHeight * lines.length)
            - fm.getDescent()) / 2;
        for (int i = 0; i < lines.length; i++) {
            int x = getWidth() / 2 - fm.stringWidth(lines[i]) / 2;
            img.drawString(lines[i], x, y);
            y += lineHeight;
        }
    }
}
