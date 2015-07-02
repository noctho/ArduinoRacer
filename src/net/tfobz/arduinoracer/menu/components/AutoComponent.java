package net.tfobz.arduinoracer.menu.components;

import java.awt.Color;
import java.io.File;

import net.tfobz.arduinoracer.util.Farben;

/**
 * Die Klasse AutoComponent ist ein BildKnopKomponent, dessen Bild immer ein
 * Knopf ist, jedoch in unterschiedlichen Farben. Man kann diesen Knopf als
 * Auto-Auswahl-Knopf benutzen
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class AutoComponent extends BildKnopfComponent {

	/**
	 * farbe des Autos im Knopf
	 */
	private int farbe;

	/**
	 * Defaultkonstruktor
	 */
	public AutoComponent() {
		this(1);
	}

	/**
	 * Konstruktor - setzt die übergebene Farbe des Autos
	 * 
	 * @param farbe
	 *            die das Auto im Knopf annehmen soll
	 */
	public AutoComponent(int farbe) {
		super(new File("auto.png"));
		setBackground(Color.BLUE);
		this.farbe = farbe;
		for (int i = 0; i < bild.getWidth(); i++)
			for (int j = 0; j < bild.getHeight(); j++)
				if (bild.getRGB(i, j) == 0xFFFFFFFF) {
					bild.setRGB(i, j, Farben.AUTO_FARBEN[farbe][0]);
				} else if (bild.getRGB(i, j) == 0xFF808080) {
					bild.setRGB(i, j, Farben.AUTO_FARBEN[farbe][1] | 0xFF000000);
				}
		repaint();
	}

	/**
	 * liefert die Farbe des Autos zurück
	 * 
	 * @return die Farbe des Autos
	 */
	public int getFarbe() {
		return farbe;
	}

	/**
	 * setzt die Farbe des Autos im Knopf
	 * 
	 * @param farbe
	 *            die das Auto annehmen soll
	 */
	public void setFarbe(int farbe) {
		for (int i = 0; i < bild.getWidth(); i++)
			for (int j = 0; j < bild.getHeight(); j++)
				if (bild.getRGB(i, j) == Farben.AUTO_FARBEN[this.farbe][0]) {
					bild.setRGB(i, j, Farben.AUTO_FARBEN[farbe][0]);
				} else if (bild.getRGB(i, j) == Farben.AUTO_FARBEN[this.farbe][1]) {
					bild.setRGB(i, j, Farben.AUTO_FARBEN[farbe][1] | 0xFF000000);
				}
		this.farbe = farbe;

	}
}
