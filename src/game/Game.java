package game;

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
	
	public void update(long milliseconds) {
		level.updateLevel(System.currentTimeMillis() - time);
		time = System.currentTimeMillis();
	}
	
	public void draw(Graphics g) {
		g.clearRect(0, 0, (level.width+1)*Platform.PLATFORM_WIDTH, (level.height+1)*Platform.PLATFORM_HEIGHT);
		for (Platform p : level) {
			Rectangle rect = p.getRect();
			g.fillRect(rect.x, rect.y, rect.width, rect.height);
			// System.out.println("Drew: " + rect);
		}
	}
	
	public Rectangle getBounds() {
		return new Rectangle(level.width, level.height);
	}

}
