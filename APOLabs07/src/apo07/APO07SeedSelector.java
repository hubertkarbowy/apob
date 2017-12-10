package apo07;

import javax.swing.JFrame;
import java.awt.Rectangle;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.Image;
import java.awt.Point;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ListModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static apo07.APO07StaticUtilityMethods.*;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import java.awt.Dialog.ModalityType;

class Point1 extends Point {
	int x;
	int y;
	
	Point1(int x, int y ) {
		this.x=x;
		this.y=y;
	}
	
	public double getX() {return x;}
	public double getY() {return y;}
	
	@Override
	public int hashCode() {
		return x*31 + y*17;
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Point1)) return false;
		Point1 theother = (Point1) other;
		if (this.x==theother.x && this.y==theother.y) return true;
		else return false;
	}
}

public class APO07SeedSelector extends JDialog {
	private JLabel lblPleaseSelectThe;
	private JButton btnDone;
	private JButton btnCancel;
	private JButton btnRemove;
	private JScrollPane scrollPane;
	private PicturePanel picturePanel;
	private BufferedImage inputimage;
	private BufferedImage outimage;
	private JPanel panel;
	private DefaultListModel deflistmodel = new DefaultListModel<>();
	private JList list;
	private JSpinner spinner;
	private JLabel lblThresholdValue;
	int threshold;
	
	public APO07SeedSelector(BufferedImage im) {
	//	setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setBounds(new Rectangle(0, 0, 800, 500));
		
		this.inputimage=im;
		outimage = deepCopy(im);
		
		
		panel = new JPanel();
		panel.setBounds(0, 500, 500, 300);
		setContentPane(panel);
		
		
		panel.setLayout(null);
		
		lblPleaseSelectThe = new JLabel("Click the image to select the seeds");
		lblPleaseSelectThe.setBounds(45, 10, 248, 15);
		panel.add(lblPleaseSelectThe);
		
		btnDone = new JButton("Done");
		btnDone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			for (int i=0; i<deflistmodel.getSize(); i++) {
				String[] pixelslist=((String)deflistmodel.get(i)).split("\\*");
				int x = Integer.parseInt(pixelslist[0]);
				int y = Integer.parseInt(pixelslist[1]);
				growRegionFromSeed(x, y);
				
			}
		}});
		btnDone.setBounds(647, 28, 71, 25);
		panel.add(btnDone);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}});
		btnCancel.setBounds(526, 28, 81, 25);
		panel.add(btnCancel);
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!list.isSelectionEmpty()) {
					deflistmodel.removeElement(list.getSelectedValue());
				}
			}
		});
		btnRemove.setBounds(402, 28, 89, 25);
		panel.add(btnRemove);
		
		list = new JList(deflistmodel);
		list.setBounds(28, 32, 348, 58);
		panel.add(list);
		
		picturePanel = new PicturePanel(null);
		picturePanel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				deflistmodel.addElement(e.getX()+"*"+e.getY());
			}
		});
		picturePanel.setBounds(46, 72, 10, 10);
		
		scrollPane = new JScrollPane(picturePanel);
		scrollPane.setBounds(56, 102, 699, 344);
		scrollPane.setVisible(true);
		
		panel.add(scrollPane);
		spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(40, 0, 255, 1));
		spinner.setBounds(536, 65, 44, 25);
		panel.add(spinner);
		
		lblThresholdValue = new JLabel("Threshold value");
		lblThresholdValue.setBounds(412, 65, 140, 15);
		panel.add(lblThresholdValue);
		
		
		setVisible(true);
		picturePanel.setVisible(true);
		picturePanel.setInternalImage(this.inputimage);
		picturePanel.setInternalImage(this.inputimage);
	}
	
	private void growRegionFromSeed(int x, int y) {
		BufferedImage grayscaleCopy = getGrayscaleImage(outimage); // nie input!
		BufferedImage outImg = deepCopy(outimage); // nie input!
		int[][] regionrepres = new int[grayscaleCopy.getWidth()][grayscaleCopy.getHeight()];
		int[][] borderrepres = new int[grayscaleCopy.getWidth()][grayscaleCopy.getHeight()];
		CopyOnWriteArrayList<String> region = new CopyOnWriteArrayList<>();
		CopyOnWriteArrayList<String> border = new CopyOnWriteArrayList<>();
		threshold = (Integer)(spinner.getValue());
		
		region.add(""+x+"x"+y);
		border.add(""+x+"x"+y);
		
		regionrepres[x][y] = 2; // 0 = unseen, 1 = seen, not in region, 2 = seen, in region
		borderrepres[x][y] = 2; // 0 = not border, 2 = border

		int clr=grayscaleCopy.getRGB(x,y);
		double averageRegionIntensity = getRGBPixelValue(clr, Color.RED);
		
		
		boolean noNewNeighbors=false;
		
		while (!noNewNeighbors) {
			noNewNeighbors=true;
			// averageRegionIntensity=region.stream().collect(Collectors.averagingDouble((Point p) -> getRGBPixelValue(grayscaleCopy.getRGB((int)p.getX(), (int)p.getY()), Color.RED)));
			double sumOfIntensities=0.0;
			for (int ay=0; ay<grayscaleCopy.getWidth(); ay++) {
				for (int ax=0; ax<grayscaleCopy.getWidth(); ax++) {
					try {
						if (regionrepres[ax][ay]==2) sumOfIntensities += getRGBPixelValue(grayscaleCopy.getRGB(ax, ay), Color.RED);
					}
					catch (ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
			averageRegionIntensity=sumOfIntensities/region.size();
			System.out.println("Average intensity = " + averageRegionIntensity);

			// for (String p : border) {
			for (int ay=0; ay<grayscaleCopy.getWidth()-1; ay++) {
				for (int ax=0; ax<grayscaleCopy.getWidth()-1; ax++) {
					System.out.println("X="+ax +"Y="+ay);
					if (borderrepres[ax][ay]==2) {
						int[] neighborhood = getNeighboringPixels(grayscaleCopy, ax, ay, 1, 1, Color.RED);
						
						if (Math.abs(neighborhood[0] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax-1][ay-1]==0) {borderrepres[ax-1][ay-1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax-1][ay-1]==0) regionrepres[ax-1][ay-1]=1;
						}
						
						if (Math.abs(neighborhood[1] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax][ay-1]==0) {borderrepres[ax][ay-1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax][ay-1]==0) regionrepres[ax][ay-1]=1;
						}
						
						if (Math.abs(neighborhood[2] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax+1][ay-1]==0) {borderrepres[ax+1][ay-1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax+1][ay-1]==0) regionrepres[ax+1][ay-1]=1;
						}
						
						if (Math.abs(neighborhood[3] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax-1][ay]==0) {borderrepres[ax-1][ay]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax-1][ay]==0) regionrepres[ax-1][ay]=1;
						}
						
						// czwarty nie 
						
						if (Math.abs(neighborhood[5] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax+1][ay]==0) {borderrepres[ax+1][ay]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax+1][ay]==0) regionrepres[ax+1][ay]=1;
						}
						
						if (Math.abs(neighborhood[6] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax-1][ay+1]==0) {borderrepres[ax-1][ay+1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax-1][ay+1]==0) regionrepres[ax-1][ay+1]=1;
						}
						
						if (Math.abs(neighborhood[7] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax][ay+1]==0) {borderrepres[ax][ay+1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax][ay+1]==0) regionrepres[ax][ay+1]=1;
						}
						
						if (Math.abs(neighborhood[8] - averageRegionIntensity) < threshold) {
							if (regionrepres[ax+1][ay+1]==0) {borderrepres[ax+1][ay+1]=2; noNewNeighbors=false;}}
						else {
							if (regionrepres[ax+1][ay+1]==0) regionrepres[ax+1][ay+1]=1;
						}
						
						if (!noNewNeighbors) System.out.println("Cos tam dodano");
						if (Math.abs(neighborhood[4] - averageRegionIntensity) < threshold) {regionrepres[ax][ay]=2; borderrepres[ax][ay]=0;}
						}
				}
				}
		
		for (int ay=0; ay<grayscaleCopy.getWidth(); ay++) {
			for (int ax=0; ax<grayscaleCopy.getWidth(); ax++) {
			   if (regionrepres[ax][ay]==2) {
				   outimage.setRGB(ax, ay, Color.BLACK.getRGB()); // hmhmhm... chyba jednak na kopii trzeba((String)deflistmodel.getElementAt(i)).split("*")
				   System.out.println("x="+ax+" y="+ay);
			   }
			}
		}
		
	} // end of while no new neighbors
		picturePanel.setInternalImage(outimage);
	}
}
