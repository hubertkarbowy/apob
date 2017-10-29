package apo07;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

import java.awt.Dimension;
import java.util.Arrays;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
	private JRadioButton rdbtnScale1;
	private JRadioButton rdbtnScale2;
	private JRadioButton rdbtnScale3;
	private JButton btnOk;
	private JButton btnCancel;
	private ButtonGroup scaleCollection;
	
	static class OpObject {
		MaskType maskType;
		int[] maskValues;
		int edgePixels;
		String label;
		
		protected OpObject (MaskType maskType, int[] maskValues, int edgePixels, String label)
		// protected OpObject (int[] maskValues, boolean[] edgePixels)
		{
			this.maskType = maskType;
			this.maskValues=maskValues;
			this.edgePixels=edgePixels;
			this.label=label;
		}
	}
	
	public APO07MaskInput() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(740, 360));
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		
		JPanel optsPanel = new JPanel();
		optsPanel.setMinimumSize(new Dimension(200, 10));
		optsPanel.setMaximumSize(new Dimension(200, 32767));
		optsPanel.setPreferredSize(new Dimension(250, 10));
		getContentPane().add(optsPanel);
		optsPanel.setLayout(null);
		
		rdbtnOp = new JRadioButton("Op1");
		rdbtnOp.setVisible(false);
		rdbtnOp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[0].maskType, operations[0]);
			}
		});
		rdbtnOp.setBounds(30, 26, 149, 23);
		optsPanel.add(rdbtnOp);
		
		rdbtnOp_1 = new JRadioButton("Op2");
		rdbtnOp_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[1].maskType, operations[1]);
			}
		});
		rdbtnOp_1.setVisible(false);
		rdbtnOp_1.setBounds(30, 53, 149, 23);
		optsPanel.add(rdbtnOp_1);
		
		rdbtnOp_2 = new JRadioButton("Op3");
		rdbtnOp_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[2].maskType, operations[2]);
			}
		});
		rdbtnOp_2.setVisible(false);
		rdbtnOp_2.setBounds(30, 80, 149, 23);
		optsPanel.add(rdbtnOp_2);
		
		rdbtnOp_3 = new JRadioButton("Op4");
		rdbtnOp_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setMask(operations[3].maskType, operations[3]);
			}
		});
		rdbtnOp_3.setVisible(false);
		rdbtnOp_3.setBounds(30, 106, 149, 23);
		optsPanel.add(rdbtnOp_3);
		
		JPanel maskPanel = new JPanel();
		getContentPane().add(maskPanel);
		maskPanel.setLayout(null);
		
		xmin3ymin3 = new JTextField();
		xmin3ymin3.setBounds(12, 30, 49, 19);
		maskPanel.add(xmin3ymin3);
		xmin3ymin3.setColumns(10);
		
		xmin2ymin3 = new JTextField();
		xmin2ymin3.setColumns(10);
		xmin2ymin3.setBounds(70, 30, 49, 19);
		maskPanel.add(xmin2ymin3);
		
		xmin1ymin3 = new JTextField();
		xmin1ymin3.setColumns(10);
		xmin1ymin3.setBounds(131, 30, 49, 19);
		maskPanel.add(xmin1ymin3);
		
		xymin3 = new JTextField();
		xymin3.setColumns(10);
		xymin3.setBounds(192, 30, 49, 19);
		maskPanel.add(xymin3);
		
		xplus1ymin3 = new JTextField();
		xplus1ymin3.setColumns(10);
		xplus1ymin3.setBounds(253, 30, 49, 19);
		maskPanel.add(xplus1ymin3);
		
		xplus2ymin3 = new JTextField();
		xplus2ymin3.setColumns(10);
		xplus2ymin3.setBounds(314, 30, 49, 19);
		maskPanel.add(xplus2ymin3);
		
		xplus3ymin3 = new JTextField();
		xplus3ymin3.setColumns(10);
		xplus3ymin3.setBounds(375, 30, 49, 19);
		maskPanel.add(xplus3ymin3);
		
		xmin3ymin2 = new JTextField();
		xmin3ymin2.setColumns(10);
		xmin3ymin2.setBounds(12, 61, 49, 19);
		maskPanel.add(xmin3ymin2);
		
		xmin2ymin2 = new JTextField();
		xmin2ymin2.setColumns(10);
		xmin2ymin2.setBounds(70, 61, 49, 19);
		maskPanel.add(xmin2ymin2);
		
		xmin1ymin2 = new JTextField();
		xmin1ymin2.setColumns(10);
		xmin1ymin2.setBounds(131, 61, 49, 19);
		maskPanel.add(xmin1ymin2);
		
		xymin2 = new JTextField();
		xymin2.setColumns(10);
		xymin2.setBounds(192, 61, 49, 19);
		maskPanel.add(xymin2);
		
		xplus1ymin2 = new JTextField();
		xplus1ymin2.setColumns(10);
		xplus1ymin2.setBounds(253, 61, 49, 19);
		maskPanel.add(xplus1ymin2);
		
		xplus2ymin2 = new JTextField();
		xplus2ymin2.setColumns(10);
		xplus2ymin2.setBounds(314, 61, 49, 19);
		maskPanel.add(xplus2ymin2);
		
		xplus3ymin2 = new JTextField();
		xplus3ymin2.setColumns(10);
		xplus3ymin2.setBounds(375, 61, 49, 19);
		maskPanel.add(xplus3ymin2);
		
		xmin3ymin1 = new JTextField();
		xmin3ymin1.setColumns(10);
		xmin3ymin1.setBounds(12, 93, 49, 19);
		maskPanel.add(xmin3ymin1);
		
		xmin2ymin1 = new JTextField();
		xmin2ymin1.setColumns(10);
		xmin2ymin1.setBounds(70, 93, 49, 19);
		maskPanel.add(xmin2ymin1);
		
		xmin1ymin1 = new JTextField();
		xmin1ymin1.setColumns(10);
		xmin1ymin1.setBounds(131, 93, 49, 19);
		maskPanel.add(xmin1ymin1);
		
		xymin1 = new JTextField();
		xymin1.setColumns(10);
		xymin1.setBounds(192, 93, 49, 19);
		maskPanel.add(xymin1);
		
		xplus1ymin1 = new JTextField();
		xplus1ymin1.setColumns(10);
		xplus1ymin1.setBounds(253, 93, 49, 19);
		maskPanel.add(xplus1ymin1);
		
		xplus2ymin1 = new JTextField();
		xplus2ymin1.setColumns(10);
		xplus2ymin1.setBounds(314, 93, 49, 19);
		maskPanel.add(xplus2ymin1);
		
		xplus3ymin1 = new JTextField();
		xplus3ymin1.setColumns(10);
		xplus3ymin1.setBounds(375, 93, 49, 19);
		maskPanel.add(xplus3ymin1);
		
		xmin3y = new JTextField();
		xmin3y.setColumns(10);
		xmin3y.setBounds(12, 124, 49, 19);
		maskPanel.add(xmin3y);
		
		xmin2y = new JTextField();
		xmin2y.setColumns(10);
		xmin2y.setBounds(70, 124, 49, 19);
		maskPanel.add(xmin2y);
		
		xmin1y = new JTextField();
		xmin1y.setColumns(10);
		xmin1y.setBounds(131, 124, 49, 19);
		maskPanel.add(xmin1y);
		
		xy = new JTextField();
		xy.setColumns(10);
		xy.setBounds(192, 124, 49, 19);
		maskPanel.add(xy);
		
		xplus1y = new JTextField();
		xplus1y.setColumns(10);
		xplus1y.setBounds(253, 124, 49, 19);
		maskPanel.add(xplus1y);
		
		xplus2y = new JTextField();
		xplus2y.setColumns(10);
		xplus2y.setBounds(314, 124, 49, 19);
		maskPanel.add(xplus2y);
		
		xplus3y = new JTextField();
		xplus3y.setColumns(10);
		xplus3y.setBounds(375, 124, 49, 19);
		maskPanel.add(xplus3y);
		
		xmin3yplus1 = new JTextField();
		xmin3yplus1.setColumns(10);
		xmin3yplus1.setBounds(12, 156, 49, 19);
		maskPanel.add(xmin3yplus1);
		
		xmin2yplus1 = new JTextField();
		xmin2yplus1.setColumns(10);
		xmin2yplus1.setBounds(70, 156, 49, 19);
		maskPanel.add(xmin2yplus1);
		
		xmin1yplus1 = new JTextField();
		xmin1yplus1.setColumns(10);
		xmin1yplus1.setBounds(131, 156, 49, 19);
		maskPanel.add(xmin1yplus1);
		
		xyplus1 = new JTextField();
		xyplus1.setColumns(10);
		xyplus1.setBounds(192, 156, 49, 19);
		maskPanel.add(xyplus1);
		
		xplus1yplus1 = new JTextField();
		xplus1yplus1.setColumns(10);
		xplus1yplus1.setBounds(253, 156, 49, 19);
		maskPanel.add(xplus1yplus1);
		
		xplus2yplus1 = new JTextField();
		xplus2yplus1.setColumns(10);
		xplus2yplus1.setBounds(314, 156, 49, 19);
		maskPanel.add(xplus2yplus1);
		
		xplus3yplus1 = new JTextField();
		xplus3yplus1.setColumns(10);
		xplus3yplus1.setBounds(375, 156, 49, 19);
		maskPanel.add(xplus3yplus1);
		
		xmin3yplus2 = new JTextField();
		xmin3yplus2.setColumns(10);
		xmin3yplus2.setBounds(12, 187, 49, 19);
		maskPanel.add(xmin3yplus2);
		
		xmin2yplus2 = new JTextField();
		xmin2yplus2.setColumns(10);
		xmin2yplus2.setBounds(70, 187, 49, 19);
		maskPanel.add(xmin2yplus2);
		
		xmin1yplus2 = new JTextField();
		xmin1yplus2.setColumns(10);
		xmin1yplus2.setBounds(131, 187, 49, 19);
		maskPanel.add(xmin1yplus2);
		
		xyplus2 = new JTextField();
		xyplus2.setColumns(10);
		xyplus2.setBounds(192, 187, 49, 19);
		maskPanel.add(xyplus2);
		
		xplus1yplus2 = new JTextField();
		xplus1yplus2.setColumns(10);
		xplus1yplus2.setBounds(253, 187, 49, 19);
		maskPanel.add(xplus1yplus2);
		
		xplus2yplus2 = new JTextField();
		xplus2yplus2.setColumns(10);
		xplus2yplus2.setBounds(314, 187, 49, 19);
		maskPanel.add(xplus2yplus2);
		
		xplus3yplus2 = new JTextField();
		xplus3yplus2.setColumns(10);
		xplus3yplus2.setBounds(375, 187, 49, 19);
		maskPanel.add(xplus3yplus2);
		
		xmin3yplus3 = new JTextField();
		xmin3yplus3.setColumns(10);
		xmin3yplus3.setBounds(12, 218, 49, 19);
		maskPanel.add(xmin3yplus3);
		
		xmin2yplus3 = new JTextField();
		xmin2yplus3.setColumns(10);
		xmin2yplus3.setBounds(70, 218, 49, 19);
		maskPanel.add(xmin2yplus3);
		
		xmin1yplus3 = new JTextField();
		xmin1yplus3.setColumns(10);
		xmin1yplus3.setBounds(131, 218, 49, 19);
		maskPanel.add(xmin1yplus3);
		
		xyplus3 = new JTextField();
		xyplus3.setColumns(10);
		xyplus3.setBounds(192, 218, 49, 19);
		maskPanel.add(xyplus3);
		
		xplus1yplus3 = new JTextField();
		xplus1yplus3.setColumns(10);
		xplus1yplus3.setBounds(253, 218, 49, 19);
		maskPanel.add(xplus1yplus3);
		
		xplus2yplus3 = new JTextField();
		xplus2yplus3.setColumns(10);
		xplus2yplus3.setBounds(314, 218, 49, 19);
		maskPanel.add(xplus2yplus3);
		
		xplus3yplus3 = new JTextField();
		xplus3yplus3.setColumns(10);
		xplus3yplus3.setBounds(375, 218, 49, 19);
		maskPanel.add(xplus3yplus3);
		
		rdbtnDontChangeEdge = new JRadioButton("No change");
		rdbtnDontChangeEdge.setSelected(true);
		rdbtnDontChangeEdge.setBounds(22, 245, 107, 23);
		maskPanel.add(rdbtnDontChangeEdge);
		
		rdbtnCopy = new JRadioButton("Copy");
		rdbtnCopy.setBounds(134, 245, 107, 23);
		maskPanel.add(rdbtnCopy);
		
		rdbtnScale1 = new JRadioButton("Scale 1");
		rdbtnScale1.setBounds(22, 265, 96, 23);
		maskPanel.add(rdbtnScale1);
		
		rdbtnScale2 = new JRadioButton("Scale 2");
		rdbtnScale2.setBounds(134, 265, 96, 23);
		maskPanel.add(rdbtnScale2);
		
		rdbtnScale3 = new JRadioButton("Scale 3");
		rdbtnScale3.setBounds(230, 265, 96, 23);
		maskPanel.add(rdbtnScale3);
		
		btnOk = new JButton("OK");
		btnOk.setBounds(342, 249, 117, 25);
		maskPanel.add(btnOk);
		
		btnCancel = new JButton("Cancel");
		btnCancel.setBounds(342, 286, 117, 25);
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
		
		scaleCollection = new ButtonGroup();
		scaleCollection.add(rdbtnDontChangeEdge); scaleCollection.add(rdbtnCopy);
		scaleCollection.add(rdbtnScale1); scaleCollection.add(rdbtnScale2); scaleCollection.add(rdbtnScale3);
		
	}
	
	public APO07MaskInput(OpObject[] operations) {
		this();
		this.operations = operations;
		System.out.println ("OpLEn= " + operations.length);
	//	this.mask1=mask1; this.mask2=mask2; this.mask3=mask3; this.mask4=mask4;
		for (int oplength=0; oplength<operations.length; oplength++) {
			if (oplength==0) {rdbtnOp.setVisible(true); rdbtnOp.setText(operations[0].label);}
			if (oplength==1) {rdbtnOp_1.setVisible(true); rdbtnOp_1.setText(operations[1].label);}
			if (oplength==2) {rdbtnOp_2.setVisible(true); rdbtnOp_2.setText(operations[2].label);}
			if (oplength==3) {rdbtnOp_3.setVisible(true); rdbtnOp_3.setText(operations[3].label);}
			
		} 		
	}
	
	private void setMask (MaskType maskType, OpObject op) {
		Arrays.stream(everyRing).forEach(x -> {x.setText("0"); x.setVisible(true);});
		if (maskType == MaskType._3x3) {
			Arrays.stream(outerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
			Arrays.stream(innerRing).forEach(x -> {x.setText("0"); x.setVisible(false);});
		}
		if (op!=null) {
			if (maskType == MaskType._3x3) {
				xmin1ymin1.setText(""+op.maskValues[0]); xymin1.setText(""+op.maskValues[1]); xplus1ymin1.setText(""+op.maskValues[2]);
				xmin1y.setText(""+op.maskValues[3]); xy.setText(""+op.maskValues[4]); xplus1y.setText(""+op.maskValues[5]);
				xmin1yplus1.setText(""+op.maskValues[6]); xyplus1.setText(""+op.maskValues[7]); xplus1yplus1.setText(""+op.maskValues[8]);
			}
		}		
	}
}
