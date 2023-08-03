import entities.Player;
import entities.Obstacle;
import generation.Room;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;
import tools.Constants;
import java.util.Iterator;
import entities.Wall;

public class Game extends PApplet{

  Player player;
  Room r;
  Clock clock;
  PApplet gameWindow;
  Obstacle obstacle;
  int gameState;
  Iterator<Wall> wallIterator;

  public void settings() {
    size(Constants.WIDTH,Constants.HEIGHT);
    player = new Player();
    clock = new Clock();
    r = new Room(player, clock);
    gameState = Constants.GAMESTATE_PLAY;
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
    r.run(gameWindow);
    player.run(gameWindow);
    r.applyBoundaries(player);
    obstacle.run(gameWindow, player);
  }

  public void keyPressed(){
    player.press(key, keyCode);
  }

  public void keyReleased(){
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
