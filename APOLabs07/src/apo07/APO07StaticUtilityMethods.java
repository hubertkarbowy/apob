package apo07;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

enum ImageType {
	GRAYSCALE, RGB_LINEAR
}

public class APO07StaticUtilityMethods {
	
	public interface TriFunction<A, B, C, R> {
		R apply(A a, B b, C c);
	}
	
	public static BufferedImage getEmptyLinearImage(int width, int height, ImageType imt) {
		ColorSpace csm = imt==ImageType.GRAYSCALE ? ColorSpace.getInstance(ColorSpace.CS_GRAY) : ColorSpace.getInstance(ColorSpace.CS_LINEAR_RGB);
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

}
