package net.tfobz.arduinoracer;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import net.tfobz.arduinoracer.komponenten.Minimap;
import net.tfobz.arduinoracer.komponenten.SpielAuto;
import net.tfobz.arduinoracer.komponenten.Strecke;
import net.tfobz.arduinoracer.menu.AutoAuswahlPanel;
import net.tfobz.arduinoracer.menu.Startmenu;
import net.tfobz.arduinoracer.menu.SteuerungPanel;
import net.tfobz.arduinoracer.spielmodi.Demonstration;
import net.tfobz.arduinoracer.spielmodi.Multiplayer;
import net.tfobz.arduinoracer.spielmodi.Rundenfahrt;
import net.tfobz.arduinoracer.spielmodi.Spielmodi;
import net.tfobz.arduinoracer.steuerung.Demonstrationssteuerung;
import net.tfobz.arduinoracer.steuerung.Steureung;
import net.tfobz.arduinoracer.steuerung.Tastatur;
import net.tfobz.arduinoracer.util.Farben;
import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

/**
 * Die Klasse Spieloberflaeche bildet die graphische Darstellung des Spiels. Sie
 * beinhaltet die Daten und alle notwendigen Anzeigen, die für ein reibungsloses
 * Darstellen nötig sind. Insbesondere beinhaltet sie den Spielmodus und die
 * Steuerungsart und alle dementsprechenden Menüs
 * 
 * @author Thomas Nocker
 *
 */
@SuppressWarnings("serial")
public class Spieloberflaeche extends PApplet {

	/**
	 * Der Zeitpunkt, bei dem das zuletzt das Bild gezeichnet wurde
	 */
	public long currentMillis = System.currentTimeMillis();
	/**
	 * Die Zeit, die seit den letzten beiden Bildzeichnungen vergangen ist
	 */
	public long deltaT = 1;
	/**
	 * Das Auto das aktiven Spielers, für den die Spieloberfläche ausgelegt ist
	 */
	private SpielAuto auto = new SpielAuto(Demonstration.aktiverSpieler);
	/**
	 * Die Stecke, auf der gefahren wird
	 */
	private Strecke strecke = null;
	/**
	 * Die Minimap, die angezeigt wird
	 */
	private Minimap minimap = null;
	/**
	 * Die Steuerungsart, mit der des Spieler spielt
	 */
	private Steureung steuerung = null;
	/**
	 * Der Spielmodus, nach dem der Spieler spielt
	 */
	private Spielmodi spielmodus = null;
	/**
	 * Der Name das aktiven Spielers
	 */
	private String spielerName = null;
	/**
	 * Das Startmenü, das alle möglichen Menüs verwaltet
	 */
	protected Startmenu startmenu = null;
	/**
	 * Der aktuelle Zustand des Spiels
	 */
	private byte zustand = 0;
	/**
	 * Die Grafik, die die Ampel vor einem Multiplayerspiel anzeigt
	 */
	private PImage ampel = null;
	/**
	 * Die Oberfläche, auf der das 3D-Bild gezeichnet wird
	 */
	public PGraphics anzeige3D = null;
	/**
	 * Die Oberfläche, auf der das 2D-Bild gezeichnet wird
	 */
	public PGraphics anzeige2D = null;

	/**
	 * Das Fenster, was für das Laden der Resourcen angezeigt wird
	 */
	private SwingWorker<Void, Void> wait = null;

	/**
	 * Startet die Spieloberfläche. Dabei werden der Spielmodus, das Startmenü
	 * und das Frame, auf dem die Spieloberfläche läuft zugewiesen. Anschließend
	 * werden die jeweiligen Dialoge angezeigt
	 * 
	 * @param frame
	 *            auf dem die Spieloberfläche läuft
	 * @param spielmodus
	 *            des Spiels
	 * @param startmenu
	 *            des Spiels
	 */
	public Spieloberflaeche(Frame frame, Spielmodi spielmodus,
			Startmenu startmenu) {
		this.startmenu = startmenu;
		this.spielmodus = spielmodus;
		this.frame = frame;
		SteuerungPanel steuerungPanel = new SteuerungPanel(null, this);
		AutoAuswahlPanel autoAuswahlPanel = new AutoAuswahlPanel(steuerungPanel);
		spielmodus.getDialoge(autoAuswahlPanel);
		steuerung = new Tastatur(this);
		try {
			steuerungPanel.setup();
		} catch (Exception e) {
			e.printStackTrace();
			frame.dispose();
		}
		new Thread() {
			public void run() {
				if (Spieloberflaeche.this.spielmodus instanceof Demonstration) {
					steuerung = new Demonstrationssteuerung(
							Spieloberflaeche.this);
					setVisible();
					getStartmenu().dispose();
				}
			}
		}.start();
	}

	public Spieloberflaeche(Startmenu startmenu) {
		this.startmenu = startmenu;
	}

	/**
	 * Diese Methode wird nach der Initialisierung der Spieloberfläche
	 * aufgerufen. Es werden alle nötigen Grafiken geladen.
	 */
	public void setup() {
		frame.setEnabled(false);
		size(frame.getWidth(), frame.getHeight(), OPENGL);
		frame.repaint();
		frame.setEnabled(true);
		noLoop();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Strecke strecke = new Strecke(Spieloberflaeche.this);
				System.out.println("setup3");
				anzeige2D = createGraphics(displayWidth, displayHeight);
				anzeige3D = createGraphics(displayWidth, displayHeight, P3D);
				auto.init(Spieloberflaeche.this);
				// schreibt den Titel des Fensters
				// setzt das Icon des Spiels

				frame.setIconImage(new ImageIcon(loadBytes("icon.png"))
						.getImage());
				// lie\u00dft die bisher beste Runde ein
				minimap = new Minimap(Spieloberflaeche.this);
				frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosed(WindowEvent e) {
						spielmodus.onStop();
						steuerung.entbindeMitSteuerung();
					}
				});
				ampel = loadImage("ampel.png");
				noSmooth();
				System.out.println("endsetup");
				Spieloberflaeche.this.strecke = strecke;
				synchronized (Spieloberflaeche.this) {
					Spieloberflaeche.this.notifyAll();
				}
				steuerung.verbindeMitSteuerung();
				noSmooth();
				requestFocus();
				addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						steuerung.keyPressed(e);
					}

					@Override
					public void keyReleased(KeyEvent e) {
						steuerung.keyReleased(e);
					}
				});
				loop();
			}
		}).start();
	}

	/**
	 * macht die Spieloberfläche sichtbar und startet die regelmäßige Zeichnung
	 * der Spieloberfläche, falls diese nicht bereits sichtbar ist
	 */
	@SuppressWarnings("deprecation")
	public void setVisible() {
		frame.setExtendedState(Frame.MAXIMIZED_BOTH);
		System.out.println("setVisible");
		startmenu.setVisible(false);
		frame.show();
		frame.setBounds(0, 0, (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize()
				.getHeight());
		frame.setTitle(spielmodus.toString());
		wait = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				synchronized (wait) {
					wait.wait();
				}
				return null;
			}
		};

		Window win = SwingUtilities.getWindowAncestor(frame);
		final JDialog dialog = new JDialog(win, "Bitte warten ...",
				ModalityType.APPLICATION_MODAL);
		dialog.setBounds(100, 100, 150, 100);

		wait.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("state")) {
					if (evt.getNewValue() == SwingWorker.StateValue.DONE) {
						dialog.dispose();
					}
				}
			}
		});
		wait.execute();

		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(progressBar, BorderLayout.CENTER);
		panel.add(new JLabel("\n    Spiel wird vorbereitet    \n\n"),
				BorderLayout.PAGE_START);
		dialog.add(panel);
		dialog.pack();
		dialog.setLocationRelativeTo(win);
		new Thread() {
			public void run() {
				dialog.setVisible(true);
			}
		}.start();
		width = 10;
		height = 10;
		frame.setVisible(true);
		init();
		spielmodus.onStart(this);
	}

	/**
	 * Diese Methode wird während des gesamten Programms ausgeführt und zeichnet
	 * die Oberfläche. Des weiteren wird die aktuelle Zeit sowie der
	 * Zeitunterschied zwischen den letzten beiden Zeichnungen angepasst.
	 */
	public void draw() {
		if (frame.isVisible() && anzeige3D != null && anzeige2D != null)
			synchronized (this) {
				try {
					anzeige3D.beginDraw();
					anzeige3D.clear();
					anzeige2D.beginDraw();
					anzeige2D.clear();
					long t0 = System.currentTimeMillis();
					// die Zeit zwischen der letzten beiden Aufrufe wird gesetzt
					deltaT = t0 - currentMillis;
					// die Zeit des letzten Funktionsaufrufes wird gesetzt
					currentMillis = t0;
					if (zustand == 0)
						zustand = Byte.MAX_VALUE;
					else if (zustand == Byte.MAX_VALUE) {
						synchronized (wait) {
							wait.notify();
						}
						int anzahl = 0;
						// es wird gepr\u00fcft ob eine Kollision existiert
						if (steuerung != null
								&& spielmodus instanceof Demonstration)
							steuerung.schreiben(auto.isKollision());
						synchronized (auto) {
							if (auto.isLinksHintenImRasen(strecke)) {
								anzahl++;
							}
							if (auto.isLinksVorneImRasen(strecke)) {
								anzahl++;
							}
							if (auto.isRechtsHintenImRasen(strecke)) {
								anzahl++;
							}
							if (auto.isRechtsVorneImRasen(strecke)) {
								anzahl++;
							}
							if (!(spielmodus instanceof Demonstration))
								auto.fahren(anzahl, this);
							// System.out.println("deltaT =" + deltaT);
							zeichneStrecke(anzahl);
						}
						if (steuerung != null
								&& !(spielmodus instanceof Demonstration))
							steuerung.schreiben(auto.isKollision());
					} else {
						synchronized (wait) {
							wait.notify();
						}
						if (zustand == -2 && deltaT < 1000) {
							((Multiplayer) spielmodus)
									.getVerbindungAufnahmeThread().setBereit(
											((Multiplayer) spielmodus)
													.getMuliplayerSpiel()
													.getId());
							zustand = -3;
						}
						if (zustand == -4 && deltaT < 1000) {
							zustand = -3;
							setStart();
						}
						zeichneStrecke(-1);
						anzeige2D.image(ampel, width / 2, height / 2);
						anzeige2D.fill(Farben.AMPELFARBE);
						anzeige2D.noStroke();
						this.ellipseMode(CENTER);
						if (zustand == 0)
							zustand = Byte.MAX_VALUE;
						else if (zustand > 0) {
							for (int i = 3 - zustand; i < 3; i++)
								anzeige2D.ellipse(width / 2 - (i - 1) * 99,
										height / 2, 67, 67);
							noLoop();
						}
					}
					anzeige3D.endDraw();
					anzeige2D.endDraw();
					imageMode(CENTER);
					translate(width / 2, height / 2);
					image(anzeige3D, 0, 0);
					imageMode(CORNER);
					image(anzeige2D, -width / 2, -height / 2);
					// System.out.println(zahl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		// System.out.println("streckeT="
		// + (System.currentTimeMillis() - streckeLong) + "\n");
	}

	public boolean sketchFullScreen() {
		return false;
	}

	/**
	 * zeichnet die Rennstrecke sowie alle darauf befindlichen Autos. Des
	 * weiteren werden alle Autos in der Minimap richtig positioniert
	 * 
	 * @param anzahlReifenImRasen
	 *            die Anzahl der Reifen des Spielautos im Rasen
	 */
	public void zeichneStrecke(int anzahlReifenImRasen) {
		if (strecke != null)
			try {
				anzeige3D.background(38, 127, 0);
				anzeige3D.translate(anzeige3D.width / 2, anzeige3D.height / 2
						+ height / 6);
				anzeige3D.rotateX(8F * PI / 24);
				anzeige3D.rotate(auto.getKurvenwinkel());
				anzeige3D.imageMode(CENTER);
				strecke.display(this, auto.getX(), auto.getY());
				anzeige3D.rotate(-auto.getKurvenwinkel());
				auto.display(this);
				anzeige3D.rotate(auto.getKurvenwinkel());
				spielmodus.onDraw(auto.getAktuelleBodenbegebenheit(this),
						anzahlReifenImRasen);
				anzeige3D.rotate(-auto.getKurvenwinkel());
				anzeige3D.rotateX(-7F * PI / 24);
				minimap.display(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * reagiert darauf wenn eine Taste auf der Tastatur losgelassen wird
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		steuerung.keyReleased(e);
	}

	/**
	 * reagiert darauf, wenn eine Taste auf der Tastatur gedrückt wird
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		steuerung.keyPressed(e);
	}

	/**
	 * liefert das Auto des Spielers
	 * 
	 * @return das Auto des Spielers
	 */
	public SpielAuto getAuto() {
		return auto;
	}

	/**
	 * liefert die Strecke
	 * 
	 * @return die Strecke
	 */
	public Strecke getStrecke() {
		return strecke;
	}

	/**
	 * liefert den Spielmodus
	 * 
	 * @return der Spielmodus
	 * 
	 */
	public Spielmodi getSpielmodus() {
		return spielmodus;
	}

	/**
	 * liefert den Namen des Spielers
	 * 
	 * @return der Name des Spielers
	 * 
	 */
	public String getSpielerName() {
		return spielerName;
	}

	/**
	 * setzt den Namen des Spielers
	 * 
	 * @param spielerName
	 *            der Name des Spielers
	 */
	public void setSpielerName(String spielerName) {
		this.spielerName = spielerName;
	}

	/**
	 * liefert das Startmenü
	 * 
	 * @return das Startmenü
	 */
	public Startmenu getStartmenu() {
		return startmenu;
	}

	/**
	 * setzt das Auto des Spielers im Spiel, auf der Minimap und bei der
	 * Steuerung
	 * 
	 * @param auto
	 *            das Auto des Spielers
	 */
	public void setAuto(SpielAuto auto) {
		this.auto = auto;
	}

	/**
	 * setzt die Steuerungsart des Spielers
	 * 
	 * @param steuerung
	 *            Steuerungsart des Spielers
	 */
	public void setSteuerung(Steureung steuerung) {
		this.steuerung = steuerung;
	}

	/**
	 * teilt dem VerbindungsaufnahmeThread mit, dass die Spieloberfläche fertig
	 * geladen ist und das Spiel startklar ist. Anschließend wird gewartet bis
	 * alle Spieler bereit sind und in einem dreisekündigem Countdown wird ein
	 * Startsignal mithilfe einer Ampel angezeigt
	 */
	public void setStart() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				long zeitpunkt = System.currentTimeMillis() + 5000;
				noLoop();
				synchronized (this) {
					try {
						this.wait(zeitpunkt - System.currentTimeMillis() - 4000);
						draw();
					} catch (Exception e) {
					}
				}
				for (int i = 3; i > 0; i--) {
					synchronized (Spieloberflaeche.this) {
						zustand = (byte) i;
						loop();
					}
					synchronized (this) {
						try {
							this.wait(zeitpunkt - System.currentTimeMillis()
									- (i - 1) * 1000);
						} catch (Exception e) {
						}
					}
				}
				loop();
				if (spielmodus instanceof Demonstration)
					((Demonstration) spielmodus).setStart(zeitpunkt);
				synchronized (Spieloberflaeche.this) {
					zustand = 0;
				}
			}
		}).start();
	}

	/**
	 * teilt dem VerbindungsaufnahmeThread mit, dass die Spieloberfläche fertig
	 * geladen ist und das Spiel startklar ist. Anschließend wird gewartet bis
	 * alle Spieler bereit sind und in einem dreisekündigem Countdown wird ein
	 * Startsignal mithilfe einer Ampel angezeigt
	 * 
	 * @param verbindungAufnahmeThread
	 *            mit welchem das Bereitsein Kommuniziert wird und von welchem
	 *            der Startzeitpunkt abgefragt wird
	 */
	public void setStart(final VerbindungAufnahmeThread verbindungAufnahmeThread) {
		zustand = -2;
		System.out.println("Zahl -2");
		System.err.println("\t\tZahlset " + zustand);
		setVisible();
		new Thread(new Runnable() {
			@Override
			public void run() {
				long zeitpunkt = verbindungAufnahmeThread.getZeitpunkt();
				noLoop();
				synchronized (this) {
					try {
						this.wait(zeitpunkt - System.currentTimeMillis() - 4000);
						draw();
					} catch (Exception e) {
					}
				}
				for (int i = 3; i > 0; i--) {
					synchronized (Spieloberflaeche.this) {
						zustand = (byte) i;
						currentMillis = System.currentTimeMillis();
						draw();
					}
					synchronized (this) {
						try {
							this.wait(zeitpunkt - System.currentTimeMillis()
									- (i - 1) * 1000);
						} catch (Exception e) {
						}
					}
				}
				currentMillis = System.currentTimeMillis();
				draw();
				loop();
				if (spielmodus instanceof Multiplayer)
					((Multiplayer) spielmodus).setStart(zeitpunkt);
				synchronized (Spieloberflaeche.this) {
					zustand = 0;
				}
			}
		}).start();
	}

	/**
	 * zeichnet eine diagonale Box mit einer Breite von 26 Pixel, die von einer
	 * übergebenen Koordinate links beginnt und bei der übergebenen rechten
	 * Koordinate aufhört
	 * 
	 * @param linksx
	 *            die übergebene linke x-Koordinate
	 * @param rechtsx
	 *            die übergebene rechte x-Koordinate
	 * @param linksy
	 *            die übergebene linke y-Koordinate
	 * @param rechtsy
	 *            die übergebene rechte y-Koordinate
	 */
	public void box(float linksx, float rechtsx, float linksy, float rechtsy) {
		// hinten
		anzeige3D.beginShape();
		anzeige3D.vertex(rechtsx, rechtsy - 13, -50);
		anzeige3D.vertex(linksx, linksy - 13, -50);
		anzeige3D.vertex(linksx, linksy + 13, -50);
		anzeige3D.vertex(rechtsx, rechtsy + 13, -50);
		anzeige3D.endShape(CLOSE);

		// oben
		anzeige3D.beginShape();
		anzeige3D.vertex(rechtsx, rechtsy - 13, -50);
		anzeige3D.vertex(rechtsx, rechtsy - 13, +50);
		anzeige3D.vertex(linksx, linksy - 13, +50);
		anzeige3D.vertex(linksx, linksy - 13, -50);
		anzeige3D.endShape(CLOSE);

		// unten
		anzeige3D.beginShape();
		anzeige3D.vertex(rechtsx, rechtsy + 13, -50);
		anzeige3D.vertex(rechtsx, rechtsy + 13, +50);
		anzeige3D.vertex(linksx, linksy + 13, +50);
		anzeige3D.vertex(linksx, linksy + 13, -50);
		anzeige3D.endShape(CLOSE);

		// rechts
		anzeige3D.beginShape();
		anzeige3D.vertex(rechtsx, rechtsy + 13, -50);
		anzeige3D.vertex(rechtsx, rechtsy + 13, +50);
		anzeige3D.vertex(rechtsx, rechtsy - 13, +50);
		anzeige3D.vertex(rechtsx, rechtsy - 13, -50);
		anzeige3D.endShape(CLOSE);

		// links
		anzeige3D.beginShape();
		anzeige3D.vertex(linksx, linksy + 13, -50);
		anzeige3D.vertex(linksx, linksy + 13, +50);
		anzeige3D.vertex(linksx, linksy - 13, +50);
		anzeige3D.vertex(linksx, linksy - 13, -50);
		anzeige3D.endShape(CLOSE);

		// vorne
		anzeige3D.beginShape();
		anzeige3D.vertex(rechtsx, rechtsy - 13, +50);
		anzeige3D.vertex(linksx, linksy - 13, +50);
		anzeige3D.vertex(linksx, linksy + 13, +50);
		anzeige3D.vertex(rechtsx, rechtsy + 13, +50);
		anzeige3D.endShape(CLOSE);
	}

	/**
	 * setzt das Ende einer Pause im Spiel, indem das Spiel wieder weiter läauft
	 * und beim spielmodus registriert wird, dass die Pause beendet ist. Falls
	 * das Spiel zurückgesetzt wurde, wird das Auto auch an die Ausgangsposition
	 * gebracht
	 * 
	 * @param reset
	 *            ob das Spiel zurückgesetzt wurde
	 */
	public void setPauseEnde(boolean reset) {
		currentMillis = System.currentTimeMillis();
		if (reset) {
			synchronized (auto) {
				auto.setGeschwindigkeit(0);
				auto.setX(-145);
				auto.setY(-900);
				auto.setKurvenradius(TWO_PI);
				auto.getAktuelleBodenbegebenheit(strecke);
				if (((Rundenfahrt) spielmodus).getRundenzeit().isGestartet())
					((Rundenfahrt) spielmodus).getRundenzeit().reset();
			}
		} else if (((Rundenfahrt) spielmodus).getRundenzeit().isGestartet())
			((Rundenfahrt) spielmodus).getRundenzeit().setEndePause(
					currentMillis);
		loop();
	}

	/**
	 * hält das Spiel an
	 */
	public void setPause() {
		noLoop();
	}

	/**
	 * liefert die Steuerung des Spiels
	 * 
	 * @return die Steuerung des Spiels
	 */
	public Steureung getSteuerung() {
		return steuerung;
	}

	public void setZustand(byte zustand) {
		this.zustand = zustand;
	}

}