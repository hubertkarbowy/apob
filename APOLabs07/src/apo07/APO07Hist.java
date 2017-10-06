package apo07;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class APO07Hist extends JPanel {
    BufferedImage pierwszy = null;
    BufferedImage drugi = null;
    BufferedImage wynikowy = null;
    
    int offsetX=0;
    int offsetY=0;
    String histTitle = "Ta klasa rozszerza JPanel.\nTu bedzie rysowany histogram.";
   
    
    
    public void paint(Graphics g) {
    	 Graphics2D g2 = (Graphics2D) g;
         Dimension size = getSize();

         Font font = new Font("Serif", Font.PLAIN, 12);
         g.setFont(font);
         
         String tempString = histTitle;
         FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
         Rectangle2D boundsTemp = font.getStringBounds(tempString, frc);
         Rectangle2D boundsCond = font.getStringBounds("", frc);
         int wText = Math.max((int)boundsTemp.getWidth(), (int)boundsCond.getWidth());
         int hText = (int)boundsTemp.getHeight() + (int)boundsCond.getHeight();
         int rX = (size.width-wText)/2;
         int rY = (size.height-hText)/2;
         
         g.setColor(Color.WHITE);
         g2.fillRect(0, 0, size.width, size.height);
         
         g.setColor(Color.BLACK);
         int xTextTemp = rX-(int)boundsTemp.getX();
         int yTextTemp = rY-(int)boundsTemp.getY();
         g.drawString(tempString, xTextTemp, yTextTemp);
    }
	
}
