package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import platforms.Platform;
import level.Level;

public class Game implements KeyListener {
	private long time;
	private Level level;
	
	public Game(String fileName) {
		try {
			level = new Level(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		time = System.currentTimeMillis();
	}
	
	public void loadLevel(String fileName) throws IOException {
		level = new Level(fileName);
	}
	
	public void clearItems(Graphics g) {
		for (Platform p : level) {
			if (p.getClass() != Platform.class) {
				Rectangle r = p.getRect();
				g.clearRect(r.x, r.y, r.width, r.height);
			}
		}
	}
	
	public void update(long currentTime) {
		level.updateLevel(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
	}
	
	public void draw(Graphics g) {
		for (Platform p : level) {
			g.setColor(Color.black);
			Rectangle r = p.getRect();
			Graphics temp = g.create(r.x, r.y, r.width, r.height);
			p.draw(temp);
			temp.dispose();
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(level.width, level.height);
	}

	//KeyboardListener
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}
}
