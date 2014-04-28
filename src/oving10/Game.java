package oving10;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Game extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TILES = 10;
	public static final int TILE_SIZE = 48;
	public static final int AMOUNT_GOLD = 5;
	
	public Player player;
	public int enemies;
	public boolean running, pause;
	public double session;
	public Sound music;
	
	public List<Entity> entities;
	
	public Game() {
		player = new Player(this);
		entities = new ArrayList<Entity>();
		enemies = 0;
		running = true;
		music = new Sound("score.wav");
		music.play();
		music.loop();
		setBounds(0, 0, TILE_SIZE*TILES+7, TILE_SIZE*TILES+30);
		setFocusable(true);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				player.keyReleased(arg0);
			}
			
			@Override
			public void keyPressed(KeyEvent arg0) {
				player.keyPressed(arg0);
				player.move();
			}
		});
		
		newGame();
	}
	
	public void newGame() {
		session = Math.random();
		entities.clear();
		running = true;
		pause = false;
		enemies++;
		create: for(int c = 0; c < AMOUNT_GOLD; c++) {
			Coin k = new Coin(this);
			for (Entity e: entities)
				if (e.collides(k)) { c--; continue create; }
			if (k.tooClose(player)) { c--; continue; }
			entities.add(k);
		}
		create: for(int e = 0; e < enemies; e++) {
			Enemy en = new Enemy(this, session);
			for (Entity ent: entities)
				if (ent.collides(en)) { e--; continue create; }
			if (en.tooClose(player)) { e--; continue; }
			entities.add(en);
			Thread enemy = new Thread(en);
			enemy.start();
		}
		entities.add(player);
	}

	private void drawGrid(Graphics2D g) {
		g.setColor(Color.WHITE);
		for (int i = 0; i<TILES; i++)
			for (int j = 0; j<TILES; j++)
				g.drawRect(i*TILE_SIZE, j*TILE_SIZE, TILE_SIZE, TILE_SIZE);
		g.setColor(Color.red);
		g.setStroke(new BasicStroke(3));
		g.drawLine(TILE_SIZE*TILES/2, 0, TILE_SIZE*TILES/2, TILE_SIZE*TILES);
		g.drawLine(0, TILE_SIZE*TILES/2, TILE_SIZE*TILES, TILE_SIZE*TILES/2);
	}
	
	public void draw(Graphics2D g) {
		// Bottom layer
		drawGrid(g);
		// Objects
		for (Entity e: entities)
			e.draw(g);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		setBackground(Color.BLACK);
		draw(g2);
	}
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Øving 10: Generics, Tråder");
		Game game = new Game();
		Dimension dim = new Dimension(game.getWidth(), game.getHeight());
		f.setPreferredSize(dim);
		f.add(game);
		f.setVisible(true);
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
	}
	
	@SafeVarargs
	public static <E> List<E> commonElements(Collection<? extends E>... c) {
		List<E> common = new ArrayList<>(c[0]);
		for(Collection<? extends E> arr: c)
			common.retainAll(arr);
		
		return common;
	}

	public void gameOver() {
		running = false;
		pause = true;
		int choice = JOptionPane.showConfirmDialog(
				this,
				"You've been caught with "
						+ player.getGold() + " coins!\n"
								+ "Play Again?",
				"Game Over",
				JOptionPane.YES_NO_OPTION);
		if (choice == JOptionPane.YES_OPTION) {
			player = new Player(this);
			music.play();
			music.loop();
			enemies = 0;
			repaint();
			newGame();
		}
		if (choice == JOptionPane.NO_OPTION) System.exit(0);
		
	}
}
