package houseStart;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 480;
	public static final int HEIGHT = WIDTH/12 * 9;
	public static final int SCALE = 2;
	public final String TITLE = "House Versus Patients Simulator";
	private boolean running = false;
	private Thread thread;
	
	private synchronized void start() {
		if(running)
			return;
		
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	private synchronized void stop() {
		if(!running)
			return;
		
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(1);
		
	}
	public void run() {
		long lastTime = System.nanoTime();
		final double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while(running) {
			
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				tick();
				delta--;
				updates++;
				
			}
			render();
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println( updates + " Ticks , FPS " + frames);
				updates = 0;
				frames = 0;
				
			
			}
		}
		stop();
	}
	private BufferedImage image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_BGR);
	
	private void tick() {
		
	}
	private void render() {
		//Returns buffer strategy, returns not if strategy not yet created
		BufferStrategy bs = this.getBufferStrategy();
		//if bs equals null, exit the function
		if (bs == null){
			createBufferStrategy(3); // 3 indicates amount of buffers
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		/////////////////////////////////////////////
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this); 
		
		
		
		
	
		
		/////////////////////////////////////////////
		g.dispose();
		bs.show();
				
	}
	public static void main(String args[]) {
		Game game = new Game();
		
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		
		JFrame frame = new JFrame(game.TITLE);
		frame.add(game);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		game.start();
	}
}
