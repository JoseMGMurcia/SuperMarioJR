package com.mygdxgame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MuroActor extends Actor {


    private Texture muroTexture;
    private World world;
    private Body body;
    private Fixture fixture;

    //esta clase ejerce de barrera para limitar el mundo

    public MuroActor (World world, Texture muroTexture,  float x, float altura, float y){
        this.world = world;
        this.muroTexture = muroTexture;
        BodyDef def = new BodyDef();
        def.position.set(x-0.5f, (y+altura)/2 );
        def.type = BodyDef.BodyType.StaticBody;
        body= world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, altura / 2);
        fixture = body.createFixture(shape, 1);
        fixture.setUserData("muro");
        shape.dispose();


    }


    public void draw(Batch batch, float parentAlpha) {

       // Esta clase no tiene dibujo visible
    }

    public void detach(){
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
