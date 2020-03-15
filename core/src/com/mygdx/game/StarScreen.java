package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class StarScreen extends BaseScreen{

    private Stage stage;

    private Texture background;
    private TextButton play, exit;
    private Skin skin;


    public StarScreen(MainGame game) {
        super(game);

        this.stage = new Stage(new FitViewport(640, 360));

        // imagen de fondo
        this.background = game.getAssetManager().get("images/welcome.png");

        // apariencia de los botones
        this.skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // inicialización de los botones
        play = new TextButton("JUGAR", skin);
        exit = new TextButton("SALIR", skin);

        //se añaden funciones a cada botón
        play.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
               StarScreen.this.game.resetGame();
            }
        });

        exit.addCaptureListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.exit(0);
            }
        });

        //disposición de tamaño y posiciones de los botones
        play.setSize(100, 40);
        exit.setSize(100, 40);
        play.setPosition(40, 270);
        exit.setPosition(40, 210);

        // se añaden los botones
        stage.addActor(play);
        stage.addActor(exit);



    }


    @Override
    public void show() {
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        //sonido de comienzo
        game.getSounds().playStartound();


    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

        //se para el sonido antes de cambiar de pantalla
        game.getSounds().stopStartound();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();


        //se dibuja la imagen de fondo antes del stage.draw();
        stage.getBatch().begin();
        stage.getBatch().draw(background,stage.getCamera().position.x-stage.getCamera().viewportWidth/2,stage.getCamera().position.y- stage.getCamera().viewportHeight/2,
                stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);
        stage.getBatch().end();

        // Se dibujan los botones como actores despues del fondo para que muestren sobre este
        stage.draw();


    }

    @Override
    public void dispose() {

        stage.clear();
        stage.dispose();
        skin.dispose();

    }
}
