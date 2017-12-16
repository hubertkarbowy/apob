package apo07;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static apo07.APO07StaticUtilityMethods.*;
import apo07.APO07StaticPointMethods.*;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class APO07Features extends JDialog {
	
	public static void imageAnalysis(BufferedImage bmp) {
        // FastBitmap bmp = ((ImageForm)ActiveMdiChild).Image;

        int perimeter = 0;
        int area = 0;
        int size = bmp.getWidth() * bmp.getHeight();
        double areaNorm = 0.0;
        int[] r0 = new int[] { 0, 0 };
        long m00 = 0;
        long m10 = 0;
        long m01 = 0;
        long m11 = 0;

        for (int x = 0; x < bmp.getWidth(); x++) {
 next:    for (int y = 0; y < bmp.getHeight(); y++) {
                int rpixel = getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                if (rpixel > 0) {
                    area++;
                    m00 += getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    m10 += x * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    m01 += y * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    m11 += x * y * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);

                    for (int i = -1; i < 2; i++) {
                        for (int j = -1; j < 2; j++) {
                            try { 
                            // if (ImageHelper.isPixelValid(x + i, y + j, bmp.Width, bmp.Height)) {
                                int sasiad = getRGBPixelValue(bmp.getRGB(x+i, y+j), Color.RED);
                                if (sasiad == 0) {
                                    perimeter++;
                                    r0[0] += x;
                                    r0[1] += y;
                                    continue next;
                                }
                            }
                            catch (Exception e) {}
                        }
                    }
                }
            }
        }

        if (perimeter > 0) {
            r0[0] = r0[0] / perimeter;
            r0[1] = r0[1] / perimeter;
        }
        areaNorm = (double)area / (double)size;
        double W1 = 2.0 * Math.sqrt((double)area / Math.PI);
        double W2 = (double)perimeter / Math.PI;
        double W3 = (double)perimeter / (2.0 * Math.sqrt((double)area * Math.PI));
        double W9 = (2.0 * Math.sqrt((double)area * Math.PI)) / (double)perimeter;
        int minX = -1;
        int maxX = -1;
        int minY = -1;
        int maxY = -1;
        boolean isSet = false;
        long sumaOdl = 0;
        long M10 = 0;
        long M01 = 0;
        long M11 = 0;

        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {
                int rpixel = getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                if (rpixel > 0) {
                    if (!isSet) {
                        isSet = true;
                        if (minX == -1) minX = x;
                        if (maxX == -1) maxX = x;
                        if (minY == -1) minY = y;
                        if (maxY == -1) maxY = y;
                    }
                    if (x < minX) minX = x;
                    if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    if (y > maxY) maxY = y;

                    M10 += (x - r0[0]) * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    M01 += (y - r0[1]) * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    M11 += (x - r0[0]) * (y - r0[1]) * getRGBPixelValue(bmp.getRGB(x, y), Color.RED);
                    sumaOdl += (long)Math.pow(Math.sqrt(Math.pow(x - r0[0], 2) + Math.pow(y - r0[1], 2)), 2);
                }
            }
        }
        int gabarytX = maxX - minX;
        int gabarytY = maxY - minY;
        int gabaryt;
        if (gabarytX > gabarytY) gabaryt = gabarytX;
        else gabaryt = gabarytY;
        double W8 = (double)gabaryt / (double)perimeter;
        double W4 = (double)area / Math.sqrt(2 * Math.PI * sumaOdl);

        JOptionPane.showMessageDialog(null,
//                        "\nBackground pixels: " + (size - area) +
                        "\nObject area: " + area +
                        "\nObject perimeter: " + perimeter +
                        "\nCentre of mass: " + r0[0] + ", " + r0[1] +
                        "\n\nCirculatity coefficients:\nW1: " + W1 + "\nW2: " + W2 + "\nW3: " + W3 +
                        "\nW4: " + W4 + "\nW8: " + W8 + "\nW9: " + W9 + ""
//                        "\n\nMoment m(0, 0): " + m00 +
//                        "\nMoment m(0, 1): " + m01 +
//                        "\nMoment m(1, 0): " + m10 +
//                        "\nMoment m(1, 1): " + m11 +
//                        "\n\nCentral moment M(1, 0): " + M10 +
//                        "\nCentral moment M(0, 1): " + M01 +
//                        "\nCentral moment M(1, 1): " + M11 +
                        );
    }
	
	public static BufferedImage turtle(BufferedImage bitmap)
    {
		BufferedImage bmp = deepCopy(bitmap);
     

        int[][] rtab = new int[bmp.getWidth()][bmp.getHeight()];
        int[][] gtab = new int[bmp.getWidth()][bmp.getHeight()];
        int[][] btab = new int[bmp.getWidth()][bmp.getHeight()];

        int i, j;
        for (i = 1; i < bmp.getWidth()- 1; i++)
        {
            for (j = 1; j < bmp.getHeight()- 1; j++)
            {
                rtab[i][j] = getRGBPixelValue(bmp.getRGB(i, j), Color.RED);
                gtab[i][j] = getRGBPixelValue(bmp.getRGB(i, j), Color.RED);
                btab[i][j] = getRGBPixelValue(bmp.getRGB(i, j), Color.RED);
            }
        }
        int d = 0;
        int pami = 0, pamj = 0, ja = 0, ia = 0;
        int x, y;
        int[] wynik = new int[bmp.getWidth() * bmp.getHeight()];
        int[] droga = new int[bmp.getWidth() * bmp.getHeight()];
 cont:  for (i = 1; i < bmp.getHeight() - 1; i++)
        {
            for (j = 1; j < bmp.getWidth() - 1; j++)
            {
                if (rtab[j][i] != 0 || gtab[j][i] != 0 || btab[j][i] != 0)
                {
                    ja = j;
                    ia = i;
                    pamj = j;
                    pami = i;
                    wynik[bmp.getWidth() * i + j] = 255;
                    // wynik[bmp.getWidth() * i + j] = bmp.Levels - 1;
                    break cont;
                }
            }
        }
 
        j = pamj;
        i = pami - 1;
        //wynik[bmp.Width * i + j] = 255;
        wynik[bmp.getWidth() * i + j] = 255 - 1;
        droga[d] = 1;
        do
        {
            x = j - pamj;
            y = i - pami;
            pamj = j;
            pami = i;
            d++;
            if (rtab[j][i] != 0 || gtab[j][i] != 0 || btab[j][i] != 0)
            {
                if (x == 0 && y == (-1))
                {
                    j--;
                    droga[d] = 2;
                }
                if (x == 1 && y == 0)
                {
                    i--;
                    droga[d] = 1;
                }
                if (x == 0 && y == 1)
                {
                    j++;
                    droga[d] = 0;
                }
                if (x == (-1) && y == 0)
                {
                    i++;
                    droga[d] = 3;
                }
            }
            else
            {
                if (x == 0 && y == (-1))
                {
                    j++;
                    droga[d] = 0;
                }
                if (x == 1 && y == 0)
                {
                    i++;
                    droga[d] = 3;
                }
                if (x == 0 && y == 1)
                {
                    j--;
                    droga[d] = 2;
                }
                if (x == (-1) && y == 0)
                {
                    i--;
                    droga[d] = 1;
                }
            }
            //wynik[bmp.Width * i + j] = 255;
            wynik[bmp.getWidth() * i + j] = 255;
        }
        while (j != ja || i != ia);
        for (i = 0; i < bmp.getHeight(); i++)
        {
            for (j = 0; j < bmp.getWidth(); j++)
            {
                //if (wynik[bmp.Width * i + j] == 255)
                if (wynik[bmp.getWidth() * i + j] == 255- 1)
                {
                    //rtab[j, i] = 255;
                    rtab[j][i] = 255 / 2;
                    gtab[j][i] = 255 / 2;
                    btab[j][i] = 255 / 2;
                }
            }
        }

        for (i = 0; i < bmp.getWidth()-1; i++)
        {
            for (j = 0; j < bmp.getHeight()-1; j++)
            {
                //bmp[i, j] = Color.FromArgb(rtab[i, j], gtab[i, j], btab[i, j]);
            	Color outColor = new Color(rtab[i][j], gtab[i][j], btab[i][j]);
				bmp.setRGB(i, j, outColor.getRGB());
				
                
            	// bmp[i, j] = (byte)rtab[i, j];
            }
        }
        return bmp;
    }
	
	public static Function<int[],Boolean> pattern3a1 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[0]>0 | neighbors[1]>0 | neighbors[2]>0) {
			if (neighbors[6]>0 | neighbors[7]>0 | neighbors[8]>0) {
				if (neighbors[3]==0 & neighbors[5]==0) {
					fitspattern=true;
				}
			}
		}
		return fitspattern;});
	
	public static Function<int[],Boolean> pattern3a1rot90 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[0]>0 | neighbors[3]>0 | neighbors[6]>0) {
			if (neighbors[2]>0 | neighbors[5]>0 | neighbors[8]>0) {
				if (neighbors[1]==0 & neighbors[7]==0) {
					fitspattern=true;
				}
			}
		}
		return fitspattern;});
	
	public static Function<int[],Boolean> pattern3a2 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[0]>0 | neighbors[1]>0 | neighbors[2]>0 | neighbors[3]>0 | neighbors[6]>0) {
			if (neighbors[5]==0 & neighbors[7]==0 & neighbors[8]==2) {
					fitspattern=true;
			}
		}
		return fitspattern;});
	
	public static Function<int[],Boolean> pattern3a2rot90 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[0]>0 | neighbors[3]>0 | neighbors[6]>0 | neighbors[7]>0 | neighbors[8]>0) {
			if (neighbors[1]==0 & neighbors[5]==0 & neighbors[2]==2) {
					fitspattern=true;
			}
		}
		return fitspattern;});
	
	public static Function<int[],Boolean> pattern3a2rot180 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[2]>0 | neighbors[5]>0 | neighbors[8]>0 | neighbors[7]>0 | neighbors[6]>0) {
			if (neighbors[1]==0 & neighbors[3]==0 & neighbors[0]==2) {
					fitspattern=true;
			}
		}
		return fitspattern;});
	
	public static Function<int[],Boolean> pattern3a2rot270 = (neighbors -> {
		boolean fitspattern=false;
		if (neighbors[0]>0 | neighbors[1]>0 | neighbors[2]>0 | neighbors[5]>0 | neighbors[8]>0) {
			if (neighbors[3]==0 & neighbors[7]==0 & neighbors[6]==2) {
					fitspattern=true;
			}
		}
		return fitspattern;});
	
//	public static Function<int[], Boolean>[] all_patterns = new Function<int[], Boolean>[6]; 
		
		// {pattern3a1, pattern3a1rot90, pattern3a2, pattern3a2rot90, pattern3a2rot180, pattern3a2rot270};
	
	public static BufferedImage thinning(BufferedImage in) {
		boolean remain=true;
		BufferedImage out = deepCopy(in);
		int width=in.getWidth();
		int height=in.getHeight();
		List<Function<int[], Boolean>> all_patterns = new ArrayList<>();
		all_patterns.add(pattern3a1); all_patterns.add(pattern3a1rot90);
		all_patterns.add(pattern3a2); all_patterns.add(pattern3a2rot90); all_patterns.add(pattern3a2rot180); all_patterns.add(pattern3a2rot270);
		
		
		while (remain) {
			remain=false;
			boolean skel=false;
			int[] jtab = new int[] {0,2,4};
			for (int jpx : jtab) { // Step 4
				for (int y=2; y<height-2; y++) { // Step 5a
					for (int x=2; x<width-2; x++) { // Step 5b - to hell with edge pixels
						 int ppix=out.getRGB(x, y);
						 int p=getRGBPixelValue(ppix, Color.RED);
						 
						 int[] neighbors = getNeighboringPixels(out, x, y, 1, 1, Color.RED);
						 int j=getRGBPixelValue(neighbors[jpx], Color.RED);
						 if (p==254 && j==0) {  // step 6
							 skel=false;
							 for (Function<int[], Boolean> pattern : all_patterns) { // Step 9
								 if (pattern.apply(neighbors)) {skel=true; break;}
							 }
							 if (skel==true) out.setRGB(x, y, new Color(2,2,2).getRGB()); // Step 10
							 else {out.setRGB(x, y, new Color(3,3,3).getRGB()); remain=true;}
							 }
						 } // TODO: Shape generator with parameters
					}
				for (int y=2; y<height-2; y++) { // Step 11
					for (int x=2; x<width-2; x++) { 
						if (getRGBPixelValue(out.getRGB(x, y), Color.RED)==3) out.setRGB(x,y,0);
						System.out.println("pval2=" + getRGBPixelValue(out.getRGB(x, y), Color.RED));
					}
				}
				}
			}
		return out;
	}
}
