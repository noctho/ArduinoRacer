package net.tfobz.arduinoracer.komponenten;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.steuerung.Steureung;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import processing.core.PApplet;

/**
 * zu den abstrakten Eigenschaften eines normalen Autos, besitzt die Klasse
 * SpielAuto noch die Möglichkeit des Fahrens. Dies wird erreicht, indem die
 * Eigenschaften der aktuellen Beschleunigung und des aktuellen Lenkwinkels
 * hinzukommen
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class SpielAuto extends Auto {
	/**
	 * Die Maximalgeschwindigkeit beim Vorwärtsfahren
	 */
	public static final int GESCHWINDIGKEITSBEGRENZER = 78;
	/**
	 * Die Maximalgeschwindigkeit beim Rückwärtsfahren
	 */
	private static final int RUECKWAERTSBEGRENZER = -30;
	/**
	 * Der Rollwiderstanskoeffizient Wert von Gummi auf Asphalt
	 */
	private static final float F_ROLL_STRASSE = 0.025F;
	/**
	 * Der Rollwiderstanskoeffizient Wert von Gummi auf Rasen
	 */
	private static final float F_ROLL_GRAS = 0.3F;
	/**
	 * Der Rollwiderstanskoeffizient Wert von Gummi auf Schlamm
	 */
	private static final float F_ROLL_SCHLAMM = 0.3F;
	/**
	 * Die Masse des Fahrzeugs
	 */
	private static final int M_FZ = 1454;
	/**
	 * Die Masse der Ladung des Fahrzeugs
	 */
	private static final int M_ZU = 85;
	/**
	 * Der aktuelle Lenkwinkel des Autos
	 */
	private float lenkWinkel = 0;
	/**
	 * Die aktuelle Beschleunigung des Autos
	 */
	private float a = 0;

	/**
	 * Konstruktor - setzt die Farbe des Autos
	 * 
	 * @param farbe
	 *            die das Auto annehmen soll
	 */
	public SpielAuto(int farbe) {
		this.farbe = farbe;
	}

	/**
	 * setzt die Beschleunigung des Autos
	 * 
	 * @param beschleunigung
	 *            die das Auto annehmen soll
	 */
	public void setBeschleunigung(int beschleunigung) {
		a = beschleunigung;
	}

	/**
	 * setzt den Lenkwinkel des Autos
	 * 
	 * @param lenkwinkel
	 *            den das Auto annehmen soll
	 */
	public void setLenkwinkel(float lenkwinkel) {
		lenkWinkel = lenkwinkel;
	}

	@Override
	public void display(Spieloberflaeche oberflaeche) {
		super.display(oberflaeche);
	}

	/**
	 * bringt das Auto zum Fahren. Dabei werden die Koordinaten sowie der
	 * Kurvenradius des Autos angepasst
	 * 
	 * @param anzahlReifenImRasen
	 *            Anzahl der Reifen des Autos im Rasen
	 * @param oberflaeche
	 *            beinhaltet u.a. das Zeitintervall, in dem gefahren wurde
	 */
	public void fahren(int anzahlReifenImRasen, Spieloberflaeche oberflaeche) {
		if (!kollision) {
			berechneGeschwindigkeit(anzahlReifenImRasen, oberflaeche);
			// System.out.println("v=" + (3.6 * v));
			// Die Rotation wird gesetzt
			kurvenwinkel += berechneLenkkung(oberflaeche.deltaT);
			// Die Rotation wird aufs Intervall zwischen - 2 pi und 2 pi
			// eingedemmt
			if (kurvenwinkel > PApplet.TWO_PI)
				kurvenwinkel -= PApplet.TWO_PI;
			else if (kurvenwinkel <= 0)
				kurvenwinkel += PApplet.TWO_PI;
			// Die y- und x-Richtungen werden gesetzt
			// deltaT *= (8F - anzahl) / 8F;
			for (int i = 0; i < (int) Math.abs(vToPixel(oberflaeche.deltaT)) / 50
					&& !kollision; i++) {
				x += 50 * v / Math.abs(v) * PApplet.sin(kurvenwinkel);
				y += 50 * v / Math.abs(v) * PApplet.cos(kurvenwinkel);
				getAktuelleBodenbegebenheit(oberflaeche);
			}
			// System.out.println(Math.abs(vToPixel(oberflaeche.deltaT))
			// / 50
			// + " "
			// + vToPixel(oberflaeche.deltaT)
			// + "\t"
			// + ((vToPixel(oberflaeche.deltaT) % 50) * PApplet
			// .sin(kurvenwinkel)) + " == "
			// + getxRichtung(oberflaeche.deltaT));
			if (!kollision) {
				x += (vToPixel(oberflaeche.deltaT) % 50)
						* PApplet.sin(kurvenwinkel);
				y += (vToPixel(oberflaeche.deltaT) % 50)
						* PApplet.cos(kurvenwinkel);
			}
			// x += getxRichtung(oberflaeche.deltaT);
			// y += getyRichtung(oberflaeche.deltaT);
		}
	}

	/**
	 * berechnet die Lenkung des Autos in Abhängigkeit der übergebenen Zeit.
	 * Dabei wird berücksichtigt, dass das Auto erst ab einer Geschwindigkeit
	 * von 0,5 m/s lenkt. Die volle Lenkung ist erst ab 8 m/s möglich, davor ist
	 * von 4 bis 8 m/s die Hälfte der Lenkung nur möglich und unter 4 m/s nur
	 * ein Viertel der Lenkung
	 * 
	 * @param deltaT
	 *            Zeit, in der das Auto lenkt
	 * @return Die Anzahl der Radianten, wie viel das Auto in Abhängigkeit der
	 *         übergeben Zeit gelenkt hat
	 */
	public float berechneLenkkung(float deltaT) {
		// bis 5 km/h wird nicht gelenkt
		float ret = 0;
		// ab mehr als 50 km/h wird die volle Lenkung eingesetzt
		if (Math.abs(v) > 3)
			ret = (lenkWinkel * deltaT) / 1000;
		// zwischen 20 km/h und 50 km/h werden 50% der Lenkung eingesetzt
		else if (Math.abs(v) >= 0.5)
			ret = (lenkWinkel * deltaT)
					/ (1000F + 3000F * (2.5F - (Math.abs(v) - 0.5F) / 2.5F));
		// zwischen 5 km/h und 20 km/h werden 25% der Lenkung eingesetzt
		if (v < 0)
			ret *= -1;
		return ret;
	}

	/**
	 * berechnet die Geschwindigkeit des Autos. Dabei werden alle
	 * Fahrwiderstände sowie eine künstliche Bremsung berücksichtigt in
	 * Abhängigkeit der Zeit, die als aktuelles Zeitintervall in der Oberfläche
	 * vorhanden ist
	 * 
	 * @param anzahlReifenImRasen
	 *            Anzahl der Reifen des Autos im Rasen
	 * @param oberflaeche
	 *            des Spiels
	 */
	public void berechneGeschwindigkeit(int anzahlReifenImRasen,
			Spieloberflaeche oberflaeche) {
		int anzahlReifenImSchlamm = 0;
		if (anzahlReifenImRasen > 0
				&& oberflaeche.getStrecke()
						.getBodenbegebenheitBei(getXAbsolut(), getYAbsolut())
						.isImSchlamm()) {
			anzahlReifenImSchlamm = anzahlReifenImRasen;
			anzahlReifenImRasen = 0;
		}
		int m = M_FZ + M_ZU;
		float a = this.a;
		float v = this.v;
		if (a < 0)
			if (v < 0)
				a = -Steureung.beschleunigung;
			else if (v < 100)
				a += 30;
			else if (v < 150)
				a += 30 - 30 * (v - 100) / 50;
		if (anzahlReifenImRasen > 0
				&& (v > GESCHWINDIGKEITSBEGRENZER - anzahlReifenImRasen * 17 || v < RUECKWAERTSBEGRENZER
						+ anzahlReifenImRasen * 5)) {
			a *= (4 - anzahlReifenImRasen) / 4F;
			anzahlReifenImRasen *= 2.5;
		} else if (anzahlReifenImSchlamm > 0)
			if (v > (GESCHWINDIGKEITSBEGRENZER - anzahlReifenImSchlamm * 19) / 1.5)
				v = (GESCHWINDIGKEITSBEGRENZER - anzahlReifenImSchlamm * 19) / 1.5F;
			else if (v < (RUECKWAERTSBEGRENZER + anzahlReifenImSchlamm * 7) / 1.5)
				v = (RUECKWAERTSBEGRENZER + anzahlReifenImSchlamm * 7) / 1.5F;
		float cw = anzahlReifenImRasen * F_ROLL_GRAS + anzahlReifenImSchlamm
				* F_ROLL_SCHLAMM
				+ (4 - anzahlReifenImRasen - anzahlReifenImSchlamm)
				* F_ROLL_STRASSE;
		float fluft = 0.6F * 0.29F * 2.1F * PApplet.pow(v / 3.6F, 2);
		float froll = 9.81F * m * cw;
		float fb = (0.01F * M_FZ + M_ZU) * a;
		float fw = fb + (fluft + froll) * (v < 0 || v == 0 && a < 0 ? -1 : 1);

		float ffahrt = a * m;

		float relativesA = (ffahrt - fw) / m;
		// System.out.println(relativesA);
		// System.out.println("ff=" + ffahrt + " fw=" + fw + " fl=" + fluft
		// + " fb=" + fb + " cw=" + cw + " v=" + (v * 3.6) + " a="
		// + relativesA);
		if (v != 0 || a != 0)
			v += relativesA * (oberflaeche.deltaT / 1000.0f);
		if (Math.abs(v) < 2 && a == 0)
			v = 0;
		if (v > GESCHWINDIGKEITSBEGRENZER)
			v = GESCHWINDIGKEITSBEGRENZER;
		else if (v < RUECKWAERTSBEGRENZER)
			v = RUECKWAERTSBEGRENZER;
		if (this.v > 0 && v < 0 || v > 0 && this.v < 0)
			v = 0;
		this.v = v;
	}

	/**
	 * liefert die aktuelle Position des Autos. Dabei kann ein Sektor sowie die
	 * Tatsache, ob sich das Auto im Schlamm, im Rasen oder im Rasen befindet
	 * zurückgeliefert werden. Bei einer Kollision wird das Auto genau so weit
	 * gesetz, dass gerade das Auto noch kollidiert. Anschließend wird das Auto
	 * absolute 10 Pixel vorgeschoben
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 * @return die aktuelle Position des Autos
	 */
	public Bodenbegebenheit getAktuelleBodenbegebenheit(
			final Spieloberflaeche oberflaeche) {
		boolean kollision = this.kollision;
		Bodenbegebenheit ret = super.getAktuelleBodenbegebenheit(oberflaeche
				.getStrecke());
		if (this.kollision && !kollision) {
			synchronized (this) {
				float x = this.x - getxRichtung(oberflaeche.deltaT);
				float y = this.y - getyRichtung(oberflaeche.deltaT);
				for (int i = 0; i < Math.abs(Strecke.berechneC(
						Math.round(getxRichtung(oberflaeche.deltaT)),
						Math.round(getyRichtung(oberflaeche.deltaT)))); i++)
					try {
						this.x = x + v / Math.abs(v) * i
								* PApplet.sin(getKurvenwinkel());
						this.y = y + v / Math.abs(v) * i
								* PApplet.cos(getKurvenwinkel());
						if (getAktuelleBodenbegebenheit(
								oberflaeche.getStrecke()).isInDerMauer()) {
							this.x = x + v / Math.abs(v) * (i + 10)
									* PApplet.sin(getKurvenwinkel());
							this.y = y + v / Math.abs(v) * (i + 10)
									* PApplet.cos(getKurvenwinkel());
							// System.out.println("true" + i);
							i = (int) Math
									.abs(Strecke.berechneC(
											Math.round(getxRichtung(oberflaeche.deltaT)),
											Math.round(getyRichtung(oberflaeche.deltaT))));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				new Thread(new Runnable() {
					@Override
					public void run() {
						synchronized (this) {
							try {
								this.wait(3000);
								synchronized (SpielAuto.this) {
									float[] checkpint = oberflaeche
											.getStrecke()
											.getCkeckpoint(
													oberflaeche.getSpielmodus()
															.getCurrentSektor());
									oberflaeche.getAuto().setTo(checkpint[0],
											checkpint[1], checkpint[2]);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
				v = 0;
			}
		}
		return ret;
	}

	/**
	 * liefert die Beschleunigung des Autos
	 * 
	 * @return die Beschleunigung des Autos
	 */
	public float getBeschleunigung() {
		return a;
	}

	/**
	 * setzt das Auto auf die übergebe Koordinate zum übergeben Kurvenwinkel
	 * 
	 * @param x
	 *            x-Koordinate auf die das Auto gesetzt werden soll
	 * @param y
	 *            y-Koordinate auf die das Auto gesetzt
	 * @param kurvernwinkel
	 *            den das Auto annehmen soll
	 */
	public void setTo(float x, float y, float kurvernwinkel) {
		this.x = x;
		this.y = y;
		this.kurvenwinkel = kurvernwinkel;
		lenkWinkel = 0;
		v = 0;
		a = 0;
		kollision = false;
		for(Reifen reifen: this.reifen)
			reifen.setTo(x, y, kurvernwinkel);
	}

	/**
	 * liefert die aktuelle Geschwindigkeit des Autos in km/h für die
	 * Geschwindigkeitsanzeige
	 * 
	 * @return die aktuelle Geschwindigkeit des Autos in km/h für die
	 *         Geschwindigkeitsanzeige
	 */
	public String getGeschwindigkeitKMpH() {
		return Math.abs(Math.round(getGeschwindigkeit() * 3.6)) + " km/h";
	}
}