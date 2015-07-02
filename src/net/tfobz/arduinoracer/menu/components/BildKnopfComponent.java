package net.tfobz.arduinoracer.menu.components;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import net.tfobz.arduinoracer.util.Farben;

/**
 * Die Klasse BildKnopfComponent ist ein Komponent, der ein Knopf darstellt,
 * welcher ein Bild beinhaltet.
 * 
 * @author noctho
 *
 */
@SuppressWarnings("serial")
public class BildKnopfComponent extends JComponent {

	/**
	 * Zeigt den Klick-Zustand des Bilds an
	 * 
	 * @author Thomas Nocker
	 *
	 */
	public static enum KlickZustand {
		NICHTS, AUSGEWAHLT, GEKLICKT
	}

	/**
	 * Das Bild, das im Knopf enthalten ist
	 */
	protected BufferedImage bild;
	/**
	 * Der aktuelle Klick-Zustand des Knopfs
	 */
	private KlickZustand klickZustand = KlickZustand.NICHTS;
	/**
	 * zeigt an, ob der Knopf ausgewählt ist, oder nicht
	 */
	private boolean ausgewaehlt = false;

	/**
	 * Konstruktor - setzt das Bild des Knopfs
	 * 
	 * @param file
	 *            des Bilds, das im Knopf angezeigt werden soll
	 */
	public BildKnopfComponent(File file) {
		try {
			setImage(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * setzt das Bild des Knopfes
	 * 
	 * @param file
	 *            des Bilds, das im Knopf angezeigt werden soll
	 * @throws IOException
	 *             Wenn ein Fehler beim Lesen des Bilds passiert
	 */
	protected void setImage(File file) throws IOException {
		if ((bild = ImageIO.read(file)) != null) {
			repaint();
		}
	}

	/**
	 * liefert das Bild im Knopf
	 * 
	 * @return das Bild im Knopf
	 */
	public BufferedImage getImage() {
		return bild;
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	protected void paintComponent(Graphics g) {
		if (bild != null) {
			g.drawImage(bild, (getWidth() - bild.getWidth()) / 2,
					(getHeight() - bild.getHeight()) / 2, bild.getWidth(),
					bild.getHeight(), null);
			switch (klickZustand) {
			case AUSGEWAHLT:
				g.setColor(Farben.AUSGEWAEHLT);
				g.fillRect(0, 0, getWidth(), getHeight());
				break;
			case GEKLICKT:
				g.setColor(Farben.GEKLICKT);
				g.fillRect(0, 0, getWidth(), getHeight());
				break;
			}
			if (ausgewaehlt) {
				g.setColor(Farben.GEKLICKT_UMRANDET);
				((Graphics2D) g).setStroke(new BasicStroke(10));
				g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
			}
		}
	}

	/**
	 * ändert den Klick-Zustand des Knopfs auf AUSGEWAEHLT
	 */
	public void setAusgewahlt() {
		setKlickZustand(KlickZustand.AUSGEWAHLT);
	}

	/**
	 * ändert den Klick-Zustand des Knopfs auf GEDRUECKT
	 */
	public void setGeklickt() {
		setKlickZustand(KlickZustand.GEKLICKT);
	}

	/**
	 * ändert den Klick-Zustand des Knopfs auf NICHTS
	 */
	public void setUnausgewaehlt() {
		setKlickZustand(KlickZustand.NICHTS);
	}

	/**
	 * ändert den Klick-Zustands des Knopfs
	 * 
	 * @param klickZustand
	 *            den der Knopf annehmen soll
	 */
	private void setKlickZustand(KlickZustand klickZustand) {
		this.klickZustand = klickZustand;
		repaint();
	}

	/**
	 * liefert zurück, ob der Knopf ausgewählt ist
	 * 
	 * @return true wenn der Knopf ausgewählt ist, ansonsten false
	 */
	public boolean isAusgewaehlt() {
		return ausgewaehlt;
	}

	/**
	 * setzt ob der Knopf ausgewählt ist oder nicht
	 * 
	 * @param ausgewaehlt
	 *            Ausgewählt-Zustand den der Knopf annehmen soll
	 */
	public void setAusgewaehlt(boolean ausgewaehlt) {
		this.ausgewaehlt = ausgewaehlt;
		repaint();
	}
}
