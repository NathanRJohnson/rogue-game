package projectiles;

import entities.Entity;
import entities.Wall;
import processing.core.PApplet;
import processing.core.PVector;

public class Ray {
  PVector origin;
  PVector direction;
  

  public Ray() {
    this.origin = new PVector();
    direction = new PVector();
  }

  public void setOrigin(PVector origin) {
    this.origin = origin;
  }

  public void setDirection(PVector direction) {
    this.direction = direction.normalize();
  }

  public boolean collidesWith(Wall wall) {
    float x4 = origin.x + direction.x;
    float y4 = origin.y + direction.y;
    float denominator = (wall.getStart().x - wall.getEnd().x)*(origin.y - y4) - (wall.getStart().y - wall.getEnd().y)*(origin.x - x4);
    if (denominator == 0) {
      return false;
    }

    float t = ((wall.getStart().x - origin.x) * (origin.y - y4) - (wall.getStart().y - origin.y) * ( origin.x - x4)) / denominator;
    float u = -((wall.getStart().x - wall.getEnd().x) * (wall.getStart().y - origin.y) - (wall.getStart().y - wall.getEnd().y) * (wall.getStart().x - origin.x)) / denominator;

    return t > 0 && t < 1 && u > 0;
  }

  /**
   * 
   * @param wall
   * @return PVector if there is an intersection, null if not.
   */
  public PVector cast(Wall wall) {
    float x4 = origin.x + direction.x;
    float y4 = origin.y + direction.y;
    float denominator = (wall.getStart().x - wall.getEnd().x)*(origin.y - y4) - (wall.getStart().y - wall.getEnd().y)*(origin.x - x4);
    if (denominator == 0) {
      return null;
    }

    float t = ((wall.getStart().x - origin.x) * (origin.y - y4) - (wall.getStart().y - origin.y) * ( origin.x - x4)) / denominator;
    float u = -((wall.getStart().x - wall.getEnd().x) * (wall.getStart().y - origin.y) - (wall.getStart().y - wall.getEnd().y) * (wall.getStart().x - origin.x)) / denominator;

    if (t > 0 && t < 1 && u > 0) {
      PVector intersection = new PVector();
      intersection.x = wall.getStart().x + t * (wall.getEnd().x - wall.getStart().x);
      intersection.y = wall.getStart().y + t * (wall.getEnd().y - wall.getStart().y);
      return intersection;
    }
    return null;
  }

  public boolean lineOfSightExistsBetween(Entity a, Entity b, Wall w) {
    return cast(w) == null;
  }

  public void display(PApplet pa) {
    pa.pushMatrix();
    pa.translate(origin.x, origin.y);
    pa.line(0, 0, direction.x * 100, direction.y * 100);
    pa.popMatrix();
  }
}
