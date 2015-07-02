package net.tfobz.arduinoracer.util;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

/**
 * Die Klasse Multiplayerspiel verwaltet ein Multiplayerspiel. Dabei wird die
 * ID, der Name, der Spielersteller, die Anzahl der Runden, die Anzahl der
 * Spieler, alle weiteren Spieler und der Host des Spiels abgespeichert
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class MultiplayerSpiel implements Serializable {

	/**
	 * Die ID des Spiels
	 */
	private int id = -1;
	/**
	 * die maximale Anzahl der Spieler des Spiels
	 */
	private int gesamtSpieler = 0;
	/**
	 * Die Anzahl der Runden im Spiel
	 */
	private int anzahlRunden = 0;
	/**
	 * Der Name des Spiels
	 */
	private String spielname = null;
	/**
	 * Der Ersteller des Spiels
	 */
	private MultiplayerSpieler spielersteller = null;
	/**
	 * Alle Spieler außer dem Spielersteller des Spiels
	 */
	private Vector<MultiplayerSpieler> spieler = new Vector<MultiplayerSpieler>();
	/**
	 * Der Host des Spiels, auf dem die Kommunikation des Spiels stattfindet
	 */
	private String host = "230.0.0.1";

	/**
	 * Defaultkonstruktor
	 */
	public MultiplayerSpiel() {
	}

	/**
	 * Konstruktor - legt die übergebenen Werte des Spiels fest
	 * 
	 * @param gesamtSpieler
	 *            Anzahl der Spieler des Spiels
	 * @param anzahlRunden
	 *            Anzahl der Runden des Spiels
	 * @param spielname
	 *            Name des Spiels
	 */
	public MultiplayerSpiel(int gesamtSpieler, int anzahlRunden,
			String spielname) {
		this.spielname = spielname;
		this.anzahlRunden = anzahlRunden;
		this.gesamtSpieler = gesamtSpieler;
	}

	/**
	 * liefert die Anzahl der Spieler des Spiels
	 * 
	 * @return die Anzahl der Spieler des Spiels
	 */
	public int getGesamtSpieler() {
		return gesamtSpieler;
	}

	/**
	 * setzt die Anzahl der Spieler des Spiels
	 * 
	 * @param gesamtSpieler
	 *            Anzahl der Spieler, die das Spiel habe soll
	 */
	public void setGesamtSpieler(int gesamtSpieler) {
		this.gesamtSpieler = gesamtSpieler;
	}

	/**
	 * liefert die Anzahl der beigetretenen Spieler des Spiels, d.h. alle
	 * aktiven Spieler außer dem Spielersteller
	 * 
	 * @return die Anzahl der beigetretenen Spieler des Spiels
	 */
	public int getBegetetreteneSpieler() {
		return spieler.size();
	}

	/**
	 * liefert die Anzahl der Runden des Spiels
	 * 
	 * @return die Anzahl der Runden des Spiels
	 */
	public int getAnzahlRunden() {
		return anzahlRunden;
	}

	/**
	 * setzt die Anzahl der Runden des Spiels
	 * 
	 * @param anzahlRunden
	 *            Anzahl der Runden, die das Spiel haben soll
	 */
	public void setAnzahlRunden(int anzahlRunden) {
		this.anzahlRunden = anzahlRunden;
	}

	/**
	 * liefert den Namen des Spiels
	 * 
	 * @return der Namen des Spiels
	 */
	public String getSpielname() {
		return spielname;
	}

	/**
	 * setzt den namen des Spiel
	 * 
	 * @param spielname
	 *            Name, den das Spiel annehmen soll
	 */
	public void setSpielname(String spielname) {
		this.spielname = spielname;
	}

	/**
	 * liefert den Ersteller des Spiels
	 * 
	 * @return der Ersteller des Spiels
	 */
	public MultiplayerSpieler getSpielersteller() {
		return spielersteller;
	}

	/**
	 * setzt den Ersteller des Spiels
	 * 
	 * @param spielersteller
	 *            der den Ersteller des Spiels annehmen soll
	 */
	public void setSpielersteller(MultiplayerSpieler spielersteller) {
		this.spielersteller = spielersteller;
	}

	/**
	 * liefert die Anzahl aller verfügbaren Spielern in Relation zu allen
	 * Spielern als String
	 * 
	 * @return Anzahl aller verfügbaren Spielern in Relation zu allen Spielern
	 */
	public String getVerfuegbareSpielerVonAllen() {
		return (gesamtSpieler - getBegetetreteneSpieler()) + "/"
				+ gesamtSpieler;
	}

	/**
	 * liefert die Spieler, die dem Spiel beigetreten sind
	 * 
	 * @return die Spieler, die dem Spiel beigetreten sind
	 */
	public Vector<MultiplayerSpieler> getSpieler() {
		return spieler;
	}

	/**
	 * liefert die ID des Spiels
	 * 
	 * @return die ID des Spiels
	 */
	public int getId() {
		return id;
	}

	/**
	 * setzt die ID des Spiels
	 * 
	 * @param id
	 *            die das Spiel annehmen soll
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * liefert den Spieler aller Spieler des Spiels mit der übergebenen ID
	 * 
	 * @param id
	 *            des gesuchten Spielers
	 * @return Spieler mit der übergebenen ID
	 */
	public MultiplayerSpieler getSpielerById(int id) {
		MultiplayerSpieler ret = null;
		if (spielersteller.getId() == id)
			ret = spielersteller;
		else
			for (MultiplayerSpieler einSpieler : spieler)
				if (einSpieler.getId() == id)
					ret = einSpieler;
		return ret;
	}

	/**
	 * liefert den Host, auf dem die Kommunikation des Spiels stattfindet
	 * 
	 * @return der Host, auf dem die Kommunikation des Spiels stattfindet
	 */
	public String getHost() {
		return host;
	}

	/**
	 * liefert alle Spieler des Spiels ohne jenem Spieler mit er übergebenen ID
	 * 
	 * @param id
	 *            der in der Liste der Spieler nicht vorkommen soll
	 * @return alle Spieler des Spiels ohne jenem Spieler mit er übergebenen ID
	 */
	public Vector<MultiplayerSpieler> getAlleSpielerOhne(int id) {
		Vector<MultiplayerSpieler> ret = new Vector<MultiplayerSpieler>();
		for (MultiplayerSpieler spieler : getAlleSpieler())
			if (spieler.getId() != id)
				ret.add(spieler);
		return ret;
	}

	/**
	 * liefert komplett alle Spiele des Spiels
	 * 
	 * @return komplett alle Spiele des Spiels
	 */
	@SuppressWarnings("unchecked")
	public Vector<MultiplayerSpieler> getAlleSpieler() {
		Vector<MultiplayerSpieler> ret = (Vector<MultiplayerSpieler>) spieler
				.clone();
		ret.add(spielersteller);
		return ret;

	}

	/**
	 * startet das Spiel, indem die Rundenserien aller Spieler mit den
	 * übergebenen Millisekunden gestartet werden
	 * 
	 * @param millis
	 *            Millisekunden, mit denen das Spiel gestartet wird
	 */
	public void setStart(long millis) {
		for (MultiplayerSpieler spieler : getAlleSpieler())
			spieler.getRundenserie().setStart(millis);
	}

	/**
	 * liefert die Anzahl der gesamten Spieler des Spiels
	 * 
	 * @return die Anzahl der gesamten Spieler des Spiels
	 */
	public int getAnzahlSpieler() {
		return gesamtSpieler + 1;
	}

	/**
	 * kontrolliert, ob der Spieler mit der übergebenen ID im Spiel bereits
	 * vorhanden ist
	 * 
	 * @param id
	 *            auf dessen Vorhandensein kontrolliert wird
	 * @return true, wenn der Spieler mit der übergebenen ID im Spiel existiert,
	 *         ansonsten false
	 */
	public boolean hasSpieler(int id) {
		boolean ret = spielersteller.getId() == id;
		for (MultiplayerSpieler einSpieler : spieler)
			if (einSpieler.getId() == id)
				ret = true;
		return ret;
	}

	/**
	 * kontrolliert, ob ein Spieler im Spiel das Auto mit der übergebenen Farbe
	 * besitzt
	 * 
	 * @param farbe
	 *            auf dessen Existenz untersucht wird
	 * @return true, wenn ein Spieler das Auto mit der übergebenen Farbe
	 *         besitzt, ansonsten false
	 */
	public boolean hasAutofarbe(int farbe) {
		boolean ret = spielersteller.getAuto().getFarbe() == farbe;
		for (MultiplayerSpieler einSpieler : spieler)
			if (einSpieler.getAuto().getFarbe() == farbe)
				ret = true;
		return ret;
	}

	/**
	 * kontrolliert ob ein Spieler im Spiel den übergebenen Namen besitzt
	 * 
	 * @param name
	 *            auf dessen Existenz untersucht wird
	 * @return true, wenn ein Spieler den übergebenen Namen hat, ansonsten false
	 */
	public boolean hasSpielername(String name) {
		boolean ret = spielersteller.getName().equals(name);
		for (MultiplayerSpieler einSpieler : spieler)
			if (einSpieler.getName().equals(name))
				ret = true;
		return ret;
	}

	/**
	 * löscht den Spieler aus der Liste der Spieler mit der übergebenen ID
	 * 
	 * @param id
	 *            dessen Spieler gelöscht werden soll
	 */
	public void removeSpieler(int id) {
		for (int i = 0; i < spieler.size(); i++)
			if (spieler.get(i).getId() == id) {
				this.spieler.remove(i);
				i = spieler.size() + 1;
			}
	}

	/**
	 * fügt ein Spieler dem Spiel hinzu
	 * 
	 * @param spieler
	 *            der dem Spiel hinzugefügt werden soll
	 */
	public void addSpieler(MultiplayerSpieler spieler) {
		this.spieler.add(spieler);
	}

	@Override
	public String toString() {
		return id + " (" + spielname + "): " + anzahlRunden + " Runden "
				+ getVerfuegbareSpielerVonAllen() + ": " + spieler.toString()
				+ spielersteller.toString();
	}

	/**
	 * generiert einen zufälligen Host, auf dem die Kommunikation des Spiels
	 * stattfindet
	 */
	public void generateHost() {
		String host = "224.0.0.0";
		while (host.equals("224.0.0.0"))
			host = (224 + new Random().nextInt(16)) + "."
					+ new Random().nextInt(256) + "."
					+ new Random().nextInt(256) + "."
					+ new Random().nextInt(256);
		this.host = host;
	}
}
