package entities;

import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;

import java.util.ArrayList;

public class Enemy extends Entity{
    public double attack_speed;
    public double attack_delay;
    public double time_of_attack;

    public Enemy(PApplet pa) {
        super(pa.random(100, 500), pa.random(100, 500), 70, 70);
        health = 255;
        r = 35;
        maxspeed = 3;
        //  attack_speed = 1.0;
        attack_delay = 1.0;
        time_of_attack = 0.0;
    }

    public void attack(Player player, Clock c) {
        PVector dist = PVector.sub(player.getPos(), this.getPos());
        if (canAttack(c) && PApplet.abs(dist.mag()) < 30.0) {
            player.health -= 25;
            time_of_attack = c.getTime();
        }
    }

    private boolean canAttack(Clock c){
        if (time_of_attack + attack_delay < c.getTime()){
            return true;
        }
        return false;
    }

    public void run(Player p, ArrayList<Enemy> enemies, Clock clock, PApplet pa) {
        PVector hunt = hunt(p.getPos());
        PVector sep = seperate(enemies);
        hunt.mult((float) 0.5);
        attack(p, clock);
        applyForce(hunt);
        applyForce(sep);
        update(pa);
        display(pa);
    }

    @Override
    void update(PApplet pa) {
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

    public PVector seperate(ArrayList<Enemy> enemies) {
        float desired_seperation = 70;
        PVector sum = new PVector();
        float count = 0;
        PVector steer = new PVector();
        for (Enemy other : enemies) {
            float d = PVector.dist(getPos(), other.getPos());
            if (d > 0 && d < desired_seperation) {
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
    }
}
