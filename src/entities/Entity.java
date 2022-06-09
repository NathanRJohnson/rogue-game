package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Projectile;

abstract class Entity {
    PVector pos, vel, acc;
    float maxspeed, r;
    float health;

    Entity(float x, float y){
        pos = new PVector(x, y);
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

    public boolean isHit(Projectile p) {
        PVector dist = PVector.sub(p.getPos(), pos);
        if (PApplet.abs(dist.mag()) < r) {
            health -= p.getDamage();
            p.setDead();
            return true;
        }
        return false;
    }

    public PVector GetPos() {
        return pos.copy();
    }

    public void setPosX(float _x){
        pos.x = _x;
    }
    public void setPosY(float _y){
        pos.y = _y;
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
