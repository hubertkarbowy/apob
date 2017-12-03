package apo07;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

import javax.swing.JDialog;
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
					outraster.setPixel(x, y, outsample);
				}
			}
		return ret;
	}
	
	public static BufferedImage thresholdImg(BufferedImage im) {
		int threshold=255;
		try {
			threshold = Integer.parseInt(JOptionPane.showInputDialog("Please give threshold: "));
			if (threshold<=0 | threshold>255) throw new NumberFormatException();
		}
		catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Please enter a value in the range 1-255");
			return null;
		}
		return thresholdImg(im, threshold);
	}
	
	public static BufferedImage thresholdImg(BufferedImage im, int threshold) {
		BufferedImage ret = getEmptyLinearImage(im);
		Raster inraster = im.getRaster();
		WritableRaster outraster = ret.getRaster();
		int numBands = inraster.getNumBands();
		int[] insample = new int[numBands]; int[] outsample = new int[numBands]; 
		
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				inraster.getPixel(x, y, insample);
				for (int band=0; band<numBands; band++) outsample[band] = insample[band] >= threshold ? 255 : 0;
				outraster.setPixel(x, y, outsample);
			}
		}
		return ret;
	}
	
	public static BufferedImage thresholdRangeImg(BufferedImage im) {
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
				for (int band=0; band<numBands; band++) outsample[band] = insample[band]>thresholdMin && insample[band]<=thresholdMax ? insample[band] : 0;
				outraster.setPixel(x,y, outsample);
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
				   inraster.getPixel(x, y, insample);
				   for (int band=0; band<numBands; band++) outsample[band]= stretchFunction.apply(insample[band], thresholdMin, thresholdMax);
				   outraster.setPixel(x, y, outsample);
			}
		}
		return ret;
	}
	
	public static BufferedImage downsample(BufferedImage im, int numLevels) {
		BufferedImage ret = getEmptyLinearImage(im);
		Raster inraster = im.getRaster();
		WritableRaster outraster = ret.getRaster();
		int numBands = im.getRaster().getNumBands();
		int[] insample = new int[numBands];
		int[] outsample = new int[numBands];
		
		float step = (float)256/(numLevels);
		
		UnaryOperator<Integer> fitIntoBands = (p) -> {float temp=Math.round(p/step); return Math.round(temp*step);};
		for (int x=0; x<im.getWidth(); x++) {
			for (int y=0; y<im.getHeight(); y++) {
				   inraster.getPixel(x, y, insample);
				   for (int band=0; band<numBands; band++) outsample[band]=fitIntoBands.apply(insample[band]);
				   outraster.setPixel(x, y, outsample);
			}
		}
		return ret;
	}
	
	public static BufferedImage arithmeticOps(BufferedImage im, BufferedImage im2, BiFunction<Integer, Integer, Integer> aluOp) {
		BufferedImage newim1 = getMaximalOfTwo(im, im2);
		BufferedImage newim2 = getMaximalOfTwo(im, im2);
		BufferedImage ret = getMaximalOfTwo(im, im2);
		
		WritableRaster inraster = newim1.getRaster(); inraster.setRect(im.getRaster());
		WritableRaster inraster2 = newim2.getRaster(); inraster2.setRect(im2.getRaster());
		WritableRaster outraster = ret.getRaster();
		
		int numBands = im.getRaster().getNumBands();
		
		for (int b=0; b<numBands; b++) {
			int[] insample1 = inraster.getSamples(0, 0, newim1.getWidth()-1, newim1.getHeight()-1, b, (int[]) null);
			int[] insample2 = inraster2.getSamples(0, 0, newim2.getWidth()-1, newim2.getHeight()-1, b, (int[]) null);
			int[] outsample = outraster.getSamples(0, 0, ret.getWidth()-1, ret.getHeight()-1, b, (int[]) null);
			for (int i=0; i<insample1.length; i++) outsample[i]=tv(aluOp.apply(insample1[i], insample2[i]));
			outraster.setSamples(0, 0, ret.getWidth()-1, ret.getHeight()-1, b, outsample);
		}
		return ret;
	}
}