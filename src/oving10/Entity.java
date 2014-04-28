package oving10;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;

interface EntityForm {
	public void draw(Graphics2D g);
	public void removeMe(List<Entity> list);
}

public abstract class Entity implements EntityForm {
	public final int SIZE;
	private int x, y;
	private Game game;
	
	public Entity(Game game, int x, int y) {
		this(game);
		this.x = x;
		this.y = y;
	}
	
	public Entity(Game game) {
		this.SIZE = Game.TILE_SIZE/2;
		this.game = game;
		x = (int)(Math.random()*Game.TILES);
		y = (int)(Math.random()*Game.TILES);
		
		//y = (int)(Math.random()*(Game.TILES - ((x > 4) ? 5 : 0)));
	}
	
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPixelX() {
		return x*Game.TILE_SIZE+((Game.TILE_SIZE-SIZE)/2);
	}
	
	public int getPixelY() {
		return y*Game.TILE_SIZE+((Game.TILE_SIZE-SIZE)/2);
	}
	
	public Rectangle getBounds() {
		return new Rectangle(getPixelX(), getPixelY(), SIZE, SIZE);
	}
	
	public <E extends Entity> boolean collides(E e) {
		return e.getBounds().intersects(getBounds());
	}
	
	public <E extends Entity> boolean tooClose(E e) {
		boolean cX, cY;
		cX = Math.abs(e.getX() - x) < Game.TILES/2;
		cY = Math.abs(e.getY() - y) < Game.TILES/2;
		if (cX && cY)
			return true;
		else
			return false;
	}
}
