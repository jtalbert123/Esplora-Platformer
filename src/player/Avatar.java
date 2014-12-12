package player;

import game.Collidable;
import game.Drawable;
import interfaces.KeyboardState;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import level.Level;

public class Avatar implements Collidable, Drawable {

	public static final int WIDTH = Level.CELL_WIDTH;
	public static final int HEIGHT = Level.CELL_HEIGHT;

	protected double x;
	protected double y;
	protected double xVelocity;
	protected double yVelocity;
	protected double xAcceleration;
	protected double yAcceleration;
	protected double xDampening;
	protected double yDampening;

	boolean canJump = false;

	boolean isAlive;

	public Avatar() {
		x = 0;
		y = 0;
		xVelocity = 0;
		yVelocity = 0;
		xAcceleration = 0;
		yAcceleration = 0;
		xDampening = .1;
		yDampening = 0;
		isAlive = true;
	}

	public Avatar(int x, int y) {
		this.x = x;
		this.y = y;
		xVelocity = 0;
		yVelocity = 0;
		xAcceleration = 0;
		yAcceleration = 0;
		xDampening = .1;
		yDampening = 0;
		isAlive = true;
	}

	public Avatar(int x, int y, boolean alive) {
		this.x = x;
		this.y = y;
		xVelocity = 0;
		yVelocity = 0;
		xAcceleration = 0;
		yAcceleration = 0;
		xDampening = .1;
		yDampening = 0;
		isAlive = alive;
	}

	@Override
	public void update(long elapsedTime, Level level) {
		double time = elapsedTime / 1000.0;
		KeyboardState kb = KeyboardState.getKeyboardState();
		if (kb.isKeyDown("w")) {
			if (canJump)
				yVelocity = -9.8/2.0;
			canJump = false;
		}
		if (kb.isKeyDown("a") && kb.isKeyUp("d")) {
			xAcceleration = -4;
		} else if (kb.isKeyDown("d") && kb.isKeyUp("a")) {
			xAcceleration = 4;
		} else {
			xAcceleration = 0;
		}

		Collidable c = onGround(level); 
		if (c != null) {
			y = (c.getRect().getMinY() - HEIGHT)/Level.CELL_HEIGHT;
			yAcceleration = 0;
			yVelocity = Math.min(yVelocity, 0);
			canJump = true;
		} else {
			yAcceleration = 9.8;
		}

		x += xVelocity * time;
		xVelocity = xVelocity * (1 - xDampening) + xAcceleration * time;

		y += yVelocity * time;
		yVelocity = yVelocity * (1 - yDampening) + yAcceleration * time;
	}

	private Collidable onGround(Level level) {
		for (Collidable c : level) {
			if (this.isCollidingWith(c)) {
				Rectangle thisRect = getRect();
				Rectangle cRect = c.getRect();

				double xDisplacement = cRect.getCenterX()
						- thisRect.getCenterX();
				double yDisplacement = cRect.getCenterY()
						- thisRect.getCenterY();
				double angle = Math.atan2(-yDisplacement, xDisplacement);

				double angleLRCorner = Math.atan2(-thisRect.height,
						thisRect.width);
				double angleLLCorner = Math.atan2(-thisRect.height,
						-thisRect.width);
				if (angle <= angleLRCorner && angle >= angleLLCorner) {
					return c;
				}
			}
		}
		return null;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(0, 0, WIDTH-1, HEIGHT-1);
		g.setColor(Color.black);
		g.drawRect(0, 0, WIDTH-1, HEIGHT-1);
	}

	@Override
	public boolean isCollidingWith(Collidable c) {
		return getRect().intersects(c.getRect()) && c.tangible();
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle((int) (x * Level.CELL_WIDTH),
				(int) (y * Level.CELL_HEIGHT), WIDTH, HEIGHT);
	}

	@Override
	public boolean tangible() {
		// TODO we will need to decide what to do here. (should platforms bounce
		// off the player).
		return true;
	}
}
