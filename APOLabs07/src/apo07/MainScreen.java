package apo07;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.Dimension;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuKeyEvent;

public class MainScreen extends JFrame {
	private JMenuBar menuBar;
	private JMenu mnPlik;
	private JMenu mnJednopunkt;
	private JSplitPane allPicsPanel;
	private JSplitPane panelInputPics;
	private JPanel panelFirstInputPic;
	private JPanel panelSecondInputPic;
	private JPanel panelOutputPic;
	private JLabel lblPierwszyObrazWejciowy;
	private JLabel lblDrugiObrazWejcioqwy;
	private JLabel lblObrazWyjciowy;
	private JMenuItem mntmNewMenuItem;
	private JMenuItem mntmOtwrzObraz;

	

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
		
		JMenuItem mntmWyjd = new JMenuItem("Wyjdź");
		mntmWyjd.addActionListener((ActionEvent event) -> {
            System.exit(0);
        });
		
		mntmNewMenuItem = new JMenuItem("Otwórz obraz 1");
		mntmNewMenuItem.addActionListener((ActionEvent event) -> {
			JFileChooser fileChooser = new JFileChooser();
			int openStatus = fileChooser.showOpenDialog(null);
			if (openStatus == JFileChooser.APPROVE_OPTION) {
	            File file = fileChooser.getSelectedFile();
	           // tu otwarcie pliku, wczytanie bitmapy etc
	        } else {
	            // kliknieto cancel
	        }
        });
		
		mntmNewMenuItem.setMnemonic('1');
		mnPlik.add(mntmNewMenuItem);
		
		mntmOtwrzObraz = new JMenuItem("Otwórz obraz 2");
		mntmOtwrzObraz.setMnemonic('2');
		mnPlik.add(mntmOtwrzObraz);
		mnPlik.add(mntmWyjd);
		
		mnJednopunkt = new JMenu("Jednopunktowe");
		mnJednopunkt.setMnemonic('j');
		menuBar.add(mnJednopunkt);
		
		// MainScreenMainPanel.add(menuBar);
		setJMenuBar(menuBar);
		
		panelFirstInputPic = new JPanel();
		panelFirstInputPic.setPreferredSize(new Dimension(100, 3260));
		panelFirstInputPic.setMinimumSize(new Dimension(100, 100));
		
		panelSecondInputPic = new JPanel();
		panelSecondInputPic.setPreferredSize(new Dimension(100, 3260));
		panelSecondInputPic.setMinimumSize(new Dimension(100, 10));
		
		panelOutputPic = new JPanel();
		panelOutputPic.setMinimumSize(new Dimension(100, 10));
		
		panelInputPics = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFirstInputPic, panelSecondInputPic);
		
		lblDrugiObrazWejcioqwy = new JLabel("drugi obraz wejścioqwy");
		panelSecondInputPic.add(lblDrugiObrazWejcioqwy);
		
		lblPierwszyObrazWejciowy = new JLabel("pierwszy obraz wejściowy");
		panelFirstInputPic.add(lblPierwszyObrazWejciowy);
		
		
		allPicsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInputPics, panelOutputPic);
		
		lblObrazWyjciowy = new JLabel("obraz wyjściowy");
		panelOutputPic.add(lblObrazWyjciowy);
		MainScreenMainPanel.add(allPicsPanel);
	}

}
