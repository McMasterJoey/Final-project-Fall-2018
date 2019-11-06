package spaceShooter;

import java.util.ArrayList;
import java.util.Observable;

import controller.GameControllerView;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;

public class SpaceShooterControllerView extends GameControllerView {
	
	private AnimationTimer gameClock;
	private SpaceShooterModel model;
	private SpaceShooterPlayer player;
	private ArrayList<AudioClip> soundfx = new ArrayList<AudioClip>();
	
	public SpaceShooterControllerView() {
		setUpGameClock();
		
	}
	
	private void setUpGameClock() {
		gameClock = new AnimationTimer() {
			@Override
			public void handle(long now) {				
				tick();
			}			
		};
	}

	private void tick() {
		// TODO Auto-generated method stub				
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void updateStatistics() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean loadSaveGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pauseGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unPauseGame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean newGame() {
		// TODO Auto-generated method stub
		return false;
	}

}
