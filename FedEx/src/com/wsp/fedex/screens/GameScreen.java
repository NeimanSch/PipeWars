package com.wsp.fedex.screens;


import java.util.Iterator;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.wsp.fedex.fedex;
import com.wsp.fedex.ships.PlayerShip;
import com.wsp.fedex.ships.EnemyShip;
import com.wsp.fedex.objects.Beam;

public class GameScreen implements Screen{
	
	final fedex game;
	
	Texture eship_1;
	Texture pipe_o;
	Texture blaster;
	Sound explosion;
	Music engine;
	OrthographicCamera cam;
	PlayerShip ship;
	SpriteBatch batch;
	Array<EnemyShip> fleet;
	Animation shipExplosionAnimation;
	Texture shipExplosionTexture;
	TextureRegion shipExplosionTextureRegion;
	long lastShipTime;
	long lastBeamTime;
	long lastEnemyBeamTime;
	public Array<Beam> beams;
	Random randomNum = new Random();
	int shipsDestroyed;
	int maxBeams;
	boolean useAccelerometer = true;
	private float elapsedTime = 0;
	boolean dead = false;

	 public GameScreen(final fedex game) {
	        this.game = game;
	        dead = false;
	        
	        TextureRegion[][] explosionRegion = new TextureRegion[1][4];
	        
	        shipExplosionTexture = new Texture(Gdx.files.internal("img/explosion_a.png"));
	        shipExplosionTextureRegion = new TextureRegion(shipExplosionTexture);
	        explosionRegion = shipExplosionTextureRegion.split(64, 64);
	        shipExplosionAnimation = new Animation(1/15f, explosionRegion[0]);
	        
                beams = new Array<Beam>();

	        // load the explosion sound effect and the ship engine
	        explosion = Gdx.audio.newSound(Gdx.files.internal("sound/blasters.wav"));
	        engine = Gdx.audio.newMusic(Gdx.files.internal("sound/ship_engine.wav"));
	        engine.setLooping(true);

	        // create the camera and the SpriteBatch
	        cam = new OrthographicCamera();
	        cam.setToOrtho(false, 720, 1280);

	        // create a Sprite to logically represent the player's ship
	        batch = new SpriteBatch();
	        
	        ship = new PlayerShip();

	        // create the fleet array and spawn the first ship
	        fleet = new Array<EnemyShip>();
	        spawnEnemyShip(this);

	    }
	 
	    private void spawnEnemyShip(GameScreen screen) {
	        EnemyShip eShip = new EnemyShip(screen);
	        fleet.add(eShip);
	        lastShipTime = TimeUtils.nanoTime();
	    }

	    @Override
	    public void render(float delta) {
	        // clear the screen with a dark blue color. The
	        // arguments to glClearColor are the red, green
	        // blue and alpha component in the range [0,1]
	        // of the color to be used to clear the screen.
	        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	        // tell the camera to update its matrices.
	        cam.update();
	        
	        elapsedTime += Gdx.graphics.getDeltaTime();

	        // tell the SpriteBatch to render in the
	        // coordinate system specified by the camera.
	        game.batch.setProjectionMatrix(cam.combined);
	        game.batch.begin();
	        game.font.draw(game.batch, "Ships  Destroyed: " + shipsDestroyed, 0, 1280);
	        game.font.draw(game.batch, "Health: " + ship.getHealth(), 0, 1240);
	        game.batch.end();
	        
	        batch.setProjectionMatrix(cam.combined);
	        batch.begin();
	        ship.draw(batch);
	        
	        for (EnemyShip eShip : fleet) {
	            eShip.draw(batch);
	        }
	        
	        for (Beam beam : beams) {
	        	beam.draw(batch);
	        }

	        ship.move();
	        
	        // shoot on space or touch
	        if((Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isTouched()) && (TimeUtils.nanoTime() - lastBeamTime > 1000000000/5)){
	        	ship.shoot();
	        	lastBeamTime = TimeUtils.nanoTime();
	        }

	        // check if we need to create a new enemy ship
	        if (TimeUtils.nanoTime() - lastShipTime > 1000000000)
	            spawnEnemyShip(this);
	        
	        Iterator<Beam> beam_iter = beams.iterator();
		    while(beam_iter.hasNext()){
		    	Beam beam = beam_iter.next();
		    	beam.move();
		    	if((beam.getY()+64)<=0){
		    		beam_iter.remove();
		        }
		    }

	        // move the fleet, remove any that are beneath the bottom edge of
	        // the screen or that hit the ship. In the later case we increase the 
	        // value our ships destroyed counter and add a sound effect.
	        Iterator<EnemyShip> iter = fleet.iterator();
	        while (iter.hasNext()) {
	        	if(dead){
	        		break;
	        	}
	        	EnemyShip enemy = iter.next();
	        	if(enemy.readyToShoot) {
	        		enemy.shoot();
	        		lastEnemyBeamTime = TimeUtils.nanoTime();
	        	}
	        	if(enemy.checkBeamCollision(ship.getBeams())) {
	        		shipsDestroyed++;
	        		batch.draw(shipExplosionAnimation.getKeyFrame(elapsedTime, false),  enemy.getX(),  enemy.getY());
	        		ship.setScore();
	        		explosion.play();
	        		iter.remove();
	        		break;
	        	}
	        	if(ship.checkBeamCollision(enemy.getBeams())) {
	        		explosion.play();
	        		ship.loseHealth();
	        		if(ship.getHealth()<=0){
	        			dead = true;
	        			batch.draw(shipExplosionAnimation.getKeyFrame(elapsedTime, false),  ship.getX(),  ship.getY());
	        			ship.dispose(batch, game);
	        		}
	        	}
	        	
	        	enemy.move();
	            if ((enemy.getY() + 64) < 0)
	                iter.remove();
	            
	            if(ship.checkShipCollision(enemy.getRectangle())) {
	            	ship.loseHealth();
	            	ship.loseHealth();
	                explosion.play();
	                iter.remove();
	            }	        	
	        }
	        
	        batch.end();
	    }

	    @Override
	    public void resize(int width, int height) {
	    }

	    @Override
	    public void show() {
	        // start the playback of the ships engine
	        // when the screen is shown
	        engine.play();
	    }

	    @Override
	    public void hide() {
	    }

	    @Override
	    public void pause() {
	    }

	    @Override
	    public void resume() {
	    }

	    @Override
	    public void dispose() {
	        eship_1.dispose();
	        pipe_o.dispose();
	        explosion.dispose();
	        engine.dispose();
	        blaster.dispose();
	        batch.dispose();
	    }


}
