package level.platforms;

import game.Collidable;

import java.awt.Graphics;
import java.util.regex.Pattern;

import player.AbstractAvatar;
import level.Level;

public class EndPoint extends Platform {
	
	public static final Pattern regex = Pattern.compile("(?:[Ee]|\\{[Ee]nd(?:point)\\})");

	public EndPoint() {
		super();
	}
	
	public EndPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean tangible(Collidable c) {
		return false;
	}

	@Override
	public Collidable makePlatform(String str, Integer x, Integer y) {
		return new EndPoint(x, y);
	}

	@Override
	public void draw(Graphics g) {
		//TODO
	}

	@Override
	public void update(long currentTime, Level level) {
		for (Collidable c : level) {
			if (this.isCollidingWith(c)) {
				if (AbstractAvatar.class.isInstance(c)) {
					//TODO end the game
				}
			}
		}
	}

}
