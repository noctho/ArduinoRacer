package net.tfobz.arduinoracer.util;

import java.awt.Color;

/**
 * Die Klasse Farben speichert fast alle Farben, die im Spiel vorkommen können
 * 
 * @author Thomas Nocker
 *
 */
public class Farben {

	public static final Color AUSGEWAEHLT = new Color(0x68025171, true);
	public static final Color GEKLICKT_UMRANDET = new Color(0x012432);
	public static final Color GEKLICKT = new Color(0x88012432, true);
	public static final int AMPELFARBE = 0xFFFF0000;
	public static final int ANZEIGETEXTFARBE = 0xFFFFFFFF;
	public static final int ANZEIGETEXTFARBE_GRUEN = 0xFF669900;
	public static final int ANZEIGETEXTFARBE_ROT = 0xFFCC0000;
	public static final int ANZEIGE_RAND = 0xC8012432;
	public static final int ANZEIGE_KOERPER = 0xC8025171;
	public static final int LETTN = 0xFF562200;

	/**
	 * Die Farben, die der Lack des Autos annehmen kann. Die Farben werden als
	 * Integerarray mit zwei Integern in einem Array gespeichert, wobei die
	 * erste Zahl die Farbe ist. Die zweite Zahl ist dieselbe Farbe etwas
	 * dunkler
	 */
	public static final int[][] AUTO_FARBEN = { { 0xFFFFFFFF, 0xFFD1D1D1 },
			{ 0xFFF4CC02, 0xFFC6A601 }, /* { 0xFF772238, 0xFF4C1623 }, */
			{ 0xFF93172E, 0xFF64101F }, /* { 0xFFB21911, 0xFF82100D }, */
			{ 0xFFCD1604, 0xFF9E1003 }, { 0xFF592C87, 0xFF492570 },
			{ 0xFFBC1662, 0xFF8C104A }, { 0xFF182F7D, 0xFF0A1435 },
			{ 0xFF4879BE, 0xFF325484 }, { 0xFF008692, 0xFF005C63 },
			{ 0xFF54CEB7, 0xFF4BB7A1 }, { 0xFF028427, 0xFF01551A },
			{ 0xFF64C43E, 0xFF4D932F }, { 0xFF585B60, 0xFF2C2E30 },
			{ 0xFFCACACC, 0xFF9E9E9E }, { 0xFFB4823B, 0xFF845F2B } };
}
