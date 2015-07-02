package net.tfobz.arduinoracer.menu;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;

/**
 * Die Klasse SpieldeteilsPanel, ist jenes Panel, bei dem es dem Spielersteller
 * ermöglicht, dass er die Eigenschaften des Multiplayersspiels setzen kann.
 * Über ein Textfeld kann der Name, über einen Slider die Anzahl der Runder und
 * über eine ComboBox die Anzahl der Gegner
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SpieldetailsPanel extends NeuesSpielPanel {
	public SpieldetailsPanel(NeuesSpielPanel previos) {
		super(previos, previos.oberflaeche);
	}

	public SpieldetailsPanel(Spieloberflaeche oberflaeche) {
		super(null, oberflaeche);
		new Thread() {
			public void run() {
				synchronized (this) {
					try {
						this.wait(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				String spielname = "Mein neues Spiel";
				while (!spielname.isEmpty()) {
					spielnameTextField.setText(spielnameTextField.getText()
							+ spielname.charAt(0));
					if (spielname.length() == 1)
						spielname = "";
					else
						spielname = spielname.substring(1);
					synchronized (this) {
						try {
							this.wait(400);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}

	private JTextField textField;
	private JTextField spielnameTextField;
	private JButton btnOk = null;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setup() {
		setBounds(100, 100, 500, 500);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0, 0.0,
				Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblRenneinstellungen = new JLabel("Renneinstellungen");
		lblRenneinstellungen.setFont(new Font("Dialog", Font.BOLD, 20));
		GridBagConstraints gbc_lblRenneinstellungen = new GridBagConstraints();
		gbc_lblRenneinstellungen.anchor = GridBagConstraints.WEST;
		gbc_lblRenneinstellungen.gridwidth = 2;
		gbc_lblRenneinstellungen.insets = new Insets(0, 0, 5, 5);
		gbc_lblRenneinstellungen.gridx = 0;
		gbc_lblRenneinstellungen.gridy = 0;
		add(lblRenneinstellungen, gbc_lblRenneinstellungen);

		JLabel lblSpielname = new JLabel("Spielname:");
		GridBagConstraints gbc_lblSpielname = new GridBagConstraints();
		gbc_lblSpielname.anchor = GridBagConstraints.EAST;
		gbc_lblSpielname.insets = new Insets(0, 0, 5, 5);
		gbc_lblSpielname.gridx = 0;
		gbc_lblSpielname.gridy = 1;
		add(lblSpielname, gbc_lblSpielname);

		spielnameTextField = new JTextField();
		GridBagConstraints gbc_spielnameTextField = new GridBagConstraints();
		gbc_spielnameTextField.gridwidth = 2;
		gbc_spielnameTextField.insets = new Insets(0, 0, 5, 0);
		gbc_spielnameTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_spielnameTextField.gridx = 1;
		gbc_spielnameTextField.gridy = 1;
		add(spielnameTextField, gbc_spielnameTextField);
		spielnameTextField.setColumns(10);

		JLabel lblAnzahlRunden = new JLabel("Anzahl Runden:");
		GridBagConstraints gbc_lblAnzahlRunden = new GridBagConstraints();
		gbc_lblAnzahlRunden.anchor = GridBagConstraints.EAST;
		gbc_lblAnzahlRunden.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnzahlRunden.gridx = 0;
		gbc_lblAnzahlRunden.gridy = 2;
		add(lblAnzahlRunden, gbc_lblAnzahlRunden);

		final JSlider slider = new JSlider();
		slider.setMajorTickSpacing(5);
		slider.setMinorTickSpacing(1);
		slider.setSnapToTicks(true);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMinimum(0);
		slider.setMaximum(50);
		GridBagConstraints gbc_slider = new GridBagConstraints();
		gbc_slider.fill = GridBagConstraints.HORIZONTAL;
		gbc_slider.insets = new Insets(0, 0, 5, 5);
		gbc_slider.gridx = 1;
		gbc_slider.gridy = 2;
		add(slider, gbc_slider);

		textField = new JTextField(slider.getValue() + "") {
			@Override
			protected Document createDefaultModel() {
				return new PlainDocument() {
					@Override
					public void insertString(int offset, String string,
							AttributeSet attr) throws BadLocationException {

						try {
							String textString = textField.getText();
							if (offset == 0)
								textString = string + textString;
							else if (offset == textString.length())
								textString = textString + string;
							else
								textString = textString.substring(0, offset)
										+ string + textString.substring(offset);
							int text = Integer.parseInt(textString);
							if (text > 0 && text <= 50
									&& textString.charAt(0) != '0') {
								super.insertString(offset, string, attr);
								slider.setValue(text);
							}
						} catch (Exception e) {

						}
					}
				};
			}
		};

		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				if (slider.getValue() == 0)
					slider.setValue(1);
				else
					textField.setText("" + slider.getValue());
			}
		});
		slider.setValue(4);
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 2;
		add(textField, gbc_textField);
		textField.setColumns(2);

		JLabel lblAnzahlGegner = new JLabel("Anzahl Gegner:");
		GridBagConstraints gbc_lblAnzahlGegner = new GridBagConstraints();
		gbc_lblAnzahlGegner.insets = new Insets(0, 0, 5, 5);
		gbc_lblAnzahlGegner.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblAnzahlGegner.gridx = 0;
		gbc_lblAnzahlGegner.gridy = 3;
		add(lblAnzahlGegner, gbc_lblAnzahlGegner);

		final JComboBox comboBox = new JComboBox(new String[] { "1", "2", "3",
				"4", "5", "6", "7", "8" });
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.anchor = GridBagConstraints.NORTHWEST;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 3;
		add(comboBox, gbc_comboBox);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 3;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0 };
		gbl_panel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		btnOk = new JButton("Weiter");
		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (spielnameTextField.getText().isEmpty())
					JOptionPane.showMessageDialog(SpieldetailsPanel.this,
							"Sie müssen dem Spiel einen Namen geben", "Fehler",
							JOptionPane.ERROR_MESSAGE);
				else {
					MultiplayerSpiel spiel = new MultiplayerSpiel(Integer
							.parseInt(comboBox.getSelectedItem().toString()),
							slider.getValue(), spielnameTextField.getText());
					((WarteAufGegnerPanel) next).setSpiel(spiel);
					ok();
				}
			}
		});
		GridBagConstraints gbc_btnOk = new GridBagConstraints();
		oberflaeche.getStartmenu().getRootPane().setDefaultButton(btnOk);
		gbc_btnOk.anchor = GridBagConstraints.EAST;
		gbc_btnOk.insets = new Insets(0, 0, 0, 5);
		gbc_btnOk.gridx = 1;
		gbc_btnOk.gridy = 0;
		panel.add(btnOk, gbc_btnOk);

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
		panel.add(btnAbbrechen, gbc_btnAbbrechen);
		setVisible(true);
	}

	@Override
	protected void setDefaultButton() {
		oberflaeche.getStartmenu().getRootPane().setDefaultButton(btnOk);
	}
}
