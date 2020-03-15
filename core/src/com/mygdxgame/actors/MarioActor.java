package com.mygdxgame.actors;

import com.badlogic.gdx.Gdx;
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

public class MarioActor extends Actor {

    private Texture marioTexture;
    private World world;
    private Body body;
    private Fixture fixture;
    private boolean vivo, saltando, hayQueSaltar , ganando;
    private float muriendo, direccion, changing;

    private List<Texture> texturas;
    private SoundFactory sounds;

    /**
     * CONSTRUCTOR
     */
    public MarioActor(World world, List<Texture> texturas, Vector2 position, SoundFactory sounds) {

        this.world = world;
        this.texturas = texturas;
        this.vivo = true;
        this.ganando = false;
        this.saltando = false;
        this.hayQueSaltar = false;
        this.muriendo = 0;
        this.sounds = sounds;

        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(def);


        PolygonShape shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.6f);
        fixture = body.createFixture(shape, 3);
        fixture.setUserData("mario");
        shape.dispose();

        setSize(Constantes.PIXELS_IN_METER, 1.2f*Constantes.PIXELS_IN_METER);
    }

    @Override
    public void act(float delta) {
        if (vivo && !ganando) {
            if (saltando) {
                float vel = this.body.getLinearVelocity().x;
                /**fuerza hacia abajo aplicada en el salto para mayor realismo */
                this.body.applyForceToCenter(0, -11, true);
                this.getBody().setLinearVelocity(vel,this.body.getLinearVelocity().y);
            } else {

                /**Obtenemos giro del movil */
                float acc = (float) (Gdx.input.getAccelerometerY()) * 1.6f;

                /**limitación de la velocidad */
                if (acc > Constantes.MAX_VELOCITY) acc = Constantes.MAX_VELOCITY;
                if (acc < -Constantes.MAX_VELOCITY) acc = -Constantes.MAX_VELOCITY;

                /**se aplica el resultado a mario */
                body.setLinearVelocity(acc, body.getLinearVelocity().y);
            }
            if (Gdx.input.justTouched()  || hayQueSaltar) {
                hayQueSaltar =false;
                this.jump();
            }

            /** Comprobación de caida por un agujero del suelo   */
            if (this.body.getPosition().y < 0) {
                this.vivo = false;
            }
        } else if (!vivo) {
            /** Si no esta vivo está muriendo y actua en consecuencia */
            this.morir(delta);
        }
        /** Elección de la textura */
        direccion = this.body.getLinearVelocity().x;
        changing = changing +delta;
        if (vivo) {
            if (saltando) {
                if (direccion < 0) {
                    marioTexture = texturas.get(5);
                } else {
                    marioTexture = texturas.get(4);
                }
                //si la velocidad es pequeña no cambia de imagen
            } else if (direccion < 0.5f) {
                if (changing < 0.250f) {
                    marioTexture = texturas.get(1);
                } else if (changing < 0.500f) {

                    marioTexture = texturas.get(3);
                } else {
                    changing = 0;
                }
            } else if (direccion > 0.5f) {
                if (changing < 0.250f) {
                    marioTexture = texturas.get(0);
                } else if (changing < 0.500f) {

                    marioTexture = texturas.get(2);
                } else {
                    changing = 0;
                }
            } else {
                // no deja al personaje moverse muy lento
                this.body.setLinearVelocity(0, 0);
            }
        }else {
            marioTexture = texturas.get(6);
        }
    }

    public void jump() {
        if (!saltando && vivo) {
            this.saltando = true;
            Vector2 velocity = this.body.getLinearVelocity();

            /** Solo se le aplica la fuerza del salto si no tenia ya impulso de subida */
            if (velocity.y < 0.5f) {
                Vector2 position = body.getPosition();
                this.body.applyLinearImpulse(0, 36, position.x, position.y, true);
                sounds.playJumpSound();
            }else if (this.body.getLinearVelocity().y == 0){
                // si no se ha aplicado fuerza de ascenso permite al personae volver a saltar
                this.setSaltando(false);
            }
        }
    }


    private void morir(float delta) {
        // la primera vez suena la música
        if (muriendo == 0f){
            sounds.stopMusicTheme();
            sounds.playDeadSound();
        }
        this.muriendo = muriendo + delta;
        //parpadeo de la muerte
        if (muriendo > 0.5f) {
            this.setVisible(false);
            if (muriendo > 0.75f) {
                this.setVisible(true);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition((body.getPosition().x - 0.5f) * Constantes.PIXELS_IN_METER,
                (body.getPosition().y - 0.6f) * Constantes.PIXELS_IN_METER);
            batch.draw(marioTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void detach() {
        body.destroyFixture(fixture);
        world.destroyBody(body);
    }

    /**
     * GETTERS & SETTERS
     */
    public boolean isVivo() {
        return vivo;
    }

    public void setVivo(boolean vivo) {
        this.vivo = vivo;
    }

    public boolean isSaltando() {
        return saltando;
    }

    public void setSaltando(boolean saltando) {
        this.saltando = saltando;
    }

    public boolean isHayQueSaltar() {
        return hayQueSaltar;
    }

    public boolean isGanando() {
        return ganando;
    }

    public void setGanando(boolean ganando) {
        this.ganando = ganando;
    }

    public void setHayQueSaltar(boolean hayQueSaltar) {
        this.hayQueSaltar = hayQueSaltar;
    }

    public Body getBody() {
        return body;
    }

    public float getMuriendo() {
        return muriendo;
    }
}
