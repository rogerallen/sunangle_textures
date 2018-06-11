//
// make_textures.java 
// create the textures that we use in sunangle program
// by Roger Allen (rallen@gmail.com)
//
// TODO clear the outer extra hatches from line endcaps or figure out how to remove endcaps.

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

class make_textures {
    static double wh = 1024.0;
    static double wh2 = wh / 2.0;
    static double wh4 = wh / 4.0;
    static double wh8 = wh / 8.0;
    static double insetw = 24.0;

    static void draw_centered_text(Graphics2D g, String s, double x, double y) {
        int strWidth, strHeight;
        int baseX, baseY;
        int topOfString;
        Font font = g.getFont();
        FontMetrics fm = g.getFontMetrics(font);
        strWidth = fm.stringWidth(s);
        strHeight = fm.getAscent();
        baseX = (int) (x - (strWidth / 2)); // Move back from center by half the width of the string.
        topOfString = (int) (y - (strHeight / 2)); // Move up from center by half the height of the string.
        baseY = topOfString + fm.getAscent(); // Baseline is fm.getAscent() pixels below the top
        g.drawString(s, baseX, baseY); // Draw the string.
    }

    static void draw_hatches(Graphics2D g, int n) {
        for (int i = 0; i < n; i++) {
            double x0 = (wh2 - insetw) * Math.sin((i / (double) n) * 2.0 * Math.PI);
            double y0 = (wh2 - insetw) * Math.cos((i / (double) n) * 2.0 * Math.PI);
            double x1 = (wh2) * Math.sin((i / (double) n) * 2.0 * Math.PI);
            double y1 = (wh2) * Math.cos((i / (double) n) * 2.0 * Math.PI);
            g.draw(new Line2D.Double(x0, y0, x1, y1));
        }
    }

    static void draw_clock(Graphics2D g, boolean forewards) {
        // System.out.println("draw_clock()");
        g.setFont(new Font("Serif", Font.PLAIN, 64));
        g.translate(wh2, wh2);

        // draw back
        Ellipse2D back = new Ellipse2D.Double(-wh2, -wh2, wh, wh);
        g.setColor(Color.WHITE);
        g.fill(back);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        Ellipse2D backOutline = new Ellipse2D.Double(-wh2, -wh2, wh , wh);
        g.draw(backOutline);

        // outer hatches
        Ellipse2D inset = new Ellipse2D.Double(-wh2 + insetw, -wh2 + insetw, wh - 2 * insetw, wh - 2 * insetw);
        g.setStroke(new BasicStroke(2F));
        g.draw(inset);
        g.setStroke(new BasicStroke(4F));
        draw_hatches(g, 24); // every hour
        g.setStroke(new BasicStroke(1F));
        // draw_hatches(g,60); // 60 min strokes are uneven vs. 24 hours
        draw_hatches(g, 12 * 24); // every 5 mins
        g.setStroke(new BasicStroke(2F));
        draw_hatches(g, 4 * 24); // every 15 mins
        g.setStroke(new BasicStroke(3F));
        draw_hatches(g, 24 * 2);

        // numbers
        for (int i = 0; i < 24; i++) {
            double x0 = (wh2 - 3 * insetw) * Math.sin((i / (double) 24) * 2.0 * Math.PI);
            if (!forewards) {
                x0 = -x0;
            }
            double y0 = (wh2 - 3 * insetw) * Math.cos((i / (double) 24) * 2.0 * Math.PI);
            draw_centered_text(g, "" + ((24 - i) % 24), x0, y0);
        }

        // clear out center
        Ellipse2D back2 = new Ellipse2D.Double(-3 * wh8, -3 * wh8, 1.5 * wh2, 1.5 * wh2);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        g.draw(back2);
        Composite curComposite = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.0F));
        g.fill(back2);
        g.setComposite(curComposite);

    }

    static void draw_compass(Graphics2D g, boolean forewards) {
        // System.out.println("draw_compass()");
        g.setFont(new Font("Serif", Font.PLAIN, 24));
        g.translate(wh2, wh2);

        // back
        Ellipse2D back = new Ellipse2D.Double(-wh2, -wh2, wh, wh);
        g.setColor(Color.WHITE);
        g.fill(back);
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));
        Ellipse2D backOutline = new Ellipse2D.Double(-wh2, -wh2, wh, wh);
        g.draw(backOutline);

        // hatches around outside
        Ellipse2D inset = new Ellipse2D.Double(-wh2 + insetw, -wh2 + insetw, wh - 2 * insetw, wh - 2 * insetw);
        g.setStroke(new BasicStroke(2F));
        g.draw(inset);
        boolean do_inset2 = false;
        if (do_inset2) {
            Ellipse2D inset2 = new Ellipse2D.Double(-3 * wh8, -3 * wh8, 1.5 * wh2, 1.5 * wh2);
            g.setColor(Color.LIGHT_GRAY);
            g.fill(inset2);
            g.setColor(Color.BLACK);
            g.draw(inset2);
        }
        g.setStroke(new BasicStroke(4));
        draw_hatches(g, 36);
        g.setStroke(new BasicStroke(1.F));
        draw_hatches(g, 360);
        g.setStroke(new BasicStroke(2.F));
        draw_hatches(g, 36 * 2);

        // numbers
        for (int i = 0; i < 36; i++) {
            double x0 = (wh2 - 2 * insetw) * Math.sin((i / (double) 36) * 2.0 * Math.PI);
            if (!forewards) {
                x0 = -x0;
            }
            double y0 = (wh2 - 2 * insetw) * Math.cos((i / (double) 36) * 2.0 * Math.PI);
            draw_centered_text(g, "" + 10 * ((36 - i + 18) % 36), x0, y0);
        }

        // directions
        Line2D ns = new Line2D.Double(0, wh8, 0, -(wh8 - 10));
        g.setStroke(new BasicStroke(4));
        g.draw(ns);
        Line2D ew = new Line2D.Double(-wh8, 0, wh8, 0);
        g.setStroke(new BasicStroke(4));
        g.draw(ew);

        // arrows
        GeneralPath p = new GeneralPath();
        p.moveTo(0, -wh8);
        p.lineTo(12, -wh8 + 36);
        p.lineTo(-12, -wh8 + 36);
        p.closePath();
        g.fill(p);

        // text
        g.setFont(new Font("Serif", Font.PLAIN, 128));
        double wht = wh4 - 2 * insetw;
        draw_centered_text(g, "S", 0, wht);
        draw_centered_text(g, "N", 0, -wht);
        if (forewards) {
            draw_centered_text(g, "E", wht, 0);
            draw_centered_text(g, "W", -wht, 0);
        } else {
            draw_centered_text(g, "W", wht, 0);
            draw_centered_text(g, "E", -wht, 0);
        }
        
    }

    static void make_image(String name) {
        //System.out.println("make_image: " + name + ".png");
        BufferedImage theImage = new BufferedImage((int) wh, (int) wh, BufferedImage.TYPE_INT_ARGB);
        Graphics2D theGraphics = (Graphics2D) theImage.getGraphics();
        theGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (name.equals(new String("clock"))) {
            draw_clock(theGraphics, true);
        } else if (name.equals(new String("clock_back"))) {
            draw_clock(theGraphics, false);
        } else if (name.equals(new String("compass"))) {
            draw_compass(theGraphics, true);
        } else if (name.equals(new String("compass_back"))) {
            draw_compass(theGraphics, false);
        }
        theGraphics.dispose();
        File theFile = new File(name + ".png");
        try {
            ImageIO.write(theImage, "PNG", theFile);
        } catch (IOException e) {
            System.out.println("ERROR: Caught Exception!");
        }
        // System.out.println("done");
    }

    public static void main(String args[]) {
        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                make_image(args[i]);
            }
        }
    }
}
