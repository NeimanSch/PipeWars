package com.wsp.fedex;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.wsp.fedex.screens.MainMenuScreen;
import com.wsp.fedex.screens.SplashScreen;

public class fedex extends Game {
	
	public SpriteBatch batch;
	
	public BitmapFont font;
	
	@Override
	public void create() {		
		batch = new SpriteBatch();
		
		font = new BitmapFont();
		this.setScreen(new SplashScreen(this));
	}

	@Override
	public void dispose() {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void render() {		
		super.render();
		
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
