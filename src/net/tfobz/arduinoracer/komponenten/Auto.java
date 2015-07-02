package net.tfobz.arduinoracer.komponenten;

import java.io.Serializable;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PApplet;
import processing.core.PShape;

/**
 * Die Klasse Auto beinhaltet in Abstrakter Form alle wichtigen Merkmal, die ein
 * Auto besitzen muss. Dazu zählen die aktuelle Koordinate, der aktuelle
 * Kurvenwinkel, die Farbe und die Grafiken, um es darzustellen. Das Fahren ist
 * jedoch mit diesem Auto unmöglich. Es ist jedoch möglich die aktuelle Position
 * auf einer übergeben Strecke abzufragen
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public abstract class Auto implements Serializable {
	/**
	 * der Abstand der Koordinaten des Autos vorne links von der Mitte aus
	 */
	protected static final int[][] VORNE_LINKS = { { -1, -48 }, { -4, -48 },
			{ -7, -48 }, { -9, -47 }, { -11, -47 }, { -13, -46 }, { -15, -45 },
			{ -16, -43 }, { -17, -42 }, { -17, -39 }, { -17, -36 },
			{ -17, -32 }, { -17, -28 }, { -17, -24 }, { -17, -20 } };
	/**
	 * der Abstand der Koordinaten des Autos vorne rechts von der Mitte aus
	 */
	protected static final int[][] VORNE_RECHTS = { { 1, -48 }, { 4, -48 },
			{ 7, -48 }, { 9, -47 }, { 11, -47 }, { 13, -46 }, { 15, -45 },
			{ 16, -43 }, { 17, -42 }, { 17, -39 }, { 17, -36 }, { 17, -32 },
			{ 17, -28 }, { 17, -24 }, { 17, -20 } };
	/**
	 * der Abstand der Koordinaten des Autos hinten links von der Mitte aus
	 */
	protected static final int[][] HINTEN_LINKS = { { -1, 48 }, { -4, 48 },
			{ -7, 48 }, { -10, 48 }, { -13, 48 }, { -16, 48 }, { -17, 47 },
			{ -17, 44 }, { -17, 41 }, { -17, 38 }, { -17, 35 }, { -17, 32 },
			{ -17, 29 }, { -17, 26 }, { -17, 23 }, { -17, 20 } };
	/**
	 * der Abstand der Koordinaten des Autos hinten rechts von der Mitte aus
	 */
	protected static final int[][] HINTEN_RECHTS = { { 1, 48 }, { 4, 48 },
			{ 7, 48 }, { 10, 48 }, { 13, 48 }, { 16, 48 }, { 17, 47 },
			{ 17, 44 }, { 17, 41 }, { 17, 38 }, { 17, 35 }, { 17, 32 },
			{ 17, 29 }, { 17, 26 }, { 17, 23 }, { 17, 20 } };
	/**
	 * Die Anzahl der Pixel, die ein Meter entsprechen
	 */
	public static final float METER_IN_PIXEL = 16F;
	/**
	 * der aktuelle Kurvenwinkel des Autos
	 */
	protected float kurvenwinkel = PApplet.TWO_PI;
	/**
	 * die aktuelle x-Koordinate auf der Rennstrecke von der Mitte der
	 * Rennstrecke aus
	 */
	protected float x = -145;
	/**
	 * die aktuelle y-Koordinate auf der Rennstrecke von der Mitte der
	 * Rennstrecke aus
	 */
	protected float y = -900;
	/**
	 * die aktuelle Geschwindigkeit des Autos
	 */
	protected float v = 0;
	/**
	 * die Farbe des Autos
	 */
	protected int farbe = -1;
	/**
	 * ob das Auto zurzeit mit der Wand kollidiert
	 */
	protected boolean kollision = false;
	/**
	 * die Karosserie des Autos
	 */
	protected PShape karosserie;
	/**
	 * die Scheiben und die Reifen des Autos
	 */
	protected PShape reifenScheiben;
	/**
	 * die Reifen des Autos
	 */
	protected Reifen[] reifen = new Reifen[4];

	/**
	 * Defaultkonstruktor
	 */
	public Auto() {
	}

	/**
	 * wandelt die übergebenen Pixel in Meter um
	 * 
	 * @param pixel
	 *            die in Meter umgewandelt werden sollen
	 * @return Meter, die den übergebenen Pixeln entsprechen
	 */
	private static float pixelToMeter(float pixel) {
		return pixel / METER_IN_PIXEL;
	}

	/**
	 * wandelt die übergebene x-Koordinate in die absolute x-Koordinate um
	 * 
	 * @param x
	 *            die in die absolute x-Koordinate umgewandelt werden soll
	 * @return absolute x-Koordinate der übergeben Koordinate
	 */
	public static float toAbsolutX(float x) {
		return 2145 - x;
	}

	/**
	 * wandelt die übergebene y-Koordinate in die absolute y-Koordinate um
	 * 
	 * @param y
	 *            die in die absolute y-Koordinate umgewandelt werden soll
	 * @return absolute y-Koordinate der übergeben Koordinate
	 */
	public static float toAbsolutY(float y) {
		return 2100 - y;
	}

	/**
	 * liefert die Farbe des Autos
	 * 
	 * @return die Farbe des Autos
	 */
	public int getFarbe() {
		return farbe;
	}

	/**
	 * liefert die absolute x-Koordinate des Autos auf der Strecke
	 * 
	 * @return die absolute x-Koordinate des Autos auf der Strecke
	 */
	public float getXAbsolut() {
		return toAbsolutX(x);
	}

	/**
	 * liefert die absolute y-Koordinate des Autos auf der Strecke
	 * 
	 * @return die absolute y-Koordinate des Autos auf der Strecke
	 */
	public float getYAbsolut() {
		return toAbsolutY(y);
	}

	/**
	 * liefert den aktuellen Kurvenwinkel des Autos
	 * 
	 * @return der aktuelle Kurvenwinkel des Autos
	 */
	public float getKurvenwinkel() {
		return kurvenwinkel;
	}

	/**
	 * liefert die y-Koordinate des Autos auf der Strecke von der Mitte der
	 * Strecke aus
	 * 
	 * @return die y-Koordinate des Autos auf der Strecke von der Mitte der
	 *         Strecke aus
	 */
	public float getY() {
		return y;
	}

	/**
	 * liefert die x-Koordinate des Autos auf der Strecke von der Mitte der
	 * Strecke aus
	 * 
	 * @return die x-Koordinate des Autos auf der Strecke von der Mitte der
	 *         Strecke aus
	 */
	public float getX() {
		return x;
	}

	/**
	 * liefert die Bodengegebenheit vorne links auf der übergebenen Strecke
	 * 
	 * @param strecke
	 *            auf der die Bodengegebenheit kontrolliert wird
	 * @return die Bodengegebenheit vorne links auf der übergebenen Strecke
	 */
	protected Bodenbegebenheit getBodenbegebenheitLinksVorne(Strecke strecke) {
		Bodenbegebenheit ret = strecke.getBodenbegebenheitBei(VORNE_LINKS,
				getXAbsolut(), getYAbsolut(), kurvenwinkel);
		return ret;
	}

	/**
	 * liefert die Bodengegebenheit vorne rechts auf der übergebenen Strecke
	 * 
	 * @param strecke
	 *            auf der die Bodengegebenheit kontrolliert wird
	 * @return die Bodengegebenheit vorne rechts auf der übergebenen Strecke
	 */
	protected Bodenbegebenheit getBodenbegebenheitRechtsVorne(Strecke strecke) {
		Bodenbegebenheit ret = strecke.getBodenbegebenheitBei(VORNE_RECHTS,
				getXAbsolut(), getYAbsolut(), kurvenwinkel);
		return ret;
	}

	/**
	 * liefert die Bodengegebenheit hinten links auf der übergebenen Strecke
	 * 
	 * @param strecke
	 *            auf der die Bodengegebenheit kontrolliert wird
	 * @return die Bodengegebenheit hinten links auf der übergebenen Strecke
	 */
	protected Bodenbegebenheit getBodenbegebenheitLinksHinten(Strecke strecke) {
		Bodenbegebenheit ret = strecke.getBodenbegebenheitBei(HINTEN_LINKS,
				getXAbsolut(), getYAbsolut(), kurvenwinkel);
		return ret;
	}

	/**
	 * liefert die Bodengegebenheit hinten rechts auf der übergebenen Strecke
	 * 
	 * @param strecke
	 *            auf der die Bodengegebenheit kontrolliert wird
	 * @return die Bodengegebenheit hinten rechts auf der übergebenen Strecke
	 */
	protected Bodenbegebenheit getBodenbegebenheitRechtsHinten(Strecke strecke) {
		Bodenbegebenheit ret = strecke.getBodenbegebenheitBei(HINTEN_RECHTS,
				getXAbsolut(), getYAbsolut(), kurvenwinkel);
		return ret;

	}

	/**
	 * liefert die aktuelle Position des Autos. Dabei kann ein Sektor sowie die
	 * Tatsache, ob sich das Auto im Schlamm, im Rasen oder im Rasen befindet
	 * zurückgeliefert werden
	 * 
	 * @param strecke
	 *            auf der nach der Position gesucht wird
	 * @return die aktuelle Position des Autos
	 */
	public Bodenbegebenheit getAktuelleBodenbegebenheit(Strecke strecke) {
		Bodenbegebenheit ret = getBodenbegebenheitLinksVorne(strecke);
		ret.merge(getBodenbegebenheitRechtsVorne(strecke));
		ret.merge(getBodenbegebenheitLinksHinten(strecke));
		ret.merge(getBodenbegebenheitRechtsHinten(strecke));
		if (!kollision && ret.isInDerMauer())
			kollision = true;
		return ret;
	}

	/**
	 * liefert den String, mit dem das Auto dem Server vermittele werden kann
	 * 
	 * @return der String, mit dem das Auto dem Server vermittele werden kann
	 */
	public String getNetzwerkString() {
		return Math.round(x) + ";" + Math.round(y) + ";" + kurvenwinkel + ";"
				+ v;
	}

	/**
	 * setzt das Auto an die übergeben x-Koordinate
	 * 
	 * @param x
	 *            Koordinate, an die das Auto gesetzt werden soll
	 */
	public void setX(float x) {
		synchronized (this) {
			this.x = x;
		}
	}

	/**
	 * setzt das Auto an die übergeben y-Koordinate
	 * 
	 * @param y
	 *            Koordinate, an die das Auto gesetzt werden soll
	 */
	public void setY(float y) {
		synchronized (this) {
			this.y = y;
		}
	}

	/**
	 * liefert zurück, ob das Auto zurzeit kollidiert
	 * 
	 * @return true, wenn das Auto mit der Wand kollidiert, ansonsten false
	 */
	public boolean isKollision() {
		return kollision;
	}

	/**
	 * setzt den aktuellen Kurvenradius des Autos
	 * 
	 * @param kurvenradius
	 *            den das Auto annehmen soll
	 */
	public void setKurvenradius(float kurvenradius) {
		synchronized (this) {
			this.kurvenwinkel = kurvenradius;
		}
	}

	/**
	 * liefert die Anzahl an Pixel, die sich das Auto in Abhängigkeit auf der
	 * Geschwindigkeit, der übergebenen Zeit und den aktuellen Kurvenradius in
	 * y-Richtung fortbewegt
	 * 
	 * @param deltaT
	 *            abhängige Zeit, in der sich das Auto fortbewegt
	 * @return Anzahl der Pixel, die sich das Auto in y-Richtung fortbewegt
	 */
	public float getyRichtung(float deltaT) {
		return vToPixel(deltaT) * PApplet.cos(kurvenwinkel);
	}

	/**
	 * liefert die Anzahl an Pixel, die sich das Auto in Abhängigkeit auf der
	 * Geschwindigkeit, der übergebenen Zeit und den aktuellen Kurvenradius in
	 * x-Richtung fortbewegt
	 * 
	 * @param deltaT
	 *            abhängige Zeit, in der sich das Auto fortbewegt
	 * @return Anzahl der Pixel, die sich das Auto in x-Richtung fortbewegt
	 */
	public float getxRichtung(float deltaT) {
		return vToPixel(deltaT) * PApplet.sin(kurvenwinkel);
	}

	/**
	 * gibt die Zeit in ms zurück, wie lange sich das Auto bereits im aktuellen
	 * Sektor befindet
	 * 
	 * @param oberflaeche
	 *            , von der die Stecke und das Zeitintervall hergenommen wird
	 * @return wie viele ms sich das Auto bereits in diesem Sektor befindet
	 */
	public long getZeitImSektor(Spieloberflaeche oberflaeche) {
		int i = 0;
		synchronized (this) {
			byte sektor = getAktuelleBodenbegebenheit(oberflaeche.getStrecke())
					.getSektor();
			float x = this.x;
			float y = this.y;
			this.x -= getxRichtung(oberflaeche.deltaT);
			this.y -= getyRichtung(oberflaeche.deltaT);
			float bis = Math.abs(Strecke.berechneC(
					Math.round(getxRichtung(oberflaeche.deltaT)),
					Math.round(getyRichtung(oberflaeche.deltaT))));
			for (; i < bis; i++) {
				this.x = x - getxRichtung(oberflaeche.deltaT) + v / Math.abs(v)
						* i * PApplet.sin(getKurvenwinkel());
				this.y = y - getyRichtung(oberflaeche.deltaT) + v / Math.abs(v)
						* i * PApplet.cos(getKurvenwinkel());
				if (getAktuelleBodenbegebenheit(oberflaeche.getStrecke())
						.getSektor() == sektor)
					bis = 0;
			}
			this.x = x;
			this.y = y;

		}
		return Math.round(1000F
				* pixelToMeter((Math.abs(Strecke.berechneC(
						Math.round(getxRichtung(oberflaeche.deltaT)),
						Math.round(getyRichtung(oberflaeche.deltaT)))) - i))
				/ v);
	}

	/**
	 * liefert die aktuelle Geschwindigkeit des Autos
	 * 
	 * @return die aktuelle Geschwindigkeit des Autos
	 */
	public float getGeschwindigkeit() {
		return v;
	}

	/**
	 * setzt die aktuelle Geschwindigkeit des Autos
	 * 
	 * @param v
	 *            die Geschwindigkeit, die das Auto annehmen soll
	 */
	public void setGeschwindigkeit(float v) {
		synchronized (this) {
			this.v = v;
		}
	}

	/**
	 * kontrolliert, ob der linke Vorderreifen sich auf der übergeben Strecke im
	 * Rasen befindet
	 * 
	 * @param strecke
	 *            auf der die Position des Reifens abgefragt wird
	 * @return true wenn der linke Vorderreifen sich auf der übergeben Strecke
	 *         im Rasen befindet, ansonsten false
	 */
	public boolean isLinksVorneImRasen(Strecke strecke) {
		return isImRasen(strecke, -22, -34, -13);
	}

	/**
	 * kontrolliert, ob der rechte Vorderreifen sich auf der übergeben Strecke
	 * im Rasen befindet
	 * 
	 * @param strecke
	 *            auf der die Position des Reifens abgefragt wird
	 * @return true wenn der rechte Vorderreifen sich auf der übergeben Strecke
	 *         im Rasen befindet, ansonsten false
	 */
	public boolean isRechtsVorneImRasen(Strecke strecke) {
		return isImRasen(strecke, -22, -34, 13);
	}

	/**
	 * kontrolliert, ob der linke Hinterreifen sich auf der übergeben Strecke im
	 * Rasen befindet
	 * 
	 * @param strecke
	 *            auf der die Position des Reifens abgefragt wird
	 * @return true wenn der linke Hinterreifen sich auf der übergeben Strecke
	 *         im Rasen befindet, ansonsten false
	 */
	public boolean isLinksHintenImRasen(Strecke strecke) {
		return isImRasen(strecke, 28, 39, -13);
	}

	/**
	 * kontrolliert, ob der rechte Hinterreifen sich auf der übergeben Strecke
	 * im Rasen befindet
	 * 
	 * @param strecke
	 *            auf der die Position des Reifens abgefragt wird
	 * @return true wenn der rechte Hinterreifen sich auf der übergeben Strecke
	 *         im Rasen befindet, ansonsten false
	 */
	public boolean isRechtsHintenImRasen(Strecke strecke) {
		return isImRasen(strecke, 28, 39, 13);
	}

	/**
	 * kontrolliert, ob sich das Auto auf den übergeben Koordinatenbereich im
	 * Rasen der übergeben Stecke befindet
	 * 
	 * @param strecke
	 *            auf der kontrolliert wird, ob sich das Auto im Rasen befindet
	 * @param yBeginn
	 *            Anfangs y-Koordinate der zu überprüfenden Stelle
	 * @param yEnde
	 *            Ende y-Koordinate der zu überprüfenden Stelle
	 * @param x
	 *            x-Koordinate der zu überprüfenden Stelle
	 * @return ob sich das Auto auf den übergeben Koordinatenbereich im Rasen
	 *         der übergeben Stecke befinde
	 */
	private boolean isImRasen(Strecke strecke, int yBeginn, int yEnde, int x) {
		boolean ret = true;
		if (yEnde < yBeginn) {
			int y = yEnde;
			yEnde = yBeginn;
			yBeginn = y;
		}
		try {
			for (int i = yBeginn; i <= yEnde && ret; i++) {
				float a = Strecke.berechneC(x, i);
				float winkel = PApplet.atan((float) x / (float) i)
						- kurvenwinkel;
				ret = strecke.getBodenbegebenheitBei(
						getXAbsolut() + (PApplet.sin(winkel) * a),
						getYAbsolut() - PApplet.cos(winkel) * a).isImGras();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * zeichnet das Auto auf der übergeben Oberfläche
	 * 
	 * @param oberflaeche
	 *            auf der das Auto gezeichnet wird
	 */
	public void display(Spieloberflaeche oberflaeche) {
		if (karosserie == null || reifenScheiben == null)
			init(oberflaeche);
		oberflaeche.anzeige3D.translate(-18, -15, 13);
		oberflaeche.anzeige3D.shapeMode(PApplet.CENTER);
		oberflaeche.anzeige3D.rotateZ(PApplet.PI);
		oberflaeche.anzeige3D.translate(2.8909735F, 0, 0);
		oberflaeche.anzeige3D.shape(karosserie);
		oberflaeche.anzeige3D.translate(-2.8909735F, 0, 0);
		oberflaeche.anzeige3D.translate(0, -10, 0);
		oberflaeche.anzeige3D.shape(reifenScheiben);
		oberflaeche.anzeige3D.translate(0, 10, 0);
		oberflaeche.anzeige3D.rotateZ(PApplet.PI);
		oberflaeche.anzeige3D.translate(18, 15, -13);
		oberflaeche.anzeige3D.rotate(getKurvenwinkel());
		oberflaeche.anzeige3D.translate(-2145 + x, -2099.5F + y, 0);
		for (Reifen reifen : this.reifen)
			reifen.display(oberflaeche, this);
		oberflaeche.anzeige3D.translate(2145 - x, 2099.5F - y, 0);
		oberflaeche.anzeige3D.rotate(-getKurvenwinkel());
	}

	/**
	 * wandelt die aktuelle Geschwindigkeit in Pixel um, die das Auto sich in
	 * Abhängigkeit der übergebenen Zeit fortbewegt
	 * 
	 * @param deltaT
	 *            Zeit in der sich das Auto fortbewegt
	 * @return Anzahl der Pixel, die das Auto sich in Abhängigkeit der
	 *         übergebenen Zeit fortbewegt
	 */
	public float vToPixel(float deltaT) {
		return METER_IN_PIXEL * v * deltaT / 1000F;
	}

	/**
	 * initialisiert die Bilder, die für das Anzeigen der Bilder notwendig sind
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void init(Spieloberflaeche oberflaeche) {
		karosserie = new PShape();
		karosserie = oberflaeche.anzeige3D.loadShape("karosserie.obj");
		karosserie.scale(10);
		karosserie.setFill(Farben.AUTO_FARBEN[farbe][0]);
		reifenScheiben = oberflaeche.anzeige3D.loadShape("reifenscheiben.obj");
		reifenScheiben.scale(10);
		reifenScheiben.setFill(0xFF000000);
		reifen[0] = new Reifen(-17, -33);
		reifen[1] = new Reifen(17, -33);
		reifen[2] = new Reifen(-17, 28);
		reifen[3] = new Reifen(17, 28);
	}

	public String toString() {
		return "x=" + x + " y=" + y + " rad=" + getKurvenwinkel();
	}

}