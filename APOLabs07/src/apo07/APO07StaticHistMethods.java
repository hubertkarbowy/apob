package apo07;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class APO07StaticHistMethods {
	
	static class BufferedImageHistogram {
		private int type=0;
		private int[] lutR = new int[256];
		private int[] lutG = new int[256];
		private int[] lutB = new int[256];
		private int[] lutGray = new int[256];
		BufferedImage im = null;
		
		BufferedImageHistogram(BufferedImage im) {
			this.im = im;
			setLut(lutR, 0);
			setLut(lutGray, 0);
			if (im.getType()==BufferedImage.TYPE_BYTE_GRAY) {
				type=0;
				setLut(lutG, 0);
				setLut(lutB, 0);
				setLut(lutGray, 0);
			}
			else {
				type=1;
				setLut(lutG, 1);
				setLut(lutB, 2);
			}
		}
		
		private void setLut(int[] which, int band) // band: 0 = red, 1 = green, 2 = blue
		{
			for (int x=0; x<im.getWidth(); x++) {			// compute lookup table
				for (int y=0; y<im.getHeight(); y++) {
					int pixel_value = im.getRaster().getSample(x, y, band);
					if (pixel_value>255) throw new RuntimeException(); // this should never get called with grayscale images
				    else which[pixel_value]++;
			 }
			}
		}
		
		protected int[] getLut(int which) {
			if (which==0) return lutR;
			else if (which==1) return lutG;
			else if (which==2) return lutB;
			else return lutGray;
		}
		
		protected BufferedImage getImage() {
			return im;
		}
	}
	
	public static int[] getGrayscaleHist(BufferedImage grayScaled)
	{
		int lut[] = new int[256];
		for (int x=0; x<grayScaled.getWidth(); x++) {			// compute lookup table
			for (int y=0; y<grayScaled.getHeight(); y++) {
				int pixel_value=grayScaled.getRaster().getSample(x, y, 0);
				// int pixel_value= grayScaled.getRGB(x, y)& 0xFF;
				if (pixel_value>255) lut[255] += 1; // this should never get called with grayscale images
				else lut[pixel_value] = lut[pixel_value]+1;
		 }
		}
		return lut;
	}
	
	public static BufferedImage histEqualize(BufferedImageHistogram inputPic, int method) {
        
     BufferedImage ret = new BufferedImage(inputPic.getImage().getWidth(), inputPic.getImage().getHeight(), inputPic.getImage().getType());
     int imgType = inputPic.type;
     
  //   
        int[] lutR = inputPic.getLut(0);
        int[] lutG = inputPic.getLut(1);
        int[] lutB = inputPic.getLut(2);
        int[][] lut = {lutR, lutG, lutB};
        Color[][] colortab = new Color[inputPic.getImage().getWidth()][inputPic.getImage().getHeight()];
        
    	float histAverageR = (float)Arrays.stream(lutR).average().orElse(0.0);
    	float histAverageG = (float)Arrays.stream(lutG).average().orElse(0.0);
    	float histAverageB = (float)Arrays.stream(lutB).average().orElse(0.0);
    	float histAverage[] = {histAverageR, histAverageG, histAverageB};
    	
    	// for (int band=0; band<=2; band++) {   //for each band
    		float hInt[] = {(float)0.0, (float)0.0, (float)0.0};
			int R[] = {0,0,0};
			float[][] left = {new float[256], new float[256], new float[256]};
	        float[][] right = {new float[256], new float[256], new float[256]};
	        int[][] newValue = {new int[256],new int[256],new int[256]};
		
	        for (int Z = 0; Z < 256; Z++) {
	            left[0][Z] = R[0]; left[1][Z] = R[1]; left[2][Z] = R[2];
	            hInt[0] += lut[0][Z]; hInt[1] += lut[1][Z]; hInt[1] += lut[1][Z]; hInt[2] += lut[2][Z];
	            
	            for (int band=0; band<=2; band++) {
	            while (hInt[band] > histAverage[band]) {
	                hInt[band] -= histAverage[band];
	                R[band]++;
	            }
	            }
	            right[0][Z] = R[0]; right[1][Z] = R[1]; right[2][Z] = R[2];
	            if (method==1) {
	            	newValue[0][Z] = (int)Math.round(((left[0][Z] + right[0][Z]) / 2.0)); // srednia
	            	newValue[1][Z] = (int)Math.round(((left[1][Z] + right[1][Z]) / 2.0)); // srednia
	            	newValue[2][Z] = (int)Math.round(((left[2][Z] + right[2][Z]) / 2.0)); // srednia
	            }
	            else if (method==2) {
	            	newValue[0][Z] = (int)(right[0][Z] - left[0][Z]); // losowa
	            	newValue[1][Z] = (int)(right[1][Z] - left[1][Z]); // losowa
	            	newValue[2][Z] = (int)(right[2][Z] - left[2][Z]); // losowa
	            }
	        }
	        
	     //   WritableRaster raster = inputPic.getImage().getRaster();
	     //   WritableRaster outraster = ret.getRaster();
	        // JOptionPane.showMessageDialog(null, "H=" + outraster.getWidth() + "W=" +outraster.getHeight());
	       for (int i = 0; i < inputPic.getImage().getWidth(); i++) {
	            for (int j = 0; j < inputPic.getImage().getHeight(); j++) {
	            	// int color = raster.getSample(i, j, band);
	            	
	            	int rgba = inputPic.getImage().getRGB(i, j);
	                Color col = new Color(rgba, true);
	                int[] oldcolor ={col.getRed(), col.getGreen(), col.getBlue()};
	                int[] newcolor = {0,0,0};
	            	
	                for (int band=0; band<=2; band++) {
		                if (left[band][oldcolor[band]] == right[band][oldcolor[band]]) newcolor[band]=oldcolor[band]; // outraster.setSample(i, j, band, color);
		                else {
		                    switch (method) {
		                    case 1:
		                    	// outraster.setSample(i, j, band, newValue[color]	);
		                    	newcolor[band] = newValue[band][oldcolor[band]];
		                    	break;
		                    case 2:
		                    	int randomNum = 0 + (int)(Math.random() * newValue[band][oldcolor[band]]);
		                    	randomNum += left[band][oldcolor[band]];
		                    	// outraster.setSample(i, j, band, randomNum);
		                    	newcolor[band] = randomNum;
		                    	break;
		                	default:
		                		break;
		                    }                  
		                }
	                }
	                Color newCol = new Color(Math.max(newcolor[0], 255), Math.max(newcolor[1], 255), Math.max(newcolor[2], 255));
	                ret.setRGB(i, j, newCol.getRGB());
	            }
	       }
	   //    if (imgType==0 || (imgType==1 && band==2)) break
     
     return ret;

    } 
}
