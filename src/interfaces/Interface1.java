package interfaces;

import game.Game;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;

import level.Level;

/**
 * This class handles visualizing/running the {@link Game}. This should be added
 * toa frame object and must be run in it's own thread, if not it will prevent
 * the frame from responding to any commands, such as closing the window.
 * 
 * @author James Talbert
 *
 */
public class Interface1 extends JPanel implements ActionListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7421846972519541464L;

	/**
	 * The {@link Canvas} the {@link #game} will be drawn on.
	 */
	Canvas canvas;

	/**
	 * The {@link Game} object that is used to handle calls to
	 * {@link Game#clearItems(Graphics) clear()}, {@link Game#update() update()}
	 * , and {@link Game#draw(Graphics) draw()} methods.
	 */
	Game game;

	/**
	 * The button to allow the user to select a new level.
	 */
	JButton newLevel;

	/**
	 * The fileChooser to select a new level file.
	 */
	JFileChooser fc;

	/**
	 * Creates a new {@link Game} from a default level file and initializes the
	 * canvas.
	 */
	public Interface1() {
		super(new BorderLayout());
		fc = new JFileChooser();
		fc.setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Level files (.txt)";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(".txt") || f.isDirectory();
			}
		});
		setKeyBindings();
		game = new Game("level1.txt");
		canvas = new Canvas();
		Rectangle r = game.getBounds();
		canvas.setSize((r.width + 1) * Level.CELL_WIDTH, (r.height + 1)
				* Level.CELL_HEIGHT);
		add(canvas);
	}
	
	private void setKeyBindings() {

		KeyboardState keyPress = KeyboardState.getKeyPressListner();
		KeyboardState keyRelease = KeyboardState.getKeyreleaseListner();
//		this.addKeyListener(kbUpdater);
		
		InputMap keyStrokes = this
				.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		//listeners
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, false), "key pressed");
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0, true), "key released");

		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "key pressed");
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "key released");

		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "key pressed");
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "key released");

		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "key pressed");
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "key released");

		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "key pressed");
		keyStrokes.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "key released");
		
		ActionMap keyActions = this.getActionMap();
		//actions
		keyActions.put("key pressed", keyPress);
		keyActions.put("key released", keyRelease);
	}

	/**
	 * Unused, kept for syntax help, in the case that it is needed in the
	 * future.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newLevel) {
			int opt = fc.showOpenDialog(null);
			if (opt == JFileChooser.APPROVE_OPTION) {
				try {
					game.loadLevel(fc.getSelectedFile().getCanonicalPath());
				} catch (IOException e1) {

				}
			}
		}
	}

	/**
	 * The starting point of the game. Runs the {@link Game#update() update}/
	 * {@link Game#draw(Graphics) draw} loop.
	 */
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Graphics g = canvas.getGraphics();
		while (true) {
			game.clearItems(g);
			game.update();
			game.draw(g);
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Creates a new {@link JFrame} and {@link Interface1}, addds the
	 * {@link Interface1} to the {@link JFrame}, and returns the
	 * {@link Interface1}.
	 * 
	 * @return the {@link Interface1} that is in the new window.
	 */
	private static Interface1 createAndShowGUI() {
		Interface1 interface1 = new Interface1();
		JFrame frame = new JFrame("Interface1");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(interface1);
		frame.pack();
		frame.setVisible(true);
		return interface1;
	}

	/**
	 * Shows a new {@link JFrame} object with a {@link Interface1} inside. Then
	 * runs the {@link Interface1} in a new {@link Thread}.
	 * 
	 * @see #createAndShowGUI()
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		Thread t = new Thread(createAndShowGUI());
		t.start();
	}
}