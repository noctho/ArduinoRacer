package net.tfobz.arduinoracer.komponenten.anzeigen;

import java.util.Collections;
import java.util.Vector;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.runden.Rundenserie;
import net.tfobz.arduinoracer.util.Farben;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Die Klasse MultiplayerAnzeige zeigt in einem die aktuellen Abstände der
 * gegnerischen Fahrer zur Zeit des aktiven Fahrers an.
 * 
 * @author Thomas Nocker
 *
 */
public class MultiplayerAnzeige {

	/**
	 * Bilder, die für die Azneige benötigt werden
	 */
	private PImage ueberkoepf, kopf, koerper, fuss, zielflagge;
	/**
	 * Spieler, deren Abstände angezeigt werden
	 */
	private Vector<MultiplayerSpieler> spieler = null;
	/**
	 * Spieler, von dem die Abstände aus gemessen werden
	 */
	private MultiplayerSpieler mainSpieler = null;
	/**
	 * Anzeige, die angezeigt wird, sobald alle Spieler die Ziellinie passiert
	 * haben
	 */
	private EndstandAnzeige endstandAnzeige = null;
	/**
	 * sagt aus, ob alle Spieler die Ziellinie passiert haben
	 */
	private boolean fertig = false;

	/**
	 * /** Konstruktor - ladet die Bilder, setzt das Spiel
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @param spiel
	 *            von dem das Ergebnis angezeigt werden soll
	 * @param mainSpieler
	 *            Spieler, von dem aus die Abstände gemessen werden
	 */
	public MultiplayerAnzeige(Spieloberflaeche oberflaeche,
			MultiplayerSpiel spiel, MultiplayerSpieler mainSpieler) {
		try {
			endstandAnzeige = new EndstandAnzeige(oberflaeche, spiel);
			ueberkoepf = oberflaeche.loadImage("zwischenzeitzusatz.png");
			kopf = oberflaeche.loadImage("multiplayerkopf.png");
			koerper = oberflaeche.loadImage("multiplayerkoerper.png");
			fuss = oberflaeche.loadImage("multiplayerfuss.png");
			zielflagge = oberflaeche.loadImage("zielflagge.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.spieler = spiel.getAlleSpieler();
		this.mainSpieler = mainSpieler;
	}

	/**
	 * zeichnet die Anzeige in der 2D-Anzeige der Spieloberfläche
	 * 
	 * @param oberflaeche
	 *            in der die Anzeige gezeichnet wird
	 */
	public void display(Spieloberflaeche oberflaeche) {
		if (fertig)
			endstandAnzeige.display(oberflaeche);
		else {
			oberflaeche.anzeige2D.translate(oberflaeche.width - getX(),
					oberflaeche.height - getY());
			oberflaeche.anzeige2D.imageMode(PApplet.CORNER);
			Collections.sort(spieler);
			oberflaeche.anzeige2D.image(ueberkoepf, getX() - ueberkoepf.width,
					0);
			oberflaeche.anzeige2D.image(kopf, 0, ueberkoepf.height);
			oberflaeche.anzeige2D.fill(Farben.ANZEIGETEXTFARBE);
			oberflaeche.anzeige2D.textAlign(PApplet.RIGHT);
			oberflaeche.anzeige2D.textSize(12);
			oberflaeche.anzeige2D.text("Runde "
					+ mainSpieler.getRundenserie().getRundenVon(), getX() - 6,
					15);
			String[] zeiten = getZeiten(oberflaeche);
			setPosition(oberflaeche, 0, ueberkoepf.height + 6, 1);
			setName(oberflaeche, 0, ueberkoepf.height + 6, spieler.get(0)
					.getName());
			setZeit(oberflaeche, 0, ueberkoepf.height + 6, zeiten[0]);
			for (int i = 0; i < getAnzahl() - 2; i++) {
				int x = 0;
				int y = ueberkoepf.height + kopf.height + i * koerper.height;
				oberflaeche.anzeige2D.image(koerper, x, y);
				setPosition(oberflaeche, x, y, 2 + i);
				setName(oberflaeche, x, y, spieler.get(1 + i).getName());
				setZeit(oberflaeche, x, y, zeiten[1 + i]);
			}
			if (getAnzahl() > 1) {
				oberflaeche.anzeige2D.image(fuss, 0, getY() - fuss.height);
				setPosition(oberflaeche, 0, getY() - fuss.height, getAnzahl());
				setName(oberflaeche, 0, getY() - fuss.height,
						spieler.get(getAnzahl() - 1).getName());
				setZeit(oberflaeche, 0, getY() - fuss.height,
						zeiten[getAnzahl() - 1]);
			}
			oberflaeche.anzeige2D.translate(-oberflaeche.width + getX(),
					-oberflaeche.height + getY());
			if (mainSpieler.getRundenserie().isFinished()) {
				oberflaeche.anzeige2D.imageMode(PApplet.CENTER);
				oberflaeche.anzeige2D.image(zielflagge, oberflaeche.width / 2,
						oberflaeche.height / 2);
			}
			if (!fertig && getAlleSpielerFerig())
				new Thread(new Runnable() {
					@Override
					public void run() {
						synchronized (this) {
							try {
								this.wait(5000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							fertig = true;
						}
					}
				}).start();
		}
	}

	/**
	 * kontrolliert, ob alle Spieler die Ziellinie passiert haben oder
	 * disqualifiziert wurden
	 * 
	 * @return true, wenn alle Spieler die Ziellinie passiert haben oder
	 *         disqualifiziert wurden, ansonsten false
	 */
	private boolean getAlleSpielerFerig() {
		boolean ret = true;
		for (MultiplayerSpieler spieler : spieler)
			ret &= spieler.getRundenserie().isFinished();
		return ret;
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
		return ueberkoepf.height + kopf.height + fuss.height
				+ (getAnzahl() - 2) * koerper.height;
	}

	/**
	 * liefert die Anzahl der Spieler
	 * 
	 * @return die Anzahl der Spieler
	 */
	public int getAnzahl() {
		return spieler.size();
	}

	/**
	 * liefert die Zeiten, die für die Spieler angezeigt werden sollen. Diese
	 * werden im Verhältnis zum Hauptspieler in Echtzeit errechnet
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @return die Zeiten, die für die Spieler angezeigt werden sollen
	 */
	public String[] getZeiten(Spieloberflaeche oberflaeche) {
		String[] ret = new String[spieler.size()];
		if (spieler.get(0).getRundenserie().isDisqualisfiziert())
			for (int i = 0; i < ret.length; i++)
				ret[i] = "DNQ";
		else {
			MultiplayerSpieler mainSpieler = this.mainSpieler;
			if (mainSpieler.getRundenserie().isDisqualisfiziert())
				mainSpieler = spieler.get(0);
			Rundenserie gesamtserie = new Rundenserie(mainSpieler
					.getRundenserie().getAnzahlRunden());
			gesamtserie.setStart(0);
			for (int i = spieler.size() - 1, j = 0; j < spieler.get(0)
					.getRundenserie().getAktuellBefahrenenSektor(); j++) {
				while (spieler.get(i).getRundenserie().isDisqualisfiziert()
						|| spieler.get(i).getRundenserie()
								.getAbsoluteSektrozeit(j) == 0)
					i--;
				gesamtserie.setAbsolutenSektor(j, spieler.get(i)
						.getRundenserie().getAbsoluteSektrozeit(j));
			}
			if (!spieler.get(0).getRundenserie().isFinished())
				gesamtserie.setNewSektor(oberflaeche.currentMillis
						- spieler.get(0).getRundenserie().getAktuelleRunde()
								.getSektorstart());
			Rundenserie referenzSerie = new Rundenserie(mainSpieler
					.getRundenserie().getAnzahlRunden());
			referenzSerie.setStart(0);
			if (gesamtserie.getAktuellBefahrenenSektor() == 0)
				referenzSerie.setNewSektor(oberflaeche.currentMillis
						- mainSpieler.getRundenserie().getAktuelleRunde()
								.getSektorstart());
			else
				for (int i = 0; i < gesamtserie.getAktuellBefahrenenSektor(); i++)
					if (i < mainSpieler.getRundenserie()
							.getAktuellBefahrenenSektor())
						referenzSerie.setAbsolutenSektor(i, mainSpieler
								.getRundenserie().getAbsoluteSektrozeit(i));
					else if (i == mainSpieler.getRundenserie()
							.getAktuellBefahrenenSektor()
							&& oberflaeche.currentMillis
									- mainSpieler.getRundenserie()
											.getAktuelleRunde()
											.getSektorstart() > gesamtserie
										.getAbsoluteSektrozeit(i))
						referenzSerie.setNewSektor(oberflaeche.currentMillis
								- mainSpieler.getRundenserie()
										.getAktuelleRunde().getSektorstart());
					else
						referenzSerie.setAbsolutenSektor(i,
								gesamtserie.getAbsoluteSektrozeit(i));
			boolean schneller = true;
			for (int i = 0; i < ret.length; i++) {
				if (spieler.get(i) == mainSpieler) {
					ret[i] = mainSpieler.getRundenserie().getAktuelleZeit(
							oberflaeche);
					schneller = false;
					// System.out.println(mainSpieler.getRundenserie().getSektor());
				} else if (spieler.get(i).getRundenserie().isDisqualisfiziert())
					ret[i] = "DNQ";
				else
					ret[i] = spieler
							.get(i)
							.getRundenserie()
							.berechneDifferenz(referenzSerie, gesamtserie,
									oberflaeche.currentMillis, schneller);
			}
		}
		return ret;
	}

	/**
	 * setzt die übergebene Position eines Spielers an die übergebene Koordinate
	 * 
	 * @param oberflaeche
	 *            an der die Position ausgegeben wird
	 * @param x
	 *            Koordinate, an der die Position angezeigt wird
	 * @param y
	 *            y-Koordinate, an der die Position angezeigt wird
	 * @param position
	 *            die angezeigt wird
	 */
	public void setPosition(Spieloberflaeche oberflaeche, int x, int y,
			int position) {
		oberflaeche.anzeige2D.textAlign(PApplet.RIGHT);
		oberflaeche.anzeige2D.textSize(24);
		oberflaeche.anzeige2D.text("" + position, x + 24, y + 22);
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
		oberflaeche.anzeige2D.textAlign(PApplet.LEFT);
		oberflaeche.anzeige2D.textSize(12);
		if (oberflaeche.textWidth(name) > 110) {
			do {
				name = name.substring(0, name.length() - 1);
			} while (oberflaeche.textWidth(name + "...") > 110);
			name += "...";
		}
		oberflaeche.anzeige2D.text(name, x + 36, y + 17);
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
		oberflaeche.anzeige2D.textSize(20);
		oberflaeche.anzeige2D.text(zeit, getX() - 8, y + 22);
	}

	/**
	 * liefert die Endstandsanzeige
	 * 
	 * @return die Endstandsanzeige
	 */
	public EndstandAnzeige getEndstandAnzeige() {
		return endstandAnzeige;
	}
}