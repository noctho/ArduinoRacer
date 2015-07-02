package net.tfobz.arduinoracer.komponenten;

import java.util.Iterator;
import java.util.Vector;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PApplet;
import processing.core.PVector;

/**
 * Die Klasse Reifen zeichnet weniger die Reifen des Autos, als viel mehr die
 * Partikel, die beim Fahren im Schlamm aufgewühlt werden. Dazu werden die
 * Koordinaten des Reifen benötigt
 * 
 * @author Thomas Nocker
 *
 */
public class Reifen {

	/**
	 * x-Koordinate des Reifen von der Mitte des Autos aus
	 */
	private float x = 0;
	/**
	 * y-Koordinate des Reifen von der Mitte des Autos aus
	 */
	private float y = 0;
	/**
	 * Die aktuellen Partikel um den Reifen
	 */
	private Vector<Particle> lettnPartikel;
	/**
	 * die aktuelle x-Koordinate des Autos, dem der Reifen gehört
	 */
	private float currentX = 0;
	/**
	 * die aktuelle y-Koordinate des Autos, dem der Reifen gehört
	 */
	private float currentY = 0;

	/**
	 * Konstruktor
	 * 
	 * @param x
	 *            x-Koordinate des Reifen
	 * @param y
	 *            y-Koordinate des Reifen
	 */
	public Reifen(float x, float y) {
		this.x = x;
		this.y = y;
		lettnPartikel = new Vector<Particle>();
	}

	/**
	 * zeichnet die Partikel in der 3D-Anzeige der Spieloberfläche um die Reifen
	 * des übergebenen Autos
	 * 
	 * @param oberflaeche
	 *            auf der die Partikel gezeichnet werden
	 * @param auto
	 *            von dem die Partikel stammen
	 */
	public void display(Spieloberflaeche oberflaeche, Auto auto) {
		if (oberflaeche.getStrecke().getBodenbegebenheitBei(currentX, currentY)
				.isImSchlamm())
			addParticles(auto);
		float a = Strecke.berechneC(x, y);
		float winkel = PApplet.atan(x / y) - auto.getKurvenwinkel();
		currentX = auto.getXAbsolut() + PApplet.sin(winkel) * a;
		currentY = auto.getYAbsolut() - PApplet.cos(winkel) * a;
		Iterator<Particle> it = lettnPartikel.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			p.display(oberflaeche, (auto.getFarbe() == oberflaeche.getAuto()
					.getFarbe() ? 255 : 200));
			if (p.isDead()) {
				it.remove();
			}
		}
	}

	/**
	 * Die Klasse Partikel stellt ein Partikel dar, das um die Reifen gezeichnet
	 * wird
	 * 
	 * @author Thomas Nocker
	 *
	 */
	private class Particle {
		/**
		 * Die Position des Partikels
		 */
		private PVector location;
		/**
		 * Die Aufwühlungs-Geschwindigkeit des Partikels
		 */
		private PVector aufwuehlgeschwindigkeit;
		/**
		 * Die Aufwühlungs-Geschwindigkeit des Partikels
		 */
		private PVector aufwuehlbeschleunigung;

		/**
		 * Konstruktor
		 * 
		 * @param location
		 *            die Position des Partikels
		 * @param aufwuehlgeschwindigkeit
		 *            die Aufwühlungs-Geschwindigkeit des Partikels
		 * @param aufwuehlbeschleunigung
		 *            die Aufwühlungs-Geschwindigkeit des Partikels
		 */
		private Particle(PVector location, PVector aufwuehlgeschwindigkeit,
				PVector aufwuehlbeschleunigung) {
			this.location = location;
			this.aufwuehlgeschwindigkeit = aufwuehlgeschwindigkeit;
			this.aufwuehlbeschleunigung = aufwuehlbeschleunigung;
			// LifeSpan = 255;
		}

		/**
		 * zeichnet ein Partikel auf der 3D-Anzeige der Spieloberfläche
		 * 
		 * @param oberflaeche
		 *            auf der das Partikel gezeichnet wird
		 * @param alpha
		 *            Alphawert der Partikelfarbe
		 */
		private void display(Spieloberflaeche oberflaeche, int alpha) {
			aufwuehlgeschwindigkeit.add(aufwuehlbeschleunigung);
			location.add(aufwuehlgeschwindigkeit);
			// LifeSpan -= 5;
			oberflaeche.anzeige3D.noStroke();
			oberflaeche.anzeige3D.fill(Farben.LETTN, alpha);
			oberflaeche.anzeige3D.translate(location.x, location.y, location.z);
			oberflaeche.anzeige3D.box(2);
			oberflaeche.anzeige3D.translate(-location.x, -location.y,
					-location.z);
		}

		/**
		 * liefert zurück, ob des Partikel bereits tot ist. Ein Partikel ist
		 * Tot, wenn das Partikel tiefer als der Boden ist
		 * 
		 * @return true, wenn das Partikel tiefer als der Boden ist, ansonsten
		 *         false
		 */
		private boolean isDead() {
			return location.z < 0;
		}
	}

	/**
	 * fügt Partikel dem Partikelsystem des Reifens hinzu
	 * 
	 * @param auto
	 *            um dessen Reifen die Partikel gehören
	 */
	private void addParticles(Auto auto) {
		float a = Strecke.berechneC(x, y);
		float winkel = PApplet.atan(x / y) - auto.getKurvenwinkel();
		float deltaX = auto.getXAbsolut() + PApplet.sin(winkel) * a - currentX;
		float deltaY = auto.getYAbsolut() - PApplet.cos(winkel) * a - currentY;
		a = Math.abs(Strecke.berechneC(deltaX, deltaY));
		PApplet random = new PApplet();
		for (int i = 0; i < a / 2; i++)
			lettnPartikel.add(new Particle(new PVector(currentX + (deltaX * i)
					/ (a / 2), currentY + (deltaY * i) / (a / 2)), new PVector(
					random.random(-0.75F, 0.75F), random.random(-0.75F, 0.75F),
					random.random(-1, 1)), new PVector(0, 0, random.random(
					-0.1F, -0.01F))));
	}

	/**
	 * setzt die Reifen zu einem neuen Punkt
	 * 
	 * @param x
	 *            x-Koordinate des Reifens
	 * @param y
	 *            y-Koordinate des Reifens
	 * @param kurvenwinkel
	 *            des Autos, dem der Reifen gehört
	 */
	public void setTo(float x, float y, float kurvenwinkel) {
		float a = Strecke.berechneC(x, y);
		float winkel = PApplet.atan(x / y) - kurvenwinkel;
		currentX = x + PApplet.sin(winkel) * a;
		currentY = y - PApplet.cos(winkel) * a;
	}
}
