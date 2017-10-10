package apo07;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;

public class APO07StaticHistMethods {
	
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
	public static BufferedImage histEqualize(ImageIcon inputPic, int[] lut, int method) {
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
        
        return null;

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
