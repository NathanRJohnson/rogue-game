import entities.Player;
import generation.Door;
import generation.Map;
import generation.Room;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Clock;
import tools.Constants;
import tools.Direction;

public class Game extends PApplet{

    Player p;
    Room r;
    Clock clock;
    Map m;

    public void settings(){
        size(Constants.WIDTH,Constants.HEIGHT);
        p = new Player();
        clock = new Clock();

        m = new Map(p, clock);
        m.buildMapGraph(this);
        r = m.getStartRoom();
//        r.initRoom(this);
    }

    public void draw(){
        r.run(this);
        p.run(this);
        r.applyBoundaries(p);

        for (Door d : r.getDoorsMap().values()) {
            if (!d.isLocked() && d.isEntered(p)) {
                Direction card_dir = d.getDirection();
                p.setPosByCompass(card_dir);
                r = d.getNextRoom();
                r.initRoom(this);
            }
        }
        clock.run();
    }

    public void keyPressed(){
        p.press(key);
    }

    public void keyReleased(){
        p.release(key);
    }

    public void mousePressed() {
        p.fire(new PVector(mouseX, mouseY));
    }

    public static void main(String[] args) {
        System.out.println("Hello World");
        String[] processingArgs = {"Game"};
        Game game = new Game();
        PApplet.runSketch(processingArgs, game);
    }
}
