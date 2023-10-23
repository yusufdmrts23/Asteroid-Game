package UI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import Domain.Ball.Ball;
import Domain.Paddle.Paddle;
import GameManager.GameController;
import GameManager.GameManager;

public class GameWindow extends JFrame {

	private GamePanel gamePanel;
	private Header header;
	private GameController gameController;
	
	public GameWindow(GameManager gameManager, Ball ball, Paddle paddle) {
		super("Outer Space Game");
		this.setLayout(null);
		gamePanel = new GamePanel(gameManager, ball, paddle);
		gamePanel.setSize(1820,840);
		gamePanel.requestFocus();
		gamePanel.setFocusable(true);
		gamePanel.setFocusTraversalKeysEnabled(false);
		add(gamePanel);
		
		header = new Header();
		header.setSize(1820, 200);
		header.setLocation(0, 840);
		add(header);
		
		this.gameController = new GameController(gamePanel, header);
		this.gameController.startGame();
	}
	
	private class KeyListenerHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		}
	}

	public GamePanel getGamePanel() {
		return gamePanel;
	}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}
}
