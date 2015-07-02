package net.tfobz.arduinoracer.runden;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

import processing.core.PApplet;

/**
 * Die Klasse Runde ist eine abgespeckte Form der Rundenzeit, bei der nur die
 * drei großen Sektorzeiten der Runde abgespeichert werden. Zudem wird noch der
 * Zeitpunkt, an dem die Runde absolviert wurde sowie der Name des Fahrers
 * abgespechert. Diese Klasse ist jedoch nicht zum Erfassen von Rundenzeiten,
 * sondern nur für das Austauschen der Runden mit dem Webservice geeignet.
 * 
 * @author Thomas Nocker
 *
 */
@XmlRootElement
public class Runde {

	/**
	 * Die Zeiten der drei großen Sektoren der Runde
	 */
	private long[] zwischenzeiten = new long[3];
	/**
	 * Der Name des Fahreres der Runde
	 */
	private String name = null;
	/**
	 * Der Zeitpunkt, an dem die Runde erfasst wurde in Millisekunden
	 */
	private long zeitpunkt = 0;

	/**
	 * Positionen des Autos während der Runde
	 */
	private ArrayList<Position> positionen = new ArrayList<Position>();

	/**
	 * Defaultkonstruktor
	 */
	public Runde() {

	}

	/**
	 * Konstruktor - legt eine Runde mit den übergebenen Parametern an
	 * 
	 * @param name
	 *            der Fahrers der Runde
	 * @param sektor1
	 *            1. große Sektor der Runde
	 * @param sektor2
	 *            2. große Sektor der Runde
	 * @param endzeit
	 *            Endzeit der Runde
	 * @param positionen
	 *            des Autos während der Runde
	 */
	public Runde(String name, long sektor1, long sektor2, long endzeit,
			ArrayList<Position> positionen) {
		zwischenzeiten[0] = sektor1;
		zwischenzeiten[1] = sektor2;
		zwischenzeiten[2] = endzeit;
		this.name = name;
		this.positionen = positionen;
	}

	/**
	 * Konstruktor - legt eine Runde mit den übergebenen Parametern an
	 * 
	 * @param name
	 *            der Fahrers der Runde
	 * @param sektor1
	 *            1. große Sektor der Runde
	 * @param sektor2
	 *            2. große Sektor der Runde
	 * @param endzeit
	 *            Endzeit der Runde
	 * @param zeitpunkt
	 *            Zeitpunkt, an dem die Runde erfasst wurde
	 */
	public Runde(String name, long sektor1, long sektor2, long endzeit,
			long zeitpunkt) {
		zwischenzeiten[0] = sektor1;
		zwischenzeiten[1] = sektor2;
		zwischenzeiten[2] = endzeit;
		this.name = name;
		this.zeitpunkt = zeitpunkt;
	}

	/**
	 * liefert die drei Zeiten der Sektoren in Millisekunden
	 * 
	 * @return die drei Zeiten der Sektoren in Millisekunden
	 */
	public long[] getZwischenzeiten() {
		return zwischenzeiten;
	}

	/**
	 * setzt die drei Zeiten der Sektoren
	 * 
	 * @param zwischenzeiten
	 *            die drei Zeiten, die die Sektoren annehemen sollen
	 */
	public void setZwischenzeiten(long[] zwischenzeiten) {
		this.zwischenzeiten = zwischenzeiten;
	}

	/**
	 * liefert die Endzeit der Runde in Millisekunden
	 * 
	 * @return die Endzeit der Runde in Millisekunden
	 */
	public long getEndzeit() {
		return zwischenzeiten[zwischenzeiten.length - 1];
	}

	/**
	 * liefert die Zeit des übergebenen großen Sektors in Millisekunden
	 * 
	 * @param sektor
	 *            dessn Zeit abgefragt wird
	 * @return die Zeit des übergebenen großen Sektors in Millisekunden
	 */
	public long getSektor(int sektor) {
		return zwischenzeiten[sektor];
	}

	/**
	 * liefert die Zeit des übergebenen großen Sektors als String
	 * 
	 * @param sektor
	 *            dessn Zeit abgefragt wird
	 * @return die Zeit des übergebenen großen Sektors alse String
	 */
	public String getSektorString(int sektor) {
		return zeitToString(zwischenzeiten[sektor]);
	}

	/**
	 * konvertiert die übergebene Zeit in Millisekunden als String der Form
	 * "m:ss,SSS"
	 * 
	 * @param zeit
	 *            die in ein String konvertiert werden soll
	 * @return die übergebene Zeit in Millisekunden als String der Form
	 *         "m:ss,SSS"
	 */
	public static String zeitToString(long zeit) {
		return new SimpleDateFormat("m:ss,SSS").format(new Date(zeit));
	}

	/**
	 * konvertiert die übergebene Zeit in Millisekunden als String der Form
	 * "s,SSS"
	 * 
	 * @param zeit
	 *            die in ein String konvertiert werden soll
	 * @return die übergebene Zeit in Millisekunden als String der Form "s,SSS"
	 */
	public static String zeitToStringOhneMinuten(long zeit) {
		return new SimpleDateFormat("s,SSS").format(new Date(zeit));
	}

	/**
	 * liefert den Namen des Fahrers der Runde
	 * 
	 * @return der Namen des Fahrers der Runde
	 */
	public String getName() {
		return name;
	}

	/**
	 * setzt den Namen des Fahrers der Runde
	 * 
	 * @param name
	 *            den der Fahrer der Runde annehmen soll
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * liefert den Zeitpunkt, an dem die Runde gefahren wurde
	 * 
	 * @return der Zeitpunkt, an dem die Runde gefahren wurde
	 */
	public long getZeitpunkt() {
		return zeitpunkt;
	}

	/**
	 * setzt den Zeitpunkt, an dem die Zeit gefahren wurde
	 * 
	 * @param zeitpunkt
	 *            an dem die Runde gefahren wurde
	 */
	public void setZeitpunkt(long zeitpunkt) {
		this.zeitpunkt = zeitpunkt;
	}

	/**
	 * liefert die Positionen des Autos während der Runde
	 * @return die Positionen des Autos während der Runde
	 */
	public ArrayList<Position> getPositionen() {
		return positionen;
	}

	/**
	 * setzt die Positionen des Autos während der Runde
	 * @param positionen die Positionen des Autos während der Runde
	 */
	public void setPositionen(ArrayList<Position> positionen) {
		this.positionen = positionen;
	}

	/**
	 * fügt eine Position des Autos während der Runde hinzu
	 * @param position eine Positionen des Autos während der Runde
	 */
	public void addPosition(Position position) {
		positionen.add(position);
	}

	/**
	 * liefert die Position des Autos während der Runde zum übergebenen Zeitpunkt
	 * @param aktuelleZeit Zeitpunkt, an dem die Position gesucht wird
	 * @return Position des Autos während der Runde zum übergebenen Zeitpunkt
	 */
	public Position getPositionBeiZeit(long aktuelleZeit) {
		Position ret = positionen.get(positionen.size() - 1);
		for (int i = 0; i < positionen.size(); i++)
			if (positionen.get(i).getRundenzeit() > aktuelleZeit) {
				if (i == 0)
					ret = positionen.get(i);
				else {
					ret = positionen.get(i - 1);
					float prozentAktuellerSektor = (float) (aktuelleZeit - positionen
							.get(i - 1).getRundenzeit())
							/ (positionen.get(i).getRundenzeit() - positionen
									.get(i - 1).getRundenzeit());
					float kurvenwinkelZasatz = positionen.get(i)
							.getKurvenwinkel()
							- positionen.get(i - 1).getKurvenwinkel();
					if (Math.abs(kurvenwinkelZasatz) > PApplet.PI) {
						if (positionen.get(i - 1).getKurvenwinkel() > PApplet.PI)
							kurvenwinkelZasatz += PApplet.TWO_PI;
						else
							kurvenwinkelZasatz -= PApplet.TWO_PI;
						;
					}
					ret = new Position(positionen.get(i - 1).getX()
							+ (positionen.get(i).getX() - positionen.get(i - 1)
									.getX()) * prozentAktuellerSektor,
							positionen.get(i - 1).getY()
									+ (positionen.get(i).getY() - positionen
											.get(i - 1).getY())
									* prozentAktuellerSektor, positionen.get(
									i - 1).getKurvenwinkel()
									+ (kurvenwinkelZasatz)
									* prozentAktuellerSektor, aktuelleZeit,
							positionen.get(i - 1).getV()
									+ (positionen.get(i).getV() - positionen
											.get(i - 1).getV())
									* prozentAktuellerSektor);
				}
				i = positionen.size();
			}
		return ret;
	}
}
