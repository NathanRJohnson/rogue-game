import entities.Player;
import entities.Spawner;
import entities.Zombie;
import entities.Obstacle;
import processing.core.PApplet;
import processing.core.PVector;
import managers.CombatManager;
import tools.Clock;
import tools.Constants;
import java.util.Iterator;
import managers.Horde;
import managers.SpawnerManager;
import entities.Wall;

public class Game extends PApplet{
  Player player;
  Clock clock;
  PApplet gameWindow;
  Obstacle obstacle;
  int gameState;
  Iterator<Wall> wallIterator;
  Horde horde;
  CombatManager cm;
  // Spawner spawner;
  SpawnerManager sm;
  
  public void settings() {
    size(Constants.WIDTH,Constants.HEIGHT);
    player = new Player();
    clock = Clock.getInstance();
    gameState = Constants.GAMESTATE_PLAY;
    horde = new Horde();
    cm = new CombatManager(player, horde);
    sm = new SpawnerManager();
    sm.AddSpawner(new Spawner(new PVector(700, 200), 50, 0))
      .AddSpawner(new Spawner(new PVector(900, 400), 50, 0))
      .AddSpawner(new Spawner(new PVector(700, 500), 50, 0));
  }

  public void setup() {
    obstacle = Obstacle.newBuilder()
        .addVertex(new PVector(500, 200))
        .addVertex(new PVector(300, 100))
        .addVertex(new PVector(300, 400))
        .addVertex(new PVector(400, 500))
        .build();
    obstacle.loadShape(this);
  }

  public void draw() {
    gameWindow = this;
    background(200, 200, 200);
    player.run(gameWindow);
    horde.run(gameWindow, player);
    obstacle.run(gameWindow, player);
    cm.runCombat();
    // spawner.display(gameWindow);
    sm.run(gameWindow, player);
  }

  public void keyPressed() {
    if (key == 'm') {
      Zombie z = new Zombie();
      sm.updateActiveSpawnersByProximityTo(player);
      sm.spawn(z);
      horde.addZombie(z);
      return;
    }
    player.press(key, keyCode);
  }

  public void keyReleased() {
    player.release(key, keyCode);
  }

  public void mousePressed() {
    player.fire(new PVector(mouseX, mouseY), obstacle);
  }

  public static void main(String[] args) {
    String[] processingArgs = {"Game"};
    Game game = new Game();
    PApplet.runSketch(processingArgs, game);
  }
}
