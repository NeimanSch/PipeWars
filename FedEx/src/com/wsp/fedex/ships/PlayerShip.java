package com.wsp.fedex.ships;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.wsp.fedex.fedex;
import com.wsp.fedex.objects.Beam;
import com.wsp.fedex.screens.GameOverScreen;
import com.wsp.fedex.screens.GameScreen;
import com.wsp.fedex.screens.MainMenuScreen;

public class PlayerShip extends Ship {

	Texture pipe;
	Texture pipe_l;
	Texture pipe_r;
	
	private Sprite player;
	private int health = 100;
	private int velX = 400;
	private int velY = 400;
	private boolean useAccelerometer = true;
	private Array<Beam> beams = new Array<Beam>();
	private int score = 0;
	
	public PlayerShip() {
		pipe   = new Texture(Gdx.files.internal("img/pipe_of.png"));
		pipe_l = new Texture(Gdx.files.internal("img/orpipe_lf.png"));
		pipe_r = new Texture(Gdx.files.internal("img/orpipe_rf.png"));
		
		player = new Sprite(pipe);
        player.setSize(64,128);
        player.setPosition(((720/2)-(64/2)), 20);
	}
	
	@Override
	public void shoot() {
		// TODO Auto-generated method stub
 		Beam beam = new Beam(player.getX()+player.getWidth()/2, player.getY()+player.getHeight(), 0, velY + 200);
        beams.add(beam);
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		 // process user input
        if (useAccelerometer){
        	if(Gdx.input.getAccelerometerX()>0 || Gdx.input.isKeyPressed(Keys.LEFT)){
        		player.setTexture(pipe_l);
        	}  
        	else if(Gdx.input.getAccelerometerX()<0 || Gdx.input.isKeyPressed(Keys.RIGHT)){
        		player.setTexture(pipe_r);
        	}
        	else {
        		player.setTexture(pipe);
        	}
        	player.translate(Gdx.input.getAccelerometerX()*-2, Gdx.input.getAccelerometerY()*-2);
        }
        
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            player.translateX(-velX * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
        	player.translateX(velX * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.UP))
        	player.translateY(velY * Gdx.graphics.getDeltaTime());
        if (Gdx.input.isKeyPressed(Keys.DOWN))
        	player.translateY(-velY * Gdx.graphics.getDeltaTime());

        // make sure the player's ship stays within the screen bounds
        if (player.getX() < 0)
            player.setX(0);
        if (player.getX() > 720 - 64)
            player.setX(720 - 64);
        if (player.getY() < 0)
            player.setY(0);
        if (player.getY() > 1280 - 64)
            player.setY(1280 - 64);
        
        Iterator<Beam> beam_iter = beams.iterator();
        while(beam_iter.hasNext()){
        	Beam beam = beam_iter.next();
        	beam.move();
        	if((beam.getY()+64)>1280){
        		beam_iter.remove();
        	}
        }
		
	}
	
	public void loseHealth() {
		health -= 10;
		Gdx.app.log("health",  Integer.toString(health));
	}
	
	public int getHealth() {
		return health;
	}
	
	public Array<Beam> getBeams() {
		return beams;
	}
	
	@Override
	public boolean checkShipCollision(Rectangle enemy) {
		return enemy.overlaps(player.getBoundingRectangle());
	}
	
	@Override
	public boolean checkBeamCollision(Array<Beam> beams) {
		Iterator<Beam> beamIter = beams.iterator();
    	while(beamIter.hasNext()){
        	Beam beam = beamIter.next();
        	if (player.getBoundingRectangle().overlaps(beam.getRectangle())) {
        		beamIter.remove();
        		return true;
        	}
        }
    	return false;
	}

	@Override
	public void dispose(SpriteBatch batch, fedex gameIn) {
		pipe.dispose();
		pipe_l.dispose();
		pipe_r.dispose();
		gameIn.setScreen(new GameOverScreen(gameIn, score));
	}

	@Override
	public void draw(SpriteBatch batch) {
		player.draw(batch);
		for (Beam beam : beams) {
        	beam.draw(batch);
        }
	}
	
	public void setScore(){
		score += 10;
	}
	
	@Override
	public float getX() {
		return player.getX();
	}
	
	@Override
	public float getY() {
		return player.getY();
	}

	@Override
	Rectangle getRectangle() {
		// TODO Auto-generated method stub
		return player.getBoundingRectangle();
	}

}
