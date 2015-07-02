package net.tfobz.arduinoracer.menu;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.menu.components.BildKnopfAdapter;
import net.tfobz.arduinoracer.menu.components.BildKnopfComponent;
import net.tfobz.arduinoracer.steuerung.Arduino;
import net.tfobz.arduinoracer.steuerung.Steureung;
import net.tfobz.arduinoracer.steuerung.Tastatur;

/**
 * Die Klasse SteuerungPanel ist jenes Panel, bei dem der Spieler seine
 * Steuerungsart aussuchen kann. Dabei kann er zwischen der Tastatur und der
 * Arduino-Steuerung auswählen. Dabei werden nur die verfügbaren Steuerungen
 * angezeigt
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SteuerungPanel extends NeuesSpielPanel {
	public SteuerungPanel(NeuesSpielPanel previos, Spieloberflaeche oberflaeche) {
		super(previos, oberflaeche);
	}

	private BildKnopfComponent arduino;
	private BildKnopfComponent tastatur;
	// private BildKnopfComponent android;
	private JLabel lblSteuerung;
	private JPanel panel;
	private JButton btnOk;
	private JButton btnAbbrechen;
	private BildKnopfAdapter adapter = null;

	/**
	 * Create the android.
	 */
	public void setup() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		lblSteuerung = new JLabel("Steuerung");
		lblSteuerung.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblSteuerung = new GridBagConstraints();
		gbc_lblSteuerung.anchor = GridBagConstraints.WEST;
		gbc_lblSteuerung.gridwidth = 4;
		gbc_lblSteuerung.insets = new Insets(0, 0, 5, 5);
		gbc_lblSteuerung.gridx = 0;
		gbc_lblSteuerung.gridy = 0;
		add(lblSteuerung, gbc_lblSteuerung);

		if (Arduino.isConnected()) {
			arduino = new BildKnopfComponent(new File("arduino.png"));
			GridBagConstraints gbc_arduino = new GridBagConstraints();
			gbc_arduino.insets = new Insets(0, 0, 5, 5);
			gbc_arduino.fill = GridBagConstraints.BOTH;
			gbc_arduino.gridx = 1;
			gbc_arduino.gridy = 1;
			arduino.setPreferredSize(new Dimension(130, 130));
			add(arduino, gbc_arduino);
		}

		tastatur = new BildKnopfComponent(new File("tastatur.png"));
		GridBagConstraints gbc_tastatur = new GridBagConstraints();
		gbc_tastatur.insets = new Insets(0, 0, 5, 5);
		gbc_tastatur.fill = GridBagConstraints.BOTH;
		gbc_tastatur.gridx = 2;
		gbc_tastatur.gridy = 1;
		tastatur.setPreferredSize(new Dimension(130, 130));
		add(tastatur, gbc_tastatur);

		// android = new BildKnopfComponent(new File("android.png"));
		// GridBagConstraints gbc_android = new GridBagConstraints();
		// gbc_android.insets = new Insets(0, 0, 5, 5);
		// gbc_android.fill = GridBagConstraints.BOTH;
		// gbc_android.gridx = 3;
		// gbc_android.gridy = 1;
		// android.setPreferredSize(new Dimension(130, 130));
		// add(android, gbc_android);

		panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 5;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 2;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnOk = new JButton("Weiter");
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (arduino != null && arduino.isAusgewaehlt()
						|| tastatur.isAusgewaehlt()
				/* || android.isAusgewaehlt() */) {
					oberflaeche.setSteuerung(getSteuerung(oberflaeche));
					adapter.exitToAll();
					ok();
				} else
					JOptionPane.showMessageDialog(SteuerungPanel.this,
							"Keine Steuerung ausgewählt", "Fehler",
							JOptionPane.ERROR_MESSAGE);
			}
		});
		oberflaeche.getStartmenu().getRootPane().setDefaultButton(btnOk);
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 1;
		panel.add(btnOk, gbc_btnOk);

		btnAbbrechen = new JButton("Zurück");
		btnAbbrechen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abbrechen();
			}
		});
		GridBagConstraints gbc_btnAbbrechen = new GridBagConstraints();
		gbc_btnAbbrechen.anchor = GridBagConstraints.SOUTHEAST;
		gbc_btnAbbrechen.insets = new Insets(0, 0, 0, 5);
		gbc_btnAbbrechen.gridx = 0;
		gbc_btnAbbrechen.gridy = 1;
		panel.add(btnAbbrechen, gbc_btnAbbrechen);
		if (arduino != null)
			adapter = new BildKnopfAdapter(new BildKnopfComponent[] { arduino,
					tastatur /*
							 * , android
							 */});
		else
			adapter = new BildKnopfAdapter(
					new BildKnopfComponent[] { tastatur /*
														 * , android
														 */});
		oberflaeche.getStartmenu().setAktuellesPanel(this);
	}

	/**
	 * liefert die ausgewählte Steuerung zurück
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @return die ausgewählte Steuerung
	 */
	public Steureung getSteuerung(Spieloberflaeche oberflaeche) {
		Steureung ret = null;
		if (arduino != null && arduino.isAusgewaehlt())
			ret = new Arduino(oberflaeche);
		if (tastatur.isAusgewaehlt())
			ret = new Tastatur(oberflaeche);
		// if (android.isAusgewaehlt())
		// ret = new Tastatur();
		return ret;
	}

	@Override
	protected void setDefaultButton() {
		oberflaeche.getStartmenu().getRootPane().setDefaultButton(btnOk);
	}
}
