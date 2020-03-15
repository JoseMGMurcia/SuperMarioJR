package com.mygdxgame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constantes;

public class SueloActor extends Actor {

    private Texture sueloTexture;
    private World world;
    private Body body;
    private Fixture fixture;

    public SueloActor (World world, Texture sueloTexture, float x, float width, float y){
        this.world = world;
        this.sueloTexture = sueloTexture;
        sueloTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat); // NO FUNCIONA


        BodyDef def = new BodyDef();
        def.position.set(x+width/2, y-0.5f);
        def.type = BodyDef.BodyType.StaticBody;
        body= world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width/2, 0.5f);
        fixture = body.createFixture(shape, 1);
        fixture.setUserData("suelo");
        shape.dispose();

        this.setSize(width* Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
        this.setPosition(x * Constantes.PIXELS_IN_METER, (y-1)* Constantes.PIXELS_IN_METER);
    }

    public void draw(Batch batch, float parentAlpha) {

        batch.draw(sueloTexture, getX(), getY(), getWidth(), getHeight());

    }






    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }
}
