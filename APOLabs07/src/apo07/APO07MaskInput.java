package apo07;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;

import static apo07.APO07StaticUtilityMethods.*;
import static apo07.APO07NeighborhoodMethods.*;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;

/*
 * C-TOR:
 * 
 * Input: APO07MaskInput (OpObject[] operations)
 * 
 */

public class APO07MaskInput extends JDialog {
	private JTextField xmin3ymin3;
	private JTextField xmin2ymin3;
	private JTextField xmin1ymin3;
	private JTextField xymin3;
	private JTextField xplus1ymin3;
	private JTextField xplus2ymin3;
	private JTextField xplus3ymin3;
	private JTextField xmin3ymin2;
	private JTextField xmin2ymin2;
	private JTextField xmin1ymin2;
	private JTextField xymin2;
	private JTextField xplus1ymin2;
	private JTextField xplus2ymin2;
	private JTextField xplus3ymin2;
	private JTextField xmin3ymin1;
	private JTextField xmin2ymin1;
	private JTextField xmin1ymin1;
	private JTextField xymin1;
	private JTextField xplus1ymin1;
	private JTextField xplus2ymin1;
	private JTextField xplus3ymin1;
	private JTextField xmin3y;
	private JTextField xmin2y;
	private JTextField xmin1y;
	private JTextField xy;
	private JTextField xplus1y;
	private JTextField xplus2y;
	private JTextField xplus3y;
	private JTextField xmin3yplus1;
	private JTextField xmin2yplus1;
	private JTextField xmin1yplus1;
	private JTextField xyplus1;
	private JTextField xplus1yplus1;
	private JTextField xplus2yplus1;
	private JTextField xplus3yplus1;
	private JTextField xmin3yplus2;
	private JTextField xmin2yplus2;
	private JTextField xmin1yplus2;
	private JTextField xyplus2;
	private JTextField xplus1yplus2;
	private JTextField xplus2yplus2;
	private JTextField xplus3yplus2;
	private JTextField xmin3yplus3;
	private JTextField xmin2yplus3;
	private JTextField xmin1yplus3;
	private JTextField xyplus3;
	private JTextField xplus1yplus3;
	private JTextField xplus2yplus3;
	private JTextField xplus3yplus3;
	
	private JTextField[] outerRing;
	private JTextField[] innerRing;
	private JTextField[] everyRing;
	private JTextField[] fiveOnThree;
	private JTextField[] threeOnFive;
	
	private OpObject[] operations;
	int mask1[]; int mask2[]; int[] mask3; int[] mask4;
	private JRadioButton rdbtnOp;
	private JRadioButton rdbtnOp_1;
	private JRadioButton rdbtnOp_2;
	private JRadioButton rdbtnOp_3;
	private ButtonGroup radioCollection;
	
	private JRadioButton rdbtnDontChangeEdge;
	private JRadioButton rdbtnCopy;
	private JRadioButton rdbtnDuplicate;
	private ButtonGroup edgeCollection;
	
	private JRadioButton rdbtnScale1;
	private JRadioButton rdbtnScale2;
	private JRadioButton rdbtnScale3;
	private ButtonGroup scaleCollection;
	
	private JButton btnOk;
	private JButton btnCancel;
	
	
	private MainScreen ms = null;
	private BufferedImage firstInputPic = null; 
	private MaskType selectedMask = null;
	private ScaleMethod selectedSM = null;
	private EdgePixels selectedEP = null;
	private JLabel lblSelectAPredefined;
	private JLabel lblEnterMaskValues;
	private JLabel lblEdgePixels;
	private JLabel lblScalePixelOutput;
	private JRadioButton rdbtnScaleIgn;
	
	private boolean twoShedsMode=false;
	private int whichShed;
	
	
	static class OpObject {
		protected MaskType maskType;
		protected OpType opType;
		protected ScaleMethod sm;
		float[] maskValues;
		String label;
		
		protected OpObject (MaskType maskType, float[] maskValues, ScaleMethod sm, String label, OpType opType)
		// protected OpObject (int[] maskValues, boolean[] edgePixels)
		{
			this.maskType = maskType;
			this.maskValues=maskValues;
			this.sm=sm;
			this.label=label;
			this.opType=opType;
		}
		
		protected MaskType getMaskType() {
			return maskType;
		}
	}
	
	public APO07MaskInput() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(740, 435));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		setLocationRelativeTo(null);
		selectedMask=MaskType._3x3;
		selectedSM=ScaleMethod.NO_SCALE;
		selectedEP=EdgePixels.LEAVE_UNCHANGED;
		
		JPanel optsPanel = new JPanel();
		optsPanel.setMinimumSize(new Dimension(200, 10));
		optsPanel.setMaximumSize(new Dimension(200, 32767));
		optsPanel.setPreferredSize(new Dimension(250, 10));
		getContentPane().add(optsPanel);
		optsPanel.setLayout(null);
		
		rdbtnOp = new JRadioButton("Op1");
		rdbtnOp.setMnemonic('1');
		rdbtnOp.setSelected(true);
		rdbtnOp.setVisible(false);
		rdbtnOp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[0].maskType, operations[0]);
				setScaleMethod(operations[0].opType, operations[0].sm);
			}
		});
		rdbtnOp.setBounds(31, 48, 210, 45);
		optsPanel.add(rdbtnOp);
		
		rdbtnOp_1 = new JRadioButton("Op2");
		rdbtnOp_1.setMnemonic('2');
		rdbtnOp_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[1].maskType, operations[1]);
				setScaleMethod(operations[1].opType, operations[1].sm);
			}
		});
		rdbtnOp_1.setVisible(false);
		rdbtnOp_1.setBounds(31, 97, 210, 45);
		optsPanel.add(rdbtnOp_1);
		
		rdbtnOp_2 = new JRadioButton("Op3");
		rdbtnOp_2.setMnemonic('3');
		rdbtnOp_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[2].maskType, operations[2]);
				setScaleMethod(operations[2].opType, operations[2].sm);
			}
		});
		rdbtnOp_2.setVisible(false);
		rdbtnOp_2.setBounds(31, 146, 210, 45);
		optsPanel.add(rdbtnOp_2);
		
		rdbtnOp_3 = new JRadioButton("Op4");
		rdbtnOp_3.setMnemonic('4');
		rdbtnOp_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[3].maskType, operations[3]);
				setScaleMethod(operations[3].opType, operations[3].sm);
			}
		});
		rdbtnOp_3.setVisible(false);
		rdbtnOp_3.setBounds(31, 195, 210, 45);
		optsPanel.add(rdbtnOp_3);
		
		JPanel maskPanel = new JPanel();
		getContentPane().add(maskPanel);
		maskPanel.setLayout(null);
		
		xmin3ymin3 = new JTextField();
		xmin3ymin3.setVisible(false);
		xmin3ymin3.setBounds(12, 47, 49, 19);
		maskPanel.add(xmin3ymin3);
		xmin3ymin3.setColumns(10);
		
		xmin2ymin3 = new JTextField();
		xmin2ymin3.setVisible(false);
		xmin2ymin3.setColumns(10);
		xmin2ymin3.setBounds(70, 47, 49, 19);
		maskPanel.add(xmin2ymin3);
		
		xmin1ymin3 = new JTextField();
		xmin1ymin3.setVisible(false);
		xmin1ymin3.setColumns(10);
		xmin1ymin3.setBounds(131, 47, 49, 19);
		maskPanel.add(xmin1ymin3);
		
		xymin3 = new JTextField();
		xymin3.setVisible(false);
		xymin3.setColumns(10);
		xymin3.setBounds(192, 47, 49, 19);
		maskPanel.add(xymin3);
		
		xplus1ymin3 = new JTextField();
		xplus1ymin3.setVisible(false);
		xplus1ymin3.setColumns(10);
		xplus1ymin3.setBounds(253, 47, 49, 19);
		maskPanel.add(xplus1ymin3);
		
		xplus2ymin3 = new JTextField();
		xplus2ymin3.setVisible(false);
		xplus2ymin3.setColumns(10);
		xplus2ymin3.setBounds(314, 47, 49, 19);
		maskPanel.add(xplus2ymin3);
		
		xplus3ymin3 = new JTextField();
		xplus3ymin3.setVisible(false);
		xplus3ymin3.setColumns(10);
		xplus3ymin3.setBounds(375, 47, 49, 19);
		maskPanel.add(xplus3ymin3);
		
		xmin3ymin2 = new JTextField();
		xmin3ymin2.setVisible(false);
		xmin3ymin2.setColumns(10);
		xmin3ymin2.setBounds(12, 78, 49, 19);
		maskPanel.add(xmin3ymin2);
		
		xmin2ymin2 = new JTextField();
		xmin2ymin2.setVisible(false);
		xmin2ymin2.setColumns(10);
		xmin2ymin2.setBounds(70, 78, 49, 19);
		maskPanel.add(xmin2ymin2);
		
		xmin1ymin2 = new JTextField();
		xmin1ymin2.setVisible(false);
		xmin1ymin2.setColumns(10);
		xmin1ymin2.setBounds(131, 78, 49, 19);
		maskPanel.add(xmin1ymin2);
		
		xymin2 = new JTextField();
		xymin2.setVisible(false);
		xymin2.setColumns(10);
		xymin2.setBounds(192, 78, 49, 19);
		maskPanel.add(xymin2);
		
		xplus1ymin2 = new JTextField();
		xplus1ymin2.setVisible(false);
		xplus1ymin2.setColumns(10);
		xplus1ymin2.setBounds(253, 78, 49, 19);
		maskPanel.add(xplus1ymin2);
		
		xplus2ymin2 = new JTextField();
		xplus2ymin2.setVisible(false);
		xplus2ymin2.setColumns(10);
		xplus2ymin2.setBounds(314, 78, 49, 19);
		maskPanel.add(xplus2ymin2);
		
		xplus3ymin2 = new JTextField();
		xplus3ymin2.setVisible(false);
		xplus3ymin2.setColumns(10);
		xplus3ymin2.setBounds(375, 78, 49, 19);
		maskPanel.add(xplus3ymin2);
		
		xmin3ymin1 = new JTextField();
		xmin3ymin1.setVisible(false);
		xmin3ymin1.setColumns(10);
		xmin3ymin1.setBounds(12, 110, 49, 19);
		maskPanel.add(xmin3ymin1);
		
		xmin2ymin1 = new JTextField();
		xmin2ymin1.setVisible(false);
		xmin2ymin1.setColumns(10);
		xmin2ymin1.setBounds(70, 110, 49, 19);
		maskPanel.add(xmin2ymin1);
		
		xmin1ymin1 = new JTextField();
		xmin1ymin1.setText("1");
		xmin1ymin1.setColumns(10);
		xmin1ymin1.setBounds(131, 110, 49, 19);
		maskPanel.add(xmin1ymin1);
		
		xymin1 = new JTextField();
		xymin1.setText("1");
		xymin1.setColumns(10);
		xymin1.setBounds(192, 110, 49, 19);
		maskPanel.add(xymin1);
		
		xplus1ymin1 = new JTextField();
		xplus1ymin1.setText("1");
		xplus1ymin1.setColumns(10);
		xplus1ymin1.setBounds(253, 110, 49, 19);
		maskPanel.add(xplus1ymin1);
		
		xplus2ymin1 = new JTextField();
		xplus2ymin1.setVisible(false);
		xplus2ymin1.setColumns(10);
		xplus2ymin1.setBounds(314, 110, 49, 19);
		maskPanel.add(xplus2ymin1);
		
		xplus3ymin1 = new JTextField();
		xplus3ymin1.setVisible(false);
		xplus3ymin1.setColumns(10);
		xplus3ymin1.setBounds(375, 110, 49, 19);
		maskPanel.add(xplus3ymin1);
		
		xmin3y = new JTextField();
		xmin3y.setVisible(false);
		xmin3y.setColumns(10);
		xmin3y.setBounds(12, 141, 49, 19);
		maskPanel.add(xmin3y);
		
		xmin2y = new JTextField();
		xmin2y.setVisible(false);
		xmin2y.setColumns(10);
		xmin2y.setBounds(70, 141, 49, 19);
		maskPanel.add(xmin2y);
		
		xmin1y = new JTextField();
		xmin1y.setText("1");
		xmin1y.setColumns(10);
		xmin1y.setBounds(131, 141, 49, 19);
		maskPanel.add(xmin1y);
		
		xy = new JTextField();
		xy.setText("1");
		xy.setColumns(10);
		xy.setBounds(192, 141, 49, 19);
		maskPanel.add(xy);
		
		xplus1y = new JTextField();
		xplus1y.setText("1");
		xplus1y.setColumns(10);
		xplus1y.setBounds(253, 141, 49, 19);
		maskPanel.add(xplus1y);
		
		xplus2y = new JTextField();
		xplus2y.setVisible(false);
		xplus2y.setColumns(10);
		xplus2y.setBounds(314, 141, 49, 19);
		maskPanel.add(xplus2y);
		
		xplus3y = new JTextField();
		xplus3y.setVisible(false);
		xplus3y.setColumns(10);
		xplus3y.setBounds(375, 141, 49, 19);
		maskPanel.add(xplus3y);
		
		xmin3yplus1 = new JTextField();
		xmin3yplus1.setVisible(false);
		xmin3yplus1.setColumns(10);
		xmin3yplus1.setBounds(12, 173, 49, 19);
		maskPanel.add(xmin3yplus1);
		
		xmin2yplus1 = new JTextField();
		xmin2yplus1.setVisible(false);
		xmin2yplus1.setColumns(10);
		xmin2yplus1.setBounds(70, 173, 49, 19);
		maskPanel.add(xmin2yplus1);
		
		xmin1yplus1 = new JTextField();
		xmin1yplus1.setText("1");
		xmin1yplus1.setColumns(10);
		xmin1yplus1.setBounds(131, 173, 49, 19);
		maskPanel.add(xmin1yplus1);
		
		xyplus1 = new JTextField();
		xyplus1.setText("1");
		xyplus1.setColumns(10);
		xyplus1.setBounds(192, 173, 49, 19);
		maskPanel.add(xyplus1);
		
		xplus1yplus1 = new JTextField();
		xplus1yplus1.setText("1");
		xplus1yplus1.setColumns(10);
		xplus1yplus1.setBounds(253, 173, 49, 19);
		maskPanel.add(xplus1yplus1);
		
		xplus2yplus1 = new JTextField();
		xplus2yplus1.setVisible(false);
		xplus2yplus1.setColumns(10);
		xplus2yplus1.setBounds(314, 173, 49, 19);
		maskPanel.add(xplus2yplus1);
		
		xplus3yplus1 = new JTextField();
		xplus3yplus1.setVisible(false);
		xplus3yplus1.setColumns(10);
		xplus3yplus1.setBounds(375, 173, 49, 19);
		maskPanel.add(xplus3yplus1);
		
		xmin3yplus2 = new JTextField();
		xmin3yplus2.setVisible(false);
		xmin3yplus2.setColumns(10);
		xmin3yplus2.setBounds(12, 204, 49, 19);
		maskPanel.add(xmin3yplus2);
		
		xmin2yplus2 = new JTextField();
		xmin2yplus2.setVisible(false);
		xmin2yplus2.setColumns(10);
		xmin2yplus2.setBounds(70, 204, 49, 19);
		maskPanel.add(xmin2yplus2);
		
		xmin1yplus2 = new JTextField();
		xmin1yplus2.setVisible(false);
		xmin1yplus2.setColumns(10);
		xmin1yplus2.setBounds(131, 204, 49, 19);
		maskPanel.add(xmin1yplus2);
		
		xyplus2 = new JTextField();
		xyplus2.setVisible(false);
		xyplus2.setColumns(10);
		xyplus2.setBounds(192, 204, 49, 19);
		maskPanel.add(xyplus2);
		
		xplus1yplus2 = new JTextField();
		xplus1yplus2.setVisible(false);
		xplus1yplus2.setColumns(10);
		xplus1yplus2.setBounds(253, 204, 49, 19);
		maskPanel.add(xplus1yplus2);
		
		xplus2yplus2 = new JTextField();
		xplus2yplus2.setVisible(false);
		xplus2yplus2.setColumns(10);
		xplus2yplus2.setBounds(314, 204, 49, 19);
		maskPanel.add(xplus2yplus2);
		
		xplus3yplus2 = new JTextField();
		xplus3yplus2.setVisible(false);
		xplus3yplus2.setColumns(10);
		xplus3yplus2.setBounds(375, 204, 49, 19);
		maskPanel.add(xplus3yplus2);
		
		xmin3yplus3 = new JTextField();
		xmin3yplus3.setVisible(false);
		xmin3yplus3.setColumns(10);
		xmin3yplus3.setBounds(12, 235, 49, 19);
		maskPanel.add(xmin3yplus3);
		
		xmin2yplus3 = new JTextField();
		xmin2yplus3.setVisible(false);
		xmin2yplus3.setColumns(10);
		xmin2yplus3.setBounds(70, 235, 49, 19);
		maskPanel.add(xmin2yplus3);
		
		xmin1yplus3 = new JTextField();
		xmin1yplus3.setVisible(false);
		xmin1yplus3.setColumns(10);
		xmin1yplus3.setBounds(131, 235, 49, 19);
		maskPanel.add(xmin1yplus3);
		
		xyplus3 = new JTextField();
		xyplus3.setVisible(false);
		xyplus3.setColumns(10);
		xyplus3.setBounds(192, 235, 49, 19);
		maskPanel.add(xyplus3);
		
		xplus1yplus3 = new JTextField();
		xplus1yplus3.setVisible(false);
		xplus1yplus3.setColumns(10);
		xplus1yplus3.setBounds(253, 235, 49, 19);
		maskPanel.add(xplus1yplus3);
		
		xplus2yplus3 = new JTextField();
		xplus2yplus3.setVisible(false);
		xplus2yplus3.setColumns(10);
		xplus2yplus3.setBounds(314, 235, 49, 19);
		maskPanel.add(xplus2yplus3);
		
		xplus3yplus3 = new JTextField();
		xplus3yplus3.setVisible(false);
		xplus3yplus3.setColumns(10);
		xplus3yplus3.setBounds(375, 235, 49, 19);
		maskPanel.add(xplus3yplus3);
		
		rdbtnDontChangeEdge = new JRadioButton("Black");
		rdbtnDontChangeEdge.setMnemonic('b');
		rdbtnDontChangeEdge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedEP=EdgePixels.ZERO;
			}
		});
		rdbtnDontChangeEdge.setBounds(22, 290, 72, 23);
		maskPanel.add(rdbtnDontChangeEdge);
		
		rdbtnCopy = new JRadioButton("No change");
		rdbtnCopy.setMnemonic('h');
		rdbtnCopy.setSelected(true);
		rdbtnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedEP=EdgePixels.LEAVE_UNCHANGED;
			}
		});
		rdbtnCopy.setBounds(98, 290, 107, 23);
		maskPanel.add(rdbtnCopy);
		
		rdbtnDuplicate = new JRadioButton("<html>Copy rows and columns</html>");
		rdbtnDuplicate.setMnemonic('r');
		rdbtnDuplicate.setVerticalAlignment(SwingConstants.TOP);
		rdbtnDuplicate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedEP=EdgePixels.DUPLICATE;
			}
		});
		rdbtnDuplicate.setBounds(219, 290, 117, 38);
		maskPanel.add(rdbtnDuplicate);
		
		
		rdbtnScale1 = new JRadioButton("Proportionally");
		rdbtnScale1.setMnemonic('p');
		rdbtnScale1.setBounds(22, 345, 134, 23);
		maskPanel.add(rdbtnScale1);
		rdbtnScale1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSM=ScaleMethod.PROPORTIONAL;
			}
		});
		
		rdbtnScale2 = new JRadioButton("3 values");
		rdbtnScale2.setMnemonic('v');
		rdbtnScale2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSM=ScaleMethod.THREE_VALUES;
			}
		});
		rdbtnScale2.setBounds(153, 345, 96, 23);
		maskPanel.add(rdbtnScale2);
		
		rdbtnScale3 = new JRadioButton("Cut");
		rdbtnScale3.setMnemonic('u');
		rdbtnScale3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedSM=ScaleMethod.CUT;
			}
		});
		rdbtnScale3.setBounds(253, 345, 96, 23);
		maskPanel.add(rdbtnScale3);
		
		rdbtnScaleIgn = new JRadioButton("No rescaling");
		rdbtnScaleIgn.setMnemonic('n');
		rdbtnScaleIgn.setSelected(true);
		rdbtnScaleIgn.setBounds(22, 372, 134, 23);
		maskPanel.add(rdbtnScaleIgn);
		
		btnOk = new JButton("OK");
		btnOk.setMnemonic('o');
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				float[] maskToApply = readMaskValues();
				ArrayList<Object> al = new ArrayList<>();
				al.add(maskToApply); al.add(selectedMask); al.add(selectedSM); al.add(selectedEP); al.add(operations[0].opType);
				APO07GenericOp genericOp = new APO07GenericOp(al);
				BufferedImage outimg = marchThroughImage(firstInputPic, genericOp);
				// BufferedImage outimg = applyMask(firstInputPic, maskToApply, selectedMask, selectedSM, selectedEP);
				ms.outputBuff=outimg;
				ms.setOutPic(outimg);
				ms.notifyHistWindow();
				if (twoShedsMode) {
					if (whichShed==1) ms.arthurTwoShedsJackson_firstShed = maskToApply;
					else if (whichShed==2) ms.arthurTwoShedsJackson_secondShed = maskToApply;
				}
				dispose();
			}
		});
		btnOk.setBounds(357, 306, 104, 25);
		maskPanel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setMnemonic('c');
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(357, 343, 104, 25);
		maskPanel.add(btnCancel);
		
		everyRing = new JTextField[] {xmin3ymin3, xmin2ymin3, xmin1ymin3, xymin3, xplus1ymin3, xplus2ymin3, xplus3ymin3, xmin3ymin2, xmin2ymin2, xmin1ymin2, xymin2, xplus1ymin2, xplus2ymin2, xplus3ymin2, xmin3ymin1, xmin2ymin1, xmin1ymin1, xymin1, xplus1ymin1, xplus2ymin1, xplus3ymin1, xmin3y, xmin2y, xmin1y, xy, xplus1y, xplus2y, xplus3y, xmin3yplus1, xmin2yplus1, xmin1yplus1, xyplus1, xplus1yplus1, xplus2yplus1, xplus3yplus1, xmin3yplus2, xmin2yplus2, xmin1yplus2, xyplus2, xplus1yplus2, xplus2yplus2, xplus3yplus2, xmin3yplus3, xmin2yplus3, xmin1yplus3, xyplus3, xplus1yplus3, xplus2yplus3, xplus3yplus3};
		outerRing = new JTextField[] {xmin3ymin3, xmin2ymin3, xmin1ymin3, xymin3, xplus1ymin3, xplus2ymin3, xplus3ymin3, xmin3ymin2, xmin3ymin2, xplus3ymin2, xmin3ymin1, xplus3ymin1, xmin3y, xplus3y, xmin3yplus1, xplus3yplus1, xmin3yplus2, xplus3yplus2, xmin3yplus3, xmin2yplus3, xmin1yplus3, xyplus3, xplus1yplus3, xplus2yplus3, xplus3yplus3};
		innerRing = new JTextField[] {xmin2ymin2, xmin1ymin2, xymin2, xplus1ymin2, xplus2ymin2, xmin2ymin1, xplus2ymin1, xmin2y, xplus2y, xmin2yplus1, xplus2yplus1, xmin2yplus2, xmin1yplus2, xyplus2, xplus1yplus2, xplus2yplus2};
		fiveOnThree = new JTextField[] {xmin2ymin1, xmin1ymin1, xymin1, xplus1ymin1, xplus2ymin1, xmin2y, xplus2y, xmin2yplus2, xmin1yplus2, xyplus2, xplus1yplus2, xplus2yplus2};
		threeOnFive = new JTextField[] {xmin1ymin2, xymin2, xplus1ymin2, xmin1yplus2, xyplus2, xplus1yplus2};
		radioCollection = new ButtonGroup();
		radioCollection.add(rdbtnOp);
		radioCollection.add(rdbtnOp_1);
		radioCollection.add(rdbtnOp_2);
		radioCollection.add(rdbtnOp_3);
		
		lblSelectAPredefined = new JLabel("Select a predefined mask");
		lblSelectAPredefined.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectAPredefined.setBounds(12, 12, 226, 15);
		optsPanel.add(lblSelectAPredefined);
		
		scaleCollection = new ButtonGroup();
		scaleCollection.add(rdbtnScale1); scaleCollection.add(rdbtnScale2); scaleCollection.add(rdbtnScale3); scaleCollection.add(rdbtnScaleIgn);
		
		edgeCollection = new ButtonGroup();
		edgeCollection.add(rdbtnDontChangeEdge); edgeCollection.add(rdbtnCopy); edgeCollection.add(rdbtnDuplicate);
 
		lblEnterMaskValues = new JLabel("Enter mask values");
		lblEnterMaskValues.setHorizontalAlignment(SwingConstants.CENTER);
		lblEnterMaskValues.setBounds(12, 12, 447, 15);
		maskPanel.add(lblEnterMaskValues);
		
		lblEdgePixels = new JLabel("Edge pixels");
		lblEdgePixels.setBounds(22, 267, 164, 15);
		maskPanel.add(lblEdgePixels);
		
		lblScalePixelOutput = new JLabel("Rescale output pixel values");
		lblScalePixelOutput.setBounds(22, 320, 196, 15);
		maskPanel.add(lblScalePixelOutput);
		
	}
	
	public APO07MaskInput(OpObject[] operations) {
		this();
		this.operations = operations;
	//	this.mask1=mask1; this.mask2=mask2; this.mask3=mask3; this.mask4=mask4;
		for (int oplength=0; oplength<operations.length; oplength++) {
			if (oplength==0) {rdbtnOp.setVisible(true); rdbtnOp.setText(operations[0].label);}
			if (oplength==1) {rdbtnOp_1.setVisible(true); rdbtnOp_1.setText(operations[1].label);}
			if (oplength==2) {rdbtnOp_2.setVisible(true); rdbtnOp_2.setText(operations[2].label);}
			if (oplength==3) {rdbtnOp_3.setVisible(true); rdbtnOp_3.setText(operations[3].label);}		
		}
		setMask(operations[0].maskType, operations[0]);
		setScaleMethod(operations[0].opType, operations[0].sm);
		selectedMask=operations[0].maskType;
		selectedSM=operations[0].sm;
		if (operations[0].opType==OpType.SMOOTHING) {
			Arrays.stream(new JComponent[]{lblScalePixelOutput, rdbtnScale1, rdbtnScale2, rdbtnScale3, rdbtnScaleIgn}).forEach(x -> x.setVisible(false));
			lblScalePixelOutput.setVisible(false);
		}
	}
	
	public APO07MaskInput(OpObject[] operations, MainScreen ms, BufferedImage first) {
		this(operations);
		this.firstInputPic=first;
		this.ms=ms;
	}
	
	public APO07MaskInput(OpObject[] operations, MainScreen ms, BufferedImage first, int whichShed) {
		this(operations, ms, first);
		this.twoShedsMode = true;
		this.whichShed=whichShed;
	}
	
	private void setScaleMethod (OpType opType, ScaleMethod sm) {
		if (opType==OpType.SMOOTHING) {
			Arrays.stream(new JComponent[]{lblScalePixelOutput, rdbtnScale1, rdbtnScale2, rdbtnScale3, rdbtnScaleIgn, lblScalePixelOutput}).forEach(x -> x.setVisible(false));
		}
		else {
			Arrays.stream(new JComponent[]{lblScalePixelOutput, rdbtnScale1, rdbtnScale2, rdbtnScale3, rdbtnScaleIgn, lblScalePixelOutput}).forEach(x -> x.setVisible(true));
			if (sm==ScaleMethod.PROPORTIONAL) rdbtnScale1.setSelected(true);
			else if (sm==ScaleMethod.THREE_VALUES) rdbtnScale2.setSelected(true);
			else if (sm==ScaleMethod.CUT) rdbtnScale3.setSelected(true);
			else rdbtnScaleIgn.setSelected(true);
		}
	}
	
	private void setMask (MaskType maskType, OpObject op) {
		Arrays.stream(everyRing).forEach(x -> {x.setText("0"); x.setVisible(true);});
		if (maskType == MaskType._3x3) {
			Arrays.stream(outerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(innerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
		}
		else if (maskType == MaskType._3x5) {
			Arrays.stream(outerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(innerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(threeOnFive).forEach(x -> {x.setText("0"); x.setVisible(false);});
		}
		else if (maskType == MaskType._5x5) {
			Arrays.stream(outerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
		}
		else if (maskType == MaskType._5x3) {
			Arrays.stream(outerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(innerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(fiveOnThree).forEach(x -> {x.setText("0"); x.setVisible(false);});
		}
		else if (maskType == MaskType._7x7) {
			// do nothing
		}
		else throw new IllegalArgumentException("Unknown mask " + maskType.toString());
		
		if (op!=null) {
			if (maskType == MaskType._3x3) {
				xmin1ymin1.setText(""+op.maskValues[0]); xymin1.setText(""+op.maskValues[1]); xplus1ymin1.setText(""+op.maskValues[2]);
				xmin1y.setText(""+op.maskValues[3]); xy.setText(""+op.maskValues[4]); xplus1y.setText(""+op.maskValues[5]);
				xmin1yplus1.setText(""+op.maskValues[6]); xyplus1.setText(""+op.maskValues[7]); xplus1yplus1.setText(""+op.maskValues[8]);
			}
			else if (maskType==MaskType._3x5) {
				xmin1ymin2.setText(""+op.maskValues[0]); xymin2.setText(""+op.maskValues[1]); xplus1ymin2.setText(""+op.maskValues[2]);
				xmin1ymin1.setText(""+op.maskValues[3]); xymin1.setText(""+op.maskValues[4]); xplus1ymin1.setText(""+op.maskValues[5]);
				xmin1y.setText(""+op.maskValues[6]); xy.setText(""+op.maskValues[7]); xplus1y.setText(""+op.maskValues[8]);
				xmin1yplus1.setText(""+op.maskValues[9]); xyplus1.setText(""+op.maskValues[10]); xplus1yplus1.setText(""+op.maskValues[11]);
				xmin1yplus2.setText(""+op.maskValues[12]); xyplus2.setText(""+op.maskValues[13]); xplus1yplus2.setText(""+op.maskValues[14]);
			}
			else if (maskType==MaskType._5x5) {
				xmin2ymin2.setText(""+op.maskValues[0]); xmin1ymin2.setText(""+op.maskValues[1]); xymin2.setText(""+op.maskValues[2]); xplus1ymin2.setText(""+op.maskValues[3]); xplus2ymin2.setText(""+op.maskValues[4]); 
				xmin2ymin1.setText(""+op.maskValues[5]); xmin1ymin1.setText(""+op.maskValues[6]); xymin1.setText(""+op.maskValues[7]); xplus1ymin1.setText(""+op.maskValues[8]); xplus2ymin1.setText(""+op.maskValues[9]);
				xmin2y.setText(""+op.maskValues[10]); xmin1y.setText(""+op.maskValues[11]); xy.setText(""+op.maskValues[12]); xplus1y.setText(""+op.maskValues[13]); xplus2y.setText(""+op.maskValues[14]);
				xmin2yplus1.setText(""+op.maskValues[15]); xmin1yplus1.setText(""+op.maskValues[16]); xyplus1.setText(""+op.maskValues[17]); xplus1yplus1.setText(""+op.maskValues[18]); xplus2yplus1.setText(""+op.maskValues[19]);
				xmin2yplus2.setText(""+op.maskValues[20]); xmin1yplus2.setText(""+op.maskValues[21]); xyplus2.setText(""+op.maskValues[22]); xplus1yplus2.setText(""+op.maskValues[23]); xplus2yplus2.setText(""+op.maskValues[24]);
			}
			else if (maskType==MaskType._5x3) { 
				xmin2ymin1.setText(""+op.maskValues[0]); xmin1ymin1.setText(""+op.maskValues[1]); xymin1.setText(""+op.maskValues[2]); xplus1ymin1.setText(""+op.maskValues[3]); xplus2ymin1.setText(""+op.maskValues[4]);
				xmin2y.setText(""+op.maskValues[5]); xmin1y.setText(""+op.maskValues[6]); xy.setText(""+op.maskValues[7]); xplus1y.setText(""+op.maskValues[8]); xplus2y.setText(""+op.maskValues[9]);
				xmin2yplus1.setText(""+op.maskValues[10]); xmin1yplus1.setText(""+op.maskValues[11]); xyplus1.setText(""+op.maskValues[12]); xplus1yplus1.setText(""+op.maskValues[13]); xplus2yplus1.setText(""+op.maskValues[14]);
			}
			else if (maskType==MaskType._7x7) { 
				xmin3ymin3.setText(""+op.maskValues[0]); xmin2ymin3.setText(""+op.maskValues[1]); xmin1ymin3.setText(""+op.maskValues[2]); xymin3.setText(""+op.maskValues[3]); xplus1ymin3.setText(""+op.maskValues[4]); xplus2ymin3.setText(""+op.maskValues[5]); xplus3ymin3.setText(""+op.maskValues[6]);
				xmin3ymin2.setText(""+op.maskValues[7]); xmin2ymin2.setText(""+op.maskValues[8]); xmin1ymin2.setText(""+op.maskValues[9]); xymin2.setText(""+op.maskValues[10]); xplus1ymin2.setText(""+op.maskValues[11]); xplus2ymin2.setText(""+op.maskValues[12]); xplus3ymin2.setText(""+op.maskValues[13]);
				xmin3ymin1.setText(""+op.maskValues[14]); xmin2ymin1.setText(""+op.maskValues[15]); xmin1ymin1.setText(""+op.maskValues[16]); xymin1.setText(""+op.maskValues[17]); xplus1ymin1.setText(""+op.maskValues[18]); xplus2ymin1.setText(""+op.maskValues[19]); xplus3ymin1.setText(""+op.maskValues[20]);
				xmin3y.setText(""+op.maskValues[21]); xmin2y.setText(""+op.maskValues[22]); xmin1y.setText(""+op.maskValues[23]); xy.setText(""+op.maskValues[24]); xplus1y.setText(""+op.maskValues[25]); xplus2y.setText(""+op.maskValues[26]); xplus3y.setText(""+op.maskValues[27]);
				xmin3yplus1.setText(""+op.maskValues[28]); xmin2yplus1.setText(""+op.maskValues[29]); xmin1yplus1.setText(""+op.maskValues[30]); xyplus1.setText(""+op.maskValues[31]); xplus1yplus1.setText(""+op.maskValues[32]); xplus2yplus1.setText(""+op.maskValues[33]); xplus3yplus1.setText(""+op.maskValues[34]);
				xmin3yplus2.setText(""+op.maskValues[35]); xmin2yplus2.setText(""+op.maskValues[36]); xmin1yplus2.setText(""+op.maskValues[37]); xyplus2.setText(""+op.maskValues[38]); xplus1yplus2.setText(""+op.maskValues[39]); xplus2yplus2.setText(""+op.maskValues[40]); xplus3yplus2.setText(""+op.maskValues[41]);
				xmin3yplus3.setText(""+op.maskValues[42]); xmin2yplus3.setText(""+op.maskValues[43]); xmin1yplus3.setText(""+op.maskValues[44]); xyplus3.setText(""+op.maskValues[45]); xplus1yplus3.setText(""+op.maskValues[46]); xplus2yplus3.setText(""+op.maskValues[47]); xplus3yplus3.setText(""+op.maskValues[48]);
			}
		}
		else throw new IllegalArgumentException("Unknown operation");
		selectedMask=maskType;
	}
	
	private float[] readMaskValues() {
		float[] retMask = null;
		try {
			if (selectedMask==MaskType._3x3) { //TODO: Uzupelnic
				retMask=new float[9];
				retMask[0]=Float.parseFloat(xmin1ymin1.getText()); retMask[1]=Float.parseFloat(xymin1.getText()); retMask[2]=Float.parseFloat(xplus1ymin1.getText());
				retMask[3]=Float.parseFloat(xmin1y.getText()); retMask[4]=Float.parseFloat(xy.getText()); retMask[5]=Float.parseFloat(xplus1y.getText());
				retMask[6]=Float.parseFloat(xmin1yplus1.getText()); retMask[7]=Float.parseFloat(xyplus1.getText()); retMask[8]=Float.parseFloat(xplus1yplus1.getText());
			}
			else if (selectedMask==MaskType._3x5) {
				retMask=new float[15];
				retMask[0]=Float.parseFloat(xmin1ymin2.getText()); retMask[1]=Float.parseFloat(xymin2.getText()); retMask[2]=Float.parseFloat(xplus1ymin2.getText());
				retMask[3]=Float.parseFloat(xmin1ymin1.getText()); retMask[4]=Float.parseFloat(xymin1.getText()); retMask[5]=Float.parseFloat(xplus1ymin1.getText());
				retMask[6]=Float.parseFloat(xmin1y.getText()); retMask[7]=Float.parseFloat(xy.getText()); retMask[8]=Float.parseFloat(xplus1y.getText());
				retMask[9]=Float.parseFloat(xmin1yplus1.getText()); retMask[10]=Float.parseFloat(xyplus1.getText()); retMask[11]=Float.parseFloat(xplus1yplus1.getText());
				retMask[12]=Float.parseFloat(xmin1yplus2.getText()); retMask[13]=Float.parseFloat(xyplus2.getText()); retMask[14]=Float.parseFloat(xplus1yplus2.getText());
			}
			else if (selectedMask==MaskType._5x5) {
				retMask=new float[25];
				retMask[0]=Float.parseFloat(xmin2ymin2.getText()); retMask[1]=Float.parseFloat(xmin1ymin2.getText()); retMask[2]=Float.parseFloat(xymin2.getText()); retMask[3]=Float.parseFloat(xplus1ymin2.getText()); retMask[4]=Float.parseFloat(xplus2ymin2.getText()); 
				retMask[5]=Float.parseFloat(xmin2ymin1.getText()); retMask[6]=Float.parseFloat(xmin1ymin1.getText()); retMask[7]=Float.parseFloat(xymin1.getText()); retMask[8]=Float.parseFloat(xplus1ymin1.getText()); retMask[9]=Float.parseFloat(xplus2ymin1.getText()); 
				retMask[10]=Float.parseFloat(xmin2y.getText()); retMask[11]=Float.parseFloat(xmin1y.getText()); retMask[12]=Float.parseFloat(xy.getText()); retMask[13]=Float.parseFloat(xplus1y.getText()); retMask[14]=Float.parseFloat(xplus2y.getText()); 
				retMask[15]=Float.parseFloat(xmin2yplus1.getText()); retMask[16]=Float.parseFloat(xmin1yplus1.getText()); retMask[17]=Float.parseFloat(xyplus1.getText()); retMask[18]=Float.parseFloat(xplus1yplus1.getText()); retMask[19]=Float.parseFloat(xplus2yplus1.getText()); 
				retMask[20]=Float.parseFloat(xmin2yplus2.getText()); retMask[21]=Float.parseFloat(xmin1yplus2.getText()); retMask[22]=Float.parseFloat(xyplus2.getText()); retMask[23]=Float.parseFloat(xplus1yplus2.getText()); retMask[24]=Float.parseFloat(xplus2yplus2.getText());
			}
			else if (selectedMask==MaskType._5x3) {
				retMask=new float[15];
				retMask[0]=Float.parseFloat(xmin2ymin1.getText()); retMask[1]=Float.parseFloat(xmin1ymin1.getText()); retMask[2]=Float.parseFloat(xymin1.getText()); retMask[3]=Float.parseFloat(xplus1ymin1.getText()); retMask[4]=Float.parseFloat(xplus2ymin1.getText()); 
				retMask[5]=Float.parseFloat(xmin2y.getText()); retMask[6]=Float.parseFloat(xmin1y.getText()); retMask[7]=Float.parseFloat(xy.getText()); retMask[8]=Float.parseFloat(xplus1y.getText()); retMask[9]=Float.parseFloat(xplus2y.getText()); 
				retMask[10]=Float.parseFloat(xmin2yplus1.getText()); retMask[11]=Float.parseFloat(xmin1yplus1.getText()); retMask[12]=Float.parseFloat(xyplus1.getText()); retMask[13]=Float.parseFloat(xplus1yplus1.getText()); retMask[14]=Float.parseFloat(xplus2yplus1.getText()); 
			}
			else if (selectedMask==MaskType._7x7) {
				retMask=new float[49];
				retMask[0]=Float.parseFloat(xmin3ymin3.getText()); retMask[1]=Float.parseFloat(xmin2ymin3.getText()); retMask[2]=Float.parseFloat(xmin1ymin3.getText()); retMask[3]=Float.parseFloat(xymin3.getText()); retMask[4]=Float.parseFloat(xplus1ymin3.getText()); retMask[5]=Float.parseFloat(xplus2ymin3.getText()); retMask[6]=Float.parseFloat(xplus3ymin3.getText()); 
				retMask[7]=Float.parseFloat(xmin3ymin2.getText()); retMask[8]=Float.parseFloat(xmin2ymin2.getText()); retMask[9]=Float.parseFloat(xmin1ymin2.getText()); retMask[10]=Float.parseFloat(xymin2.getText()); retMask[11]=Float.parseFloat(xplus1ymin2.getText()); retMask[12]=Float.parseFloat(xplus2ymin2.getText()); retMask[13]=Float.parseFloat(xplus3ymin2.getText()); 
				retMask[14]=Float.parseFloat(xmin3ymin1.getText()); retMask[15]=Float.parseFloat(xmin2ymin1.getText()); retMask[16]=Float.parseFloat(xmin1ymin1.getText()); retMask[17]=Float.parseFloat(xymin1.getText()); retMask[18]=Float.parseFloat(xplus1ymin1.getText()); retMask[19]=Float.parseFloat(xplus2ymin1.getText()); retMask[20]=Float.parseFloat(xplus3ymin1.getText()); 
				retMask[21]=Float.parseFloat(xmin3y.getText()); retMask[22]=Float.parseFloat(xmin2y.getText()); retMask[23]=Float.parseFloat(xmin1y.getText()); retMask[24]=Float.parseFloat(xy.getText()); retMask[25]=Float.parseFloat(xplus1y.getText()); retMask[26]=Float.parseFloat(xplus2y.getText()); retMask[27]=Float.parseFloat(xplus3y.getText()); 
				retMask[28]=Float.parseFloat(xmin3yplus1.getText()); retMask[29]=Float.parseFloat(xmin2yplus1.getText()); retMask[30]=Float.parseFloat(xmin1yplus1.getText()); retMask[31]=Float.parseFloat(xyplus1.getText()); retMask[32]=Float.parseFloat(xplus1yplus1.getText()); retMask[33]=Float.parseFloat(xplus2yplus1.getText()); retMask[34]=Float.parseFloat(xplus3yplus1.getText()); 
				retMask[35]=Float.parseFloat(xmin3yplus2.getText()); retMask[36]=Float.parseFloat(xmin2yplus2.getText()); retMask[37]=Float.parseFloat(xmin1yplus2.getText()); retMask[38]=Float.parseFloat(xyplus2.getText()); retMask[39]=Float.parseFloat(xplus1yplus2.getText()); retMask[40]=Float.parseFloat(xplus2yplus2.getText()); retMask[41]=Float.parseFloat(xplus3yplus2.getText()); 
				retMask[42]=Float.parseFloat(xmin3yplus3.getText()); retMask[43]=Float.parseFloat(xmin2yplus3.getText()); retMask[44]=Float.parseFloat(xmin1yplus3.getText()); retMask[45]=Float.parseFloat(xyplus3.getText()); retMask[46]=Float.parseFloat(xplus1yplus3.getText()); retMask[47]=Float.parseFloat(xplus2yplus3.getText()); retMask[48]=Float.parseFloat(xplus3yplus3.getText());
			}
			else throw new IllegalArgumentException("Unknown mask type");
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException("Incorrect mask values.");
		}
		return retMask;
	}
}