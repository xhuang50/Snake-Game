package game;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	//Game loop
	private Thread thread;
	private boolean running;
	private long targetTime;
	
	//Snake
	private final int SIZE = 10;
	private Entity head;
	private Entity nut;
	private ArrayList<Entity> snake;
	private int score;
	private int level;
	private boolean gameover;
	
	//movement
	private int dx;
	private int dy;
	
	//Direction
	private boolean up;
	private boolean down;
	private boolean left;
	private boolean right;
	private boolean start;
	
	public GamePanel() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
	}
	
	public void addNotify() {
		super.addNotify();
		thread = new Thread(this);
		thread.start();
	}
	
	private void setFPS(int fps) {
		targetTime = 1000 / fps;
	}
	
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP) {
			up = true;
		}
		if (k == KeyEvent.VK_DOWN) {
			down = true;
		}
		if (k == KeyEvent.VK_LEFT) {
			left = true;
		}
		if (k == KeyEvent.VK_RIGHT) {
			right = true;
		}
		if (k == KeyEvent.VK_ENTER) {
			start = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int k = e.getKeyCode();
		
		if (k == KeyEvent.VK_UP) {
			up = false;
		}
		if (k == KeyEvent.VK_DOWN) {
			down = false;
		}
		if (k == KeyEvent.VK_LEFT) {
			left = false;
		}
		if (k == KeyEvent.VK_RIGHT) {
			right = false;
		}
		if (k == KeyEvent.VK_ENTER) {
			start = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void run() {
		if (running) {
			return;
		}
		init();

		long startTime;
		long elaspsed;
		long wait;
		while(running) {
			startTime = System.nanoTime();
			update();
			requestRender();
			elaspsed = System.nanoTime() - startTime;
			wait = targetTime - elaspsed / 1000000;
			if (wait > 0) {
				try {
					Thread.sleep(wait);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void init() {
		image = new BufferedImage(WIDTH, HEIGHT,BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUplevel();

	}
	
	private void setUplevel() {
		snake = new ArrayList<Entity>();
		head = new Entity(SIZE);
		head.setPosition(WIDTH/2, HEIGHT/2);
		snake.add(head);
		for (int i = 1; i < 3; i++) {
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() + (i * SIZE), head.getY());
			snake.add(e);
		}
		nut = new Entity(SIZE);
		setNut();
		score = 0;
		
		gameover = false;
		level = 1;
		dx = 0;
		dy = 0;
		setFPS(level * 10);
	}
	
	public void setNut() {
		int x = (int) (Math.random() * (WIDTH - SIZE));
		int y = (int) (Math.random() * (WIDTH - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		nut.setPosition(x, y);
	}	
	
	private void requestRender() {
		render(g2d);
		Graphics g = getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
	}
	
	private void update() {
		if (gameover) {
			if (start) {
				setUplevel();
			}
			
			return;
		}
		if (up && dy == 0) {
			dy = -SIZE;
			dx = 0;
		}
		if (down && dy == 0) {
			dy = SIZE;
			dx = 0;
		}
		if (left && dx == 0) {
			dy = 0;
			dx = -SIZE;
		}
		if (right && dx == 0 && dy != 0) {
			dy = 0;
			dx = SIZE;
		}
		if (dx != 0 || dy != 0) {
			for (int i = snake.size() - 1; i > 0; i--) {
				snake.get(i).setPosition(
						snake.get(i-1).getX(), 
						snake.get(i-1).getY());
				
				
			}
			head.move(dx, dy);
		}
		
		for (Entity e : snake) {
			if (e.isCollsion(head)) {
				gameover = true;
				break;
			}
		}
			
		if (nut.isCollsion(head)) {
			score++;
			setNut();
			//
			Entity e = new Entity(SIZE);
			e.setPosition(-1000, -1000);
			snake.add(e);
			if (score % 10 == 0) {
				level++;
				if (level > 10) {
					level = 10;
				}
				setFPS(level * 10);

			}
		}
		
		if (head.getX() < 0) {
			head.setX(WIDTH - 10);
		}
		if (head.getY() < 0) {
			head.setY(HEIGHT - 10);
		}
		if (head.getX() > WIDTH - 10) {
			head.setX( 0);
		}
		if (head.getY() > HEIGHT - 10) {
			head.setY(0);
		}
	} 
	
	public void render(Graphics2D g2d) {
		g2d.clearRect(0,0,WIDTH, HEIGHT);
		g2d.setColor(Color.GREEN);
		for(Entity e: snake) {
			e.render(g2d);
		}
		
		g2d.setColor(Color.RED);
		nut.render(g2d);
		if (gameover) {
			g2d.drawString("Game Over!", 150, 200);
		}

		g2d.setColor(Color.WHITE);
		g2d.drawString("Score : " + score + " Level : " + level,10, 10);
		if (dx == 0 && dy == 0) {
			g2d.drawString("Ready?", 150, 200);
		}
		
	}

}
