package entities;

import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Projectile;
import projectiles.Ray;
import tools.Clock;
import tools.CoolDown;

import java.util.List;

public class Zombie extends Entity {
  private CoolDown timeToAttackPoint;
  private CoolDown backSwing;
  private boolean isAttacking;
  private double attackDamage;
  private float attackRange;
  protected boolean canAttack;
  protected PVector facing;

  public Zombie() {
    this(0, 0);
  }
  
  public Zombie(float x, float y) {
    super(x, y, 35, 35);
    attackDamage = 25;
    attackRange = 45;
    canAttack = true;
    health = 255;
    isAttacking = false;
    maxspeed = 3;
    r = 30;
    facing = new PVector();
    backSwing = new CoolDown(1);
    timeToAttackPoint = new CoolDown(1.5);
  }

  public void run(PApplet pa, List<Zombie> zombies, Clock clock) {
    PVector sep = separate(zombies);
    applyForce(sep);
    if (isAttacking) {
      // attack(player);
    } else {
      // may rename this to move at some point
      update(pa);
    }
    display(pa);
  }

  public void hunt(PVector target){
    PVector hunt = target(target);
    hunt.mult((float) 0.5);
    applyForce(hunt);
  }

  public void initateAttack() {
    this.vel.mult(0);
    isAttacking = true;
    timeToAttackPoint.start();
  }

  // Don't know if I like passing the player in here right now
  // Kinda want to be able to reuse this for barriers later on too.
  public void attack(Player player) {
    if (timeToAttackPoint.hasElapsed()) {
      backSwing.start();
      timeToAttackPoint.reset();
      if (inRange(player.getPos())) {
        player.receiveDamage(this.attackDamage);
      }
    }

    if (backSwing.hasElapsed()) {
      canAttack = true;
      isAttacking = false;
      backSwing.reset();
    }
  }

  public boolean isAttacking(){
    return isAttacking;
  }

  public boolean inRange(PVector target){
    return PApplet.abs(PVector.dist(target, getPos())) < attackRange;
  }

  public boolean isHitHead(Projectile projectile) {
    return PApplet.abs(PVector.dist(projectile.getPos(), getPos())) <= r/2;
  }

  public boolean isHitBody(Projectile projectile) {
    return PApplet.abs(PVector.dist(projectile.getPos(), new PVector(getPos().x + r/2, getPos().y))) <= r/2
        || PApplet.abs(PVector.dist(projectile.getPos(), new PVector(getPos().x - r/2, getPos().y))) <= r/2;
  }

  @Override
  public void update(PApplet pa) {
    updateHitBox();
    vel.add(acc);
    vel.limit(maxspeed);
    addToPos(vel);
    acc.mult(0);
    facing = vel;

  }

  protected PVector target(PVector player_pos) {
    PVector dir = PVector.sub(player_pos, this.getPos());
    dir.normalize();
    return dir;
  }

  protected void seek(PVector target) {
    PVector dir = PVector.sub(target, this.getPos());
    dir.normalize();
    dir.mult(maxspeed);
    applyForce(dir);
  }

  public PVector separate(List<Zombie> zombies) {
    float desired_separation = 50;
    PVector sum = new PVector();
    float count = 0;
    PVector steer = new PVector();
    for (Zombie other : zombies) {
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

  public void follow(List<Path> paths) {
    PVector target = null;
    float shortestDistance = 100000000;
    for (Path path : paths) {
      PVector newTarget = getClosestPathTarget(path);
      if (newTarget == null) {
        continue;
      }
      float newDist = PVector.dist(newTarget, getPos());
      if (newDist < shortestDistance) {
        shortestDistance = newDist;
        target = newTarget;
      }
    }
    if (target != null) {
      seek(target);
    }
  }

  public void follow(Path path) {
    PVector target = getClosestPathTarget(path);
    if (target != null) {
      seek(target);
    }
  }

  private PVector getClosestPathTarget(Path path) {
    // Step 1: Predict the vehicle’s future location.
    PVector predict = vel.copy();
    predict.normalize();
    predict.mult(25);
    PVector predictPos = PVector.add(getPos(), predict);

    PVector target = null;
    float worldRecord = 100000;
    // Step 2: Find the normal point along the path.
    for (int i = 0; i < path.numPoints()-1; i++) {
      PVector a = path.getPoint(i);
      PVector b = path.getPoint(i+1);

      PVector normalPoint = getNormalPoint(predictPos, a, b);
      if (normalPoint.x < a.x || normalPoint.x > b.x) {
        normalPoint = b.copy();
      }

      // Step 4: If we are off the path,
      // seek that target in order to stay on the path.
      float distance = PVector.dist(normalPoint, predictPos);
      if (distance < worldRecord) {
        worldRecord = distance;

        PVector dir = PVector.sub(a, b);
        dir.normalize();
        dir.mult(10); // TODO: oversimplification. Should be based on distance to path and velocity;
        target = normalPoint.copy();
        target.add(dir);
      }
    }
    
    if (worldRecord > path.getRadius()) {
        return target;
    }
    return null;
  }

  private PVector getNormalPoint(PVector p, PVector a, PVector b) {
    PVector ap = PVector.sub(p, a);
    // PVector that points from a to b
    PVector ab = PVector.sub(b, a);

    // Using the dot product for scalar projection
    ab.normalize();
    ab.mult(ap.dot(ab));

    // Finding the normal point along the line segment
    PVector normalPoint = PVector.add(a, ab);

    return normalPoint;
  }

  @Override
  void display(PApplet pa) {
    float theta = facing.heading();
    pa.pushMatrix();
    pa.translate(getPos().x, getPos().y);
    pa.rotate(theta);
    pa.fill(120, 150, 120);
    pa.ellipse(0, r/2, r, r);
    pa.ellipse(0, -r/2, r, r);
    pa.fill(140, 150, 140);
    pa.ellipse(0, 0, r, r);
    
    pa.popMatrix();
    // displayHitBox(pa);
  }
}
