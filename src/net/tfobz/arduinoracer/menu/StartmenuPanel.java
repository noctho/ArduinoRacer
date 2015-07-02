package net.tfobz.arduinoracer.menu;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;

import net.tfobz.arduinoracer.SpielStart;
import net.tfobz.arduinoracer.runden.Runde;
import net.tfobz.arduinoracer.spielmodi.Multiplayer;
import net.tfobz.arduinoracer.spielmodi.Rundenfahrt;
import net.tfobz.arduinoracer.util.Netzwerkstrings;

/**
 * Die Klasse StartmenuPanel ist jenes Panel, bei dem der Benutzer sein weiteres
 * Vorgehen entscheiden kann. Ihm ist es offen, ein Spiel zu erstellen, einem
 * Spiel beizutreten, eine neue Rundenfahrt zu starten oder die Rangliste
 * abzufragen.
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class StartmenuPanel extends JPanel {

	@SuppressWarnings("deprecation")
	public StartmenuPanel(final Startmenu startmenu) {
		setBounds(100, 100, 450, 300);
		setBorder(new EmptyBorder(5, 5, 5, 5));
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 0, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gbl_contentPane);

		JButton btnNeuesSpiel = new JButton("Neues Spiel");
		btnNeuesSpiel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new SpielStart(new Multiplayer(true), startmenu);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(StartmenuPanel.this,
							"Server nicht erreichbar", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnNeuesSpiel = new GridBagConstraints();
		gbc_btnNeuesSpiel.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNeuesSpiel.insets = new Insets(0, 0, 5, 0);
		gbc_btnNeuesSpiel.gridx = 0;
		gbc_btnNeuesSpiel.gridy = 0;
		add(btnNeuesSpiel, gbc_btnNeuesSpiel);

		JButton btnSpielbeitreten = new JButton("Spiel beitreten");
		btnSpielbeitreten.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new SpielStart(new Multiplayer(false), startmenu);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(StartmenuPanel.this,
							"Server nicht erreichbar", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		GridBagConstraints gbc_btnSpielbeitreten = new GridBagConstraints();
		gbc_btnSpielbeitreten.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnSpielbeitreten.insets = new Insets(0, 0, 5, 0);
		gbc_btnSpielbeitreten.gridx = 0;
		gbc_btnSpielbeitreten.gridy = 1;
		add(btnSpielbeitreten, gbc_btnSpielbeitreten);

		JButton btnRundenfahrt = new JButton("Rundenfahrt");
		btnRundenfahrt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					new SpielStart(new Rundenfahrt(), startmenu);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(StartmenuPanel.this,
							"Server nicht erreichbar", "Fehler",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				}
			}
		});
		GridBagConstraints gbc_btnRundenfahrt = new GridBagConstraints();
		gbc_btnRundenfahrt.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRundenfahrt.insets = new Insets(0, 0, 5, 0);
		gbc_btnRundenfahrt.gridx = 0;
		gbc_btnRundenfahrt.gridy = 2;
		add(btnRundenfahrt, gbc_btnRundenfahrt);

		JButton btnRangliste = new JButton("Rangliste");
		btnRangliste.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Vector<Runde> runden = ClientBuilder
						.newClient()
						.target("http://" + Netzwerkstrings.IP_ADRESSE
								+ ":8080/ArduinoRacerREST/rest")
						.path("arduinoracer/rangliste").request()
						.get(new GenericType<Vector<Runde>>() {
						});
				if (runden == null || runden.isEmpty())
					JOptionPane.showMessageDialog(StartmenuPanel.this,
							"Server nicht erreichbar", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				else {
					new RanglistenDialog(runden, startmenu);
					startmenu.disable();
				}
			}
		});
		GridBagConstraints gbc_btnRangliste = new GridBagConstraints();
		gbc_btnRangliste.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRangliste.insets = new Insets(0, 0, 5, 0);
		gbc_btnRangliste.gridx = 0;
		gbc_btnRangliste.gridy = 3;
		add(btnRangliste, gbc_btnRangliste);

		JButton btnBeenden = new JButton("Beenden");
		btnBeenden.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		GridBagConstraints gbc_btnBeenden = new GridBagConstraints();
		gbc_btnBeenden.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBeenden.gridx = 0;
		gbc_btnBeenden.gridy = 4;
		add(btnBeenden, gbc_btnBeenden);
	}
}
