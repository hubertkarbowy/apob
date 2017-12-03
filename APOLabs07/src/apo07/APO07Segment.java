package apo07;

import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.JOptionPane;

public class APO07Segment {
	
	static BufferedImage smartSegmentThreshold(BufferedImage im, APO07Hist imHist) {
		
		Map<String,Integer> histDesc = imHist.describeThisHist();
		JOptionPane.showMessageDialog(null, "Peak 1 [" + histDesc.get("peak1idx") + "]=" + histDesc.get("peak1val") + " Peak 2 [" + histDesc.get("peak2idx") + "]=" + histDesc.get("peak2val") + " Valley[" + histDesc.get("valleyidx") + "]="+ histDesc.get("valleyval"));
		
		return null;
	}
	
}
