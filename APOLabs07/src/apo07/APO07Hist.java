package apo07;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static apo07.APO07StaticHistMethods.*;

enum ArrowDirection {
	UPWARD, LEFTWARD
}

public class APO07Hist extends JPanel {
    BufferedImage pierwszy = null;
    
    int offsetX=0;
    int offsetY=0;
    String histTitle = "Ta klasa rozszerza JPanel.\nTu bedzie rysowany histogram.";
    static class HistogramPaintException extends RuntimeException { // just in case sth is f*ckd up inside the overriden paint method
    	public HistogramPaintException(String msg) {
    		super(msg);
    	}
    }
    
    // CONSTRUCTORS
    
    public APO07Hist ()
    {
    	histTitle = "Histogram";
    }
    
    public APO07Hist (String histTitle)
    {
    	this.histTitle = histTitle;
    }
    
    public APO07Hist (String histTitle, BufferedImage im)
    {
    	this.histTitle = histTitle;
    	this.pierwszy = im;
    }
    
    // USER METHODS
    
    public void setHistTile (String histTitle) {
    	this.histTitle = histTitle;
    	repaint();
    	revalidate();
    }
    
    public void setNewImage (BufferedImage im) {
    	this.pierwszy=null;
    	repaint();
    	revalidate();
    	this.pierwszy = im;
    	repaint();
    	revalidate();
    }
    
    // METHODS THAT DO THE ACTUAL 2D DRAWING
    @Override
    public void paint(Graphics g) {
    	 Graphics2D g2 = (Graphics2D) g;
         Dimension size = getSize();
         Font font = new Font("Arial", Font.PLAIN, 14);

         int[] lut = new int[256]; // lookup table
         
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
         int xTextTemp = rX-(int)boundsTemp.getX(); // centres the text
         int yTextTemp = rY-(int)boundsTemp.getY();
         g.drawString(tempString, xTextTemp, 20);
         
         int maxX = size.width;
         int maxY = size.height;
         int yAxis_startX = 20;  int yAxis_startY=maxY-20; int yAxis_endX=20; int yAxis_endY=20;
         int xAxis_startX = 20;  int xAxis_startY=maxY-20; int xAxis_endX=maxX-20; int xAxis_endY=maxY-20;
                  
         drawArrow(g, yAxis_startX, yAxis_startY, yAxis_endX, yAxis_endY, ArrowDirection.UPWARD);
         drawArrow(g, xAxis_startX, xAxis_startY, xAxis_startX+513, xAxis_endY, ArrowDirection.LEFTWARD); // what the hell. Let's fix hist width to 512 + 1 pixels (two pixels for each grayscale value)
         
         if (pierwszy==null) { // Clear the histogram view
        	 g.setColor(Color.WHITE);
        	 g.fillRect(yAxis_endX+1, yAxis_endY-1, 512, yAxis_startY-yAxis_endY-1);
        	 System.out.println("Cleared hist");
         }
         else
         {
        	 BufferedImage grayScaled = new BufferedImage(pierwszy.getWidth(), pierwszy.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        	 Graphics gg = grayScaled.getGraphics();
        	 System.out.println("W="+grayScaled.getWidth()+", H="+grayScaled.getHeight());
        	 gg.drawImage(pierwszy, 0, 0, null);
        	 lut = getGrayscaleHist(grayScaled);
        	 int maxBarHeight = yAxis_endY-yAxis_startY-1;
        	 paintBars(g, lut, xAxis_startX+1, xAxis_startY-1, maxBarHeight);
         }
         
         
    }
    private void drawArrow(Graphics g, int startX, int startY, int endX, int endY, ArrowDirection ad) {
    	Graphics2D g2 = (Graphics2D) g;
    	int[] triangle_coordinatesX;
    	int[] triangle_coordinatesY;
    	
    	g.setColor(Color.BLACK);
    	Line2D.Double line = new Line2D.Double(startX, startY, endX, endY);
    	g2.draw(line);
    	
    	switch (ad) {
    		case UPWARD:
    			triangle_coordinatesX = new int[] {endX-5,endX+5,endX};
    			triangle_coordinatesY = new int[] {endY,endY, endY-5};
    			break;
    		default:
    			triangle_coordinatesX = new int[] {endX,endX, endX+5};
    			triangle_coordinatesY = new int[] {endY-5,endY+5, endY};
    			break;
    	}
    	g2.fillPolygon(triangle_coordinatesX, triangle_coordinatesY, 3);
    }
    
    private void paintBars(Graphics g, int[] lut, int xAxis_startX, int xAxis_startY, int maxBarHeight) // drawing values from 0 to 255, the bar width is 2 pixels per value
    {
    	try {
	    	int lutMaxValue = Arrays.stream(lut).max().orElseThrow(() -> new APO07Hist.HistogramPaintException("Unable to paint histogram. Wish I knew why..."));
	    	g.setColor(Color.PINK);	    	
	    	for (int x=0; x<=255; x++) {
	    		int barHeight = Math.round(((float)lut[x]/lutMaxValue)*Math.abs(maxBarHeight));
	    		g.fillRect(xAxis_startX+(2*x), xAxis_startY-barHeight, 2, barHeight);
	    	}
    	}
    	catch (HistogramPaintException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage());
    	}
    }
}