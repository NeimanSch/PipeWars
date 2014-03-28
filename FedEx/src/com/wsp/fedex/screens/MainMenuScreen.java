package com.wsp.fedex.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.wsp.fedex.fedex;

public class MainMenuScreen implements Screen {
	
	final fedex game;
	
	OrthographicCamera camera;
	
	public MainMenuScreen(final fedex gam){
		game = gam;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 720, 1280);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.setScale(2);
        game.font.draw(game.batch, "Welcome to Pipe Wars!!! ", 200, 640);
        game.font.draw(game.batch, "Tap anywhere to begin!", 200, 600);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
		
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
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
		
	}

}
