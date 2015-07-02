package net.tfobz.arduinoracer.runden;

import java.util.ArrayList;

import net.tfobz.arduinoracer.Spieloberflaeche;

/**
 * Die Klasse Rundenzeit verwaltet das Fahren einer Runde. Dabei werden pro
 * Runde 33 Zwischenzeiten erfasst, die in drei große Sektoren unterteilt sind.
 * Damit das möglich ist, wird jeweils der aktuelle Sektor abgelegt. Des
 * weiteren ermöglicht diese Klasse, dass man in einer Runde eine Pause einlegt.
 * 
 * @author Thomas Nocker
 *
 */
public class Rundenzeit implements Comparable<Rundenzeit>, Cloneable {

	/**
	 * zwischenzeiten, die pro Runde erfasst werden
	 */
	private long[] zwischenzeiten = new long[33];
	/**
	 * aktueller Sektor der Runde
	 */
	private byte sektor = -1;
	/**
	 * Millisekunden, an der der Sektor gestartet ist
	 */
	private long sektorstart = 0;
	/**
	 * Pausenzeit im aktuellen Sektor
	 */
	private long pausenzeit = 0;
	/**
	 * Zeit des Starts der Pause
	 */
	private long pausenstart = 0;
	/**
	 * Positionen, an denen sich das Auto während der Runde befindet
	 */
	private ArrayList<Position> positionen = new ArrayList<Position>();

	/**
	 * startet eine Runder in der übergebenen Zeit
	 * 
	 * @param start
	 *            Zeitpunkt des Rundenstarts
	 */
	public void setStart(long start) {
		reset();
		sektorstart = start;
		sektor = 0;
	}

	/**
	 * hält die Runde an, d.h. es wird der Startzeitpunkt der Pause gesetzt
	 * 
	 * @param millis
	 *            in denen die Pause startet
	 */
	public void setPause(long millis) {
		pausenstart = millis;
	}

	/**
	 * fährt die Runde fort, d.h. es wird der Startzeitpunkt der Pause auf 0
	 * gesetzt und die Pausenzeit mit den übergebenen Millisekunden minus dem
	 * vorherigen Pausenstart addiert
	 * 
	 * @param millis
	 *            in denen die Runde fortfährt
	 */
	public void setEndePause(long millis) {
		pausenzeit = millis - pausenstart;
		pausenstart = 0;
	}

	/**
	 * setzt die Millisekunden, an denen ein neuer Sektor begonnen wurde und
	 * inkrementiert den aktuelle Sektor
	 * 
	 * @param millis
	 *            in denen der neue Sektor betreten wurde
	 */
	public void setNewSektor(long millis) {
		if (millis == -1)
			zwischenzeiten[sektor] = -1;
		else
			zwischenzeiten[sektor] = millis - sektorstart - pausenzeit;
		sektorstart = millis;
		pausenzeit = 0;
		// System.err.println("New Sektor" + sektor + " zeit="
		// + zwischenzeiten[sektor]);
		if (sektor < 32)
			sektor++;
	}

	/**
	 * liefert zurück, ob die Runde bereits beendet oder ungültig ist
	 * 
	 * @return true, wenn die Runde bereits beendet oder ungültig ist, ansonsten
	 *         false
	 */
	public boolean isFinished() {
		return zwischenzeiten[zwischenzeiten.length - 1] != 0;
	}

	/**
	 * liefert die Endzeit der Runde in Millisekunden zurück
	 * 
	 * @return die Endzeit der Runde in Millisekunden zurück
	 */
	public long getRundenzeit() {
		if (!isFinished())
			throw new RuntimeException("Die Runde ist noch nich beendet");
		long ret = 0;
		for (long zwischenzeit : zwischenzeiten)
			ret += zwischenzeit;
		return ret;
	}

	/**
	 * liefert die Gesamtzwischenzeit des übergebenen Sektors zurück
	 * 
	 * @param sektor
	 *            dessen Gesamtzwischenzeit gesucht wird
	 * @return die Gesamtzwischenzeit des übergebenen Sektors zurück
	 */
	public long getGesamtZwischenzeit(int sektor) {
		long ret = zwischenzeiten[sektor];
		if (ret == 0) {
			throw new RuntimeException("Sektor noch nicht befahren: " + sektor);
		}
		for (int i = 0; i < sektor; i++)
			ret += zwischenzeiten[i];
		return ret;
	}

	/**
	 * liefert die Zeit des gesamten großen 1. Sektors in Millisekunden zurück
	 * 
	 * @return die Zeit des gesamten großen 1. Sektors in Millisekunden zurück
	 */
	public long getSektor1() {
		return getGesamtZwischenzeit(10);
	}

	/**
	 * liefert die Zeit des gesamten großen 2. Sektors in Millisekunden zurück
	 * 
	 * @return die Zeit des gesamten großen 2. Sektors in Millisekunden zurück
	 */
	public long getSektor2() {
		return getGesamtZwischenzeit(21);
	}

	/**
	 * liefert zurück, ob die Runde gestartet ist
	 * 
	 * @return true wenn die Runde gestartet ist, ansonsten false
	 */
	public boolean isGestartet() {
		return sektor >= 0;
	}

	/**
	 * liefert den aktuellen Sektor der Runde zurück
	 * 
	 * @return der aktuelle Sektor der Runde zurück
	 */
	public byte getSektor() {
		return sektor;
	}

	/**
	 * liefert den Sektor, in dem sich das Auto zurzeit befindet
	 * @return der Sektor, in dem sich das Auto zurzeit befindet
	 */
	public int getLetztenBefahrenenSektor() {
		int ret = 0;
		while (ret < zwischenzeiten.length && zwischenzeiten[ret] != 0)
			ret++;
		if (ret > sektor)
			ret = sektor;
		else
			ret--;
		return ret;
	}

	/**
	 * liefert die Endzeit der Runde als String zurück
	 * 
	 * @return die Endzeit der Runde als String zurück
	 */
	public String getEndzeitString() {
		return Runde.zeitToString(getRundenzeit());
	}

	/**
	 * liefert die aktuelle Zeit der Runde als String zurück
	 * 
	 * @param oberflaeche
	 *            von der die aktuelle Zeit kommt
	 * @return die aktuelle Zeit der Runde als String zurück
	 */
	public String getAktuelleZeitString(Spieloberflaeche oberflaeche) {
		String ret = null;
		if (isGestartet())
			ret = Runde.zeitToString(getAktuelleZeit(oberflaeche));
		return ret;
	}

	/**
	 * liefert die aktuelle Zeit der Runde in Millisekunden zurück. Wenn die
	 * Runde beendet ist, wird die Endzeit zurückgeliefert
	 * 
	 * @param oberflaeche
	 *            von der die aktuelle Zeit kommt
	 * @return die aktuelle Zeit der Runde in Millisekunden zurück
	 */
	public long getAktuelleZeit(Spieloberflaeche oberflaeche) {
		return isFinished() ? getRundenzeit()
				: (oberflaeche.currentMillis - sektorstart - pausenzeit + ((sektor - 1 >= 0) ? getGesamtZwischenzeit(sektor - 1)
						: 0));
	}

	/**
	 * liefert den Start des aktuellen Sektors
	 * 
	 * @return den Start des aktuellen Sektors
	 */
	public long getSektorstart() {
		return sektorstart;
	}

	/**
	 * liefert die Zeit der Pause des aktuellen Sektors
	 * 
	 * @return die Zeit der Pause des aktuellen Sektors
	 */
	public long getPausenzeit() {
		return pausenzeit;
	}

	/**
	 * setzt den übergebenen Sektor mit der übergebenen Zeit
	 * 
	 * @param sektor
	 *            der gesetzt werden soll
	 * @param zeit
	 *            die der übergebene Sektor annehmen soll
	 */
	public void setSektor(int sektor, long zeit) {
		zwischenzeiten[sektor] = zeit;
		if (this.sektor == sektor && !isFinished())
			this.sektor++;
	}

	/**
	 * liefert die Zeit des übergebenen Sektors
	 * 
	 * @param sektor
	 *            dessen Zeit abgefragt wird
	 * @return die Zeit des übergebenen Sektors
	 */
	public long getSektorzeit(int sektor) {
		return zwischenzeiten[sektor];
	}

	/**
	 * konvertiert die Rundenzeit in ein Runde, die dem Webservice mitgeliefert
	 * werden kann
	 * 
	 * @param name
	 *            des Fahrers der Runde
	 * @return diese Rundenzeit als Runde, die dem Webservice mitgeliefert
	 *         werden kann
	 */
	public Runde toRunde(String name) {
		return new Runde(name, getSektor1(), getSektor2(), getRundenzeit(),
				positionen);
	}

	/**
	 * liefert zurück, ob die aktuelle Runde noch gültig ist, oder nicht
	 * 
	 * @return true, wenn die aktuelle Runde ungültig ist, ansonsten false
	 */
	public boolean isDisqualifiziert() {
		return zwischenzeiten[zwischenzeiten.length - 1] == -1;
	}

	/**
	 * setzt die aktuelle Runde zurück
	 */
	public void reset() {
		if (isGestartet()) {
			zwischenzeiten = new long[33];
			sektor = -1;
			sektorstart = 0;
			pausenzeit = 0;
			pausenstart = 0;
			positionen = new ArrayList<Position>();
		}
	}

	/**
	 * fügt eine Position des Autos während der Runde hinzu
	 * @param position eine Positionen des Autos während der Runde
	 */
	public void addPosition(Position position) {
		positionen.add(position);
	}

	@Override
	public int compareTo(Rundenzeit rundenzeit) {
		int ret = 0;
		if (sektor > rundenzeit.sektor)
			ret = 1;
		else if (sektor < rundenzeit.sektor)
			ret = -1;
		else
			ret = (int) ((zwischenzeiten[sektor - 1] - rundenzeit.zwischenzeiten[rundenzeit.sektor - 1]));
		return ret;
	}
}
