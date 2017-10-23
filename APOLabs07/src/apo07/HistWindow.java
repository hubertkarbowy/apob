package apo07;

import javax.swing.JFrame;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class HistWindow extends JFrame {
	private APO07Hist Hist1Panel;
	private APO07Hist Hist2Panel;
	private JPanel upperPanel;
	private JPanel lowerPanel;
	private JPanel leftHistPanel;
	private JPanel rightHistPanel;
	private JLabel leftHistLabel;
	private JComboBox leftHistChooser;
	private JLabel rightHistLabel;
	private JComboBox rightHistChooser;
	private BufferedImage im1;
	private BufferedImage im2;
	private BufferedImage imout;
	private JCheckBox chckbxBez0;
	private JCheckBox chckbxBez1;
	
	
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
		
		lowerPanel = new JPanel();
		lowerPanel.setMaximumSize(new Dimension(32767, 40));
		lowerPanel.setPreferredSize(new Dimension(10, 40));
		getContentPane().add(lowerPanel);
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.X_AXIS));
		
		leftHistPanel = new JPanel();
		lowerPanel.add(leftHistPanel);
		
		leftHistLabel = new JLabel("Lewy histogram:");
		leftHistPanel.add(leftHistLabel);
		
		leftHistChooser = new JComboBox();
		leftHistChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (leftHistChooser.getSelectedIndex()) {
					case 0:
						Hist1Panel.setNewImage(im1);
						break;
					case 1:
						Hist1Panel.setNewImage(im2);
						break;
					default:
						Hist1Panel.setNewImage(imout);
						break;
				}
			}
		});
		leftHistChooser.setModel(new DefaultComboBoxModel(new String[] {"Obraz 1", "Obraz 2", "Obraz Wy"}));
		leftHistChooser.setSelectedIndex(0);
		leftHistPanel.add(leftHistChooser);
		
		rightHistPanel = new JPanel();
		lowerPanel.add(rightHistPanel);
		
		rightHistLabel = new JLabel("Prawy histogram:");
		rightHistPanel.add(rightHistLabel);
		
		rightHistChooser = new JComboBox();
		rightHistChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				switch (rightHistChooser.getSelectedIndex()) {
				case 0:
					Hist2Panel.setNewImage(im1);
					break;
				case 1:
					Hist2Panel.setNewImage(im2);
					break;
				default:
					Hist2Panel.setNewImage(imout);
					break;
			}
			}
		});
		rightHistChooser.setModel(new DefaultComboBoxModel(new String[] {"Obraz 1", "Obraz 2", "Obraz Wy"}));
		rightHistChooser.setSelectedIndex(2); 
		rightHistPanel.add(rightHistChooser);
		
		chckbxBez0 = new JCheckBox("Bez 0");
		chckbxBez0.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxBez0.isSelected()) Hist2Panel.toggleExtremes((byte)0, true);
				else Hist2Panel.toggleExtremes((byte)0, false);
				Hist2Panel.repaint();
				Hist2Panel.revalidate();
			}
		});
		rightHistPanel.add(chckbxBez0);
		
		chckbxBez1 = new JCheckBox("Bez 1");
		chckbxBez1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (chckbxBez1.isSelected()) Hist2Panel.toggleExtremes((byte)1, true);
				else Hist2Panel.toggleExtremes((byte)1, false);
				Hist2Panel.repaint();
				Hist2Panel.revalidate();
			}
		});
		rightHistPanel.add(chckbxBez1);
		
		this.im1=null; this.im2=null; this.imout=null;
		setLocationRelativeTo(null);
	}
	
	public HistWindow(BufferedImage im1)
	{
		this();
		this.im1=im1; this.im2=null; this.imout=null;
		Hist1Panel.setNewImage(im1);
	}
	
	public HistWindow(BufferedImage im1, BufferedImage im2)
	{
		this(im1); this.im2=im2;
		// this.im1=im1; this.im2=im2; this.imout=null;
		// Hist1Panel.setNewImage(im1);
		Hist2Panel.setNewImage(im2);
	}
	
	public HistWindow(BufferedImage im1, BufferedImage im2, BufferedImage imout) // todo: wykomentowac ost linie zmieniajac na imout
	{
		this(im1, im2);
		// this.im1=im1; this.im2=im2; this.imout=null;
		this.imout=imout;
//		Hist1Panel.setNewImage(im1);
		Hist2Panel.setNewImage(imout);
	}
	
	public void updateHistograms(BufferedImage im1, BufferedImage im2, BufferedImage imout) {
		this.im1=im1; this.im2=im2; this.imout=imout;
		
		int currentSelection1 = leftHistChooser.getSelectedIndex();
		int currentSelection2 = rightHistChooser.getSelectedIndex();
		if (currentSelection1==0) Hist1Panel.setNewImage(this.im1);
		if (currentSelection1==1) Hist1Panel.setNewImage(this.im2);
		if (currentSelection1==2) Hist1Panel.setNewImage(this.imout);
		if (currentSelection2==0) Hist2Panel.setNewImage(this.im1);
		if (currentSelection2==1) Hist2Panel.setNewImage(this.im2);
		if (currentSelection2==2) Hist2Panel.setNewImage(this.imout);
	}
}
