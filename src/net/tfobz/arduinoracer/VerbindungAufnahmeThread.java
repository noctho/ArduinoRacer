package net.tfobz.arduinoracer;

import static net.tfobz.arduinoracer.util.Netzwerkstrings.BEITRITTS_ABSAGE;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.BEITRITTS_BESTAETIGUNG;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.CLOSE;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.IP_ADRESSE;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIELER_BEREIT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_BEITRITT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_LOESCHUNG;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_OK;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_RUECKTRITT;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.SPIEL_START;
import static net.tfobz.arduinoracer.util.Netzwerkstrings.UMFRAGE_VERFUEGBARE_SPIELE;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Vector;

import net.tfobz.arduinoracer.spielmodi.Multiplayer;
import net.tfobz.arduinoracer.util.MultiplayerSpiel;
import net.tfobz.arduinoracer.util.MultiplayerSpieler;

/**
 * Die Klasse VerbindungsAufnahmeThread ist dafür verantwortlich, dass bei einem
 * Multiplayerspiel die Verbindung mit dem Server aufgenommen wird. Dabei ist es
 * egal, ob der Spieler, der eine Verbindung aufnimmt, dies als Spielersteller
 * oder als Spielbeitreter tut.
 * 
 * @author Thomas Nocker
 *
 */
public class VerbindungAufnahmeThread extends Thread {

	/**
	 * Streams mit dem Server
	 */
	private OutputStreamWriter out = null;
	private ObjectInputStream in = null;
	/**
	 * die zuletzt erhaltene Nachricht
	 */
	private String nachricht = null;
	/**
	 * die Liste aller verfügbaren Spiele
	 */
	private Vector<MultiplayerSpiel> spiele = null;
	/**
	 * Socket für den Verbindungsaufbau
	 */
	private Socket socket = null;
	/**
	 * der Spieler, der die Verbindung aufnimmt
	 */
	private MultiplayerSpieler spieler = null;
	/**
	 * Listener, der auf die gelesenen Daten vom Server reagiert
	 */
	private ActionListener listener = null;
	/**
	 * der Spielmodus Multiplayer, mit dem gespielt wird
	 */
	private Multiplayer multiplayer = null;
	/**
	 * der Zeitpunkt, an dem das Spiel startet
	 */
	private long zeitpunkt;

	/**
	 * nimmt die Verbindung mit dem Server auf und startet den Thread
	 * 
	 * @param multiplayer
	 *            Spielmodus des Spielers
	 */
	public VerbindungAufnahmeThread(Multiplayer multiplayer) {
		this.multiplayer = multiplayer;
		try {
			socket = new Socket(IP_ADRESSE, ServerThreadPool.PORT);
			out = new OutputStreamWriter(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Fehler: " + e.toString());
		}
		start();
	}

	/**
	 * ließt alle hereinkommende Daten vom Server und reagiert danach
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Object read;
						boolean closed = false;
						while (!closed && (read = in.readObject()) != null) {
							try {
								if (read instanceof Vector) {
									spiele = (Vector<MultiplayerSpiel>) read;
									listener.actionPerformed(null);
									System.err.println("listened" + read);
								} else {
									System.err.println(read);
									if (read.toString().indexOf(SPIEL_OK) == 0) {
										System.out.println("Spieok");
										for (MultiplayerSpiel spiel : spiele)
											if (spiel.hasSpieler(spieler
													.getId())) {
												System.out
														.println("hasspieler");
												multiplayer
														.setMultiplayerSpiel(spiel);
												System.out
														.println("multiplayerSpielset");
												multiplayer
														.setMultiplayerSpieler(spieler
																.getId());
												System.out
														.println("multiplayerSpieerlset");
												listener.actionPerformed(new ActionEvent(
														spiel, spiel.getId(),
														read.toString()));
												System.out
														.println("actionListened");
											}
									} else if (read instanceof Integer)
										spieler.setId((int) read);
									else {
										if (read.toString()
												.indexOf(SPIEL_START) == 0) {
											zeitpunkt = System
													.currentTimeMillis() + 5000;
											synchronized (VerbindungAufnahmeThread.this) {
												VerbindungAufnahmeThread.this
														.notifyAll();
											}
											try {
												out.write(CLOSE + "\n");
												out.flush();
												closed = true;
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
										nachricht = read.toString();
									}
								}
								synchronized (VerbindungAufnahmeThread.this) {
									VerbindungAufnahmeThread.this.notifyAll();
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					} catch (Exception e) {
						try {
							in.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							out.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						try {
							socket.close();
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * der Spieler treten beim übergeben Spiel aus
	 * 
	 * @param spiel
	 *            bei dem der Spieler austreten will
	 * @throws Exception
	 *             wenn ein Fehler beim Senden auftritt
	 */
	public void spielAustreten(MultiplayerSpiel spiel) throws Exception {
		out.write(SPIEL_RUECKTRITT + ";" + spiel.getId() + "\n");
		out.flush();
	}

	/**
	 * der Spieler teilt dem Server mit, dass er dem übergebenen Spiel betreten
	 * will. Anschließend wird das Ergebnis der Teilnahme mitgeteilt
	 * 
	 * @param spiel
	 *            bei dem der Spieler beitreten will
	 * @return die Antwort des Servers zur Beitrittsanfrage
	 * @throws Exception
	 *             wenn das Senden der Teilnahme fehlschlägt
	 */
	public String spielTeilnehmen(MultiplayerSpiel spiel) throws Exception {
		out.write(SPIEL_BEITRITT + ";" + spiel.getId() + "\n");
		out.flush();
		while (nachricht == null || nachricht.indexOf(BEITRITTS_ABSAGE) != 0
				&& nachricht.indexOf(BEITRITTS_BESTAETIGUNG) != 0) {
			synchronized (this) {
				this.wait();
			}
		}
		String ret = null;
		if (nachricht.indexOf(BEITRITTS_BESTAETIGUNG) != 0)
			ret = nachricht.substring(nachricht.lastIndexOf(";") + 1);
		nachricht = null;
		return ret;
	}

	/**
	 * fügt ein das übergeben Spiel zu den verfügbaren Spielen
	 * 
	 * @param spiel
	 *            das zu den verfügbaren Spielen hinzugefügt wird
	 * @throws Exception
	 *             wenn ein Fehler beim Senden zum Server passiert
	 */
	public void spielHinzufuegen(MultiplayerSpiel spiel) throws Exception {
		out.write(SPIEL + ";" + spiel.getAnzahlRunden() + ";"
				+ spiel.getGesamtSpieler() + ";" + spiel.getSpielname() + "\n");
		out.flush();
	}

	/**
	 * löscht das übergebene Spiel von den verfügbaren Spielen
	 * 
	 * @param spiel
	 *            das von den verfügbaren Spielen gelöscht werden soll
	 * @throws Exception
	 *             wenn ein Fehler beim Senden zum Server passiert
	 */
	public void spielLoeschen(MultiplayerSpiel spiel) throws Exception {
		out.write(SPIEL_LOESCHUNG + ";" + spiel.getId() + "\n");
		out.flush();
	}

	/**
	 * initialisiert den übergeben Spieler. Dabei werden die Daten des Spielers
	 * zum Server geschickt und die ID des Spielers gesetzt
	 * 
	 * @param spieler
	 *            der dem Server mitgeteilt werden soll
	 */
	public void initSpieler(MultiplayerSpieler spieler) {
		if (spieler.getId() == -1)
			try {
				this.spieler = spieler;
				out.write(spieler.getRegistrierNetzwerkString());
				out.flush();
				while (spieler.getId() == -1)
					synchronized (this) {
						try {
							wait();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * schickt dem Server eine Anfrage, die verfügbaren Spiele zu erhalten
	 * 
	 * @throws Exception
	 *             wenn ein Fehler bei der Kommunikation mit dem Server passiert
	 */
	public void getSpiele() throws Exception {
		out.write(UMFRAGE_VERFUEGBARE_SPIELE + "\n");
		out.flush();
	}

	/**
	 * liefert die verfügbaren Spiele
	 * 
	 * @return die verfügbaren Spiele
	 */
	public Vector<MultiplayerSpiel> getVerfuegbareMultiplayerSpiele() {
		return spiele;
	}

	/**
	 * liefert den Spieler des Spiels
	 * 
	 * @return der Spieler des Spiels
	 */
	public MultiplayerSpieler getSpieler() {
		return spieler;
	}

	/**
	 * kontrolliert, ob der Server erreichbar ist
	 * 
	 * @return true, wenn der Server erreichbar ist, ansonsten false
	 */
	public static boolean isVerfuegbar() {
		boolean ret = true;
		try {
			new Socket(IP_ADRESSE, ServerThreadPool.PORT).close();
		} catch (Exception e) {
			e.printStackTrace();
			ret = false;
		}
		return ret;
	}

	/**
	 * setzt den Listener, der beim Empfangen von Daten vom Server aufgerufen
	 * wird
	 * 
	 * @param listener
	 *            der gesetzt werden soll
	 */
	public void setActionListener(ActionListener listener) {
		this.listener = listener;
	}

	/**
	 * löscht den aktuellen Listener, der beim Empfangen von Daten vom Server
	 * aufgerufen
	 */
	public void removeActionListener() {
		this.listener = null;
	}

	/**
	 * liefert den Zeitpunkt, an dem das Spiel starten soll
	 * 
	 * @return der Zeitpunkt, an dem das Spiel starten soll
	 */
	public long getZeitpunkt() {
		while (zeitpunkt == 0)
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		return zeitpunkt;
	}

	/**
	 * sendet dem Server, dass der Spieler bereit zum Spielen ist beim Spiel mit
	 * der übergeben ID
	 * 
	 * @param id
	 *            des Spiels bei dem der Spieler bereit ist
	 */
	public void setBereit(int id) {
		System.out.println("setBeitritt");
		try {
			out.write(SPIELER_BEREIT + ";" + id + "\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
