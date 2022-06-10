package entities;

import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;

import java.util.ArrayList;

public class Enemy extends Entity{
    public double attack_speed;
    public double attack_delay;
    public double time_of_attack;
    public double attack_backswing;

    public Enemy(PApplet pa) {
        super(pa.random(100, 500), pa.random(100, 500), 70, 70);
        health = 255;
        r = 35;
        maxspeed = 3;
        //  attack_speed = 1.0;
        attack_delay = 0.5;
        attack_backswing = 0.25;
        time_of_attack = 0.0;
    }

    public void attack(Player player, Clock c) {

        if (canAttack(c) && this.collides(player)) {
            player.health -= 25;
            time_of_attack = c.getTime();
            this.vel.mult(0);
        }
    }

    private boolean canAttack(Clock c){
        if (time_of_attack + attack_delay + attack_backswing < c.getTime()){
            return true;
        }
        return false;
    }

    private boolean isAttacking(Clock c){
        if (time_of_attack + attack_backswing > c.getTime()){
            return true;
        }
        return false;
    }

    public void run(Player p, ArrayList<Enemy> enemies, Clock clock, PApplet pa) {
        PVector hunt = hunt(p.getPos());
        PVector sep = separate(enemies);
        hunt.mult((float) 0.5);
        attack(p, clock);
        applyForce(hunt);
        applyForce(sep);
        if (!isAttacking(clock)) {
            update(pa);
        }
        display(pa);
    }

    @Override
    public void update(PApplet pa) {
        updateHitBox();
        vel.add(acc);
        vel.limit(maxspeed);
        addToPos(vel);
        acc.mult(0);
    }

    PVector hunt(PVector player_pos) {
        PVector dir = PVector.sub(player_pos, this.getPos());
        dir.normalize();
        dir.mult(1);
        return dir;
    }

    public PVector separate(ArrayList<Enemy> enemies) {
        float desired_separation = 70;
        PVector sum = new PVector();
        float count = 0;
        PVector steer = new PVector();
        for (Enemy other : enemies) {
            float d = PVector.dist(getPos(), other.getPos());
            if (d > 0 && d < desired_separation) {
                PVector diff = PVector.sub(getPos(), other.getPos());
                diff.normalize();
                diff.div(d);
                sum.add(diff);
                count++;
            }
        }
        if (count > 0) {
            sum.div(count);
            sum.setMag(maxspeed);
            steer = PVector.sub(sum, vel);
            //steer.limit(maxforce);
        }

        return steer;
    }

    @Override
    void display(PApplet pa) {
//        pa.rectMode(PApplet.CENTER);
        pa.fill(255 - health, health, 0);
        pa.rect(getPos().x, getPos().y, 2*r, 2*r);
//        displayHitBox(pa);
    }
}
