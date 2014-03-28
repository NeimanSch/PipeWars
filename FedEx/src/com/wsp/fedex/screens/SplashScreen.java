package com.wsp.fedex.screens;

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.wsp.fedex.fedex;
import com.wsp.fedex.tween.SpriteAccessor;

public class SplashScreen  implements Screen {
 
	final fedex game;
	
	private SpriteBatch batch;
	private Sprite splash;
	private TweenManager tweenManager;
	Music splash_sound;
	
	OrthographicCamera camera;
	
	public SplashScreen(final fedex gam){
		game = gam;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 400);
		
		splash_sound = Gdx.audio.newMusic(Gdx.files.internal("sound/spaceship.wav"));
        splash_sound.setLooping(false);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		tweenManager.update(delta);
		
		batch.begin();
		splash.draw(batch);
		batch.end();
		
		if(!splash_sound.isPlaying()){
			game.setScreen(new MainMenuScreen(game));
            dispose();
		}
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		splash_sound.play();
		
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		
		Texture splashTexture = new Texture("img/splash.png");
		splash = new Sprite(splashTexture);
		splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		Timeline.createSequence()
			.push(Tween.set(splash, SpriteAccessor.ALPHA).target(0))
			.beginParallel()
				.push(Tween.to(splash, SpriteAccessor.ALPHA, 2).target(1))
				.push(Tween.to(splash, SpriteAccessor.ALPHA, 2).target(0).delay(4))
			.repeat(-1, 0)
			.start(tweenManager);
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
		splash_sound.dispose();
		
		
	}

}
