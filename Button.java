import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;


public class Button extends GUIComponent {
    
    private static final double ASCENT_MULT = .85;
   private static final Color DEFAULT_HOVER = new Color(238, 238, 238);
   private GreenfootImage up;
   private GreenfootImage down;
   private GreenfootImage hover;
   private GreenfootImage bgUp;
   private GreenfootImage bgDown;
   private GreenfootImage bgHover;
   private Color bgColorDown = Color.LIGHT_GRAY;
   private Color bgColorHover = DEFAULT_HOVER;
   private boolean hoverState;

    
    public Button() {
        this("          ", 0);
    }

    
    public Button(String labelText) {
        this(labelText, 0);
    }

    
    public Button(String text, int idNumber) {
        super(text);
        setID(idNumber);
        repaint();
    }

    
    public Button(String text, Font font, Color textColor) {
        super(text, font, textColor, DEFAULT_BG);
        repaint();
    }

    
    public Button(String text, Font font, Color textColor, Color bgColor) {
        super(text, font, textColor, bgColor);
        repaint();
    }

  
    public Button(GreenfootImage normalBG, GreenfootImage pressedBG,
            GreenfootImage hoverBG) {
        if (normalBG == null) {
            throw new IllegalArgumentException("normalBG is null.");
        }
        if (pressedBG == null) {
            throw new IllegalArgumentException("pressedBG is null.");
        }
        if (normalBG == null) {
            throw new IllegalArgumentException("hoverBG is null.");
        }
       
        bgUp = new GreenfootImage(normalBG);
        up = new GreenfootImage(normalBG);
        bgDown = new GreenfootImage(pressedBG);
        down = new GreenfootImage(pressedBG);
        bgHover = new GreenfootImage(hoverBG);
        hover = new GreenfootImage(hoverBG);
        setImage(normalBG);
        setBorder(null);
    }

   
    public void act() {
        if (isEnabled()) {
            super.act(); 
            if (Greenfoot.mouseMoved(this)) {
                setImage(hover);
            } else if (Greenfoot.mouseMoved(null)) {
                setImage(up);
            }
            if (Greenfoot.mousePressed(this)) {
                setImage(down);
            }
            if (Greenfoot.mouseClicked(null)
                    || Greenfoot.mouseDragEnded(null)) {
                setImage(up);
            }
            if (Greenfoot.mouseClicked(this)) {
                setImage(hover);
                fireActionEvent();
            }
        }
    }

    
    public boolean isPressed() {
        return down == getImage();
    }

    
    public void setSize(Dimension d) {
       
        if (bgUp != null) {
            up.scale(d.width, d.height);
            bgUp.scale(d.width, d.height);
        }
        if (bgDown != null) {
            down.scale(d.width, d.height);
            bgDown.scale(d.width, d.height);
        }
        if (bgHover != null) {
            hover.scale(d.width, d.height);
            bgHover.scale(d.width, d.height);
        }
       
        super.setSize(d);
    }

  
    public void setBackgroundPressed(Color bgDownColor) {
        bgColorDown = bgDownColor;
        repaint();
    }

    public void setBackgroundHover(Color bgHoverColor) {
        bgColorHover = bgHoverColor;
        repaint();
    }

    public void paintComponent(Graphics g) {
        hoverState = (hover == getImage());
      
        int width = getWidth();
        int height = getHeight();
        if (!isFixedSize()) {
          
            Dimension d = getTextDimension(getText(), g);
            width = d.width;
            height = d.height;
        }

        if (bgUp == null) {
            up = prepareImage(up, getBackground(), width, height);
        } else {
            up.drawImage(bgUp, 0, 0);
        }
     
        if (bgDown == null) {
            down = prepareImage(down, bgColorDown, width, height);
        } else {
            down.drawImage(bgDown, 0, 0);
        }
      
        if (bgHover == null) {
            hover = prepareImage(hover, bgColorHover, width, height);
        } else {
            hover.drawImage(bgHover, 0, 0);
        }
       
        setImage(up);
    }

  
    private GreenfootImage prepareImage(GreenfootImage img, Color bgColor,
            int imgWidth, int imgHeight) {
       
        if (img == null || img.getWidth() != imgWidth
                || img.getHeight() != imgHeight || bgColor.getAlpha() < 255) {
            img = new GreenfootImage(imgWidth, imgHeight);
        }
        img.setColor(bgColor);
        img.fill();
        return img;
    }

   
    protected void paintBorder(Graphics g) {
        super.paintBorder(up.getAwtImage().getGraphics());
        super.paintBorder(down.getAwtImage().getGraphics());
        super.paintBorder(hover.getAwtImage().getGraphics());
    }

    public void paintText(Graphics g) {
        String btnText = getText();
        if (btnText == null || btnText.length() == 0) return;
        String[] lines = splitLines(btnText, g);

    
        if (getFont() != null) up.setFont(getFont());
        if (getForeground() != null) up.setColor(getForeground());
        printText(up, lines, 0);

        if (getFont() != null) down.setFont(getFont());
        if (getForeground() != null) down.setColor(getForeground());
        printText(down, lines, 1);

        if (getFont() != null) hover.setFont(getFont());
        if (getForeground() != null) hover.setColor(getForeground());
        printText(hover, lines, 0);

        if (hoverState) {
            setImage(hover);
        } else {
            setImage(up);
        }
    }

   
    private void printText(GreenfootImage img, String[] lines, int offset) {
        Graphics g = img.getAwtImage().getGraphics();
        if (getFont() != null) g.setFont(getFont());
        FontMetrics fm = g.getFontMetrics();
        int lineHeight = (int) (fm.getHeight() * ASCENT_MULT);
        int height = lineHeight * lines.length;
        int width = 0;
        int y = lineHeight + (img.getHeight() - height - fm.getDescent()) / 2;
        for (int i = 0; i < lines.length; i++) {
            int lineWidth = fm.stringWidth(lines[i]);
            width = Math.max(width, lineWidth);
            int x = (img.getWidth() - lineWidth) / 2;
            img.drawString(lines[i], x + offset, y + offset);
            y += lineHeight;
        }
        if (isFocusOwner()) {
            img.setColor(Color.GRAY);
            int x = (img.getWidth() - width) / 2 - 1;
            y = (img.getHeight() - height) / 2;
            img.drawRect(x + offset, y + offset, width + 1, height);
        }
    }
}
