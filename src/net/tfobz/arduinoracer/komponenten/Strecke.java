package net.tfobz.arduinoracer.komponenten;

import java.io.File;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Die Klasse Strecke stellt eine Rennstrecke dar. Sie besteht aus einem Array
 * aus Bildern, die Koordinaten zugewiesen sind. Des weiteren besitzen sie ein
 * Array aus Bodengegebenheiten, damit jeder Pixel einer Bodengegebenheit
 * zugeordnet ist. Dazu kommt noch für jeden Sektor ein Checkpoint, auf dem man
 * im Falle einer Kollision in jenem Sektor zurückgesetzt wird. Dieser
 * Checkpoint besteht aus einer x-, einer y-Koordinate und dem Kurvenradius
 * 
 * @author Thomas Nocker
 *
 */
public class Strecke {

	/**
	 * Die Checkpoints, auf die das Auto im Falle einer Kollision zurückgesetzt
	 * wird. Sie bestehen aus einer x-, einer y-Koordinate und dem Kurvenradius
	 */
	private float[][] ckeckpoints = { { 2304, 1846, 0 }, { 2304, 1210, 0 },
			{ 2447, 488, 5.23F }, { 3109, 291, 5 }, { 3620, 648, 2 },
			{ 2977, 884, 2 }, { 2704, 1520, 3.85F }, { 3192, 1877, 4.55F },
			{ 3861, 1984, 4 }, { 4042, 2466, 3.14F }, { 4032, 2940, 2.8F },
			{ 3709, 3608, 2.35F }, { 2672, 3955, 1.57F },
			{ 1215, 3955, 1.54F }, { 286, 3707, 0.46F }, { 186, 2835, 6 },
			{ 567, 2916, 2.8F }, { 977, 3316, 6.1F }, { 1056, 2764, 0 },
			{ 901, 1906, 0.8F }, { 445, 1412, 0.46F }, { 236, 519, 6 },
			{ 601, 203, 4.6F }, { 971, 635, 3 }, { 888, 1227, 3.1F },
			{ 1314, 1364, 5.9F }, { 1491, 834, 5.8F }, { 1907, 666, 3.32F },
			{ 1952, 1297, 3.14F }, { 1952, 2106, 3.14F },
			{ 1952, 3197, 3.14F }, { 2319, 3215, 0 }, { 2319, 2524, 0 } };
	
	private static final int[] farben = { 0xFF0000, 0xEC0000, 0xD90000, 0xC60000, 0xB30000,
			0xA00000, 0x8D0000, 0x7A0000, 0x670000, 0x540000, 0x410000,
			0x00FF00, 0x00EC00, 0x00D900, 0x00C600, 0x00B300, 0x00A000,
			0x008D00, 0x007A00, 0x006700, 0x005400, 0x004100, 0x0000FF,
			0x0000EC, 0x0000D9, 0x0000C6, 0x0000B3, 0x0000A0, 0x00008D,
			0x00007A, 0x000067, 0x000054, 0x000041 };
	/**
	 * Die Bilder, wie die Rennstrecke aussieht
	 */
	private PImage[] bilder = new PImage[7];
	/**
	 * Die Koordinate, an denen die Bilder der Rennstrecke dargestellt werden
	 */
	private int[][] koordinaten = { { 1748, 351 }, { 2434, 74 },
			{ 2667, 1664 }, { 3259, 2087 }, { 160, 3705 }, { 0, 1895 },
			{ 118, 93 } };
	/**
	 * Die Bodengegebenheiten für jedem Pixel der Rennstrecke
	 */
	private byte[][] bodenbegebenheiten = null;

	/**
	 * Konstruktor - ladet die Bilder und die Bodengegebenheiten
	 * 
	 * @param oberflaeche
	 *            des Spiels
	 */
	public Strecke(Spieloberflaeche oberflaeche) {
		try {
			for (int i = 0; i < bilder.length; i++)
				bilder[i] = oberflaeche.loadImage("strecke" + File.separator
						+ "strecke" + (i + 1) + ".png");
			// bild = new PImage(ImageIO.read("reifenstapelneuifag.png");
			PImage bild = oberflaeche.loadImage("reifenstapelneuifag.png");
			System.out.println(bild.height);
			System.out.println(bild.width);
			try {
				String text = "";
				bodenbegebenheiten = new byte[bild.width][bild.width];
				for (int i = 0; i < bild.height; i++) {
					for (int j = 0; j < bild.width; j++)
						switch (bild.get(j, i)) {
						case 0xff000000:
							bodenbegebenheiten[i][j] = -1;
							break;
						default:
							bodenbegebenheiten[i][j] = indexOf(bild.get(j, i));
							if (bodenbegebenheiten[i][j] == -2)
								System.err.println("bigfault:x=" + j + " y=" + i);
						}
				}
				System.out.println("länge=" + text.length());
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		} catch (Exception e) {

		}
	}

	/**
	 * berechnet anhand des Satzes von Pythagoras die Hypotenuse c aus den
	 * übergeben Katheten a und b
	 * 
	 * @param a
	 *            Kathete, von der die Hypotenuse berechnet werden soll
	 * @param b
	 *            Kathete, von der die Hypotenuse berechnet werden soll
	 * @return Hypotenuse aus a und b
	 */
	public static float berechneC(float a, float b) {
		float ret = PApplet.sqrt(PApplet.pow(a, 2) + PApplet.pow(b, 2));
		if (b > 0)
			ret *= -1;
		return ret;
	}

	/**
	 * liefert die Bodengegebenheit auf der Strecke bei der übergebenen
	 * Koordinate
	 * 
	 * @param x
	 *            x-Koordinate, an der die Bodengegebenheit abgefragt wird
	 * @param y
	 *            y-Koordinate, an der die Bodengegebenheit abgefragt wird
	 * @return die Bodengegebenheit auf der Strecke bei der übergebenen
	 *         Koordinate
	 */
	public Bodenbegebenheit getBodenbegebenheitBei(float x, float y) {
		Bodenbegebenheit ret = null;
		try {
			ret = new Bodenbegebenheit(
					bodenbegebenheiten[Math.round(y)][Math.round(x)]);
		} catch (Exception e) {
			ret = new Bodenbegebenheit((byte) -1);
		}
		return ret;
	}

	/**
	 * zeichnet die Strecke in die 3D-Anzeige der Spieloberfläche, die um die
	 * übergebenen x- und y-Koordinaten verschoben ist
	 * 
	 * @param oberflaeche
	 *            auf dessen 3D-Anzeige die Strecke gezeichnet wird
	 * 
	 * @param x
	 *            Verschiebung der Strecke um die x-Achse
	 * @param y
	 *            Verschiebung der Strecke um die y-Achse
	 */
	public void display(Spieloberflaeche oberflaeche, float x, float y) {
		oberflaeche.anzeige3D.noStroke();
		oberflaeche.anzeige3D.lights();
		oberflaeche.anzeige3D.translate(-2145 + x, -2099.5F + y, 0);
		oberflaeche.anzeige3D.imageMode(PApplet.CORNER);
		oberflaeche.anzeige3D.rectMode(PApplet.CORNER);
		oberflaeche.anzeige3D.fill(0x00FFFFFF);
		oberflaeche.anzeige3D.translate(0, 0, -2);
		oberflaeche.anzeige3D.rect(-100, -100, bodenbegebenheiten.length + 200,
				bodenbegebenheiten[0].length + 200);
		oberflaeche.anzeige3D.translate(0, 0, 1);
		oberflaeche.anzeige3D.fill(0xFFC0C0C0);
		for (int i = 0; i < bilder.length; i++)
			oberflaeche.anzeige3D.image(bilder[i], koordinaten[i][0],
					koordinaten[i][1]);
		oberflaeche.anzeige3D.imageMode(PApplet.CENTER);
		oberflaeche.anzeige3D.translate(2145, 26, 0);
		oberflaeche.anzeige3D.box(4290, 52, 100);
		oberflaeche.anzeige3D.translate(0, 4199 - 52, 0);
		oberflaeche.anzeige3D.box(4290, 52, 100);
		oberflaeche.anzeige3D.translate(0, -4199 + 52, 0);
		oberflaeche.anzeige3D.translate(-2145, -26, 0);
		oberflaeche.anzeige3D.translate(26, 2099.5F, 0);
		oberflaeche.anzeige3D.box(52, 4199, 100);
		oberflaeche.anzeige3D.translate(4290 - 52, 0, 0);
		oberflaeche.anzeige3D.box(52, 4199, 100);
		oberflaeche.anzeige3D.translate(-4290 + 52, 0, 0);
		oberflaeche.anzeige3D.translate(-26, -2099.5F, 0);
		oberflaeche.anzeige3D.translate(1517, 3763, 0);
		oberflaeche.anzeige3D.box(1884, 26, 100);
		oberflaeche.anzeige3D.translate(-1517, -3763, 0);
		oberflaeche.anzeige3D.translate(1104F, 727.5F, 0);
		oberflaeche.anzeige3D.box(26, 1455, 100);
		oberflaeche.anzeige3D.translate(-1104F, -727.5F, 0);
		oberflaeche.anzeige3D.translate(2158F, 1715.5F, 0);
		oberflaeche.anzeige3D.box(26, 3431, 100);
		oberflaeche.anzeige3D.translate(-2158, -1715.5F, 0);
		oberflaeche.anzeige3D.translate(357, 2196, 0);
		oberflaeche.anzeige3D.box(714, 26, 100);
		oberflaeche.anzeige3D.translate(-357, -2196, 0);
		oberflaeche.anzeige3D.translate(909, 2658, 0);
		oberflaeche.anzeige3D.box(26, 532, 100);
		oberflaeche.anzeige3D.translate(-909, -2658, 0);
		oberflaeche.anzeige3D.translate(379, 3094, 0);
		oberflaeche.anzeige3D.box(26, 948, 100);
		oberflaeche.anzeige3D.translate(-379, -3094, 0);
		oberflaeche.anzeige3D.translate(678, 915, 0);
		oberflaeche.anzeige3D.box(26, 1130, 100);
		oberflaeche.anzeige3D.translate(-678, -915, 0);
		oberflaeche.anzeige3D.translate(1199, 2853.5F, 0);
		oberflaeche.anzeige3D.box(26, 1843, 100);
		oberflaeche.anzeige3D.translate(-1199, -2853.5F, 0);
		oberflaeche.anzeige3D.translate(1797, 2255.5F, 0);
		oberflaeche.anzeige3D.box(26, 3041, 100);
		oberflaeche.anzeige3D.translate(-1797, -2255.5F, 0);
		oberflaeche.anzeige3D.translate(2447, 2307.5F, 0);
		oberflaeche.anzeige3D.box(26, 2937, 100);
		oberflaeche.anzeige3D.translate(-2447, -2307.5F, 0);
		oberflaeche.box(367, 575, 3556, 3763);
		oberflaeche.box(2434, 3672, 851, 458);
		oberflaeche.box(714, 921, 2196, 2405);
		oberflaeche.box(665, 1211, 1466, 1945);
		oberflaeche.anzeige3D.translate(2145 - x, 2099.5F - y, 0);
	}

	/**
	 * gibt eine zusammenfassende Bodengegebenheit auf der Strecke, die auf den
	 * Punkten den übergebenen Koordinaten, die den Abstand zur übergebenen
	 * Koordinate bilden, befinden. Dabei wird immer der größte Sektor genommen
	 * sowie, falls eine Koordinate irgendwo kollidiert, immer die Kollision
	 * 
	 * @param kooerdinaten
	 *            die von der übergebenen Koordinate entfernt sind
	 * @param x
	 *            x-Koordinate von der die Entfernung gemessen wird
	 * @param y
	 *            y-Koordinate von der die Entfernung gemessen wird
	 * @param kurvenwinkel
	 *            des Abstandes der Koordinaten
	 * @return die Zusammenfassende Bodengegebenheit der übergebenen Koordinaten
	 */
	public Bodenbegebenheit getBodenbegebenheitBei(int[][] kooerdinaten,
			float x, float y, float kurvenwinkel) {
		float a = berechneC(kooerdinaten[0][0], kooerdinaten[0][1]);
		float winkel = PApplet.atan((float) kooerdinaten[0][0]
				/ (float) kooerdinaten[0][1])
				- kurvenwinkel;
		Bodenbegebenheit ret = null;
		ret = getBodenbegebenheitBei(x + (PApplet.sin(winkel) * a),
				y - PApplet.cos(winkel) * a);
		for (int i = 1; i < kooerdinaten.length && !ret.isInDerMauer(); i++) {
			try {
				a = berechneC(kooerdinaten[i][0], kooerdinaten[i][1]);
				winkel = PApplet.atan((float) kooerdinaten[i][0]
						/ (float) kooerdinaten[i][1])
						- kurvenwinkel;
				ret.merge(getBodenbegebenheitBei(x + (PApplet.sin(winkel) * a),
						y - PApplet.cos(winkel) * a));
			} catch (Exception e) {
				System.out.println((x + (PApplet.sin(winkel) * a)) + ","
						+ (y - PApplet.cos(winkel) * a));
				e.printStackTrace();
			}
		}
		return ret;
	}

	/**
	 * liefert den Checkpoint des übergebenen Sektors
	 * 
	 * @param sektor
	 *            von dem der Checkpoint gesucht wird
	 * @return der Checkpoint des übergebenen Sektors
	 */
	public float[] getCkeckpoint(byte sektor) {
		float[] ret = ckeckpoints[sektor].clone();
		ret[0] = 2145 - ret[0];
		ret[1] = 2100 - ret[1];
		return ret;
	}

	private byte indexOf(int value) {
		for (byte i = 0; i < farben.length; i++)
			if ((farben[i] | 0xFF000000) == value)
				return i;
			else if ((farben[i] | 0x80000000) == value)
				return (byte) (i + 33);
		return -2;
	}
}
