package com.mygdxgame.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Constantes;

// Esta clase construimos el marco de la imagen con sus caracterícticas correspondients.
public class NubeGrandeImagen extends Image {

    public NubeGrandeImagen(Texture nubeTexture, int x, int y, float width, float height){
        // Dentro del marco ajustamos la imagen y hacemos que se repita por el marco hasta completarlo.
        nubeTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion imgTextureRegion = new TextureRegion(nubeTexture);
        imgTextureRegion.setRegion(0,0,nubeTexture.getWidth()*(int)width/6,nubeTexture.getHeight());

        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(imgTextureRegion);
        setDrawable(imgTextureRegionDrawable);
        setSize(Constantes.PIXELS_IN_METER*width ,Constantes.PIXELS_IN_METER*height );
        setPosition(x * Constantes.PIXELS_IN_METER, y * Constantes.PIXELS_IN_METER);
    }
}
