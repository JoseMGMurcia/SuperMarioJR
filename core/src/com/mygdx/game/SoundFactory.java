package com.mygdx.game;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundFactory {


    private Music mainMusic, hurryUoMusic ,startSound, victorySound , gameOverSound;
    private Sound junmpSound, coinSound, deadSound, kicksound, winSound, getCoinSound ;

    public SoundFactory(MainGame game) {
        //se cargan los sonidos
        mainMusic = game.getAssetManager().get("sounds/main.ogg");
        junmpSound = game.getAssetManager().get("sounds/salto.wav");
        gameOverSound = game.getAssetManager().get("sounds/gameOver.mp3");
        coinSound = game.getAssetManager().get("sounds/coin.wav");
        deadSound = game.getAssetManager().get("sounds/dead.wav");
        winSound = game.getAssetManager().get("sounds/win.wav");
        getCoinSound = game.getAssetManager().get("sounds/getCoin.wav");
        kicksound = game.getAssetManager().get("sounds/kick.wav");
        startSound = game.getAssetManager().get("sounds/start.mp3");
        victorySound = game.getAssetManager().get("sounds/victory.mp3");
        hurryUoMusic = game.getAssetManager().get("sounds/hurryUp.mp3");
    }


    // musica de fondo
    public void playMusicTheme(){
        mainMusic.setVolume(0.2f);
        mainMusic.setLooping(true);
        mainMusic.play();
    }

    //musica de fondo para poco tiempo restante
    public void playMusicHurryUpTheme(){
        hurryUoMusic.setVolume(0.4f);
        hurryUoMusic.setLooping(true);
        hurryUoMusic.play();
    }
    public void stopMusicTheme()       { mainMusic.stop();    }
    public void stopMusicHurryUpTheme(){ hurryUoMusic.stop(); }
    public void playCoinSound()        { coinSound.play();    }
    public void playJumpSound()        { junmpSound.play();   }
    public void playDeadSound()        { deadSound.play();    }
    public void stopDeadSound()        { deadSound.stop();    }
    public void playGameOverSound()    { gameOverSound.play();}
    public void stopGameOverSound()    { gameOverSound.stop();}
    public void playWinSound()         { winSound.play();     }
    public void playGetCoinSound()     { getCoinSound.play(); }
    public void playKickSound()        { kicksound.play();    }
    public void playStartound()        { startSound.play();   }
    public void stopStartound()        { startSound.stop();   }
    public void playVictorySound()     { victorySound.play(); }
    public void stopVictorySound()     { victorySound.stop(); }

}
