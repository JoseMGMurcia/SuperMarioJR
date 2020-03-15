package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdxgame.actors.MonedaActor;

import java.util.ArrayList;
import java.util.List;

public class VictoryScreen extends BaseScreen {

    private Stage stage;
    private float accumDelta;
    private Texture background;
    private List<MonedaActor> coins = new ArrayList<MonedaActor>();
    private ArrayList<Texture> coinTextures;
    private World world;
    private BitmapFont score, vidas, tiempo,total, record;
    private String puntuacionString, vidasString, tiempoString, totalString, recordString;
    private int totalScoreInt, recordInt;

    public VictoryScreen(MainGame game) {
        super(game);
        this.stage = new Stage(new FitViewport(640 * 2, 360 * 2));

        // imagen de fondo
        this.background = game.getAssetManager().get("images/pantallaWin.png");
        this.world = new World(new Vector2(0, -10), true);

        // se define el estilo de los marcadores
        score = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        vidas = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        tiempo = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        total = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        record = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);

        // calculo de la puntuacion
        totalScoreInt = game.getScore() + game.getVidasInt() * Constantes.SCORE_VIDA + game.getTiemppoRestante() * Constantes.SCORE_SECOND;
        // se obtiene el record anterior
        recordInt = game.getPreferences();

        // posible cambio de record
        if (totalScoreInt > recordInt) {
            recordInt = totalScoreInt;
            game.savePreferences(recordInt);
        }

        // formateo de los mensajes a mostrar
        puntuacionString = String.format("%s. %s", foramtearString("Score", Integer.toString(game.getScore())), game.getScore());
        vidasString = String.format("%s... %s", foramtearString("Lives", Integer.toString(game.getVidasInt())), game.getVidasInt());
        tiempoString = String.format("%s... %s", foramtearString("Time", Integer.toString(game.getTiemppoRestante())), game.getTiemppoRestante());
        totalString = String.format("%s. %s", foramtearString("Score", Integer.toString(totalScoreInt)), totalScoreInt);
        recordString = String.format("%s %s", foramtearString("Record", Integer.toString(recordInt)), recordInt);

    }

    //se añaden los puntos necesarios
    public String foramtearString(String palabra, String puntos) {
        for (int i = 0; palabra.length() + puntos.length() < 25; i++) {
            palabra = String.format("%s.",palabra);
        }
        return palabra;
    }


    @Override
    public void show() {
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        //Carga textura monedas
        coinTextures = new ArrayList<>(6);
        coinTextures.add((Texture) game.getAssetManager().get("images/coin1.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin2.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin3.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin4.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin5.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin6.png"));

        //se añaden las monedas necesarias
        for (int i = 0; i < 12; i++) {
            coins.add(new MonedaActor(world, new Vector2(14.5f + i, 12f), coins, game.getSounds(), coinTextures));
            coins.add(new MonedaActor(world, new Vector2(14.5f + i, 7f), coins, game.getSounds(), coinTextures));
            if (i < 4) {
                coins.add(new MonedaActor(world, new Vector2(14.5f, 8f + i), coins, game.getSounds(), coinTextures));
                coins.add(new MonedaActor(world, new Vector2(25.5f, 8f + i), coins, game.getSounds(), coinTextures));
            }
        }
        for (MonedaActor coin : coins)
            stage.addActor(coin);

        game.getSounds().playVictorySound();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        for (MonedaActor coin : coins)
            coin.detach();
        game.getSounds().stopVictorySound();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //control del tiempo total
        accumDelta = accumDelta + delta;
        stage.act();

        // se dibujan los marcadores
        stage.getBatch().begin();
        stage.getBatch().draw(background, stage.getCamera().position.x - stage.getCamera().viewportWidth / 2, stage.getCamera().position.y - stage.getCamera().viewportHeight / 2,
                stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);
        score.draw(stage.getBatch(), puntuacionString, stage.getCamera().position.x + 100,
                stage.getCamera().position.y + 160);
        vidas.draw(stage.getBatch(), vidasString, stage.getCamera().position.x + 100,
                stage.getCamera().position.y + 130);
        tiempo.draw(stage.getBatch(), tiempoString, stage.getCamera().position.x + 100,
                stage.getCamera().position.y + 100);
        total.draw(stage.getBatch(), totalString, stage.getCamera().position.x + 100,
                stage.getCamera().position.y + 70);
        record.draw(stage.getBatch(), recordString, stage.getCamera().position.x + 100,
                stage.getCamera().position.y + 15);
        stage.getBatch().end();
        stage.draw();

        // control para salir de la pantalla
        if (accumDelta > 30 || (accumDelta > 4 && Gdx.input.justTouched())) {
            accumDelta = 0;
            game.goHome();
        }
    }

    @Override
    public void dispose() {
        stage.clear();
        stage.dispose();
        world.dispose();
        for (Texture texture : coinTextures)
            texture.dispose();
    }
}
