package net.tfobz.arduinoracer;

import java.awt.BorderLayout;
import java.awt.Frame;

import net.tfobz.arduinoracer.menu.Startmenu;
import net.tfobz.arduinoracer.spielmodi.Spielmodi;
import processing.core.PApplet;

/**
 * Die Klasse Spiel Start ist das Frame, auf welchem die Spieloberfläche läuft
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SpielStart extends Frame {

	/**
	 * startet die Spieloberfläche mit dem übergebenen Spielmodus und des
	 * übergeben Startmenüs
	 * 
	 * @param spielmodus
	 *            mit dem das Spiel gestartet werden soll
	 * @param startmenu
	 *            mit dem das Spiel gestartet werden soll
	 */
	public SpielStart(Spielmodi spielmodus, Startmenu startmenu) {
		super("Embedded PApplet");

		setLayout(new BorderLayout());
		PApplet embed = new Spieloberflaeche(this, spielmodus, startmenu);
		add(embed, BorderLayout.CENTER);
	}
}
