package com.mygdxgame.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Constantes;

// Esta clase construimos el marco de la imagen con sus caracterícticas correspondients.
public class SueloImagen extends Image {

    public SueloImagen(Texture sueloTexture, int x, int width, int y){
        // Dentro del marco ajustamos la imagen y hacemos que se repita por el marco hasta completarlo,
        // el tamaño de alto es siempre fijo.
        sueloTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
        TextureRegion imgTextureRegion = new TextureRegion(sueloTexture);
        imgTextureRegion.setRegion(0,0,sueloTexture.getWidth()*width,sueloTexture.getHeight());

        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(imgTextureRegion);
        setDrawable(imgTextureRegionDrawable);
        setSize(Constantes.PIXELS_IN_METER *width,Constantes.PIXELS_IN_METER );
        setPosition(x * Constantes.PIXELS_IN_METER, (y-1)* Constantes.PIXELS_IN_METER);
    }
}
