package oving10;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;

public class Coin extends Entity {

	public Coin(Game game) {
		super(game);
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.fillOval(getPixelX(), getPixelY(), SIZE, SIZE);
	}

	@Override
	public void removeMe(List<Entity> list) {
		list.remove(this);
	}
	
}