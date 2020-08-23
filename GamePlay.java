package BrickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JPanel;

//KeyListener is for moving the arrows, ActionListner is for moving the ball
public class GamePlay extends JPanel implements KeyListener, ActionListener {

	private boolean play = false;
	private int score = 0;
	
	private int totalBricks = 21;
	
	//Timing of ball
	private Timer timer;
	private int delay = 2;
	
	//Starting position of player
	private int playerX = 310;
	
	private int round = 1;
	private int paddleSpeed = 20;
	
	//Starting position of ball
	private int ballposX = 120;
	private int ballposY = 350;
	//Direction ball will move
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	public GamePlay() {
		map = new MapGenerator(3, 7);
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		//Background
		g.setColor(Color.black);
		g.fillRect(1,  1,  692, 592);
		
		//Map
		map.draw((Graphics2D)g);
		
		//Borders
		g.setColor(Color.cyan);
		g.fillRect(683,  0,  4,  592);
		g.fillRect(0,  0,  4,  592);
		g.fillRect(0,  0,  692,  4);
		
		//Score
		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.BOLD, 25));
		g.drawString("Score: "+score, 550, 35);
			
		//Paddle
		g.setColor(Color.magenta);
		g.fillRect(playerX,  550, 100, 8);
		
		//Ball
		g.setColor(Color.blue);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		//Round number
		g.setColor(Color.white);
		g.setFont(new Font("Times New Roman", Font.BOLD, 25));
		g.drawString("Round: "+round, 300, 35);
		
		//To begin playing
		if(totalBricks == 21 && play == false) {
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString("Press the arrow keys to begin!", 165, 300);
		}
		
		//Game is won
		if(round == 3 && totalBricks == 0) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString("You Won! Score: " + score, 210, 300);
			g.setFont(new Font("Times New Roman", Font.BOLD, 25));
			g.drawString("Press Enter to Play Again", 200, 350);
		}
		
		//Proceed to next level
		else if(totalBricks == 0) {
			round += 1;
			delay -= 1;
			timer.stop();
			timer = new Timer(delay, this);
			timer.start();
			paddleSpeed += 20;
			
			ballposX = 120;
			ballposY = 350;
			ballXdir = -1;
			ballYdir = -2;
			playerX = 310;
			totalBricks = 21;
			map = new MapGenerator(3, 7);
			
			repaint();
		}
		
		//If ball misses paddle, game over
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("Times New Roman", Font.BOLD, 30));
			g.drawString("Game Over, Score: " + score, 200, 300);
			g.setFont(new Font("Times New Roman", Font.BOLD, 25));
			g.drawString("Press Enter to Play Again", 200, 350);
		}
		
		g.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(play) {
			
			//If ball touches paddle
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = -ballYdir;
			}
			
			A: for(int i = 0; i < map.map.length; i++) {
				for(int j = 0; j < map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j*map.brickWidth+80;
						int brickY = i*map.brickHeight+50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--;
							score+=5;
								
							if(ballposX+19 <= brickRect.x || ballposX+1 >= brickRect.x + brickRect.width) {
								ballXdir = -ballXdir;
							}
							else {
								ballYdir = -ballYdir;
							}
							
							break A;
						}
					}
				}
			}
			
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			// If ball touches borders
			if(ballposX < 0) {
				ballXdir = -ballXdir;
			}
			if(ballposY < 0) {
				ballYdir = -ballYdir;
			}
			if(ballposX > 660) {
				ballXdir = -ballXdir;
			}
		}
		
		// Shows changes made to paddle (calls paint method)
		repaint();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//Where arrow keys will be detected
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			//Ensure does not leave panel size
			if(playerX >= 600 ) {
				playerX = 600;
			}
			else {
				moveRight();
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			}
			else {
				moveLeft();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				
				delay = 2;
				timer.stop();
				timer = new Timer(delay, this);
				timer.start();
				
				round = 1;
				paddleSpeed = 20;
				map = new MapGenerator(3, 7);
				
				repaint();
			}
		}
	}

	public void moveRight() {
		play = true;
		// Moves to the right
		playerX += paddleSpeed;
	}
	
	public void moveLeft() {
		play = true;
		// Moves to the left
		playerX -= paddleSpeed;
	}
	
	

}
