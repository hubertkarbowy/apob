package apo07;

import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class HistWindow extends JFrame {
	private APO07Hist Hist1Panel;
	private APO07Hist Hist2Panel;
	private APO07Hist HistOutPanel;
	
	public HistWindow() {
		setBounds(new Rectangle(0, 0, 800, 300));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		Hist1Panel = new APO07Hist();
		Hist1Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(Hist1Panel);
		
		Hist2Panel = new APO07Hist();
		Hist2Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(Hist2Panel);
		
		HistOutPanel = new APO07Hist();
		HistOutPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		getContentPane().add(HistOutPanel);
	}

}
