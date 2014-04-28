package oving10;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Enemy extends Entity implements Runnable {
	private int xa, ya;
	private double session;
	private long speed;

	public Enemy(Game game, double session) {
		super(game);
		xa = ya = 0;
		speed = 1000;
		this.session = session;
	}
	
	public void move() {
		int playerX = getGame().player.getX();
		int playerY = getGame().player.getY();
		if (playerX>getX()) xa = 1;
		if (playerX<getX()) xa = -1;
		if (playerY<getY()) ya = -1;
		if (playerY>getY()) ya = 1;
		
		updateXY();
		getGame().repaint();
	}
	
	public void updateXY() {
			setX(getX() + xa);
			setY(getY() + ya);
		xa = ya = 0;
	}
	
	private boolean emptySpot(int x, int y) {
		for (Entity e: getGame().entities)
			if (e instanceof Enemy)
				if (this.collides(e))
					if (this != e){
						System.out.println("Collide!");
						return false;
					}
		return true;
	}

	@Override
	public void draw(Graphics2D g) {

		g.setStroke(new BasicStroke(2));
		g.setColor(Color.red);
		g.fillOval(getPixelX()+SIZE/8, getPixelY()+SIZE/8, SIZE-SIZE/4, SIZE-SIZE/4);
		g.drawLine(
				getPixelX(), 
				getPixelY(), 
				getPixelX()+SIZE, 
				getPixelY()+SIZE);
		g.drawLine(
				getPixelX()+SIZE, 
				getPixelY(), 
				getPixelX(), 
				getPixelY()+SIZE);
	}

	@Override
	public void run() {
		Player p = getGame().player;
		while(getGame().session == session &&
				getGame().running &&
				!getGame().pause) {
			move();
			if(this.collides(p)) {
				getGame().running = false;
				getGame().music.stop();
				SoundEffect.KILLED.play();
				getGame().gameOver();
			}
			if (speed > 300)
				speed -= 50;
			try {
				Thread.sleep(speed);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void removeMe(List<Entity> list) {
		System.out.println("Enemies can't be killed!");
	}

}
