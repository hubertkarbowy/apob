package apo07;

import static apo07.APO07StaticUtilityMethods.getEmptyLinearImage;
import static apo07.APO07StaticUtilityMethods.getRGBPixelValue;

import java.awt.Color;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.function.BiFunction;

enum ImageType {
	GRAYSCALE, RGB_LINEAR
}

public class APO07StaticUtilityMethods {
	
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}
	
	public static BufferedImage getEmptyLinearImage(int width, int height, ImageType imt) {
		ColorSpace csm = imt==ImageType.GRAYSCALE ? ColorSpace.getInstance(ColorSpace.CS_GRAY) : ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ComponentColorModel cm = new ComponentColorModel(csm, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
		BufferedImage ret = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
		
		return ret;
	}
	
	public static BufferedImage getEmptyLinearImage(BufferedImage im) {
		if (im==null) throw new IllegalArgumentException("Please load an image");
		if (im.getType()==BufferedImage.TYPE_BYTE_GRAY) return getEmptyLinearImage(im.getWidth(), im.getHeight(), ImageType.GRAYSCALE);
		else return getEmptyLinearImage(im.getWidth(), im.getHeight(), ImageType.RGB_LINEAR);
	}
	
	public static BufferedImage getGrayscaleImage(BufferedImage im) {
	BufferedImage tmpImg = getEmptyLinearImage(im.getWidth(), im.getHeight(), ImageType.RGB_LINEAR);
	for (int x=0; x<im.getWidth(); x++) {
		for (int y=0; y<im.getHeight(); y++) {
			Color c = new Color(im.getRGB(x, y));
			int red = (int)(c.getRed() * 0.299);
			int green = (int)(c.getGreen() * 0.587);
			int blue = (int)(c.getBlue() *0.114);
			Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
			tmpImg.setRGB(x,y,newColor.getRGB());							
		}}
		return tmpImg;
	}
	
	public static int tv(int inV) { // cuts off extreme values in saturated images
		if (inV<0) return 0;
		else if (inV>255) return 255;
		else return inV;
	}
	
	public static BufferedImage getMaximalOfTwo(BufferedImage im1, BufferedImage im2) {
		ColorSpace csm = ColorSpace.getInstance( im1.getType() == BufferedImage.TYPE_BYTE_GRAY ? ColorSpace.CS_GRAY : ColorSpace.CS_sRGB);
		ComponentColorModel cm = new ComponentColorModel(csm, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		WritableRaster raster = cm.createCompatibleWritableRaster(Math.max(im1.getWidth(), im2.getWidth()), Math.max(im1.getHeight(), im2.getHeight()));
		BufferedImage ret = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
		return ret;
	}
    
    public static byte[] scaleImage(byte[] imageData, int[] inputArray, ScaleMethod scaleMethod) { /*RIPPED*/
        double min = Arrays.stream(inputArray).min().orElse(0);
        double max = Arrays.stream(inputArray).max().orElse(255);
        System.out.printf("MIN:\t%f MAX:\t%f", min, max);

        if (scaleMethod == ScaleMethod.CUT) { // cut scaling
            for (int i = 0; i < imageData.length; ++i) {
                inputArray[i] = (inputArray[i] < 0 ? 0 : inputArray[i] > 255 ? 255 : inputArray[i]); // skalowanie przez obcinanie
                inputArray[i] += imageData[i] & 0xFF;
                imageData[i] = (byte) (inputArray[i] < 0 ? 0 : inputArray[i] > 255 ? 255 : inputArray[i]); // skalowanie przez obcinanie
            }

        } else if (scaleMethod == ScaleMethod.PROPORTIONAL) { // proportional scaling
            for (int i = 0; i < imageData.length; ++i)
                imageData[i] = (byte) ((((double)inputArray[i] - min) / (max - min)) * 255); // skalowanie proporcjonalne

        } else if (scaleMethod == ScaleMethod.NO_SCALE) { // 3 value scaling
            for (int i = 0; i < imageData.length; ++i)
                imageData[i] = (byte)(inputArray[i] < 0 ? 0 : inputArray[i] == 0 ? 128 : 255);

        } else if(scaleMethod == ScaleMethod.NO_SCALE){ // no scaling
            for (int i = 0; i < imageData.length; ++i)
                imageData[i] = (byte)inputArray[i];
        }
        return imageData;
    }
    
    public static int[] getNeighboringPixels(BufferedImage im, int x, int y, int horiz, int vert, Color whichColor) {
    	
    	int outpixels[] = new int[((2*vert)+1)*((2*horiz)+1)];
    	int counter=0;
    	for (int alty=y-vert; alty<=y+vert; alty++) {
			   for (int altx=x-horiz; altx<=x+horiz; altx++) {
				   try {
					   int clr= im.getRGB(altx,alty);
					   outpixels[counter]=getRGBPixelValue(clr, whichColor);
				   }
				   catch (Exception e) {
					   outpixels[counter]=0;
				   }
				   counter++;
			   }
	    }
    	return outpixels;
    }
    
    public static int getRGBPixelValue(int inPixel, Color whichColor) {
    	if (whichColor==Color.BLUE) return (inPixel & 0x000000ff);
    	else if (whichColor==Color.GREEN) return (inPixel & 0x0000ff00) >> 8;
        else return (inPixel & 0x00ff0000) >> 16;
    }
    
    public static float hadamardProductAndSum(float[] mask1, float[] mask2) {
		if (mask1.length != mask2.length) throw new IllegalArgumentException("Hadamard product: unequal matrices size");
		float result=0.0f;
		for (int x=0; x<mask1.length; x++) result += mask1[x]*mask2[x];
		return result;
	}
    
    public static BufferedImage deepCopy(BufferedImage bi) {
    	 ColorModel cm = bi.getColorModel();
    	 boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    	 WritableRaster raster = bi.copyData(null);
    	 return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    	}

}
