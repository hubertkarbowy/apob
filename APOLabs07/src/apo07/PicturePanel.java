package apo07;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class PicturePanel extends JPanel {

	private Image internalImage;
	
	public PicturePanel (Image internalImage) {
		if (internalImage!=null) {
			this.internalImage = internalImage;
		}
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		if (internalImage!=null) {
			g.drawImage(internalImage, 0, 0, this); // redraw on jsplitpane resize
		}
		else {
			g.clearRect(getX(), getY(), getWidth(), getHeight()); 
		}
	}
	
	public void setInternalImage(BufferedImage internalImage) {
		Dimension internalImageDimension = new Dimension(internalImage.getWidth(), internalImage.getHeight());
		this.internalImage = internalImage;
		if (this.internalImage==null) System.out.println("NULL");
		else System.out.println("NOT NULL");
		if (this.getGraphics()==null) System.out.println("GRAPHICS NULL");
		else System.out.println("GRAPHICS NOT NULL");
		this.getGraphics().drawImage(this.internalImage, 0, 0, null);
		this.setSize(internalImage.getWidth(null), internalImage.getWidth(null));
		this.setPreferredSize(internalImageDimension);
		this.setMaximumSize(internalImageDimension);
	}
	
	public void clearInternalImage() {
		if (this.internalImage==null) return;
		this.getGraphics().clearRect(0, 0, this.internalImage.getWidth(null), this.internalImage.getHeight(null));
		this.repaint();
		this.internalImage = null;
		this.getGraphics().dispose();
		this.setSize(10,10);
		this.setPreferredSize(new Dimension(10,10));
		this.repaint();
	}
	
}
