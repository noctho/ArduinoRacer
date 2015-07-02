package net.tfobz.arduinoracer.menu;

import java.awt.Color;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JWindow;

import net.tfobz.arduinoracer.runden.Runde;
import net.tfobz.arduinoracer.util.Farben;

/**
 * Die Klasse RanglistenDialog ist der Dialog, der aufgerufen wird, wenn die
 * Rangliste aller Rundenfahrten abgefragt wird. Dabei werden in einem Dialog
 * alle gespeicherten Rundenfahrten angezeigt
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class RanglistenDialog extends JWindow {

	private BufferedImage obenLinks, obenRechts;
	private JPanel panel;
	private JLabel[] zeitLabels = null;
	private JLabel[] nameLabels = null;

	@SuppressWarnings("deprecation")
	public RanglistenDialog(final Vector<Runde> runden, JFrame parent) {
		super(parent);
		setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
		setBackground(new Color(0x00000000, true));
		try {
			obenLinks = loadImage(new File("obenLinks.png"));
			obenRechts = loadImage(new File("obenRechts.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(50, 50, (int) screenSize.getWidth() - 100,
				(int) screenSize.getHeight() - 100);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		{
			Font font = new Font("SansSerif", Font.PLAIN, 37);
			JComponent compontent = new JComponent() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					setSize(RanglistenDialog.this.getWidth(), 44);
					g.drawImage(obenLinks, 0, 0, obenLinks.getWidth(),
							obenLinks.getHeight(), this);
					g.drawImage(obenRechts, getWidth() - obenRechts.getWidth(),
							0, obenRechts.getWidth(), obenRechts.getHeight(),
							this);

					g.setColor(new Color(Farben.ANZEIGE_RAND));
					g.fillRect(obenLinks.getWidth(), 0, getWidth() - 2
							* obenLinks.getWidth(), obenLinks.getHeight());
					g.fillRect(obenLinks.getWidth(), getWidth() - 6, getWidth()
							- 2 * obenLinks.getWidth(), 6);
					g.fillRect(0, obenLinks.getWidth(), 6, getWidth() - 2
							* obenLinks.getHeight());
					g.fillRect(getWidth() - 6, obenLinks.getHeight(), 6,
							getWidth() - 2 * obenLinks.getHeight());
					g.fillRect(6, obenLinks.getHeight(), getWidth() - 12,
							44 - obenLinks.getHeight());
					paintComponents(g);
				}
			};
			compontent.setLayout(null);
			compontent.setBounds(0, 0, getWidth(), 44);
			compontent.setMinimumSize(new Dimension(44, 44));
			JLabel label = new JLabel("Rangliste");
			label.setBounds(22, 0, getWidth() - 24, 44);
			label.setFont(font);
			label.setForeground(Color.WHITE);
			JButton button = new JButton("Close");
			button.setBounds(getWidth() - 120, 12, 100, 20);
			compontent.add(button);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					((Startmenu) RanglistenDialog.this.getParent()).enable();
					dispose();
				}
			});
			compontent.add(label);
			GridBagConstraints gbc_component = new GridBagConstraints();
			gbc_component.gridwidth = 3;
			gbc_component.insets = new Insets(0, 0, 43, 5);
			gbc_component.anchor = GridBagConstraints.FIRST_LINE_START;
			gbc_component.fill = GridBagConstraints.BOTH;
			gbc_component.gridx = 0;
			gbc_component.gridy = 0;
			getContentPane().add(compontent, gbc_component);
		}
		{
			JComponent compontent = new JComponent() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					setBackground(Color.BLACK);
					setSize(6, getHeight());
					g.setColor(new Color(Farben.ANZEIGE_RAND));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			compontent.setLayout(null);
			GridBagConstraints gbc_component = new GridBagConstraints();
			gbc_component.insets = new Insets(0, 0, 0, 5);
			gbc_component.fill = GridBagConstraints.BOTH;
			gbc_component.gridx = 0;
			gbc_component.gridy = 1;
			getContentPane().add(compontent, gbc_component);
		}
		{
			JComponent compontent = new JComponent() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					setSize(6, getHeight());
					g.setColor(new Color(Farben.ANZEIGE_RAND));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			compontent.setLayout(null);
			GridBagConstraints gbc_component2 = new GridBagConstraints();
			gbc_component2.insets = new Insets(0, 0, 0, 5);
			gbc_component2.fill = GridBagConstraints.BOTH;
			gbc_component2.gridx = 2;
			gbc_component2.gridy = 1;
			getContentPane().add(compontent, gbc_component2);
		}
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.insets = new Insets(0, 0, 0, 0);
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 1;
			gbc_scrollPane.gridy = 1;
			scrollPane.setBackground(new Color(Farben.ANZEIGE_KOERPER));
			getContentPane().add(scrollPane, gbc_scrollPane);
			panel = new JPanel() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					for (int i = 0; i < runden.size(); i++) {
						zeitLabels[i].setBounds(panel.getWidth() - 110, i * 40,
								106, 40);
						nameLabels[i].setBounds(55, i * 40,
								panel.getWidth() - 210, 40);
					}
				}
			};
			panel.setSize(new Dimension(scrollPane.getWidth(),
					runden.size() * 40));
			panel.setLayout(null);
			panel.setPreferredSize(new Dimension(scrollPane.getWidth(), runden
					.size() * 40));
			panel.setBackground(new Color(Farben.ANZEIGE_KOERPER));
			scrollPane.setViewportView(panel);
		}
		{
			JComponent compontent = new JComponent() {
				@Override
				public void paint(Graphics g) {
					super.paint(g);
					setSize(getWidth(), 6);
					setBackground(Color.BLACK);
					g.setColor(new Color(Farben.ANZEIGE_RAND));
					g.fillRect(0, 0, getWidth(), getHeight());
				}
			};
			compontent.setLayout(null);
			GridBagConstraints gbc_component2 = new GridBagConstraints();
			gbc_component2.insets = new Insets(0, 0, 5, 0);
			gbc_component2.fill = GridBagConstraints.BOTH;
			gbc_component2.gridx = 0;
			gbc_component2.gridy = 2;
			gbc_component2.gridwidth = 3;
			getContentPane().add(compontent, gbc_component2);
		}
		Font font = new Font("SansSerif", Font.PLAIN, 24);
		zeitLabels = new JLabel[runden.size()];
		nameLabels = new JLabel[runden.size()];
		for (int i = 0; i < runden.size(); i++) {
			JLabel positionsLabel = new JLabel("" + i);
			positionsLabel.setFont(font);
			positionsLabel.setBounds(10, i * 40, 45, 40);
			panel.add(positionsLabel);
			zeitLabels[i] = new JLabel(Runde.zeitToString(runden.get(i)
					.getEndzeit()));
			zeitLabels[i].setFont(font);
			zeitLabels[i].setBounds(panel.getWidth() - 110, i * 40, 106, 40);
			zeitLabels[i].setForeground(Color.WHITE);
			panel.add(zeitLabels[i]);
			nameLabels[i] = new JLabel(runden.get(i).getName());
			nameLabels[i].setFont(font);
			nameLabels[i].setBounds(55, i * 40, panel.getWidth() - 210, 40);
			nameLabels[i].setForeground(Color.WHITE);
			panel.add(nameLabels[i]);
		}
		setVisible(true);
		requestFocus();
	}

	// @Override
	// public void paint(Graphics g) {
	// // panel.setPreferredSize(new Dimension(panel.getWidth(),
	// // panel.getWidth() + 50));
	// super.paint(g);
	// g.drawImage(obenLinks, 0, 0, obenLinks.getWidth(),
	// obenLinks.getHeight(), this);
	// g.drawImage(untenLinks, 0, getWidth() - untenLinks.getHeight(),
	// untenLinks.getWidth(), untenLinks.getHeight(), this);
	// g.drawImage(obenRechts, getWidth() - obenRechts.getWidth(), 0,
	// obenRechts.getWidth(), obenRechts.getHeight(), this);
	// g.drawImage(untenRechts, getWidth() - obenRechts.getWidth(), getWidth()
	// - untenLinks.getHeight(), this);
	// g.setColor(new Color(Farben.ANZEIGE_RAND));
	// g.fillRect(obenLinks.getWidth(), 0,
	// getWidth() - 2 * obenLinks.getWidth(), obenLinks.getHeight());
	// g.fillRect(obenLinks.getWidth(), getWidth() - 6, getWidth() - 2
	// * obenLinks.getWidth(), 6);
	// g.fillRect(0, obenLinks.getWidth(), 6,
	// getWidth() - 2 * obenLinks.getHeight());
	// g.fillRect(getWidth() - 6, obenLinks.getHeight(), 6, getWidth() - 2
	// * obenLinks.getHeight());
	// g.fillRect(6, obenLinks.getHeight(), getWidth() - 12,
	// 44 - obenLinks.getHeight());
	// g.setColor(new Color(Farben.ANZEIGE_KOERPER));
	// g.fillRect(6, 44, getWidth() - 12,
	// getWidth() - 44 - untenLinks.getHeight());
	// g.fillRect(untenLinks.getWidth(), getWidth() - untenLinks.getHeight(),
	// getWidth() - 2 * untenLinks.getWidth(),
	// untenLinks.getHeight() - 6);
	// g.setColor(new Color(0xFFFFFF));
	// // pApplet.anzeige2D.textSize(38);
	// // pApplet.anzeige2D.textAlign(PApplet.LEFT);
	// // g.text("Endstand nach " + spiel.getAnzahlRunden()
	// // + " Runden", 30, 37);
	// // String[] zeiten = getZeiten(pApplet, spieler);
	// for (int i = 0; i < runden.size(); i++) {
	// int x = 0;
	// int y = 44 + i * 40;
	// // zeitLabels[i].setBounds(getWidth() - 117, 44 + i * 40, 106, 40);
	// // nameLabels[i].setBounds(65, 44 + i * 40, getWidth() - 180, 40);
	// // setPosition(pApplet, x, y, i + 1);
	// // setName(pApplet, x, y, spieler.get(i).getName());
	// // setZeit(pApplet, x, y, zeiten[i]);
	// }
	// }

	public BufferedImage loadImage(File file) throws IOException {
		BufferedImage ret = ImageIO.read(file);
		for (int i = 0; i < ret.getWidth(); i++)
			for (int j = 0; j < ret.getHeight(); j++)
				if (ret.getRGB(i, j) != 0xFFFFFF)
					ret.setRGB(i, j, ret.getRGB(i, j) | 0xFF000000);
		return ret;
	}
}
