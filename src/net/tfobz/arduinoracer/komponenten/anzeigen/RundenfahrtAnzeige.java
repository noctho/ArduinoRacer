package net.tfobz.arduinoracer.komponenten.anzeigen;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.runden.Runde;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Die Klasse RundenfahrtAnzeige zeigt in einer aktiven Runde des Fahrers dessen
 * aktuelle Rundenzeit an. An drei Stellen werden Zwischenzeiten erhoben
 * 
 * @author Thomas Nocker
 *
 */
public class RundenfahrtAnzeige {

	/**
	 * Bilder, die für die Anzeige benötigt werden
	 */
	private PImage koepf, koerper;
	/**
	 * der Name des aktiven Fahrers
	 */
	private String spielername = null;
	/**
	 * die aktuelle Rundenzeit, die angezeigt werden soll
	 */
	private String zeit = null;
	/**
	 * die aktuelle Zwischenzeit, die angezeigt werden soll
	 */
	private String haeder = null;
	/**
	 * zeigt an, bis wann die Rundenzeit nicht geändert werden soll
	 */
	private long keineAenderung = 0;

	/**
	 * Konstruktor - ladet die Bilder, die für die Anzeige benötigt werden
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public RundenfahrtAnzeige(Spieloberflaeche oberflaeche) {
		try {
			koepf = oberflaeche.loadImage("zwischenzeitzusatz.png");
			koerper = oberflaeche.loadImage("zeitanzeige.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		setName(oberflaeche.getSpielerName(), oberflaeche);

	}

	/**
	 * zeichnet die Anzeige in der 2D-Anzeige der Spieloberfläche. Falls eine
	 * Zwischenzeit vorhanden wird, wird diese in der richtigen Farbe
	 * gezeichnet, ansonsten fällt die gesamte Anzeige für die Zwischenzeit weg.
	 * 
	 * @param oberflaeche
	 *            in der die Anzeige gezeichnet wird
	 */
	public void display(Spieloberflaeche oberflaeche) {
		oberflaeche.anzeige2D.translate(oberflaeche.width - getX(),
				oberflaeche.height - getY());
		if (haeder != null) {
			if (haeder.charAt(0) == '-')
				oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE_GRUEN);
			else if (haeder.charAt(0) == '+')
				oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE_ROT);
			else
				oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE);
			oberflaeche.anzeige2D.image(koepf, getX() - koepf.width, 0);
			oberflaeche.anzeige2D.textSize(14);
			oberflaeche.anzeige2D.textAlign(PApplet.CENTER);
			oberflaeche.anzeige2D.text(haeder, getX() - koepf.width / 2, 17);
		}
		if (zeit != null) {
			oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE);
			oberflaeche.anzeige2D.image(koerper, 0, koepf.height);
			oberflaeche.anzeige2D.textSize(24);
			oberflaeche.anzeige2D.textAlign(PApplet.CENTER);
			oberflaeche.anzeige2D.text(zeit, 167, koepf.height + 28);
			oberflaeche.anzeige2D.textSize(12);
			oberflaeche.anzeige2D.textAlign(PApplet.LEFT);
			oberflaeche.anzeige2D.text(spielername, 15, koepf.height + 25);
		}
		oberflaeche.anzeige2D.translate(-oberflaeche.width + getX(),
				-oberflaeche.height + getY());
	}

	/**
	 * liefert die Breite der Anzeige
	 * 
	 * @return die Breite der Anzeige
	 */
	public int getX() {
		return koerper.width;
	}

	/**
	 * liefert die Höhe der Anzeige
	 * 
	 * @return die Höhe der Anzeige
	 */
	public int getY() {
		return koerper.height + koepf.height;
	}

	/**
	 * setzt den Name des Fahrers, der so groß gewählt werden muss, dass er
	 * immer platz hat, ansonsten wird er abgeschnitten
	 * 
	 * @param name
	 *            den der Fahrer besitzt
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void setName(String name, Spieloberflaeche oberflaeche) {
		oberflaeche.textSize(12);
		if (oberflaeche.textWidth(name) > 100) {
			do {
				name = name.substring(0, name.length() - 1);
			} while (oberflaeche.textWidth(name + "...") > 100);
			name += "...";
		}
		this.spielername = name;
	}

	/**
	 * setzt die aktuelle Rundenzeit, die dem Fahrer angezeigt werden soll
	 * 
	 * @param zeit
	 *            die dem Fahrer angezeigt werden soll
	 */
	public void setZeit(String zeit) {
		if (keineAenderung != 0)
			if (System.currentTimeMillis() > keineAenderung) {
				haeder = null;
				keineAenderung = 0;
			}
		if (keineAenderung == 0) {
			this.zeit = zeit;
			if (zeit == null && haeder != null)
				haeder = null;
		}
	}

	/**
	 * setzt die Zwischenzeit, die dem Fahrer zusätzlich angezeigt werden soll
	 * 
	 * @param zeit
	 *            die dem Fahrer zusätzlich angezeigt werden soll
	 */
	public void setZusatzzeit(String zeit) {
		haeder = zeit;
	}

	/**
	 * setzt die Zwischenzeit, die dem Fahrer zusätzlich angezeigt werden soll.
	 * Dieser Zeit wird noch ein Plus oder ein Minus angehängt, je nachdem, ob
	 * der Fahrer langsamer oder schneller ist
	 * 
	 * @param zeit
	 *            die dem Fahrer zusätzlich angezeigt werden soll
	 */
	public void setZwischenzeit(long zeit) {
		if (zeit > 0)
			haeder = "+";
		else
			haeder = "-";
		zeit = Math.abs(zeit);
		if (zeit > 60000)
			haeder += Runde.zeitToString(zeit);
		else
			haeder += Runde.zeitToStringOhneMinuten(zeit);
		keineAenderung = System.currentTimeMillis() + 5000;

	}

}
