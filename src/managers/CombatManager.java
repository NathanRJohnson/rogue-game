package managers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import entities.Player;
import entities.Zombie;
import projectiles.Projectile;
import tools.Constants;

public class CombatManager {
  Player player;
  Horde horde;
  // tracks which projectiles have hit which zombies
  HashMap<Projectile, LinkedList<Zombie>> collisionHistoryMap;
  CollisionHandler collisionHandler;
  

  public CombatManager(Player player, Horde horde) {
    this.player = player;
    this.horde = horde;
    this.collisionHistoryMap = new HashMap<>();
    collisionHandler = new CollisionHandler();
  }

  public void runCombat() {
    for (Zombie z : horde.getZombies()) {
      runProjectileZombieCollisions(z);
      runZombiePlayerCollisions(z);
    }
    
    flushCollisionHistoryMap();
  }

  private void runProjectileZombieCollisions(Zombie z) {
    for (Projectile p : player.getLauncher().getProjectiles()) {
      collisionHandler.check(p, z);
      if (collisionHandler.detectsCollision()) {
        z.receiveDamage(collisionHandler.getDamageDone());
        p.reduceDamageBy(15); // bullet loses impact as it travels through enemies
        collisionHistoryMap.putIfAbsent(p, new LinkedList<>());
        collisionHistoryMap.get(p).add(z);
      }
    }
  }

  private void runZombiePlayerCollisions(Zombie z) {
    if (z.inRange(player.getPos()) && !z.isAttacking()) {
        z.initateAttack();
      }
    }

  // Removes dead projectiles from the history
  private void flushCollisionHistoryMap(){
    Iterator<Projectile> iter = collisionHistoryMap.keySet().iterator();
    while (iter.hasNext()) {
      Projectile p = (Projectile) iter.next();
      if (p.isDead()) {
        iter.remove();
      }
    }
  }

  private class CollisionHandler {
    private double damage;

    public CollisionHandler() {
      damage = 0;
    }

    public void check(Projectile p, Zombie z) {
      damage = 0;
      
      if (collisionHistoryMap.containsKey(p) && collisionHistoryMap.get(p).contains(z)) {
        return;
      }

      if (z.isHitHead(p)) {
        damage = p.getDamage() * Constants.HEADSHOT_MODIFIER;
      } else if (z.isHitBody(p)) {
        damage = p.getDamage() * Constants.HEADSHOT_MODIFIER;
      }
    }

    public double getDamageDone() {
      return damage;
    }

    public boolean detectsCollision() {
      return damage > 0;
    }
  }

}
