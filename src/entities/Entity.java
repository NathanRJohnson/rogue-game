package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Projectile;

abstract class Entity extends GameObject {
    PVector vel, acc;
    float maxspeed, r;
    float health;

    Entity(float x, float y, float _w, float _h){
        super( new PVector(x, y), _w, _h );
        vel = new PVector();
        acc = new PVector();
        maxspeed = 6;
    }

    abstract void display(PApplet p);
    abstract void update(PApplet p);

    public boolean isDead(){
        if (health < 0.0){
            return true;
        }
        return false;
    }

    void applyForce(PVector force) {
        acc.add(force);
    }

    public void hit(Projectile p) {
        health -= p.getDamage();
        p.setDead();
    }

    public PVector GetVel() {
        return vel.copy();
    }

    public void setXVel(float _x) {
        vel.x = _x;
    }

    public void setYVel(float _y) {
        vel.y = _y;
    }
}
