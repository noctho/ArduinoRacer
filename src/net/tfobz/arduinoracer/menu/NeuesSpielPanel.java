package net.tfobz.arduinoracer.menu;

import javax.swing.JPanel;

import net.tfobz.arduinoracer.Spieloberflaeche;

/**
 * Die Klasse NeuesSpielPanel ist die abstrakte Vorlage des des steuerbaren
 * Panels, das bei der Erstellung oder Beitretung eines Spiels konsultiert wird.
 * Es enthält ein Vorgänger- und ein Nachfolger-Panel
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public abstract class NeuesSpielPanel extends JPanel {

	/**
	 * Das Vorgänger-Panel
	 */
	protected NeuesSpielPanel previus = null;
	/**
	 * Das Nachfolger-Panel
	 */
	protected NeuesSpielPanel next = null;
	protected Spieloberflaeche oberflaeche = null;

	public NeuesSpielPanel(NeuesSpielPanel previos, Spieloberflaeche oberflaeche) {
		super();
		this.oberflaeche = oberflaeche;
		if (previos != null) {
			this.previus = previos;
			previos.next = this;
		}
	}

	/**
	 * erstellt das Panel, d.h. es werden alle Komponenten eingefügt
	 */
	public abstract void setup();

	/**
	 * beendet das Panel
	 */
	public void finish() {
		setVisible(false);
	}

	/**
	 * geht zum Vorgänger-Panel zurück
	 */
	public void abbrechen() {
		removeAll();
		if (previus != null) {
			oberflaeche.getStartmenu().setAktuellesPanel(previus);
			previus.setDefaultButton();
		} else {
			oberflaeche.getStartmenu().setAktuellesPanel(
					new StartmenuPanel(oberflaeche.getStartmenu()));
			oberflaeche.frame.dispose();
		}
	}

	/**
	 * setzt den Standartknopf des Panels
	 */
	protected abstract void setDefaultButton();

	/**
	 * geht zum Nachfolger-Panel
	 */
	public void ok() {
		if (next != null) {
			next.setup();
			oberflaeche.getStartmenu().setAktuellesPanel(next);
		} else {
			oberflaeche.setVisible();
			oberflaeche.getStartmenu().dispose();
		}
	}

	/**
	 * liefert das Nachfolger-Panel
	 * @return das Nachfolger-Panel
	 */
	public NeuesSpielPanel getNext() {
		return next;
	}
}
