package apo07;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import javax.imageio.ImageIO;


import apo07.APO07StaticHistMethods.*;

import java.awt.event.ActionListener;
import java.awt.image.*;

import static apo07.APO07StaticPointMethods.*;
import static apo07.APO07StaticUtilityMethods.*;

public class MainScreen extends JFrame {
	private JMenuBar menuBar;
	private JMenu mnPlik;
	private JMenu mnLab1;
	private JSplitPane allPicsPanel;
	private JSplitPane panelInputPics;
	private PicturePanel panelFirstInputPic;
	private PicturePanel panelSecondInputPic;
	private PicturePanel panelOutputPic;
	private JMenuItem mnOpenPic1;
	private JMenuItem mnOpenPic2;
	
	private BufferedImage firstInputBuff;
	private BufferedImage secondInputBuff;
	private BufferedImage outputBuff;
	
	private JScrollPane panelFirstInputScrollPane;
	private JScrollPane panelSecondInputScrollPane;
	private JScrollPane panelOutputScrollPane;
	
	private JPanel toolsPanel;
	private JButton histButton;
	private JLabel lblNarzdzia;
	private JPanel clearPanel;
	private JLabel lblCzy;
	private JButton btnClrPic1;
	private JButton btnClrPic2;
	private JButton btnClrPicOut;
	private JMenuItem mntmEq1;
	
	private HistWindow histWindowSingleton;
	private JButton btnZamieNaCzb;
	private JMenuItem mntmEqualizeown;
	private JMenu mnLab;
	private JMenuItem mntmInvert;
	private JMenuItem mntmThreshold;
	private JMenuItem mntmThresholdrange;
	private JMenuItem mntmStretch;
	
	private enum PicturePanelAsEnum {INPUT_1, INPUT_2, OUTPUT};

	

	/**
	 * One bloated ugly constructor
	 */
	public MainScreen() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setPreferredSize(new Dimension(900, 600));
		setTitle("Algorytmy Przetwarzania Obrazów 07");
		setMinimumSize(new Dimension(900, 600));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 591, 322);
		JPanel MainScreenMainPanel = new JPanel();
		MainScreenMainPanel.setPreferredSize(new Dimension(900, 600));
		MainScreenMainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainScreenMainPanel);
		MainScreenMainPanel.setLayout(new BoxLayout(MainScreenMainPanel, BoxLayout.Y_AXIS));
		MainScreenMainPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		menuBar = new JMenuBar();
		menuBar.setMaximumSize(new Dimension(500, 30));
		
		mnPlik = new JMenu("Plik");
		mnPlik.setMnemonic('p');
		menuBar.add(mnPlik);
		
		JMenuItem mnExit = new JMenuItem("Wyjdź");
		mnExit.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
		
		mnOpenPic1 = new JMenuItem("Otwórz obraz 1");
		mnOpenPic1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
		mnOpenPic1.addActionListener((ActionEvent event) -> {
			loadPicIntoPanel(panelFirstInputPic, PicturePanelAsEnum.INPUT_1);
        });
		
		
		mnOpenPic1.setMnemonic('1');
		mnPlik.add(mnOpenPic1);
		
		mnOpenPic2 = new JMenuItem("Otwórz obraz 2");
		mnOpenPic2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
		mnOpenPic2.setMnemonic('2');
		mnOpenPic2.addActionListener((ActionEvent event) -> {
			loadPicIntoPanel(panelSecondInputPic, PicturePanelAsEnum.INPUT_2);
        });
		
		
		mnPlik.add(mnOpenPic2);
		mnPlik.add(mnExit);
		
		mnLab1 = new JMenu("Lab 1");
		mnLab1.setMnemonic('1');
		menuBar.add(mnLab1);
		
		mntmEq1 = new JMenuItem("Equalize (avg)");
		mntmEq1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		mntmEq1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null) {
					JOptionPane.showMessageDialog(null, "Please load the first picture.");
					return;
				}
				BufferedImageHistogram bih = new BufferedImageHistogram(firstInputBuff);
				outputBuff = apo07.APO07StaticHistMethods.histEqualize(bih, 1);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmEq1.setMnemonic('a');
		mnLab1.add(mntmEq1);
		
		mntmEqualizeown = new JMenuItem("Equalize (subtr)");
		mntmEqualizeown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null) { JOptionPane.showMessageDialog(null, "Please load the first picture."); return; }
				BufferedImageHistogram bih = new BufferedImageHistogram(firstInputBuff);
				outputBuff = apo07.APO07StaticHistMethods.histEqualize(bih, 2); 
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmEqualizeown.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnLab1.add(mntmEqualizeown);
		
		// MainScreenMainPanel.add(menuBar);
		setJMenuBar(menuBar);
		
		mnLab = new JMenu("Lab 2");
		mnLab.setMnemonic('2');
		menuBar.add(mnLab);
		
		mntmInvert = new JMenuItem("Invert");
		mntmInvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (firstInputBuff==null) { JOptionPane.showMessageDialog(null, "Please load the first picture."); return; }
				else {
					// panelOutputPic.setInternalImage(negateImg(firstInputBuff));
					outputBuff = invertImg(firstInputBuff);
					panelOutputPic.setInternalImage(outputBuff);
					notifyHistWindow();
				}
			}
		});
		mntmInvert.setMnemonic('n');
		mnLab.add(mntmInvert);
		
		mntmThreshold = new JMenuItem("Threshold");
		mntmThreshold.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (firstInputBuff==null) { JOptionPane.showMessageDialog(null, "Please load the first picture."); return; }
				else {
					outputBuff = thresholdImg(firstInputBuff);
					panelOutputPic.setInternalImage(outputBuff);
					notifyHistWindow();
				}
			}
		});
		mntmThreshold.setMnemonic('t');
		mnLab.add(mntmThreshold);
		
		mntmThresholdrange = new JMenuItem("Threshold (range)");
		mntmThresholdrange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputBuff = thresholdRangeImg(firstInputBuff);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmThresholdrange.setMnemonic('r');
		mnLab.add(mntmThresholdrange);
		
		mntmStretch = new JMenuItem("Stretch");
		mntmStretch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
				outputBuff = stretchToRange(firstInputBuff);
				panelOutputPic.setInternalImage(outputBuff);
				}
				catch (IllegalArgumentException e) {
					JOptionPane.showMessageDialog(null, e.getMessage());
				}
				notifyHistWindow();
			}
		});
		mntmStretch.setMnemonic('s');
		mnLab.add(mntmStretch);
		
		panelFirstInputPic = new PicturePanel(null);
		panelSecondInputPic = new PicturePanel(null);
		
		panelOutputPic = new PicturePanel(null);
		
		panelFirstInputScrollPane = new JScrollPane(panelFirstInputPic);
		panelSecondInputScrollPane = new JScrollPane(panelSecondInputPic);
		panelOutputScrollPane = new JScrollPane(panelOutputPic);
		
		panelInputPics = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFirstInputScrollPane, panelSecondInputScrollPane);
		panelInputPics.setResizeWeight(0.5);
		
		allPicsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInputPics, panelOutputScrollPane);
		allPicsPanel.setResizeWeight(0.4);
		
		// TOOLS PANEL
		
		toolsPanel = new JPanel();
		toolsPanel.setPreferredSize(new Dimension(10, 40));
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
		
		lblNarzdzia = new JLabel("Narzędzia:");
		toolsPanel.add(lblNarzdzia);
		
		histWindowSingleton = new HistWindow(firstInputBuff, secondInputBuff, outputBuff);
		histWindowSingleton.setVisible(false);
		
		histButton = new JButton("Hist");
		histButton.setMnemonic('h');
		histButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	           histWindowSingleton.setVisible(true);
				
			}
		});
		toolsPanel.add(histButton);
		
		
		MainScreenMainPanel.add(toolsPanel);
		toolsPanel.setAlignmentX(LEFT_ALIGNMENT);
		
		btnZamieNaCzb = new JButton("Change2BW");
		btnZamieNaCzb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage tmpImg;
				if (firstInputBuff!=null) {
					tmpImg = new BufferedImage(firstInputBuff.getWidth(), firstInputBuff.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
					tmpImg.getGraphics().drawImage(firstInputBuff, 0, 0, null);
		             // convert the original colored image to grayscale
		            // ColorConvertOp op = new ColorConvertOp(firstInputBuff.getColorModel().getColorSpace(), tmpImg.getColorModel().getColorSpace(),null);
		            // op.filter(firstInputBuff,tmpImg);
					
					
					firstInputBuff=tmpImg;
					panelFirstInputPic.setInternalImage(firstInputBuff);
				}
				
				if (secondInputBuff!=null) {
					tmpImg = new BufferedImage(secondInputBuff.getWidth(), secondInputBuff.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
					tmpImg.getGraphics().drawImage(secondInputBuff, 0, 0, null);
					secondInputBuff=tmpImg;
					panelSecondInputPic.setInternalImage(secondInputBuff);
				}
				notifyHistWindow();
			}
		});
		btnZamieNaCzb.setMnemonic('c');
		toolsPanel.add(btnZamieNaCzb);
		
		clearPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) clearPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.RIGHT);
		toolsPanel.add(clearPanel);
		
		lblCzy = new JLabel("Czyść:");
		clearPanel.add(lblCzy);
		
		btnClrPic1 = new JButton("Obraz 1");
		btnClrPic1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				firstInputBuff = null;
				panelFirstInputPic.clearInternalImage();
				notifyHistWindow();
			}
		});
		clearPanel.add(btnClrPic1);
		
		btnClrPic2 = new JButton("Obraz 2");
		btnClrPic2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				secondInputBuff = null;
				panelSecondInputPic.clearInternalImage();
				notifyHistWindow();
			}
		});
		clearPanel.add(btnClrPic2);
		
		btnClrPicOut = new JButton("Obraz out");
		btnClrPicOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				outputBuff = null;
				panelOutputPic.clearInternalImage();
				notifyHistWindow();
			}
		});
		clearPanel.add(btnClrPicOut);
		MainScreenMainPanel.add(allPicsPanel);
	}
	
	private void loadPicIntoPanel(PicturePanel whichPanel, PicturePanelAsEnum panelEnum) {
		JFileChooser fileChooser = new JFileChooser();
		try {
		int openStatus = fileChooser.showOpenDialog(null);
		if (openStatus == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Otwieranie pliku" + file.toString());
            if (panelEnum==PicturePanelAsEnum.INPUT_1) { 
            	BufferedImage tempImage = ImageIO.read(file);
            	firstInputBuff = getEmptyLinearImage(tempImage.getWidth(), tempImage.getHeight(), tempImage.getType()==BufferedImage.TYPE_BYTE_GRAY ? ImageType.GRAYSCALE : ImageType.RGB_LINEAR);
            	firstInputBuff.getGraphics().drawImage(tempImage, 0, 0, null); // workaround to use linear rgb colorspace and strip alpha channel
            	tempImage=null;
                whichPanel.setInternalImage(firstInputBuff);
            }
            if (panelEnum==PicturePanelAsEnum.INPUT_2) { 
            	BufferedImage tempImage = ImageIO.read(file);
            	secondInputBuff = getEmptyLinearImage(tempImage.getWidth(), tempImage.getHeight(), tempImage.getType()==BufferedImage.TYPE_BYTE_GRAY ? ImageType.GRAYSCALE : ImageType.RGB_LINEAR);
            	secondInputBuff.getGraphics().drawImage(tempImage, 0, 0, null); // workaround to use linear rgb colorspace and strip alpha channel
            	tempImage=null;
                whichPanel.setInternalImage(secondInputBuff);
            }
            notifyHistWindow();
            
        } else {
            // cancel clicked
        }
		}
		catch (IOException e) {}
	}
	
	private void setOutPic(BufferedImage imgToSet) {
		outputBuff = imgToSet; 
		panelOutputPic.setInternalImage(outputBuff);
		notifyHistWindow();
	}
	
	private void notifyHistWindow() {
		histWindowSingleton.updateHistograms(firstInputBuff, secondInputBuff, outputBuff);
	}
}
