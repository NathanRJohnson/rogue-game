package entities;

import processing.core.PApplet;
import processing.core.PVector;
import tools.CoolDown;

public class Spawner {
  private PVector position;
  private float spawnRadius;
  private CoolDown coolDown;

  public Spawner(PVector position, float radius, double coolDown) {
    this.position = position.copy();
    this.spawnRadius = radius;
    this.coolDown = new CoolDown(coolDown);
  }

  public void spawn(Zombie z) {
    PVector spawnPoint = getSpawnPosition();
    z.setPos(spawnPoint);
  }

  private PVector getSpawnPosition() {
    PApplet pa = new PApplet();
    return new PVector(position.x + pa.random(-spawnRadius, spawnRadius), position.y + pa.random(-spawnRadius, spawnRadius));
  }

  public void startCooldown() {
    coolDown.start();
  }

  public boolean isAvailable() {
    return !coolDown.isCoolingDown();
  }

  public PVector getPos() {
    return position.copy();
  }

  public void display(PApplet pa) {
    pa.fill(0);
    pa.ellipse(position.x, position.y, 5, 5);
    pa.noFill();
    pa.stroke(1);
    pa.ellipse(position.x, position.y, spawnRadius, spawnRadius);
  }
}
