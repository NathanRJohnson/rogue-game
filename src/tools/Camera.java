package tools;

import entities.Entity;
import processing.core.PApplet;
import processing.core.PVector;

public class Camera {
  private PVector position;
  private PVector offset;
  
  public Camera() {
    position = new PVector();
    offset = new PVector(Constants.WIDTH/2, Constants.HEIGHT/2);
  }

  public void follow(PApplet pa, Entity entity) {
    position.set(entity.getPos()).sub(offset);
    pa.translate(-position.x, -position.y);
  }
  
}