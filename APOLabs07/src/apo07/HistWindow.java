package apo07;

import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class HistWindow extends JFrame {
	private APO07Hist Hist1Panel;
	private APO07Hist Hist2Panel;
	private JPanel upperPanel;
	private JPanel panel;
	
	public HistWindow() {
		setBounds(new Rectangle(0, 0, 1200, 600));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		upperPanel = new JPanel();
		getContentPane().add(upperPanel);
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		
		Hist1Panel = new APO07Hist();
		upperPanel.add(Hist1Panel);
		Hist1Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		Hist2Panel = new APO07Hist();
		upperPanel.add(Hist2Panel);
		Hist2Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		Hist2Panel.setHistTile("Histogram 2 (klasa)");
		
		panel = new JPanel();
		panel.setMaximumSize(new Dimension(32767, 100));
		panel.setPreferredSize(new Dimension(10, 100));
		getContentPane().add(panel);
	}
	
	public HistWindow(BufferedImage im1)
	{
		this();
		Hist1Panel.setNewImage(im1);
	}
	
	public HistWindow(BufferedImage im1, BufferedImage im2)
	{
		this();
		Hist1Panel.setNewImage(im1);
		Hist2Panel.setNewImage(im2);
	}

}
