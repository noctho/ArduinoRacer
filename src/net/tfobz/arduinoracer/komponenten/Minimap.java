package net.tfobz.arduinoracer.komponenten;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Die Klasse Minimap ist für die Darstellung der Minimap verantwortlich. Dabei
 * wird die Minimap in einer eigenen Grafik gezeichnet, damit die Minimap
 * abgeschnitten werden kann und nicht immer die ganze Minimap angezeigt wird.
 * Über der Minimap wird eine Geschwindigkeitsanzeige angezeigt
 * 
 * @author Thomas Nocker
 *
 */
public class Minimap {
	/**
	 * Die Bilder, die für die Minimap benötigt werden
	 */
	PImage bild, pfeilele, anzeige;
	/**
	 * Grafik, auf der die Minimap gezeichnet wird
	 */
	private PGraphics minimapAnzeige = null;

	/**
	 * Konstruktor - ladet die Bilder und legt die Anzeige an
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public Minimap(Spieloberflaeche oberflaeche) {
		try {
			bild = oberflaeche.loadImage("minimap.png");
			pfeilele = oberflaeche.loadImage("pfeilele.png");
			anzeige = oberflaeche.loadImage("minimapanzeige.png");
			setFarbe(oberflaeche);
		} catch (Exception e) {
			e.printStackTrace();
		}
		minimapAnzeige = oberflaeche.createGraphics(120, 120);
		minimapAnzeige.imageMode(PApplet.CENTER);
	}

	/**
	 * zeichnet die Minimap und die Geschwindigkeitsanzeige in die 2D-Anzeige
	 * der Spieloberfläche
	 * 
	 * @param oberflaeche
	 *            auf der die Minimap und die Geschwindigkeitsanzeige angezeigt
	 *            wird
	 */
	public void display(Spieloberflaeche oberflaeche) {
		oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE);
		oberflaeche.anzeige2D.imageMode(PApplet.CENTER);
		oberflaeche.anzeige2D.rectMode(PApplet.CENTER);
		oberflaeche.anzeige2D.translate(anzeige.width / 2,
				oberflaeche.getHeight() - anzeige.height / 2);
		oberflaeche.anzeige2D.image(anzeige, 0, 0);
		minimapAnzeige.beginDraw();
		minimapAnzeige.clear();
		minimapAnzeige.translate(60, 60);
		float winkel = oberflaeche.getAuto().getKurvenwinkel();
		minimapAnzeige.rotate(winkel);
		minimapAnzeige.image(bild, oberflaeche.getAuto().getX() / 30,
				oberflaeche.getAuto().getY() / 30);
		oberflaeche.getSpielmodus().displayMinimap(minimapAnzeige);
		minimapAnzeige.rotate(-winkel);
		minimapAnzeige.image(pfeilele, 0, 0);
		minimapAnzeige.translate(-60, -60);
		minimapAnzeige.endDraw();
		oberflaeche.anzeige2D.image(minimapAnzeige, 0, 10);
		oberflaeche.anzeige2D.textSize(18);
		oberflaeche.anzeige2D.textAlign(PApplet.RIGHT);
		oberflaeche.anzeige2D.text(oberflaeche.getAuto()
				.getGeschwindigkeitKMpH(), 40, -58);
		oberflaeche.anzeige2D.translate(-anzeige.width / 2,
				-oberflaeche.getHeight() + anzeige.height / 2);
	}

	/**
	 * ladet alle Komponenten der Minimap, die von der Farbe des Autos abhängig
	 * sind
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @param farbe
	 *            die der Farbe des Autos entspricht
	 */
	private void setFarbe(Spieloberflaeche oberflaeche) {
		try {
			pfeilele = oberflaeche.loadImage("pfeilele.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < pfeilele.width; i++)
			for (int j = 0; j < pfeilele.height; j++)
				if (pfeilele.get(i, j) == 0xFFFFD800) {
					pfeilele.set(i, j, oberflaeche.color(
							Farben.AUTO_FARBEN[oberflaeche.getAuto().getFarbe()][0], 255));
				}
	}
}
