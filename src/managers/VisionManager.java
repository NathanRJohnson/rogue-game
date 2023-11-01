package managers;

import java.util.Iterator;

import entities.Entity;
import entities.Obstacle;
import entities.Wall;
import processing.core.PApplet;
import processing.core.PVector;
import projectiles.Ray;

public class VisionManager {
  VisionManager instance;
  Obstacle obstacle;
  Ray ray;

  public VisionManager(Obstacle obstacle) {
    this.obstacle = obstacle;
    ray = new Ray();
  }

  public void run(PApplet pa) {
    ray.display(pa);
  }

  public boolean lineOfSightExistsBetween(Entity a, Entity b) {
    PVector direction = PVector.sub(b.getPos(), a.getPos());
    ray.setOrigin(a.getPos());
    ray.setDirection(direction);

    Iterator<Wall> wallIterator = obstacle.getIterator();
    Wall w;
    while (wallIterator.hasNext()) {
      w = wallIterator.next();
      PVector collision = ray.cast(w);
      if (collision != null) {
        if (PVector.dist(collision, b.getPos()) > PVector.dist(collision, a.getPos())){
          return false;
        }
      }
    }
      return true;
  }
}
     

