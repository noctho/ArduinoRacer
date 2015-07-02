package net.tfobz.arduinoracer.menu;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Die Klasse Startmenu ist jene Klasse, die das Menü des Spiels verwaltet. es
 * ist möglich ein Panel des jeweiligen Menüs zu setzen. Dieses ist
 * standardmäßig das Hauptmenü
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class Startmenu extends JFrame {

	private JPanel contentPane;
	public Thread warteThread = new Thread();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Startmenu frame = new Startmenu();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Startmenu() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			setIconImage(new ImageIcon("icon.png").getImage());
			// Lists all files in folder
			File folder = new File(".");
			for (File file : folder.listFiles()) {
				if (file.getPath().endsWith(".log"))
					file.delete();
			}
		} catch (Exception e) {

		}
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 500, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);

		contentPane.add(new StartmenuPanel(this), BorderLayout.CENTER);
	}

	/**
	 * setzt das steuerbare Panel im Menüs
	 * 
	 * @param panel
	 *            das als aktives Panel festgelegt werden soll
	 */
	public void setAktuellesPanel(JPanel panel) {
		contentPane.remove(0);
		contentPane.add(panel, BorderLayout.CENTER);
		setVisible(true);
		repaint();
	}

}
