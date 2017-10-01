package apo07;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuKeyEvent;
import javax.swing.JScrollPane;

public class MainScreen extends JFrame {
	private JMenuBar menuBar;
	private JMenu mnPlik;
	private JMenu mnJednopunkt;
	private JSplitPane allPicsPanel;
	private JSplitPane panelInputPics;
	private PicturePanel panelFirstInputPic;
	private PicturePanel panelSecondInputPic;
	private PicturePanel panelOutputPic;
	private JLabel lblDrugiObrazWejcioqwy;
	private JLabel lblObrazWyjciowy;
	private JMenuItem mnOpenPic1;
	private JMenuItem mnOpenPic2;
	
	private ImageIcon firstInputPic;
	private ImageIcon secondInputPic;
	private JScrollPane panelFirstInputScrollPane;
	private JScrollPane panelSecondInputScrollPane;

	

	/**
	 * One bloated ugly constructor
	 */
	public MainScreen() {
		setTitle("Algorytmy Przetwarzania Obrazów 07");
		setMinimumSize(new Dimension(500, 500));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 591, 322);
		JPanel MainScreenMainPanel = new JPanel();
		MainScreenMainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainScreenMainPanel);
		MainScreenMainPanel.setLayout(new BoxLayout(MainScreenMainPanel, BoxLayout.Y_AXIS));
		
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
		mnOpenPic1.addActionListener((ActionEvent event) -> {
			JFileChooser fileChooser = new JFileChooser();
			int openStatus = fileChooser.showOpenDialog(null);
			if (openStatus == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	            System.out.println("Otwieranie pliku" + file.toString());
	            firstInputPic = new ImageIcon(file.toString());
	            Image firstInputPicImg = firstInputPic.getImage();
	            panelFirstInputPic.setInternalImage(firstInputPicImg);
	        } else {
	            // kliknieto cancel
	        }
        });
		
		mnOpenPic1.setMnemonic('1');
		mnPlik.add(mnOpenPic1);
		
		mnOpenPic2 = new JMenuItem("Otwórz obraz 2");
		mnOpenPic2.setMnemonic('2');
		mnPlik.add(mnOpenPic2);
		mnPlik.add(mnExit);
		
		mnJednopunkt = new JMenu("Jednopunktowe");
		mnJednopunkt.setMnemonic('j');
		menuBar.add(mnJednopunkt);
		
		// MainScreenMainPanel.add(menuBar);
		setJMenuBar(menuBar);
		
		panelFirstInputPic = new PicturePanel(null);
		panelFirstInputPic.setPreferredSize(new Dimension(100, 3260));
		panelFirstInputPic.setMinimumSize(new Dimension(100, 100));
		
		panelSecondInputPic = new PicturePanel(null);
		panelSecondInputPic.setPreferredSize(new Dimension(100, 3260));
		panelSecondInputPic.setMinimumSize(new Dimension(100, 10));
		
		panelOutputPic = new PicturePanel(null);
		panelOutputPic.setMinimumSize(new Dimension(100, 10));
		
		panelFirstInputScrollPane = new JScrollPane(panelFirstInputPic);
		panelFirstInputScrollPane.setMinimumSize(new Dimension(22, 100));
		panelSecondInputScrollPane = new JScrollPane(panelSecondInputPic);
		panelSecondInputScrollPane.setMinimumSize(new Dimension(22, 100));
		
		panelInputPics = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFirstInputScrollPane, panelSecondInputScrollPane);
		
		lblDrugiObrazWejcioqwy = new JLabel("drugi obraz wejściowy");
		panelSecondInputPic.add(lblDrugiObrazWejcioqwy);
		
		allPicsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInputPics, panelOutputPic);
		
		lblObrazWyjciowy = new JLabel("obraz wyjściowy");
		panelOutputPic.add(lblObrazWyjciowy);
		MainScreenMainPanel.add(allPicsPanel);
	}

}
