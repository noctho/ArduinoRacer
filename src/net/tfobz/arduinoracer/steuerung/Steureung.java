package net.tfobz.arduinoracer.steuerung;

import java.awt.event.KeyEvent;

/**
 * Das Interface Steuerung ist jenes Interface, das implementiert werden muss,
 * falls eine Klasse ein Steuerungsart bei der Spieloberfläche darstellen will.
 * 
 * @author Thomas Nocker
 *
 */
public interface Steureung {
	/**
	 * immer zu setzende Beschleunigung der Vorwärtsbewegung des Autos
	 */
	int beschleunigung = 20;
	/**
	 * immer zu setzende Beschleunigung der Rückwärtsbewegung des Autos
	 */
	int bremsung = -60;
	/**
	 * immer zu setzender Lenkwinkel des Autos
	 */
	float lenkung = 1.3F;

	/**
	 * Es wird Verbindung mit der etwaigen externen Steuerung aufgenommen
	 */
	void verbindeMitSteuerung();

	/**
	 * Löst die Verbindung mit der etwaigen externen Steuerung auf
	 */
	void entbindeMitSteuerung();

	/**
	 * schickt die nötigen Daten zur Steuerung
	 * 
	 * @param kollision
	 *            ob das Auto kollidiert oder nicht
	 */
	void schreiben(boolean kollision);

	/**
	 * reagiert, ob eine Taste in der Tastatur gedrückt ist
	 * 
	 * @param e
	 *            Event des Tastendrucks
	 */
	void keyPressed(KeyEvent e);

	/**
	 * reagiert, ob eine Taste in der Tastatur losgelassen wurde
	 * 
	 * @param e
	 *            Event des Tastenloslassens
	 */
	void keyReleased(KeyEvent e);

}
