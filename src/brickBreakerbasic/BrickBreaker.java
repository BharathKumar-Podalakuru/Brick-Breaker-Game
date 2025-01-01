package brickBreakerbasic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BrickBreaker extends JPanel implements ActionListener,KeyListener{
	private Timer timer;
	private boolean gameRunning;
	private int ballX,ballY,ballDirectionX,ballDirectionY;
	private int paddleX;
	private int paddleWidth=100,paddleHeight=10;
	private int ballDiameter=20;
	private int brickWidth=60,brickHeight=30;
	private boolean[][] bricks;
	
	public BrickBreaker() {
		this.setPreferredSize(new Dimension(800,600));
		this.setBackground(Color.BLACK);
		this.setFocusable(true);
		this.addKeyListener(this);
		timer=new Timer(5,this);
		intializeGame();
	}

	private void intializeGame() {
		ballX=400;
		ballY=300;
		ballDirectionX=2;
		ballDirectionY=2;
		paddleX=350;
		gameRunning=true;
		bricks=new boolean[5][10];
		for(int i=0;i<5;i++) {
			for(int j=0;j<10;j++) {
				bricks[i][j]=true;
			}
		}
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i=0;i<5;i++) {
			for(int j=0;j<10;j++) {
				if(bricks[i][j]) {
					g.setColor(Color.RED);
					g.fillRect(j*brickWidth+10, i*brickHeight+50, brickWidth, brickHeight);
				}
			}
		}
		g.setColor(Color.GREEN);
		g.fillRect(paddleX, getHeight()-50, paddleWidth, paddleHeight);
		g.setColor(Color.YELLOW);
		g.fillOval(ballX, ballY, ballDiameter, ballDiameter);
		if(!gameRunning) {
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial",Font.BOLD,30));
			if(areAllBricksBroken()) {
				g.drawString("You WIn!", 330, 300);
			}
			else {
				g.drawString("Game Over!",330,300);
			}
		}
	}

	private boolean areAllBricksBroken() {
		for(int i=0;i<5;i++) {
			for(int j=0;j<10;j++) {
				if(bricks[i][j]) {
					return false;
				}
			}
		}
		return true;
	}
	public void moveBall() {
		ballX+=ballDirectionX;
		ballY+=ballDirectionY;
		if(ballX<=0 || ballX>=getWidth()-ballDiameter) {
			ballDirectionX=-ballDirectionX;
		}
		if(ballY<=0) {
			ballDirectionY=-ballDirectionY;
		}
		if(ballY>=getHeight()-ballDiameter) {
			gameRunning=false;
		}
	}

	public static void main(String[] args) {
		JFrame frame=new JFrame("BrickBreaker");
		BrickBreaker gamePanel=new BrickBreaker();
		frame.setVisible(true);
		frame.add(gamePanel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		gamePanel.timer.start();

	}
	private void checkCollisions() {
		if(new Rectangle(ballX,ballY,ballDiameter,ballDiameter).intersects(new Rectangle(paddleX,getHeight()-50,paddleWidth,paddleHeight))) {
			ballDirectionY=-ballDirectionY;
		}
		for(int i=0;i<5;i++) {
			for(int j=0;j<10;j++) {
				if(bricks[i][j]) {
					Rectangle brickRect=new Rectangle(j*brickWidth+10, i*brickHeight+50, brickWidth, brickHeight);
						if(new Rectangle(ballX,ballY,ballDiameter,ballDiameter).intersects(brickRect)) {
							bricks[i][j]=false;
							ballDirectionY=-ballDirectionY;
						}
				}
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode=e.getKeyCode();
		if(keyCode==KeyEvent.VK_LEFT && paddleX>0) {
			paddleX-=15;
		}
		else if(keyCode==KeyEvent.VK_RIGHT && paddleX<getWidth()-paddleWidth) {
			paddleX+=15;
		}
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(gameRunning) {
			moveBall();
			checkCollisions();
			if(areAllBricksBroken()) {
				gameRunning=false;
			}
			repaint();
		}
		
	}

}
