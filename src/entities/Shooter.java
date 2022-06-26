package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Launcher;
import projectiles.Projectile;
import tools.Clock;

import java.util.ArrayList;

public class Shooter extends Enemy{
    Launcher launcher;
    public Shooter(PApplet pa){
        super(pa);
        maxspeed = 0;
        attack_range = 250;
        launcher = new Launcher(getCenteredPos(), attack_range, 5);
        damage = 50;

        launcher.setDamage((float) damage);
    }

    public void run(Player player, ArrayList<Enemy> enemies, Clock clock, PApplet pa) {
        PVector target = target(player.getPos());
        if (inRange(target) && canAttack(clock)){
            attack(player, clock);
        }

        for (Projectile p : launcher.getProjectiles()){
            if (p.collides(player)){
                player.health -= damage;
                time_of_attack = clock.getTime();
            }
        }

        display(pa);
    }

    @Override
    protected PVector target(PVector player_pos) {
        return PVector.sub(player_pos, this.getPos());
    }

    @Override
    public void attack(Player player, Clock c) {
        launcher.fire(player.getPos());
        time_of_attack = c.getTime();
    }

    void display(PApplet pa) {
//        pa.rectMode(PApplet.CENTER);
        pa.fill(0, health, 255 - health);
        pa.rect(getPos().x, getPos().y, 2*r, 2*r);
        for (Projectile p : launcher.getProjectiles()){
            p.run(pa);
        }
//        displayHitBox(pa);
        launcher.displayAttackRange(pa);
    }

}
