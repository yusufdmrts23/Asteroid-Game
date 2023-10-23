package GameManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import UI.GamePanel;
import UI.Header;

public class GameController {
	
	private GamePanel gamePanel;
	private Header header;
	private GameManager gameManager;
	
	private Timer timer = new Timer(10, new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (gameManager.isPlay()) {
				gameManager.moveBall();
				gameManager.moveSimpleAsteroids();
				gameManager.moveFirmAsteroids();
				gameManager.moveExplosiveAsteroids();
				gameManager.moveGiftAsteroids();
				gameManager.performProtectingAlien();
				//stopAndResumeGame();
			}
			gamePanel.repaint();
		}
	});
	
	public GameController(GamePanel gamePanel,Header header) {
		this.gamePanel = gamePanel;
		this.header = header;
		this.gameManager = this.gamePanel.getGameManager();
	}
	
	public void startGame() {
		timer.start();
	}
	
	private void stopAndResumeGame() {
		
	}
	
}
