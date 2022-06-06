package projectiles;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;

public class Launcher {
    private PVector pos;
    private float range, speed, damage;
    private ArrayList<Projectile> projectiles;
    Iterator<Projectile> it;


    public Launcher(PVector _pos, float _r, float _s) {
        projectiles = new ArrayList<Projectile>();
        pos = _pos.copy();
        range = _r;
        speed = _s; //projectile speed
        damage = 17;
    }

    public void update(PApplet p) {
        it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile temp = it.next();
            temp.run(p);
            if (temp.isDead()) {
                it.remove();
            }
        }
    }

    public void fire(PApplet pa) {
        PVector mouse = new PVector(pa.mouseX, pa.mouseY);
        //PVector mouse = new PVector(width/2, 0);
        PVector direction = PVector.sub(mouse, pos);
        direction.normalize();
        direction.mult(speed);
        Projectile p = new Projectile(pos, range, speed, damage);
        p.setVel(direction);
        projectiles.add(p);
    }

    public ArrayList<Projectile> getProjectiles(){
        return projectiles;
    }

    public void setPos(PVector _p) {
        pos.set(_p);
    }

    public void setRange(float _r) {
        range = _r;
    }

    public void setSpeed(float _s) {
        speed = _s;
    }

    public void setDamage(float _d) {
        damage = _d;
    }

    public void increaseDamage(float _d) {
        damage += _d;
    }

    public float getDamage() {
        return damage;
    }
}
