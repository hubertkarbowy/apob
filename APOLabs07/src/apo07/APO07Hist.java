package apo07;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import static apo07.APO07StaticHistMethods.*;
import static apo07.APO07StaticUtilityMethods.*;

enum ArrowDirection {
	UPWARD, LEFTWARD
}

public class APO07Hist extends JPanel {
    BufferedImage pierwszy = null;
    
    int offsetX=0;
    int offsetY=0;
    String histTitle = "Ta klasa rozszerza JPanel.\nTu bedzie rysowany histogram.";
    public double mean = 0.0;  // to hell with encapsulation
    public double variance = 0.0;
    public double sd = 0.0;
    public double median = 0.0;
    public double skewness = 0.0;
    public double kurtosis = 0.0;
    
    
    static class HistogramPaintException extends RuntimeException { // just in case sth is f*ckd up inside the overriden paint method
    	public HistogramPaintException(String msg) {
    		super(msg);
    	}
    }
    
    static class Paire implements Comparable {
    	int firstel;
    	int secondel;
    	public Paire(int a, int b) {firstel=a; secondel=b;}
    	
    	@Override
    	public int compareTo(Object xa) {
    		Paire zz = (Paire) xa;
    		return (this.secondel-this.firstel)-(zz.secondel-zz.firstel);
    	}
    	
    	public String toString() {
    		return "1st: "+firstel+", 2nd: "+secondel+" length: "+(secondel-firstel);
    	}
    }
    boolean noZero = false;
    boolean noMax = false;
    int[] lut = new int[256]; // lookup table
    
    Map<String, Integer> histDesc = new HashMap<>();
    
    public Map<String, Integer> describeThisHist() {
    	// Find "twin peaks" - assume histogram has a bimodal distribution
    	char[] copyOfLut = new char[256];
    	lut[0]='+';
    	for (int a=1; a<255; a++) {
    		if (lut[a]>lut[a-1]) copyOfLut[a]='+';
    		else copyOfLut[a]='-';
    	}
    	System.out.println(Arrays.toString(lut));
    	
    	// TODELETE START
    	
    	 for (int maxlit=0, maxlval=0,
    			 maxrit=0, maxrval=0,
    			 lit=0, rit=255;;lit++,rit--) {
    			 if (lut[lit]>maxlval) {maxlit=lit; maxlval=lut[lit]; histDesc.put("peak1idx", lit); histDesc.put("peak1val", lut[lit]);}
    			 if (lut[rit]>maxrval) {maxrit=rit; maxrval=lut[rit]; histDesc.put("peak2idx", rit); histDesc.put("peak2val", lut[rit]);}
    			 if (maxlit==maxrit && maxlval==maxrval) break;
    	}
    	
    	// Now find the minimum between the peaks
    	for (int i=histDesc.get("peak1idx"), valleyidx=0, valleyval=255; i<histDesc.get("peak2idx"); i++) {
    		if (lut[i]<valleyval) {valleyval=lut[i]; valleyidx=i; histDesc.put("valleyidx", i); histDesc.put("valleyval", lut[i]);}
    	} 
    	
    	// TODELETE END
    	
    	boolean isSmoothed=false;
    	int windowSize=4;
    	int sensitivity=2;
    	
    	while (!isSmoothed) {
    		isSmoothed=true;
    		int noOfPluses=0;
    		for (int i=0; i<copyOfLut.length-windowSize-1; i++) {
    			for (int j=0; j<windowSize; j++) if (lut[i+j+1]>lut[i+j]) noOfPluses++;
    			if (noOfPluses>=sensitivity) {
    				if (copyOfLut[i]=='-') {
    					copyOfLut[i]='+';
    					isSmoothed=false;
    				}
    			}
    		}
    	}
    	
    	isSmoothed=false;
    	
    	while (!isSmoothed) {
    		isSmoothed=true;
    		int noOfPluses=0;
    		for (int i=0; i<copyOfLut.length-windowSize-1; i++) {
    			for (int j=0; j<windowSize; j++) if (lut[i+j+1]<lut[i+j]) noOfPluses++;
    			if (noOfPluses>=sensitivity) {
    				if (copyOfLut[i]=='+') {
    					copyOfLut[i]='-';
    					isSmoothed=false;
    				}
    			}
    		}
    	}
    	
    	ArrayList<Paire> listofpluses = new ArrayList<>();
    	
    	int startcounter=0; int endcounter=0; boolean counting=false;
    	for (int i=0; i<copyOfLut.length; i++){
    		if (copyOfLut[i]=='+') {
    			if (counting) endcounter=i;
    			else {startcounter=i; counting=true;}
    		}
    		if (copyOfLut[i]=='-') {
    			if (counting) {
    				endcounter=i-1;
    				listofpluses.add(new Paire(startcounter,endcounter));
    				counting=false;
    			}
    		}
    	}
    	
   // 	listofpluses.forEach(x -> System.out.println(x));
    	
    	
    	
    	return histDesc;    	
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
    	lut = getGrayscaleHist(im);
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
    	
    	
    	if (im==null) return;
BufferedImage gs = getGrayscaleImage(pierwszy);
     	
     	
     	// texture descriptors
     	
     	int bitmapSize=gs.getHeight()*gs.getWidth();
     	int[] grayScaleBitmapUnfolded = new int[bitmapSize];
     	
     	long runningSum=0l;
     	int counter=0;
     	for (int y=0; y<gs.getHeight(); y++) {
     		for (int x=0; x<gs.getWidth(); x++) {
     			int clr = pierwszy.getRGB(x, y);
     			runningSum += getRGBPixelValue(clr, Color.RED);
     			grayScaleBitmapUnfolded[counter] = getRGBPixelValue(clr, Color.RED);
     			counter++;
     		}
     	}
     	mean = (runningSum/bitmapSize); // sod n-1
     	
     	runningSum=0l;
     	counter=0;
     	variance=0.0;
     	
     	for (int y=0; y<gs.getHeight(); y++) {
     		for (int x=0; x<gs.getWidth(); x++) {
     			int clr = pierwszy.getRGB(x, y);		
     			int pxval = getRGBPixelValue(clr, Color.RED);
     			variance += ((pxval-mean)*(pxval-mean))/(bitmapSize);
     			kurtosis += ((pxval-mean)*(pxval-mean)*(pxval-mean)*(pxval-mean))/(bitmapSize);
     		}
     	}
     	
     	sd = Math.sqrt(variance);
     	median = APO07NeighborhoodMethods.getMedian.apply(grayScaleBitmapUnfolded);
     	skewness = (3*(mean-median))/sd;
     	kurtosis = (kurtosis/(Math.pow(sd, 4))-3);
    }
    
    // METHODS THAT DO THE ACTUAL 2D DRAWING
    @Override
    public void paint(Graphics g) {
    	 Graphics2D g2 = (Graphics2D) g;
         Dimension size = getSize();
         Font font = new Font("Arial", Font.PLAIN, 14);
         
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
    
    public void toggleExtremes(byte which, boolean state) {
    	if (which==0) noZero = state ? true : false;
    	if (which==1) noMax = state ? true : false;
    }
    
    private void paintBars(Graphics g, int[] lut, int xAxis_startX, int xAxis_startY, int maxBarHeight) // drawing values from 0 to 255, the bar width is 2 pixels per value
    {
    	try {
    		int[] copyOfLut = Arrays.copyOfRange(lut, noZero ? 1 : 0, noMax ? lut.length-2 : lut.length-1);
	    	int lutMaxValue = Arrays.stream(copyOfLut).max().orElseThrow(() -> new APO07Hist.HistogramPaintException("Unable to paint histogram. Wish I knew why..."));
	    	g.setColor(Color.PINK);	    	
	    	for (int x=(noZero?1:0); x<=(noMax?254:255); x++) {
	    		int barHeight = Math.round(((float)lut[x]/lutMaxValue)*Math.abs(maxBarHeight));
	    		g.fillRect(xAxis_startX+(2*x), xAxis_startY-barHeight, 2, barHeight);
	    	}
    	}
    	catch (HistogramPaintException e) {
    		JOptionPane.showMessageDialog(null, e.getMessage());
    	}
    }
}