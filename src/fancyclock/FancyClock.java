/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fancyclock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.geom.AffineTransform;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author waterbucket
 */
public class FancyClock {

    JFrame frame;
    RainbowClock rc;

    public FancyClock() {
        this.frame = new JFrame();
        this.frame.setSize(new Dimension(100, 100));
        this.frame.setTitle("Cluck");
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.add(this.rc = new RainbowClock());
        this.frame.pack();
        this.frame.setVisible(true);
        this.rc.startClock();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        FancyClock fc = new FancyClock();
    }

    private static class RainbowClock extends JPanel {

        private final SimpleDateFormat sdf;
        private final LinkedList<String> times;
        private final AffineTransform shear;
        private final Font font;
        private float hue = 0f;
        private final float saturation = 1f;
        private final float brightness = 1f;
        private int previousX;
        private int previousY;
        
        public RainbowClock() {
            this.font = new Font(Font.MONOSPACED,Font.BOLD, 50);
            this.shear = new AffineTransform();
            this.shear.shear(0, 0.5);
            this.sdf = new SimpleDateFormat("HH:mm:ss");
            this.setBackground(Color.BLACK);
            this.times = new LinkedList<>();
            this.setVisible(true);
        }

        @Override
        protected void paintComponent(Graphics grphcs) {
            super.paintComponent(grphcs);
            Graphics2D g2d = (Graphics2D) grphcs;
            g2d.setFont(font.deriveFont(this.shear));
            for(int i = times.size()-1; i >= 0; i--){
                this.previousX = (this.getWidth()/2+10)+10 * i;
                this.previousY = (this.getHeight()/2-10)-10 * i;
                if (this.hue >= 1) {
                    this.hue = 0f;
                } else {
                    this.hue = this.hue + 0.025f;
                }
                g2d.setColor(Color.getHSBColor(this.hue, this.saturation, this.brightness));
                g2d.drawString(times.get(i), this.previousX, this.previousY);
            }
            g2d.setColor(Color.WHITE);
            g2d.drawString(this.getTime(), this.getWidth() / 2, this.getHeight() / 2);
            this.times.addFirst(this.getTime());
        }

        private void startClock() {
            this.previousX = this.getWidth()/2;
            this.previousY = this.getHeight()/2;
            int timeDelay = 1000; // msecs delay
            new Timer(timeDelay, (ActionEvent arg0) -> {
                this.repaint();
                this.revalidate();
                this.hue = 0f;
                if(this.times.size() > 500){
                    this.times.removeLast();
                }
            }).start();
        }

        private String getTime() {
            return this.sdf.format(Calendar.getInstance().getTime());
        }

    }

}
