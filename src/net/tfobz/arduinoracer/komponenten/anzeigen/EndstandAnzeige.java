package net.tfobz.arduinoracer.komponenten.anzeigen;

import java.util.Collections;
import java.util.Vector;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.steuerung.Arduino;
import net.tfobz.arduinoracer.steuerung.Demonstrationssteuerung;
import net.tfobz.arduinoracer.steuerung.Tastatur;
import net.tfobz.arduinoracer.util.Farben;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Die Klasse EndstandAnzeige zeigt 3 Sekunden nachdem das letzte Auto in einem
 * Multiplayerspiel die Ziellinie passiert hat den Endstand des Rennens an.
 * Dabei wird die Zeit des Siegers angezeigt sowie Abstände der weiteren
 * platzierten Fahrern zum Sieger
 * 
 * @author Thomas Nocker
 *
 */
public class EndstandAnzeige {

	/**
	 * für die Umrandung der Anzeige
	 */
	private PImage obenLinks, untenLinks, obenRechts, untenRechts;
	/**
	 * Rennen, von dem die Endstansanzeige angezeigt werden soll
	 */
	private MultiplayerSpiel spiel = null;
	/**
	 * ob die Anzeige bereits angezeigt wird
	 */
	private boolean angezeigt = false;

	/**
	 * Konstruktor - ladet die Bilder, setzt das Spiel
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @param spiel
	 *            von dem das Ergebnis angezeigt werden soll
	 */
	public EndstandAnzeige(Spieloberflaeche oberflaeche, MultiplayerSpiel spiel) {
		try {
			obenLinks = oberflaeche.loadImage("obenLinks.png");
			obenRechts = oberflaeche.loadImage("obenRechts.png");
			untenLinks = oberflaeche.loadImage("untenLinks.png");
			untenRechts = oberflaeche.loadImage("untenRechts.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.spiel = spiel;
	}

	/**
	 * zeichnet das Ergebnis des Rennens auf der 2D-Anzeige der Spieloberfläche.
	 * Nachdem die Anzeige angezeigt wurde, wird verhindert, dass nochmals die
	 * Spieloberfläche neu gezeichnet wird
	 * 
	 * @param oberflaeche
	 *            auf der das Ergebnis gezeigt werden soll
	 */
	public void display(Spieloberflaeche oberflaeche) {
		try {
			oberflaeche.anzeige2D.translate(oberflaeche.width / 2,
					oberflaeche.height - obenLinks.height);
			oberflaeche.anzeige2D.noStroke();
			oberflaeche.anzeige2D.imageMode(PApplet.CORNER);
			oberflaeche.anzeige2D.rectMode(PApplet.CORNER);
			String text = null;
			if (oberflaeche.getSteuerung() instanceof Arduino)
				text = "In den Joystick drücken, um zu beenden";
			if (oberflaeche.getSteuerung() instanceof Tastatur
					|| oberflaeche.getSteuerung() instanceof Demonstrationssteuerung)
				text = "ESC drücken, um zu beenden";
			oberflaeche.anzeige2D.textAlign(PApplet.CENTER);
			oberflaeche.anzeige2D.textSize(18);
			oberflaeche.anzeige2D.image(obenLinks,
					-oberflaeche.anzeige2D.textWidth(text) / 2
							- obenLinks.width, 0);
			oberflaeche.anzeige2D.image(obenRechts,
					+oberflaeche.anzeige2D.textWidth(text) / 2, 0);
			oberflaeche.anzeige2D.fill(Farben.ANZEIGE_RAND);
			oberflaeche.anzeige2D.rect(
					-oberflaeche.anzeige2D.textWidth(text) / 2, 0,
					oberflaeche.anzeige2D.textWidth(text), obenRechts.height);
			oberflaeche.noLoop();
			oberflaeche.anzeige2D.fill(0xffffffff);
			oberflaeche.anzeige2D.text(text, 0, 19);
			oberflaeche.anzeige2D.translate(-oberflaeche.width / 2,
					-oberflaeche.height + obenLinks.height);
			oberflaeche.anzeige2D.translate(
					(oberflaeche.width - getX(oberflaeche)) / 2,
					(oberflaeche.height - getY()) / 2);
			oberflaeche.anzeige2D.image(obenLinks, 0, 0);
			oberflaeche.anzeige2D.image(untenLinks, 0, getY()
					- untenLinks.height);
			oberflaeche.anzeige2D.image(obenRechts, getX(oberflaeche)
					- obenRechts.width, 0);
			oberflaeche.anzeige2D.image(untenRechts, getX(oberflaeche)
					- untenRechts.height, getY() - untenRechts.height);
			oberflaeche.anzeige2D.fill(Farben.ANZEIGE_RAND);
			oberflaeche.anzeige2D.rect(obenLinks.width, 0, getX(oberflaeche)
					- 2 * obenLinks.width, obenLinks.height);
			oberflaeche.anzeige2D.rect(obenLinks.width, getY() - 6,
					getX(oberflaeche) - 2 * obenLinks.width, 6);
			oberflaeche.anzeige2D.rect(0, obenLinks.height, 6, getY() - 2
					* obenLinks.height);
			oberflaeche.anzeige2D.rect(getX(oberflaeche) - 6, obenLinks.height,
					6, getY() - 2 * obenLinks.height);
			oberflaeche.anzeige2D.rect(6, obenLinks.height,
					getX(oberflaeche) - 12, 44 - obenLinks.height);
			oberflaeche.anzeige2D.fill(Farben.ANZEIGE_KOERPER);
			oberflaeche.anzeige2D.rect(6, 44, getX(oberflaeche) - 12, getY()
					- 44 - untenLinks.height);
			oberflaeche.anzeige2D.rect(untenLinks.width, getY()
					- untenLinks.height, getX(oberflaeche) - 2
					* untenLinks.width, untenLinks.height - 6);
			Vector<MultiplayerSpieler> spieler = spiel.getAlleSpieler();
			Collections.sort(spieler);
			oberflaeche.anzeige2D.fill(0xFFFFFFFF);
			oberflaeche.anzeige2D.textSize(38);
			oberflaeche.anzeige2D.textAlign(PApplet.LEFT);
			oberflaeche.anzeige2D.text(
					"Endstand nach "
							+ (spiel.getAnzahlRunden() == 1 ? "einer Runde"
									: +spiel.getAnzahlRunden() + " Runden"),
					30, 37);
			String[] zeiten = getZeiten(oberflaeche, spieler);
			for (int i = 0; i < getAnzahl(); i++) {
				int x = 0;
				int y = 44 + i * 40;
				setPosition(oberflaeche, x, y, i + 1);
				setName(oberflaeche, x, y, spieler.get(i).getName());
				setZeit(oberflaeche, x, y, zeiten[i]);
			}
			oberflaeche.anzeige2D.translate(oberflaeche.width,
					oberflaeche.height);
		} catch (Exception e) {
			e.printStackTrace();
		}
		angezeigt = true;
	}

	/**
	 * liefert die Breite des Anzeige im Verhältnis zur Spieloberfläche
	 * 
	 * @param oberflaeche
	 *            in dessen Verhältnis die Breite geliefert wird
	 * @return die Breite des Anzeige im Verhältnis zur Spieloberfläche
	 */
	public int getX(Spieloberflaeche oberflaeche) {
		return oberflaeche.width - 100;
	}

	/**
	 * liefert die Höhe der Anzeige
	 * 
	 * @return die Höhe der Anzeige
	 */
	public int getY() {
		return 50 + getAnzahl() * 40;
	}

	/**
	 * liefert die Anzahl der Spieler
	 * 
	 * @return die Anzahl der Spieler
	 */
	public int getAnzahl() {
		return spiel.getAnzahlSpieler();
	}

	/**
	 * liefert die Zeiten, die für die Spieler angezeigt werden sollen
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @param spieler
	 *            dessen Zeiten angezeigt werden sollen
	 * @return die Zeiten, die für die Spieler angezeigt werden sollen
	 */
	public String[] getZeiten(Spieloberflaeche oberflaeche,
			Vector<MultiplayerSpieler> spieler) {
		String[] ret = new String[spieler.size()];
		if (spieler.get(0).getRundenserie().isDisqualisfiziert())
			ret[0] = "DNQ";
		else
			ret[0] = spieler.get(0).getRundenserie()
					.getAktuelleZeit(oberflaeche);
		for (int i = 1; i < ret.length; i++) {
			if (spieler.get(i).getRundenserie().isDisqualisfiziert())
				ret[i] = "DNQ";
			else
				ret[i] = "+"
						+ spieler
								.get(i)
								.getRundenserie()
								.getEnddiferenz(
										spieler.get(0).getRundenserie(),
										oberflaeche);
		}
		return ret;
	}

	/**
	 * setzt die übergebene Position eines Spielers an die übergebene Koordinate
	 * 
	 * @param oberflaeche
	 *            an der die Position ausgegeben wird
	 * @param x
	 *            x-Koordinate, an der die Position angezeigt wird
	 * @param y
	 *            y-Koordinate, an der die Position angezeigt wird
	 * @param position
	 *            die angezeigt wird
	 */
	public void setPosition(Spieloberflaeche oberflaeche, int x, int y,
			int position) {
		oberflaeche.anzeige2D.textAlign(PApplet.LEFT);
		oberflaeche.anzeige2D.textSize(32);
		oberflaeche.anzeige2D.text("" + position, x + 12, y + 32);
	}

	/**
	 * setzt den übergebenen Namen eines Spielers an die übergebene Koordinate
	 * 
	 * @param oberflaeche
	 *            an der der Name ausgegeben wird
	 * @param x
	 *            x-Koordinate, an der der Name angezeigt wird
	 * @param y
	 *            y-Koordinate, an der der Name angezeigt wird
	 * @param name
	 *            der angezeigt wird
	 */
	public void setName(Spieloberflaeche oberflaeche, int x, int y, String name) {
		oberflaeche.anzeige2D.textSize(24);
		if (oberflaeche.textWidth(name) > getX(oberflaeche) - 240) {
			do {
				name = name.substring(0, name.length() - 1);
			} while (oberflaeche.textWidth(name + "...") > getX(oberflaeche) - 240);
			name += "...";
		}
		oberflaeche.anzeige2D.text(name, x + 45, y + 27);
	}

	/**
	 * setzt die übergebene Zeit eines Spielers an die übergebene Koordinate
	 * 
	 * @param oberflaeche
	 *            an der die Zeit ausgegeben wird
	 * @param x
	 *            x-Koordinate, an der die Zeit angezeigt wird
	 * @param y
	 *            y-Koordinate, an der die Zeit angezeigt wird
	 * @param zeit
	 *            die angezeigt wird
	 */
	public void setZeit(Spieloberflaeche oberflaeche, int x, int y, String zeit) {
		oberflaeche.anzeige2D.textAlign(PApplet.RIGHT);
		oberflaeche.anzeige2D.textSize(32);
		oberflaeche.anzeige2D.text(zeit, getX(oberflaeche) - 8, y + 32);
	}

	/**
	 * liefert zurück, ob die Anzeige bereits angezeigt wird
	 * 
	 * @return true, wenn die Anzeige bereits angezeigt wird, ansonsten false
	 */
	public boolean isAngezeigt() {
		return angezeigt;
	}
}