package oving10;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.List;

public class Player extends Entity {
	private int xa, ya, gold;
	
	public Player(Game game) {
		super(game, 9, 9);
		gold = 0;
		xa = 0;
		ya = 0;
	}
	
	public void setXa(int xa) {
		this.xa = xa;
	}

	public void setYa(int ya) {
		this.ya = ya;
	}

	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}

	public void move() {
		boolean empty = true;
		if (getX()+xa >=0 && getX()+xa < Game.TILES)
			setX(getX()+xa);
		if (getY()+ya >= 0 && getY()+ya < Game.TILES)
			setY(getY()+ya);
		for (int e = 0; e<getGame().entities.size()-1; e++) {
			Entity en = getGame().entities.get(e);
			if (this.collides(en)) {
				if (en instanceof Coin) {
					gold++;
					en.removeMe(getGame().entities);
					SoundEffect.COIN.play();
				}
				if (en instanceof Enemy) {
					getGame().running = false;
					getGame().gameOver();
				}
			}
		}
		for (Entity e: getGame().entities)
			if (e instanceof Coin)
				empty = false;
		if (empty)
			getGame().newGame();
		getGame().repaint();
	}
	
	public void keyReleased(KeyEvent e) {
		xa = 0;
		ya = 0;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_LEFT)	xa = -1;
		if (key == KeyEvent.VK_RIGHT)	xa = 1;
		if (key == KeyEvent.VK_UP)		ya = -1;
		if (key == KeyEvent.VK_DOWN)	ya = 1;
	}
	
	public void draw(Graphics2D g) {
		g.setColor(Color.CYAN);
		g.fillRoundRect(getPixelX(), getPixelY(), SIZE, SIZE, 10, 10);
	}

	@Override
	public void removeMe(List<Entity> list) {
		System.out.println("Can't remove player!");
		
	}
}
