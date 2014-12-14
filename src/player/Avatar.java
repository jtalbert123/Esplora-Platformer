package player;

import game.Collidable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import level.Level;
import level.platforms.MovingPlatform;

public class Avatar extends AbstractAvatar {

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

	protected boolean canJump = false;

	protected boolean isAlive;

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

	public Avatar(double x, double y) {
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

	public Avatar(double x, double y, boolean alive) {
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
		// TODO Auto-generated method stub
		//super.update(elapsedTime, level);
	}
/*
	@Override
	public void update(long elapsedTime, Level level) {
		double time = elapsedTime / 1000.0;
		KeyboardState kb = KeyboardState.getKeyboardState();

			if (kb.isKeyDown("w")) {
				if (canJump)
					yVelocity = -9.8 / 1.5;
				canJump = false;
			}
			if (kb.isKeyDown("a") && kb.isKeyUp("d")) {
				xAcceleration = -9;

			} else if (kb.isKeyDown("d") && kb.isKeyUp("a")) {
				xAcceleration = 9;
			} else {
				xAcceleration = 0;
			}

			if (x < 0) {
				xVelocity = 0;
				x = 0;
			} else if (x > level.width) {
				xVelocity = 0;
				x = level.width;
			}
			if (y > level.height + 1) {
				isAlive = false;
			}
			double tempYV = 0;
			double tempXV = 0;
			Collidable cBelow = platformBelow(level);
			if (cBelow != null) {
				y = (cBelow.getRect().getMinY() - HEIGHT + 1)
						/ ((double) Level.CELL_HEIGHT);
				yAcceleration = Math.min(yAcceleration, 0);
				yVelocity = Math.min(yVelocity, 0);
				if (MovingPlatform.class.isInstance(cBelow)) {
					tempXV = ((MovingPlatform) cBelow).getXVelocity();
					tempYV = ((MovingPlatform) cBelow).getYVelocity();
				}
				canJump = true;
			} else {
				yAcceleration = 9.8;
			}

			Collidable cAbove = platformAbove(level);
			if (cAbove != null) {
				if (MovingPlatform.class.isInstance(cAbove))
					yVelocity = Math.max(yVelocity,
							((MovingPlatform) cAbove).getYVelocity());
				else
					yVelocity = Math.max(yVelocity, 0);

				yAcceleration = Math.max(yAcceleration, 0);
				y = cAbove.getRect().getMaxY() / Level.CELL_HEIGHT;
			}

			Collidable cRight = platformRight(level);
			if (cRight != null) {
				if (MovingPlatform.class.isInstance(cRight))
					xVelocity = Math.min(xVelocity,
							((MovingPlatform) cRight).getXVelocity());
				else
					xVelocity = Math.min(xVelocity, 0);
				x = (cRight.getRect().getMinX() - WIDTH) / Level.CELL_WIDTH;
			}

			Collidable cLeft = platformLeft(level);
			if (cLeft != null) {
				if (MovingPlatform.class.isInstance(cLeft))
					xVelocity = Math.max(xVelocity,
							((MovingPlatform) cLeft).getXVelocity());
				else
					xVelocity = Math.max(xVelocity, 0);
				x = (cLeft.getRect().getMaxX()) / Level.CELL_WIDTH;
			}

			if (cLeft != null && cRight != null) {
				isAlive = false;
			}
			if (cAbove != null && cBelow != null) {
				isAlive = false;
			}

			tempXV += xVelocity;
			tempYV += yVelocity;

			x += tempXV * time;
			xVelocity = xVelocity * (1 - xDampening) + xAcceleration * time;

			y += tempYV * time;
			yVelocity = yVelocity * (1 - yDampening) + yAcceleration * time;
	}
*/
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		int diameter = Math.min(WIDTH, HEIGHT) - 1;
		g.fillOval((WIDTH - diameter)/2, (HEIGHT - diameter)/2, diameter, diameter);
	}

	@Override
	public boolean isCollidingWith(Collidable c) {
		//TODO
		return true;
	/*	Rectangle r = c.getRect();
		if (!MovingPlatform.class.isInstance(c)) {
			r.width -= 2;
			r.height -= 2;
			r.x += 1;
			r.y += 1;
		}
		return getRect().intersects(r) && c.tangible(this);
		*/
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle((int) (x * Level.CELL_WIDTH),
				(int) (y * Level.CELL_HEIGHT), WIDTH, HEIGHT);
	}

	@Override
	public boolean tangible(Collidable c) {
		// TODO we will need to decide what to do here. (should platforms bounce
		// off the player).
		if (MovingPlatform.class.isInstance(c)) {
			//c is a moving platform (or subclass).
			return false;
		}
		return true;
	}

	public boolean isAlive() {
		return isAlive;
	}

	@Override
	public Rectangle2D getLogicalBounds() {
		// TODO Auto-generated method stub
		return new Rectangle2D.Double();
	}
}
