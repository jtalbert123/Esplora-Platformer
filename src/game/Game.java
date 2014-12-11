package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import level.Level;
import level.Platform;

public class Game {
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
	
	public void clearItems(Graphics g) {
		for (Platform p : level) {
			if (p.getClass() != Platform.class) {
				Rectangle r = p.getRect();
				g.clearRect(r.x, r.y, r.width, r.height);
			}
		}
	}
	
	public void update(long milliseconds) {
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

}
