package projectiles;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;

import entities.Obstacle;
import entities.Wall;
import tools.Constants;
import tools.Clock;

public class Launcher {
    private PVector pos;
    private float range, speed, damage, bloomRate;
    private int bulletsInCurrentSpray;
    private int bulletsBeforeBloom;
    private int maxBloom;
    private ArrayList<Projectile> projectiles;
    Iterator<Projectile> it;
    private Ray ray;
    private Clock clock = Clock.getInstance();
    private double lastShotTime;

    public Launcher(PVector _pos, float _speed) {
        projectiles = new ArrayList<Projectile>();
        pos = _pos.copy();
        range = 0;
        speed = _speed; //projectile speed
        damage = 0;
        ray = new Ray();
        lastShotTime = 0;
        bulletsBeforeBloom = 2;
        bloomRate = 4;
        maxBloom = 32;
    }

    public void update(PApplet p) {
      clock.run();
      if (clock.getTime() - lastShotTime > 0.5) {
        bulletsInCurrentSpray = 0;
      }
      ray.setOrigin(pos);
        it = projectiles.iterator();
        while (it.hasNext()) {
            Projectile temp = it.next();
            temp.run(p);
            if (temp.isDead()) {
                it.remove();
            }
        }
    }

    public void fire(PVector target, Obstacle obs) {
        PVector error = new PVector();
        if (bulletsInCurrentSpray > bulletsBeforeBloom) {
          float bloom = PApplet.min(bulletsInCurrentSpray * bloomRate, maxBloom);
          error = PVector.random2D().normalize().mult(bloom);
        }
        PVector direction = PVector.sub(target, pos).sub(error);
        ray.setDirection(direction);
        range = calculateProjectileDistanceFromObstacle(obs);
        direction.normalize();
        direction.mult(speed);
        Projectile p = new Projectile(pos, range, speed, damage);
        p.setVel(direction);
        projectiles.add(p);
        bulletsInCurrentSpray += 1;
        lastShotTime = clock.getTime();
    }
    
    // Saving for later
    // private float calculateProjectileDistance(List<Obstacle> obstacles) {
    //   float newRange = Constants.MAX_RANGE;
    //   Iterator<Obstacle> obstacleIterator = obstacles.iterator();
    //   while (obstacleIterator.hasNext()) {
    //     newRange = PApplet.min(
    //       calculateProjectileDistanceFromObstacle(obstacleIterator.next()),
    //       newRange
    //     );
    //   }
    //   return newRange;
    // }

    private float calculateProjectileDistanceFromObstacle(Obstacle obstacle) {
      float newRange = Constants.MAX_RANGE;
      Iterator<Wall> wallIterator = obstacle.getIterator();
      while (wallIterator.hasNext()) {    
        PVector point = ray.cast(wallIterator.next());
        if (point != null) {
          newRange = PApplet.min(PVector.dist(point, pos), newRange);
        }
      }
      return newRange;
    }

    // private void displayAttackRange(PApplet pa){
    //     ray.display(pa);
    // }

    public ArrayList<Projectile> getProjectiles(){
        return projectiles;
    }

    public void setPos(PVector _p) {
        pos.set(_p);
    }

    public void setRange(float _r) {
        range = _r;
    }

    //change the speed of the projectile
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
