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
import com.mygdx.game.SoundFactory;

import java.util.List;

public class MonedaActor extends Actor {

    private Texture coinTexture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean cogida;
    private List<MonedaActor> list;
    private SoundFactory sounds;
    private List<Texture> texturas;
    private float changing;


    public MonedaActor(World world, Vector2 position, List<MonedaActor> list, SoundFactory sounds, List<Texture> texturas) {
        this.world = world;
        this.coinTexture = texturas.get(0);
        this.list = list;
        this.sounds = sounds;
        this.texturas = texturas;
        this.changing = 0;


        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.4f, 0.4f);
        fixture = body.createFixture(shape, 2);
        fixture.setUserData("coin");
        shape.dispose();

        setSize(Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
    }

    @Override
    public void act(float delta) {
        //las monedas no se mueven e intentan dejar de hacerlo si se las impulsa
        this.body.setLinearVelocity(0,0);

        //si el personaje la toca desaparece
        if (cogida) {
            this.desaparecer(delta);
        }

        //control de la animación en función de delta
        changing = changing + delta;
        if ( changing < 0.15f)      coinTexture = texturas.get(0);
        else if ( changing < 0.3f) coinTexture = texturas.get(1);
        else if ( changing < 0.45f) coinTexture = texturas.get(2);
        else if ( changing < 0.6f) coinTexture = texturas.get(3);
        else if ( changing < 0.75f) coinTexture = texturas.get(4);
        else if ( changing < 0.9f) coinTexture = texturas.get(5);
        else changing = 0;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.4f) * Constantes.PIXELS_IN_METER,
                (body.getPosition().y - 0.4f) * Constantes.PIXELS_IN_METER);
        batch.draw(coinTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    //gestión de la desaparición total
    private void desaparecer(float delta) {
        sounds.playGetCoinSound();
        this.list.remove(this);
        this.world.destroyBody(body);
        this.remove();
    }

    public boolean isCogida() {
        return cogida;
    }

    public void setCogida(boolean cogida) {
        this.cogida = cogida;
    }

    public Body getBody() {
        return body;
    }

}
