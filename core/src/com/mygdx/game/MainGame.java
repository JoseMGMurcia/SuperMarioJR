package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

public class MainGame extends Game {

	private static final String PREFERENCES = "MarioPreferences";
	private static final String HIGH_SCORE_KEY = "TopScore";
	private Preferences preferences;
	private AssetManager assetManager;
	private BaseScreen victoryScreen, endScreen, startScreen;
	private GameScreen gameScreen;
	private SoundFactory sounds;
	private int vidasInt, score , tiemppoRestante;

	@Override
	public void create () {
		vidasInt = 3;

		//se cargan los recursos necesarios
		assetManager = new AssetManager();

		//texturas
		assetManager.load("images/caja.png", Texture.class);
		assetManager.load("images/cajaVacia.png", Texture.class);
		assetManager.load("images/seta.png", Texture.class);
		assetManager.load("images/seta2.png", Texture.class);

        assetManager.load("images/mario1Der.png", Texture.class);
        assetManager.load("images/mario1Izq.png", Texture.class);
        assetManager.load("images/mario2Der.png", Texture.class);
        assetManager.load("images/mario2Izq.png", Texture.class);
        assetManager.load("images/marioSaltDer.png", Texture.class);
        assetManager.load("images/marioSaltIzq.png", Texture.class);
        assetManager.load("images/mariom.png", Texture.class);
        assetManager.load("images/gameOver.png", Texture.class);

		assetManager.load("images/suelo.png", Texture.class);
		assetManager.load("images/nube1.png", Texture.class);
		assetManager.load("images/nube2.png", Texture.class);
		assetManager.load("images/nube3.png", Texture.class);

		assetManager.load("images/tuboRoto.png", Texture.class);
		assetManager.load("images/nubeOscura.png", Texture.class);

		assetManager.load("images/tubo.png", Texture.class);
		assetManager.load("images/ladrillo.png", Texture.class);
		assetManager.load("images/castillo.png", Texture.class);
		assetManager.load("images/castilloFinal.png", Texture.class);
		assetManager.load("images/senal.png", Texture.class);
		assetManager.load("images/arbusto.png", Texture.class);
		assetManager.load("images/montana.png", Texture.class);
		assetManager.load("images/bandera.png", Texture.class);

		assetManager.load("images/coin1.png", Texture.class);
		assetManager.load("images/coin2.png", Texture.class);
		assetManager.load("images/coin3.png", Texture.class);
		assetManager.load("images/coin4.png", Texture.class);
		assetManager.load("images/coin5.png", Texture.class);
		assetManager.load("images/coin6.png", Texture.class);

		assetManager.load("images/welcome.png", Texture.class);
		assetManager.load("images/pantallaWin.png", Texture.class);
		assetManager.load("images/pantallaEnd.png", Texture.class);

        assetManager.load("images/monedaMar.png", Texture.class);
        assetManager.load("images/marioCabeza.png", Texture.class);

		//SONIDOS
		assetManager.load("sounds/main.ogg", Music.class);
		assetManager.load("sounds/hurryUp.mp3", Music.class);
		assetManager.load("sounds/start.mp3", Music.class);
		assetManager.load("sounds/victory.mp3", Music.class);
		assetManager.load("sounds/gameOver.mp3", Music.class);

		assetManager.load("sounds/salto.wav", Sound.class);
		assetManager.load("sounds/dead.wav", Sound.class);
		assetManager.load("sounds/win.wav", Sound.class);
		assetManager.load("sounds/getCoin.wav", Sound.class);
		assetManager.load("sounds/coin.wav", Sound.class);
		assetManager.load("sounds/kick.wav", Sound.class);

		assetManager.finishLoading();

		// se inicializan las pantallas
		sounds = new SoundFactory(this);
		gameScreen = new GameScreen(this);
		victoryScreen = new VictoryScreen(this);
		endScreen = new EndScreen(this);
		startScreen = new StarScreen(this);
		this.setScreen( startScreen);
	}

	//paso a la pantalla de victoria
	public void win(int score , int tiempoRestante ){
		this.score = score;
		this.tiemppoRestante = tiempoRestante;
		victoryScreen = new VictoryScreen(this);
		this.setScreen(victoryScreen);
	}

	// Guardado de la puntuación mas alta
	public void savePreferences(int scoreToave){
		preferences = Gdx.app.getPreferences(PREFERENCES);
		preferences.putInteger(HIGH_SCORE_KEY, scoreToave);
		preferences.flush();

	}

	//obtiene la puntuación mas alta
	public int getPreferences(){
		preferences = Gdx.app.getPreferences(PREFERENCES);
		return  preferences.getInteger(HIGH_SCORE_KEY);
	}


	// aplica un reseteo total de la pantalla de juego
	public void applyDead(){
		vidasInt--;
		if (vidasInt > 0){
			gameScreen = new GameScreen(this);
			this.setScreen(gameScreen);
		}else {
			endScreen = new EndScreen(this);
			this.setScreen(endScreen);
		}
	}

	//reinicio del juego
	public void resetGame(){
		this.vidasInt = 3;
		this.gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	//vuelve a la pantalla de inicio
	public  void goHome(){
		this.setScreen(startScreen);
	}



	// GETTERS & SETTERS

	public SoundFactory getSounds() {return sounds;	}

	public int getVidasInt() {return vidasInt;}

	public int getScore() {return score;}

	public int getTiemppoRestante() {return tiemppoRestante;}

	public AssetManager getAssetManager() {return assetManager;}

	public void setScore(int score) {this.score = score;}

	public void setTiemppoRestante(int tiemppoRestante) {this.tiemppoRestante = tiemppoRestante;}
}
