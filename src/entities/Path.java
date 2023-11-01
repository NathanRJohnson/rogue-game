package entities;

import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;

public class Path {
 
  private ArrayList<PVector> points;
 
  // A path has a radius, i.e. how wide it is.
  private float radius;
 
  public Path() {
    points = new ArrayList<>();
    radius = 20;
  }

  public void addPoint(float x, float y) {
    addPoint(new PVector(x, y));
  }

  public void addPoint(PVector point) {
    points.add(point);
  }

  public int numPoints() {
    return points.size();
  }

  public PVector getPoint(int index) {
    return points.get(index);
  }

  public float getRadius() {
    return radius;
  }

  public void clear() {
    points.clear();
  }
 
  public void display(PApplet pa) {  // Display the path.
    // pa.strokeWeight(radius*2);
    // pa.stroke(0,100);
    pa.noFill();
    pa.beginShape();
    for (PVector v : points) {
      pa.vertex(v.x, v.y);
    }
    pa.endShape();
  }
}