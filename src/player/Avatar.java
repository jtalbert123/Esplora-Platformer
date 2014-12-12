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
	protected double xVeloity;
	protected double yVeloity;
	
	boolean isAlive;
	
	public Avatar() {
		x = 0;
		y = 0;
		xVeloity = 0;
		yVeloity = 0;
		isAlive = true;
	}
	
	public Avatar(int x, int y) {
		this.x = x;
		this.y = y;
		xVeloity = 1;
		yVeloity = 1;
		isAlive = true;
	}
	
	public Avatar(int x, int y, boolean alive) {
		this.x = x;
		this.y = y;
		xVeloity = 1;
		yVeloity = 1;
		isAlive = alive;
	}
	
	@Override
	public void update(long elapsedTime, Level level) {
		double time = elapsedTime/1000.0;
		KeyboardState kb = KeyboardState.getKeyboardState();
		if (kb.isKeyDown("w")) {
			y-=yVeloity*time;
		}
		if (kb.isKeyDown("a")) {
			x-=xVeloity*time;
		}
		if (kb.isKeyDown("s")) {
			y+=yVeloity*time;
		}
		if (kb.isKeyDown("d")) {
			x+=xVeloity*time;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.green);
		g.fillOval(0, 0, WIDTH, HEIGHT);
	}

	@Override
	public boolean isCollidingWith(Collidable c) {
		return getRect().intersects(c.getRect());
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle((int)(x*Level.CELL_WIDTH), (int)(y*Level.CELL_HEIGHT), WIDTH, HEIGHT);
	}

	@Override
	public boolean tangible() {
		//TODO we will need to decide what to do here. (should platforms bounce off the player).
		return false;
	}
}
