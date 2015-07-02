package net.tfobz.arduinoracer.spielmodi;

import static net.tfobz.arduinoracer.util.Netzwerkstrings.AUTO;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.VerbindungAufnahmeThread;
import net.tfobz.arduinoracer.komponenten.MultiplayerAuto;
import net.tfobz.arduinoracer.komponenten.anzeigen.MultiplayerAnzeige;
import net.tfobz.arduinoracer.menu.NeuesSpielPanel;
import net.tfobz.arduinoracer.menu.SpielBeitretenPanel;
import net.tfobz.arduinoracer.menu.SpieldetailsPanel;
import net.tfobz.arduinoracer.menu.StartmenuPanel;
import net.tfobz.arduinoracer.menu.WarteAufGegnerPanel;
import net.tfobz.arduinoracer.util.Bodenbegebenheit;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;
import processing.core.PGraphics;

/**
 * Die Klasse Multiplayer, die das Interface Spielmodi implementiert, verwaltet
 * jenen Spielmodus, der Rennfahren gegen Gegner im Netzwerk ermöglicht. Dabei
 * wird die festgelegt, ob der Spieler Spielersteller oder Spielbeitreter ist.
 * Das Spiel selbst und damit all seine Spieler werden verwaltet und eine
 * Kommunikation mit den ebenso. Es werden auch die nötigen Anzeigen für das
 * Spiel verwaltet
 * 
 * @author Thomas Nocker
 *
 */
public class Multiplayer implements Spielmodi {

	/**
	 * UDP-Socket, über das die Kommunikation mit den Gegnern abläuft
	 */
	private MulticastSocket socket = null;
	/**
	 * Spiel das verwaltet wird
	 */
	private MultiplayerSpiel spiel = null;
	/**
	 * Der Spieler mit dem gespielt wird
	 */
	private MultiplayerSpieler multiplayerSpieler = null;
	/**
	 * Die Anzeige, die während dem Rennen angezeigt wird
	 */
	private MultiplayerAnzeige anzeige = null;
	/**
	 * Jener Thread, mit dem die Verbindung zu den Gegnern aufgenommen wird
	 */
	private VerbindungAufnahmeThread verbindungAufnahmeThread = null;
	/**
	 * legt fest, ob der Spiel Spielersteller oder Spielbeitreter ist
	 */
	private boolean server;
	/**
	 * Oberfläche des Spiels
	 */
	private Spieloberflaeche oberflaeche = null;

	/**
	 * Konstruktor - legt fest ob der Spiel Spielersteller oder Spielbeitreter
	 * ist und erstellt einen Verbindungsaufnahme-Thread
	 * 
	 * @param server
	 *            true, wenn Spieler Spielersteller ist, ansonsten false
	 */
	public Multiplayer(boolean server) {
		this.server = server;
		verbindungAufnahmeThread = new VerbindungAufnahmeThread(this);
	}

	/**
	 * liefert den Verbindungsaufnahme-Thread
	 * 
	 * @return der Verbindungsaufnahme-Thread
	 */
	public VerbindungAufnahmeThread getVerbindungAufnahmeThread() {
		return verbindungAufnahmeThread;
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
		if (server)
			new WarteAufGegnerPanel(new SpieldetailsPanel(parent), this);
		else
			new SpielBeitretenPanel(parent, this);
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

			while (true) {

				byte[] buf = new byte[256];

				DatagramPacket answer = new DatagramPacket(buf, buf.length);
				socket.receive(answer);

				String received = new String(answer.getData());
				if (received.indexOf(AUTO) == 0
						&& received.indexOf("" + spiel.getId()) == AUTO
								.length()) {
					received = received.substring(received.indexOf(";") + 1);
					int id = Integer.parseInt(received.substring(0,
							received.indexOf(";")));
					if (id != multiplayerSpieler.getId()) {
						MultiplayerSpieler spieler = spiel
								.getSpielerById(Integer.parseInt(received
										.substring(0, received.indexOf(";"))));
						// System.out.println(received);
						synchronized (spieler) {
							received = received.substring(
									received.indexOf(";") + 1).trim();
							spieler.getAuto().setX(
									Integer.parseInt(received.substring(0,
											received.indexOf(";"))));
							received = received
									.substring(received.indexOf(";") + 1);
							spieler.getAuto().setY(
									Integer.parseInt(received.substring(0,
											received.indexOf(";"))));
							received = received
									.substring(received.indexOf(";") + 1);
							spieler.getAuto().setKurvenradius(
									Float.parseFloat(received.substring(0,
											received.indexOf(";"))));
							received = received
									.substring(received.indexOf(";") + 1);
							spieler.getAuto().setGeschwindigkeit(
									Float.parseFloat(received.substring(0,
											received.indexOf(";"))));
							spieler.setAktuelleRundenzeit(Long
									.parseLong(received.substring(received
											.indexOf(";") + 1)));
						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * sendet die Übergebene Nachricht an alle Gegner
	 * 
	 * @param text
	 *            der an alle Gegner
	 * @throws IOException
	 *             Wenn ein Fehler beim Senden passiert
	 */
	public void sende(String text) throws IOException {
		byte[] buf = text.getBytes();
		InetAddress address = InetAddress.getByName(spiel.getHost());
		DatagramPacket query = new DatagramPacket(buf, buf.length, address,
				44747);
		socket.send(query);
	}

	/**
	 * beginnt die Kommunikation mit den Gegnern
	 */
	@Override
	public void onStart(final Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
		oberflaeche.getAuto().setX(-145);
		oberflaeche.getAuto().setY(-900);
		// for (MultiplayerSpieler spieler : spiel.getSpieler())
		// spieler.getAuto().setFarbe(oberflaeche);
		try {
			socket = new MulticastSocket(44747);
			InetAddress group = InetAddress.getByName(spiel.getHost());
			socket.joinGroup(group);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		try {
			socket.leaveGroup(InetAddress.getByName(spiel.getHost()));
			socket.close();
		} catch (Exception e) {
		}
	}

	/**
	 * zeichnet alle Spieler in die Spieloberfläche und erfasst deren
	 * Rundenzeiten
	 */
	@Override
	public void onDraw(Bodenbegebenheit bodenbegebenheit, int anzahlReifenImGras) {
		for (MultiplayerSpieler spieler : spiel
				.getAlleSpielerOhne(multiplayerSpieler.getId()))
			spieler.getAuto().display(oberflaeche);
		try {
			multiplayerSpieler.getAuto().setX(oberflaeche.getAuto().getX());
			multiplayerSpieler.getAuto().setY(oberflaeche.getAuto().getY());
			multiplayerSpieler.getAuto().setKurvenradius(
					oberflaeche.getAuto().getKurvenwinkel());
			multiplayerSpieler.getAuto().setGeschwindigkeit(
					oberflaeche.getAuto().getGeschwindigkeit());
			multiplayerSpieler.setAktuelleRundenzeit(oberflaeche);
			sende(AUTO + spiel.getId() + ";"
					+ multiplayerSpieler.getNetzwerkString(oberflaeche));

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
		return "Multiplayer";
	}
}
