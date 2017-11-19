package apo07;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;

import javax.imageio.ImageIO;


import apo07.APO07StaticHistMethods.*;

import java.awt.event.ActionListener;
import java.awt.image.*;

import static apo07.APO07StaticPointMethods.*;
import static apo07.APO07StaticUtilityMethods.*;
import static apo07.APO07NeighborhoodMethods.*;

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
	
	protected BufferedImage firstInputBuff;
	protected BufferedImage secondInputBuff;
	protected BufferedImage outputBuff;
	
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
	private JMenuItem mntmDownsample;
	private JMenu mnLogical;
	private JMenuItem mntmAdd;
	private JMenuItem mntmSubtract;
	private JMenuItem mntmDiff;
	private JMenuItem mntmAnd;
	private JMenuItem mntmOr;
	private JMenuItem mntmXor;
	private JMenu mnLab_1;
	private JMenuItem mntmSmoothing;
	
	private enum PicturePanelAsEnum {INPUT_1, INPUT_2, OUTPUT};
	
	protected MainScreen ms = this;
	private JMenuItem mntmLaplace;
	private JMenuItem mntmEdgeDetect;
	private JMenu mnMedianFiltering;
	private JMenuItem mntmx;
	private JMenuItem mntmx_1;
	private JMenuItem mntmx_2;
	private JMenuItem mntmx_3;
	private JMenuItem mntmNewMenuItem;
	private JMenu mnLogicalFiltering;
	private JMenuItem mntmKillVerticalLines;
	private JMenuItem mntmKillHorizontalLines;
	private JMenuItem mntmKillIsolatedPoints;
	private JMenuItem mntmRoberts;
	private JMenuItem mntmSobel;
	private JMenu mnLab_2;
	private JMenuItem mntmTwoKernels;
	
	protected float[] arthurTwoShedsJackson_firstShed;
	protected float[] arthurTwoShedsJackson_secondShed;
	private JMenuItem mntmErode;
	private JMenuItem mntmDilate;
	private JMenuItem mntmErodex;
	private JMenuItem mntmDilatex;
	private JMenu mnErode;
	private JMenu mnDilate;
	private JMenu mnOpen;
	private JMenuItem mntmx_4;
	private JMenuItem mntmx_5;
	private JMenu mnClose;
	private JMenuItem mntmx_6;
	private JMenuItem mntmx_7;
	

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
		
		mntmDownsample = new JMenuItem("Downsample");
		mntmDownsample.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
					int numLevels = Integer.parseInt(JOptionPane.showInputDialog("Downsample to how many levels (min 2, max 255)?"));
					if (numLevels<2 || numLevels>255) throw new IllegalArgumentException("Please enter a value 2-255");
					outputBuff=downsample(firstInputBuff, numLevels);
					panelOutputPic.setInternalImage(outputBuff);
				}
				catch (IllegalArgumentException err) {
					JOptionPane.showMessageDialog(null, err.getMessage());
				}
				finally {notifyHistWindow();}
			}
		});
		mntmDownsample.setMnemonic('d');
		mnLab.add(mntmDownsample);
		
		mnLogical = new JMenu("Arithmetic and logical");
		mnLogical.setMnemonic('l');
		mnLab.add(mnLogical);
		
		mntmAdd = new JMenuItem("Add");
		mntmAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> x+y);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmAdd);
		
		mntmSubtract = new JMenuItem("Subtract");
		mntmSubtract.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> Math.max((x-y),0));
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmSubtract);

		mntmDiff = new JMenuItem("Diff");
		mntmDiff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> Math.abs((x-y)));
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmDiff);
		
		mntmAnd = new JMenuItem("AND");
		mntmAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> x&y);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmAnd);
		
		mntmOr = new JMenuItem("OR");
		mntmOr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> x|y);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmOr);
		
		mntmXor = new JMenuItem("XOR");
		mntmXor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null | secondInputBuff==null) throw new IllegalArgumentException("Please load both images");
				outputBuff=arithmeticOps(firstInputBuff, secondInputBuff, (x,y) -> x^y);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLogical.add(mntmXor);
		
		mnLab_1 = new JMenu("Lab 3");
		mnLab_1.setMnemonic('3');
		menuBar.add(mnLab_1);
		
		mntmSmoothing = new JMenuItem("Smoothing");
		mntmSmoothing.setMnemonic('s');
		mntmSmoothing.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
				APO07MaskInput.OpObject[] params = getMask(OpType.SMOOTHING);
				APO07MaskInput newWindow = new APO07MaskInput(params, ms, firstInputBuff);
				newWindow.setVisible(true);
				notifyHistWindow();
			}
		});
		mnLab_1.add(mntmSmoothing);
		
		mntmLaplace = new JMenuItem("Laplace");
		mntmLaplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
				APO07MaskInput.OpObject[] params = getMask(OpType.LAPLACE);
				APO07MaskInput newWindow = new APO07MaskInput(params, ms, firstInputBuff);
				newWindow.setVisible(true);
				notifyHistWindow();
			}
		});
		mntmLaplace.setMnemonic('l');
		mnLab_1.add(mntmLaplace);
		
		mntmEdgeDetect = new JMenuItem("Edge detect");
		mntmEdgeDetect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
				APO07MaskInput.OpObject[] params = getMask(OpType.EDGE_DETECT);
				APO07MaskInput newWindow = new APO07MaskInput(params, ms, firstInputBuff);
				newWindow.setVisible(true);
				notifyHistWindow();
			}
		});
		mntmEdgeDetect.setMnemonic('e');
		mnLab_1.add(mntmEdgeDetect);
		
		mntmRoberts = new JMenuItem("Roberts");
		mntmRoberts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.ROBERTS, ScaleMethod.CUT);
			}
		});
		mnLab_1.add(mntmRoberts);
		
		mntmSobel = new JMenuItem("Sobel");
		mntmSobel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.SOBEL, ScaleMethod.CUT);
			}
		});
		mnLab_1.add(mntmSobel);
		
		mnMedianFiltering = new JMenu("Median filtering");
		mnMedianFiltering.setMnemonic('m');
		mnLab_1.add(mnMedianFiltering);
		
		mntmx = new JMenuItem("3x3");
		mntmx.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.MEDIAN);
			}
		});
		mntmx.setMnemonic('3');
		mnMedianFiltering.add(mntmx);
		
		mntmx_1 = new JMenuItem("5x5");
		mntmx_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._5x5, OpType.MEDIAN);
			}
		});
		
		mntmx_3 = new JMenuItem("3x5");
		mntmx_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x5, OpType.MEDIAN);
			}
		});
		mnMedianFiltering.add(mntmx_3);
		mntmx_1.setMnemonic('5');
		mnMedianFiltering.add(mntmx_1);
		
		mntmx_2 = new JMenuItem("7x7");
		mntmx_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._7x7, OpType.MEDIAN);
			}
		});
		
		mntmNewMenuItem = new JMenuItem("5x3");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._5x3, OpType.MEDIAN);
			}
		});
		mnMedianFiltering.add(mntmNewMenuItem);
		mntmx_2.setMnemonic('7');
		mnMedianFiltering.add(mntmx_2);
		
		mnLogicalFiltering = new JMenu("Logical filtering");
		mnLogicalFiltering.setMnemonic('o');
		mnLab_1.add(mnLogicalFiltering);
		
		mntmKillVerticalLines = new JMenuItem("Kill vertical lines");
		mntmKillVerticalLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
//				outputBuff = logicalFiltering(firstInputBuff, Direction.VERTICAL);
//				panelOutputPic.setInternalImage(outputBuff);
//				notifyHistWindow();
				allWithoutMask(MaskType._3x3, OpType.LOGICAL, Direction.VERTICAL);
			}
		});
		mntmKillVerticalLines.setMnemonic('v');
		mnLogicalFiltering.add(mntmKillVerticalLines);
		
		mntmKillHorizontalLines = new JMenuItem("Kill horizontal lines");
		mntmKillHorizontalLines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
//				outputBuff = logicalFiltering(firstInputBuff, Direction.HORIZONTAL);
//				panelOutputPic.setInternalImage(outputBuff);
//				notifyHistWindow();
				allWithoutMask(MaskType._3x3, OpType.LOGICAL, Direction.HORIZONTAL);
			}
		});
		mntmKillHorizontalLines.setMnemonic('h');
		mnLogicalFiltering.add(mntmKillHorizontalLines);
		
		mntmKillIsolatedPoints = new JMenuItem("Kill isolated points");
		mntmKillIsolatedPoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
//				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
//				outputBuff = logicalFiltering(firstInputBuff, Direction.ISOLATED);
//				panelOutputPic.setInternalImage(outputBuff);
//				notifyHistWindow();
				allWithoutMask(MaskType._3x3, OpType.LOGICAL, Direction.ISOLATED);
			}
		});
		mntmKillIsolatedPoints.setMnemonic('i');
		mnLogicalFiltering.add(mntmKillIsolatedPoints);
		
		mnLab_2 = new JMenu("Lab 4");
		mnLab_2.setMnemonic('4');
		menuBar.add(mnLab_2);
		
		mntmTwoKernels = new JMenuItem("Two kernels");
		mntmTwoKernels.setMnemonic('k');
		mntmTwoKernels.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (firstInputBuff==null) throw new IllegalArgumentException("Please load the first image");
				APO07MaskInput.OpObject[] params = getMask(OpType.FIRST_KERNEL);
				APO07MaskInput newWindow = new APO07MaskInput(params, ms, firstInputBuff, 1);
				newWindow.setModal(true);
				newWindow.setVisible(true);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
				
				params = getMask(OpType.SECOND_KERNEL);
				newWindow = new APO07MaskInput(params, ms, firstInputBuff, 2);
				newWindow.setModal(true);
				newWindow.setVisible(true);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
				
				params = getMask(arthurTwoShedsJackson_firstShed, arthurTwoShedsJackson_secondShed);
				newWindow = new APO07MaskInput(params, ms, firstInputBuff);
				newWindow.setVisible(true);
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mnLab_2.add(mntmTwoKernels);
		
		mnErode = new JMenu("Erode");
		mnErode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnErode.setMnemonic('r');
		mnLab_2.add(mnErode);
		
		mntmErode = new JMenuItem("Erode (4x4)");
		mnErode.add(mntmErode);
		mntmErode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.ERODE, NeighborhoodType._4CONNECTED);
			}
		});
		mntmErode.setMnemonic('4');
		
		mntmErodex = new JMenuItem("Erode (8x8)");
		mnErode.add(mntmErodex);
		mntmErodex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.ERODE, NeighborhoodType._8CONNECTED);
			}
		});
		mntmErodex.setMnemonic('8');
		
		mnDilate = new JMenu("Dilate");
		mnDilate.setMnemonic('d');
		mnLab_2.add(mnDilate);
		
		mntmDilate = new JMenuItem("Dilate (4x4)");
		mnDilate.add(mntmDilate);
		mntmDilate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.DILATE, NeighborhoodType._4CONNECTED);
			}
		});
		mntmDilate.setMnemonic('4');
		
		mntmDilatex = new JMenuItem("Dilate (8x8)");
		mnDilate.add(mntmDilatex);
		mntmDilatex.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allWithoutMask(MaskType._3x3, OpType.DILATE, NeighborhoodType._8CONNECTED);
			}
		});
		mntmDilatex.setMnemonic('8');
		
		mnOpen = new JMenu("Open");
		mnOpen.setMnemonic('o');
		mnLab_2.add(mnOpen);
		
		mntmx_4 = new JMenuItem("4x4");
		mntmx_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> paramsList= new ArrayList<>();
				paramsList.add(MaskType._3x3); paramsList.add(OpType.ERODE); paramsList.add(NeighborhoodType._4CONNECTED);
				APO07GenericOp genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(firstInputBuff, genericOp);
				
				paramsList.add(MaskType._3x3); paramsList.add(OpType.DILATE); paramsList.add(NeighborhoodType._4CONNECTED);
				genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(outputBuff, genericOp);
				
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmx_4.setMnemonic('4');
		mnOpen.add(mntmx_4);
		
		mntmx_5 = new JMenuItem("8x8");
		mntmx_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> paramsList= new ArrayList<>();
				paramsList.add(MaskType._3x3); paramsList.add(OpType.ERODE); paramsList.add(NeighborhoodType._8CONNECTED);
				APO07GenericOp genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(firstInputBuff, genericOp);
				
				paramsList.add(MaskType._3x3); paramsList.add(OpType.DILATE); paramsList.add(NeighborhoodType._8CONNECTED);
				genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(outputBuff, genericOp);
				
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmx_5.setMnemonic('8');
		mnOpen.add(mntmx_5);
		
		mnClose = new JMenu("Close");
		mnClose.setMnemonic('c');
		mnLab_2.add(mnClose);
		
		mntmx_6 = new JMenuItem("4x4");
		mntmx_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> paramsList= new ArrayList<>();
				paramsList.add(MaskType._3x3); paramsList.add(OpType.DILATE); paramsList.add(NeighborhoodType._4CONNECTED);
				APO07GenericOp genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(firstInputBuff, genericOp);
				
				paramsList.add(MaskType._3x3); paramsList.add(OpType.ERODE); paramsList.add(NeighborhoodType._4CONNECTED);
				genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(outputBuff, genericOp);
				
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmx_6.setMnemonic('4');
		mnClose.add(mntmx_6);
		
		mntmx_7 = new JMenuItem("8x8");
		mntmx_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> paramsList= new ArrayList<>();
				paramsList.add(MaskType._3x3); paramsList.add(OpType.DILATE); paramsList.add(NeighborhoodType._8CONNECTED);
				APO07GenericOp genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(firstInputBuff, genericOp);
				
				paramsList.add(MaskType._3x3); paramsList.add(OpType.ERODE); paramsList.add(NeighborhoodType._8CONNECTED);
				genericOp = new APO07GenericOp(paramsList);
				outputBuff = marchThroughImage(outputBuff, genericOp);
				
				panelOutputPic.setInternalImage(outputBuff);
				notifyHistWindow();
			}
		});
		mntmx_7.setMnemonic('8');
		mnClose.add(mntmx_7);
		
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
					tmpImg = getEmptyLinearImage(firstInputBuff.getWidth(), firstInputBuff.getHeight(), ImageType.RGB_LINEAR);
					for (int x=0; x<firstInputBuff.getWidth(); x++) {
						for (int y=0; y<firstInputBuff.getHeight(); y++) {
							Color c = new Color(firstInputBuff.getRGB(x, y));
							int red = (int)(c.getRed() * 0.299);
							int green = (int)(c.getGreen() * 0.587);
							int blue = (int)(c.getBlue() *0.114);
							Color newColor = new Color(red+green+blue,red+green+blue,red+green+blue);
							tmpImg.setRGB(x,y,newColor.getRGB());							
						}
					}
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
	
	protected void setOutPic(BufferedImage imgToSet) {
		outputBuff = imgToSet; 
		panelOutputPic.setInternalImage(outputBuff);
		notifyHistWindow();
	}
	
	protected void notifyHistWindow() {
		histWindowSingleton.updateHistograms(firstInputBuff, secondInputBuff, outputBuff);
	}
	
	protected BufferedImage getFirst() {
		return firstInputBuff;
	}
	
	protected void allWithoutMask(MaskType maskType, OpType opType) {
		allWithoutMask(maskType, opType, ScaleMethod.NO_SCALE, null, null);
	}
	
	protected void allWithoutMask(MaskType maskType, OpType opType, NeighborhoodType neighboorhoodType) {
		if (opType != OpType.CLOSE && opType != OpType.OPEN && opType != OpType.ERODE && opType != OpType.DILATE) throw new IllegalArgumentException("Neighborhood type can be specified only for morphological operations");
		allWithoutMask(maskType, opType, ScaleMethod.NO_SCALE, null, neighboorhoodType);
	}
	
	protected void allWithoutMask(MaskType maskType, OpType opType, Direction direction) {
		if (opType != OpType.LOGICAL) throw new IllegalArgumentException("Direction can be specified only for logical operations.");
		allWithoutMask(maskType, opType, ScaleMethod.NO_SCALE, direction, null);
	}
	protected void allWithoutMask(MaskType maskType, OpType opType, ScaleMethod sm) {
		allWithoutMask(maskType, opType, sm, null, null);
	}
	protected void allWithoutMask(MaskType maskType, OpType opType, ScaleMethod scaleMethod, Direction direction, NeighborhoodType neighborhoodType) {
		ArrayList<Object> paramsList= new ArrayList<>();
		paramsList.add(maskType); paramsList.add(opType); paramsList.add(scaleMethod);
		if (direction!=null) paramsList.add(direction);
		if (neighborhoodType!=null) paramsList.add(neighborhoodType);
		APO07GenericOp genericOp = new APO07GenericOp(paramsList);
		outputBuff = marchThroughImage(firstInputBuff, genericOp);
		panelOutputPic.setInternalImage(outputBuff);
		notifyHistWindow();
	}
	
	
}
