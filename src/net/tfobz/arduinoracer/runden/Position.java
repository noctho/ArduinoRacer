package net.tfobz.arduinoracer.runden;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Diese Klasse bildet einen Punkt ab, an dem sich ein Spieler zu einem
 * bestimmten Zeitpunkt während der Runde befindet
 * 
 * @author Thomas Nocker
 *
 */
@XmlRootElement
public class Position {
	/**
	 * x-Koordinate des Autos
	 */
	private float x = 0;
	/**
	 * y-Koordinate des Autos
	 */
	private float y = 0;
	/**
	 * Kurvenwinkel des Autos
	 */
	private float kurvenwinkel = 0;
	/**
	 * Zeitpunkt des Position während der Runde
	 */
	private long rundenzeit = 0;
	/**
	 * Geschwindigkeit des Autos
	 */
	private float v = 0;

	/**
	 * Defaultkonstruktor
	 */
	public Position() {
	}

	/**
	 * Konstruktor
	 * @param x x-Koordinate des Autos
	 * @param y y-Koordinate des Autos
	 * @param kurvenwinkel des Autos
	 * @param rundenzeit des Autos
	 * @param v Geschwindigkeit des Autos
	 */
	public Position(float x, float y, float kurvenwinkel, long rundenzeit,
			float v) {
		super();
		this.x = x;
		this.y = y;
		this.kurvenwinkel = kurvenwinkel;
		this.rundenzeit = rundenzeit;
		this.v = v;
	}

	/**
	 * liefert die x-Koordinate des Autos
	 * @return x-Koordinate des Autos
	 */
	public float getX() {
		return x;
	}

	/**
	 * setzt die x-Koordinate des Autos
	 * @param x x-Koordinate des Autos
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * liefert die y-Koordinate des Autos
	 * @return y-Koordinate des Autos
	 */
	public float getY() {
		return y;
	}

	/**
	 * setzt die y-Koordinate des Autos
	 * @param y y-Koordinate des Autos
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * liefert den Kurvenwinkel des Autos
	 * @return Kurvenwinkels des Autos
	 */
	public float getKurvenwinkel() {
		return kurvenwinkel;
	}

	/**
	 * setzt den Kurvenwinkel des Autos
	 * @param kurvenwinkel Kurvenwinkel des Autos
	 */
	public void setKurvenwinkel(float kurvenwinkel) {
		this.kurvenwinkel = kurvenwinkel;
	}

	/**
	 * liefert den Zeitpunkt des Position während der Runde
	 * @return Zeitpunkt des Position während der Runde
	 */
	public long getRundenzeit() {
		return rundenzeit;
	}

	/**
	 * setzt den Zeitpunkt des Position während der Runde
	 * @param rundenzeit Zeitpunkt des Position während der Runde
	 */
	public void setRundenzeit(long rundenzeit) {
		this.rundenzeit = rundenzeit;
	}

	/**
	 * liefert die Geschwindigkeit des Autos
	 * @return Geschwindigkeit des Autos
	 */
	public float getV() {
		return v;
	}

	/**
	 * setzt die Geschwindigkeit des Autos
	 * @param v Geschwindigkeit des Autos
	 */
	public void setV(float v) {
		this.v = v;
	}
}
