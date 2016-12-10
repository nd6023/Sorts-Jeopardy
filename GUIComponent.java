import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public abstract class GUIComponent extends Actor {
    public static final Font DEFAULT_FONT = new Font("Dialog", Font.PLAIN, 12);
    public static final Color DEFAULT_FG = Color.BLACK;
    public static final Color DEFAULT_BG = new Color(204, 204, 204);
   
    private static final Insets DEFAULT_INSETS = new Insets(1, 3, 2, 3);

    private int id;
    private boolean enabled = true;
    private boolean visible = false;
    private String text = "";
    private Color foreground = DEFAULT_FG;
    private Color background = DEFAULT_BG;
    private Dimension size;
    private Font font = DEFAULT_FONT;
    private Border border = new LineBorder();
    private List<ActionListener> listeners = new ArrayList<ActionListener>();

    private static GUIComponent focusOwner;

    private boolean focusable = true;

   
    public GUIComponent() { }

  
    public GUIComponent(String str) {
        text = str;
        if (text == null) text = "";
    }

    
    public GUIComponent(String str, Font f, Color fg, Color bg) {
        text = str;
        if (text == null) text = "";
        font = f;
        foreground = fg;
        background = bg;
    }

   
    public GUIComponent(String str, Font f, Color fg, Color bg, Border b) {
        text = str;
        if (text == null) text = "";
        font = f;
        foreground = fg;
        background = bg;
        border = b;
    }

  
    public void act() {
        if (Greenfoot.mousePressed(this) && !isFocusOwner()) {
            requestFocus();
        }
    }

   
    public void setID(int idNumber) {
        id = idNumber;
    }

  
    public int getID() {
        return id;
    }

 
    public void setEnabled(boolean b) {
        enabled = b;
        if (enabled) {
            repaint();
        } else if (!enabled) {
            makeDisabledImage(getImage());
        }
    }

   
    private static void makeDisabledImage(GreenfootImage img) {
        if (img == null) return;
        final float RED_LUMINANCE = 0.229f;
        final float GREEN_LUMINANCE = 0.587f;
        final float BLUE_LUMINANCE = 0.114f;
        final int RGB_MIN = 148;
        final int RGBA_MAX = 255;
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                Color c = img.getColorAt(x, y);
                if (c.getAlpha() >= RGBA_MAX) {
                    float red = c.getRed() * RED_LUMINANCE;
                    float green = c.getGreen() * GREEN_LUMINANCE;
                    float blue = c.getBlue() * BLUE_LUMINANCE;
                    int luminance = (int) (red + green + blue);
                    if (luminance < RGB_MIN) luminance = RGB_MIN;
                    Color color = new Color(luminance, luminance, luminance);
                    img.setColorAt(x, y, color);
                }
            }
        }
    }

  
    public boolean isEnabled() {
        return enabled;
    }

   
    public void setText(String newText) {
        String oldText = text;
        text = newText;
        if (text == null) text = "";
        if (text != oldText) repaint();
    }

 
    public String getText() {
        return text;
    }

 
    public void setFont(Font newFont) {
        Font oldFont = font;
        font = newFont;
        if (newFont != null) getImage().setFont(newFont);
        if (font != oldFont) repaint();
    }

  
    public Font getFont() {
        if (font != null) {
            return font;
        }
        return getImage().getFont();
    }

 
    public void setSize(Dimension d) {
        Dimension oldSize = size;
        size = new Dimension(d); // defensive copy
        if (size != oldSize) repaint();
    }

    public int getHeight() {
        if (size != null) {
            return size.height;
        } else {
            return getImage().getHeight();
        }
    }

 
    public int getWidth() {
        if (size != null) {
            return size.width;
        } else {
            return getImage().getWidth();
        }
    }

  
    public boolean isFixedSize() {
        return size != null;
    }

  
    public void setForeground(Color fg) {
        Color oldColor = foreground;
        foreground = fg;
        if (foreground != oldColor) repaint();
    }

  
    public Color getForeground() {
        return foreground;
    }

   
    public void setBackground(Color bg) {
        Color oldColor = background;
        background = bg;
        if (background != oldColor) repaint();
    }

  
    public Color getBackground() {
        return background;
    }

   
    public void setBorder(Border newBorder) {
        Border oldBorder = border;
        border = newBorder;
        if (border != oldBorder) repaint();
    }

   
    public Border getBorder() {
        return border;
    }

  
    public Insets getInsets() {
        if (border != null) {
            return border.getBorderInsets();
        }
        return DEFAULT_INSETS;
    }

 
    public String toString() {
        return getClass().getName() + "(id=" + id + ", text=\"" + text
            + "\", width=" + size.width + ", height=" + size.height + ")";
    }

  

   
    public void setFocusable(boolean canFocus) {
        focusable = canFocus;
    }

 
    public boolean isFocusable() {
        return focusable;
    }

    public boolean requestFocus() {
        if (isFocusable() && isEnabled()) {
            if (focusOwner != null) {
                GUIComponent oldFocusOwner = focusOwner;
                focusOwner = null;
                oldFocusOwner.repaint(); 
            }
           
            focusOwner = this;
            focusOwner.repaint(); 
            return true;
        }
        return false;
    }

    public boolean isFocusOwner() {
        return focusOwner == this;
    }


   
    public void addActionListener(ActionListener al) {
        listeners.add(al);
    }

  
    protected void fireActionEvent() {
        for (int i = 0; i < listeners.size(); i++) {
            ActionListener al = listeners.get(i);
            al.actionPerformed(this);
        }
    }

    public void removeActionListener(ActionListener al) {
        listeners.remove(al);
    }

 

   
    public void addedToWorld(World w) {
        visible = true;
        repaint();
        if (!enabled) makeDisabledImage(getImage());
    }

    public void repaint() {
        if (visible && enabled) {
            Graphics g = getGraphics();
            paintComponent(g);
           
            g = getGraphics();
            paintBorder(g);
            paintText(g);  
        }
    }

  
    public Graphics getGraphics() {
        Graphics g = getImage().getAwtImage().getGraphics();
        if (getFont() != null) g.setFont(getFont());
        if (getForeground() != null) g.setColor(getForeground());
        return g;
    }

  
    public void paintComponent(Graphics g) { /* override in subclass */ }

  
    protected void paintBorder(Graphics g) {
        Border b = getBorder();
        if (b != null) {
            b.paintBorder(this, g);
        }
    }

   
    public void paintText(Graphics g) { /* override in subclass */ }

   
    public static String[] wordWrap(String str, int max) {
        if (max <= 0) return new String[0];
        Pattern wrapRE =
            Pattern.compile(".{0," + (max - 1) + "}(?:\\S(?:-| |\n|$)|\n|$)");
        List<String> list = new LinkedList<String>();
        Matcher m = wrapRE.matcher(str);
        while (m.find()) list.add(m.group());
        if (list.get(list.size() - 1).equals("")) list.remove(list.size() - 1);
        return (String[]) list.toArray(new String[list.size()]);
    }

  
    public Dimension getTextDimension(String str, Graphics g) {
       
        if (str == null) str = "";
        FontMetrics fm = g.getFontMetrics();
      
        String[] lines = str.split("\n");
        int width = 1;
        int height = 0;
        Rectangle2D[] bounds = new Rectangle2D[lines.length];
        for (int i = 0; i < lines.length; i++) {
            bounds[i] = g.getFontMetrics().getStringBounds(lines[i], g);
            width = Math.max(width, (int) Math.ceil(bounds[i].getWidth()));
            height += Math.ceil(bounds[i].getHeight());
        }
        Insets insets = getInsets();
        if (insets != null) {
            width += insets.left + insets.right;
            height += insets.top + insets.bottom;
        }
        if (height <= 0) height = 1;
        return new Dimension(width, height);
    }

 
    public String[] splitLines(String str, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        if (str == null) str = "";
        String[] lines = null;
        if (isFixedSize()) {
            int pixelsWide = fm.stringWidth(str);
            Insets insets = getInsets();
            if (insets != null) {
                pixelsWide += (insets.left + insets.right);
            }
            int maxChars = Math.round(getWidth()
                / ((float) pixelsWide / str.length()));
            lines = wordWrap(str, maxChars);
        } else {
            lines = str.split("\n");
        }
        return lines;
    }
}


interface ActionListener {
  
    public void actionPerformed(GUIComponent c);
}


interface Border {
  
    void paintBorder(GUIComponent c, Graphics g);

  
    Insets getBorderInsets();
}


class LineBorder implements Border {
   
    private static final int X_PAD = 3;
    private Color color;
    private int thickness;

    
    public LineBorder() {
        this(Color.BLACK, 1);
    }

   
    public LineBorder(Color lineColor, int lineThickness) {
        color = lineColor;
        thickness = lineThickness;
    }

  
    public void paintBorder(GUIComponent c, Graphics g) {
        Color oldColor = g.getColor();
        g.setColor(color);
        for (int i = 0; i < thickness; i++)  {
            g.drawRect(i, i, c.getWidth() - i - i - 1,
                c.getHeight() - i - i - 1);
        }
        g.setColor(oldColor);
    }

  
    public Insets getBorderInsets()       {
        return new Insets(thickness, thickness + X_PAD, thickness,
            thickness + X_PAD);
    }
}
