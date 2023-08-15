package managers;

import java.util.ArrayList;
import java.util.LinkedList;

import entities.Player;
import entities.Spawner;
import entities.Zombie;
import processing.core.PApplet;
import processing.core.PVector;

public class SpawnerManager {
  ArrayList<Spawner> allSpawners;
  LinkedList<Spawner> activeSpawners;

  public SpawnerManager() {
    allSpawners = new ArrayList<>();
    activeSpawners = new LinkedList<>();
  }

  // TODO: for debugging. will be removed at a later point. 
  public void run(PApplet pApplet, Player player) {
    for (Spawner spawner : allSpawners) {
      spawner.display(pApplet);
      pApplet.ellipse(spawner.getPos().x, spawner.getPos().y, 350, 350);
    }
  }

  public void spawn(Zombie zombie) {
    for (Spawner spawner : activeSpawners) {
      if (spawner.isAvailable()) {
        spawner.spawn(zombie);
        spawner.startCooldown();
        // cycle the spawner to the back of the queue
        activeSpawners.add(activeSpawners.pop());
        return;
      }
    }
  }

  public void updateActiveSpawnersByProximityTo(Player player) {
    for (Spawner spawner : allSpawners) {
      if (playerInRange(player, spawner)) {
        if (!activeSpawners.contains(spawner)) {
          activeSpawners.add(spawner);
        }
      } else {
        activeSpawners.remove(spawner);
      }
    }
  }

  public SpawnerManager AddSpawner(Spawner spawner) {
    allSpawners.add(spawner);
    return this;
  }

  private boolean playerInRange(Player player, Spawner spawner) {
    return PVector.dist(player.getPos(), spawner.getPos()) < 175;
  }

  
}
