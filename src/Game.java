import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import entities.Obstacle;
import entities.Path;
import entities.Player;
import entities.Zombie;
import managers.Horde;
import managers.VisionManager;
import tools.Camera;
import tools.Clock;
import tools.Mouse;
import tools.Constants;


public class Game extends PApplet{
  Clock clock;
  Camera camera;
  PApplet gameWindow;
  Horde horde;
  Path path, pathB;
  ArrayList<Path> paths;
  Mouse mouse;
  Obstacle obstacle;
  Player player;
  VisionManager visionManager;


  public void settings() {
    size(Constants.WIDTH, Constants.HEIGHT);
    clock = Clock.getInstance();
    camera = new Camera();
    mouse = Mouse.getInstance();
    path = new Path();
    pathB = new Path();
    paths = new ArrayList<>();
    horde = new Horde();
    player = new Player();
  }

  public void setup() {
    gameWindow = this;
    obstacle = Obstacle.newBuilder()
              .addVertex(new PVector(0, 400))
              .addVertex(new PVector(Constants.WIDTH - 200, 400))
              .addVertex(new PVector(Constants.WIDTH - 200, 500))
              .addVertex(new PVector(0, 500))
              .build();
    obstacle.loadShape(gameWindow);
    path.addPoint(0, Constants.HEIGHT/2 - 30);
    path.addPoint(Constants.WIDTH, Constants.HEIGHT/2 - 30);
    pathB.addPoint(Constants.WIDTH, Constants.HEIGHT/2 - 300);
    pathB.addPoint(Constants.WIDTH, Constants.HEIGHT/2 + 200);
    paths.add(path);
    paths.add(pathB);
    visionManager = new VisionManager(obstacle);
  }

  public void draw() {
    clock.run();
    background(200, 200, 200);
    camera.follow(gameWindow, player);
    mouse.setPositionRelativeTo(gameWindow, player.getPos());
    player.run(gameWindow);
    horde.run(gameWindow, visionManager, player, paths);
    path.display(gameWindow);
    pathB.display(gameWindow);
    obstacle.run(gameWindow);
    visionManager.run(gameWindow);
  }

  public void keyPressed() {
    if (key == 'm') {
      Zombie z = new Zombie();
      horde.addZombie(z);
      return;
    }
    if (key == 'n') {
      path.clear();
      path.addPoint(0, Constants.HEIGHT/2);
      path.addPoint(random(100, 300), random(100, 600));
      path.addPoint(random(400, 700), random(100, 600));
      path.addPoint(random(800, 1000), random(100, 600));
      path.addPoint(Constants.WIDTH, Constants.HEIGHT/2);
    }
    player.press(key, keyCode);
  }

  public void keyReleased() {
    player.release(key, keyCode);
  }

  public void mousePressed() {
    player.fire(mouse.getPosition(), obstacle);
  }

  public static void main(String[] args) {
    String[] processingArgs = {"Game"};
    Game game = new Game();
    PApplet.runSketch(processingArgs, game);
  }
}
