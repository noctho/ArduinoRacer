package net.tfobz.arduinoracer.spielmodi;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.menu.NeuesSpielPanel;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import processing.core.PGraphics;

/**
 * Das Interface Spielmodi ist jenes Interface, das implementiert werden muss,
 * falls eine Klasse ein Spielmodus bei der Spieloberfläche darstellen will.
 * 
 * @author Thomas Nocker
 *
 */
public interface Spielmodi extends Runnable {

	/**
	 * setzt dem übergebenen Dialog als Kind die nötigen Dialoge für den
	 * jeweiligen Spielmodus
	 * 
	 * @param dialog
	 *            deren Kinder hinzugefügt werden
	 */
	void getDialoge(NeuesSpielPanel dialog);

	/**
	 * liefert den aktuellen Sektor auf der Rennstrecke
	 * 
	 * @return der aktuelle Sektor auf der Rennstrecke
	 */
	byte getCurrentSektor();

	/**
	 * wird beim Start des Spiels ausgeführt
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	void onStart(Spieloberflaeche oberflaeche);

	/**
	 * wird beim Zeichnen der Spieloberfläche ausgeführt
	 * 
	 * @param bodenbegebenheit
	 *            aktuelle Bodengegebenheit des Spielautos
	 * @param anzahlReifenImGras
	 *            Anzahl der Reifen des Spielautos im Rasen
	 */
	void onDraw(Bodenbegebenheit bodenbegebenheit, int anzahlReifenImGras);

	/**
	 * wird beim Beenden des Spiels ausgeführt
	 */
	void onStop();

	/**
	 * zeichnet in der übergebenen Minimap die nötigen Komponenten für den
	 * jeweiligen Spielmodus
	 * 
	 * @param grafik
	 *            der Minimap
	 */
	void displayMinimap(PGraphics grafik);

	/**
	 * reagiert darauf, wenn das Spiel unterbrochen wird
	 */
	void setPauseknopfGedrueckt();
}
