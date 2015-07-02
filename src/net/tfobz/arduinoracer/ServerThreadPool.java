package net.tfobz.arduinoracer;

import static net.tfobz.arduinoracer.util.Netzwerkstrings.AUTO;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.BEITRITTS_ABSAGE;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.BEITRITTS_BESTAETIGUNG;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.CLOSE;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIELER_BEREIT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_BEITRITT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_LOESCHUNG;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_OK;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_RUECKTRITT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_START;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.UMFRAGE_VERFUEGBARE_SPIELE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import net.tfobz.arduinoracer.komponenten.MultiplayerAuto;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;

/**
 * Die Klasse ServerThreadPool ist der Server, der notwendig ist um
 * Multiplayerspiele zu erstellen oder solchen beizutreten. Er verwaltet die
 * aktuellen Spiele, die ein Spieler erstellt hat bzw. bei denen ein Spieler
 * betreten kann. Zudem werden alle vorhandenen Verbindungen mit dem Server
 * verwaltet.
 * 
 * @author Thomas Nocker
 *
 */
public class ServerThreadPool extends Thread {
	/**
	 * Der Port, bei dem die Verbindungen stattfinden
	 */
	public static final int PORT = 47474;

	/**
	 * Alle zurzeit noch nicht gestarteten oder offen Spiele
	 */
	private Vector<MultiplayerSpiel> spiele = new Vector<MultiplayerSpiel>();
	/**
	 * Der Executor verwaltet die Verbindungen
	 */
	private Executor executor = null;
	/**
	 * Alle zurzeit offenen Verbindungen
	 */
	private Vector<ArduinoRacerConnectionThread> threads = new Vector<ServerThreadPool.ArduinoRacerConnectionThread>();
	/**
	 * Die aktuelle ID, die nach jeder Zuweisung eines Spielers oder eines
	 * Spiels inkrementiert wird
	 */
	private int currentID = 0;

	/**
	 * startet den Server
	 * 
	 * @param args
	 *            werden nicht berücksichtigt
	 */
	public static void main(String[] args) {
		new ServerThreadPool().start();
	}

	@Override
	public void run() {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(PORT);
			executor = Executors.newFixedThreadPool(100);
			while (true) {
				ArduinoRacerConnectionThread thread = new ArduinoRacerConnectionThread(
						serverSocket.accept());
				threads.add(thread);
				executor.execute(thread);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				serverSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * liefert das MultiplayerSpiel mit der übergebenen ID zurück
	 * 
	 * @param id
	 *            des gesuchten Multiplayerspiels
	 * @return Multiplayerspiel, der übergebenen id, null wenn die id nicht
	 *         gefunden wird
	 */
	public MultiplayerSpiel getSpielById(int id) {
		MultiplayerSpiel ret = null;

		for (MultiplayerSpiel spiel : spiele)
			if (spiel.getId() == id)
				ret = spiel;
		return ret;
	}

	/**
	 * sendet alle verfügbaren Spiele an alle offenen Verbindungen
	 */
	public void sendeVerfuegbareSpieleAnAlle() {
		System.out.println("alle" + spiele);
		for (ArduinoRacerConnectionThread thread : threads)
			try {
				if (thread.toReset)
					thread.out.reset();
				else
					thread.toReset = true;
				thread.out.writeObject(spiele);
				thread.out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * Die Klasse ArduinoRacerConnectionThread verwaltet die Verbindung eines
	 * Clients mit dem Server. Dabei werden die Eingaben abgefragt und
	 * dementsprechend reagiert
	 * 
	 * @author Thomas Nocker
	 *
	 */
	public class ArduinoRacerConnectionThread extends Thread {

		private Socket client = null;
		private ObjectOutputStream out = null;
		private BufferedReader in = null;
		/**
		 * Der Spieler, der der Verbindung zugeordnet ist
		 */
		private MultiplayerSpieler spieler = new MultiplayerSpieler();
		/**
		 * ob bereits einmal alle Spiele an den Client geschickt wurden und die
		 * Verbindung zuerst noch zurückgesetzt werden muss
		 */
		private boolean toReset = false;
		/**
		 * ob das Spiel des Spielers geladen ist
		 */
		private boolean bereit = false;

		/**
		 * legt eine neue Verbindung mit dem übergebenen Client an
		 * 
		 * @param client
		 *            mit dem die Verbindung angelegt wird
		 */
		public ArduinoRacerConnectionThread(Socket client) {
			this.client = client;
			try {
				out = new ObjectOutputStream(client.getOutputStream());
				in = new BufferedReader(new InputStreamReader(
						client.getInputStream()));
				System.out.println("conntected");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public String toString() {
			return spieler.getId() + "";
		}

		/**
		 * fragt die Eingaben des Clients ab und reagiert dementsprechend
		 */
		@Override
		public void run() {

			try {
				String zeile = null;
				while ((zeile = in.readLine()) != null && !zeile.equals(CLOSE)) {
					System.out.println(zeile);
					synchronized (spiele) {
						if (zeile.indexOf(UMFRAGE_VERFUEGBARE_SPIELE) == 0) {
							sendeVerfuegbareSpiele();
						} else if (zeile.indexOf(AUTO) == 0) {
							behandleSpielerdetails(zeile);
						} else if (zeile.indexOf(SPIEL_BEITRITT) == 0) {
							behandleBeitritt(zeile);
						} else if (zeile.indexOf(SPIEL_RUECKTRITT) == 0) {
							behandleRuecktritt(zeile);
						} else if (zeile.indexOf(SPIEL) == 0) {
							behandleNeuesSpiel(zeile);
						} else if (zeile.indexOf(SPIELER_BEREIT) == 0) {
							behandleBereitsein(zeile);
						} else if (zeile.indexOf(SPIEL_LOESCHUNG) == 0) {
							behandleSpielLoeschen(zeile);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
					in.close();
					client.close();
					threads.remove(this);
					System.out.println("close" + spiele.size());
					for (int i = 0; i < spiele.size(); i++) {
						if (spiele.get(i).getSpielersteller().getId() == spieler
								.getId()) {
							spiele.remove(i);
							i = spiele.size() + 1;
						} else
							System.err.println(spiele.get(i)
									.getSpielersteller().getId()
									+ " != " + spieler.getId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		/**
		 * setzt, dass der Client bereit zum Spielen ist. Wenn alle Clients
		 * bereit zum Spielen sind, wird allen ein Startsignal geschickt
		 * 
		 * @param zeile
		 *            Daten des Clients
		 */
		private void behandleBereitsein(String zeile) {
			bereit = true;
			int id = Integer.parseInt(zeile.substring(zeile.indexOf(";") + 1));
			MultiplayerSpiel spiel = getSpielById(id);
			spiel.addSpieler(spieler);
			boolean alleBereit = true;
			for (MultiplayerSpieler spieler : spiel.getAlleSpieler())
				for (ArduinoRacerConnectionThread thread : threads)
					if (spieler.getId() == thread.spieler.getId())
						alleBereit &= thread.bereit;
			System.out.println("ALLEBEREIT?" + alleBereit);
			if (alleBereit) {
				spiele.remove(spiel);
				try {
					sendeVerfuegbareSpieleAnAlle();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				for (MultiplayerSpieler spieler : spiel.getAlleSpieler())
					for (ArduinoRacerConnectionThread thread : threads)
						if (spieler.getId() == thread.spieler.getId()) {
							try {
								thread.out.writeObject(SPIEL_START);
								System.out.println(SPIEL_START);
								thread.out.flush();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
			}
		}

		/**
		 * passt die Daten (Auto, Name) des Spielers an und schickt diesem seine
		 * zugewiesene ID
		 * 
		 * @param zeile
		 *            Daten, auf die der Spieler angepasst werden soll
		 * @throws IOException
		 *             wenn das Senden der ID fehlschlägt
		 */
		private void behandleSpielerdetails(String zeile) throws IOException {
			spieler = new MultiplayerSpieler();
			spieler.setAuto(new MultiplayerAuto(Integer.parseInt(zeile
					.substring(AUTO.length(), zeile.indexOf(";")))));
			zeile = zeile.substring(zeile.indexOf(";") + 1);
			spieler.setName(zeile);
			synchronized (this) {
				spieler.setId(currentID++);
			}
			out.writeObject(spieler.getId());
		}

		/**
		 * fügt ein neues Spiel zu allen verfügbaren Spielen nach den
		 * übergebenen Daten hinzu und weißt diesem eine ID zu. Anschließend
		 * werden alle Spiele an alle Clients geschickt
		 * 
		 * @param zeile
		 *            Daten, nach denen das Spiel erstellt werden soll
		 */
		private void behandleNeuesSpiel(String zeile) {
			zeile = zeile.substring(zeile.indexOf(";") + 1);
			MultiplayerSpiel spiel = new MultiplayerSpiel();
			spiel.setAnzahlRunden(Integer.parseInt(zeile.substring(0,
					zeile.indexOf(";"))));
			zeile = zeile.substring(zeile.indexOf(";") + 1);
			spiel.setGesamtSpieler(Integer.parseInt(zeile.substring(0,
					zeile.indexOf(";"))));
			zeile = zeile.substring(zeile.indexOf(";") + 1);
			spiel.setSpielname(zeile);
			spiel.setSpielersteller(spieler);
			spiel.generateHost();
			System.out.println(spiel.getHost());
			synchronized (this) {
				spiel.setId(currentID++);
			}
			spiele.add(spiel);
			System.out.println(spiel);
			sendeVerfuegbareSpieleAnAlle();
		}

		/**
		 * löscht das Spiel, das den übergebenen Daten entspricht, jedoch nur,
		 * wenn der Client, der das Spiel löschen will das Spiel erstellt hat.
		 * Nachdem das Spiel gelöscht wird, werden alle Spiele an alle Clients
		 * geschickt
		 * 
		 * @param zeile
		 *            Daten, des zu löschenden Spiels
		 */
		private void behandleSpielLoeschen(String zeile) {
			zeile = zeile.substring(zeile.indexOf(";") + 1);
			MultiplayerSpiel spiel = getSpielById(Integer.parseInt(zeile));
			if (spiel.getSpielersteller().getId() == spieler.getId()) {
				spiele.remove(spiel);
				sendeVerfuegbareSpieleAnAlle();
			}
		}

		/**
		 * löscht den Spieler vom übergeben Spiel und sendet alle Spiele an
		 * alle.
		 * 
		 * @param zeile
		 *            Daten, des Spiels, bei dem der Spieler gelöscht werden
		 *            soll
		 */
		private void behandleRuecktritt(String zeile) {
			MultiplayerSpiel spiel = getSpielById(Integer.parseInt(zeile
					.substring(zeile.indexOf(";") + 1)));
			spiel.removeSpieler(spieler.getId());
			sendeVerfuegbareSpieleAnAlle();
		}

		/**
		 * behandelt einen Beitritt des Clients zu ein Spiel. Dabei werden die
		 * übergebenen Daten zu einem vorhanden Spiel zugewiesen und bei einem
		 * erfolglosen Beitritt eine dementsprechende Meldung geschickt.
		 * Ansonsten wir eine Beitrittsbestätigung geschickt.
		 * 
		 * @param zeile
		 *            Daten des Clients
		 * 
		 * @throws IOException
		 *             Wenn beim Senden ein Fehler auftritt
		 */
		private void behandleBeitritt(String zeile) throws IOException {
			System.out.println("behandleBeitritt");
			int id = Integer.parseInt(zeile.substring(zeile.indexOf(";") + 1));
			MultiplayerSpiel spiel = getSpielById(id);
			String grund = null;
			if (spiel == null)
				grund = "Teilnahme am Spiel fehlgeschlagen";
			else if (spiel.hasSpieler(spieler.getId()))
				grund = "Spieler mit Ihrer ID ist bereits im Spiel vorhanden";
			else if (spiel.hasAutofarbe(spieler.getAuto().getFarbe()))
				grund = "Spieler mit Ihrem Auto ist bereits im Spiel vorhanden";
			else if (spiel.hasSpielername(spieler.getName()))
				grund = "Spieler mit Ihrem Name ist bereits im Spiel vorhanden";
			else {
				spiel.addSpieler(spieler);
				out.writeObject(BEITRITTS_BESTAETIGUNG + ";" + id);
				out.flush();
				sendeVerfuegbareSpieleAnAlle();
				if (spiel.getGesamtSpieler() == spiel.getBegetetreteneSpieler()) {
					for (MultiplayerSpieler spieler : spiel.getSpieler())
						for (ArduinoRacerConnectionThread thread : threads)
							if (spieler.getId() == thread.spieler.getId()) {
								thread.out.writeObject(SPIEL_OK);
								System.out.println("SPIEL_PK");
								thread.out.flush();
							}
					System.out.println(spiel.getSpielersteller().getId()
							+ threads.toString());
					for (ArduinoRacerConnectionThread thread : threads)
						if (spiel.getSpielersteller().getId() == thread.spieler
								.getId()) {
							thread.out.writeObject(SPIEL_OK);
							System.out.println("SPIEL_OK TO SERVER");
						}
				}
			}
			if (grund != null) {
				out.writeObject(BEITRITTS_ABSAGE + ";" + id + ";" + grund);
				out.flush();
			}
		}

		/**
		 * sendet alle Spiele an den Client
		 * 
		 * @throws IOException
		 *             wenn die Sendung fehlschlägt
		 */
		private void sendeVerfuegbareSpiele() throws IOException {
			if (toReset)
				out.reset();
			else
				toReset = true;
			out.writeObject(spiele);
			out.flush();
			System.out.println("written");
		}

	}
}
