package net.tfobz.arduinoracer.util;

/**
 * Die Klasse Bodengegebenheit verwaltet die Position die ein Pixel auf der
 * Rennstrecke besitzt. Dabei wird sich der Sektor gemerkt und ob man sich im
 * Rasen oder im Schlamm befindet. Falls kein Sektor gemerkt wird, ist das Auto
 * in der Mauer
 * 
 * @author Thomas Nocker
 *
 */
public class Bodenbegebenheit {

	/**
	 * der Sektor der die Bodengegebenheit annimmt
	 */
	private byte sektor;

	/**
	 * Konstruktor - setzt den Sektor der Bodengegebenheit
	 * 
	 * @param sektor
	 *            den die Bodengegebnheit annehmen soll
	 */
	public Bodenbegebenheit(byte sektor) {
		this.sektor = sektor;
	}

	/**
	 * liefert zurück, ob sich die Bodengegebenheit im Rasen befindet
	 * 
	 * @return true, wenn die Bodengegebenheit sich im Rasen befindet, ansonsten
	 *         fasle
	 */
	public boolean isImGras() {
		return sektor >= 33;
	}

	/**
	 * liefert zurück, ob sich die Bodengegebenheit in der Mauer befindet
	 * 
	 * @return true, wenn die Bodengegebenheit sich in der Mauer befindet,
	 *         ansonsten fasle
	 */
	public boolean isInDerMauer() {
		return sektor == -1;
	}

	/**
	 * liefert zurück, ob sich die Bodengegebenheit im Schlamm befindet. Im
	 * Schlamm befindet sie sich, wenn sie sich im Rasen befindet und der
	 * aktuelle Sektor 15, 16 oder 17 ist
	 * 
	 * @return true, wenn die Bodengegebenheit sich im Schlamm befindet,
	 *         ansonsten fasle
	 */
	public boolean isImSchlamm() {
		return isImGras()
				&& (getSektor() == 15 || getSektor() == 16 || getSektor() == 17);
	}

	/**
	 * liefert den Sektor der aktuellen Bodengegebenheit
	 * 
	 * @return der Sektor der aktuellen Bodengegebenheit
	 */
	public byte getSektor() {
		return (byte) (sektor % 33);
	}

	/**
	 * mischt zwei Bodengegebenheiten miteinander. Dabei wird außer Acht gelassen,
	 * ob das Auto sich im Rasen befindet oder nicht. Es wird immer der am
	 * weitesten vorne befindliche Sektor herangezogen und falls eine
	 * Bodengegebenheit sich in der Mauer befindet, wird immer die Mauer
	 * hergenommen
	 * 
	 * @param b
	 *            Bodengegebenheit mit der die aktuelle Bodengegebenheit
	 *            gemischt werden soll
	 */
	public void merge(Bodenbegebenheit b) {
		if (b.sektor == -1 || sektor == -1)
			sektor = -1;
		else {
			if (b.getSektor() > getSektor()
					&& (getSektor() != 0 && b.getSektor() != 32 || b
							.getSektor() == 32 && getSektor() == 31))
				sektor = b.getSektor();
		}
	}
}
