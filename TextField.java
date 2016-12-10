import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;


public class TextField extends GUIComponent {
    private static final Color CARET_COLOR = Color.RED;
    private static final Color HIGHLIGHT_COLOR = Color.PINK;
    private static final long KEYSTOKE_DELAY = 110;
    private static final long BLINK_DELAY = 400;
    private static final int X_PAD = 1;
    private StringBuilder input;
    private int numCols;
    private TextLayout layout = null;
    private boolean validTextLayout = false;
    private long lastKeystrokeTime;
    private int index1 = 0;
    private int index2;
    private float originX;
    private float originY;
    private GreenfootImage cursorOn;
    private GreenfootImage cursorOff;
    private long lastBlinkTime;
    private boolean blinkOn;


   
    public TextField() {
        this ("", 1);
    }

 
    public TextField(String text) {
        this (text, 1);
    }

  
    public TextField(int columns) {
        this ("", columns);
    }

    public TextField(String text, int columns) {
        this (text, columns, DEFAULT_FONT, DEFAULT_FG, Color.WHITE);
    }


    public TextField(String text, int columns, Font font) {
        this (text, columns, font, DEFAULT_FG, Color.WHITE);
    }

  
    public TextField(String text, int columns, Font font, Color fg, Color bg) {
        super(text, font, fg, bg, null);
        numCols = columns;
        input = new StringBuilder(text);
        super.repaint();
        originX = getInsets().left + X_PAD;
    }

  
    public void act() {
        super.act(); 
        if (!isEnabled()) return;

        
        if (Greenfoot.mousePressed(this)) {
            MouseInfo e = Greenfoot.getMouseInfo();
            if (e != null && layout != null) {
                index1 = getHitPosition(e.getX(), e.getY());
                index2 = index1;
            }
           
            super.repaint();
        }

        if (Greenfoot.mouseDragged(this)) {
            MouseInfo e = Greenfoot.getMouseInfo();
            if (e != null && layout != null) {
                index2 = getHitPosition(e.getX(), e.getY());
            }

            super.repaint();
        }

        if (!isFocusOwner()) return;


        if (System.currentTimeMillis() - lastBlinkTime > BLINK_DELAY) {
            lastBlinkTime = System.currentTimeMillis();
            blinkOn = !blinkOn;
            if (blinkOn) {
                setImage(cursorOn);
            } else {
                setImage(cursorOff);
            }
        }

        if (System.currentTimeMillis() - lastKeystrokeTime < KEYSTOKE_DELAY) {
            return;
        }
        lastKeystrokeTime = System.currentTimeMillis();

        TextHitInfo newPosition = null;
        String key = Greenfoot.getKey();
        if (key != null && key.length() == 1) {
       
            deleteHighlighedChars();
            input.insert(index1, key);
            index1++;
            index2 = index1;
            repaint();
        } else if (Greenfoot.isKeyDown("space")) {
            deleteHighlighedChars();
            input.insert(index1, " ");
            index1++;
            index2 = index1;
            repaint();
        } else if (Greenfoot.isKeyDown("backspace")) {
            if (index1 != index2) {
                deleteHighlighedChars();
                repaint();
            } else if (input.length() > 0 && index1 > 0) {
                --index1;
                input.deleteCharAt(index1);
                index2 = index1;
                repaint();
            }
        } else if (Greenfoot.isKeyDown("enter")) {
            fireActionEvent();
        } else if (Greenfoot.isKeyDown("right") && layout != null) {
            newPosition = layout.getNextRightHit(index2);
        } else if (Greenfoot.isKeyDown("left") && layout != null) {
            newPosition = layout.getNextLeftHit(index1);
        }
        if (newPosition != null) {
            index1 = newPosition.getInsertionIndex();
            index2 = index1;
            super.repaint();
        }
    }

    public void setText(String newText) {
        if (newText == null) {
            input.replace(0, input.length(), "");
        } else {
            input.replace(0, input.length(), newText);
        }
        index1 = 0;
        index2 = index1;
        repaint();
    }

 
    public String getText() {
        return input.toString();
    }


    public void setColumns(int columns) {
        int oldVal = numCols;
        if (columns < 0) {
            throw new IllegalArgumentException("columns less than zero.");
        }
        if (numCols != oldVal) {
            numCols = columns;
            repaint();
        }
    }


    public int getColumns() {
        return numCols;
    }


    public void repaint() {
        validTextLayout = false;
        super.repaint();
    }


    public void paintComponent(Graphics g) {
        GreenfootImage img = null;
        if (isFixedSize()) {
            img = new GreenfootImage(getWidth(), getHeight());
        } else {
            
            FontMetrics fm = g.getFontMetrics();
            String text = getText();
            if (text == null) text = "";
            int width = fm.charWidth('m') * numCols;
            if (width <= 0) width = 1;
            int height = fm.getHeight();
            Insets insets = getInsets();
            if (insets != null) {
                width += insets.left + insets.right;
                height += insets.top + insets.bottom;
            }
            img = new GreenfootImage(width, height);
        }
        if (getBackground().getTransparency() > 0) {
            img.setColor(getBackground());
            img.fill();
        }
        if (getBorder() == null) {
            img.setColor(Color.BLACK);
            img.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);
            
            if (isFocusOwner()) {
                img.drawRect(1, 1, img.getWidth() - 3, img.getHeight() - 3);
            }
        }
        setImage(img);
    }

  
    public void paintText(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        TextLayout tl = getTextLayout(g2);
        if (tl != null) {
            scrollToVisible();
            AffineTransform at
                = AffineTransform.getTranslateInstance(originX, originY);

            if (isFocusOwner()) {
                Shape hilight = tl.getLogicalHighlightShape(index1, index2);
                hilight = at.createTransformedShape(hilight);
                g2.setColor(HIGHLIGHT_COLOR);
                g2.fill(hilight);
            }
            if (getForeground() != null) g2.setColor(getForeground());
            tl.draw(g2, originX, originY);
            cursorOff = new GreenfootImage(getImage());
            if (isFocusOwner()) {
              
                Shape[] carets = tl.getCaretShapes(index2);
                g2.setColor(CARET_COLOR);
                Shape caret = at.createTransformedShape(carets[0]);
                g2.draw(caret);
            }
            cursorOn = getImage();
        } else if (isFocusOwner()) {
            cursorOff = new GreenfootImage(getImage());
            g2.setColor(CARET_COLOR);
            Insets i = getInsets();
            g2.drawLine(i.left + X_PAD, i.top, i.right + X_PAD,
                getHeight() - i.bottom);
            cursorOn = getImage();
        }
    }

    private void scrollToVisible() {
        if (layout == null) return;
        Insets i = getInsets();
        int xMin = i.left + X_PAD;
        int xMax = getWidth() - i.right - X_PAD;
        int cursorX = layout.getCaretShapes(index2)[0].getBounds().x;
        float adjustX = layout.getAdvance() - xMax + originX;
        if (originX < xMin && adjustX < 0) {
            originX = originX - adjustX;
            if (originX > xMin) originX = xMin;
        }
        if (cursorX < xMin - originX) {
            originX = xMin - cursorX;
        } else if (cursorX > xMax - originX) {
        originX = xMax - cursorX;
        }
    }

 
    private TextLayout getTextLayout(Graphics g) {
        if (!validTextLayout) {
            layout = null;
            if (input.length() > 0) {
                Graphics2D g2 = (Graphics2D) g;
                FontRenderContext frc = g2.getFontRenderContext();
                layout = new TextLayout(getText(), getFont(), frc);
                originY = (getHeight() + layout.getAscent()
                           - layout.getDescent()) / 2;
            }
        }
        validTextLayout = true;
        return layout;
    }

  
    private int getHitPosition(int mouseX, int mouseY) {
        float x = mouseX - getX() + getWidth() / 2 - originX;
        float y = mouseY - getY() + getHeight() / 2;
        TextHitInfo hit = layout.hitTestChar(x, y);
        return hit.getInsertionIndex();
    }

    private void deleteHighlighedChars() {
        if (index2 > index1) {
            input.delete(index1, index2);
            index2 = index1;
        } else if (index1 > index2) {
            input.delete(index2, index1);
            index1 = index2;
        }
    }
}
