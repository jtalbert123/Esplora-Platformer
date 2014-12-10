package interfaces;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import level.Platform;

public class Interface1 extends JPanel implements ActionListener, KeyListener,
		Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7421846972519541464L;

	Canvas canvas;
	Game game;

	public Interface1() {
		super(new BorderLayout());
		game = new Game("level1.txt");
		canvas = new Canvas();
		Rectangle r = game.getBounds();
		canvas.setSize((r.width + 1) * Platform.PLATFORM_WIDTH,
				(r.height + 1) * Platform.PLATFORM_HEIGHT);
		add(canvas);
		canvas.addKeyListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == /* a thing */null) {
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (e.getSource() == canvas) {

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getSource() == canvas) {

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getSource() == canvas) {

		}
	}

	public void run() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Graphics g = canvas.getGraphics();
		while (true) {
			game.update(System.currentTimeMillis());
			game.draw(g);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Interface1.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private static Interface1 createAndShowGUI() {
		Interface1 interface1 = new Interface1();
		JFrame frame = new JFrame("Interface1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(interface1);
		frame.pack();
		frame.setVisible(true);
		return interface1;
	}

	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		Thread t = new Thread(createAndShowGUI());
		t.start();
	}
}