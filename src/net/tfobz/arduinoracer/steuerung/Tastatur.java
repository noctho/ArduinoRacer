package net.tfobz.arduinoracer.steuerung;

import java.awt.event.KeyEvent;

import net.tfobz.arduinoracer.Spieloberflaeche;

/**
 * Die Klasse Tastatur verwaltet die Steuerung des Spiels mit einer
 * Tastatur-basierten Steuerung
 * 
 * @author Thomas Nocker
 *
 */
public class Tastatur implements Steureung {

	/**
	 * Die Spieloberfläche des Spiels
	 */
	private Spieloberflaeche oberflaeche = null;

	/**
	 * Konstruktor - setzt die Oberfläche des Spiels
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public Tastatur(Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
	}

	/**
	 * tut nichts, da die Tastatur nicht mit dem Computer Verbindung aufnehmen
	 * muss
	 */
	@Override
	public void verbindeMitSteuerung() {
	}

	/**
	 * tut nichts
	 */
	@Override
	public void entbindeMitSteuerung() {
	}

	/**
	 * tut nichts, da zur Tastatur keine Daten gesendet werden
	 */
	@Override
	public void schreiben(boolean kollision) {
	}

	/**
	 * passt die Beschleunigung und den Lenkwinkel des Spielautos der
	 * Spieloberfläche je nach Tasteneingabe an
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			oberflaeche.getAuto().setBeschleunigung(beschleunigung);
			break;
		case KeyEvent.VK_DOWN:
			oberflaeche.getAuto().setBeschleunigung(bremsung);
			break;
		case KeyEvent.VK_RIGHT:
			oberflaeche.getAuto().setLenkwinkel(-lenkung);
			break;
		case KeyEvent.VK_LEFT:
			oberflaeche.getAuto().setLenkwinkel(lenkung);
			break;
		case KeyEvent.VK_ESCAPE:
			oberflaeche.getSpielmodus().setPauseknopfGedrueckt();
			break;
		}
	}

	/**
	 * passt die Beschleunigung und den Lenkwinkel des Spielautos der
	 * Spieloberfläche je nach Tasteneingabe an
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			oberflaeche.getAuto().setBeschleunigung(0);
			break;
		case KeyEvent.VK_DOWN:
			oberflaeche.getAuto().setBeschleunigung(0);
			break;
		case KeyEvent.VK_RIGHT:
			oberflaeche.getAuto().setLenkwinkel(0);
			break;
		case KeyEvent.VK_LEFT:
			oberflaeche.getAuto().setLenkwinkel(0);
			break;
		}
	}
}
