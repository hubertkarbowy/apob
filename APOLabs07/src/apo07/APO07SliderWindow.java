package apo07;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.Dialog.ModalityType;
import java.util.Hashtable;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Rectangle;
import static apo07.APO07StaticPointMethods.*;

public class APO07SliderWindow extends JDialog {
	private JSlider slider;
	private Hashtable sliderLabels = new Hashtable();
	private JButton btnOk;
	
	private PicturePanel outpic = null;
	private BufferedImage imin = null;
	private BufferedImage imout = null;
	private JLabel thresholdValueLabel;
	
	
	
	public APO07SliderWindow(BufferedImage im, PicturePanel outpic) {
		setBounds(new Rectangle(0, 0, 400, 150));
		this.imin=im;
		this.outpic=outpic;
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("APO07 Threshold");
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getContentPane().setLayout(null);
		
		sliderLabels.put(new Integer(0), new JLabel("0") ); sliderLabels.put(new Integer(127), new JLabel("127") ); sliderLabels.put(new Integer(255), new JLabel("255") );
		slider = new JSlider();
		slider.setMaximum(255);
		slider.setLabelTable(sliderLabels);
		slider.setToolTipText("");
		slider.setBounds(12, 28, 317, 47);
		slider.setPaintLabels(true);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				thresholdValueLabel.setText(""+slider.getValue());
				imout=thresholdImg(imin, slider.getValue());
				outpic.setInternalImage(imout);
			}
		});
		getContentPane().add(slider);
		
		btnOk = new JButton("Done");
		btnOk.setMnemonic('o');
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnOk.setBounds(144, 77, 117, 25);
		getContentPane().add(btnOk);
		
		thresholdValueLabel = new JLabel("val");
		thresholdValueLabel.setBounds(347, 45, 70, 15);
		getContentPane().add(thresholdValueLabel);
		setLocationRelativeTo(null);
	}
}
