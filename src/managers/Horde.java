package managers;

import java.util.Iterator;
import java.util.LinkedList;

import entities.Zombie;
import entities.Player;
import processing.core.PApplet;
import tools.Clock;

public class Horde {
  
  private LinkedList<Zombie> zombies;
  private Clock clock;

  public Horde() {
    zombies = new LinkedList<>();
    clock = Clock.getInstance();
  }

  public void run(PApplet pa, Player player) {
    clock.run();
    Iterator<Zombie> zIterator = zombies.iterator();
    while (zIterator.hasNext()) {
      Zombie z = zIterator.next();
      z.run(pa, player, zombies, clock);

      if (z.isDead()) {
        zIterator.remove();
      }
    }
  }

  public void addZombie(Zombie zombie) {
    zombies.add(zombie);
  }

  public LinkedList<Zombie> getZombies() {
    return zombies;
  }

}
