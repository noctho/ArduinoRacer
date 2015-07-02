package net.tfobz.arduinoracer.menu.components;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Die Klasse BildKnopfAdapter ist ein MouseListener, der bei den
 * Bild-Knopf-Adapter eingesetzt werden kann
 * 
 * @author Thomas Nocker
 *
 */
public class BildKnopfAdapter implements MouseListener {

	/**
	 * Alle BildKnopfComponentent, die den Listener haben sollen
	 */
	private BildKnopfComponent[] bildkoBildKnopfs;

	/**
	 * Konstruktor - setzt die jeweiligen BildKnopf-Componenten
	 * @param bildkoBildKnopfs die der Adapter verwalten soll
	 */
	public BildKnopfAdapter(BildKnopfComponent[] bildkoBildKnopfs) {
		this.bildkoBildKnopfs = bildkoBildKnopfs;
		for (BildKnopfComponent component : bildkoBildKnopfs)
			component.addMouseListener(this);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		BildKnopfComponent component = (BildKnopfComponent) e.getSource();
		for (BildKnopfComponent component2 : bildkoBildKnopfs)
			if (component2.isAusgewaehlt())
				component2.setAusgewaehlt(false);
		component.setAusgewaehlt(true);
		component.setAusgewahlt();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		((BildKnopfComponent) e.getSource()).setGeklickt();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		((BildKnopfComponent) e.getSource()).setUnausgewaehlt();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		((BildKnopfComponent) e.getSource()).setAusgewahlt();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * setzt, dass die Maus von allen Knöpfen verschwindet
	 */
	public void exitToAll() {
		for(BildKnopfComponent component: bildkoBildKnopfs)
			component.setUnausgewaehlt();
	}

}
