package net.tfobz.arduinoracer.menu.components;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.tfobz.arduinoracer.util.MultiplayerSpiel;

/**
 * Die Klasse SpielAnzeige ist jenes Panel, bei dem ein verfügbares Spiel
 * angezeigt wird. Es ist möglich in einer Liste verschiedene solcher Panels
 * anzuzeigen
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SpielAnzeige extends JPanel {

	public SpielAnzeige(MultiplayerSpiel verfuegbaresSpiel,
			ActionListener listener) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblName = new JLabel(verfuegbaresSpiel.getSpielname());
		lblName.setFont(new Font("Dialog", Font.BOLD, 18));
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.gridwidth = 5;
		gbc_lblName.insets = new Insets(0, 0, 5, 0);
		gbc_lblName.gridx = 0;
		gbc_lblName.gridy = 0;
		add(lblName, gbc_lblName);

		JLabel lblVon = new JLabel("von:");
		GridBagConstraints gbc_lblVon = new GridBagConstraints();
		gbc_lblVon.insets = new Insets(0, 0, 5, 5);
		gbc_lblVon.gridx = 0;
		gbc_lblVon.gridy = 1;
		add(lblVon, gbc_lblVon);

		JLabel lblSpielername = new JLabel(verfuegbaresSpiel
				.getSpielersteller().getName());
		GridBagConstraints gbc_lblSpielername = new GridBagConstraints();
		gbc_lblSpielername.anchor = GridBagConstraints.WEST;
		gbc_lblSpielername.gridwidth = 4;
		gbc_lblSpielername.insets = new Insets(0, 0, 5, 0);
		gbc_lblSpielername.gridx = 1;
		gbc_lblSpielername.gridy = 1;
		add(lblSpielername, gbc_lblSpielername);

		JLabel lblAnzahlRunden = new JLabel("Anzahl Runden:");
		GridBagConstraints gbc_lblAnzahlRunden = new GridBagConstraints();
		gbc_lblAnzahlRunden.gridwidth = 2;
		gbc_lblAnzahlRunden.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnzahlRunden.gridx = 0;
		gbc_lblAnzahlRunden.gridy = 2;
		add(lblAnzahlRunden, gbc_lblAnzahlRunden);

		JLabel lblRundenanzahl = new JLabel(""
				+ verfuegbaresSpiel.getAnzahlRunden());
		GridBagConstraints gbc_lblRundenanzahl = new GridBagConstraints();
		gbc_lblRundenanzahl.insets = new Insets(0, 0, 5, 5);
		gbc_lblRundenanzahl.gridx = 2;
		gbc_lblRundenanzahl.gridy = 2;
		add(lblRundenanzahl, gbc_lblRundenanzahl);

		JLabel label = new JLabel(
				verfuegbaresSpiel.getVerfuegbareSpielerVonAllen());
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 3;
		gbc_label.gridy = 2;
		add(label, gbc_label);

		JLabel lblVerfgbar = new JLabel("verfügbar");
		GridBagConstraints gbc_lblVerfgbar = new GridBagConstraints();
		gbc_lblVerfgbar.insets = new Insets(0, 0, 5, 0);
		gbc_lblVerfgbar.gridx = 4;
		gbc_lblVerfgbar.gridy = 2;
		add(lblVerfgbar, gbc_lblVerfgbar);

		JButton btnBeitreten = new JButton("Beitreten");
		btnBeitreten.addActionListener(listener);
		GridBagConstraints gbc_btnBeitreten = new GridBagConstraints();
		gbc_btnBeitreten.anchor = GridBagConstraints.EAST;
		gbc_btnBeitreten.gridwidth = 5;
		gbc_btnBeitreten.gridx = 0;
		gbc_btnBeitreten.gridy = 3;
		add(btnBeitreten, gbc_btnBeitreten);

	}

}
