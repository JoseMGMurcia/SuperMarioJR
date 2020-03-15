package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class WinScreen extends BaseScreen {

    private Stage stage;


    public WinScreen(MainGame game) {
        super(game);
        stage = new Stage(new FitViewport(640, 360));
    }


    @Override
    public void show() {
        stage.setDebugAll(true);
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 0.4f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

        stage.dispose();

    }
}

