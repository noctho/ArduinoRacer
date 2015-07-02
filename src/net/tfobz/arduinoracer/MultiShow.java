package net.tfobz.arduinoracer;

import java.awt.EventQueue;
import java.awt.Toolkit;

import net.tfobz.arduinoracer.menu.SpielBeitretenPanel;
import net.tfobz.arduinoracer.menu.SpieldetailsPanel;
import net.tfobz.arduinoracer.menu.Startmenu;
import net.tfobz.arduinoracer.menu.WarteAufGegnerPanel;
import net.tfobz.arduinoracer.spielmodi.Multiplayer;

/**
 * Diese Klasse wurde zu reinen Testzwecken entwickelt unst starten den Server,
 * sowie zwei Fenster für die Kommunikationsaufnahme des Multiplayers
 * 
 * @author Thomas Nocker
 *
 */
public class MultiShow {

	public MultiShow() {
		new ServerThreadPool().start();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Startmenu frame = new Startmenu();
					frame.setVisible(true);
					frame.setLocation(
							(int) ((Toolkit.getDefaultToolkit().getScreenSize()
									.getWidth() - 2 * frame.getWidth()) / 4),
							(int) ((Toolkit.getDefaultToolkit().getScreenSize()
									.getHeight() - frame.getHeight()) / 2));
					SpieldetailsPanel spieldetailsPanel = new SpieldetailsPanel(
							new Spieloberflaeche(frame));
					new WarteAufGegnerPanel(spieldetailsPanel, new Multiplayer(
							true));
					spieldetailsPanel.setup();
					frame.setAktuellesPanel(spieldetailsPanel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				try {
					Startmenu frame = new Startmenu();
					frame.setLocation(
							(int) Toolkit.getDefaultToolkit().getScreenSize()
									.getWidth()
									- frame.getWidth()
									- (int) ((Toolkit.getDefaultToolkit()
											.getScreenSize().getWidth() - 2 * frame
											.getWidth()) / 4), (int) ((Toolkit
									.getDefaultToolkit().getScreenSize()
									.getHeight() - frame.getHeight()) / 2));
					frame.setVisible(true);
					SpielBeitretenPanel spielBeitretenPanel = new SpielBeitretenPanel(
							new Spieloberflaeche(frame), new Multiplayer(true));
					spielBeitretenPanel.setup();
					frame.setAktuellesPanel(spielBeitretenPanel);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
