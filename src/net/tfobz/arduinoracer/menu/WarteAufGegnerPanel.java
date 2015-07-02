package net.tfobz.arduinoracer.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.tfobz.arduinoracer.VerbindungAufnahmeThread;
import net.tfobz.arduinoracer.spielmodi.Multiplayer;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.Netzwerkstrings;

/**
 * Die Klasse WarteAufGegnerPanel ist jenes Panel, bei dem der Spielersteller
 * darauf warten muss, bis genügend Spieler seinem Spiel beigetreten sind und er
 * das Spiel starten darf. Dem Spieler steht es jedoch frei, ob er wartet oder
 * das Spiel zurückzieht
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class WarteAufGegnerPanel extends NeuesSpielPanel implements
		ActionListener {

	private MultiplayerSpiel spiel = null;
	private Multiplayer netzwerk = null;
	private JLabel beigetreteneSpieler;
	private VerbindungAufnahmeThread verbindungAufnahmeThread = null;

	public WarteAufGegnerPanel(NeuesSpielPanel previos, Multiplayer netzwerk) {
		super(previos, previos.oberflaeche);
		this.netzwerk = netzwerk;
	}

	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public void setup() {
		verbindungAufnahmeThread = netzwerk.getVerbindungAufnahmeThread();
		verbindungAufnahmeThread.setActionListener(this);
		verbindungAufnahmeThread
				.initSpieler(previus.previus != null?((AutoAuswahlPanel) previus.previus)
						.getMultiplayerSpieler(): AutoAuswahlPanel.getDefaultMultiplayerSpieler());
		try {
			verbindungAufnahmeThread.spielHinzufuegen(spiel);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 1.0,
				Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblName = new JLabel(spiel.getSpielname());
			lblName.setFont(new Font("Dialog", Font.BOLD, 18));
			GridBagConstraints gbc_lblName = new GridBagConstraints();
			gbc_lblName.gridwidth = 2;
			gbc_lblName.insets = new Insets(0, 0, 5, 0);
			gbc_lblName.gridx = 0;
			gbc_lblName.gridy = 0;
			contentPanel.add(lblName, gbc_lblName);
		}
		{
			beigetreteneSpieler = new JLabel(spiel.getBegetetreteneSpieler()
					+ "/" + spiel.getGesamtSpieler());
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.anchor = GridBagConstraints.EAST;
			gbc_label.insets = new Insets(0, 0, 5, 5);
			gbc_label.gridx = 0;
			gbc_label.gridy = 1;
			contentPanel.add(beigetreteneSpieler, gbc_label);
		}
		{
			JLabel lblBeigetreten = new JLabel("beigetreten");
			GridBagConstraints gbc_lblBeigetreten = new GridBagConstraints();
			gbc_lblBeigetreten.insets = new Insets(0, 0, 5, 0);
			gbc_lblBeigetreten.anchor = GridBagConstraints.WEST;
			gbc_lblBeigetreten.gridx = 1;
			gbc_lblBeigetreten.gridy = 1;
			contentPanel.add(lblBeigetreten, gbc_lblBeigetreten);
		}
		{
			JLabel lblAnzahlrunden = new JLabel("" + spiel.getAnzahlRunden());
			GridBagConstraints gbc_lblAnzahlrunden = new GridBagConstraints();
			gbc_lblAnzahlrunden.anchor = GridBagConstraints.EAST;
			gbc_lblAnzahlrunden.insets = new Insets(0, 0, 0, 5);
			gbc_lblAnzahlrunden.gridx = 0;
			gbc_lblAnzahlrunden.gridy = 2;
			contentPanel.add(lblAnzahlrunden, gbc_lblAnzahlrunden);
		}
		{
			JLabel lblRunden = new JLabel("Runden");
			GridBagConstraints gbc_lblRunden = new GridBagConstraints();
			gbc_lblRunden.anchor = GridBagConstraints.WEST;
			gbc_lblRunden.gridx = 1;
			gbc_lblRunden.gridy = 2;
			contentPanel.add(lblRunden, gbc_lblRunden);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Zurück");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						try {
							verbindungAufnahmeThread.spielLoeschen(spiel);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						contentPanel.removeAll();
						abbrechen();
					}
				});
			}
		}
		setVisible(true);
	}

	/**
	 * setzt das Spiel auf das gewartet werden soll
	 * 
	 * @param spiel
	 *            auf das gewartet werden soll
	 */
	public void setSpiel(MultiplayerSpiel spiel) {
		this.spiel = spiel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e == null) {
			for (MultiplayerSpiel spiel : verbindungAufnahmeThread
					.getVerfuegbareMultiplayerSpiele())
				if (spiel.getSpielersteller().getId() == verbindungAufnahmeThread
						.getSpieler().getId())
					this.spiel = spiel;
			beigetreteneSpieler.setText(spiel.getBegetetreteneSpieler() + "/"
					+ spiel.getGesamtSpieler());
			repaint();
			super.oberflaeche.getStartmenu().setVisible(true);
		} else if (e.getActionCommand().indexOf(Netzwerkstrings.SPIEL_OK) == 0) {
			super.oberflaeche.setStart(verbindungAufnahmeThread);
			verbindungAufnahmeThread.removeActionListener();
			ok();
		}
	}

	@Override
	protected void setDefaultButton() {
	}
}
