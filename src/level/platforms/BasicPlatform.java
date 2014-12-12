package level.platforms;

import game.Collidable;

import java.awt.Graphics;
import java.awt.Rectangle;

import level.Level;

public class BasicPlatform extends Platform {

	public Collidable makePlatform(String str, Integer x, Integer y) {
		return new BasicPlatform(x, y, 0, true);
	}

	protected BasicPlatform(double x, double y, int type, boolean tangible) {
		this.x = x;
		this.y = y;
		this.type = type;
	}

	protected BasicPlatform() {
		x = -1;
		y = -1;
		type = -1;
	}

	@Override
	public String toString() {
		return String.format("'-' at (%f, %f)", x, y);
	}

	/**
	 * Draws a black box with rounded corners.
	 * 
	 * @param g
	 *            the graphics object to be used
	 */
	public void draw(Graphics g) {
		Rectangle rect = getRect();
		g.fillRoundRect(0, 0, rect.width, rect.height, (int) rect.width / 5,
				(int) rect.height / 5);
	}

	public void update(long milliseconds, Level level) {
		return;
	}
	
	@Override
	public boolean tangible(Collidable c) {
		return true;
	}
}
