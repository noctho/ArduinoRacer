package net.tfobz.arduinoracer.util;

import java.io.Serializable;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.komponenten.MultiplayerAuto;
import net.tfobz.arduinoracer.runden.Rundenserie;

/**
 * Die Klasse MultiplayerSpieler verwaltet jenem Spieler, der einem
 * Multiplayerspiel beitreten kann. Dabei sind sein Auto, seine ID, sein Name
 * und seine Rundenserie notwendig
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class MultiplayerSpieler implements Serializable,
		Comparable<MultiplayerSpieler> {
	/**
	 * Das Auto des Spielers
	 */
	private MultiplayerAuto auto = null;
	/**
	 * Die ID des Spielers
	 */
	private int id = -1;
	/**
	 * Der Name des Spielers
	 */
	private String name = null;
	/**
	 * Die Rundenserie des Spielers
	 */
	private Rundenserie rundenserie = null;
	/**
	 * Der aktuelle Sektor des Spielers
	 */
	private byte sektor = -1;
	/**
	 * Die aktuelle Rundenzeit des Spieler, die vom Gegner über das Netzwerk
	 * stammt
	 */
	private long aktuelleRundenzeit;

	/**
	 * Defaultkonstruktor
	 */
	public MultiplayerSpieler() {

	}

	/**
	 * Konstruktor - setzt die übergebenen Werte
	 * 
	 * @param auto
	 *            mit dem der Spieler fahren soll
	 * @param id
	 *            des Spielers
	 * @param name
	 *            des Spielers
	 */
	public MultiplayerSpieler(MultiplayerAuto auto, int id, String name) {
		super();
		this.auto = auto;
		this.id = id;
		this.name = name;
	}

	/**
	 * liefert das Auto des Spielers
	 * 
	 * @return das Auto des Spielers
	 */
	public MultiplayerAuto getAuto() {
		return auto;
	}

	/**
	 * setzt das Auto des Spielers
	 * 
	 * @param auto
	 *            das der Spieler haben soll
	 */
	public void setAuto(MultiplayerAuto auto) {
		this.auto = auto;
	}

	/**
	 * liefert die ID des Spielers
	 * 
	 * @return die ID des Spielers
	 */
	public int getId() {
		return id;
	}

	/**
	 * setzt die ID des Spielers
	 * 
	 * @param id
	 *            die der Spieler besitzen soll
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * liefert den Namen des Spielers
	 * 
	 * @return der Name des Spielers
	 */
	public String getName() {
		return name;
	}

	/**
	 * setzt den Namen des Spielers
	 * 
	 * @param name
	 *            den der Spieler haben soll
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * liefert den String, die Daten des Spielers den Gegnern mitgeteilt werden
	 * können
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @return String der aktuellen Daten des Spielers
	 */
	public String getNetzwerkString(Spieloberflaeche oberflaeche) {
		return id + ";" + auto.getNetzwerkString() + ";"
				+ rundenserie.getAktuelleZeitZahl(oberflaeche);
	}

	/**
	 * liefert den String, damit der Spieler sich beim Server registrieren kann
	 * 
	 * @return Registrierungsstring für den Server
	 */
	public String getRegistrierNetzwerkString() {
		return Netzwerkstrings.AUTO + auto.getFarbe() + ";" + name + "\n";
	}

	/**
	 * legt gemäß der Anzahl der Runden des Spiels eine neue Rundenserie an
	 * 
	 * @param anzahlRunden
	 *            die Anzahl der Runden des Spiels
	 */
	public void setAnzahlRunden(int anzahlRunden) {
		rundenserie = new Rundenserie(anzahlRunden);
	}

	/**
	 * liefert die Rundenserie des Spielers
	 * 
	 * @return die Rundenserie des Spielers
	 */
	public Rundenserie getRundenserie() {
		return rundenserie;
	}

	/**
	 * errechnet die aktuelle Rundenzeit des Spielers anhand seiner Position
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void setRundenzeit(Spieloberflaeche oberflaeche) {
		Bodenbegebenheit bodenbegebenheit = auto
				.getAktuelleBodenbegebenheit(oberflaeche.getStrecke());
		if (bodenbegebenheit.getSektor() != sektor
				&& bodenbegebenheit.getSektor() != -1) {
			if ((sektor + 1) % 33 == bodenbegebenheit.getSektor())
				if (bodenbegebenheit.getSektor() == (rundenserie
								.getSektor() + 1) % 33) {
					long zeit = aktuelleRundenzeit
							+ rundenserie.getAlleZusatzzeiten();
					if (rundenserie.getSektor() == 32)
						zeit -= auto.getZeitImSektor(oberflaeche);
					rundenserie.setNewSektor(zeit);
				}
			sektor = bodenbegebenheit.getSektor();
		} else if (oberflaeche.currentMillis
				- rundenserie.getAktuelleRunde().getSektorstart() > 60000) {
			rundenserie.setNewSektor(-1);
		}
	}

	/**
	 * liefert den aktuellen Sektor des Spielers
	 * 
	 * @return der aktuelle Sektor des Spielers
	 */
	public byte getSektor() {
		return sektor;
	}

	/**
	 * setzt die aktuelle Rundenzeit des Spielers
	 * 
	 * @param aktuelleRundenzeit
	 *            die aktuelle Zeit, die die Runde annehmen soll
	 */
	public void setAktuelleRundenzeit(long aktuelleRundenzeit) {
		this.aktuelleRundenzeit = aktuelleRundenzeit;
		if (rundenserie.isFinished()
				&& rundenserie.getAbsoluteZwischenzeit(rundenserie
						.getAbsolutenSektor()) != this.aktuelleRundenzeit)
			rundenserie.setAbsolutenSektor(
					rundenserie.getAbsolutenSektor(),
					aktuelleRundenzeit
							- rundenserie.getAbsoluteZwischenzeit(rundenserie
									.getAbsolutenSektor() - 1));
	}

	/**
	 * setzt die aktuelle Rundenzeit anhand der aktuellen Rundenzeit der
	 * Rundenserie
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void setAktuelleRundenzeit(Spieloberflaeche oberflaeche) {
		aktuelleRundenzeit = rundenserie.getAktuelleZeitZahl(oberflaeche);
	}

	@Override
	public int compareTo(MultiplayerSpieler multiplayerSpieler) {
		return rundenserie.compareTo(multiplayerSpieler.rundenserie);
	}

	@Override
	public String toString() {
		return id + ": " + name;
	}
}
