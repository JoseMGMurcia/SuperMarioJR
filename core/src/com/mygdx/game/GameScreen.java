package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdxgame.actors.MonedaActor;
import com.mygdxgame.actors.TuberiaActor;
import com.mygdxgame.images.CastilloImagen;
import com.mygdxgame.images.NubeGrandeImagen;
import com.mygdxgame.images.NubeImagen;
import com.mygdxgame.images.SueloImagen;
import com.mygdxgame.actors.CajaSorpresaActor;
import com.mygdxgame.actors.MarioActor;
import com.mygdxgame.actors.MuroActor;
import com.mygdxgame.actors.SetaActor;
import com.mygdxgame.actors.SueloActor;

import java.util.ArrayList;
import java.util.List;


public class GameScreen extends BaseScreen {

    private Stage stage;
    private World world;

    //actores
    private MarioActor mario;
    private MuroActor muro;
    private SetaActor seta;
    private CajaSorpresaActor caja;
    private MonedaActor coin;

    private List<SueloActor> suelos = new ArrayList<SueloActor>();
    private List<SetaActor> setas = new ArrayList<SetaActor>();
    private List<CajaSorpresaActor> cajas = new ArrayList<CajaSorpresaActor>();
    private List<TuberiaActor> tubierias = new ArrayList<TuberiaActor>();
    private List<MonedaActor> coins = new ArrayList<MonedaActor>();

    //texturas
    private Texture setaTexture, setaTexture2, sueloTexture, muroTexture, nube1Texture, nube2Texture, nube3Texture, cajaTexture, cajaVaciaTexture,
            tuberiaTexture, ladrilloTexture, senalTexture, castilloTexture, arbustoTexture, montanaTexture, castilloFinTexture, banderaTexture, monedaMarTexture, cabezaMarioTexture;

    private ArrayList<Texture> marioTextures, coinTextures;

    //imágenes
    private CastilloImagen castillo;
    private CastilloImagen castilloFinal;
    private NubeGrandeImagen nubeGrande;
    private NubeImagen monedaMar;
    private NubeImagen cabezaMar;
    private List<NubeImagen> arbustos = new ArrayList<NubeImagen>();
    private List<NubeImagen> nubesImg = new ArrayList<NubeImagen>();
    private List<SueloImagen> suelosImg = new ArrayList<SueloImagen>();

    // eje Y mínimo para la cámara y tiempo transcurrido desde el inicio de la animación de victoria.
    private float minimumY, winning;
    private boolean nuevaMoneda, victoria, firstTimeWinning, hurryUpTime;

    //marcadores
    private BitmapFont marcador;
    private int puntuacion;
    private BitmapFont vida;
    private int vidas;
    private BitmapFont tiempo;
    private float tiempoRestante;
    private BitmapFont moneda;
    private int monedas;

    //valores a mostrar
    private String puntuacionString, monedasString, tiempoString, vidasString;


    public GameScreen(MainGame game) {
        super(game);
        this.stage = new Stage(new FitViewport(640, 360));
        this.world = new World(new Vector2(0, -10), true);
        this.minimumY = stage.getCamera().position.y;
        this.winning = 0f;
        this.nuevaMoneda = false;
        this.victoria = false;
        this.firstTimeWinning = true;
        this.hurryUpTime = true;
        this.vidas = game.getVidasInt();
        this.puntuacion = 0;
        this.monedas = 0;
        this.tiempoRestante = 160f;
        this.vidasString = String.format("x0%s", vidas);



        marcador = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        vida = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        tiempo = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);
        moneda = new BitmapFont(Gdx.files.internal("font/fuente.fnt"), Gdx.files.internal("font/fuente_0.tga"), false);


        // Gestión de las colisiones
        world.setContactListener(new ContactListener() {
            @Override
            public synchronized void beginContact(Contact contact) {
                //colisiones con suelo, seta o tuberias hacen que mario deje de estar "saltando"
                if (hayColision(contact, "mario", "suelo") || hayColision(contact, "mario", "seta") || hayColision(contact, "mario", "tuberia")) {
                    mario.setSaltando(false);
                    if (Gdx.input.isTouched()) {
                        mario.setHayQueSaltar(true);
                    }
                    if (hayColision(contact, "mario", "seta")) {
                        seta = getSetaIn(contact);
                        if (seta.getY() + 0.5f * Constantes.PIXELS_IN_METER > mario.getY()) {
                            mario.setVivo(false);
                        } else {
                            seta.setVivo(false);
                            GameScreen.this.puntuacion = GameScreen.this.puntuacion + Constantes.SCORE_SETA;
                            mario.setSaltando(false);
                            mario.setHayQueSaltar(true);
                        }
                    }
                    // coliion con caja
                } else if (hayColision(contact, "mario", "caja")) {
                    caja = getCajaIn(contact);
                    if (!caja.isVacia() && mario.getBody().getLinearVelocity().y > 0.5f && mario.getBody().getPosition().y < caja.getBody().getPosition().y) {
                        GameScreen.this.caja.setVacia(true);
                        GameScreen.this.nuevaMoneda = true;
                    }
                    // colisión con moneda
                } else if (hayColision(contact, "mario", "coin")) {
                    coin = getCoinIn(contact);
                    if (!coin.isCogida()) {
                        GameScreen.this.puntuacion = GameScreen.this.puntuacion + Constantes.SCORE_COIN;
                        GameScreen.this.monedas = GameScreen.this.monedas + 1;
                    }
                    coin.setCogida(true);
                }
            }

            @Override
            public void endContact(Contact contact) {
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
            }

            private boolean hayColision(Contact c, Object obA, Object obB) {
                return (c.getFixtureA().getUserData().equals(obA) && c.getFixtureB().getUserData().equals(obB)) ||
                        (c.getFixtureA().getUserData().equals(obB) && c.getFixtureB().getUserData().equals(obA));
            }

            //obtiene la seta implicada en la colisión
             private SetaActor getSetaIn(Contact c) {
                for (SetaActor setaActor : setas) {
                    if (setaActor.getX() - mario.getX() < 100 && setaActor.getX() - mario.getX() > -100) {
                        return setaActor;
                    }
                }
                return null;
            }
            //obtiene la caja implicada en la colisión
            private CajaSorpresaActor getCajaIn(Contact c) {
                for (CajaSorpresaActor cajaSorpresaActor : cajas) {
                    if (cajaSorpresaActor.getX() - mario.getX() < 100 && cajaSorpresaActor.getX() - mario.getX() > -100) {
                        return cajaSorpresaActor;
                    }
                }
                return null;
            }
            //obtiene la moneda implicada en la colisión
            private MonedaActor getCoinIn(Contact c) {
                for (MonedaActor monedaActor : coins) {
                    if (monedaActor.getX() - mario.getX() < 100 && monedaActor.getX() - mario.getX() > -100) {
                        return monedaActor;
                    }
                }
                return null;
            }
        });

        //vidas = new Label();

    }

    @Override
    public void show() {
        stage.setDebugAll(false); // On true se renderizan los bordes verdes de los actores e imágenes
        marioTextures = new ArrayList<>(7);
        coinTextures = new ArrayList<>(6);

        //CARGA DE TEXTURAS
        //textura de mario
        marioTextures.add((Texture) game.getAssetManager().get("images/mario1Der.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/mario1Izq.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/mario2Der.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/mario2Izq.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/marioSaltDer.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/marioSaltIzq.png"));
        marioTextures.add((Texture) game.getAssetManager().get("images/mariom.png"));
        //textura actores
        setaTexture = game.getAssetManager().get("images/seta.png");
        setaTexture2 = game.getAssetManager().get("images/seta2.png");
        sueloTexture = game.getAssetManager().get("images/suelo.png");
        muroTexture = game.getAssetManager().get("images/suelo.png");
        cajaTexture = game.getAssetManager().get("images/caja.png");
        cajaVaciaTexture = game.getAssetManager().get("images/cajaVacia.png");
        coinTextures.add((Texture) game.getAssetManager().get("images/coin1.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin2.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin3.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin4.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin5.png"));
        coinTextures.add((Texture) game.getAssetManager().get("images/coin6.png"));

        // Texturas de imagenes
        nube1Texture = game.getAssetManager().get("images/nube1.png");
        nube2Texture = game.getAssetManager().get("images/nube2.png");
        nube3Texture = game.getAssetManager().get("images/nube3.png");
        tuberiaTexture = game.getAssetManager().get("images/tubo.png");
        ladrilloTexture = game.getAssetManager().get("images/ladrillo.png");
        castilloTexture = game.getAssetManager().get("images/castillo.png");
        castilloFinTexture = game.getAssetManager().get("images/castilloFinal.png");
        senalTexture = game.getAssetManager().get("images/senal.png");
        arbustoTexture = game.getAssetManager().get("images/arbusto.png");
        montanaTexture = game.getAssetManager().get("images/montana.png");
        banderaTexture = game.getAssetManager().get("images/bandera.png");

        //texturas de los marcadores
        monedaMarTexture = game.getAssetManager().get("images/monedaMar.png");
        cabezaMarioTexture = game.getAssetManager().get("images/marioCabeza.png");


        //se inicializan los actores
        muro = new MuroActor(world, muroTexture, -0.5f, 40, 0);
        mario = new MarioActor(world, marioTextures, new Vector2(1.5f, 3), game.getSounds());

        //imagenes de principio y fin
        castillo = new CastilloImagen(castilloTexture, -10, 1, 10, 10);
        castilloFinal = new CastilloImagen(castilloFinTexture, 228, 1, 5, 5);

        //disposición aleatoria de todos los elementos
        worldReset();


        //comienza la música
        game.getSounds().playMusicTheme();
    }

    public void worldReset() {

        suelos.add(new SueloActor(world, sueloTexture, -20, 67, 1));
        suelos.add(new SueloActor(world, sueloTexture, 50, 47, 1));
        suelos.add(new SueloActor(world, sueloTexture, 100, 47, 1));
        suelos.add(new SueloActor(world, sueloTexture, 150, 47, 1));
        suelos.add(new SueloActor(world, sueloTexture, 200, 46, 1));


        suelosImg.add(new SueloImagen(sueloTexture, -20, 67, 1));
        suelosImg.add(new SueloImagen(sueloTexture, 50, 47, 1));
        suelosImg.add(new SueloImagen(sueloTexture, 100, 47, 1));
        suelosImg.add(new SueloImagen(sueloTexture, 100, 47, 1));
        suelosImg.add(new SueloImagen(sueloTexture, 150, 47, 1));
        suelosImg.add(new SueloImagen(sueloTexture, 200, 46, 1));

        for (int i = 4; i < 224; i = i + 16) {
            int random = (int) (Math.random() * 5 + 1);
            suelos.add(new SueloActor(world, ladrilloTexture, i, random, 4));
            suelosImg.add(new SueloImagen(ladrilloTexture, i, random, 4));
            if (random > 2) {
                cajas.add(new CajaSorpresaActor(world, cajaTexture, cajaVaciaTexture, new Vector2(i + 1.5f, 3.5f)));
            }
            if (random == 5) {
                suelos.add(new SueloActor(world, ladrilloTexture, i + 8, random, 6));
                suelosImg.add(new SueloImagen(ladrilloTexture, i + 8, random, 6));
                coins.add(new MonedaActor(world, new Vector2(i + 8.5f, 7.5f), coins, game.getSounds(), coinTextures));
                coins.add(new MonedaActor(world, new Vector2(i + 9.5f, 8.7f), coins, game.getSounds(), coinTextures));
                coins.add(new MonedaActor(world, new Vector2(i + 10.5f, 9.2f), coins, game.getSounds(), coinTextures));
                coins.add(new MonedaActor(world, new Vector2(i + 11.5f, 8.7f), coins, game.getSounds(), coinTextures));
                coins.add(new MonedaActor(world, new Vector2(i + 12.5f, 7.5f), coins, game.getSounds(), coinTextures));

            }
        }

        for (MonedaActor coin : coins)
            coin.getBody().setGravityScale(0);


        for (int i = 7; i < 224; i = i + 18) {
            setas.add(new SetaActor(world, setaTexture, setaTexture2, new Vector2(i, 1.5f), mario, setas, game.getSounds()));

        }



        /** Nubes */
        for (int i = 0; i < 225; i = i + 13) {
            int random = (int) (Math.random() * 3 + 1);
            nubesImg.add(new NubeImagen(nube2Texture, i, 5 + random, 3, 1.5f));
            if (random == 1) {
                random = (int) (Math.random() * 3 + 1);
                nubesImg.add(new NubeImagen(nube1Texture, i + 3 + random, 5 + random, 2f, 1.5f));
            }
        }

        /** Poblando el suelo */
        // En este bucle posicionamos los elementos cogiendo un multiplo para cada elemento
        // dependiendo de la posición del mapa.
        int cont = 4;
        while (cont < 225) {
            // Evitamos las caidas.
            if (cont == 46 || cont == 96 || cont == 146 || cont == 196) {
                cont += 5;
            } else {
                // Posicionamos las tuberías.
                if (cont == 12 || cont % 32 == 0) {
                    int random = (int) (Math.random() * 3 + 1);
                    tubierias.add(new TuberiaActor(world, tuberiaTexture, new Vector2(cont, 1f)));
                    if (random == 1) {
                        if (cont < 43 || cont > 50 && cont < 93 || cont > 100 && cont < 143 || cont > 150 && cont < 193) {
                            tubierias.add(new TuberiaActor(world, tuberiaTexture, new Vector2(cont + 2, 1f)));
                            cont += 2;
                        }
                    }
                    cont += 2;
                    // Posicionamos los arbustos y las montañas.
                } else if (cont == 4 || cont % 9 == 0) {
                    int random = (int) (Math.random() * 6 + 1);
                    if (random == 1) {
                        if (cont < 43 || cont > 50 && cont < 93 || cont > 100 && cont < 143 || cont > 150 && cont < 193) {
                            arbustos.add(new NubeImagen(montanaTexture, cont, 1, 4f, 2f));
                            cont += 4;
                        }
                    } else if (random == 2) {
                        if (cont < 43 || cont > 50 && cont < 92 || cont > 100 && cont < 142 || cont > 150 && cont < 192) {
                            arbustos.add(new NubeImagen(montanaTexture, cont + 1, 1, 4f, 2f));
                            arbustos.add(new NubeImagen(arbustoTexture, cont, 1, 1.5f, 1f));
                            cont += 5;
                        }
                    } else {
                        arbustos.add(new NubeImagen(arbustoTexture, cont, 1, 1.5f, 1f));
                        cont += 3;
                    }
                } else {
                    cont += 1;
                }
            }
        }

        nubeGrande = new NubeGrandeImagen(nube3Texture, -20, 10, 300, 3.5f);

        arbustos.add(new NubeImagen(senalTexture, 2, 1, 1.5f, 1));
        arbustos.add(new NubeImagen(banderaTexture, 225, 1, 1.3f, 7));


        monedaMar = new NubeImagen(monedaMarTexture, 0, 0, 0.5f, 0.5f);
        cabezaMar = new NubeImagen(cabezaMarioTexture, 0, 0, 0.5f, 0.4f);

        /** Se añaden arbustos, montañas y la señal */
        for (NubeImagen nube : arbustos)
            stage.addActor(nube);

        stage.addActor(castillo);
        stage.addActor(castilloFinal);
        stage.addActor(mario);
        stage.addActor(nubeGrande);

        for (SueloActor suelo : suelos)
            stage.addActor(suelo);
        for (SetaActor seta : setas)
            stage.addActor(seta);
        for (SueloImagen suelo : suelosImg)
            stage.addActor(suelo);
        for (CajaSorpresaActor cajaSorpresaActor : cajas)
            stage.addActor(cajaSorpresaActor);
        for (TuberiaActor tubo : tubierias)
            stage.addActor(tubo);
        for (NubeImagen nube : nubesImg)
            stage.addActor(nube);
        for (MonedaActor coin : coins)
            stage.addActor(coin);

        stage.addActor(monedaMar);
        stage.addActor(cabezaMar);
    }


    @Override
    public void hide() {

        //limpieza de bodys y Fixtures
        game.getSounds().stopMusicTheme();
        game.getSounds().stopMusicHurryUpTheme();
        mario.detach();
        muro.detach();

        for (SueloActor suelo : suelos)
            suelo.detach();
        for (SetaActor seta : setas)
            seta.detach();
        for (CajaSorpresaActor cajaSorpresaActor : cajas)
            cajaSorpresaActor.detach();
        for (MonedaActor coin : coins)
            coin.detach();
        for (TuberiaActor tubo : tubierias)
            tubo.detach();

        mario.remove();
        stage.clear();
        suelos.clear();
        setas.clear();
        cajas.clear();
        coins.clear();
        tubierias.clear();
        world.dispose();
    }

    @Override
    public void render(float delta) {
        //limpieza de la pantalla
        Gdx.gl.glClearColor(0.2f, 0.6f, 0.8f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //movimiento del mundo
        stage.act();
        world.step(delta, 6, 2);

        //Se ajusta el tiempo restante
        tiempoRestante = tiempoRestante - delta;

        //música de poco tiempo restante
        if ( tiempoRestante < Constantes.HURRY_UP_TIME && hurryUpTime){
            hurryUpTime = false;
            game.getSounds().stopMusicTheme();
            game.getSounds().playMusicHurryUpTheme();
        }

        //posicion X de la cámara
        if (stage.getCamera().position.x > mario.getX() + Constantes.CAMERA_ADJUST) {
            stage.getCamera().position.set(mario.getX() + Constantes.CAMERA_ADJUST, stage.getCamera().position.y, stage.getCamera().position.z);
        } else if (stage.getCamera().position.x < mario.getX() - Constantes.CAMERA_ADJUST) {
            stage.getCamera().position.set(mario.getX() - Constantes.CAMERA_ADJUST, stage.getCamera().position.y, stage.getCamera().position.z);
        }

        //posición Y de la cámara
        if (mario.getY() > minimumY + 30) {
            stage.getCamera().position.set(stage.getCamera().position.x, mario.getY() - 30, stage.getCamera().position.z);
        } else {
            stage.getCamera().position.set(stage.getCamera().position.x, minimumY, stage.getCamera().position.z);
        }

        //Posicion elementos gráficos de la interfaz
        monedaMar.setPosition(stage.getCamera().position.x - 44, (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 53);
        cabezaMar.setPosition(stage.getCamera().position.x - 44, (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 29);


        if (!victoria) {

            //Nueva moneda aparece de la caja
            if (nuevaMoneda) {
                this.nuevaMoneda = false;
                MonedaActor monedaActor = new MonedaActor(world, new Vector2(caja.getX() / Constantes.PIXELS_IN_METER + 0.4f, 4.3f), coins, game.getSounds(), coinTextures);
                game.getSounds().playCoinSound();
                coins.add(monedaActor);
                stage.addActor(monedaActor);

                // impulso hacia arriba de nueva moneda apareciendo
                Vector2 position = caja.getBody().getPosition();
                monedaActor.getBody().applyLinearImpulse(0, 5, position.x, position.y, true);

            }

            //control del tiempo de la animación de la muerte de mario
            if (mario.getMuriendo() > 3.5f || tiempoRestante < 0) {
                game.applyDead();
            }
            //Condición de victoria del juego
            if (mario.getX() > 225 * Constantes.PIXELS_IN_METER) {
                this.mario.setGanando(true);
                this.victoria = true;
            }
        } else {
            //animación de victoria
            this.winning = winning + delta;
            if (firstTimeWinning) {
                firstTimeWinning = false;
                //asignación puntuaciones
                game.setScore(puntuacion);
                game.setTiemppoRestante((int) tiempoRestante);
                game.getSounds().stopMusicTheme();
                game.getSounds().playWinSound();

            }
            mario.getBody().setLinearVelocity(0, -1.8f);
            if (winning > 3.5f && mario.getBody().getPosition().y > 1) {
                mario.getBody().setLinearVelocity(1.5f, 0);
            }
            if (mario.getBody().getPosition().x > 230.5f) {
                game.win(puntuacion, (int) tiempoRestante);
            }
        }

        stage.draw();

        //despues de dibujar la escena se añaden los marcadores sobre ella
        puntuacionString = String.format("SCORE\n%04d", puntuacion);
        monedasString = String.format("x%02d", monedas);
        tiempoString = String.format("TIME\n%03d", (int) tiempoRestante);

        stage.getBatch().begin();
        marcador.draw(stage.getBatch(), puntuacionString, (stage.getCamera().position.x - stage.getCamera().viewportWidth / 2) + 10,
                (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 10);
        vida.draw(stage.getBatch(), vidasString, stage.getCamera().position.x - 20,
                (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 10);
        moneda.draw(stage.getBatch(), monedasString, stage.getCamera().position.x - 20,
                (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 30);
        tiempo.draw(stage.getBatch(), tiempoString, (stage.getCamera().position.x + stage.getCamera().viewportWidth / 2) - 75,
                (stage.getCamera().position.y + stage.getCamera().viewportHeight / 2) - 10);
        stage.getBatch().end();
    }

    @Override
    public void dispose() {
        stage.getBatch().dispose();
        stage.dispose();
        world.dispose();
        for (Texture texture : marioTextures)
            texture.dispose();
        for (Texture texture : coinTextures)
            texture.dispose();
        setaTexture.dispose();
        sueloTexture.dispose();
        muroTexture.dispose();
        nube1Texture.dispose();
        nube2Texture.dispose();
        nube3Texture.dispose();
        cajaTexture.dispose();
        tuberiaTexture.dispose();
        ladrilloTexture.dispose();
        senalTexture.dispose();
        castilloTexture.dispose();
        arbustoTexture.dispose();
        montanaTexture.dispose();
        castilloFinTexture.dispose();
        banderaTexture.dispose();
        cabezaMarioTexture.dispose();
        monedaMarTexture.dispose();

    }
}
