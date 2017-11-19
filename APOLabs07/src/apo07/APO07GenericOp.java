package apo07;

import java.util.ArrayList;
import java.util.List;

public class APO07GenericOp {
	
	protected MaskType maskType;
	protected NeighborhoodType neighborhoodType;
	protected Direction direction;
	protected OpType opType;
	protected ScaleMethod scaleMethod;
	protected EdgePixels edgePixels;
	float[] customMaskValues;
	
	public APO07GenericOp(ArrayList<Object> zzz) {
		for (Object o : zzz) {
			if (o instanceof MaskType) this.maskType=(MaskType)o;
			if (o instanceof NeighborhoodType) this.neighborhoodType=(NeighborhoodType)o;
			if (o instanceof Direction) this.direction=(Direction)o;
			if (o instanceof OpType) this.opType=(OpType)o;
			if (o instanceof ScaleMethod) this.scaleMethod = (ScaleMethod)o;
			if (o instanceof EdgePixels) this.edgePixels=(EdgePixels)o;
			if (o instanceof float[]) this.customMaskValues=(float[])o;
		}
	}
}
