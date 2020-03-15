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

public class SetaActor extends Actor {

    private Texture setaTexture, setaTexture2, setaEnd;
    private World world;
    private Body body;
    private MarioActor mario;
    private Fixture fixture;
    private boolean vivo, toLeft, firstTimeDead;
    float muriendo, change;
    private List<SetaActor> list;
    private SoundFactory sounds;


    public SetaActor(World world, Texture setaTexture, Texture setaTexture2, Vector2 position, MarioActor mario, List<SetaActor> list, SoundFactory sounds) {
        this.world = world;
        this.setaTexture = setaTexture;
        this.setaTexture2 = setaTexture2;
        this.setaEnd = setaTexture;
        this.sounds = sounds;

        this.list = list;
        this.vivo = true;
        this.toLeft = false;
        this.firstTimeDead = true;
        this.mario = mario;
        this.muriendo = 0;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);
        fixture = body.createFixture(shape, 8);
        fixture.setUserData("seta");
        shape.dispose();

        setSize(Constantes.PIXELS_IN_METER, Constantes.PIXELS_IN_METER);
    }

    @Override
    public void act(float delta) {
        if (this.vivo && mario.isVivo() ) {
            //solo se mueve si esta cerca del personaje
            if ((mario.getBody().getPosition().x - this.body.getPosition().x) < Constantes.ACT_DISTANCE && (mario.getBody().getPosition().x - this.body.getPosition().x) > -Constantes.ACT_DISTANCE) {
                if (this.body.getLinearVelocity().isZero()) {
                    this.toLeft = !this.toLeft;
                }
                //movimiento hacia una u otra dirección, componente Y -3 para que caiga mas rápido
                if (toLeft) {
                    this.body.setLinearVelocity(-2, -3f);
                } else {
                    this.body.setLinearVelocity(2, -3f);
                }
            } else {
                this.body.setLinearVelocity(0, 0);
            }
        } else
            this.morir(delta);

        // cambia de imagen cada 350ms para animar a la seta
        change = change + delta;
        if (change > 0.350f) {
            change = 0;
            if (setaEnd.equals(setaTexture)) {
                setaEnd = setaTexture2;
            } else {
                setaEnd = setaTexture;
            }
        }

        // muere si ha caido
        if (this.body.getPosition().y < -1) {

            //este booleano controla que no se produzca sonido de eta muriendo en caso de caida
            this.firstTimeDead = false;
            this.morir(delta);
        }
    }

    private void morir(float delta) {
        this.muriendo = muriendo + delta;

        // la primera vez reproduce el sonido
        if (this.firstTimeDead && mario.isVivo()){
            this.firstTimeDead = false;
            sounds.playKickSound();
        }
        //animació de la muerte
        if (muriendo > 0.5f) {
            this.setVisible(false);
            if (muriendo > 0.75f) {
                this.setVisible(true);
                if (muriendo > 1) {
                    // se borra la sera
                    this.list.remove(this);
                    this.world.destroyBody(body);
                    this.remove();
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * Constantes.PIXELS_IN_METER,
                (body.getPosition().y - 0.5f) * Constantes.PIXELS_IN_METER);
        batch.draw(setaEnd, getX(), getY(), getWidth(), getHeight());

    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

}
