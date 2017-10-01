package apo07;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class PicturePanel extends JPanel {

	private Image internalImage;
	
	public PicturePanel (Image internalImage) {
		if (internalImage!=null) {
			this.internalImage = internalImage;
		}
	}
	
	public void paintComponent (Graphics g) {
		if (internalImage!=null) {
			g.drawImage(internalImage, 0, 0, this); // redraw on jsplitpane resize
		}
	}
	
	public void setInternalImage(Image internalImage) {
		this.internalImage = internalImage;
		this.getGraphics().drawImage(this.internalImage, 0, 0, this);
	}
	
}
