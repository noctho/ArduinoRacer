package net.tfobz.arduinoracer.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.VerbindungAufnahmeThread;
import net.tfobz.arduinoracer.menu.components.SpielAnzeige;
import net.tfobz.arduinoracer.spielmodi.Multiplayer;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.Netzwerkstrings;

/**
 * Die Klasse SpielBeitretenPanel ist das Panel, das Panel, das angezeigt wird,
 * wenn ein Spielbeitreter dabei ist ein Spiel auszusuchen. Dabei werden ihm
 * alle Spiele angezeigt und er kann sich ein verfügbares aussuchen.
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SpielBeitretenPanel extends NeuesSpielPanel implements
		ActionListener {

	public SpielBeitretenPanel(NeuesSpielPanel previos, Multiplayer netzwerk) {
		super(previos, previos.oberflaeche);
		this.netzwerk = netzwerk;
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSpielBeitreten = new JLabel("Spiel beitreten");
			lblSpielBeitreten.setFont(new Font("Dialog", Font.BOLD, 20));
			GridBagConstraints gbc_lblSpielBeitreten = new GridBagConstraints();
			gbc_lblSpielBeitreten.anchor = GridBagConstraints.WEST;
			gbc_lblSpielBeitreten.insets = new Insets(0, 0, 5, 0);
			gbc_lblSpielBeitreten.gridx = 0;
			gbc_lblSpielBeitreten.gridy = 0;
			contentPanel.add(lblSpielBeitreten, gbc_lblSpielBeitreten);
		}
		{
			JLabel lblVerfgbareSpiele = new JLabel("Verfügbare Spiele:");
			GridBagConstraints gbc_lblVerfgbareSpiele = new GridBagConstraints();
			gbc_lblVerfgbareSpiele.anchor = GridBagConstraints.WEST;
			gbc_lblVerfgbareSpiele.insets = new Insets(0, 0, 5, 0);
			gbc_lblVerfgbareSpiele.gridx = 0;
			gbc_lblVerfgbareSpiele.gridy = 1;
			contentPanel.add(lblVerfgbareSpiele, gbc_lblVerfgbareSpiele);
		}
	}

	public SpielBeitretenPanel(Spieloberflaeche spieloberflaeche,
			Multiplayer netzwerk) {
		super(null, spieloberflaeche);
		this.netzwerk = netzwerk;
		setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 1.0,
				Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblSpielBeitreten = new JLabel("Spiel beitreten");
			lblSpielBeitreten.setFont(new Font("Dialog", Font.BOLD, 20));
			GridBagConstraints gbc_lblSpielBeitreten = new GridBagConstraints();
			gbc_lblSpielBeitreten.anchor = GridBagConstraints.WEST;
			gbc_lblSpielBeitreten.insets = new Insets(0, 0, 5, 0);
			gbc_lblSpielBeitreten.gridx = 0;
			gbc_lblSpielBeitreten.gridy = 0;
			contentPanel.add(lblSpielBeitreten, gbc_lblSpielBeitreten);
		}
		{
			JLabel lblVerfgbareSpiele = new JLabel("Verfügbare Spiele:");
			GridBagConstraints gbc_lblVerfgbareSpiele = new GridBagConstraints();
			gbc_lblVerfgbareSpiele.anchor = GridBagConstraints.WEST;
			gbc_lblVerfgbareSpiele.insets = new Insets(0, 0, 5, 0);
			gbc_lblVerfgbareSpiele.gridx = 0;
			gbc_lblVerfgbareSpiele.gridy = 1;
			contentPanel.add(lblVerfgbareSpiele, gbc_lblVerfgbareSpiele);
		}
	}

	private final JPanel contentPanel = new JPanel();
	private JPanel panel;
	private boolean beigetreten;
	private Multiplayer netzwerk;
	private VerbindungAufnahmeThread verbindungAufnahmeThread = null;

	/**
	 * Create the dialog.
	 */
	public void setup() {
		verbindungAufnahmeThread = netzwerk.getVerbindungAufnahmeThread();
		verbindungAufnahmeThread.setActionListener(this);
		verbindungAufnahmeThread
				.initSpieler(previus != null ? ((AutoAuswahlPanel) previus)
						.getMultiplayerSpieler() : AutoAuswahlPanel
						.getDefaultMultiplayerSpieler());
		{
			JScrollPane scrollPane = new JScrollPane();
			GridBagConstraints gbc_scrollPane = new GridBagConstraints();
			gbc_scrollPane.fill = GridBagConstraints.BOTH;
			gbc_scrollPane.gridx = 0;
			gbc_scrollPane.gridy = 2;
			contentPanel.add(scrollPane, gbc_scrollPane);
			{
				panel = new JPanel();
				scrollPane.setViewportView(panel);
				GridBagLayout gbl_panel = new GridBagLayout();
				gbl_panel.columnWidths = new int[] { 0, 0 };
				gbl_panel.rowHeights = new int[] { 0, 0 };
				gbl_panel.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
				gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
				panel.setLayout(gbl_panel);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			add(buttonPane, BorderLayout.SOUTH);
			{
				JButton cancelButton = new JButton("Zurück");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						abbrechen();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		try {
			verbindungAufnahmeThread.getSpiele();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setVisible(true);
	}

	/**
	 * zeigt alle verfügbare Spiele im Panel an
	 */
	public void setSpiele() {
		panel.removeAll();
		for (int i = 0; i < verbindungAufnahmeThread
				.getVerfuegbareMultiplayerSpiele().size(); i++) {
			final MultiplayerSpiel spiel = verbindungAufnahmeThread
					.getVerfuegbareMultiplayerSpiele().get(i);
			SpielAnzeige spielAnzeige = new SpielAnzeige(spiel,
					new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							try {
								if(previus == null)
									System.exit(0);
								String fehler = verbindungAufnahmeThread
										.spielTeilnehmen(spiel);
								if (fehler != null)
									JOptionPane
											.showMessageDialog(
													SpielBeitretenPanel.this,
													fehler, "Fehler",
													JOptionPane.ERROR_MESSAGE);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
						}
					});
			GridBagConstraints gbc_spielAnzeige = new GridBagConstraints();
			gbc_spielAnzeige.fill = GridBagConstraints.HORIZONTAL;
			gbc_spielAnzeige.gridx = 0;
			gbc_spielAnzeige.gridy = i * 2;
			panel.add(spielAnzeige, gbc_spielAnzeige);
			JSeparator separator = new JSeparator();
			separator.setBackground(Color.BLACK);
			GridBagConstraints gbc_separator = new GridBagConstraints();
			gbc_separator.fill = GridBagConstraints.HORIZONTAL;
			gbc_separator.gridx = 0;
			gbc_separator.gridy = i * 2 + 1;
			panel.add(separator, gbc_separator);
		}
		if (panel.getComponentCount() == 0) {
			JLabel label = new JLabel("kein Spiel verfügbar");
			GridBagConstraints gbc_label = new GridBagConstraints();
			gbc_label.fill = GridBagConstraints.HORIZONTAL;
			gbc_label.gridx = 0;
			gbc_label.gridy = 0;
			panel.add(label, gbc_label);
		} else
			panel.remove(panel.getComponentCount() - 1);
		repaint();
		super.oberflaeche.getStartmenu().setVisible(true);
	}

	/**
	 * setzt das Flag beigetreten
	 * 
	 * @param beigetreten
	 *            Flag, das gesetzt werden soll
	 */
	public void setBeigetreten(boolean beigetreten) {
		this.beigetreten = beigetreten;
	}

	/**
	 * liefert zurück, ob das Flag beigetreten gesetzt ist
	 * 
	 * @return true, wenn das Flag beigetreten gesetzt ist, ansonsten false
	 */
	public boolean getBeigetreten() {
		return beigetreten;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e == null)
			setSpiele();
		else {
			if (e.getActionCommand().indexOf(Netzwerkstrings.SPIEL_OK) == 0) {
				super.oberflaeche.setStart(verbindungAufnahmeThread);
				verbindungAufnahmeThread.removeActionListener();
				ok();
			}
		}
	}

	@Override
	protected void setDefaultButton() {
	}

	@Override
	public void removeAll() {
		panel.removeAll();
	}
}
