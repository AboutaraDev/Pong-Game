import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int) (GAME_WIDTH*(5/9.0));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	
	Thread gameThread;
	Image image;
	Random random;
	Graphics graphic;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
	
	GamePanel() {
		newBall();
		newPaddle();
		score = new Score(GAME_WIDTH, GAME_HEIGHT);
		
		this.setPreferredSize(SCREEN_SIZE);
		this.setFocusable(true);
		this.addKeyListener(new AL());
		
	
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void newBall() {
		random = new Random();
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
	}
	public void newPaddle() {
		
		paddle1 = new Paddle(0, (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH, PADDLE_HEIGHT, 1);
		paddle2 = new Paddle((GAME_WIDTH-PADDLE_WIDTH), (GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH, PADDLE_HEIGHT, 2);

	}
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphic = image.getGraphics();
		draw(graphic);
		g.drawImage(image, 0, 0, this);
		}
	public void draw(Graphics g) {
		
		paddle1.draw(g);
		paddle2.draw(g);
		ball.draw(g);
		score.draw(g);
	}
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	public void checkCollisions() {
		
		// bouncs ball off on top & bottom window edges
		
		if(ball.y <= 0) 
			ball.setYDirection(-ball.yVelocity);
		if(ball.y >= (GAME_HEIGHT-BALL_DIAMETER))
			ball.setYDirection(-ball.yVelocity);
		// bouncs ball off paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++;
			if(ball.yVelocity > 0) 
				ball.yVelocity++;
			else
				ball.yVelocity--;
			
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.xVelocity++;
			if(ball.yVelocity > 0) 
				ball.yVelocity++;
			else
				ball.yVelocity--;
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		// give point to player1
		if(ball.x <0) {
			score.player1++;
			newPaddle();
			newBall();
		}
		if(ball.x > GAME_WIDTH-BALL_DIAMETER) {
			score.player2++;
			newPaddle();
			newBall();
		}
		//stops paddles
		if(paddle1.y <= 0) 
			paddle1.y = 0;
		if(paddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		if(paddle2.y <= 0) 
			paddle2.y = 0;
		if(paddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
	}
	@Override
	public void run() {
		
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ ns;
			lastTime = now;
			if(delta >= 1) {
				move();
				checkCollisions();
				repaint();
				delta--;
			}
		}
	}
	
	public class AL extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}
	
}
