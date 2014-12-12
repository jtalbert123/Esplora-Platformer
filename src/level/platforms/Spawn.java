package level.platforms;

import game.Collidable;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.regex.Pattern;

import level.Level;

public class Spawn extends Platform {
	
	public static final Pattern regex = Pattern.compile("[Ss]");
	
	public Spawn(double x, double y) {
		this.x = x;
		this.y = y;
		this.type = 3;
		this.tangible = false;
	}

	public Spawn() {
		this.x = -1;
		this.y = -1;
		this.type = -1;
		this.tangible = false;
	}
	
	@Override
	public Collidable makePlatform(String str, Integer x, Integer y) {
		//System.out.println("Creating spawn object.");
		return new Spawn(x, y);
	}
	
	@Override
	public void draw(Graphics g) {
		Rectangle r = getRect();
		g.setColor(Color.green);
		g.drawRect(r.x, r.y, r.width-1, r.height-1);
	}

	@Override
	public void update(long milliseconds, Level level) {
		return;
	}
}
