package apo07;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.function.Consumer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class MainScreen extends JFrame {
	private JMenuBar menuBar;
	private JMenu mnPlik;
	private JMenu mnJednopunkt;
	private JSplitPane allPicsPanel;
	private JSplitPane panelInputPics;
	private PicturePanel panelFirstInputPic;
	private PicturePanel panelSecondInputPic;
	private PicturePanel panelOutputPic;
	private JMenuItem mnOpenPic1;
	private JMenuItem mnOpenPic2;
	
	private ImageIcon firstInputPic;
	private ImageIcon secondInputPic;
	private ImageIcon outputPic;
	private JScrollPane panelFirstInputScrollPane;
	private JScrollPane panelSecondInputScrollPane;
	private JScrollPane panelOutputScrollPane;
	
	private JPanel toolsPanel;
	private JButton histButton;
	private JLabel lblNarzdzia;
	
	private Consumer<ActionEvent> aaa;

	

	/**
	 * One bloated ugly constructor
	 */
	public MainScreen() {
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
		mnOpenPic1.addActionListener((ActionEvent event) -> {
			loadPicIntoPanel(panelFirstInputPic, 1);
        });
		
		
		mnOpenPic1.setMnemonic('1');
		mnPlik.add(mnOpenPic1);
		
		mnOpenPic2 = new JMenuItem("Otwórz obraz 2");
		mnOpenPic2.setMnemonic('2');
		mnOpenPic2.addActionListener((ActionEvent event) -> {
			loadPicIntoPanel(panelSecondInputPic, 2);
        });
		
		
		mnPlik.add(mnOpenPic2);
		mnPlik.add(mnExit);
		
		mnJednopunkt = new JMenu("Jednopunktowe");
		mnJednopunkt.setMnemonic('j');
		menuBar.add(mnJednopunkt);
		
		// MainScreenMainPanel.add(menuBar);
		setJMenuBar(menuBar);
		
		panelFirstInputPic = new PicturePanel(null);
		panelFirstInputPic.setMaximumSize(new Dimension(100, 100));
		panelFirstInputPic.setPreferredSize(new Dimension(100, 100));
		panelFirstInputPic.setMinimumSize(new Dimension(100, 100));
		
		panelSecondInputPic = new PicturePanel(null);
		panelSecondInputPic.setPreferredSize(new Dimension(100, 100));
		panelSecondInputPic.setMinimumSize(new Dimension(100, 10));
		
		panelOutputPic = new PicturePanel(null);
		panelOutputPic.setPreferredSize(new Dimension(400, 10));
		panelOutputPic.setMinimumSize(new Dimension(400, 10));
		
		panelFirstInputScrollPane = new JScrollPane(panelFirstInputPic);
		panelFirstInputScrollPane.setPreferredSize(new Dimension(400, 300));
		panelFirstInputScrollPane.setMinimumSize(new Dimension(400, 250));
		panelSecondInputScrollPane = new JScrollPane(panelSecondInputPic);
		panelSecondInputScrollPane.setPreferredSize(new Dimension(103, 200));
		panelSecondInputScrollPane.setMinimumSize(new Dimension(22, 100));
		panelOutputScrollPane = new JScrollPane(panelOutputPic);
		panelOutputScrollPane.setPreferredSize(new Dimension(103, 300));
		panelOutputScrollPane.setMinimumSize(new Dimension(103, 300));
		
		panelInputPics = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelFirstInputScrollPane, panelSecondInputScrollPane);
		panelInputPics.setMinimumSize(new Dimension(400, 212));
		panelInputPics.setPreferredSize(new Dimension(400, 615));
		
		allPicsPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelInputPics, panelOutputScrollPane);
		allPicsPanel.setMinimumSize(new Dimension(900, 302));
		allPicsPanel.setPreferredSize(new Dimension(900, 617));
		
		// TOOLS PANEL
		
		toolsPanel = new JPanel();
		toolsPanel.setPreferredSize(new Dimension(10, 40));
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.X_AXIS));
		
		lblNarzdzia = new JLabel("Narzędzia:");
		toolsPanel.add(lblNarzdzia);
		
		
		histButton = new JButton("Hist");
		histButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Image firstInputPicImg = null;
				Image secondInputPicImg = null;
				BufferedImage b = null;
				BufferedImage b2 = null;
				try { 
					firstInputPicImg = firstInputPic.getImage();
					secondInputPicImg = secondInputPic.getImage();
					}
				catch (Exception ex) {}
				
	           try {
				b = new BufferedImage (firstInputPicImg.getWidth(null), firstInputPicImg.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
				b.getGraphics().drawImage(firstInputPicImg, 0,0,null);
				
	            b2 = new BufferedImage (secondInputPicImg.getWidth(null), secondInputPicImg.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
	            b2.getGraphics().drawImage(secondInputPicImg, 0,0,null);
	           }
	           catch (Exception ex) {}

	           new HistWindow(b, b2).setVisible(true);
				
			}
		});
		toolsPanel.add(histButton);
		
		
		MainScreenMainPanel.add(toolsPanel);
		toolsPanel.setAlignmentX(LEFT_ALIGNMENT);
		MainScreenMainPanel.add(allPicsPanel);
	}
	
	private void loadPicIntoPanel(PicturePanel whichPanel, int whichPanelAsInt) {
		JFileChooser fileChooser = new JFileChooser();
		int openStatus = fileChooser.showOpenDialog(null);
		if (openStatus == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            System.out.println("Otwieranie pliku" + file.toString());
            if (whichPanelAsInt==1) { 
            	firstInputPic = new ImageIcon(file.toString());
            	Image firstInputPicImg = firstInputPic.getImage();
                whichPanel.setInternalImage(firstInputPicImg);
            }
            if (whichPanelAsInt==2) { 
            	secondInputPic = new ImageIcon(file.toString());
            	Image secondInputPicImg = secondInputPic.getImage();
                whichPanel.setInternalImage(secondInputPicImg);
            }
            
        } else {
            // cancel clicked
        }
	}
}
