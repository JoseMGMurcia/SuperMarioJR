package com.mygdxgame.images;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.Constantes;

// Esta clase construimos el marco de la imagen con sus caracterícticas correspondients.
public class NubeImagen extends Image {

    // Esta calse puede construirse de forma variable para adaptarse al tipo de imagenes o Texturas.
    public NubeImagen(Texture nubeTexture, int x, int y,float width,float height){
        nubeTexture.setWrap(Texture.TextureWrap.ClampToEdge, Texture.TextureWrap.ClampToEdge);
        TextureRegion imgTextureRegion = new TextureRegion(nubeTexture);
        imgTextureRegion.setRegion(0,0,nubeTexture.getWidth(),nubeTexture.getHeight());

        TextureRegionDrawable imgTextureRegionDrawable = new TextureRegionDrawable(imgTextureRegion);
        setDrawable(imgTextureRegionDrawable);
        setSize(Constantes.PIXELS_IN_METER*width ,Constantes.PIXELS_IN_METER*height );
        setPosition(x * Constantes.PIXELS_IN_METER, y * Constantes.PIXELS_IN_METER);
    }
}
