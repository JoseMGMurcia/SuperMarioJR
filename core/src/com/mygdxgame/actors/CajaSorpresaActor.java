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

public class CajaSorpresaActor extends Actor {

    private Texture cajaTexture ,cajaVaciaTexture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean vacia;

    public CajaSorpresaActor(World world, Texture CajaTexture, Texture cajaVaciaTexture, Vector2 position) {
        this.world = world;
        this.cajaTexture = CajaTexture;
        this.vacia = false;
        this.cajaVaciaTexture = cajaVaciaTexture;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(shape, 5);
        fixture.setUserData("caja");
        shape.dispose();

        setSize(Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * Constantes.PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * Constantes.PIXELS_IN_METER);
        if (!this.vacia)batch.draw(cajaTexture, getX(), getY(), getWidth(), getHeight());
        else batch.draw(cajaVaciaTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isVacia() {
        return vacia;
    }

    public void setVacia(boolean vacia) {
        this.vacia = vacia;
    }

    public Body getBody() {
        return body;
    }

}

