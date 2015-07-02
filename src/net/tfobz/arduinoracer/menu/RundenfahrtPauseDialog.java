package net.tfobz.arduinoracer.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;

import net.tfobz.arduinoracer.MultiShow;
import net.tfobz.arduinoracer.Spieloberflaeche;

/**
 * Die Klasse RundenfahrtPause ist der Dialog, der bei der Pause einer
 * Rundenfahrt angezeigt wird. Dabei wird die Möglichkeit einer Fortsetzung,
 * eines Neustarts und eines Beenden des Spiels gegeben.
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class RundenfahrtPauseDialog extends JDialog {

	public RundenfahrtPauseDialog(final Spieloberflaeche oberflaeche) {
		super(oberflaeche.frame, "Pause");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setBounds(oberflaeche.frame.getX() + oberflaeche.frame.getWidth() / 2
				- 110, oberflaeche.frame.getY() + oberflaeche.frame.getHeight()
				/ 2 - 151, 221, 152);
		setResizable(false);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 1.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		{
			JButton btnSpielFortsetzen = new JButton("Runde fortsetzen");
			btnSpielFortsetzen.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					oberflaeche.setPauseEnde(false);
					dispose();
				}
			});
			GridBagConstraints gbc_btnSpielFortsetzen = new GridBagConstraints();
			gbc_btnSpielFortsetzen.anchor = GridBagConstraints.SOUTH;
			gbc_btnSpielFortsetzen.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnSpielFortsetzen.insets = new Insets(0, 0, 5, 0);
			gbc_btnSpielFortsetzen.gridx = 0;
			gbc_btnSpielFortsetzen.gridy = 0;
			getContentPane().add(btnSpielFortsetzen, gbc_btnSpielFortsetzen);
		}
		{
			JButton btnRundeNeuStarten = new JButton("Runde neu starten");
			btnRundeNeuStarten.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					oberflaeche.setPauseEnde(true);
					dispose();
				}
			});
			GridBagConstraints gbc_btnRundeNeuStarten = new GridBagConstraints();
			gbc_btnRundeNeuStarten.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnRundeNeuStarten.insets = new Insets(0, 0, 5, 0);
			gbc_btnRundeNeuStarten.gridx = 0;
			gbc_btnRundeNeuStarten.gridy = 1;
			getContentPane().add(btnRundeNeuStarten, gbc_btnRundeNeuStarten);
		}
		{
			JButton btnBeenden = new JButton("Beenden");
			btnBeenden.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					dispose();
					oberflaeche.getStartmenu().setAktuellesPanel(
							new StartmenuPanel(oberflaeche.getStartmenu()));
					oberflaeche.frame.dispose();
				}
			});
			{
				JButton btnZeigeMultiplayer = new JButton("Beenden für Multiplayer");
				btnZeigeMultiplayer.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
						oberflaeche.frame.dispose();
						new MultiShow();
					}
				});
				GridBagConstraints gbc_btnZeigeMultiplayer = new GridBagConstraints();
				gbc_btnZeigeMultiplayer.fill = GridBagConstraints.HORIZONTAL;
				gbc_btnZeigeMultiplayer.insets = new Insets(0, 0, 5, 0);
				gbc_btnZeigeMultiplayer.gridx = 0;
				gbc_btnZeigeMultiplayer.gridy = 2;
				getContentPane().add(btnZeigeMultiplayer, gbc_btnZeigeMultiplayer);
			}
			GridBagConstraints gbc_btnBeenden = new GridBagConstraints();
			gbc_btnBeenden.anchor = GridBagConstraints.NORTH;
			gbc_btnBeenden.fill = GridBagConstraints.HORIZONTAL;
			gbc_btnBeenden.gridx = 0;
			gbc_btnBeenden.gridy = 3;
			getContentPane().add(btnBeenden, gbc_btnBeenden);
		}
		for (KeyListener listener : oberflaeche.getKeyListeners())
			addKeyListener(listener);
		setVisible(true);
	}
}
