package UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import GameManager.GameManager;

public class Header extends JPanel implements ActionListener{
	private JButton pauseButton;
	private JButton resumeButton;
	private JButton saveButton;
	private JLabel scoreLabel;
	private int score = 0;
	private JLabel livesLabel;
	private int lives = 3;
	
	private JLabel remainingSimpleAsteroidsLabel;
	private JLabel remainingFirmAsteroidsLabel;
	private JLabel remainingExplosiveAsteroidsLabel;
	private JLabel remainingGiftAsteroidsLabel;
	
	private boolean pause = false;
	
	public Header() {
		
		setBackground(Color.RED);
		this.setLayout(null);
		
		this.pauseButton = new JButton("Pause");
		pauseButton.setFocusable(false);
		pauseButton.addActionListener(this);
		pauseButton.setSize(100,30);
		pauseButton.setLocation(810, 0);
		add(pauseButton);
		
		this.resumeButton = new JButton("Resume");
		resumeButton.setFocusable(false);
		resumeButton.addActionListener(this);
		resumeButton.setSize(100,30);
		resumeButton.setLocation(910,0);
		add(resumeButton);
		
		this.saveButton = new JButton("Save");
		saveButton.setFocusable(false);
		saveButton.addActionListener(this);
		saveButton.setSize(100,30);
		saveButton.setLocation(1010,0);
		add(saveButton);
		
		this.scoreLabel = new JLabel("Score = 0");
		scoreLabel.setFocusable(false);
		scoreLabel.setSize(150, 80);
		scoreLabel.setLocation(10,10);
		add(scoreLabel);
		
		this.livesLabel = new JLabel("Lives = 0");
		livesLabel.setFocusable(false);
		livesLabel.setSize(150, 80);
		livesLabel.setLocation(10,50);
		add(livesLabel);
		
		initializeAsteroidsLabel();
		
	}
	
	/*
	 * Initialize
	 */
	
	private void initializeSimpleAsteroidsLabel() {
		this.remainingSimpleAsteroidsLabel = new JLabel("Simple Asteroids = ");
		remainingSimpleAsteroidsLabel.setFocusable(false);
		remainingSimpleAsteroidsLabel.setSize(200, 80);
		remainingSimpleAsteroidsLabel.setLocation(150,10);
		add(remainingSimpleAsteroidsLabel);
	}
	
	private void initializeFirmAsteroidsLabel() {
		this.remainingFirmAsteroidsLabel = new JLabel("Firm Asteroids = ");
		remainingFirmAsteroidsLabel.setFocusable(false);
		remainingFirmAsteroidsLabel.setSize(200, 80);
		remainingFirmAsteroidsLabel.setLocation(150,30);
		add(remainingFirmAsteroidsLabel);
	}
	
	private void initializeExplosiveAsteroidsLabel() {
		this.remainingExplosiveAsteroidsLabel = new JLabel("Explosive Astereoids = ");
		remainingExplosiveAsteroidsLabel.setFocusable(false);
		remainingExplosiveAsteroidsLabel.setSize(200, 80);
		remainingExplosiveAsteroidsLabel.setLocation(150,50);
		add(remainingExplosiveAsteroidsLabel);
	}
	
	private void initializeGiftAsteroidsLabel() {
		this.remainingGiftAsteroidsLabel = new JLabel("Gift Asteroids = ");
		remainingGiftAsteroidsLabel.setFocusable(false);
		remainingGiftAsteroidsLabel.setSize(200, 80);
		remainingGiftAsteroidsLabel.setLocation(150,70);
		add(remainingGiftAsteroidsLabel);
	}
	
	private void initializeAsteroidsLabel() {
		initializeSimpleAsteroidsLabel();
		initializeFirmAsteroidsLabel();
		initializeExplosiveAsteroidsLabel();
		initializeGiftAsteroidsLabel();
	}
	
	/*
	 * Update Labels
	 */
	private void updateScoreLabel() {
		scoreLabel.setText("Score =" + score);
	}
	
	private void updateLivesLabel() {
		livesLabel.setText("Lives = " + lives);
	}
	
	public boolean isPause() {
		return pause;
	}
	
		@Override
	public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
		if (e.getSource() == pauseButton ) {
				pause = true;
		}
		else if (e.getSource() == resumeButton ) {
			pause = false;
		}
		else if (e.getSource() == saveButton ) {
			((GameWindow) SwingUtilities.getWindowAncestor(this)).getGamePanel().getGameManager().addSavegameToDatabase();
			((GameWindow) SwingUtilities.getWindowAncestor(this)).getGamePanel().getGameManager().saveAsteroidsToDatabase();
			((GameWindow) SwingUtilities.getWindowAncestor(this)).getGamePanel().getGameManager().setHasBeenSaved(true);
		}
	}
}
