package net.tfobz.arduinoracer.menu;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.tfobz.arduinoracer.komponenten.MultiplayerAuto;
import net.tfobz.arduinoracer.komponenten.SpielAuto;
import net.tfobz.arduinoracer.menu.components.AutoComponent;
import net.tfobz.arduinoracer.menu.components.BildKnopfAdapter;
import net.tfobz.arduinoracer.util.Farben;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;

/**
 * Die Klasse AutoAuswahlPanel ermöglicht die Wahl eines der 16 verschiedene
 * Autofarben und des Namen des Spielers
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class AutoAuswahlPanel extends NeuesSpielPanel {

	public AutoAuswahlPanel(NeuesSpielPanel previos) {
		super(previos, previos.oberflaeche);
	}

	private AutoComponent[] autos;
	private JButton btnOK;
	private JTextField textField;
	private BildKnopfAdapter adapter = null;

	/**
	 * Create the panel.
	 */
	public void setup() {
		setBounds(100, 100, 500, 400);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, 1.0, 1.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridwidth = 5;
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblName = new JLabel("Name:");
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.insets = new Insets(0, 0, 0, 5);
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		panel.add(lblName, gbc_lblName);

		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);

		JLabel lblAutowahl = new JLabel("Autowahl");
		lblAutowahl.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblAutowahl = new GridBagConstraints();
		gbc_lblAutowahl.anchor = GridBagConstraints.WEST;
		gbc_lblAutowahl.gridwidth = 5;
		gbc_lblAutowahl.insets = new Insets(0, 0, 5, 0);
		gbc_lblAutowahl.gridx = 0;
		gbc_lblAutowahl.gridy = 1;
		add(lblAutowahl, gbc_lblAutowahl);

		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.anchor = GridBagConstraints.EAST;
		gbc_panel_1.gridwidth = 5;
		gbc_panel_1.insets = new Insets(0, 0, 0, 5);
		gbc_panel_1.fill = GridBagConstraints.VERTICAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 5;
		add(panel_1, gbc_panel_1);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel_1.rowHeights = new int[] { 0, 0 };
		gbl_panel_1.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel_1.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel_1.setLayout(gbl_panel_1);

		btnOK = new JButton("Weiter");
		btnOK.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (textField.getText().isEmpty())
					JOptionPane.showMessageDialog(AutoAuswahlPanel.this,
							"Sie haben sich keinen Namen gegeben", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				else if (getFarbe() == -1)
					JOptionPane.showMessageDialog(AutoAuswahlPanel.this,
							"Sie haben sich kein Auto ausgesucht", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				else {
					oberflaeche.setAuto(getAuto());
					oberflaeche.setSpielerName(getSpielername());
					adapter.exitToAll();
					ok();
				}
			}
		});
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		gbc_btnOk.anchor = GridBagConstraints.EAST;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 0;
		panel_1.add(btnOK, gbc_btnOk);

		JButton btnAbbrechen = new JButton("Zurück");
		btnAbbrechen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				abbrechen();
			}
		});
		GridBagConstraints gbc_btnAbbrechen = new GridBagConstraints();
		gbc_btnAbbrechen.anchor = GridBagConstraints.EAST;
		gbc_btnAbbrechen.insets = new Insets(0, 0, 0, 5);
		gbc_btnAbbrechen.gridx = 0;
		gbc_btnAbbrechen.gridy = 0;
		panel_1.add(btnAbbrechen, gbc_btnAbbrechen);
		autos = new AutoComponent[Farben.AUTO_FARBEN.length];
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 5; j++) {
				AutoComponent auto = new AutoComponent(i * 5 + j);
				autos[i * 5 + j] = auto;
				GridBagConstraints gbc_autoLinks = new GridBagConstraints();
				gbc_autoLinks.insets = new Insets(0, 0, 5, 5);
				gbc_autoLinks.fill = GridBagConstraints.BOTH;
				gbc_autoLinks.gridx = j;
				gbc_autoLinks.gridy = i + 2;
				add(autos[i * 5 + j], gbc_autoLinks);
			}
		adapter = new BildKnopfAdapter(autos);
		setDefaultButton();
		setVisible(true);
	}

	/**
	 * liefert die gewählte Farbe des Autos zurück
	 * 
	 * @return die gewählte Farbe des Autos
	 */
	public int getFarbe() {
		int ret = -1;
		for (AutoComponent auto : autos)
			if (auto.isAusgewaehlt())
				ret = auto.getFarbe();
		return ret;
	}

	/**
	 * liefert den eingegebenen Namen des Spielers zurück
	 * 
	 * @return der eingegebene Name des Spielers
	 */
	public String getSpielername() {
		return textField.getText();
	}

	/**
	 * liefert den Spieler mit den eingegebenen Werten zurück
	 * 
	 * @return der Spieler mit den eingegebenen Werten
	 */
	public MultiplayerSpieler getMultiplayerSpieler() {
		return new MultiplayerSpieler(new MultiplayerAuto(getFarbe()), -1,
				getSpielername());
	}

	/**
	 * liefert das ausgewählte Auto zurück
	 * 
	 * @return das ausgewählte Auto
	 */
	public SpielAuto getAuto() {
		return new SpielAuto(getFarbe());
	}

	@Override
	protected void setDefaultButton() {
		oberflaeche.getStartmenu().getRootPane().setDefaultButton(btnOK);
	}

	public static MultiplayerSpieler getDefaultMultiplayerSpieler() {
		return new MultiplayerSpieler(new MultiplayerAuto(0), -1,
				"Sepp");
	}
}
