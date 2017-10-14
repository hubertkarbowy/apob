package apo07;

import java.awt.Color;
import java.awt.image.*;

import javax.swing.JOptionPane;

public class APO07StaticPointMethods {

	public static BufferedImage negateImg(BufferedImage im) {
		BufferedImage ret = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		int type = im.getType();
			for (int x=0; x<im.getWidth(); x++) {
				for (int y=0; y<im.getHeight(); y++) {
					int rgba = im.getRGB(x, y);
					int pixelValue;
	                Color col = new Color(rgba, true);
	                if (im.getType()==BufferedImage.TYPE_BYTE_GRAY)
	                {
	                //	JOptionPane.showMessageDialog(null, "This is a BW img!");
	                	//col = new Color((255 - (col.getRed())),
                        //        (255 - (col.getGreen())), 
                        //        (255 - (col.getBlue())));
	                	WritableRaster wr = im.getRaster();
	                	int aa[] = new int[1];
	                	wr.getPixel(x, y, aa);
	                	pixelValue = 255 - aa[0];
	                	
	                }
	                else {
	                col = new Color(255 - col.getRed(),
	                                255 - col.getGreen(),
	                                255 - col.getBlue());
	                pixelValue = col.getRGB();
	                }
	                
	                ret.setRGB(x, y, pixelValue);
				}
			}
		return ret;
	}
	
	public static BufferedImage thresholdImg(BufferedImage im) {
		BufferedImage ret = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		int threshold = 255;
		try {
			threshold = Integer.parseInt(JOptionPane.showInputDialog("Please give threshold: "));
			if (threshold<=0 | threshold>255) throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a value in the range 1-255");
			return null;
		}
		
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				int rgba = im.getRGB(x, y);
	               Color col = new Color(rgba, true);
	               int[] newcol = {0,0,0};
	               newcol[0] = col.getRed()>=threshold ? 255 : 0;
	               newcol[1] = col.getGreen()>=threshold ? 255 : 0;
	               newcol[2] = col.getBlue()>=threshold ? 255 : 0;
	               
	               col = new Color(newcol[0], newcol[1], newcol[2]);
	               ret.setRGB(x, y, col.getRGB());
			}
		}
		return ret;
	}
	
	public static BufferedImage thresholdRangeImg(BufferedImage im) {
		BufferedImage ret = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		int threshold1 = 255; int threshold2 = 255;
		try {
			threshold1 = Integer.parseInt(JOptionPane.showInputDialog("Please give threshold MIN: "));
			if (threshold1<0 | threshold2>255) throw new NumberFormatException();
			threshold2 = Integer.parseInt(JOptionPane.showInputDialog("Please give threshold MAX: "));
			if (threshold2<0 | threshold2>255) throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a value in the range 0-255");
			return null;
		}
		int thresholdMin = Math.min(threshold1, threshold2);
		int thresholdMax = Math.max(threshold1, threshold2);
		
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				int rgba = im.getRGB(x, y);
	               Color col = new Color(rgba, true);
	               int[] newcol = {0,0,0};
	               newcol[0] = col.getRed()>=thresholdMin && col.getRed()<=thresholdMax ? col.getRed() : 0;
	               newcol[1] = col.getGreen()>=thresholdMin && col.getGreen()<=thresholdMax ? col.getGreen() : 0;
	               newcol[2] = col.getBlue()>=thresholdMin && col.getBlue()<=thresholdMax ? col.getBlue() : 0;
	               
	               col = new Color(newcol[0], newcol[1], newcol[2]);
	               ret.setRGB(x, y, col.getRGB());
			}
		}
		return ret;
	}
	
	public static BufferedImage reduceGrayscale(BufferedImage im) {
		BufferedImage ret = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		int threshold = 255;
		try {
			threshold = Integer.parseInt(JOptionPane.showInputDialog("Please give threshold: "));
			if (threshold<=0 | threshold>255) throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a value in the range 1-255");
			return null;
		}
		
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				int rgba = im.getRGB(x, y);
	               Color col = new Color(rgba, true);
	               int[] newcol = {0,0,0};
	               newcol[0] = col.getRed()>=threshold ? 255 : 0;
	               newcol[1] = col.getGreen()>=threshold ? 255 : 0;
	               newcol[2] = col.getBlue()>=threshold ? 255 : 0;
	               
	               col = new Color(newcol[0], newcol[1], newcol[2]);
	               ret.setRGB(x, y, col.getRGB());
			}
		}
		return ret;
	}
}