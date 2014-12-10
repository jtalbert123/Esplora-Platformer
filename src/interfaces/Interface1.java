package interfaces;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import level.Level;
import level.Platform;

public class Interface1 extends JPanel implements ActionListener, KeyListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7421846972519541464L;
	
	private static final int width = 20;
	private static final int height = 15;

	Canvas canvas;
	Level level;

	public Interface1() {
		super(new BorderLayout());
		try {
			level = new Level("level1.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
		canvas = new Canvas();
		canvas.setSize(level.width*width+width, level.height*height+height);
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
		long time = System.currentTimeMillis();
		Graphics g = canvas.getGraphics();
		while (true) {
			level.updateLevel(System.currentTimeMillis() - time);
			time = System.currentTimeMillis();
			draw(g);
			try {
				Thread.sleep(75);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void draw(Graphics g) {
		g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for (Platform p : level.elements) {
			Rectangle rect = new Rectangle((int)(p.x*width), (int)(p.y*height), width-1, height-1);
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
			//System.out.println("Drew: " + rect);
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