package apo07;

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
				if (pixel_value>255) lut[255] += 1; // this should never get called with grayscale images
				else lut[pixel_value] = lut[pixel_value]+1;
		 }
		}
		return lut;
	}
	
	public static BufferedImage histEqualize(BufferedImageHistogram inputPic, int method) {
        
     BufferedImage ret = new BufferedImage(inputPic.getImage().getWidth(), inputPic.getImage().getHeight(), inputPic.getImage().getType());
     int imgType = inputPic.type;
     
     for (int band=0; band<=2; band++) {   //for each band
        int[] lut = inputPic.getLut(band);
        int R = 0;
        float hInt = (float)0.0;
        float[] left = new float[256];
        float[] right = new float[256];
        int[] newValue = new int[256];
    	float histAverage = (float)Arrays.stream(lut).average().orElse(0.0);
    	
        for (int Z = 0; Z < 256; Z++) {
            left[Z] = R;
            hInt += lut[Z];
            while (hInt > histAverage) {
                hInt -= histAverage;
                R++;
            }

            right[Z] = R;
            if (method==1) newValue[Z] = (int)Math.round(((left[Z] + right[Z]) / 2.0)); // srednia
            else if (method==2) newValue[Z] = (int)(right[Z] - left[Z]); // losowa
        }
        
        WritableRaster raster = inputPic.getImage().getRaster();
        WritableRaster outraster = ret.getRaster();
        JOptionPane.showMessageDialog(null, "H=" + outraster.getWidth() + "W=" +outraster.getHeight());
       for (int i = 0; i < inputPic.getImage().getWidth(); i++) {
            for (int j = 0; j < inputPic.getImage().getHeight(); j++) {
            	int color = raster.getSample(i, j, band);               
                if (left[color] == right[color]) outraster.setSample(i, j, band, color);
                else {
                    switch (method) {
                    case 1:
                    	outraster.setSample(i, j, band, newValue[color]	);
                    	break;
                    case 2:
                    	int randomNum = 0 + (int)(Math.random() * newValue[color]);
                    	randomNum += left[color];
                    	outraster.setSample(i, j, band, randomNum);
                	default:
                		break;
                    }                  
                }
            }
       }
       if (imgType==0 || (imgType==1 && band==2)) break;
     }
     
     /*
     ret = new BufferedImage(inputPic.getImage().getWidth(), inputPic.getImage().getHeight(), inputPic.getImage().getType());
     
     int sum =0;
     int[] iarray = new int[1];
     int anzpixel= inputPic.getImage().getWidth() * inputPic.getImage().getHeight();
     // build a Lookup table LUT containing scale factor
        float[] lut2 = new float[anzpixel]; 
        for ( int i=0; i < 255; ++i )
        {
            sum += inputPic.getLut(0)[i];
            lut2[i] = sum * 255 / anzpixel;
        }

        // transform image using sum histogram as a Lookup table
        for (int x = 1; x < inputPic.getImage().getWidth(); x++) {
            for (int y = 1; y < inputPic.getImage().getHeight(); y++) {
                int valueBefore=inputPic.getImage().getRaster().getPixel(x, y,iarray)[0];
                int valueAfter= (int) lut2[valueBefore];
                iarray[0]=valueAfter;
                ret.getRaster().setPixel(x, y, iarray); 
            }
        }
     */
     
     return ret;

    } 
}
