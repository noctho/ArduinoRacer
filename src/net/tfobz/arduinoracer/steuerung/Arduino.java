package net.tfobz.arduinoracer.steuerung;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Enumeration;

import net.tfobz.arduinoracer.Spieloberflaeche;
import net.tfobz.arduinoracer.komponenten.SpielAuto;
import processing.core.PApplet;

/**
 * Die Klasse Arduino verwaltet die Steuerung des Spiels mit einer
 * Arduino-basierten Steuerung
 * 
 * @author Thomas Nocker
 *
 */
public class Arduino implements Steureung, SerialPortEventListener {

	static {
		try {
			System.setProperty("java.library.path", "nativ");

			Field fieldSysPath = ClassLoader.class
					.getDeclaredField("sys_paths");
			fieldSysPath.setAccessible(true);
			fieldSysPath.set(null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Portnamen, die der Aurduino auf unterschiedlichen Betriebssystemen
	 * annehmen kann
	 */
	public static final String PORT_NAMES[] = { "/dev/tty.usbserial-A9007UX1",
			"/dev/ttyACM0", "/dev/ttyUSB0", "COM7", "COM3", };
	/**
	 * Das Timeout in Millisekunden, das bei der Kommunikation gesetzt wird
	 */
	private static final int TIME_OUT = 2000;
	/**
	 * Die Geschwindigkeit in Millisekunden, mit dem die Kommunikation passiert
	 */
	private static final int DATA_RATE = 9600;
	/**
	 * Die Schnittstelle, mit der die Kommunikation passiert
	 */
	private SerialPort serialPort;
	/**
	 * Der Einlestestream der Arduinodaten
	 */
	private InputStream input;
	/**
	 * Der Ausschreibestream zum Arduino
	 */
	private OutputStream output;
	/**
	 * Sie Oberfläche des Spiels
	 */
	private Spieloberflaeche oberflaeche = null;

	/**
	 * Konstruktor - legt die Spieloberfläche fest
	 * 
	 * @param oberflaeche
	 *            die gesetzt werden soll
	 */
	public Arduino(Spieloberflaeche oberflaeche) {
		this.oberflaeche = oberflaeche;
	}

	/**
	 * nimmt die Verbindung mit dem Arduino auf und legt die Streams fest
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void verbindeMitSteuerung() {
		System.setProperty("gnu.io.rxtx.SerialPorts", "COM7");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
					.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			input = serialPort.getInputStream();
			output = serialPort.getOutputStream();

			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * schließt die Verbindung der Streams und mit dem Aurduino
	 */
	@Override
	public void entbindeMitSteuerung() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
			isConnected();
		}
	}

	/**
	 * schickt Daten zum Arduino
	 */
	@Override
	public void schreiben(boolean kolission) {
		System.out.println("kolission="+kolission);
		int ausgabe = 0;
		if (kolission)
			// Wenn das Auto kollidiert ist, werden alls 500ms alle roten oder
			// gar keine LEDs leuchten
			if (System.currentTimeMillis() / 500 % 2 == 0)
				ausgabe = 2 + 8 * 4;
			else
				ausgabe = 8 * 5;
		else {
			if (oberflaeche.getAuto().getGeschwindigkeit() > 0)
				// Wenn das Auto vorw\u00e4rts f\u00e4hrt, leuchtet die RGB LED
				// gr\u00fcn
				ausgabe = 1;
			else if (oberflaeche.getAuto().getBeschleunigung() < 0
					&& oberflaeche.getAuto().getGeschwindigkeit() > 0)
				// Wenn das Auto vorw\u00e4rts f\u00e4hrt,leuchtet die RGB LED
				// rot
				ausgabe = 2;
			else if (oberflaeche.getAuto().getBeschleunigung() < 0
					&& oberflaeche.getAuto().getGeschwindigkeit() < 0)
				// Wenn das Auto r\u00fcckw\u00e4rts f\u00e4hrt, leuchtet die
				// RGB LED rosa
				ausgabe = 3;
			ausgabe += (int) (PApplet.abs(oberflaeche.getAuto()
					.getGeschwindigkeit())
					/ SpielAuto.GESCHWINDIGKEITSBEGRENZER * 8.) * 4;
		}
		// es werden die Daten zum Arduino geschickt
		try {
			output.write(ausgabe);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * reagiert auf keine Tasteneingaben
	 */
	@Override
	public void keyPressed(java.awt.event.KeyEvent e) {
	}

	/**
	 * reagiert auf kein Tastenloslassen
	 */
	@Override
	public void keyReleased(java.awt.event.KeyEvent e) {
	}

	/**
	 * reagiert darauf, wenn Daten vom Arduino kommen, indem Beschleunigungen
	 * und Lenkwinkel gesetzt werden
	 */
	@Override
	public void serialEvent(SerialPortEvent oEvent) {
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
			try {
				byte[] buffer = new byte[input.available() - 1];
				input.read(buffer);
				int status = input.read();
				if (status != -1) {
					oberflaeche.getAuto().setBeschleunigung(0);
					if ((status % 2) == 0)
						// Wenn das Bit 0 0 ist, ist das Gaspedal getreten und
						// die Beschleunigung wird auf 100 m/s/s gesetzt
						oberflaeche.getAuto().setBeschleunigung(beschleunigung);
					if (((status / 2) % 2) == 0) {
						// Wenn das Bit 1 0 ist, ist das Bremspedal getreten
						// Wenn bereits das Gaspedal getreten ist,
						// wird blockiert um 1 inkrementiert, ansonsten auf 0
						// gesetzt
						// die Beschleunigung wird auf -200 m/s/s gesetzt
						oberflaeche.getAuto().setBeschleunigung(bremsung);
					}
					// Wenn bereits 20 mal blockiert wurde, wird isBlockiert auf
					// true
					// gesetzt
					else if (((status / 4) % 2) == 1) {
						// Wenn das Bit 2 1 ist, wird das Auto zurückgesetzt
						// zur\u00fcckgesetzt
						oberflaeche.getSpielmodus().setPauseknopfGedrueckt();
					}
					// Wenn das Auto nicht schon bereits kollidiert ist, wird
					// die
					// Lenkung angenommen
					if (status / 8 == 4 || status / 8 == 5)
						oberflaeche.getAuto().setLenkwinkel(0);
					else
						oberflaeche.getAuto().setLenkwinkel(
								0.26666666f * (status / 8) - lenkung);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	/**
	 * liefert zurück, ob der Arduino mit dem Computer verknüpft ist
	 * @return true, wenn der Arduino mit dem Computer verknüpft ist, ansonsten false
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isConnected() {
		CommPortIdentifier portId = null;
		try {
			System.setProperty("gnu.io.rxtx.SerialPorts", "COM7");
			Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
			while (portEnum.hasMoreElements()) {
				CommPortIdentifier currPortId = (CommPortIdentifier) portEnum
						.nextElement();
				for (String portName : Arduino.PORT_NAMES) {
					if (currPortId.getName().equals(portName)) {
						portId = currPortId;
						break;
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return portId != null;
	}
}
