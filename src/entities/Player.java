package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Launcher;
import tools.Constants;
import tools.Mouse;

public class Player extends Entity {
  private final int FORWARD = 0;
  private final int BACKWARD = 1;
  private final int LEFT = 2;
  private final int RIGHT = 3;
  private final int SPRINT = 4;
  private final int SLIDE = 5;
  private final int WALK = 6;
  private final int SPRINT_CHARGE_MAX = 70;
  private final int SLIDE_CHARGE_MAX = 20;
  private boolean[] inputs;
  private Launcher launcher;
  private PVector facing;
  private float sprintSpeed, walkSpeed, slideSpeed;
  private int movementState;
  private int sprintCharge, slideCharge;
  public Player() {
    super(0, 0, 40, 40);
    facing = new PVector();
    inputs = new boolean[6];
    launcher = new Launcher(getPos(), 16);
    launcher.setDamage(45); 
    r = Constants.PLAYER_RADIUS;
    health = 255;
    walkSpeed = 3;
    sprintSpeed = 6;
    slideSpeed = 10;
    maxspeed = walkSpeed;
  }

  public void run(PApplet p){
    update(p);
    display(p);
  }

  @Override
  public void update(PApplet pa) {
    facing = PVector.sub(Mouse.getInstance().getPosition().sub(Constants.ORIGIN), getPos());
    addToPos(vel);
    launcher.update(pa);
    launcher.setPos(getPos());
    updateHitBox();

    if (movementState == SLIDE){
      maxspeed = slideSpeed;
      slide();
    } else {
      move();
      slideCharge = PApplet.max(SLIDE_CHARGE_MAX, slideCharge + 1);
      if (movementState == SPRINT) {
        maxspeed = sprintSpeed;
        // spend sprint energy
        sprintCharge = PApplet.max(0, sprintCharge - 1);
        if (sprintCharge <= 0) { movementState = WALK; inputs[SPRINT] = false; };
      } else {
        maxspeed = walkSpeed;
        // recharge sprint speed
        sprintCharge = PApplet.min(SPRINT_CHARGE_MAX, sprintCharge + 1);
      }
    }

    //drag effect
    if (this.acc.mag() == 0){
      PVector inv = vel.copy();
      inv.normalize();
      inv.mult(-1);
      inv.mult((float) 0.6);
      acc.add(inv);
      
      // removes creep
      if (PApplet.abs(vel.mag()) <= 0.3) {
        vel.mult(0);
      }
    }

    vel.add(acc);
    vel.limit(maxspeed);
    acc.mult(0);
  }

  private void slide() {
    if (slideCharge <= 0) {
      movementState = SPRINT;
    }
    PVector direction = vel.copy();
    direction.normalize();
    direction.mult(slideSpeed);
    vel.add(direction);
    slideCharge = PApplet.max(0, slideCharge - 1);
  }

  @Override
  public void display(PApplet pa) {
    // pa.fill(193, 37, 173);
    float theta = facing.heading() + PApplet.PI / 2;
    pa.fill(0, 50, 200);
    pa.stroke(0);
    pa.pushMatrix();
    pa.translate(getPos().x, getPos().y);
    pa.rotate(theta);
    pa.ellipse(0, 0, 2 * r, 2 *r);
    pa.fill(200, 200, 50);
    pa.ellipse(0, -10, r, r/2);
    pa.popMatrix();
    pa.fill(0);
    pa.textSize(16);
    pa.text("Health", 55, 720);
    pa.fill(0, 200, 0);
    pa.rect(55, 730, PApplet.max(health, 0), 35);
    pa.stroke(1);
    pa.rect(55, 730, 255, 35);
  }

  public void fire(PVector target, Obstacle obs) {
    launcher.fire(target, obs);
  }

  public Launcher getLauncher() {
    return launcher;
  }

  public void move() {
    if (inputs[FORWARD]) {
      acc.y = -1;
    }
    if (inputs[BACKWARD]) {
      acc.y = 1;
    }

    if (inputs[LEFT]) {
      acc.x = -1;
    }
    if (inputs[RIGHT]) {
      acc.x = 1;
    }
    if (inputs[SPRINT]) {
      if (sprintCharge >= SPRINT_CHARGE_MAX - 20) {
        movementState = SPRINT;
      } else if (movementState != SPRINT) {
        inputs[SPRINT] = false;
      }
    } else {
      movementState = WALK;
    }

    if (inputs[SLIDE] && inputs[SPRINT]) {
      if (movementState == SPRINT && slideCharge >= 0) {
        movementState = SLIDE;
        slideCharge = 15;
      }
    }
  }

  public void press(char k, int keyCode) {
    if (k == 'w' || k == 'W') {
        inputs[FORWARD] = true;
    } else if (k == 's' || k == 'S') {
        inputs[BACKWARD] = true;
    }

    if (k == 'a' || k == 'A') {
        inputs[LEFT] = true;
    } else if (k == 'd' || k == 'D') {
        inputs[RIGHT] = true;
    }

    if (keyCode == PApplet.SHIFT) {
        inputs[SPRINT] = true;
    }

    if (keyCode == PApplet.CONTROL) {
      inputs[SLIDE] = true;
    }
  }

  public void release(char k, int keyCode) {
      if (k == 'w' || k == 'W') {
          inputs[FORWARD] = false;
          acc.y = 0;
      } else if (k == 's' || k == 'S') {
          inputs[BACKWARD] = false;
          acc.y = 0;
      }

      if (k == 'a' || k == 'A') {
          inputs[LEFT] = false;
          acc.x = 0;

      } else if (k == 'd' || k == 'D') {
          inputs[RIGHT] = false;
          acc.x = 0;
      }

      if (keyCode == PApplet.SHIFT) {
        inputs[SPRINT] = false;
      }

      if (keyCode == PApplet.CONTROL){
        inputs[SLIDE] = false;
      }
  }
}
