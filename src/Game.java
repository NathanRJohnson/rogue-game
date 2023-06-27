import entities.Player;
import entities.Obstacle;
import generation.Room;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;
import tools.Constants;

public class Game extends PApplet{

  Player player;
  Room r;
  Clock clock;
  PApplet gameWindow;
  Obstacle obstacle;
  int gameState;

  public void settings(){
    size(Constants.WIDTH,Constants.HEIGHT);
    player = new Player();
    clock = new Clock();
    obstacle = Obstacle.newBuilder()
        .addVertex(new PVector(500, 200))
        .addVertex(new PVector(300, 100))
        .addVertex(new PVector(300, 400))
        .addVertex(new PVector(400, 500))
        .build();
    // BA = new Wall( new PVector(500, 200), new PVector(300, 100)); // top
    // CB = new Wall(new PVector(400, 500), new PVector(500, 200)); // right
    // DC = new Wall(new PVector(300, 400), new PVector(400, 500)); // bottom
    // AD = new Wall(new PVector(300, 100), new PVector(300, 400)); // left
    r = new Room(player, clock);
    gameState = Constants.GAMESTATE_PLAY;
  }

  public void draw() {
    gameWindow = this;
    r.run(gameWindow);
    obstacle.run(gameWindow, player);
    player.run(gameWindow);
    r.applyBoundaries(player);
    
  }

  public void keyPressed(){
    player.press(key, keyCode);
  }

  public void keyReleased(){
    player.release(key, keyCode);
  }

  public void mousePressed() {
    player.fire(new PVector(mouseX, mouseY));
  }

  public static void main(String[] args) {
    String[] processingArgs = {"Game"};
    Game game = new Game();
    PApplet.runSketch(processingArgs, game);
  }
}
