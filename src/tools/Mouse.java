package tools;

import processing.core.PApplet;
import processing.core.PVector;

public class Mouse {
  private static Mouse instance;
  private PVector position;

  private Mouse() {
    position = new PVector();
  }

  public static Mouse getInstance() {
    if (instance == null) {
      instance = new Mouse();
    }
    return instance;
  }

  public void setPositionRelativeTo(PApplet pa, PVector origin) {
    position.set(pa.mouseX, pa.mouseY).add(origin);
  }

  public PVector getPosition() {
    return position.copy();
  }
}