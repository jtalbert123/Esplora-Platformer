package level;

import java.awt.Graphics;
import java.awt.Rectangle;

public class BasicPlatform extends Platform {

	public Platform makePlatform(String str, Integer x, Integer y) {
		return new BasicPlatform(x, y, 0, true);
	}

	protected BasicPlatform(double x, double y, int type, boolean tangible) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.tangible = tangible;
	}

	protected BasicPlatform() {
		x = -1;
		y = -1;
		type = -1;
		tangible = false;
	}

	@Override
	public String toString() {
		return String.format("'-' at (%f, %f)", x, y);
	}

	/**
	 * draws the representation of the object in the top left corner of the
	 * provided graphics object
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
}
