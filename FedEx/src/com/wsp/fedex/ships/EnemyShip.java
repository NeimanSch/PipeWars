package com.wsp.fedex.ships;

import java.util.Iterator;
import java.util.Random;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.wsp.fedex.fedex;
import com.wsp.fedex.objects.Beam;
import com.wsp.fedex.screens.GameScreen;

public class EnemyShip extends Ship {
	
	Texture enemyTexture;
	Texture enemyBeam;
	
	public boolean readyToShoot;
	
	private Sprite enemy;
	private int velocityY = -200;
	private int velocityX = 200;
	private long lastShotTime;
	private GameScreen gameScreen;
	Random randomNum = new Random();
	
	public EnemyShip(GameScreen screen) {
		enemyTexture = new Texture(Gdx.files.internal("img/eship_1.png"));
		enemyBeam = new Texture(Gdx.files.internal("img/Ebeam.png"));
				
		gameScreen = screen;
		
		enemy = new Sprite(enemyTexture);
		enemy.setPosition(MathUtils.random(0, 720 - 64),1280);
	    enemy.setSize(64,64);
	    
	    readyToShoot = true;
	}

	@Override
	public void shoot() {
		Beam beam = new Beam(enemyBeam, enemy.getX() + enemy.getWidth() / 3, enemy.getY() - enemy.getHeight() / 2, velocityX / 2, velocityY - 125);
		gameScreen.beams.add(beam);
        lastShotTime = TimeUtils.nanoTime();
        readyToShoot = false;
	}

	@Override
	public void move() {
		
		if(((TimeUtils.nanoTime() - lastShotTime) > 1000000000) || (randomNum.nextInt() > 1000000000) && 
			(randomNum.nextInt() > 1000000000) && (randomNum.nextInt() > 1000000000)) readyToShoot = true;
		
		enemy.translateY(velocityY * Gdx.graphics.getDeltaTime());
		enemy.translateX(velocityX * Gdx.graphics.getDeltaTime());
		
		if((enemy.getX() >= 720 - 64) || (enemy.getX() <= 0)) {
			velocityX = -velocityX;
		}
		
	}
	
	public Array<Beam> getBeams() {
		return gameScreen.beams;
	}

	@Override
	public void dispose(SpriteBatch batch, fedex gameIn) {
		
	}

	@Override
	public void draw(SpriteBatch batch) {
		enemy.draw(batch);
	}

	@Override
	public Rectangle getRectangle() {
		return enemy.getBoundingRectangle();
	}
	
	@Override
	public float getX() {
		return enemy.getX();
	}
	
	@Override
	public float getY() {
		return enemy.getY();
	}

	@Override
	public boolean checkShipCollision(Rectangle player) {
		return player.overlaps(enemy.getBoundingRectangle());
	}
	
	@Override
	public boolean checkBeamCollision(Array<Beam> beams) {
		Iterator<Beam> beamIter = beams.iterator();
    	while(beamIter.hasNext()){
        	Beam beam = beamIter.next();
        	if (enemy.getBoundingRectangle().overlaps(beam.getRectangle())) {
        		beamIter.remove();
        		return true;
        	}
        }
    	return false;
	}

}
