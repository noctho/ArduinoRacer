package net.tfobz.arduinoracer.spielmodi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.komponenten.MultiplayerAuto;
import net.tfobz.arduinoracer.komponenten.Auto;
import net.tfobz.arduinoracer.komponenten.anzeigen.MultiplayerAnzeige;
import net.tfobz.arduinoracer.menu.NeuesSpielPanel;
import net.tfobz.arduinoracer.menu.StartmenuPanel;
import net.tfobz.arduinoracer.runden.Position;
import net.tfobz.arduinoracer.runden.Runde;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;
import processing.core.PGraphics;

/**
 * Dieser Spielmodus wurde zur Demonstration eines Multiplayer-Spies ohne
 * verfügbaren Serverentwickelt
 * 
 * @author Thomas Nocker
 *
 */
public class Demonstration implements Spielmodi {

	/**
	 * Das Spiel, das abläuft
	 */
	private MultiplayerSpiel spiel = null;
	/**
	 * Der Spieler mit dem gespielt wird
	 */
	public MultiplayerSpieler multiplayerSpieler = null;
	/**
	 * Die Anzeige, die während dem Rennen angezeigt wird
	 */
	private MultiplayerAnzeige anzeige = null;
	/**
	 * Oberfläche des Spiels
	 */
	private Spieloberflaeche oberflaeche = null;

	/**
	 * die Rundeserien der einzellnen Spieler
	 */
	private Vector<Runde> rundenserien = new Vector<Runde>();
	
	/**
	 * die Runde des aktiven Spielers
	 */
	public Runde rundenserieSpieler = null;
	/**
	 * Namen der Spieler
	 */
	public static String[] namen = { "Sepp", "Andreas", "Dorothea", "Franz",
			"Rosa", "Alfred" };

	/**
	 * der aktive Spieler
	 */
	public static int aktiverSpieler = 1;

	/**
	 * Das Vaterpanel
	 */
	private NeuesSpielPanel parent;

	/**
	 * Konstruktor - legt fest ob der Spiel Spielersteller oder Spielbeitreter
	 * ist und erstellt einen Verbindungsaufnahme-Thread
	 * 
	 */
	@SuppressWarnings("resource")
	public Demonstration() {
		Vector<MultiplayerSpieler> spieler = new Vector<MultiplayerSpieler>();
		int i = 1;
		try {
			for (;; i++) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream("spieler" + i + ".bin")));
				MultiplayerSpieler multiSpieler = new MultiplayerSpieler(
						new MultiplayerAuto(i), i, namen[i - 1]);
				Runde runde = new Runde();
				String line = null;
				Position letztePosition = null;
				float deltaT = 0;
				while ((line = br.readLine()) != null) {
					float x = Float.parseFloat(line.substring(0,
							line.indexOf(";")));
					line = line.substring(line.indexOf(";") + 1);
					float y = Float.parseFloat(line.substring(0,
							line.indexOf(";")));
					line = line.substring(line.indexOf(";") + 1);
					float kurvenwinkel = Float.parseFloat(line.substring(0,
							line.indexOf(";")));
					line = line.substring(line.indexOf(";") + 1);
					long rundenzeit = Long.parseLong(line.substring(line
							.indexOf(";") + 1));
					float v = 0;
					if (letztePosition != null) {
						if (rundenzeit - letztePosition.getRundenzeit() != 0)
							deltaT = 0.001F * (rundenzeit - letztePosition
									.getRundenzeit());
						v = ((float) Math.sqrt(Math.pow(letztePosition.getX()
								- x, 2)
								+ Math.pow(letztePosition.getY() - y, 2)))
								/ (Auto.METER_IN_PIXEL * deltaT);
						if (v > 81)
							v = 0;
					}
					letztePosition = new Position(x, y, kurvenwinkel,
							rundenzeit, v);
					runde.addPosition(letztePosition);
				}
				if (runde.getPositionen().size() == 0) {
					throw new RuntimeException();
				}
				multiSpieler.getAuto().setX(-145);
				multiSpieler.getAuto().setY(-900);
				if (i == aktiverSpieler) {
					multiplayerSpieler = multiSpieler;
					rundenserieSpieler = runde;
				} else {
					rundenserien.add(runde);
					spieler.add(multiSpieler);
				}
				br.close();
			}
		} catch (Exception e) {
		}
		spiel = new MultiplayerSpiel(spieler.size(), 1, "Fag");
		for (MultiplayerSpieler multiSpieler : spieler)
			spiel.addSpieler(multiSpieler);
		spiel.setSpielersteller(multiplayerSpieler);
		setMultiplayerSpiel(spiel);
	}

	/**
	 * setzt die Spieloberfläche
	 * 
	 * @param oberflaeche
	 *            Spieloberfläche, die der Multiplayer annehmen soll
	 */
	public void setSpieloberflaeche(Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
	}

	/**
	 * setzt das Spiel des Multiplayers
	 * 
	 * @param spiel
	 *            das der Multiplayer annehmen soll
	 */
	public void setMultiplayerSpiel(MultiplayerSpiel spiel) {
		this.spiel = spiel;
		for (MultiplayerSpieler spieler : spiel.getAlleSpieler())
			spieler.setAnzahlRunden(spiel.getAnzahlRunden());
	}

	/**
	 * setzt den Start des Spiels, indem mit den übergebenen Millisekunden das
	 * Spiel gestartet wird
	 * 
	 * @param millis
	 *            Millisekunden mit denen das Spiel gestartet wird
	 */
	public void setStart(long millis) {
		spiel.setStart(millis);
	}

	/**
	 * liefert das Multiplayerspiel des Multiplayers
	 * 
	 * @return das Multiplayerspiel des Multiplayers
	 */
	public MultiplayerSpiel getMuliplayerSpiel() {
		return spiel;
	}

	/**
	 * setzt den Multiplayerspieler des Multiplayers, indem von allen Spielern
	 * des Spiels der Spieler mit der übergebenen ID gesucht wird
	 * 
	 * @param id
	 *            des Spielers, der Spieler des Multiplayer sein soll
	 */
	public void setMultiplayerSpieler(int id) {
		for (MultiplayerSpieler spieler : spiel.getAlleSpieler())
			if (spieler.getId() == id)
				this.multiplayerSpieler = spieler;
	}

	/**
	 * fügt zum übergebenen Dialog die Dialoge hinzu, die entweder dem
	 * Spielersteller oder dem Spielbeitreter entsprechen
	 */
	@Override
	public void getDialoge(NeuesSpielPanel parent) {
		this.parent = parent;
	}

	/**
	 * initialisiert das Spiel
	 * @param oberflaeche
	 */
	public void postInit(Spieloberflaeche oberflaeche) {
		parent.ok();
	}

	@Override
	public byte getCurrentSektor() {
		return multiplayerSpieler.getSektor();
	}

	/**
	 * unterbindet eine Pause während des Spiels und beendet das Spiel beim Ende
	 * des Rennens
	 */
	@Override
	public void setPauseknopfGedrueckt() {
		if (anzeige.getEndstandAnzeige().isAngezeigt()) {
			oberflaeche.getStartmenu().setAktuellesPanel(
					new StartmenuPanel(oberflaeche.getStartmenu()));
			oberflaeche.frame.dispose();
		}
	}

	/**
	 * verwaltet die Kommunikation mit den Gegnern
	 */
	@Override
	public void run() {
		try {
			oberflaeche.getAuto().setX(-167);
			oberflaeche.getAuto().setY(184);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * beginnt die Kommunikation mit den Gegnern
	 */
	@Override
	public void onStart(final Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
		oberflaeche.setZustand((byte) -4);
		oberflaeche.getAuto().setX(-167);
		oberflaeche.getAuto().setY(184);
		multiplayerSpieler.setAuto(new MultiplayerAuto(oberflaeche.getAuto()
				.getFarbe()));
		if (multiplayerSpieler.getAuto().getFarbe() != 0
				&& multiplayerSpieler.getAuto().getFarbe() < multiplayerSpieler
						.getId())
			spiel.getSpielerById(multiplayerSpieler.getAuto().getFarbe())
					.getAuto().setFarbe(0, oberflaeche);
		new Thread(new Runnable() {
			@Override
			public void run() {
				anzeige = new MultiplayerAnzeige(oberflaeche, spiel,
						multiplayerSpieler);
			}
		}).start();
	}

	/**
	 * beendet die Kommunikation mit den Gegnern
	 */
	@Override
	public void onStop() {
	}

	/**
	 * zeichnet alle Spieler in die Spieloberfläche und erfasst deren
	 * Rundenzeiten
	 */
	@Override
	public void onDraw(Bodenbegebenheit bodenbegebenheit, int anzahlReifenImGras) {
		for (int i = 0; i < spiel
				.getAlleSpielerOhne(multiplayerSpieler.getId()).size(); i++) {
			MultiplayerSpieler spieler = spiel.getAlleSpielerOhne(
					multiplayerSpieler.getId()).get(i);
			Position position = rundenserien
					.get(i)
					.getPositionBeiZeit(
							multiplayerSpieler.getRundenserie()
									.getSerienstart() == 0 ? 0
									: (oberflaeche.currentMillis - multiplayerSpieler
											.getRundenserie().getSerienstart()));
			spieler.getAuto().setX(position.getX());
			spieler.getAuto().setY(position.getY());
			spieler.getAuto().setKurvenradius(position.getKurvenwinkel());
			spieler.getAuto().display(oberflaeche);
			spieler.setAktuelleRundenzeit(oberflaeche);
		}
		try {
			multiplayerSpieler.getAuto().setX(oberflaeche.getAuto().getX());
			multiplayerSpieler.getAuto().setY(oberflaeche.getAuto().getY());
			multiplayerSpieler.getAuto().setKurvenradius(
					oberflaeche.getAuto().getKurvenwinkel());
			multiplayerSpieler.getAuto().setGeschwindigkeit(
					oberflaeche.getAuto().getGeschwindigkeit());
			multiplayerSpieler.setAktuelleRundenzeit(oberflaeche);
			// TODO speichern
			// out.println(multiplayerSpieler.getAuto().getNetzwerkString()
			// + ";"
			// + (multiplayerSpieler.getRundenserie().getSerienstart() == 0 ? 0
			// : (oberflaeche.currentMillis - multiplayerSpieler
			// .getRundenserie().getSerienstart())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (anzahlReifenImGras != -1) {
			for (MultiplayerSpieler spieler : spiel.getAlleSpieler())
				spieler.setRundenzeit(oberflaeche);
			anzeige.display(oberflaeche);
		}
	}

	/**
	 * zeigt alle Autos des Spiels in der übergebenen Minimap an
	 */
	@Override
	public void displayMinimap(PGraphics grafik) {
		try {
			for (MultiplayerSpieler spieler : spiel
					.getAlleSpielerOhne(multiplayerSpieler.getId())) {
				synchronized (spieler) {
					MultiplayerAuto auto = spieler.getAuto();
					grafik.translate(-(auto.getX() - oberflaeche.getAuto()
							.getX()) / 30, -(auto.getY() - oberflaeche
							.getAuto().getY()) / 30);
					grafik.rotate(-auto.getKurvenwinkel());
					grafik.image(auto.getPfeilele(), 0, 0);
					grafik.rotate(auto.getKurvenwinkel());
					grafik.translate((auto.getX() - oberflaeche.getAuto()
							.getX()) / 30, (auto.getY() - oberflaeche.getAuto()
							.getY()) / 30);
				}
			}
		} catch (Exception e) {
		}
	}

	@Override
	public String toString() {
		return "Demonstration";
	}

	/**
	 * liefert den aktiven Spieler
	 * @return der aktive Spieler
	 */
	public MultiplayerSpieler getSpieler() {
		return multiplayerSpieler;
	}

}
