package apo07;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import static apo07.APO07StaticUtilityMethods.*;

public class APO07StaticHistMethods {
	
	static class BufferedImageHistogram {
		private int type=0;
		private int[][] lutAll = {new int[256], new int[256], new int[256]};
		BufferedImage im = null;
		
		BufferedImageHistogram(BufferedImage im) {
			this.im = im;
			Raster inraster = im.getRaster();
			int[] insample = new int[inraster.getNumBands()];
			
			for (int x=0; x<im.getWidth(); x++) {			// compute lookup table
				for (int y=0; y<im.getHeight(); y++) {
					im.getRaster().getPixel(x, y, insample);
					for (int component=0; component<insample.length; component++) lutAll[component][insample[component]]++;
			 }
			}
		}
		
		protected int[] getLut(int which) {
			if (which==0) return lutAll[0];
			else if (which==1) return lutAll[1];
			else if (which==2) return lutAll[2];
			else return lutAll[0];
		}
		
		protected int[][] getLut() {return lutAll;}
		
		protected BufferedImage getImage() {
			return im;
		}
	}
	
	public static int[] getGrayscaleHist(BufferedImage im)
	{
		int[] lut = new int[256];
		Raster inraster = im.getRaster();
		int numBands = inraster.getNumBands();
		int[] insample = new int[numBands];
		
		for (int x=0; x<im.getWidth(); x++) {			// compute lookup table
			for (int y=0; y<im.getHeight(); y++) {
				im.getRaster().getPixel(x, y, insample);
				int avgValue = Math.round(Arrays.stream(insample).sum()/numBands); // This handles both grayscale and rgb images
				lut[avgValue]++;
		 }
		}
		return lut;
	}
	
	public static BufferedImage histEqualize(BufferedImageHistogram inputPic, int method) {
        
	// BufferedImage ret = new BufferedImage(inputPic.getImage().getWidth(), inputPic.getImage().getHeight(), inputPic.getImage().getType());
	BufferedImage ret = getEmptyLinearImage(inputPic.getImage());
	Raster inraster = inputPic.getImage().getRaster();
	WritableRaster outraster = ret.getRaster();
	int numBands = inraster.getNumBands();
	if (numBands>3) throw new IllegalArgumentException("Max 3 bands!");
	  
	int[][] lutAll = inputPic.getLut();
	float[] histAverage = {(float)0.0, (float)0.0, (float)0.0};
	for (int i=0; i<=2; i++) histAverage[i] = (float)Arrays.stream(lutAll[i]).average().orElse(0.0);
	float hInt[] = {(float)0.0, (float)0.0, (float)0.0};
	int R[] = {0,0,0};
	float[][] left = {new float[256], new float[256], new float[256]};
	float[][] right = {new float[256], new float[256], new float[256]};
	int[][] newValue = {new int[256],new int[256],new int[256]};
	int[] insample = new int[numBands];
	int[] outsample = new int[numBands];
		
    for (int Z = 0; Z < 256; Z++) {
    	for (int band=0; band<numBands; band++) {
    		left[band][Z] = R[band];
    		hInt[band] += lutAll[band][Z];
            while (hInt[band] > histAverage[band]) {
                hInt[band] -= histAverage[band];
                R[band]++;
            }
            right[band][Z] = R[band];
        
        
        if (method==1) newValue[band][Z] = (int)Math.round(((left[band][Z] + right[band][Z]) / 2.0)); // srednia
        else if (method==2) newValue[band][Z] = (int)(right[band][Z] - left[band][Z]); // losowa
    	}
   }
	      
       for (int x = 0; x < inputPic.getImage().getWidth(); x++) {
            for (int y = 0; y < inputPic.getImage().getHeight(); y++) {
            	// int color = raster.getSample(i, j, band);
            	inraster.getPixel(x, y, insample);
            	for (int band=0; band<numBands; band++) {
            		if (left[band][insample[band]] == right[band][insample[band]]) outsample[band]=insample[band];
            		else {
	                    if (method==1) outsample[band] = newValue[band][insample[band]];
	                    else if (method==2) {
	                    	int randomNum = 0 + (int)(Math.random() * newValue[band][insample[band]]);
	                    	randomNum += left[band][insample[band]];
	                    	outsample[band] = randomNum;
	                    }
            		}
            	}
            	outraster.setPixel(x, y, outsample);
            }
       }
     return ret;

    } 
}
