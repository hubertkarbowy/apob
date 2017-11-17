package apo07;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import static apo07.APO07StaticUtilityMethods.*;


enum MaskType {
	_2x2, _3x3, _5x5, _3x5, _5x3, _7x7;
}

enum Direction {
	HORIZONTAL, VERTICAL, ISOLATED;
}

enum OpType {
	SMOOTHING, LAPLACE, SHARPEN, EDGE_DETECT, ROBERTS, SOBEL, WHATEVER;
}

enum ScaleMethod {
	NO_SCALE, PROPORTIONAL, THREE_VALUES, CUT;
}

enum EdgePixels {
	ZERO, LEAVE_UNCHANGED, DUPLICATE;
}

public class APO07NeighborhoodMethods {

		public interface QuadFunction<A, B, C, D, R> {
			R apply(A a, B b, C c, D d);
		}
	
		static TriFunction<int[], float[], Integer, Float> universalNeighborhoodOperator = (neighborhoodPixels, maskPixels, maskSum) -> {
			if (neighborhoodPixels.length != maskPixels.length) throw new IllegalArgumentException("Dimensions of input pixels array and mask pixels array do not match.");  
			int nominator=0;
			float result = 0.0f;
			for (int a=0; a<neighborhoodPixels.length; a++) nominator += (neighborhoodPixels[a]*maskPixels[a]);
			result = (float)nominator/maskSum;
			return result;
		};
		
		static TriFunction<int[], Integer, OpType, Float> robertsAndSobelOperator = (neighborhoodPixels, thisPixel, opType) -> {
			float result = 0.0f;
			if (opType==OpType.ROBERTS) { // Let's cheat and be lazy with a 3x3 mask and a loop. Nobody's life depends on the efficient implementation of Roberts in Java because nobody will code such things for practical purposes in Java anyway...
				result=(float)Math.sqrt(Math.pow(neighborhoodPixels[4]-neighborhoodPixels[8], 2) + Math.pow(neighborhoodPixels[7]-neighborhoodPixels[5], 2));
			}
			else if (opType==OpType.SOBEL)
			{
				float sX = (neighborhoodPixels[2] + 2*neighborhoodPixels[5] + neighborhoodPixels[8])-(neighborhoodPixels[0] + 2*neighborhoodPixels[3] + neighborhoodPixels[6]);
				float sY = (neighborhoodPixels[6] + 2*neighborhoodPixels[7] + neighborhoodPixels[8])-(neighborhoodPixels[0] + 2*neighborhoodPixels[1] + neighborhoodPixels[2]);
				result = (float)Math.sqrt(sX*sX + sY*sY);
			}
			return result;
		};
		
		
		static Function<int[], Integer> getMedian = (neighborhoodPixels) -> {
			int[] sorted = Arrays.copyOf(neighborhoodPixels, neighborhoodPixels.length);
			Arrays.sort(sorted);
			if (sorted.length % 2 == 1) return sorted[(sorted.length-1)/2];
			else {
				int first = sorted[(sorted.length/2)-1]; int second = sorted[(sorted.length/2)];
				return first/second;
			}
		};
		
		static BiFunction<int[], Direction, Integer> logicalFilter = (np, dir) -> {
			if (np.length!=9) throw new IllegalArgumentException("Unsupported mask");
			int a=np[1]; int b=np[3]; int c=np[5]; int d=np[7]; int x=np[4];
			switch (dir) {
				case HORIZONTAL:
					if (a==d) return a;
					else return x;
				case VERTICAL:
					if (b==c) return b;
					else return x;
				case ISOLATED:
					if (a==b & b==c & c==d) return a;
					else return x;
			}
			return x;
		};
		
		public static APO07MaskInput.OpObject[] getMask(OpType opSet) {
			
			if (opSet==OpType.SMOOTHING) {
				APO07MaskInput.OpObject smoothen1 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {1,1,1,1,1,1,1,1,1}, ScaleMethod.NO_SCALE, "<html>3x3 smoothing [boxcar]</html>", OpType.SMOOTHING);
				APO07MaskInput.OpObject smoothen2 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {1,1,1,1,2,1,1,1,1}, ScaleMethod.NO_SCALE, "<html>3x3 smoothing [alt]</html>", OpType.SMOOTHING);
				APO07MaskInput.OpObject smoothen3 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {1,2,1,2,4,2,1,2,1}, ScaleMethod.NO_SCALE, "<html>3x3 smoothing [Gaussian]</html>", OpType.SMOOTHING);
				APO07MaskInput.OpObject smoothen4 = new APO07MaskInput.OpObject(MaskType._7x7, new float[] {0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f, 0.00002292f, 0.00078634f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f, 0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f, 0.00038771f, 0.01330373f, 0.11098164f, 0.22508352f, 0.11098164f, 0.01330373f, 0.00038771f, 0.00019117f, 0.00655965f, 0.05472157f, 0.11098164f, 0.05472157f, 0.00655965f, 0.00019117f, 0.00002292f, 0.00078633f, 0.00655965f, 0.01330373f, 0.00655965f, 0.00078633f, 0.00002292f, 0.00000067f, 0.00002292f, 0.00019117f, 0.00038771f, 0.00019117f, 0.00002292f, 0.00000067f}, ScaleMethod.NO_SCALE, "<html>7x7 smoothing [Gaussian]</html>", OpType.SMOOTHING);
				APO07MaskInput.OpObject[] table = {smoothen1, smoothen2, smoothen3, smoothen4};
				return table;
			}
			if (opSet==OpType.LAPLACE) {
				APO07MaskInput.OpObject laplace1 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {0,1,0,1,-4,1,0,1,0}, ScaleMethod.PROPORTIONAL, "<html>Laplace 1</html>", OpType.LAPLACE);
				APO07MaskInput.OpObject laplace2 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {0,-1,0,-1,4,-1,0,-1,0}, ScaleMethod.THREE_VALUES, "<html>Laplace 2</html>", OpType.LAPLACE);
				APO07MaskInput.OpObject laplace3 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {-1,-1,-1,-1,8,-1,-1,-1,-1}, ScaleMethod.CUT, "<html>Laplace 3</html>", OpType.LAPLACE);
				APO07MaskInput.OpObject laplace4 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {1,-2,1,-2,4,-2,1,-2,1}, ScaleMethod.PROPORTIONAL, "<html>Laplace 4</html>", OpType.LAPLACE);
				APO07MaskInput.OpObject[] table = {laplace1, laplace2, laplace3, laplace4};
				return table;
			}
			if (opSet==OpType.EDGE_DETECT) {
				APO07MaskInput.OpObject ed1 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {1,-2,1,-2,5,-2,1,-2,1}, ScaleMethod.PROPORTIONAL, "<html>Edge detect 1</html>", OpType.EDGE_DETECT);
				APO07MaskInput.OpObject ed2 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {-1,-1,-1,-1,9,-1,-1,-1,-1}, ScaleMethod.PROPORTIONAL, "<html>Edge detect 2</html>", OpType.EDGE_DETECT);
				APO07MaskInput.OpObject ed3 = new APO07MaskInput.OpObject(MaskType._3x3, new float[] {0,-1,0,-1,5,-1,0,-1,0}, ScaleMethod.PROPORTIONAL, "<html>Edge detect 3</html>", OpType.EDGE_DETECT);
				return new APO07MaskInput.OpObject[] {ed1, ed2, ed3};
			}
			else return null;
		}
		
		public static BufferedImage applyMask (BufferedImage inputPic, float[] maskToApply, MaskType maskType, ScaleMethod sm, EdgePixels ep) {
			int startx=1; int starty=1; int endx=inputPic.getWidth()-2; int endy=inputPic.getHeight()-2;  int vert=1; int horiz=1;
			float[][] redChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] greenChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] blueChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			
			BufferedImage ret = APO07StaticUtilityMethods.getEmptyLinearImage(inputPic);
			Raster inraster = inputPic.getRaster();
			WritableRaster outraster = ret.getRaster();

			double[] maskAsDouble = new double[maskToApply.length];
			float maskSum;
			
			if (maskType==MaskType._3x3) {startx=1; starty=1; endx=inputPic.getWidth()-2; endy=inputPic.getHeight()-2; vert=1; horiz=1;}
			else if (maskType==MaskType._3x5) {startx=1; starty=2; endx=inputPic.getWidth()-2; endy=inputPic.getHeight()-3; vert=2; horiz=1;}
			else if (maskType==MaskType._5x5) {startx=2; starty=2; endx=inputPic.getWidth()-3; endy=inputPic.getHeight()-3; vert=2; horiz=2;}
			else if (maskType==MaskType._5x3) {startx=2; starty=1; endx=inputPic.getWidth()-3; endy=inputPic.getHeight()-2; vert=1; horiz=2;}
			else if (maskType==MaskType._7x7) {startx=3; starty=3; endx=inputPic.getWidth()-4; endy=inputPic.getHeight()-4; vert=3; horiz=3;}
			else throw new IllegalArgumentException("No such mask");
			
			for (int n=0; n<maskToApply.length; n++) maskAsDouble[n]=maskToApply[n];
			maskSum = (float)Arrays.stream(maskAsDouble).sum();
			if ((int) maskSum==0) maskSum=1;
			
			
			// First we deal with the edge pixels
			if (ep==null || ep==EdgePixels.ZERO) { // TODO: do poprawy: piksele skrajne
				// do nothing
			}
			else if (ep==EdgePixels.LEAVE_UNCHANGED) {
				outraster.setPixels(0, 0, inputPic.getWidth(), vert, inraster.getPixels(0, 0, inputPic.getWidth(), vert, (int[])null)); // top
			    outraster.setPixels(0, 0, horiz, inputPic.getHeight(), inraster.getPixels(0, 0, horiz, inputPic.getHeight(), (int[])null)); // left
			    outraster.setPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, inraster.getPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, (int[])null)); // bottom
			    outraster.setPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), inraster.getPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), (int[])null)); // bottom
			}
			else if (ep==EdgePixels.DUPLICATE) {
			    outraster.setPixels(0, 0, inputPic.getWidth(), vert, inraster.getPixels(0, 0, inputPic.getWidth(), vert, (int[])null)); // top
			    outraster.setPixels(0, 0, horiz, inputPic.getHeight(), inraster.getPixels(0, 0, horiz, inputPic.getHeight(), (int[])null)); // left
			    outraster.setPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, inraster.getPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, (int[])null)); // bottom
			    outraster.setPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), inraster.getPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), (int[])null)); // bottom
			}
			
			
			// Next we apply the mask to the remaining pixels
			Color[] channels = new Color[] {Color.RED, Color.GREEN, Color.BLUE};
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] newValues = new float[3];
					for (Color c : channels) {
					     int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						 int neighborhood[]=getNeighboringPixels(inputPic, x, y, horiz, vert, c);
						 newValues[colorAsInt]=universalNeighborhoodOperator.apply(neighborhood, maskToApply, (int)maskSum);
						 if (x==21 & y==15) {
							 System.out.print(c==Color.RED ? "R=" : c==Color.GREEN ? "G=" : "B=");
							 System.out.print(getRGBPixelValue(inputPic.getRGB(x, y), c) + " ");
						 }
					}
					if (x==21 & y==15) System.out.println("");
					redChannel[x][y]=newValues[0]; greenChannel[x][y]=newValues[1]; blueChannel[x][y]=newValues[2]; // temporary assignment
				}
			}

			float[] minvalues = new float[3]; float[] maxvalues = new float[3];
			for (int z=0; z<3; z++) minvalues[z]=10000;
			
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] pixelsBeforeRescaling = {redChannel[x][y], greenChannel[x][y], blueChannel[x][y]};
					for (Color c : channels) {
						int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						float outPxChannelValue = pixelsBeforeRescaling[colorAsInt];
						if (outPxChannelValue < minvalues[colorAsInt]) minvalues[colorAsInt] = outPxChannelValue;
						if (outPxChannelValue > maxvalues[colorAsInt]) maxvalues[colorAsInt] = outPxChannelValue;
					}
				}
			}
			
			if (sm!=ScaleMethod.NO_SCALE) {
				for (int y=starty; y<endy; y++) {
					for (int x=startx; x<endx; x++) {
						float[] outPixels = {redChannel[x][y], greenChannel[x][y], blueChannel[x][y]};
						float[] rescaledPixel = new float[3];
						for (Color c : channels) {
							int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
							float outPixelBeforeRescaling=outPixels[colorAsInt];
							if (sm==ScaleMethod.PROPORTIONAL) rescaledPixel[colorAsInt] = ((outPixelBeforeRescaling-minvalues[colorAsInt])/(maxvalues[colorAsInt] - minvalues[colorAsInt]))*255;
						    else if (sm==ScaleMethod.THREE_VALUES) rescaledPixel[colorAsInt] = outPixelBeforeRescaling<0 ? 0 : outPixelBeforeRescaling> 255 ? 255 : 128;
							else if (sm==ScaleMethod.CUT) rescaledPixel[colorAsInt] = outPixelBeforeRescaling < 0 ? 0 : outPixelBeforeRescaling > 255 ? 255 : (int)outPixelBeforeRescaling;
						   }
						if (x==21 && y==15) {
							System.out.println("Before rescaling:");
							System.out.println("R="+redChannel[21][15] + " G="+greenChannel[21][15] + " B="+blueChannel[21][15]);
							System.out.println("Min values: R=" + minvalues[0] + " G="+minvalues[1] + " B="+minvalues[2]);
							System.out.println("Max values: R=" + maxvalues[0] + " G="+maxvalues[1] + " B="+maxvalues[2]);
							System.out.println("After rescaling:\n"+Arrays.toString(rescaledPixel));
						}
						redChannel[x][y]=rescaledPixel[0]; greenChannel[x][y]=rescaledPixel[1]; blueChannel[x][y]=rescaledPixel[2];
					}
				}
			}
			
			for (int y=starty; y<endy; y++) {
				for (int x=startx; x<endx; x++) {
					try {
						Color outColor = new Color((int)redChannel[x][y], (int)greenChannel[x][y], (int)blueChannel[x][y]);
						ret.setRGB(x, y, outColor.getRGB());
					}
					catch (IllegalArgumentException e) {
						System.out.println(e.getMessage() + " " + redChannel[x][y] + " " + greenChannel[x][y] + " " + blueChannel[x][y]);
					}
				}
			}
			return ret;
		}
		
		public static BufferedImage medianFiltering (BufferedImage inputPic, MaskType maskType) {
			int startx=1; int starty=1; int endx=inputPic.getWidth()-2; int endy=inputPic.getHeight()-2;  int vert=1; int horiz=1;
			float[][] redChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] greenChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] blueChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			
			BufferedImage ret = APO07StaticUtilityMethods.getEmptyLinearImage(inputPic);
			Raster inraster = inputPic.getRaster();
			WritableRaster outraster = ret.getRaster();
			
			if (maskType==MaskType._3x3) {startx=1; starty=1; endx=inputPic.getWidth()-2; endy=inputPic.getHeight()-2; vert=1; horiz=1;}
			else if (maskType==MaskType._3x5) {startx=1; starty=2; endx=inputPic.getWidth()-2; endy=inputPic.getHeight()-3; vert=2; horiz=1;}
			else if (maskType==MaskType._5x5) {startx=2; starty=2; endx=inputPic.getWidth()-3; endy=inputPic.getHeight()-3; vert=2; horiz=2;}
			else if (maskType==MaskType._5x3) {startx=2; starty=1; endx=inputPic.getWidth()-3; endy=inputPic.getHeight()-2; vert=1; horiz=2;}
			else if (maskType==MaskType._7x7) {startx=3; starty=3; endx=inputPic.getWidth()-4; endy=inputPic.getHeight()-4; vert=3; horiz=3;}
			else throw new IllegalArgumentException("No such mask");
			
		    // edge pixels
			outraster.setPixels(0, 0, inputPic.getWidth(), vert, inraster.getPixels(0, 0, inputPic.getWidth(), vert, (int[])null)); // top
		    outraster.setPixels(0, 0, horiz, inputPic.getHeight(), inraster.getPixels(0, 0, horiz, inputPic.getHeight(), (int[])null)); // left
		    outraster.setPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, inraster.getPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, (int[])null)); // bottom
		    outraster.setPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), inraster.getPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), (int[])null)); // bottom
			
		    Color[] channels = new Color[] {Color.RED, Color.GREEN, Color.BLUE};
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] newValues = new float[3];
					for (Color c : channels) {
					     int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						 int neighborhood[]=getNeighboringPixels(inputPic, x, y, horiz, vert, c);
						 newValues[colorAsInt]=getMedian.apply(neighborhood);
						 if (x==21 & y==15) {
							 System.out.print(c==Color.RED ? "R=" : c==Color.GREEN ? "G=" : "B=");
							 System.out.print(getRGBPixelValue(inputPic.getRGB(x, y), c) + " ");
						 }
					}
					if (x==21 & y==15) System.out.println("");
					redChannel[x][y]=newValues[0]; greenChannel[x][y]=newValues[1]; blueChannel[x][y]=newValues[2]; // temporary assignment
					Color outColor = new Color((int)redChannel[x][y], (int)greenChannel[x][y], (int)blueChannel[x][y]);
					ret.setRGB(x, y, outColor.getRGB());
				}
			}
			return ret;
		}
		
		public static BufferedImage logicalFiltering (BufferedImage inputPic, Direction dir) {
			int startx=1; int starty=1; int endx=inputPic.getWidth()-2; int endy=inputPic.getHeight()-2;  int vert=1; int horiz=1;
			float[][] redChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] greenChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] blueChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			
			BufferedImage ret = APO07StaticUtilityMethods.getEmptyLinearImage(inputPic);
			Raster inraster = inputPic.getRaster();
			WritableRaster outraster = ret.getRaster();
			
		    // edge pixels
			outraster.setPixels(0, 0, inputPic.getWidth(), vert, inraster.getPixels(0, 0, inputPic.getWidth(), vert, (int[])null)); // top
		    outraster.setPixels(0, 0, horiz, inputPic.getHeight(), inraster.getPixels(0, 0, horiz, inputPic.getHeight(), (int[])null)); // left
		    outraster.setPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, inraster.getPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, (int[])null)); // bottom
		    outraster.setPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), inraster.getPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), (int[])null)); // right
			
		    Color[] channels = new Color[] {Color.RED, Color.GREEN, Color.BLUE};
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] newValues = new float[3];
					for (Color c : channels) {
					     int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						 int neighborhood[]=getNeighboringPixels(inputPic, x, y, horiz, vert, c);
						 newValues[colorAsInt]=logicalFilter.apply(neighborhood, dir);
						 if (x==21 & y==15) {
							 System.out.print(c==Color.RED ? "R=" : c==Color.GREEN ? "G=" : "B=");
							 System.out.print(getRGBPixelValue(inputPic.getRGB(x, y), c) + " ");
						 }
					}
					if (x==21 & y==15) System.out.println("");
					redChannel[x][y]=newValues[0]; greenChannel[x][y]=newValues[1]; blueChannel[x][y]=newValues[2]; // temporary assignment
					Color outColor = new Color((int)redChannel[x][y], (int)greenChannel[x][y], (int)blueChannel[x][y]);
					ret.setRGB(x, y, outColor.getRGB());
				}
			}
			return ret;
		}
		
		public static BufferedImage sobelRoberts (BufferedImage inputPic, OpType opType) {
			int startx=1; int starty=1; int endx=inputPic.getWidth()-2; int endy=inputPic.getHeight()-2;  int vert=1; int horiz=1;
			float[][] redChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] greenChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			float[][] blueChannel = new float[inputPic.getWidth()][inputPic.getHeight()];
			
			BufferedImage ret = APO07StaticUtilityMethods.getEmptyLinearImage(inputPic);
			Raster inraster = inputPic.getRaster();
			WritableRaster outraster = ret.getRaster();
			
		    // edge pixels
			outraster.setPixels(0, 0, inputPic.getWidth(), vert, inraster.getPixels(0, 0, inputPic.getWidth(), vert, (int[])null)); // top
		    outraster.setPixels(0, 0, horiz, inputPic.getHeight(), inraster.getPixels(0, 0, horiz, inputPic.getHeight(), (int[])null)); // left
		    outraster.setPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, inraster.getPixels(0, inputPic.getHeight()-vert-1, inputPic.getWidth(), vert+1, (int[])null)); // bottom
		    outraster.setPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), inraster.getPixels(inputPic.getWidth()-horiz-1, 0, horiz+1, inputPic.getHeight(), (int[])null)); // right
			
		    Color[] channels = new Color[] {Color.RED, Color.GREEN, Color.BLUE};
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] newValues = new float[3];
					for (Color c : channels) {
					     int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						 int neighborhood[]=getNeighboringPixels(inputPic, x, y, horiz, vert, c);
						 newValues[colorAsInt]=robertsAndSobelOperator.apply(neighborhood, getRGBPixelValue(inputPic.getRGB(x, y), c), opType);
						 if (x==21 & y==15) {
							 System.out.print(c==Color.RED ? "R=" : c==Color.GREEN ? "G=" : "B=");
							 System.out.print(getRGBPixelValue(inputPic.getRGB(x, y), c) + " ");
						 }
					}
					if (x==21 & y==15) System.out.println("");
					redChannel[x][y]=newValues[0]; greenChannel[x][y]=newValues[1]; blueChannel[x][y]=newValues[2]; // temporary assignment
//					Color outColor = new Color((int)redChannel[x][y], (int)greenChannel[x][y], (int)blueChannel[x][y]);
//					ret.setRGB(x, y, outColor.getRGB());
				}
			}
			
			float[] minvalues = new float[3]; float[] maxvalues = new float[3];
			for (int z=0; z<3; z++) minvalues[z]=10000;
			
			for (int x=startx; x<endx; x++) {
				for (int y=starty; y<endy; y++) {
					float[] pixelsBeforeRescaling = {redChannel[x][y], greenChannel[x][y], blueChannel[x][y]};
					for (Color c : channels) {
						int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
						float outPxChannelValue = pixelsBeforeRescaling[colorAsInt];
						if (outPxChannelValue < minvalues[colorAsInt]) minvalues[colorAsInt] = outPxChannelValue;
						if (outPxChannelValue > maxvalues[colorAsInt]) maxvalues[colorAsInt] = outPxChannelValue;
					}
				}
			}
			
			ScaleMethod sm = ScaleMethod.CUT;
			if (sm!=ScaleMethod.NO_SCALE) {
				for (int y=starty; y<endy; y++) {
					for (int x=startx; x<endx; x++) {
						float[] outPixels = {redChannel[x][y], greenChannel[x][y], blueChannel[x][y]};
						float[] rescaledPixel = new float[3];
						for (Color c : channels) {
							int colorAsInt = c==Color.GREEN ? 1 : c==Color.BLUE ? 2 : 0;
							float outPixelBeforeRescaling=outPixels[colorAsInt];
							if (sm==ScaleMethod.PROPORTIONAL) rescaledPixel[colorAsInt] = ((outPixelBeforeRescaling-minvalues[colorAsInt])/(maxvalues[colorAsInt] - minvalues[colorAsInt]))*255;
						    else if (sm==ScaleMethod.THREE_VALUES) rescaledPixel[colorAsInt] = outPixelBeforeRescaling<0 ? 0 : outPixelBeforeRescaling> 255 ? 255 : 128;
							else if (sm==ScaleMethod.CUT) rescaledPixel[colorAsInt] = outPixelBeforeRescaling < 0 ? 0 : outPixelBeforeRescaling > 255 ? 255 : (int)outPixelBeforeRescaling;
						   }
						if (x==21 && y==15) {
							System.out.println("Before rescaling:");
							System.out.println("R="+redChannel[21][15] + " G="+greenChannel[21][15] + " B="+blueChannel[21][15]);
							System.out.println("Min values: R=" + minvalues[0] + " G="+minvalues[1] + " B="+minvalues[2]);
							System.out.println("Max values: R=" + maxvalues[0] + " G="+maxvalues[1] + " B="+maxvalues[2]);
							System.out.println("After rescaling:\n"+Arrays.toString(rescaledPixel));
						}
						redChannel[x][y]=rescaledPixel[0]; greenChannel[x][y]=rescaledPixel[1]; blueChannel[x][y]=rescaledPixel[2];
					}
				}
			}
			
			for (int y=starty; y<endy; y++) {
				for (int x=startx; x<endx; x++) {
					try {
						Color outColor = new Color((int)redChannel[x][y], (int)greenChannel[x][y], (int)blueChannel[x][y]);
						ret.setRGB(x, y, outColor.getRGB());
					}
					catch (IllegalArgumentException e) {
						System.out.println(e.getMessage() + " " + redChannel[x][y] + " " + greenChannel[x][y] + " " + blueChannel[x][y]);
					}
				}
			}
			
			return ret;
		}
}
