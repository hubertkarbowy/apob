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

enum MaskType {
	_3x3, _5x5, _3x5, _5x3, _7x7;
}

enum OpType {
	SMOOTHING, SHARPEN, EDGE_DETECT, WHATEVER;
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
	
	public static int tv(int inV) { // cuts off extreme values in saturated images
		if (inV<0) return 0;
		else if (inV>255) return 255;
		else return inV;
	}
	
	public static BufferedImage getMaximalOfTwo(BufferedImage im1, BufferedImage im2) {
		ColorSpace csm = ColorSpace.getInstance( im1.getType() == BufferedImage.TYPE_BYTE_GRAY ? ColorSpace.CS_GRAY : ColorSpace.CS_LINEAR_RGB);
		ComponentColorModel cm = new ComponentColorModel(csm, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		WritableRaster raster = cm.createCompatibleWritableRaster(Math.max(im1.getWidth(), im2.getWidth()), Math.max(im1.getHeight(), im2.getHeight()));
		BufferedImage ret = new BufferedImage(cm, raster, cm.isAlphaPremultiplied(), null);
		return ret;
	}
	
	public static APO07MaskInput.OpObject[] getSmoothing(OpType opSet) {
		
		if (opSet==OpType.SMOOTHING) {
			APO07MaskInput.OpObject smoothen1 = new APO07MaskInput.OpObject(MaskType._3x3, new int[] {1,1,1,1,1,1,1,1,1}, 1);
			APO07MaskInput.OpObject smoothen2 = new APO07MaskInput.OpObject(MaskType._3x3, new int[] {1,1,1,1,2,1,1,1,1}, 1);
			APO07MaskInput.OpObject smoothen3 = new APO07MaskInput.OpObject(MaskType._3x3, new int[] {1,2,1,2,4,2,1,2,1}, 1);
			APO07MaskInput.OpObject[] table = {smoothen1, smoothen2, smoothen3};
			return table;
		}
		else return null;
	}
	//	protected OpObject (MaskType maskType, int[] maskValues, int edgePixels)
	

}
