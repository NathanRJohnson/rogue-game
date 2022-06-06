package generation;

import entities.Player;
import processing.core.PApplet;
import processing.core.PVector;
import tools.Constants;
import tools.Direction;

public class Door {
    private PVector pos;
    private float w, h;
    private Room nextRoom;
    private Direction direction;

    Door(Direction _direction, Room _nextRoom) {
        pos = new PVector();
        nextRoom = _nextRoom;
        direction = _direction;
        //north south (0, 2)
        if (direction.getNumericalValue() % 2 == 0){
            w = 150;
            h = 25;
            pos.x = Constants.WIDTH/2;
            int m = direction.getNumericalValue()/2;
            pos.y = m * Constants.HEIGHT; // - m * h
        } else { //east west (3, 1)
            w = 25;
            h = 150;
            int m = (direction.getNumericalValue() - 1)/2;
            pos.y = Constants.HEIGHT/2;
            pos.x = m * Constants.WIDTH; // - m * w
        }
    }

    public Room getNextRoom(){
        return nextRoom;
    }

    public Direction getDirection() {
        return direction;
    }

    void display(PApplet pa) {

        pa.fill(204,	136, 153); // #CC8899
        pa.rectMode(PApplet.CENTER);
        pa.rect(pos.x, pos.y, w, h);
    }

    public boolean isEntered(Player p) {
        PVector dist = PVector.sub(p.getPos(), pos);
        if (PApplet.abs(dist.x)<= w/2 && PApplet.abs(dist.y)<= h/2) {
            return true;
        }
        return false;
    }
}
