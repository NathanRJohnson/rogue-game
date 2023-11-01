package managers;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import entities.Path;
import entities.Player;
// import entities.Player;
import entities.Zombie;

import tools.Clock;

public class Horde {
  
  private LinkedList<Zombie> zombies;
  private Clock clock;

  public Horde() {
    zombies = new LinkedList<>();
    clock = Clock.getInstance();
  }

  public void run(PApplet pa, VisionManager visionManager, Player player, Path path) {
    Iterator<Zombie> zIterator = zombies.iterator();
    while (zIterator.hasNext()) {
      Zombie z = zIterator.next();
      z.run(pa, zombies, clock);

      if (visionManager.lineOfSightExistsBetween(z, player)) {
        z.hunt(player.getPos());
        System.out.println("HUNT");
      } else {
        z.follow(path);
        System.out.println("PATH");
      }

      if (z.isDead()) {
        zIterator.remove();
      }
    }
  }

  public void run(PApplet pa, VisionManager visionManager, Player player, ArrayList<Path> paths) {
    Iterator<Zombie> zIterator = zombies.iterator();
    while (zIterator.hasNext()) {
      Zombie z = zIterator.next();
      z.run(pa, zombies, clock);

      if (visionManager.lineOfSightExistsBetween(z, player)) {
        z.hunt(player.getPos());
        System.out.println("HUNT");
      } else {
        z.follow(paths);
        System.out.println("PATH");
      }

      if (z.isDead()) {
        zIterator.remove();
      }
    }
  }

  public void hunt(PVector target) {
    for (Zombie z : zombies) {
      z.hunt(target);
    }
  }

  public void follow(Path p){
    for (Zombie z : zombies) {
      z.follow(p);
    }
  }

  public void addZombie(Zombie zombie) {
    zombies.add(zombie);
  }

  public LinkedList<Zombie> getZombies() {
    return zombies;
  }

}
