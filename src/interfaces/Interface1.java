package interfaces;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import level.Level;

public class Interface1 extends JPanel implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7421846972519541464L;
	
	Canvas canvas;
	Level level;

	public Interface1() {
		super(new BorderLayout());
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

	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = Interface1.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	private static void createAndShowGUI() {
		JFrame frame = new JFrame("Interface1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Interface1());
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				createAndShowGUI();
			}
		});
	}
}