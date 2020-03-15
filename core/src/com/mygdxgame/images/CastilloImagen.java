package com.mygdxgame.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Constantes;

// Esta clase construimos el marco de la imagen con sus caracterícticas correspondients.
public class CastilloImagen extends Image {

    public CastilloImagen(Texture castilloTexture, int x, int y, int width, int height){
        // Dentro del marco ajustamos la imagen para que se extienda en el.
        castilloTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        TextureRegion imgTextureRegion = new TextureRegion(castilloTexture);
        imgTextureRegion.setRegion(0,0,castilloTexture.getWidth(),castilloTexture.getHeight());

        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(imgTextureRegion);
        setDrawable(imgTextureRegionDrawable);
        setSize(Constantes.PIXELS_IN_METER*width ,Constantes.PIXELS_IN_METER*height );
        setPosition(x * Constantes.PIXELS_IN_METER, y * Constantes.PIXELS_IN_METER);
    }
}
