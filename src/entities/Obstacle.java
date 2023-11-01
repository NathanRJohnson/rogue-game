package entities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ArrayList;

import processing.core.PApplet;
import processing.core.PVector;
import processing.core.PShape;

public class Obstacle {
  private ArrayList<Wall> walls;
  private Iterator<Wall> wallIterator;
  private PShape shape;
  private Builder builder;

  public Obstacle(Builder builder) {
    this.builder = builder;
    walls = new ArrayList<>(builder.verticies.size() - 1);
    makeWalls(builder);
  }

  private void makeWalls(Builder builder){
    Iterator<PVector> it = builder.verticies.iterator();
    PVector firstVertex = it.next();
    PVector lastVertex = firstVertex;
    PVector currentVertex;

    while (it.hasNext()){
      currentVertex = it.next();
      walls.add(new Wall(lastVertex, currentVertex));
      lastVertex = currentVertex;
    }
    walls.add(new Wall(lastVertex, firstVertex));
  }

  public void run(PApplet pa){
    wallIterator = walls.iterator();
    while (wallIterator.hasNext()){
      Wall w = wallIterator.next();
      // w.run(player);
      w.display(pa);
    }
    pa.shape(shape);
  }

  public void loadShape(PApplet pa) {
    shape = pa.createShape();
    shape.beginShape();
    for (PVector vertex : builder.verticies) {
      shape.vertex(vertex.x, vertex.y);
    }
    shape.endShape(PApplet.CLOSE);
    shape.setFill(pa.color(50, 0, 20));
  }

  public Iterator<Wall> getIterator() {
    return walls.iterator();
  }
  
  public static Builder newBuilder(){
    return new Builder();
  }

  public static class Builder {
    private LinkedList<PVector> verticies = new LinkedList<>();

    // Hide constructor
    private Builder(){}

    public Builder addVertex(PVector vertex){
      verticies.add(vertex);
      return this;
    }

    public Obstacle build(){
      if (verticies.size() < 2){
        throw new IllegalCallerException("Must add atleast 2 vertices to obstacle");
      }
      return new Obstacle(this);
    }
  }
}