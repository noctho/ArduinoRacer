package net.tfobz.arduinoracer.komponenten;

import java.io.Serializable;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PImage;

/**
 * zu den abstrakten Eigenschaften eines normalen Autos, besitzt die Klasse
 * MultiplayerAuto noch die Möglichkeit des Anzeigens eines Pfeiles in der
 * Minimap hinzukommen
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class MultiplayerAuto extends Auto implements Serializable {
	/**
	 * Pfeil, den das Auto auf der Minimap anzeigt
	 */
	private PImage pfeilele;

	/**
	 * Defaultkonstruktor
	 */
	public MultiplayerAuto() {

	}

	/**
	 * Konstruktor - setzt die Farbe des Autos
	 * 
	 * @param farbe
	 *            die das Auto annehmen soll
	 */
	public MultiplayerAuto(int farbe) {
		this.farbe = farbe;
	}

	/**
	 * liefert den Pfeil für die Minimap
	 * 
	 * @return der Pfeil für die Minimap
	 */
	public PImage getPfeilele() {
		return pfeilele;
	}

	/**
	 * initialisiert das Auto leicht transparent (für ein Multiplayerauto
	 * notwendig)
	 */
	@Override
	public void init(Spieloberflaeche oberflaeche) {
		try {
			super.init(oberflaeche);
			karosserie.setFill(oberflaeche.color(Farben.AUTO_FARBEN[farbe][0],
					100));
			reifenScheiben.setFill(oberflaeche.color(0xff000000, 100));
			pfeilele = oberflaeche.loadImage("pfeilele.png");
			for (int i = 0; i < pfeilele.width; i++)
				for (int j = 0; j < pfeilele.height; j++)
					if (pfeilele.get(i, j) == 0xFFFFD800) {
						pfeilele.set(i, j, oberflaeche.color(
								Farben.AUTO_FARBEN[farbe][0], 100));
					}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void display(Spieloberflaeche oberflaeche) {
		oberflaeche.anzeige3D.translate(oberflaeche.getAuto().getX() - x,
				oberflaeche.getAuto().getY() - y, 0);
		oberflaeche.anzeige3D.rotate(-getKurvenwinkel());
		super.display(oberflaeche);
		oberflaeche.anzeige3D.rotate(getKurvenwinkel());
		oberflaeche.anzeige3D.translate(x - oberflaeche.getAuto().getX(), y
				- oberflaeche.getAuto().getY(), 0);
	}

	/**
	 * setzt die Farbe des Autos
	 * 
	 * @param farbe
	 *            die das Auto annehmen soll
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void setFarbe(int farbe, Spieloberflaeche oberflaeche) {
		this.farbe = farbe;
		init(oberflaeche);
	}
}