package projectiles;

import processing.core.PApplet;
import processing.core.PVector;

public class Projectile {
    PVector pos, vel;
    float size;
    float lifespan, decay_rate, damage;
    boolean isDead;

    public Projectile(PVector _pos, float _range, float _speed, float _damage) {
        pos = _pos.copy();
        vel = new PVector();
        lifespan = _range;
        decay_rate = _speed;
        damage = _damage;
        isDead = false;
    }

    public boolean isDead() {
        return isDead || lifespan < 0.0;
    }

    public void run(PApplet p) {
        update();
        display(p);
    }

    private void update() {
        pos.add(vel);
    }

    private void display(PApplet p) {
        p.fill(255, 0, 0);
        p.ellipse(pos.x, pos.y, 15, 15);
        lifespan -= decay_rate;
    }

    public void setVel(PVector _v) {
        vel = _v;
    }

    public double getDamage(){
        return damage;
    }

    public void setDead(){
        isDead = true;
    }

    public PVector getPos() {
        return pos;
    }
}
