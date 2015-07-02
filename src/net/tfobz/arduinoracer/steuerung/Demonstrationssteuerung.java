package net.tfobz.arduinoracer.steuerung;

import java.awt.event.KeyEvent;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.runden.Position;
import net.tfobz.arduinoracer.spielmodi.Demonstration;

/**
 * Die Klasse Demonstrationssteurung ist eine Steuerung, die keine Steuerung
 * unterstützt
 * 
 * @author Thomas Nocker
 *
 */
public class Demonstrationssteuerung implements Steureung {

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
	public Demonstrationssteuerung(Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
	}

	/**
	 * tut nichts, nichts Computer Verbindung aufnehmen muss
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

	@Override
	public void schreiben(boolean kollision) {
		if (oberflaeche.getSpielmodus() instanceof Demonstration) {
			Position position = ((Demonstration) oberflaeche.getSpielmodus()).rundenserieSpieler
					.getPositionBeiZeit(((Demonstration) oberflaeche
							.getSpielmodus()).multiplayerSpieler
							.getRundenserie().getSerienstart() == 0 ? 0
							: (oberflaeche.currentMillis - ((Demonstration) oberflaeche
									.getSpielmodus()).multiplayerSpieler
									.getRundenserie().getSerienstart()));
			oberflaeche.getAuto().setX(position.getX());
			oberflaeche.getAuto().setY(position.getY());
			oberflaeche.getAuto().setKurvenradius(position.getKurvenwinkel());
			oberflaeche.getAuto().setGeschwindigkeit(position.getV());
		}
	}

	/**
	 * tut nichts
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
			oberflaeche.getSpielmodus().setPauseknopfGedrueckt();
	}

	/**
	 * tut nichts
	 */
	@Override
	public void keyReleased(KeyEvent e) {
	}
}
