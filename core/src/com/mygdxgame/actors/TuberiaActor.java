package com.mygdxgame.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.game.Constantes;

public class TuberiaActor extends Actor {



    private Texture tuberiaTexture;
    private World world;
    private Body body;
    private Fixture fixture;
    private int random;



    public TuberiaActor(World world, Texture tuberiaTexture, Vector2 position) {
        this.world = world;
        this.tuberiaTexture = tuberiaTexture;


        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        random = (int) (Math.random() * 3 + 1);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, random);
        fixture = body.createFixture(shape, 1);
        fixture.setUserData("tuberia");
        shape.dispose();

        setSize(Constantes.PIXELS_IN_METER*2, Constantes.PIXELS_IN_METER*random);
        setPosition((position.x-1 ) * Constantes.PIXELS_IN_METER,
                (position.y) * Constantes.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(tuberiaTexture, getX(), getY(), getWidth(), getHeight());

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

}
