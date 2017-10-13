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
					int pixel_value=im.getRaster().getSample(x, y, band);
					if (pixel_value>255) which[255] += 1; // this should never get called with grayscale images
					else which[pixel_value] = which[pixel_value]+1;
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
        
     for (int band=0; band<2; band++) {   //for each band
        int[] lut = inputPic.getLut(band);
        int R = 0;
        float hInt = (float)0.0;
        float[] left = new float[256];
        float[] right = new float[256];
        int[] newValue = new int[256];
    	float histAverage = (float)Arrays.stream(lut).average().orElse(0.0);
        
        for (int i = 0; i < 256; ++i) {
            left[i] = R;
            hInt += lut[i];
            while (hInt > histAverage) {
                hInt -= histAverage;
                if (R < 255) R++;
            }

            right[i] = R;
            if (method==1) newValue[i] = (int)((left[i] + right[i]) / 2.0); // srednia
            else if (method==2) newValue[i] = (int)(right[i] - left[i]); // losowa
            else if (method==3) newValue[i] = (int)((left[i] + right[i]) / 2.0); // tzw. wlasna
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
                    	outraster.setSample(i, j, band, color);
                    	break;
                	default:
                		break;
                    }                  
                }
            }
       }
       if (imgType==0) break;
     }
     return ret;

        // ponizsze do przerobienia
        
        /*
        for (int i = 0; i < bmp.Size.Width; ++i) {
            for (int j = 0; j < bmp.Size.Height; ++j) {
                Color color = bmp[i, j];
                if (left[color.R] == right[color.R])
                    bmp[i, j] = Color.FromArgb(color.A, (int)left[color.R], (int)left[color.R], (int)left[color.R]);
                else {
                    switch (method) {
                        case EqualizationMethod.Averages:
                            bmp[i, j] = Color.FromArgb(color.A, (int)newValue[color.R], (int)newValue[color.R], (int)newValue[color.R]);
                            break;
                        case EqualizationMethod.Random:
                            Random rnd = new Random();
                            int value = (int)left[color.R] + rnd.Next(newValue[color.R] + 1);
                            bmp[i, j] = Color.FromArgb(color.A, value, value, value);
                            break;
                        case EqualizationMethod.Neighborhood8:
                            double average = 0;
                            int count = 0;
                            foreach (Point offset in new Point[] { new Point(1, 0), new Point(-1, 0), new Point(0, 1), new Point(0, -1), new Point(1, 1), new Point(-1, -1), new Point(-1, 1), new Point(1, -1) }) {
                                if (i + offset.X >= 0 && i + offset.X < bmp.Width && j + offset.Y >= 0 && j + offset.Y < bmp.Height) {
                                    average += bmp[i + offset.X, j + offset.Y].R;
                                    ++count;
                                }
                            }
                            average /= count;
                            if (average > right[color.R])
                                bmp[i, j] = Color.FromArgb(color.A, (int)right[color.R], (int)right[color.R], (int)right[color.R]);
                            else if (average < left[color.R])
                                bmp[i, j] = Color.FromArgb(color.A, (int)left[color.R], (int)left[color.R], (int)left[color.R]);
                            else
                                bmp[i, j] = Color.FromArgb(color.A, (int)average, (int)average, (int)average);
                            break;
                        case EqualizationMethod.Own:
                            bmp[i, j] = Color.FromArgb(color.A, (int)newValue[color.R], (int)newValue[color.R], (int)newValue[color.R]);
                            break;
                    }
                }
            }
        } */
    } 
}
