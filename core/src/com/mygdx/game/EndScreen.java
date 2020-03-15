package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdxgame.actors.MarioActor;
import com.mygdxgame.actors.SetaActor;
import com.mygdxgame.actors.SueloActor;
import com.mygdxgame.actors.TuberiaActor;
import com.mygdxgame.images.CastilloImagen;
import com.mygdxgame.images.NubeImagen;
import com.mygdxgame.images.SueloImagen;
import java.util.ArrayList;
import java.util.List;

public class EndScreen extends BaseScreen {

    private Stage stage;
    private World world;
    private float accumDelta;
    private CastilloImagen castillo;
    private Texture gameOver, nube1Texture, setaTexture, setaTexture2 , sueloTexture,  castilloTexture, tuberiaTexture;
    private List<SueloActor> suelos = new ArrayList<SueloActor>();
    private List<SueloImagen> suelosImg = new ArrayList<SueloImagen>();
    private List<SetaActor> setas = new ArrayList<SetaActor>();
    private List<TuberiaActor> tubierias = new ArrayList<TuberiaActor>();
    private List<NubeImagen> nubesImg = new ArrayList<NubeImagen>();
    private MarioActor mario;
    private ArrayList<Texture> marioTextures;



    public EndScreen(MainGame game) {
        super(game);
        this.stage = new Stage(new FitViewport(640, 360));
        this.world = new World(new Vector2(0, -10), true);
        this.accumDelta = 0;
        this.gameOver = game.getAssetManager().get("images/gameOver.png");

        //carga de teturas
        this.setaTexture = game.getAssetManager().get("images/seta.png");
        this.setaTexture2 = game.getAssetManager().get("images/seta2.png");
        this.sueloTexture = game.getAssetManager().get("images/suelo.png");
        this.castilloTexture = game.getAssetManager().get("images/castillo.png");
        this.tuberiaTexture = game.getAssetManager().get("images/tuboRoto.png");
        this.marioTextures = new ArrayList<>(7);
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        this.nube1Texture = game.getAssetManager().get("images/nubeOscura.png");
    }


    @Override
    public void show() {
        stage.setDebugAll(false);
        Gdx.input.setInputProcessor(stage);

        //disposición de elementos
        suelos.add(new SueloActor(world, sueloTexture, -20, 67, 1));
        suelosImg.add(new SueloImagen(sueloTexture, -20, 67, 1));
        castillo = new CastilloImagen(castilloTexture, 4, 1, 6, 6);

        tubierias.add(new TuberiaActor(world, tuberiaTexture, new Vector2(0, 1f)));
        tubierias.add(new TuberiaActor(world, tuberiaTexture, new Vector2(14, 1f)));

        // un actor mario es necesario, pero e colocado fuera de cámara
        mario = new MarioActor(world, marioTextures, new Vector2(-10, 3), game.getSounds());
        //poniendo salto a true no se le permite moverse
        mario.setSaltando(true);


        setas.add(new SetaActor(world, setaTexture, setaTexture2, new Vector2(3, 1.5f), mario, setas, game.getSounds()));
        setas.add(new SetaActor(world, setaTexture, setaTexture2, new Vector2(8, 1.5f), mario, setas, game.getSounds()));
        setas.add(new SetaActor(world, setaTexture, setaTexture2, new Vector2(11, 1.5f), mario, setas, game.getSounds()));

        nubesImg.add(new NubeImagen(nube1Texture, 2, 5, 3, 1.5f));
        nubesImg.add(new NubeImagen(nube1Texture, 9, 6, 3, 1.5f));

        stage.addActor(castillo);
        stage.addActor(mario);

        for (SueloActor suelo : suelos)
            stage.addActor(suelo);
        for (SueloImagen suelo : suelosImg)
            stage.addActor(suelo);
        for (TuberiaActor tubo: tubierias)
            stage.addActor(tubo);
        for (SetaActor seta: setas)
            stage.addActor(seta);
        for (NubeImagen nube: nubesImg)
            stage.addActor(nube);

        game.getSounds().playGameOverSound();
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);

        game.getSounds().stopGameOverSound();
        for (SueloActor suelo : suelos)
            suelo.detach();
        for (TuberiaActor tubo: tubierias)
            tubo.detach();
        for (SetaActor seta: setas)
            seta.detach();


        suelos.clear();
        setas.clear();
    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.108f, 0.09f, 0.51f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        accumDelta = accumDelta + delta;

        stage.act();
        stage.draw();

        //dibujo del cartel de game over
        stage.getBatch().begin();
        stage.getBatch().draw(gameOver,stage.getCamera().position.x-stage.getCamera().viewportWidth/2,stage.getCamera().position.y- stage.getCamera().viewportHeight/2,
                stage.getCamera().viewportWidth, stage.getCamera().viewportHeight);
        stage.getBatch().end();


        // si pasan 30 segundos o si se toca la pantalla despues de 4 vuelve a la pantalla de inicio
        if( accumDelta > 30 || ( accumDelta > 4 && Gdx.input.justTouched() ) ){
            accumDelta= 0;
            game.goHome();
        }
    }

    @Override
    public void dispose() {
        stage.dispose();
        world.dispose();
        setaTexture.dispose();
        setaTexture2.dispose();
        sueloTexture.dispose();
        tuberiaTexture.dispose();
        castilloTexture.dispose();
        nube1Texture.dispose();
        gameOver.dispose();
        for (Texture texture: marioTextures) {
            texture.dispose();
        }
    }
}