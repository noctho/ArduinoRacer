package net.tfobz.arduinoracer.runden;

import net.tfobz.arduinoracer.Spieloberflaeche;

/**
 * Die Klasse Rundenserie verwaltet die Rundenzeiten eine Abfolge an Runden, die
 * für ein Rennen gegeneinander herangezogen werden kann. Die erfolgt, indem
 * alle Runden sowie die aktuelle Runde abgelegt werden
 * 
 * @author Thomas Nocker
 *
 */
public class Rundenserie implements Comparable<Rundenserie>, Cloneable {
	/**
	 * Alle Runden die erfasst werden
	 */
	private Rundenzeit[] runden = null;
	/**
	 * Jene Runde, die zurzeit befahren wird
	 */
	private int aktuelleRunde = -1;
	/**
	 * Der Zeitpunkt in Millisekunden, an dem die Runde gefahren wurde
	 */
	private long serienstart = 0;
	/**
	 * Die Gesamtzeit, die das Rennen pausiert hat
	 */
	private long gesamtpause = 0;

	/**
	 * Konstruktor - legt eine Rundenserie mit der übergebenen Anzahl an Runden
	 * an
	 * 
	 * @param anzahlRunden
	 *            Anzahl der Runden, die die Rundenserie haben soll
	 */
	public Rundenserie(int anzahlRunden) {
		runden = new Rundenzeit[anzahlRunden];
		for (int i = 0; i < runden.length; i++)
			runden[i] = new Rundenzeit();
	}

	/**
	 * setzt den Start der Rundenserie und startet die erste Runde mit den
	 * übergebenen Millisekunden
	 * 
	 * @param millis
	 *            Millisekunden, an denen die Rundenserie gestartet werden soll
	 */
	public void setStart(long millis) {
		serienstart = millis;
		aktuelleRunde = 0;
		runden[0].setStart(millis);
	}

	/**
	 * setzt die Pause der Rundenserie, indem die aktuelle Runde pausiert wird
	 * 
	 * @param millis
	 *            Millisekunden, an denen die Pause beginnen soll
	 */
	public void setPause(long millis) {
		runden[aktuelleRunde].setPause(millis);
	}

	/**
	 * setzt das Ende der Pause, indem die aktuelle Runde fortgesetzt wird
	 * 
	 * @param millis
	 *            Millisekunden, an denen die Pause enden soll
	 */
	public void setEndePause(long millis) {
		runden[aktuelleRunde].setEndePause(millis);
	}

	/**
	 * setzt einen neuen Sektor. Dabei werden die übergebenen Millisekunden
	 * sowohl als Ende des letzten Sektors, als auch als Anfang des neuen
	 * Sektors genommen
	 * 
	 * @param millis
	 *            Millisekunden, an denen der Sektor beginnt bzw. endet
	 */
	public void setNewSektor(long millis) {
		if (millis == -1)
			while (!isFinished()) {
				runden[aktuelleRunde].setNewSektor(millis);
				if (runden[aktuelleRunde].isFinished() && !isFinished()) {
					aktuelleRunde++;
					if (!isFinished())
						runden[aktuelleRunde].setStart(millis);
				}
			}
		if (!isFinished()) {
			runden[aktuelleRunde].setNewSektor(millis);
			if (runden[aktuelleRunde].isFinished() && !isFinished()) {
				aktuelleRunde++;
				if (!isFinished())
					runden[aktuelleRunde].setStart(millis);
			}
		}
	}

	/**
	 * liefert zurück, ob die Rundenserie beendet oder ungültig ist
	 * 
	 * @return true wenn die Rundenserie beendet oder ungültig ist, ansonsten
	 *         false
	 */
	public boolean isFinished() {
		return isDisqualisfiziert() || runden[runden.length - 1].isFinished();
	}

	/**
	 * liefert die Anzahl der Runden der Rundenserie zurück
	 * 
	 * @return die Anzahl der Runden der Rundenserie zurück
	 */
	public int getAnzahlRunden() {
		return runden.length;
	}

	/**
	 * liefert die Zeit des aktuellen Sektor zurück
	 * 
	 * @return die Zeit des aktuellen Sektor
	 */
	public byte getSektor() {
		return runden[aktuelleRunde].getSektor();
	}

	/**
	 * liefert den absoluten Sektor, d.h. es wird jene Zahl an Sektoren
	 * zurückgeliefert, die in dieser Rundenserie bereits absolviert worden sind
	 * 
	 * @return der absolute Sektor
	 */
	public int getAbsolutenSektor() {
		int ret = aktuelleRunde
				* 33
				+ (runden[aktuelleRunde].isFinished() ? 33
						: runden[aktuelleRunde].getLetztenBefahrenenSektor());
		return ret == -1 ? 0 : ret;
	}

	/**
	 * liefert den Sektor, in dem sich das Auto zurzeit befindet
	 * @return der Sektor, in dem sich das Auto zurzeit befindet
	 */
	public int getAktuellBefahrenenSektor() {
		int ret = aktuelleRunde
				* 33
				+ (runden[aktuelleRunde].isFinished() ? 33
						: runden[aktuelleRunde].getSektor());
		return ret == -1 ? 0 : ret;
	}

	/**
	 * liefert die aktuelle Zeit der Rundenserie als String zurück
	 * 
	 * @param oberflaeche
	 *            von der die aktuelle Zeit stammt
	 * @return die aktuelle Zeit der Rundenserie als String
	 */
	public String getAktuelleZeit(Spieloberflaeche oberflaeche) {
		return Runde.zeitToString(getAktuelleZeitZahl(oberflaeche));
	}

	/**
	 * liefert die aktuelle Zeit der Rundenserie in Millisekunden
	 * 
	 * @param oberflaeche
	 *            von der die aktuelle Zeit stammt
	 * @return die aktuelle Zeit der Rundenserie in Millisekunden
	 */
	public long getAktuelleZeitZahl(Spieloberflaeche oberflaeche) {
		long zeit = 0;
		if (isGestartet()) {
			for (int i = 0; i < aktuelleRunde; i++)
				zeit += runden[i].getRundenzeit();
			zeit += runden[aktuelleRunde].getAktuelleZeit(oberflaeche);
		}
		return zeit;
	}

	/**
	 * liefert zurück, ob die Rundenserie gestartet ist oder nicht
	 * 
	 * @return true, wenn die Rundenserie gestartet ist, ansonsten false
	 */
	private boolean isGestartet() {
		return runden[0].isGestartet();
	}

	/**
	 * berechnet die Differenz der übergebenen Referenzzeit zu dieser
	 * Rundenserie und gibt sie als String zurück. Dabei wird der aktuelle
	 * Sektor mit jener Zeit aufgefüllt, die er beim passieren zurzeit haben
	 * würde. Die weiteren nicht befahrenen Sektoren werden mit einer
	 * Gesamtserie aufgefüllt, die immer mit den am schlechtesten platzierten
	 * Fahren am jeweiligen Sektor aufgefüllt wurden
	 * 
	 * @param referenzSerie
	 *            Rundenserie, mit der die Rundenserie verglichen wird
	 * @param gesamtserie
	 *            Rundenserie mit der die Rundenserie aufgefüllt wird
	 * @param zeit
	 *            die der aktuellen Zeit in Millisekunden entspricht
	 * @return der Abstand der Rundenserie zur übergebenen Referenzserie
	 */
	public String berechneDifferenz(Rundenserie referenzSerie,
			Rundenserie gesamtserie, long zeit, boolean schneller) {
		String ret = "";
		long abstand = 0;
		if (isGestartet()) {
			Rundenserie meineRundenserie = new Rundenserie(getAnzahlRunden());
			meineRundenserie.setStart(0);
			for (int i = 0; i < gesamtserie.getAktuellBefahrenenSektor(); i++)
				if (i < getAktuellBefahrenenSektor())
					meineRundenserie.setAbsolutenSektor(i,
							getAbsoluteSektrozeit(i));
				else if (i == getAktuellBefahrenenSektor()
						&& zeit - runden[aktuelleRunde].getSektorstart() > gesamtserie
								.getAbsoluteSektrozeit(i))
					meineRundenserie.setAbsolutenSektor(i, zeit
							- runden[aktuelleRunde].getSektorstart());
				else
					meineRundenserie.setAbsolutenSektor(i,
							gesamtserie.getAbsoluteSektrozeit(i));
			// System.out.print("deto  ");
			abstand = meineRundenserie.getAbsoluteZwischenzeit(gesamtserie
					.getAbsolutenSektor());
			//
			abstand -= referenzSerie.getAbsoluteZwischenzeit(gesamtserie
					.getAbsolutenSektor());
			if (schneller && abstand > 0 || !schneller && abstand < 0
					|| abstand == 0) {
				ret = "-";
				// System.err.println();
				// referenzSerie.getAktuelleRunde().println();
				// meineRundenserie.getAktuelleRunde().println();
			} else {
				if (abstand > 0)
					ret = "+";
				else
					ret = "-";
				abstand = Math.abs(abstand);
				if (abstand > 60000)
					ret += Runde.zeitToString(abstand);
				else
					ret += Runde.zeitToStringOhneMinuten(abstand);
			}
		}
		return ret;
	}

	/**
	 * liefert die Gesamtzeit des übergebenen absoluten Sektors in Millisekunden
	 * 
	 * @param sektor
	 *            absoluter Sektor, an dem die Zeit abgefragt wird
	 * @return die Gesamtzeit des übergebenen absoluten Sektors in Millisekunden
	 */
	public long getAbsoluteZwischenzeit(int sektor) {
		long ret = 0;
		try {
			for (int i = 0; i < sektor / 33; i++)
				ret += runden[i].getRundenzeit();
			if (sektor % 33 >= 0)
				ret += runden[sektor / 33].getGesamtZwischenzeit(sektor % 33);
		} catch (RuntimeException e) {
		}
		return ret;
	}

	/**
	 * liefert die absolute Sektorzeit in Millisekunden des übergebenen
	 * absoluten Sektor
	 * 
	 * @param sektor
	 *            absoluter Sektor, an dem die Zeit abgefragt wird
	 * @return die absolute Sektorzeit in Millisekunden
	 */
	public long getAbsoluteSektrozeit(int sektor) {
		return runden[sektor / 33].getSektorzeit(sektor % 33);
	}

	/**
	 * liefert zurück in welcher Runde von wie vielen Runden sich die
	 * Rundenserie zurzeit befindet
	 * 
	 * @return in welcher Runde von wie vielen Runden sich die Rundenserie
	 *         zurzeit befindet
	 */
	public String getRundenVon() {
		return (aktuelleRunde + 1) + "/" + runden.length;
	}

	/**
	 * liefert alle bisherigen Pausenzeiten, die zum Serienstart addiert werden
	 * 
	 * @return alle bisherigen Pausenzeiten, die zum Serienstart addiert werden
	 */
	public long getAlleZusatzzeiten() {
		return serienstart + gesamtpause;
	}

	/**
	 * setzt die Zeit des übergebenen absoluten Sektor
	 * 
	 * @param absoluterSektor
	 *            dessen Zeit gesetzt werden soll
	 * @param zeit
	 *            die der übergebene absolute Sektor annehmen soll
	 */
	public void setAbsolutenSektor(int absoluterSektor, long zeit) {
		if (!isDisqualisfiziert() || zeit == -1) {
			runden[absoluterSektor / 33].setSektor(absoluterSektor % 33, zeit);
			if (absoluterSektor / 33 == aktuelleRunde
					&& runden[aktuelleRunde].isFinished() && !isFinished()) {
				aktuelleRunde++;
				if (!isFinished())
					runden[aktuelleRunde].setStart(zeit);
			}
		}
	}

	/**
	 * liefert die Rundenzeit der aktuellen Runde
	 * 
	 * @return die Rundenzeit der aktuellen Runde
	 */
	public Rundenzeit getAktuelleRunde() {
		return runden[aktuelleRunde];
	}

	/**
	 * liefert die Differenz der Endzeit zur übergeben Referenzzeit
	 * 
	 * @param rundenserie
	 *            Referenzzeit, von der der Abstand der Endzeit gemessen wird
	 * @param oberflaeche
	 *            des Spiels
	 * @return die Differenz der Endzeit zur übergeben Referenzzeit
	 */
	public String getEnddiferenz(Rundenserie rundenserie,
			Spieloberflaeche oberflaeche) {
		long zeit = Math.abs(getAktuelleZeitZahl(oberflaeche)
				- rundenserie.getAktuelleZeitZahl(oberflaeche));
		String ret = null;
		if (zeit > 60000)
			ret = Runde.zeitToString(zeit);
		else
			ret = Runde.zeitToStringOhneMinuten(zeit);
		return ret;
	}

	/**
	 * liefert zurück, ob die Runde gültig ist oder nicht
	 * 
	 * @return true, wenn die Runde ungültig ist, ansonsten false
	 */
	public boolean isDisqualisfiziert() {
		return runden[runden.length - 1].isDisqualifiziert();
	}

	@Override
	public int compareTo(Rundenserie rundenserie) {
		int ret = 0;
		if (isGestartet())
			if (isDisqualisfiziert()
					|| this.getAbsolutenSektor() < rundenserie
							.getAbsolutenSektor()
					&& !rundenserie.isDisqualisfiziert())
				ret = 1;
			else if (rundenserie.isDisqualisfiziert()
					|| this.getAbsolutenSektor() > rundenserie
							.getAbsolutenSektor() && !isDisqualisfiziert())
				ret = -1;
			else
				ret = ((Long) getAbsoluteZwischenzeit(getAbsolutenSektor()))
						.compareTo(rundenserie
								.getAbsoluteZwischenzeit(getAbsolutenSektor()));
		return ret;
	}

	/**
	 * liefert den Zeitpunkt des Startes der Rundenserie
	 * @return der Zeitpunkt des Startes der Rundenserie
	 */
	public long getSerienstart() {
		return serienstart;
	}

	/**
	 * liefert alle Runden der Rundenserie
	 * @return alle Runden der Rundenserie
	 */
	public Rundenzeit[] getRunden() {
		return runden;
	}
}
