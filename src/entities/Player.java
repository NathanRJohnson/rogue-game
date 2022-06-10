package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Launcher;
import tools.Constants;
import tools.Direction;

public class Player extends Entity{
    private boolean[] inputs;
    private Launcher launcher;
    private PVector facing;

    public Player() {
        super(500,500, 40, 40);
        facing = new PVector();

        inputs = new boolean[4];
        launcher = new Launcher(getPos(), 400, 8);
        r = 15;
        launcher.setDamage(150);
        health = 255;
    }

    public void run(PApplet p){
        update(p);
        display(p);
    }

    @Override
    public void update(PApplet p) {
        launcher.update(p);
        move();
        facing = PVector.sub(new PVector(p.mouseX, p.mouseY), getPos());

        //removes creep
        if (PApplet.abs(vel.mag())<= 0.1) {
            vel.mult(0);
        }

        addToPos(vel);
        launcher.setPos(getPos());
        updateHitBox();

        //drag effect
        PVector inv = vel.copy();
        inv.normalize();
        inv.mult(-1);
        inv.mult((float) 0.3);
        acc.add(inv);

        vel.add(acc);
        vel.limit(maxspeed);

        acc.mult(0);
    }

    @Override
    public void display(PApplet pa) {
        pa.fill(193,	37,	173);
        float theta = facing.heading() + PApplet.PI/2;
        pa.fill(255-health, health, 0);
        pa.stroke(0);
        pa.pushMatrix();
        pa.translate(getPos().x + r, getPos().y + r);
        pa.rotate(theta);
        pa.beginShape();
        pa.vertex(0, -r*2);
        pa.vertex(-r, r*2);
        pa.vertex(r, r*2);
        pa.endShape(PApplet.CLOSE);
        pa.popMatrix();
//        displayHitBox(pa);
    }

    public void setPosByCompass(Direction d) {
        switch (d) {
            case NORTH:
                setPos(Constants.WIDTH/2, Constants.HEIGHT - 100);
                break;
            case SOUTH:
                setPos(Constants.WIDTH/2, 100);
                break;
            case EAST:
                setPos(100, Constants.HEIGHT/2);
                break;
            case WEST:
                setPos(Constants.WIDTH - 100, Constants.HEIGHT/2);
        }
    }

    public void fire(PApplet p) {
        launcher.fire(p);
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public void press(char k) {
        if (k == 'w' || k == 'W') {
            inputs[0] = true;
        } else if (k == 's' || k == 'S') {
            inputs[1] = true;
        }

        if (k == 'a' || k == 'A') {
            inputs[2] = true;
        } else if (k == 'd' || k == 'D') {
            inputs[3] = true;
        }
    }

    public void move() {
        if (inputs[0]) {
            acc.y = -1;
        }
        if (inputs[1]) {
            acc.y = 1;
        }

        if (inputs[2]) {
            acc.x = -1;
        }
        if (inputs[3]) {
            acc.x = 1;
        }
    }

    public void release(char k) {
        if (k == 'w' || k == 'W') {
            inputs[0] = false;
        } else if (k == 's' || k == 'S') {
            inputs[1] = false;
        }

        if (k == 'a' || k == 'A') {
            inputs[2] = false;
        } else if (k == 'd' || k == 'D') {
            inputs[3] = false;
        }
    }
}
