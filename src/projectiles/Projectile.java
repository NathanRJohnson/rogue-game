package projectiles;

import entities.GameObject;
import processing.core.PApplet;
import processing.core.PVector;

public class Projectile extends GameObject {
    PVector vel;
    float size;
    float lifespan, decay_rate, damage;
    boolean isDead;

    public Projectile(PVector _pos, float _range, float _speed, float _damage){
      super(_pos.copy(),15, 15);
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
      addToPos(vel);
      lifespan -= decay_rate;
      updateHitBox();
    }

    private void display(PApplet pa) {
      pa.fill(150, 150, 0);
      pa.ellipse(getPos().x + 7, getPos().y + 7, 6, 6);
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

    public void reduceDamageBy(double amount) {
      this.damage -= amount;
    }

}
