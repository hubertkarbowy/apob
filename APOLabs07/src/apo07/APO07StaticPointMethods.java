package apo07;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.function.BiFunction;

import javax.swing.JOptionPane;

import static apo07.APO07StaticUtilityMethods.*;

public class APO07StaticPointMethods {

	public static BufferedImage invertImg(BufferedImage im) {
		BufferedImage ret = getEmptyLinearImage(im);
		Raster inraster = im.getRaster();
		WritableRaster outraster = ret.getRaster();
		int[] insample = new int[inraster.getNumBands()];
		int[] outsample = new int[outraster.getNumBands()];
		
		int type = im.getType();
			for (int x=0; x<im.getWidth(); x++) {
				for (int y=0; y<im.getHeight(); y++) {
					inraster.getPixel(x, y, insample);
					for (int component=0; component<insample.length; component++) {outsample[component] = 255-insample[component];}
//					int rgba = im.getRGB(x, y);
//					int pixelValue;
//	                Color col = new Color(rgba, true);
//	                if (im.getType()==BufferedImage.TYPE_BYTE_GRAY)
//	                {
//	                	WritableRaster wr = im.getRaster();
//	                	int aa[] = new int[1];
//	                	wr.getPixel(x, y, aa);
//	                	pixelValue = 255 - aa[0];
//	                	
//	                }
	              //  else {
//	                col = new Color(255 - col.getRed(),
//	                                0,0);
//	                pixelValue = col.getRGB();
//	              //  }
	                //ret.setRGB(x, y, pixelValue);
					
//                	int oldPV = im.getRaster().getSample(x, y, 0);
//                	if (x % 10 == 0 ) System.out.println(oldPV + "*");
//                	int pixelValue = 255 - oldPV;
//                	ret.getRaster().setSample(x, y, 0, pixelValue);
					outraster.setPixel(x, y, outsample);
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
		// BufferedImage ret = new BufferedImage(im.getWidth(), im.getHeight(), im.getType());
		BufferedImage ret = getEmptyLinearImage(im);
		Raster inraster = im.getRaster();
		WritableRaster outraster = ret.getRaster();
		int numBands = inraster.getNumBands();
		int threshold1 = 255; int threshold2 = 255;
		
		int[] insample = new int[numBands];
		int[] outsample = new int[numBands];
		
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
				// int rgba = im.getRGB(x, y);
				inraster.getPixel(x, y, insample);
				for (int band=0; band<numBands; band++) outsample[band] = insample[band]>thresholdMin && col.getRed()<=thresholdMax ? insample[band] : 0
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
	
	public static BufferedImage stretchToRange(BufferedImage im) {
		BufferedImage ret = getEmptyLinearImage(im);
		Raster inraster = im.getRaster();
		WritableRaster outraster = ret.getRaster();
		
		int threshold1 = 0; int threshold2 = 255;
		try {
			if (im==null) throw new IllegalArgumentException();
			threshold1 = Integer.parseInt(JOptionPane.showInputDialog("Please give stretch MIN: "));
			if (threshold1<0 | threshold2>255) throw new NumberFormatException();
			threshold2 = Integer.parseInt(JOptionPane.showInputDialog("Please give stretch MAX: "));
			if (threshold2<0 | threshold2>255) throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a value in the range 0-255");
			return null;
		}
		int thresholdMin = Math.min(threshold1, threshold2);
		int thresholdMax = Math.max(threshold1, threshold2);
		int numBands = im.getRaster().getNumBands();
		int[] insample = new int[numBands];
		int[] outsample = new int[numBands];
		TriFunction<Integer, Integer, Integer, Integer> stretchFunction = (p,p1,p2) -> {
			if (p<=p1 || p>p2) return 0;
			else return ((p-p1)*256)/(p2-p1);
		};
		
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				// int rgba = im.getRGB(x, y);
	               inraster.getPixel(x, y, insample);
				   for (int band=0; band<numBands; band++) outsample[band]= stretchFunction.apply(insample[band], thresholdMin, thresholdMax);
//	               Color col = new Color(rgba, true);
//	               int[] newcol = {0,0,0};
//	               newcol[0] = col.getRed()>=thresholdMin && col.getRed()<=thresholdMax ? col.getRed() : 0;
//	               newcol[1] = col.getGreen()>=thresholdMin && col.getGreen()<=thresholdMax ? col.getGreen() : 0;
//	               newcol[2] = col.getBlue()>=thresholdMin && col.getBlue()<=thresholdMax ? col.getBlue() : 0;
	               
//	               col = new Color(newcol[0], newcol[1], newcol[2]);
//	               ret.setRGB(x, y, col.getRGB());
				   outraster.setPixel(x, y, outsample);
			}
		}
		return ret;
	}
}